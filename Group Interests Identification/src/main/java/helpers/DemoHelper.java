package helpers;

import java.text.SimpleDateFormat;

public class DemoHelper {
    public static final ThreadLocal<SimpleDateFormat> DateTimeFormatter = new ThreadLocal<SimpleDateFormat>(){
        @Override
        protected SimpleDateFormat initialValue()
        {
            return new SimpleDateFormat(Constants.DEMO_DATE_FORMAT);
        }
    };
}
