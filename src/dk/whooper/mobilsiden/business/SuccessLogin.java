package dk.whooper.mobilsiden.business;

import java.io.Serializable;

public class SuccessLogin implements Serializable {
    private Number id;
    private String name;
    private String token;

    public Number getId() {
        return this.id;
    }

    public void setId(Number id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
