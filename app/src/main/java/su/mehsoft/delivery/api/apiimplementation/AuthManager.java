package su.mehsoft.delivery.api.apiimplementation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import su.mehsoft.delivery.api.AuthAPI;

public class AuthManager {
    static final String URL ="http://pxm.000webhostapp.com/api/";

    public static AuthAPI getApi() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        AuthAPI authAPI = retrofit.create(AuthAPI.class);

        return authAPI;
    }
}
