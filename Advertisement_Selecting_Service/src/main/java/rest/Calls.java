package rest;

import model.Person;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import static java.util.Arrays.stream;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class Calls {
   /* public static void get_interests(Person[] persons){
        System.out.println(stream(persons).map(x->x.getDemographic_group()).toArray().toString());
    }*/

}
