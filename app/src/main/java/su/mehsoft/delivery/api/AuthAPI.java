package su.mehsoft.delivery.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import su.mehsoft.delivery.api.model.User;

public interface AuthAPI {
    @POST("auth.php")
    @FormUrlEncoded
    Call<User> logIn(@Field("login") String login,
                     @Field("password") String password,
                     @Field("action") String action);
}
