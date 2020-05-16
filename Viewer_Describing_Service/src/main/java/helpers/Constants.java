package helpers;

import model.Person;

import java.util.Map;

public class Constants {
    public static final String DEMO_DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";
    public static String Viewer_Describing_Service;
    public static Map<Integer, Person> centroids;
    public static String CLUSTERS;
    public static int PORT;

    public static void print_keys(int key, double d){
        System.out.println(key+" "+d);
    }
}
