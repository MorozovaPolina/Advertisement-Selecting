package rest;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import helpers.Constants;
import model.Person;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

import static helpers.DemoHelper.logMessage;
import static javax.ws.rs.core.MediaType.*;

@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Path("system")
//@WebService
public class RestResources {
    @POST
    @Path("select_advertisement")
    public void select_advertisement(Person[] persons) {
        for(Person person : persons){
            person.print_person();
            demographic_group(person);
            person.print_person();

        }


    }

    @GET
    @Path("retrain")
    public void retrain(){
        System.out.println("retrain");
    }

    public void demographic_group(Person person){
        Client client =  new Client();
        WebResource webResource = client.resource(Constants.Viewer_Describing_Service);
        ClientResponse clientResponse = webResource.accept(APPLICATION_JSON_TYPE).type(APPLICATION_JSON_TYPE).post(ClientResponse.class, person.toJSON());
        if(clientResponse.getStatus() == 200) {

            person.setDemographic_group(Long.parseLong(clientResponse.getEntity(String.class)));
            //logMessage("Got demographical status for a viewer "+ person.toJSON());
        }
       // else logMessage("Demographical group identification failed with status"+clientResponse.getStatus());
    }

}
