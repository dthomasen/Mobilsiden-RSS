package dk.whooper.mobilsiden.business;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class Article implements Serializable, Comparable<Article> {
    private String author;
    private String bodytext;
    private String category;
    private String comments;
    private String header;
    private String id;
    private List<Images> images;
    private String published;
    private List<RelatedArticles> relatedArticles;
    private String url;
    private List<Videos> videos;
    private Integer views;
    private Boolean unread;

    public Boolean isUnread() {
        return unread;
    }

    public void setUnread(Boolean unread) {
        this.unread = unread;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBodytext() {
        return this.bodytext;
    }

    public void setBodytext(String bodytext) {
        this.bodytext = bodytext;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getHeader() {
        return this.header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Images> getImages() {
        return this.images;
    }

    public void setImages(List<Images> images) {
        this.images = images;
    }

    public String getPublished() {
        return this.published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public List<RelatedArticles> getRelatedArticles() {
        return this.relatedArticles;
    }

    public void setRelatedArticles(List<RelatedArticles> relatedArticles) {
        this.relatedArticles = relatedArticles;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Videos> getVideos() {
        return this.videos;
    }

    public void setVideos(List<Videos> videos) {
        this.videos = videos;
    }

    public Integer getViews() {
        return this.views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    @Override
    public int compareTo(Article another) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            return dateFormatter.parse(another.getPublished()).compareTo(dateFormatter.parse(this.getPublished()));
        } catch (ParseException e) {
            //Nothing
        }

        return 0;
    }
}
