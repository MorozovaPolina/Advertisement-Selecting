package rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import helpers.Constants;
import model.Advertisement;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("system")
//@WebService
public class RestResources {
    @POST
    @Path("evaluate")
    public Map<Integer, Double> select_advertisement(ArrayList<Integer> viewers) {
        Map<Integer, Double> advertisements_evaluation = new HashMap<>();

        Set <String> topics = new HashSet();
        for (Map<String, Double> ad_topics : Constants.Advertisements.values())
            topics.addAll(ad_topics.keySet());
        //System.out.println(advertisement_id);
        for (int viewer : viewers) {
            Advertisement AD = new Advertisement(viewer, topics);
            Map<String, Double> viewer_interests = get_viewer_interests(AD);
            Advertisement.print_map(viewer_interests);
            for (int advertisement_id : Constants.Advertisements.keySet()) {
                if(!advertisements_evaluation.containsKey(advertisement_id))
                    advertisements_evaluation.put(advertisement_id, Advertisement.interest(viewer_interests, Constants.Advertisements.get(advertisement_id))/viewers.size());
                else advertisements_evaluation.put(advertisement_id, advertisements_evaluation.get(advertisement_id)+Advertisement.interest(viewer_interests, Constants.Advertisements.get(advertisement_id))/viewers.size());
            }
            Advertisement.print_ads(advertisements_evaluation);
        }
        return advertisements_evaluation;
    }


    @GET
    @Path("demo")
    public String demo() {
        System.out.println("Demo!");
        return "Demo";
    }

    public Map<String, Double> get_viewer_interests(Advertisement advertisement) {
        Map<String, Double> result = new HashMap<>();
        Client client = new Client();
        WebResource webResource = client.resource(Constants.VIEWER_INTERESTS_EVALUATION_SERVICE);
        ClientResponse clientResponse = webResource.accept(APPLICATION_JSON_TYPE).type(APPLICATION_JSON_TYPE).post(ClientResponse.class, advertisement.toJSON());
        if (clientResponse.getStatus() == 200) {
            try {
               result = new ObjectMapper().readValue(clientResponse.getEntity(String.class), HashMap.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            //logMessage("Got demographical status for a viewer "+ person.toJSON());
        }
        return result;
    }


}
