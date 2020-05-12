import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

import helpers.Constants;


public class Advertisement_Selection_launcher {
    public static void main(String[] args) throws Exception {
        initialize();
        Locale.setDefault(Locale.ENGLISH);
        Server server = new Server(Constants.PORT);
        WebAppContext context = new WebAppContext("Advertisement_Selecting_Service/src/main/webapp", "/");

        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            // Fix for Windows, so Jetty doesn't lock files
            context.getInitParams().put("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false");
        }

        server.setHandler(context);
        server.start();

    }

    public static void initialize() {
        try (InputStream inputStream = new FileInputStream("Advertisement_Selecting_Service/src/main/resources/config.properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);
            Constants.Viewer_Describing_Service = properties.getProperty("Viewer_Describing_Service");
            Constants.PORT = Integer.parseInt(properties.getProperty("port_number"));
            Constants.Interests_Evaluation_service = properties.getProperty("Interests_Evaluation_service");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
