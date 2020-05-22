import com.google.common.collect.Table;
import helpers.Constants;
import model.Entity;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.FloydWarshallShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.io.CSVExporter;
import org.jgrapht.io.CSVFormat;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

import java.io.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import static helpers.Constants.*;
import static helpers.Constants.ontology;


public class Interest_launcher {
    public static void main(String[] args) throws Exception {
        initiate();

            Locale.setDefault(Locale.ENGLISH);
            Server server = new Server(PORT);
            WebAppContext context = new WebAppContext("Viewer_Interests_Evaluation_Service/src/main/webapp", "/");

            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                // Fix for Windows, so Jetty doesn't lock files
                context.getInitParams().put("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false");
            }

            server.setHandler(context);
            server.start();
    }

        public static void initiate() {
            try(InputStream inputStream = new FileInputStream("Viewer_Interests_Evaluation_Service/src/main/resources/config.properties")) {
                Properties properties = new Properties();
                properties.load(inputStream);
                PORT = Integer.parseInt(properties.getProperty("port_number"));
                toParentWeight = Double.parseDouble(properties.getProperty("toParentWeight"));
                toChildWeight = Double.parseDouble(properties.getProperty("toChildWeight"));
                toAssociatedWeight = Double.parseDouble(properties.getProperty("toAssociatedWeight"));
                //System.out.println("initiated");
                ontology = upload_Ontology();

                graph = new FloydWarshallShortestPaths<>(create_graph(ontology));
                interests = initialize_interests();
                print_distances();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (OWLOntologyCreationException e) {
                e.printStackTrace();
            }
        }


    public static HashMap<String, Entity> upload_Ontology() throws OWLOntologyCreationException {
            OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
            File ontology_file = new File("Viewer_Interests_Evaluation_Service/src/main/resources/Files/Interests_v0.3.owl");
            OWLOntology owlOntology = ontologyManager.loadOntologyFromOntologyDocument(ontology_file);
            IRI iri = owlOntology.getOntologyID().getOntologyIRI().get();
            HashMap<String, Entity> ontology=  new HashMap<>();
            Entity Thing = new Entity("Thing");
            ontology.put("Thing", Thing);
            for(OWLClass Class: owlOntology.getClassesInSignature()) {
                Entity entity = new Entity(Class.getIRI().getShortForm());
                ontology.put(Class.getIRI().getShortForm(), entity);
            }
            for(OWLClass Class: owlOntology.getClassesInSignature()) {
                Entity entity = ontology.get(Class.getIRI().getShortForm());
                if(owlOntology.getSubClassAxiomsForSubClass(Class).isEmpty())
                    entity.addParent(Thing);
                else
                    for(OWLSubClassOfAxiom parent: owlOntology.getSubClassAxiomsForSubClass(Class))
                        entity.addParent(ontology.get(parent.getSuperClass().asOWLClass().getIRI().getShortForm()));

                //System.out.println(entity.asOWLClass().getIRI().getShortForm() );
            }
            for(OWLObjectProperty property: owlOntology.getObjectPropertiesInSignature()){
                for( OWLObjectPropertyDomainAxiom domain: owlOntology.getObjectPropertyDomainAxioms(property)) {
                    Entity entity = null;
                    for(OWLClassExpression owlClass: domain.getDomain().getClassesInSignature()) {
                        //if(owlClass instanceof OWLClass)
                        entity = ontology.get(owlClass.asOWLClass().getIRI().getShortForm());
                    }
                    //else continue;

                    for (OWLObjectPropertyRangeAxiom range : owlOntology.getObjectPropertyRangeAxioms(property)) {
                        for(OWLClassExpression owlClass: range.getRange().getClassesInSignature())
                        //if(owlClass instanceof OWLClass)
                        {
                            Entity toEntity = ontology.get(owlClass.asOWLClass().getIRI().getShortForm());
                            if (entity==null || entity.getAssociated().contains(toEntity)) break;
                            entity.addAssociated(toEntity);
                            entity = ontology.get(owlClass.asOWLClass().getIRI().getShortForm());
                        }
                        //else continue;

                    }
                }
            }
            //System.out.println(property);
            //for(Object c:  property.components().collect(Collectors.toList()))
            //if(c instanceof OWLClass) System.out.println(((OWLClass) c).asOWLClass().getIRI().getShortForm());

            return ontology;
        }

        public static Graph<Entity, DefaultWeightedEdge> create_graph(HashMap<String,Entity> ontology){
            Graph<Entity, DefaultWeightedEdge> graph = new SimpleDirectedWeightedGraph(DefaultWeightedEdge.class);
            for(Entity entity: ontology.values()) graph.addVertex(entity);
            for(Entity entity:ontology.values()){
                for(Entity parent: entity.getParents())
                    graph.setEdgeWeight(graph.addEdge(entity, parent), Constants.toParentWeight);
                for(Entity child: entity.getChildren())
                    graph.setEdgeWeight(graph.addEdge(entity, child), Constants.toChildWeight);
                for(Entity associated : entity.getAssociated())
                    graph.setEdgeWeight(graph.addEdge(entity, associated), Constants.toAssociatedWeight);
            }
            return graph;

        }

        public static Map<Integer, Map<String, Double>> initialize_interests() throws IOException {
            Map<Integer, Map<String, Double>> interests = new HashMap<>();
            FileInputStream fileInputStream = new FileInputStream(new File("Viewer_Interests_Evaluation_Service/src/main/resources/Files/Interests.xlsx"));
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet interests_sheet = workbook.getSheetAt(0);
            for(Row row: interests_sheet){

                if(row.getRowNum() == 0)continue;
                Map<String, Double> group_interest = null;
                for(Cell cell:row) {
                    if (cell.getColumnIndex() == 0) {
                        if (!interests.containsKey(cell.getNumericCellValue())) {
                            group_interest = new HashMap<>();
                            interests.put((int) cell.getNumericCellValue(), group_interest);
                        }
                    }
                    else
                        group_interest.put(interests_sheet.getRow(0).getCell(cell.getColumnIndex()).getStringCellValue(), cell.getNumericCellValue());
                }
            }
            return interests;
        }

        public static void print_distances(){
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Distances");
        Row row = sheet.createRow(0);
        int cells=1;
        for(String subject: ontology.keySet() ) {
            row.createCell(cells).setCellValue(subject);
            cells++;
        }
        int row_number = 1;
        for(String subject:ontology.keySet()){
            row = sheet.createRow(row_number);
            row_number++;
            row.createCell(0).setCellValue(subject);
            cells=1;
            for(String target: ontology.keySet()){
                row.createCell(cells).setCellValue(graph.getPathWeight(ontology.get(subject), ontology.get(target)));
                cells++;
            }
        }
            try (FileOutputStream outputStream = new FileOutputStream("distances.xlsx")) {
                workbook.write(outputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

}

