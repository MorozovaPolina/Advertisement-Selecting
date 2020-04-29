import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

import helpers.Constants;


public class Viewer_Describing_Launcher {
    public static void main(String[] args) throws Exception {
        try(InputStream inputStream = new FileInputStream("Viewer Describing Service/src/main/resources/config.properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);
            int port = Integer.parseInt(properties.getProperty("port_number"));

            Locale.setDefault(Locale.ENGLISH);
            Server server = new Server(port);
            WebAppContext context = new WebAppContext("Viewer Describing Service/src/main/webapp", "/");

            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                // Fix for Windows, so Jetty doesn't lock files
                context.getInitParams().put("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false");
            }

            server.setHandler(context);
            server.start();
        }

}
}
