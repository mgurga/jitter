package xyz.mgurga.jitter.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class Tweet implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long id;
	
	@Column(columnDefinition = "text")
	private String content;
	private int likes;
	private int retweets;
	private int quoteTweets;
	@Transient
	private TAccount author;
	private String handle;
	private String tid;
	private int replyNumber;
	@Column(columnDefinition = "text")
	private String imageurls; // converted to ArrayList and back, comma separated
	private String links; // converted to ArrayList and back, comma separated
	private String postDate; // timezone is always UTC, convert as needed
	private String device;
	private boolean isRetweet = false;
	private String fetchDate; // timezone is always UTC, convert as needed
	private boolean isReply = false;
	private String replyTo;

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
		return new ArrayList<String>(Arrays.asList(this.imageurls.split(", ")));
	}

	public void setImageurls(ArrayList<String> imageurlslist) {
		this.imageurls = String.join(", ", imageurlslist);
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public ArrayList<String> getLinks() {
		return new ArrayList<String>(Arrays.asList(this.links.split(", ")));
	}

	public void setLinks(ArrayList<String> linkslist) {
		this.links = String.join(", ", linkslist);
	}

	public boolean isRetweet() {
		return isRetweet;
	}

	public void setRetweet(boolean isRetweet) {
		this.isRetweet = isRetweet;
	}

	public String getFetchDate() {
		return fetchDate;
	}

	public void setFetchDate(String fetchDate) {
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

	public boolean isReply() {
		return isReply;
	}

	public void setReply(boolean isReply) {
		this.isReply = isReply;
	}

	public int getQuoteTweets() {
		return quoteTweets;
	}

	public void setQuoteTweets(int quotetweets) {
		this.quoteTweets = quotetweets;
	}

	public int getReplyNumber() {
		return replyNumber;
	}

	public void setReplyNumber(int replyNumber) {
		this.replyNumber = replyNumber;
	}

	public String getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}
}
