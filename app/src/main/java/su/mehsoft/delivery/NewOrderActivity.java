package su.mehsoft.delivery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import su.mehsoft.delivery.api.OrderAPI;
import su.mehsoft.delivery.api.apiimplementation.OrderManager;
import su.mehsoft.delivery.api.model.RespondCode;

public class NewOrderActivity extends AppCompatActivity implements View.OnClickListener{

    public SharedPreferences sPrefs;
    private final String PREFS = "logInfo";
    private final String PREFS_LOGGED = "logged";
    private final String PREFS_LOGIN = "user_login";
    private final String PREFS_TOKEN = "token";
    private final String PREFS_EMAIL = "user_email";
    private final String PREFS_ID = "user_id";

    private TextView tvOrderStatus;
    private EditText etOrderName;
    private EditText etOrderDescription;
    private EditText etOrderLocation;
    private EditText etOrderSalary;
    private Button btnNewOrder;
    private ProgressBar pbNewOrder;

    private static OrderAPI orderApi;

    private volatile Integer isAdded = 0;

    private void initViews() {
        tvOrderStatus = findViewById(R.id.tvOrderStatus);
        etOrderName = findViewById(R.id.etOrderName);
        etOrderDescription = findViewById(R.id.etOrderDescription);
        etOrderLocation = findViewById(R.id.etOrderLocation);
        etOrderSalary = findViewById(R.id.etOrderSalary);
        btnNewOrder = findViewById(R.id.btnNewOrder);
        pbNewOrder = findViewById(R.id.pbNewOrder);

        btnNewOrder.setOnClickListener(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        initViews();
        pbNewOrder.setVisibility(ProgressBar.INVISIBLE);
        tvOrderStatus.setText("");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNewOrder:
                pbNewOrder.setVisibility(ProgressBar.VISIBLE);
                String orderName = etOrderName.getText().toString();
                String orderDescription = etOrderDescription.getText().toString();
                String orderLocation = etOrderLocation.getText().toString();
                Integer orderSalary = Integer.parseInt(etOrderSalary.getText().toString());

                sPrefs = getSharedPreferences(PREFS,MODE_PRIVATE);
                Integer userId = sPrefs.getInt(PREFS_ID, -1);


                orderApi = OrderManager.getApi();

                Call<RespondCode> addNewOrder = orderApi.addOrder(userId, orderName, orderDescription, orderLocation, orderSalary, "2019-01-29");

                addNewOrder.enqueue(new Callback<RespondCode>() {
                    @Override
                    public void onResponse(Call<RespondCode> call, Response<RespondCode> response) {
                        if(response.body().getRespondCode().equals("All OK"))
                            isAdded = 1;
                        else
                            isAdded = -1;
                    }

                    @Override
                    public void onFailure(Call<RespondCode> call, Throwable t) {
                        isAdded = -2;
                    }
                });

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(isAdded == 0);

                        Log.d("NewOrder", "isAdded returned: "+ isAdded);
                        if(isAdded == 1) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    synchronized (this) {
                                        pbNewOrder.setVisibility(ProgressBar.INVISIBLE);
                                        tvOrderStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
                                        tvOrderStatus.setText(getString(R.string.success));
                                        Intent intent = new Intent(NewOrderActivity.this, OrdersActivity.class);
                                        startActivity(intent);
                                        NewOrderActivity.this.finish();
                                    }
                                }
                            });
                        }
                        else if (isAdded == -1) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    synchronized (this) {
                                        pbNewOrder.setVisibility(ProgressBar.INVISIBLE);
                                        tvOrderStatus.setTextColor(getResources().getColor(R.color.colorAccent));
                                        tvOrderStatus.setText(getString(R.string.errorPosting));
                                    }
                                }
                            });
                        }
                        else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pbNewOrder.setVisibility(ProgressBar.INVISIBLE);
                                    tvOrderStatus.setTextColor(getResources().getColor(R.color.colorAccent));
                                    tvOrderStatus.setText(getString(R.string.connectionError));
                                }
                            });
                        }
                    }
                }).start();



                break;
        }
    }
}
