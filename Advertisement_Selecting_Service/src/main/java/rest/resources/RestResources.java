package rest.resources;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import helpers.Constants;
import model.Person;
import org.codehaus.jackson.map.ObjectMapper;


import javax.servlet.annotation.WebServlet;
import javax.ws.rs.*;
import java.io.IOException;
import java.util.*;

import static javax.ws.rs.core.MediaType.*;
import static rest.Calculations.get_advertisement;

@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Path("system")
@WebServlet
public class RestResources {
    @POST
    @Path("select_advertisement")
    public int select_advertisement(Person[] persons) throws IOException {
        ArrayList<Integer> viewers = new ArrayList<>();
        for (Person person : persons) {
            int demografic_group = (int) demographic_group(person);
            person.setDemographic_group(demografic_group);
            viewers.add(demografic_group);
            person.print_person();
        }
        return get_advertisement(get_interests(viewers));
    }

    public static long demographic_group(Person person) {
        Client client = new Client();
        WebResource webResource = client.resource(Constants.Viewer_Describing_Service);
        ClientResponse clientResponse = webResource.accept(APPLICATION_JSON_TYPE).type(APPLICATION_JSON_TYPE).post(ClientResponse.class, person.toJSON());
        if (clientResponse.getStatus() == 200) {
            return Long.parseLong(clientResponse.getEntity(String.class));
        } else new Exception();
        return -1;
    }

    public static Map<String, Double> get_interests(ArrayList<Integer> vewers) throws IOException {
        Map<String, Double> result = new HashMap<>();
        Client client = new Client();
        WebResource webResource = client.resource(Constants.Interests_Evaluation_service);
        ClientResponse clientResponse = webResource.accept(APPLICATION_JSON_TYPE).type(APPLICATION_JSON_TYPE).post(ClientResponse.class,
                new ObjectMapper().writeValueAsString(vewers));
        if (clientResponse.getStatus() == 200) {
            result = new ObjectMapper().readValue(clientResponse.getEntity(String.class), HashMap.class);
        }
        return result;
    }

}
