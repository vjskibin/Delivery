package su.mehsoft.delivery.api.model;

import com.google.gson.annotations.SerializedName;

public class RespondCode {
    @SerializedName("respond_code")
    private String respondCode;

    RespondCode(String respondCode) {
        this.respondCode = respondCode;
    }

    public String getRespondCode() {
        return this.respondCode;
    }

}
