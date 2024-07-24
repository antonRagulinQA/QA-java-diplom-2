package DiplomProject.restClients.httpClients;

import DiplomProject.ApiUrls;
import io.restassured.response.Response;
import DiplomProject.requestClient.Order;


public class OrderHTTPClient extends BaseHTTP {
    public Response createOrder(Order order, String token) {
        return doPostRequest(
                ApiUrls.MAIN_HOST + ApiUrls.ORDERS,
                order,
                "application/json",
                token
        );
    }
    public Response getIngredientList() {
        return doGetRequest(
                ApiUrls.MAIN_HOST + ApiUrls.INGREDIENTS
        );
    }

    public Response getOrderList(String token) {
        return doGetRequest(
                ApiUrls.MAIN_HOST + ApiUrls.ORDERS,
                token
        );
    }

    public Response getOrderListAll() {
        return doGetRequest(
                ApiUrls.MAIN_HOST + ApiUrls.ORDERS_ALL
        );
    }
}
