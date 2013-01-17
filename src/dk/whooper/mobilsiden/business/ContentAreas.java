package dk.whooper.mobilsiden.business;

import java.io.Serializable;

public class ContentAreas implements Serializable {
    private Number id;
    private String name;
    private Number totalArticles;

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

    public Number getTotalArticles() {
        return this.totalArticles;
    }

    public void setTotalArticles(Number totalArticles) {
        this.totalArticles = totalArticles;
    }
}
