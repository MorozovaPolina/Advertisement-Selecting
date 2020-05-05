import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

import helpers.Constants;


public class Advertisement_Selection_launcher {
    public static void main(String[] args) throws Exception {
        try(InputStream inputStream = new FileInputStream("Advertisement_Selecting_Service/src/main/resources/config.properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);
            Constants.Viewer_Describing_Service = properties.getProperty("Viewer_Describing_Service");
            int port = Integer.parseInt(properties.getProperty("port_number"));
            Constants.Interests_Evaluation_service = properties.getProperty("Interests_Evaluation_service");

            Locale.setDefault(Locale.ENGLISH);
            Server server = new Server(port);
            WebAppContext context = new WebAppContext("Advertisement_Selecting_Service/src/main/webapp", "/");

            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                // Fix for Windows, so Jetty doesn't lock files
                context.getInitParams().put("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false");
            }

            server.setHandler(context);
            server.start();
        }

}
}
