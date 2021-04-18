package xyz.mgurga.jitter.utils;

import java.io.Serializable;
import java.util.ArrayList;

public class Tweet implements Serializable {
	private String content;
	private int likes;
	private int retweets;
	private TAccount author;
	private ArrayList<String> imageurls;
	private String postDate;
	private String device;

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getLikes() {
		return likes;
	}
	public void setLikes(int likes) {
		this.likes = likes;
	}
	public int getRetweets() {
		return retweets;
	}
	public void setRetweets(int retweets) {
		this.retweets = retweets;
	}
	public TAccount getAuthor() {
		return author;
	}
	public void setAuthor(TAccount author) {
		this.author = author;
	}
	public String getPostDate() {
		return postDate;
	}
	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}
	public ArrayList<String> getImageurls() {
		return imageurls;
	}
	public void setImageurls(ArrayList<String> imageurls) {
		this.imageurls = imageurls;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
}
