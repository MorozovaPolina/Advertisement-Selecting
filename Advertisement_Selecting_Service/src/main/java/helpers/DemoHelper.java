package helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;

public class DemoHelper {
    public static Logger logger = LoggerFactory.getLogger("requestLogger");
    public static final ThreadLocal<SimpleDateFormat> DateTimeFormatter = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(Constants.DEMO_DATE_FORMAT);
        }
    };

    public static final void logMessage(String message) {
        logger.info(message);

    }
}
