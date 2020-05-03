
import model.Entity;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import model.Entity;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import helpers.Constants;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.FloydWarshallShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

import static helpers.Constants.*;


public class Viewer_Interests_Evaluation_Launcher {
    public static void main(String[] args) throws Exception {
            initiate();
            Locale.setDefault(Locale.ENGLISH);
            Server server = new Server(PORT);
            WebAppContext context = new WebAppContext("Interests_Evaluation_Service/src/main/webapp", "/");

           if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                // Fix for Windows, so Jetty doesn't lock files
                context.getInitParams().put("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false");
            }
            server.setHandler(context);
            server.start();
            server.join();
            System.out.println("Server strated");
        }


    public static void initiate() {
        try (InputStream inputStream = new FileInputStream("Interests_Evaluation_Service/src/main/resources/config.properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);
            PORT = Integer.parseInt(properties.getProperty("port_number"));
            Constants.toParentWeight = Double.parseDouble(properties.getProperty("toParentWeight"));
            Constants.toChildWeight = Double.parseDouble(properties.getProperty("toChildWeight"));
            Constants.toAssociatedWeight = Double.parseDouble(properties.getProperty("toAssociatedWeight"));
            ontology = upload_Ontology();
            graph = new FloydWarshallShortestPaths<>(create_graph(ontology));
            ontology.get("Cars").printEntity();
            ontology.get("Sport").printEntity();
            System.out.println(graph.getPathWeight(ontology.get("Cars"), ontology.get("Sport")));
            interests = initialize_interests();
            System.out.println("Initiation end");

        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, Entity> upload_Ontology() throws OWLOntologyCreationException {
        OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
        File ontology_file = new File("Interests_Evaluation_Service/src/main/resources/Interests_v0.3.owl");
        OWLOntology owlOntology = ontologyManager.loadOntologyFromOntologyDocument(ontology_file);
        IRI iri = owlOntology.getOntologyID().getOntologyIRI().get();
        System.out.println(iri);
        HashMap<String, Entity> ontology=  new HashMap<>();
        Entity Thing = new Entity("Thing");
        ontology.put("Thing", Thing);
        for(OWLClass Class: owlOntology.getClassesInSignature()) {
            Entity entity = new Entity(Class.getIRI().getShortForm());
            ontology.put(Class.getIRI().getShortForm(), entity);
            entity.printEntity();
        }
        for(OWLClass Class: owlOntology.getClassesInSignature()) {
            Entity entity = ontology.get(Class.getIRI().getShortForm());
            if(owlOntology.getSubClassAxiomsForSubClass(Class).isEmpty())
                entity.addParent(Thing);
            else
                for(OWLSubClassOfAxiom parent: owlOntology.getSubClassAxiomsForSubClass(Class))
                    entity.addParent(ontology.get(parent.getSuperClass().asOWLClass().getIRI().getShortForm()));
            entity.printEntity();

            //System.out.println(entity.asOWLClass().getIRI().getShortForm() );
        }
        for(OWLObjectProperty property: owlOntology.getObjectPropertiesInSignature()){
            System.out.println(property.getIRI().getShortForm());
            for( OWLObjectPropertyDomainAxiom domain: owlOntology.getObjectPropertyDomainAxioms(property)) {
                System.out.println("Domain "+domain.getDomain().getClassesInSignature());
                Entity entity = null;
                for(OWLClassExpression owlClass: domain.getDomain().getClassesInSignature()) {
                    //if(owlClass instanceof OWLClass)
                    System.out.println(owlClass.asOWLClass().getIRI().getShortForm());
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
                entity.printEntity();
                System.out.println();
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
        FileInputStream fileInputStream = new FileInputStream(new File("Interests_Evaluation_Service/src/main/resources/Interests.xlsx"));
        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
        XSSFSheet interests_sheet = workbook.getSheetAt(0);
        for(Row row: interests_sheet){
            Map<String, Double> group_interest = null;
            if(row.getRowNum() == 0)continue;
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

}