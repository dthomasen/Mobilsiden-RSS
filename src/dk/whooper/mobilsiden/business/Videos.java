package dk.whooper.mobilsiden.business;

public class Videos {
    private String urlImage;
    private String title;
    private String url;

    public String getUrlImage() {
        return this.urlImage;
    }

    public void setUrlImage(String thumbUrl) {
        this.urlImage = thumbUrl;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
