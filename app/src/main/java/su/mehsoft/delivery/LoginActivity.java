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
import su.mehsoft.delivery.api.AuthAPI;
import su.mehsoft.delivery.api.apiimplementation.AuthManager;
import su.mehsoft.delivery.api.model.User;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnSignIn;
    private Button btnSignUp;
    private EditText etLogin;
    private EditText etPassword;
    private ProgressBar pbLogin;
    private volatile Integer isLogged = 0;
    private static AuthAPI authApi;
    private TextView tvLoginStatus;
    public SharedPreferences sPrefs;

    void initViews()
    {
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);
        etLogin = findViewById(R.id.etLogin);
        etPassword = findViewById(R.id.etPassword);
        pbLogin = findViewById(R.id.pbLogin);
        tvLoginStatus = findViewById(R.id.tvLoginStatus);
        btnSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        pbLogin.setVisibility(ProgressBar.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btnSignIn:
                tvLoginStatus.setText("");
                isLogged = 0;
                pbLogin.setVisibility(ProgressBar.VISIBLE);
                /*new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            synchronized (this) {
                                wait(2000);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        pbLogin.setVisibility(ProgressBar.INVISIBLE);
                                    }
                                });
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                */
                String login = etLogin.getText().toString();
                String password = etPassword.getText().toString();
                Log.d("Logging","Login: "+login+" Password: "+password);
                authApi = AuthManager.getApi();
                Call<User> signIn = authApi.logIn(login, password, "login");

                signIn.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        Log.d("APIResponse", "Last token: "+response.body().getLastToken());
                        isLogged = 1;
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        isLogged = -1;
                    }
                });
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(isLogged == 0);
                        Log.d("Signing in", "isLogged returned"+isLogged);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                synchronized (this)
                                {

                                    pbLogin.setVisibility(ProgressBar.INVISIBLE);
                                    if(isLogged == -1)
                                        tvLoginStatus.setText(getString(R.string.errorLogging));
                                    else {
                                        tvLoginStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
                                        tvLoginStatus.setText(getString(R.string.success));
                                        Intent intent = new Intent(LoginActivity.this, OrdersActivity.class);
                                        startActivity(intent);
                                        LoginActivity.this.finish();
                                    }

                                }
                            }
                        });
                    }
                }).start();


                break;


            case R.id.btnSignUp:

                break;
        }
    }
}
