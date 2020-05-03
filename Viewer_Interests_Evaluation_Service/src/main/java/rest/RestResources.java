package rest;



import model.Adverisement;
import model.Entity;

import javax.ws.rs.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static helpers.Constants.*;
import static helpers.Constants.ontology;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Path("system")
//@WebService
public class RestResources {

    @POST
    @Path("get_interests")
    public Map<String, Double> get_interests(Adverisement adverisement) {
        System.out.println("REquest");
        int demographical_group = adverisement.getDemographical_group();
        Map<String, Double> interest_values = new HashMap<>();
        for(String subject: adverisement.getSubjects()){
            double interest = calculate_interest(demographical_group, subject);
            interest_values.put(subject, interest);
            System.out.println(subject+": "+interest);
        }
        return interest_values;
    }

    public double calculate_interest(int demographical_group, String subject){
        if(interests.get(demographical_group).containsKey(subject))
            return interests.get(demographical_group).get(subject).doubleValue();
        Map<String, Double> group_interests = interests.get(demographical_group);
        ArrayList<String> closest_known_value = new ArrayList<>();
        double min_dictance = Double.POSITIVE_INFINITY;
        for(String known_interests: group_interests.keySet()){
            Entity entity = ontology.get(known_interests);
            double distance = graph.getPathWeight(ontology.get(subject), entity);
            if(distance<min_dictance){
                closest_known_value.clear();
                closest_known_value.add(known_interests);
                min_dictance = distance;
            }
            else if (min_dictance==distance) closest_known_value.add(known_interests);
        }
        if(closest_known_value.size()==1) return (group_interests.get(closest_known_value.get(0))/(1+min_dictance));
        else if (closest_known_value.size()>1){
            double max_interest = 0.0;
            for(String close_value: closest_known_value) {
                double candidate = group_interests.get(close_value) / (1 + min_dictance);
                if(candidate>max_interest)max_interest=candidate;
            }
            return max_interest;
        }
        return 0.0;
    }

    @GET
    public String demo(){
        System.out.println("Demo!");
        return "Demo";
    }

}
