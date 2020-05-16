import model.Emotions;
import model.Person;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import helpers.Constants;


public class Viewer_Describing_Launcher {
    public static void main(String[] args) throws Exception {
            initialize();
            Locale.setDefault(Locale.ENGLISH);
            Server server = new Server(Constants.PORT);
            WebAppContext context = new WebAppContext("Viewer_Describing_Service/src/main/webapp", "/");

            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                // Fix for Windows, so Jetty doesn't lock files
                context.getInitParams().put("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false");
            }

            server.setHandler(context);
            server.start();


}
public static void initialize(){
    try(InputStream inputStream = new FileInputStream("Viewer_Describing_Service/src/main/resources/config.properties")) {
        Properties properties = new Properties();
        properties.load(inputStream);
        Constants.PORT = Integer.parseInt(properties.getProperty("port_number"));
        Constants.centroids = initialize_clusters();
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }

}

public static Map<Integer, Person> initialize_clusters() throws IOException {
    Map<Integer, Person> clusters = new HashMap<>();
    FileInputStream fileInputStream = new FileInputStream(new File("Viewer_Describing_Service/src/main/resources/files/Centroids.xlsx"));
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
    XSSFSheet Clusters_sheet = workbook.getSheetAt(0);
    for(Row row: Clusters_sheet) {
        if (row.getRowNum() == 0) continue;
        Person cluster = new Person();
        cluster.setAge(row.getCell(1).getNumericCellValue());
        cluster.setGender(row.getCell(2).getStringCellValue());
        Emotions emotion = new Emotions();
        emotion.setAnger(row.getCell(3).getNumericCellValue());
        emotion.setContempt(row.getCell(4).getNumericCellValue());
        emotion.setDisgust(row.getCell(5).getNumericCellValue());
        emotion.setFear(row.getCell(6).getNumericCellValue());
        emotion.setHappiness(row.getCell(7).getNumericCellValue());
        emotion.setNeutral(row.getCell(8).getNumericCellValue());
        emotion.setSadness(row.getCell(9).getNumericCellValue());
        emotion.setSurprise(row.getCell(10).getNumericCellValue());
        cluster.setEmotion(emotion);
        clusters.put((int)row.getCell(0).getNumericCellValue(), cluster);
    }

    for(Integer key: clusters.keySet()){
        System.out.print(key+" ");
        clusters.get(key).print_person();
    }
    return clusters;


}
}
