package rest;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import helpers.Constants;
import model.Gender;
import model.Person;



import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("system")
//@WebService
public class RestResources {
    @POST
    @Path("get_demographic_group")
    public long select_advertisement(Person person) {
        person.print_person();
        long group_id = 2*person.getAge()/10;
        if(person.getGender()== Gender.male) group_id++;
        person.setDemographic_group(group_id);
        person.print_person();
        return group_id;
    }

    @GET
    public void demo(){
        System.out.println("Demo!");
    }

}
