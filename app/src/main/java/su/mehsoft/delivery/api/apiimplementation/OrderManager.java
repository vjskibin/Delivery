package su.mehsoft.delivery.api.apiimplementation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import su.mehsoft.delivery.api.OrderAPI;
import su.mehsoft.delivery.api.model.Order;

public class OrderManager {
    static final String URL ="http://pxm.000webhostapp.com/api/";

    public static OrderAPI getApi() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        OrderAPI orderAPI = retrofit.create(OrderAPI.class);

        return orderAPI;
    }
}
