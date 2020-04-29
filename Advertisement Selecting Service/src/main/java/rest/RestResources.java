package rest;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import helpers.Constants;
import model.DemoInObject;
import model.DemoOutObject;
import model.Person;


import javax.jws.WebService;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("system")
//@WebService
public class RestResources {
    @POST
    @Path("select_advertisement")
    public void select_advertisement(Person[] persons) {
        for(Person person : persons){
            person.print_person();
        }


    }

    @GET
    @Path("retrain")
    public void retrain(){
        System.out.println("retrain");
    }

    public Person demographic_group(Person person){
        Client client = new Client();
        WebResource webResource = client.resource(Constants.Viewer_Describing_Service);
        webResource.type("application/json").post(ClientResponse.class, person.toJSON());
        //person = webResource.entity(Person);
        return person;
    }



    @POST
    @Path("demo")
    public DemoOutObject demo(DemoInObject in) {
        return new DemoOutObject(in.getRequest_id(),
                in.getTransaction_type(),
                in.getTime(),
                in.getTarget(),
                in.getSource(),
                in.getOrder_number());
    }

}
