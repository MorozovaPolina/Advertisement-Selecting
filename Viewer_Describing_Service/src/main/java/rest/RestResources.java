package rest;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import helpers.Constants;
import model.Gender;
import model.Person;



import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import static helpers.Constants.centroids;
import static helpers.Constants.print_keys;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("system")
//@WebService
public class RestResources {
    @POST
    @Path("get_demographic_group")
    public int get_demographic_group(Person person) {
        person.print_person();
        int closest_centroid = -1;
        double distance = Double.POSITIVE_INFINITY;
        for(int key: centroids.keySet()){
            double d = calculate_distance(person, centroids.get(key));
            if(d<distance){
                closest_centroid = key;
                distance = d;
            }
            print_keys(key, d);
        }

        person.setDemographic_group(closest_centroid);
        return closest_centroid;
    }

    public static double calculate_distance(Person person, Person centroid){
        double result =0;
        result+=Math.pow((person.getAge()-centroid.getAge()), 2);
        if(person.getGender()!=centroid.getGender()) result++;
        result+=Math.pow((person.getEmotion().getAnger()-centroid.getEmotion().getAnger()),2);
        result+=Math.pow((person.getEmotion().getContempt()-centroid.getEmotion().getContempt()),2);
        result+=Math.pow((person.getEmotion().getDisgust()-centroid.getEmotion().getDisgust()),2);
        result+=Math.pow((person.getEmotion().getFear()-centroid.getEmotion().getFear()),2);
        result+=Math.pow((person.getEmotion().getHappiness()-centroid.getEmotion().getHappiness()),2);
        result+=Math.pow((person.getEmotion().getNeutral()-centroid.getEmotion().getNeutral()),2);
        result+=Math.pow((person.getEmotion().getSadness()-centroid.getEmotion().getSadness()),2);
        result+=Math.pow((person.getEmotion().getSurprise()-centroid.getEmotion().getSurprise()),2);
        return Math.pow(result, 0.5);
    }

    @GET
    public void demo(){
        System.out.println("Demo!");
    }

}
