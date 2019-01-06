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
import su.mehsoft.delivery.api.OrderAPI;
import su.mehsoft.delivery.api.apiimplementation.OrderManager;
import su.mehsoft.delivery.api.model.Order;


public class MainActivity extends AppCompatActivity {

    private static OrderAPI orderApi;
    public String URL = "https://pxm.000webhostapp.com/api/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        orderApi = retrofit.create(OrderAPI.class);

        Call<List<Order>> orders = orderApi.getOrders("show");
        orders.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                Toast.makeText(getApplicationContext(), String.format("OK"), Toast.LENGTH_SHORT).show();
                Log.d("Network", "Request OK");
                List<Order> allOrders = response.body();
                try {
                    for (Order or: allOrders)
                    {
                        System.out.println(or.getId());
                        System.out.println(or.getCreatorId());
                        System.out.println(or.getName());
                        System.out.println(or.getDescription());
                        System.out.println(or.getSalary());
                        System.out.println(or.getLocation());
                        System.out.println(or.getDateCreated()+"\n********************\n");
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
        });

        /*orderApi.getOrders().enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {

                System.out.print("LALALALALA");
                for (Order or: response.body())
                {
                    System.out.println(or.getId());
                    System.out.println(or.getCreatorId());
                    System.out.println(or.getName());
                    System.out.println(or.getDescription());
                    System.out.println(or.getSalary());
                    System.out.println(or.getLocation());
                    System.out.println(or.getDateCreated()+"\n********************\n");
                }
            }


            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Meh", Toast.LENGTH_LONG).show();
                System.out.print("KEKEKEKEKE");
                t.printStackTrace();
            }
        });*/
    }
}
