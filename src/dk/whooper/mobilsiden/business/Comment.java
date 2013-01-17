package dk.whooper.mobilsiden.business;

import java.io.Serializable;

public class Comment implements Serializable {
    private String content;
    private String created;
    private Number id;
    private Number parentId;
    private String title;
    private String user;

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated() {
        return this.created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Number getId() {
        return this.id;
    }

    public void setId(Number id) {
        this.id = id;
    }

    public Number getParentId() {
        return this.parentId;
    }

    public void setParentId(Number parentId) {
        this.parentId = parentId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
