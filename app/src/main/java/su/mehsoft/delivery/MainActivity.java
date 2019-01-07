package su.mehsoft.delivery;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import su.mehsoft.delivery.algorithms.MD5;
import su.mehsoft.delivery.api.AuthAPI;
import su.mehsoft.delivery.api.OrderAPI;
import su.mehsoft.delivery.api.apiimplementation.AuthManager;
import su.mehsoft.delivery.api.apiimplementation.OrderManager;
import su.mehsoft.delivery.api.model.Order;
import su.mehsoft.delivery.api.model.RespondCode;
import su.mehsoft.delivery.api.model.User;


public class MainActivity extends AppCompatActivity {

    private static OrderAPI orderApi;
    private static AuthAPI authApi;
    public String URL = "https://pxm.000webhostapp.com/api/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        orderApi = OrderManager.getApi();
        authApi = AuthManager.getApi();
        /*Call<List<Order>> orders = orderApi.getOrders();
        orders.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                Toast.makeText(getApplicationContext(), String.format("OK"), Toast.LENGTH_SHORT).show();
                Log.d("Network", "Request OK");
                List<Order> allOrders = response.body();
                try {
                    for (Order or: allOrders)
                    {
                        Log.d("APIRespond",or.getId().toString());
                        Log.d("APIRespond",or.getCreatorId().toString());
                        Log.d("APIRespond",or.getName());
                        Log.d("APIRespond",or.getDescription());
                        Log.d("APIRespond",or.getSalary().toString());
                        Log.d("APIRespond",or.getLocation());
                        Log.d("APIRespond",or.getDateCreated()+"\n********************\n");
                    }
                }
                catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Log.d("Network", "Request failed");
                Toast.makeText(getApplicationContext(), String.format("NOT OK"), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });*/

        /*Call<RespondCode> addOrder = orderApi.addOrder(1, "Продам отстойник", "Super class", "51.134234, 12.234285", 555, "2019-01-07 12:33:14");
        addOrder.enqueue(new Callback<RespondCode>() {
            @Override
            public void onResponse(Call<RespondCode> call, Response<RespondCode> response) {
                Log.d("APIRespond", "Succesfully added");
                Log.d("APIRespond", response.body().getRespondCode());
            }

            @Override
            public void onFailure(Call<RespondCode> call, Throwable t) {
                Log.d("APIRespond", "Query failed");
                t.printStackTrace();
            }
        });*/

        /*Call<RespondCode> deleteOrder = orderApi.deleteOrder(4);
        deleteOrder.enqueue(new Callback<RespondCode>() {
            @Override
            public void onResponse(Call<RespondCode> call, Response<RespondCode> response) {
                Log.d("APIRespond", "Succesfully deleted");
                Log.d("APIRespond", response.body().getRespondCode());
            }

            @Override
            public void onFailure(Call<RespondCode> call, Throwable t) {
                Log.d("APIRespond", "Query failed");
                t.printStackTrace();
            }
        });*/

        Call<User> logIn = authApi.logIn("admin","admin","login");
        logIn.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d("APIRespond", "Succesfully logged in");

                Log.d("APIRespond","Login: "+response.body().getLogin());
                Log.d("APIRespond","Email: "+response.body().getEmail());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("APIRespond", "Query failed");
                t.printStackTrace();
            }
        });

    }
}
