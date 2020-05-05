import helpers.Constants;
import model.Advertisement;
import org.apache.poi.ss.usermodel.Cell;
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



public class Group_Interests_launcher {
    public static void main(String[] args) throws Exception {
        try(InputStream inputStream = new FileInputStream("Group_Interests_Identification/src/main/resources/config.properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);
            int port = Integer.parseInt(properties.getProperty("port_number"));
            Constants.VIEWER_INTERESTS_EVALUATION_SERVICE = properties.getProperty("Viewer_Interests_Evaluation_Service");

            Locale.setDefault(Locale.ENGLISH);
            Server server = new Server(port);
            WebAppContext context = new WebAppContext("Group_Interests_Identification/src/main/webapp", "/");
            Constants.Advertisements = upload_advertisements();

            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                // Fix for Windows, so Jetty doesn't lock files
                context.getInitParams().put("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false");
            }

            server.setHandler(context);
            server.start();

            for(Map<String, Double> advertisement : Constants.Advertisements.values()) {
                for (String key : advertisement.keySet())
                    System.out.println(key+": "+advertisement.get(key));
                System.out.println("___________________-");
            }
        }

}
public static Map<Integer, Map<String, Double>> upload_advertisements() throws IOException {
        Map<Integer, Map<String, Double>> Advertisement = new HashMap<>();
    FileInputStream fileInputStream = new FileInputStream(new File("Group_Interests_Identification/src/main/resources/files/Advertisemnets.xlsx"));
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
    XSSFSheet Advertisements_sheet = workbook.getSheetAt(0);
    for(Row row: Advertisements_sheet){
        if(row.getRowNum()==0) continue;
        Map<String, Double> advertisement_subjects =null;
        for(Cell cell:row) {
            if (cell.getColumnIndex() == 0) {
                if (!Advertisement.containsKey(cell.getNumericCellValue())) {
                    advertisement_subjects = new HashMap<>();
                    Advertisement.put((int) cell.getNumericCellValue(), advertisement_subjects);
                }
            }
            else
                advertisement_subjects.put(Advertisements_sheet.getRow(0).getCell(cell.getColumnIndex()).getStringCellValue(), cell.getNumericCellValue());
        }


    }
    return Advertisement;
}
}
