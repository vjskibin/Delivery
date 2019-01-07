package su.mehsoft.delivery.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.Query;
import su.mehsoft.delivery.api.model.Order;
import su.mehsoft.delivery.api.model.RespondCode;

public interface OrderAPI {
    //show all orders
    @GET("orders.php?action=show")
    Call<List<Order>> getOrders();

    //add an order
    @GET("orders.php?action=add")
    Call<RespondCode> addOrder(@Query("creator_id") Integer creatorId,
                               @Query("name") String name,
                               @Query("description") String description,
                               @Query("location") String location,
                               @Query("salary") Integer salary,
                               @Query("date_created") String date_created
    );

    @GET("orders.php?action=delete")
    Call<RespondCode> deleteOrder(@Query("id") Integer id);
}
