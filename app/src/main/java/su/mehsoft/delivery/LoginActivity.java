package su.mehsoft.delivery;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import su.mehsoft.delivery.api.AuthAPI;
import su.mehsoft.delivery.api.apiimplementation.AuthManager;
import su.mehsoft.delivery.api.model.RespondCode;
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
    private Integer alreadyLogged;

    private final String PREFS = "logInfo";
    private final String PREFS_LOGGED = "logged";
    private final String PREFS_LOGIN = "user_login";
    private final String PREFS_TOKEN = "token";
    private final String PREFS_EMAIL = "user_email";
    private final String PREFS_ID = "user_id";

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
        authApi = AuthManager.getApi();


        sPrefs = getSharedPreferences(PREFS,MODE_PRIVATE);
        String savedToken = sPrefs.getString(PREFS_TOKEN, "-1");
        boolean isLogged = sPrefs.getBoolean(PREFS_LOGGED, false);
        Integer userId = sPrefs.getInt(PREFS_ID, -1);

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        if(permissionCheck == PackageManager.PERMISSION_DENIED)
            Toast.makeText(getApplicationContext(), String.format("NO INTERNET"), Toast.LENGTH_SHORT).show();

        Log.d("sPrefs","Saved token: " + savedToken);
        Log.d("sPrefs","isLogged: " + isLogged);
        Log.d("sPrefs","userId: " + userId);
        alreadyLogged = 0;
        if(isLogged) {
            Call<RespondCode> checkToken = authApi.checkToken("check_token", userId, savedToken);

            checkToken.enqueue(new Callback<RespondCode>() {
                @Override
                public void onResponse(Call<RespondCode> call, Response<RespondCode> response) {
                    Log.d("APIResponse",response.body().getRespondCode());
                    if (response.body().getRespondCode().equals("Auth failed")) {
                        alreadyLogged = -2; //Expired token
                    }
                    else if (response.body().getRespondCode().equals("Auth ok")) {
                        alreadyLogged = 1; // All ok
                    }
                    else {
                        alreadyLogged = -1; //Something goes really wrong
                    }
                }

                @Override
                public void onFailure(Call<RespondCode> call, Throwable t) {
                    alreadyLogged = -1; //Something goes really wrong
                    Toast.makeText(LoginActivity.this,"Reauth failed", Toast.LENGTH_SHORT).show();
                }
            });

            new Thread(new Runnable() {
                @Override
                public void run() {
                    synchronized (this) {
                        try {
                            wait(5000);
                            if (alreadyLogged == 0) alreadyLogged = -1;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }).start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (alreadyLogged == 0);
                    Log.d("Logging before load", "alreadyLogged returned: "+alreadyLogged);
                    if (alreadyLogged == 1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                synchronized (this) {
                                    Intent intent = new Intent(LoginActivity.this, OrdersActivity.class);
                                    startActivity(intent);
                                    LoginActivity.this.finish();
                                }
                            }
                        });
                    }

                }
            }).start();
        }



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
                        /*sPrefs = getPreferences(MODE_PRIVATE);
                        String savedToken = sPrefs.getString("token", "asd");*/
                        sPrefs = getSharedPreferences(PREFS,MODE_PRIVATE);
                        SharedPreferences.Editor ed = sPrefs.edit();
                        ed.putBoolean(PREFS_LOGGED, true);
                        ed.putString(PREFS_TOKEN, response.body().getLastToken());
                        ed.putInt(PREFS_ID, response.body().getId());
                        ed.putString(PREFS_LOGIN, response.body().getLogin());
                        ed.putString(PREFS_EMAIL, response.body().getEmail());
                        ed.apply();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        isLogged = -1;
                        Toast.makeText(LoginActivity.this,"Login failed", Toast.LENGTH_SHORT).show();
                        t.printStackTrace();
                    }
                });
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(isLogged == 0);
                        Log.d("Signing in", "isLogged returned: "+isLogged);

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

                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
                break;
        }
    }
}
