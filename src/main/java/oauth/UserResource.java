package oauth;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.StringReader;

@Path("/oauth")
public class UserResource {
    private final UserService service = new UserService();
    private final AuthenticatedUserService authenticatedUserService = new AuthenticatedUserService();

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(String jsonInput) {
        JsonObject jsonObject = Json.createReader(new StringReader(jsonInput)).readObject();
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        String hash = service.authenticateUser(username, password);
        authenticatedUserService.addUser(service.findUserByUsername(username).getId(), service.hashPassword(username));

        return Response.ok().entity("{\"hash\": \"" + hash + "\"}").type(MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(String jsonInput) {
        JsonObject jsonObject = Json.createReader(new StringReader(jsonInput)).readObject();
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        String name = jsonObject.getString("name");
        User newUser = service.registerUser(username, password, name);
        authenticatedUserService.addUser(newUser.getId(), service.hashPassword(username));

        return Response.ok().entity("{\"Name\": \"" + newUser.getName() + "\"}").type(MediaType.APPLICATION_JSON).build();
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