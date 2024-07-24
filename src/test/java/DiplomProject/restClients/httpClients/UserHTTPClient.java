package DiplomProject.restClients.httpClients;

import DiplomProject.ApiUrls;
import io.restassured.response.Response;
import DiplomProject.requestClient.User;

public class UserHTTPClient extends BaseHTTP {public Response createUser(User user) {
    return doPostRequest(
            ApiUrls.MAIN_HOST + ApiUrls.CREATE_USER,
            user,
            "application/json"
    );
}
    public Response deleteUser(String token) {
        return doDeleteRequest(
                ApiUrls.MAIN_HOST + ApiUrls.USER,
                token
        );
    }
    public Response loginUser(User user) {
        return doPostRequest(
                ApiUrls.MAIN_HOST + ApiUrls.LOGIN_USER,
                user,
                "application/json"
        );
    }
    public Response updateUser(User user, String token) {
        return doPatchRequest(
                ApiUrls.MAIN_HOST + ApiUrls.USER,
                user,
                "application/json",
                token
        );
    }
}
