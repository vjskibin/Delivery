package su.mehsoft.delivery;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

    private static AuthAPI authApi;

    public SharedPreferences sPrefs;

    void initViews()
    {
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);
        etLogin = findViewById(R.id.etLogin);
        etPassword = findViewById(R.id.etPassword);

        btnSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btnSignIn:
                String login = etLogin.getText().toString();
                String password = etPassword.getText().toString();
                Log.d("Logging","Login: "+login+" Password: "+password);
                authApi = AuthManager.getApi();
                Call<User> signIn = authApi.logIn(login, password, "login");

                signIn.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {

                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });
                break;


            case R.id.btnSignUp:

                break;
        }
    }
}
