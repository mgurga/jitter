package Scraper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
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
	private String twitterurl = "https://mobile.twitter.com/";

	public Scraper() {
		FirefoxOptions options = new FirefoxOptions();
		options.setHeadless(true);
		options.addArguments("--window-size=1000,720");
		System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "/dev/null");
		// TODO: generate different (yet still compatible) useragents for each Scraper
		driver = new FirefoxDriver(options);
		wait = new WebDriverWait(driver, 20);
	}

	public String[] getInfoFromURL(String url) throws IOException {
		String author = null;
		String id = null;
		String[] urlparts = url.split("/");

		for (int i = 0; i < urlparts.length; i++) {
			if (urlparts[i].contains("twitter.com")) {
				author = urlparts[i + 1];
			}
			if (urlparts[i].equals("status")) {
				id = urlparts[i + 1];
			}
		}

		if (author == null)
			throw new IOException("could not decode author");
		if (id == null)
			throw new IOException("could not decode id");

		String[] out = { author, id };
		return out;
	}

	public Tweet getTweetFromURL(String url, boolean getAuthor) throws IOException {
		String[] info = getInfoFromURL(url);
		return this.getTweet(info[0], info[1], getAuthor);
	}

	public Tweet getTweet(String author, String id, boolean getAuthor) throws IOException {
		String tweeturl = this.twitterurl + author + "/status/" + id;
		Tweet out = new Tweet();
		System.out.println(author + "/status/" + id);
		driver.get(tweeturl);

		WebElement tweetelement = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("article")));
		String baseXpath = "//article/div/div/div";

		out.setId(id);

		// set likes and retweets, retry if parsing error
		// TODO: Sometimes NumberFormatException error
		try {
			// check if tweet is a reply and which level reply
			WebElement targetarticle = tweetelement
					.findElement(By.xpath("//article/div/div/div/div/div[last()][@role]/../../../../.."));
			List<WebElement> allarticles = tweetelement.findElements(By.xpath("//article"));
			for (int i = 0; i < allarticles.size(); i++) {
				if (allarticles.get(i).getAttribute("innerHTML").equals(targetarticle.getAttribute("innerHTML"))) {
					System.out.println("set basepath to article #" + (i));
					out.setReplyNumber(i);
					if (i >= 1) {
						baseXpath = "(//article)[" + (i + 1) + "]/div/div/div";
						String parentXpath = "(//article)[" + (i) + "]/div/div/div";
						out.setReply(true);
						out.setReplyTo(tweetelement.findElement(By.xpath(parentXpath + "//a[@aria-label]"))
								.getAttribute("href").replace(this.twitterurl, "/"));
						System.out.println("reply to: " + out.getReplyTo());
					} else {
						baseXpath = "//article/div/div/div";
					}
				}
			}

			// get tweet metrics such as likes, retweets, and quote tweets
			try {
				List<WebElement> tweetmetrics = tweetelement
						.findElements(By.xpath("(" + baseXpath + "/div/div[last()-1]//span[@style])/../.."));
				for (WebElement metric : tweetmetrics) {
					if (metric.getText().contains("Retweet")) {
						out.setRetweets(parser.parseStr(metric.getText().replaceAll("[^0-9.]", "")));
					}
					if (metric.getText().contains("Like")) {
						out.setLikes(parser.parseStr(metric.getText().replaceAll("[^0-9.]", "")));
					}
					if (metric.getText().contains("Quote Tweet")) {
						out.setQuoteTweets(parser.parseStr(metric.getText().replaceAll("[^0-9.]", "")));
					}
				}
			} catch (NoSuchElementException n) {
				System.out.println("unable to find tweet metrics");
				out.setLikes(0);
				out.setRetweets(0);
				out.setQuoteTweets(0);
			}

			// get tweet content
			if (out.isReply()) {
				out.setContent(parser.parseTweet(
						tweetelement.findElement(By.xpath("(" + baseXpath + "/div[last()]/div/div)[2]")).getText()));
			} else {
				out.setContent(parser
						.parseTweet(tweetelement.findElement(By.xpath(baseXpath + "/div[last()]/div/div")).getText()));
			}
		} catch (NoSuchElementException e) {
			System.out.println("tweet info not found, " + e.getMessage());
		} catch (NumberFormatException n) {
			System.out.println("problem parsing tweet " + author + "/status/" + id + ", trying again");
			return getTweet(author, id, true);
		}

		out.setRetweet(false);

		List<WebElement> tweetimgs = tweetelement.findElements(By.xpath(baseXpath + "/div[last()]/div[last()-3]//img"));
		ArrayList<String> imgurls = new ArrayList<String>();
		for (WebElement imgele : tweetimgs) {
			try {
				String rawurl = imgele.getAttribute("src");
				rawurl = rawurl.replace("name=small", "name=large");
				rawurl = rawurl.replace("format=jpg", "format=png");
				if (rawurl.contains("/profile_images/"))
					out.setRetweet(true);
				if (!rawurl.contains("/emoji/") && !rawurl.contains("/profile_images/"))
					imgurls.add(rawurl);
			} catch (StaleElementReferenceException s) {
				System.out.println("caught stale tweet image");
			}
		}
		out.setImageurls(imgurls);

		String rawdatestr;
		if (!out.isReply()) {
			try {
				rawdatestr = tweetelement.findElement(By.xpath(baseXpath + "/div[last()]/div[last()-2]//a[1]"))
						.getText();
				if (rawdatestr.equals(""))
					throw new NoSuchElementException("Date is empty");
			} catch (NoSuchElementException n) {
				System.out.println("trying second date element");
				rawdatestr = tweetelement.findElement(By.xpath(baseXpath + "/div[last()]/div[last()-1]//a[1]"))
						.getText();
			}
		} else {
			try {
				rawdatestr = tweetelement.findElement(By.xpath(baseXpath + "/div[last()]/div[last()-2]//a[1]"))
						.getText();
				if (rawdatestr.equals("")) {
					throw new NoSuchElementException("got link");
				}
			} catch (NoSuchElementException n) {
				System.out.println("trying second date element");
				rawdatestr = tweetelement.findElement(By.xpath(baseXpath + "/div[last()]/div[last()-1]//a[1]"))
						.getText();
			}
		}

		rawdatestr = rawdatestr.replace("· ", "");
		DateTimeFormatter twittertimeformat = DateTimeFormatter.ofPattern("h:mm a MMM d, uuuu");
		LocalDateTime postdt = LocalDateTime.from(twittertimeformat.parse(rawdatestr));
		ZonedDateTime zpostdt = ZonedDateTime.of(postdt, ZoneId.systemDefault());
		out.setPostDate(zpostdt.withZoneSameInstant(ZoneId.of("UTC")).toString());

		String postdevice;
		try {
			postdevice = tweetelement.findElement(By.xpath(baseXpath + "/div[last()]/div[last()-2]//a[2]")).getText();
		} catch (NoSuchElementException n) {
			postdevice = tweetelement.findElement(By.xpath(baseXpath + "/div[last()]/div[last()-1]//a[2]")).getText();
		}

		out.setDevice(postdevice);

		if (getAuthor)
			out.setAuthor(this.getAccountInfo(author));
		out.setHandle(author);

		out.setFetchDate(ZonedDateTime.now(ZoneId.of("UTC")).toString());

		return out;
	}

	public ArrayList<String[]> getTweetReplyLinks(Tweet tweet) throws IOException {
		String tweeturl;
		if (tweet.isReply()) {
			tweeturl = this.twitterurl + tweet.getAuthor().getHandle() + "/status/" + tweet.getId() + "?ref_src=twsrc";
		} else {
			tweeturl = this.twitterurl + tweet.getAuthor().getHandle() + "/status/" + tweet.getId();
		}
		ArrayList<String[]> out = new ArrayList<>();

		driver.get(tweeturl);

		WebElement searchelement = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("article")));
		List<WebElement> ses = searchelement.findElements(By.xpath("//article//a[@aria-label]"));

		for (int i = 0; i < ses.size(); i++) {
			int maxreply = Math.min(3 + tweet.getReplyNumber(), ses.size());
			if (i > tweet.getReplyNumber() && i <= maxreply) {
				String url = ses.get(i).getAttribute("href");
				out.add(this.getInfoFromURL(url));
			}
		}

		return out;
	}

	public TAccount getAccountInfo(String handle) {
		String accounturl = this.twitterurl + handle;
		TAccount out = new TAccount();
		out.setHandle(handle);
		System.out.println("getting @" + handle);

		if (handle.equals("favicon.ico"))
			return null;

		driver.get(accounturl);
		String baseXpath = "//main/div/div/div/div[1]/div/div[2]/div/div/div[1]";
		WebElement accountelement = null;
		try {
			accountelement = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("article")));
		} catch (NoSuchElementException e) {
		}
		out.setNickname(accountelement.findElement(By.xpath(baseXpath + "/div/div[2]/div/div/div")).getText());

		out.setAvatarUrl(accountelement.findElement(By.xpath(baseXpath + "/div/div[1]//img")).getAttribute("src"));
		try {
			out.setHeaderUrl(
					accountelement.findElement(By.xpath(baseXpath + "/a/div/div[2]//img")).getAttribute("src"));
		} catch (NoSuchElementException n) {
		}

		out.setFollowing(parser
				.parseStr(accountelement.findElement(By.xpath(baseXpath + "/div/div[last()]/div//span[1]")).getText()));
		out.setFollowers(parser.parseStr(
				accountelement.findElement(By.xpath(baseXpath + "/div/div[last()]/div[last()]//span[1]")).getText()));
		
		try {
			out.setBio(
					parser.parseTweet(accountelement.findElement(By.xpath(baseXpath + "/div/div[3]/div/div")).getText()));
		} catch(NoSuchElementException n) {
			out.setBio("");
		}
		
		out.setFetchDate(ZonedDateTime.now(ZoneId.of("UTC")).toString());

		return out;
	}

	public ArrayList<Tweet> getAccountTweets(String handle) throws IOException {
		String searchurl = this.twitterurl + "/search?q=from:" + handle + " -filter:replies&src=typed_query&f=live";
		ArrayList<Tweet> out = new ArrayList<Tweet>();

		driver.get(searchurl);

		WebElement searchelement = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("article")));
		List<WebElement> ses = searchelement.findElements(By.xpath("//article//time/..[@href]"));
		ArrayList<String> hrefs = new ArrayList<String>();

		for (WebElement i : ses) {
			hrefs.add(i.getAttribute("href"));
		}

		System.out.println("(0/" + hrefs.size() + ") got " + hrefs.size() + " tweet urls");

		for (int i = 0; i < hrefs.size(); i++) {
			out.add(this.getTweetFromURL(hrefs.get(i), true));
			System.out.println("(" + (i + 1) + "/" + hrefs.size() + ") " + hrefs.get(i) + " done");
		}

		System.out.println("done");

		return out;
	}

	public ArrayList<String[]> getAccountTweetLinks(String handle) throws IOException {
		String searchurl = this.twitterurl + "/search?q=from:" + handle + " -filter:replies&src=typed_query&f=live";
		ArrayList<String[]> out = new ArrayList<>();

		driver.get(searchurl);

		WebElement searchelement = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("article")));
		List<WebElement> ses = searchelement.findElements(By.xpath("//article//time/..[@href]"));

		for (WebElement a : ses) {
			String url = a.getAttribute("href");
			out.add(this.getInfoFromURL(url));
		}

		return out;
	}

	public void quit() {
		driver.quit();
	}
}
