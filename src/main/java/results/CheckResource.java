package results;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import oauth.AuthenticatedUserService;
import oauth.UserService;

import java.io.StringReader;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedList;

import static results.AreaResultChecker.checkArea;
import static results.AreaResultChecker.validateXYR;

@Path("/results")
public class CheckResource {
    private final UserService service = new UserService();
    private final AuthenticatedUserService authenticatedUserService = new AuthenticatedUserService();

    @POST
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTable(String jsonInput) {
        JsonObject jsonObject = Json.createReader(new StringReader(jsonInput)).readObject();

        String hash;

        try {
            hash = (jsonObject.getString("hash"));
        } catch (NumberFormatException | NullPointerException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        final long startExec = System.nanoTime();

        CheckAreaResults results = new CheckAreaResults(hash);

        LinkedList<CheckArea> checkAreas = results.getResults();

        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

        for (CheckArea checkArea : checkAreas) {
            JsonObjectBuilder resultJson = Json.createObjectBuilder()
                    .add("x", checkArea.getX())
                    .add("y", checkArea.getY())
                    .add("r", checkArea.getR())
                    .add("result", checkArea.isResult())
                    .add("calculationTime", checkArea.getExecTime())
                    .add("time", checkArea.getExecutedAt().toString());

            jsonArrayBuilder.add(resultJson);
        }

        return Response.ok().entity(jsonArrayBuilder.build().toString()).type(MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/calculate")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkArea(String jsonInput) {
        JsonObject jsonObject = Json.createReader(new StringReader(jsonInput)).readObject();

        double dx;
        double dy;
        double dr;
        String hash;

        try {
            dx = Double.parseDouble(jsonObject.getString("x"));
            dy = Double.parseDouble(jsonObject.getString("y"));
            dr = Double.parseDouble(jsonObject.getString("r"));
            hash = (jsonObject.getString("hash"));
        } catch (NumberFormatException | NullPointerException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if (!validateXYR(dx, dy, dr)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\": \"Некорректный запрос\"}").build();
        }

        final long startExec = System.nanoTime();

        CheckAreaResults results = new CheckAreaResults(hash);
        results.newResult(dx, dy, dr, hash);

        LinkedList<CheckArea> checkAreas = results.getResults();

        final long endExec = System.nanoTime();
        final long executionTime = (endExec - startExec);

        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

        for (CheckArea checkArea : checkAreas) {
            JsonObjectBuilder resultJson = Json.createObjectBuilder()
                    .add("x", checkArea.getX())
                    .add("y", checkArea.getY())
                    .add("r", checkArea.getR())
                    .add("result", checkArea.isResult())
                    .add("calculationTime", executionTime)
                    .add("time", checkArea.getExecutedAt().toString());

            jsonArrayBuilder.add(resultJson);
        }

        return Response.ok().entity(jsonArrayBuilder.build().toString()).type(MediaType.APPLICATION_JSON).build();
    }


    @POST
    @Path("/clear")
    @Produces(MediaType.APPLICATION_JSON)
    public Response clear(String jsonInput) throws SQLException {
        JsonObject jsonObject = Json.createReader(new StringReader(jsonInput)).readObject();

        String hash;

        try {
            hash = (jsonObject.getString("hash"));
        } catch (NumberFormatException | NullPointerException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        final long startExec = System.nanoTime();

        CheckAreaResults results = new CheckAreaResults(hash);
        results.clearResults();

        LinkedList<CheckArea> checkAreas = results.getResults();

        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

        for (CheckArea checkArea : checkAreas) {
            JsonObjectBuilder resultJson = Json.createObjectBuilder()
                    .add("x", checkArea.getX())
                    .add("y", checkArea.getY())
                    .add("r", checkArea.getR())
                    .add("result", checkArea.isResult())
                    .add("calculationTime", checkArea.getExecTime())
                    .add("time", checkArea.getExecutedAt().toString());

            jsonArrayBuilder.add(resultJson);
        }

        return Response.ok().entity(jsonArrayBuilder.build().toString()).type(MediaType.APPLICATION_JSON).build();
    }


    @OPTIONS
    @Path("{path : .*}")
    public Response options() {
        return Response.ok("")
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                .header("Access-Control-Max-Age", "1209600")
                .build();
    }
}