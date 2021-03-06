package xyz.mgurga.jitter;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import Scraper.Scraper;
import xyz.mgurga.jitter.utils.TAccount;
import xyz.mgurga.jitter.utils.Tweet;

class ScraperTests {
	static Scraper scraper = new Scraper();

	@Tag("Tweet")
	@Test
	void simpleTweet() throws IOException {
		Tweet result = scraper.getTweetFromURL("https://twitter.com/Twitter/status/1375500518366216199", false);
		
		assertEquals("subtweeting is an art", result.getContent());
		assertEquals("Twitter Web App", result.getDevice());
		assertEquals("2021-03-26T17:30Z[UTC]", result.getPostDate().toString());
		assertTrue(result.getRetweets() > 7000);
		assertTrue(result.getLikes() >= 78000);
	}
	
	@Tag("Tweet")
	@Test
	void simpleTweetWithLink() throws IOException {
		Tweet result = scraper.getTweetFromURL("https://twitter.com/Twitter/status/508964519872176129", false);
		
		assertEquals("A new way for you to discover and buy products on Twitter:", result.getContent());
		assertEquals("TweetDeck", result.getDevice());
		assertEquals("2014-09-08T13:06Z[UTC]", result.getPostDate().toString());
		assertTrue(result.getRetweets() > 920);
		assertTrue(result.getLikes() > 660);
	}
	
	@Tag("Tweet")
	@Test
	void simpleTweetWithImage() throws IOException {
		Tweet result = scraper.getTweetFromURL("https://twitter.com/Twitter/status/673882166980284416", false);
		
		assertEquals("Starting today, we’re introducing a richer photo experience on https://twitter.com/: "
				+ "https://blog.twitter.com/2015/a-new-look-for-your-twittercom-photos", result.getContent());
		assertEquals("Twitter Web Client", result.getDevice());
		assertEquals("2015-12-07T15:10Z[UTC]", result.getPostDate().toString());
		assertTrue(result.getRetweets() > 1300);
		assertTrue(result.getLikes() > 2160);
	}
	
	@Tag("Tweet")
	@Test
	void multilineTweet() throws IOException {
		Tweet result = scraper.getTweetFromURL("https://twitter.com/TwitterDev/status/1370110639545417729", false);
		
		assertEquals("On Tuesday at 12 pm EST, @jessicagarson will cover how to start working with the data returned from v2 of the #twitterapi \n"
				+ "\n"
				+ "She'll be focusing on how to work with nested JSON and sending your data to a CSV. \n"
				+ "\n"
				+ "Join us on Twitch!", result.getContent());
		assertEquals("Twitter Web App", result.getDevice());
		assertEquals("2021-03-11T20:33Z[UTC]", result.getPostDate().toString());
		assertTrue(result.getLikes() > 40);
		assertTrue(result.getRetweets() > 10);
	}
	
	@Tag("Tweet")
	@Test
	void multilineRetweet() throws IOException {
		Tweet result = scraper.getTweetFromURL("https://twitter.com/TwitterDev/status/1371363033352638467", false);
		
		assertEquals("If you're using v2 full-archive search for Academic Research, you won't want to miss this livestream hosted by our research dev advocate @suhemparack.\n"
				+ "\n"
				+ "Mark your calendars to join us on http://twitch.tv/twitterdev Thursday March 18th, 10AM PT/ 1PM ET.", result.getContent());
		assertEquals("Twitter Web App", result.getDevice());
		assertEquals("2021-04-23T04:00Z[UTC]", result.getPostDate());
		assertTrue(result.getLikes() > 30);
		assertTrue(result.getRetweets() > 7);
	}
	
	@Tag("Tweet")
	@Test
	void imageTweet() throws IOException {
		Tweet result = scraper.getTweetFromURL("https://twitter.com/GoogleDoodles/status/1385443314523115520", false);
		
		assertEquals("Today’s #GoogleDoodle celebrates Ñ—not only a letter but a representation of Hispanic heritage.\n"
				+ "\n"
				+ "#Didyouknow? To save time & shorten words, 12th-century scribes wrote a tiny \"n\" (a tilde or virgulilla) on top to create Ñ ✍️\n"
				+ "\n"
				+ "🎨 by guest artist @min_wins→ http://goo.gle/3uLtSmD", result.getContent());
		assertEquals("Sprinklr", result.getDevice());
		assertEquals("2021-04-23T04:00Z[UTC]", result.getPostDate().toString());
		assertEquals(result.getImageurls().size(), 1);
		assertTrue(result.getImageurls().get(0).contains("EzoVWh8WQAQE7ir"));
		assertTrue(result.getRetweets() > 170);
		assertTrue(result.getLikes() > 800);
	}
	
	@Tag("Account")
	@Test
	void TwitterAccount() throws IOException {
		TAccount result = scraper.getAccountInfo("Twitter");
		
		assertEquals("Twitter", result.getNickname());
		assertEquals("Twitter", result.getHandle());
		assertEquals("https://pbs.twimg.com/profile_images/1354479643882004483/Btnfm47p_200x200.jpg", result.getAvatarUrl());
		assertEquals("What's happening?!", result.getBio());
		assertTrue(result.getFollowers() > 59000000);
		assertTrue(result.getFollowing() > 30);
	}
	
	@Tag("Account")
	@Test
	void GoogleAccount() throws IOException {
		TAccount result = scraper.getAccountInfo("Google");
		
		assertEquals("Google", result.getNickname());
		assertEquals("Google", result.getHandle());
		assertEquals("https://pbs.twimg.com/profile_images/1343584679664873479/Xos3xQfk_200x200.jpg", result.getAvatarUrl());
		assertEquals("#HeyGoogle", result.getBio());
		assertTrue(result.getFollowers() >= 23000000);
		assertTrue(result.getFollowing() > 200);
	}
	
	@Tag("Account")
	@Test
	void AndroidAccount() throws IOException {
		TAccount result = scraper.getAccountInfo("Android");
		
		assertEquals("Android", result.getNickname());
		assertEquals("Android", result.getHandle());
		assertEquals("https://pbs.twimg.com/profile_images/1164525925242986497/N5_DCXYQ_200x200.jpg", result.getAvatarUrl());
		assertEquals("#Android is made for everyone. Follow along for the latest updates and stories behind our tech.", result.getBio());
		assertTrue(result.getFollowers() >= 10000000);
		assertTrue(result.getFollowing() > 100);
	}
	
	@AfterAll
	static void cleanup() throws IOException {
		scraper.quit();
	}
}
