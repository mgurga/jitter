package xyz.mgurga.jitter.utils;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class Tweet implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long id;
	
	@Column(columnDefinition = "text")
	private String content;
	private int likes;
	private int retweets;
	@Transient
	private TAccount author;
	private String handle;
	private String tid;
	@Column(columnDefinition = "text")
	private ArrayList<String> imageurls;
	private ArrayList<String> links;
	private ZonedDateTime postDate; // timezone is always UTC, convert as needed
	private String device;
	private boolean isRetweet = false;
	private ZonedDateTime fetchDate;

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

	public ZonedDateTime getPostDate() {
		return postDate;
	}

	public void setPostDate(ZonedDateTime postDate) {
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

	public ArrayList<String> getLinks() {
		return links;
	}

	public void setLinks(ArrayList<String> links) {
		this.links = links;
	}

	public boolean isRetweet() {
		return isRetweet;
	}

	public void setRetweet(boolean isRetweet) {
		this.isRetweet = isRetweet;
	}

	public ZonedDateTime getFetchDate() {
		return fetchDate;
	}

	public void setFetchDate(ZonedDateTime fetchDate) {
		this.fetchDate = fetchDate;
	}

	public String getId() {
		return tid;
	}

	public void setId(String id) {
		this.tid = id;
	}

	public String getHandle() {
		return handle;
	}

	public void setHandle(String handle) {
		this.handle = handle;
	}
}
