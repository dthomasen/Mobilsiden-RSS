package dk.whooper.mobilsiden.business;

public class Item {
	public String title;
	public String link;
	public String description;
	public String comments;
	public String author;
	public String pubDate;
	
	public Item(){
		
	}
	
	public Item(String title, String link, String description, String comments, String author, String pubDate){
		this.title = title;
		this.link = link;
		this.description = description;
		this.comments = comments;
		this.author = author;
		this.pubDate = pubDate;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getPubDate() {
		return pubDate;
	}
	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}
	
	public String toString(){
		return title+" "+link+" "+description+" "+comments+" "+author+" "+pubDate;
	}
}
