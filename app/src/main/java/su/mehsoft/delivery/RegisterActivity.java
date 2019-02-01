package su.mehsoft.delivery;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import su.mehsoft.delivery.api.model.RespondCode;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etRegLogin;
    private Button btnRegister;
    private EditText etRegPassword;
    private EditText etRegPasswordAgain;
    private EditText etEmail;
    private TextView tvRegStatus;
    private ProgressBar pbReg;

    private volatile Integer isRegistered;

    private static AuthAPI authApi;

    private void initViews() {
        btnRegister = findViewById(R.id.btnRegister);
        etRegLogin = findViewById(R.id.etRegLogin);
        etRegPassword = findViewById(R.id.etRegPassword);
        etRegPasswordAgain = findViewById(R.id.etRegPasswordAgain);
        tvRegStatus = findViewById(R.id.tvRegStatus);
        pbReg = findViewById(R.id.pbReg);
        etEmail = findViewById(R.id.etRegEmail);
        btnRegister.setOnClickListener(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        pbReg.setVisibility(ProgressBar.INVISIBLE);
        tvRegStatus.setText("");


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister:

                String login = etRegLogin.getText().toString();
                String pass = etRegPassword.getText().toString();
                String passAgain = etRegPasswordAgain.getText().toString();
                String email = etEmail.getText().toString();

                isRegistered = 0;

                if (!pass.equals(passAgain)) {
                    tvRegStatus.setTextColor(getResources().getColor(R.color.colorAccent));
                    tvRegStatus.setText(getString(R.string.passDoesNotMatch));
                     break;
                }

                if (    TextUtils.isEmpty(etRegLogin.getText().toString()) ||
                        TextUtils.isEmpty(etRegPassword.getText().toString()) ||
                        TextUtils.isEmpty(etRegPasswordAgain.getText().toString()) ||
                        TextUtils.isEmpty(etEmail.getText().toString())) {
                    tvRegStatus.setTextColor(getResources().getColor(R.color.colorAccent));
                    tvRegStatus.setText(getString(R.string.requireFields));
                    break;
                }
                pbReg.setVisibility(ProgressBar.VISIBLE);

                authApi = AuthManager.getApi();

                Call<RespondCode> register =  authApi.register("register",login, pass, email);
                register.enqueue(new Callback<RespondCode>() {
                    @Override
                    public void onResponse(Call<RespondCode> call, Response<RespondCode> response) {

                        Log.d("APIResponse",response.body().getRespondCode());
                        if(response.body().getRespondCode().equals("User already exists")) {
                            isRegistered = -2; //User exists
                        }
                        //Register successful
                        else if(response.body().getRespondCode().equals("Register successful")) {
                            isRegistered = 1; //All ok
                        }
                        else {
                            isRegistered = -1; //Something goes motherfckn' wrong
                        }

                    }

                    @Override
                    public void onFailure(Call<RespondCode> call, Throwable t) {
                        isRegistered = -1;
                        t.printStackTrace();

                    }
                });

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(isRegistered == 0);
                        Log.d("Register", "isRegistered returned: "+ isRegistered);

                        if(isRegistered == 1) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    synchronized (this) {
                                            pbReg.setVisibility(ProgressBar.INVISIBLE);
                                            tvRegStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
                                            tvRegStatus.setText(getString(R.string.success));
                                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            RegisterActivity.this.finish();

                                    }

                                }
                            });
                        }
                        else if (isRegistered == -2) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    synchronized (this) {
                                        pbReg.setVisibility(ProgressBar.INVISIBLE);
                                        tvRegStatus.setText(getString(R.string.userExists));
                                    }
                                }
                            });
                        }
                        else if (isRegistered == -1) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    synchronized (this) {
                                        pbReg.setVisibility(ProgressBar.INVISIBLE);
                                        tvRegStatus.setText(getString(R.string.errorRegistering));
                                    }
                                }
                            });

                        }


                    }
                }).start();



                break;
        }
    }
}
