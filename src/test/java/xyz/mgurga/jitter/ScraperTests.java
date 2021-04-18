package xyz.mgurga.jitter;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import Scraper.Scraper;
import xyz.mgurga.jitter.utils.Tweet;

class ScraperTests {
	Scraper scraper = new Scraper();

	@Test
	void simpleTweet() throws IOException {
		Tweet result = scraper.getTweetFromURL("https://twitter.com/Twitter/status/1375500518366216199");
		
		assertEquals("subtweeting is an art", result.getContent());
		assertTrue(result.getRetweets() > 7500);
		assertTrue(result.getLikes() >= 78400);
	}
	
	@Test
	void multilineTweet() throws IOException {
		Tweet result = scraper.getTweetFromURL("https://twitter.com/TwitterDev/status/1370110639545417729");
		
		assertEquals("On Tuesday at 12 pm EST, @jessicagarson will cover how to start working with the data returned from v2 of the #twitterapi \n"
				+ "\n"
				+ "She'll be focusing on how to work with nested JSON and sending your data to a CSV. \n"
				+ "\n"
				+ "Join us on Twitch!", result.getContent());
		assertTrue(result.getLikes() > 40);
		assertTrue(result.getRetweets() > 10);
	}

}
