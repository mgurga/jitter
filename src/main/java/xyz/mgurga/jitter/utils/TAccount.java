package xyz.mgurga.jitter.utils;

import java.io.Serializable;
import java.net.URL;
import java.time.ZonedDateTime;

public class TAccount implements Serializable {
	private String handle;
	private String nickname;
	private String bio;
	private String avatarUrl;
	private String headerUrl;
	private int following;
	private int followers;
	private ZonedDateTime fetchDate;
	
	public TAccount() {}
	public TAccount(String handle) {
		this.handle = handle;
	}
	
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getBio() {
		return bio;
	}
	public void setBio(String bio) {
		this.bio = bio;
	}
	public int getFollowing() {
		return following;
	}
	public void setFollowing(int following) {
		this.following = following;
	}
	public int getFollowers() {
		return followers;
	}
	public void setFollowers(int followers) {
		this.followers = followers;
	}
	public String getHandle() {
		return handle;
	}
	public void setHandle(String handle) {
		this.handle = handle;
	}
	public String getAvatarUrl() {
		return avatarUrl;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	public String getHeaderUrl() {
		return headerUrl;
	}
	public void setHeaderUrl(String headerUrl) {
		this.headerUrl = headerUrl;
	}
	public ZonedDateTime getFetchDate() {
		return fetchDate;
	}
	public void setFetchDate(ZonedDateTime fetchDate) {
		this.fetchDate = fetchDate;
	}
}
