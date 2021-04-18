package Scraper;

import java.io.IOException;

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

		out.setContent(parser.parseTweet(
				tweetelement.findElement(By.xpath(baseXpath + "/div[last()]/div/div")).getText()));
		out.setRetweets(parser.parseStr(
				tweetelement.findElement(By.xpath("(" + baseXpath + "/div[last()]/div[last()-1]/div//span)[2]")).getText()));
		out.setLikes(parser.parseStr(
				tweetelement.findElement(By.xpath("(" + baseXpath + "/div[last()]/div[last()-1]/div//span[@style])[3]")).getText()));

		return out;
	}
	
	public void quit() {
		driver.quit();
	}
}
