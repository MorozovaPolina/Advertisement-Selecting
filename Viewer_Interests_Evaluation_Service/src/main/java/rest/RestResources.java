package rest;

import model.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Map;


@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("system")
public class RestResources {

    @POST
    @Path("get_interests")
    public Map<String, Double> get_interests(Adverisement adverisement) {
        return adverisement.calculate_interests();
    }



    @GET
    public String demo(){
        System.out.println("Demo!");
        return "Demo";
    }

    @GET
    @Path("retrain")
    public void retrain(){
        System.out.println("retrain");
    }

}
