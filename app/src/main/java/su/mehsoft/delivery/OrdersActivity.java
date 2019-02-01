package su.mehsoft.delivery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import su.mehsoft.delivery.adapter.AdapterOrder;
import su.mehsoft.delivery.api.AuthAPI;
import su.mehsoft.delivery.api.OrderAPI;
import su.mehsoft.delivery.api.apiimplementation.AuthManager;
import su.mehsoft.delivery.api.apiimplementation.OrderManager;
import su.mehsoft.delivery.api.model.Order;
import su.mehsoft.delivery.api.model.RespondCode;

public class OrdersActivity extends AppCompatActivity {



    private static OrderAPI orderApi;
    private static AuthAPI authApi;
    private RecyclerView recyclerView;
    private List<Order> posts;
    private ListView lvOrders;
    private ProgressBar pbOrdersLoading;
    private Integer ordersLoaded;
    private ArrayList<Order> allOrders;
    private AdapterOrder adbOrder;
    private TextView tvHelloUser;

    public SharedPreferences sPrefs;
    private final String PREFS = "logInfo";
    private final String PREFS_LOGGED = "logged";
    private final String PREFS_LOGIN = "user_login";
    private final String PREFS_TOKEN = "token";
    private final String PREFS_EMAIL = "user_email";
    private final String PREFS_ID = "user_id";

    private Integer logoutDone;


    private void initViews() {
        ListView lvOrders = findViewById(R.id.lvOrders);
        ProgressBar pbOrdersLoading = findViewById(R.id.pbOrdersLoading);
        tvHelloUser = findViewById(R.id.tvHelloUser);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch(item.getItemId()) {
            case R.id.menuAdd:
                Intent intent = new Intent(this, NewOrderActivity.class);
                startActivity(intent);
                break;

            case R.id.menuLogOut:


                sPrefs = getSharedPreferences(PREFS,MODE_PRIVATE);
                String savedToken = sPrefs.getString(PREFS_TOKEN, "-1");
                boolean isLogged = sPrefs.getBoolean(PREFS_LOGGED, false);
                Integer userId = sPrefs.getInt(PREFS_ID, -1);


                SharedPreferences.Editor ed = sPrefs.edit();
                ed.remove(PREFS_LOGGED);
                ed.putBoolean(PREFS_LOGGED,false);
                ed.remove(PREFS_EMAIL);
                ed.remove(PREFS_ID);
                ed.remove(PREFS_LOGIN);
                ed.remove(PREFS_TOKEN);
                ed.apply();


                sPrefs = getSharedPreferences(PREFS,MODE_PRIVATE);
                String savedToken1 = sPrefs.getString(PREFS_TOKEN, "-1");
                boolean isLogged1 = sPrefs.getBoolean(PREFS_LOGGED, false);
                Integer userId1 = sPrefs.getInt(PREFS_ID, -1);

                logoutDone = 0;
                authApi = AuthManager.getApi();
                Call<RespondCode> logOut = authApi.logOut("logout",userId,savedToken);
                logOut.enqueue(new Callback<RespondCode>() {
                    @Override
                    public void onResponse(Call<RespondCode> call, Response<RespondCode> response) {
                        Log.d("APIResponse",response.body().getRespondCode());
                        logoutDone = 1;
                    }

                    @Override
                    public void onFailure(Call<RespondCode> call, Throwable t) {
                        logoutDone = -1;
                        t.printStackTrace();
                    }
                });

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (logoutDone == 0);
                        if(logoutDone == 1) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    synchronized (this) {
                                        Intent intent = new Intent(OrdersActivity.this, LoginActivity.class);
                                        startActivity(intent);

                                        OrdersActivity.this.finish();
                                    }
                                }
                            });
                        }

                    }
                }).start();

                break;

        }



        return super.onOptionsItemSelected(item);
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

        sPrefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        try {
            String userHello = "Hello, " + sPrefs.getString(PREFS_LOGIN,"-1");
            tvHelloUser.setText(userHello);
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }

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
                        allOrders.add(or);
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
                            lvOrders.setAdapter(adbOrder);
                        }
                    });
                }
            }
        }).start();

    }
}
