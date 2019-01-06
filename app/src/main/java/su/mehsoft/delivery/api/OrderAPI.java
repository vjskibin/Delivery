package su.mehsoft.delivery.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.Query;
import su.mehsoft.delivery.api.model.Order;

public interface OrderAPI {
    //@Multipart
    @GET("orders.php")
    //@Headers("Content-Type: application/json; charset=UTF-8")
    Call<List<Order>> getOrders(@Query("action") String action);
}
