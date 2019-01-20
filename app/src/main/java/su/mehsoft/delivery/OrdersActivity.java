package su.mehsoft.delivery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import su.mehsoft.delivery.adapter.AdapterOrder;
import su.mehsoft.delivery.api.OrderAPI;
import su.mehsoft.delivery.api.apiimplementation.OrderManager;
import su.mehsoft.delivery.api.model.Order;

public class OrdersActivity extends AppCompatActivity {

    private static OrderAPI orderApi;
    private RecyclerView recyclerView;
    private List<Order> posts;
    private ListView lvOrders;
    private ProgressBar pbOrdersLoading;
    private Integer ordersLoaded;
    private ArrayList<Order> allOrders;
    private AdapterOrder adbOrder;;

    String[] names = { "Иван", "Марья", "Петр", "Антон", "Даша", "Борис",
            "Костя", "Игорь", "Анна", "Денис", "Андрей" };

    private void initViews() {
        ListView lvOrders = findViewById(R.id.lvOrders);
        ProgressBar pbOrdersLoading = findViewById(R.id.pbOrdersLoading);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        initViews();
        ordersLoaded = 0;

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        orderApi = OrderManager.getApi();
        pbOrdersLoading = findViewById(R.id.pbOrdersLoading);
        pbOrdersLoading.setVisibility(ProgressBar.VISIBLE);
        allOrders = new ArrayList<Order>();
        Call<List<Order>> orders = orderApi.getOrders();
        orders.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                Toast.makeText(getApplicationContext(), String.format("OK"), Toast.LENGTH_SHORT).show();
                Log.d("APIResponse", "Request OK");
                int i = 0;

                List<Order> ordrs = response.body();
                try {
                    for (Order or: ordrs)
                    {
                        Log.d("APIRespond",or.getId().toString());
                        Log.d("APIRespond",or.getCreatorId().toString());
                        Log.d("APIRespond",or.getName());
                        Log.d("APIRespond",or.getDescription());
                        Log.d("APIRespond",or.getSalary().toString());
                        Log.d("APIRespond",or.getLocation());
                        Log.d("APIRespond",or.getDateCreated()+"\n********************\n");
                        allOrders.add(or);
                        names[i] = or.getName();
                        i++;
                    }

                }
                catch (NullPointerException e) {
                    e.printStackTrace();
                }
                ordersLoaded = 1;
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Log.d("Network", "Request failed");
                Toast.makeText(getApplicationContext(), String.format("NOT OK"), Toast.LENGTH_SHORT).show();
                ordersLoaded = -1;
                t.printStackTrace();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    while(ordersLoaded == 0);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lvOrders = findViewById(R.id.lvOrders);
                            pbOrdersLoading.setVisibility(ProgressBar.INVISIBLE);
                            adbOrder= new AdapterOrder (OrdersActivity.this, 0, allOrders);

                            /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(OrdersActivity.this,
                                    android.R.layout.simple_list_item_1, names);*/
                            lvOrders.setAdapter(adbOrder);
                        }
                    });
                }
            }
        }).start();

    }
}
