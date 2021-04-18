package Scraper;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import xyz.mgurga.jitter.utils.*;

public class Scraper {
	private Parser parser = new Parser();
	
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
		
		ChromeOptions options = new ChromeOptions();
//		options.addArguments("headless");
		options.addArguments("mute-audio");
		options.addArguments("disable-gpu");
		options.addArguments("window-size=1000,720");
		options.addArguments("ignore-certificate-errors");
		options.addArguments("allow-running-insecure-content");
		options.addArguments("disable-dev-shm-usage");
		options.addArguments("user-agent='Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36'");
		WebDriver driver = new ChromeDriver(options);
		WebDriverWait wait = new WebDriverWait(driver, 20);
		driver.get(twitterurl);
		
		WebElement tweetelement = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("article")));
		String baseXpath = "//article/div/div/div";
		
//		WebClient wc = new WebClient(BrowserVersion.CHROME);
//		wc.getOptions().setThrowExceptionOnScriptError(false);
//		wc.getOptions().setJavaScriptEnabled(true);
//		HtmlPage page = wc.getPage(twitterurl);
//		
//		wc.waitForBackgroundJavaScriptStartingBefore(10000);
//		
//		String tweetcontent = page.getFirstByXPath(baseXpath + "/div[last()]/div/div");
//		String tweetretweets = page.getFirstByXPath(baseXpath + "/div[last()]/div[last()-1]/div//span");
//		String tweetlikes = page.getFirstByXPath("(" + baseXpath + "/div[last()]/div[last()-1]/div//span[@style])[3]");
//		
		out.setContent(parser.parseTweet(
				tweetelement.findElement(By.xpath(baseXpath + "/div[last()]/div/div")).getText()));
		out.setRetweets(parser.parseStr(
				tweetelement.findElement(By.xpath(baseXpath + "/div[last()]/div[last()-1]/div//span")).getText()));
		out.setLikes(parser.parseStr(
				tweetelement.findElement(By.xpath("(" + baseXpath + "/div[last()]/div[last()-1]/div//span[@style])[3]")).getText()));

//		wc.close();
		
		driver.close();
		driver.quit();
		
		return out;
	}
}
