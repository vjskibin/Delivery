package su.mehsoft.delivery;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.LifecycleObserver;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
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
    private View mProgressBar;
    public SharedPreferences sPrefs;
    public TextView tvMain;

    void initViews()
    {
        mProgressBar = findViewById(R.id.mProgressBar);
        tvMain = findViewById(R.id.tvMain);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        orderApi = OrderManager.getApi();
        authApi = AuthManager.getApi();

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
                //mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                Log.d("APIRespond", "Succesfully logged in");

                Log.d("APIRespond","Login: "+response.body().getLogin());
                Log.d("APIRespond","Email: "+response.body().getEmail());
                Log.d("APIRespond", "Last token: "+response.body().getLastToken());
                sPrefs = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor ed = sPrefs.edit();
                ed.putString("token", response.body().getLastToken());
                ed.putInt("user_id", response.body().getId());
                ed.commit();

                Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("APIRespond", "Query failed");
                t.printStackTrace();
            }
        });



    }

    @Override
    protected void onStart()
    {
        super.onStart();
        mProgressBar = findViewById(R.id.mProgressBar);
        mProgressBar.setVisibility(ProgressBar.VISIBLE);


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (this)
                    {
                        wait(5000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mProgressBar = findViewById(R.id.mProgressBar);
                                mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                                tvMain = findViewById(R.id.tvMain);
                                sPrefs = getPreferences(MODE_PRIVATE);
                                String savedToken = sPrefs.getString("token", "asd");
                                tvMain.setText(savedToken);
                                Toast.makeText(MainActivity.this,savedToken, Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {

                synchronized (this)
                {
                    try {
                        wait(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    sPrefs = getPreferences(MODE_PRIVATE);
                    String token = sPrefs.getString("token", "asd");
                    Integer id = sPrefs.getInt("user_id", 0);
                    Call<RespondCode> logOut = authApi.logOut("logout", id, token);
                    logOut.enqueue(new Callback<RespondCode>() {
                        @Override
                        public void onResponse(Call<RespondCode> call, Response<RespondCode> response) {
                            Log.d("APIRespond", response.body().getRespondCode());
                        }

                        @Override
                        public void onFailure(Call<RespondCode> call, Throwable t) {
                            Log.d("APIRespond", "Query failed");
                            t.printStackTrace();
                        }
                    });
                }

            }
        }).start();


    }
    @Override
    protected void onRestart()
    {
        super.onRestart();

    }

    @Override
    protected void onResume()
    {
        super.onResume();

    }
    @Override
    protected void onPause()
    {
        super.onPause();

    }
    @Override
    protected void onStop()
    {
        super.onStop();

    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();

    }
}
