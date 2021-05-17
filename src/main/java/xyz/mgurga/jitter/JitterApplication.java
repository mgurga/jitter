package xyz.mgurga.jitter;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import Scraper.Scraper;
import xyz.mgurga.jitter.database.TAccountRepository;
import xyz.mgurga.jitter.database.TweetRepository;
import xyz.mgurga.jitter.utils.TAccount;
import xyz.mgurga.jitter.utils.Tweet;

@SpringBootApplication
@Controller
public class JitterApplication {
	@Autowired
	TweetRepository tweetRepository;
	@Autowired
	TAccountRepository taccountRepository;
	
	static ArrayList<TAccount> defaultaccounts = new ArrayList<TAccount>();
	Scraper scraper = new Scraper();
	
	public static void main(String[] args) {
		defaultaccounts.add(new TAccount("Twitter"));
		defaultaccounts.add(new TAccount("Google"));
		defaultaccounts.add(new TAccount("Android"));
		SpringApplication.run(JitterApplication.class, args);
	}
	
	@GetMapping(value="/home")
	public String home(Model model) {
		model.addAttribute("fas", defaultaccounts);
		return "home.html";
	}
	
	@GetMapping(value="/{handle}")
	public String account(@PathVariable String handle, Model model) throws IOException {
		model.addAttribute("fas", defaultaccounts);
		model.addAttribute("account", scraper.getAccountInfo(handle));
		model.addAttribute("acctweets", scraper.getAccountTweets(handle));
		return "account.html";
	}
	
	@GetMapping(value="/{handle}/status/{id}")
	public String singleTweet(@PathVariable String handle, @PathVariable String id, Model model) throws IOException {
		model.addAttribute("fas", defaultaccounts);
		model.addAttribute("tweet", getTweet(handle, id));
		return "tweet.html";
	}
	
	public Tweet getTweet(String handle, String id) throws IOException {
		if(!(tweetRepository == null)) {
			for (Tweet existingTweet : tweetRepository.findAll())
				if(existingTweet.getHandle().equals(handle) && existingTweet.getId().equals(id)) {
					System.out.println("loaded cached tweet");
					existingTweet.setAuthor(getTAcc(handle));
					return existingTweet;
				}
			
			Tweet out = scraper.getTweet(handle, id);
			tweetRepository.save(out);
			taccountRepository.save(out.getAuthor());
			return out;
		} else {
			Tweet out = scraper.getTweet(handle, id);
			return out;
		}
	}
	
	public TAccount getTAcc(String handle) {
		if(!(taccountRepository == null)) {
			for (TAccount existingTAcc : taccountRepository.findAll())
				if(existingTAcc.getHandle().equals(handle))
					return existingTAcc;
			
			TAccount out = scraper.getAccountInfo(handle);
			taccountRepository.save(out);
			return out;
		} else {
			TAccount out = scraper.getAccountInfo(handle);
			return out;
		}
	}
}
