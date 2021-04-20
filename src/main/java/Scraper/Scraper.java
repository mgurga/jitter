package Scraper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import xyz.mgurga.jitter.utils.*;

public class Scraper {
	private Parser parser = new Parser();
	
	private WebDriver driver;
	private WebDriverWait wait;
	
	public Scraper() {
		FirefoxOptions options = new FirefoxOptions();
		options.setHeadless(true);
		options.addArguments("--window-size=1000,720");
		// TODO: generate different (yet still compatible) useragents for each Scraper
		driver = new FirefoxDriver(options);
		wait = new WebDriverWait(driver, 20);
	}
	
	public Tweet getTweetFromURL(String url) throws IOException {
		String author = null;
		String id = null;
		String[] urlparts = url.split("/");
		
		for(int i = 0; i < urlparts.length; i++) {
			if(urlparts[i].equals("twitter.com")) {
				author = urlparts[i+1];
			}
			if(urlparts[i].equals("status")) {
				id = urlparts[i+1];
			}
		}
		
		if(author == null)
			throw new IOException("could not decode author");
		if(id == null)
			throw new IOException("could not decode id");
		
		return this.getTweet(author, id);
	}
	
	public Tweet getTweet(String author, String id) throws IOException {
		String twitterurl = "https://mobile.twitter.com/" + author + "/status/" + id;
		Tweet out = new Tweet();
		
		driver.get(twitterurl);
		
		WebElement tweetelement = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("article")));
		String baseXpath = "//article/div/div/div";
		
		// main tweet content
		out.setContent(parser.parseTweet(
				tweetelement.findElement(By.xpath(baseXpath + "/div[last()]/div/div")).getText()));
		
		// set likes and retweets
		if(tweetelement.findElements(By.xpath(baseXpath + "/div[last()]/div[last()-1]/div//span[@style]")).size() == 2) {
			out.setRetweets(parser.parseStr(
					tweetelement.findElement(By.xpath("(" + baseXpath + "/div[last()]/div[last()-1]/div//span)[1]")).getText()));
			out.setLikes(parser.parseStr(
					tweetelement.findElement(By.xpath("(" + baseXpath + "/div[last()]/div[last()-1]/div//span[@style])[2]")).getText()));
		} else {
			out.setRetweets(parser.parseStr(
					tweetelement.findElement(By.xpath("(" + baseXpath + "/div[last()]/div[last()-1]/div//span)[2]")).getText()));
			out.setLikes(parser.parseStr(
					tweetelement.findElement(By.xpath("(" + baseXpath + "/div[last()]/div[last()-1]/div//span[@style])[3]")).getText()));
		}
		
		// set tweet post date
		String rawdatestr = tweetelement.findElement(By.xpath(baseXpath + "/div[last()]/div[last()-2]//a[1]")).getText();
		rawdatestr = rawdatestr.replace("· ", "");
		DateTimeFormatter twittertimeformat = DateTimeFormatter.ofPattern("h:mm a MMM d, uuuu");
		LocalDateTime postdt = LocalDateTime.from(twittertimeformat.parse(rawdatestr));
		ZonedDateTime zpostdt = ZonedDateTime.of(postdt, ZoneId.systemDefault());
		out.setPostDate(zpostdt.withZoneSameInstant(ZoneId.of("UTC")));
		
		// set post device
		String postdevice = tweetelement.findElement(By.xpath(baseXpath + "/div[last()]/div[last()-2]//a[2]")).getText();
		out.setDevice(postdevice);
		
		return out;
	}
	
	public void quit() {
		driver.quit();
	}
}
