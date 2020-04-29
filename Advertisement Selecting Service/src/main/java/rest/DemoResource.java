package rest;

import helpers.DemoHelper;
import model.DemoInObject;
import model.DemoOutObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;

/*@Path("demo")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)*/
public class DemoResource {
   /* @POST
    public DemoOutObject handle(DemoInObject in) {
        return new DemoOutObject(in.getRequest_id(),
                in.getTransaction_type(),
                in.getTime(),
                in.getTarget(),
                in.getSource(),
                in.getOrder_number());
    }*/
}
