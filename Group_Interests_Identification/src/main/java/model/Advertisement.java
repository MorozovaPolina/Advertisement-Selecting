package model;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class Advertisement {
    public int demographical_group;
    Set <String> subjects;

    public Advertisement(int demographical_group, Set<String> subjects){
        this.demographical_group = demographical_group;
        this.subjects = subjects;
    }

    public static void print_string_map(Map<String, String> map) {
        for(String key:map.keySet()) System.out.println(key+" - interest = "+map.get(key));
    }

    public Set<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<String> subjects) {
        this.subjects = subjects;
    }

    public int getDemographical_group() {
        return demographical_group;
    }

    public void setDemographical_group(int demographical_group) {
        this.demographical_group = demographical_group;
    }

    public String toJSON()  {
        try {
            String result = new ObjectMapper().writeValueAsString(this);
            return result;
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    public static double interest(Map<String, Double> interests, Map<String, Double> subjects){
        double result = 0.0;
        for(String subject : subjects.keySet()){
            System.out.println(subject);
            System.out.println(interests.get(subject));
            result+=interests.get(subject)*subjects.get(subject);
        }
        return result;
    }

    public static void print_map(Map<String, Double> viewer_interests){
        for(String key:viewer_interests.keySet()) System.out.println(key+": interest = "+viewer_interests.get(key));
    }
    public static void print_ads(Map<Integer, Double> viewer_interests){
        for(int key:viewer_interests.keySet()) System.out.println(key+": interest = "+viewer_interests.get(key));
    }
}
