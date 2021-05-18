package xyz.mgurga.jitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private static final Logger log = LoggerFactory.getLogger(JitterApplication.class);
	static ArrayList<TAccount> defaultaccounts = new ArrayList<>();
	Scraper scraper = new Scraper();
	
	public static void main(String[] args) {
		SpringApplication.run(JitterApplication.class, args);
	}
	
	@PostConstruct
	public void setup() {
		if(tweetRepository == null)
			log.warn("tweet repository failed to autowire");
		if(taccountRepository == null)
			log.warn("taccount repository failed to autowire");
		
		String[] accs = {"Twitter", "Google", "Android"};
		for(int i = 0; i < accs.length; i++) {
			log.info("(" + (i + 1) + "/" + accs.length + ") loading default account: @" + accs[i]);
			defaultaccounts.add(getTAcc(accs[i]));
		}
		log.info("successfully loaded default accounts");
	}
	
	@GetMapping(value="/home")
	public String home(Model model) throws IOException {
		model.addAttribute("fas", defaultaccounts);
		ArrayList<Tweet> out = new ArrayList<>();
		for(TAccount acc : defaultaccounts) {
			out.addAll(getAccTwts(acc.getHandle()));
		}
		Collections.sort(out, Comparator.comparing(Tweet::getPostDate).reversed());
		model.addAttribute("tweets", out);
		return "timeline.html";
	}
	
	@GetMapping(value="/multi/{handles}")
	public String multi(@PathVariable String handles, Model model) throws IOException {
		model.addAttribute("fas", defaultaccounts);
		ArrayList<Tweet> out = new ArrayList<>();
		log.info(handles);
		String[] handlelist = handles.split(",");
		for(String acc : handlelist) {
			out.addAll(getAccTwts(acc));
		}
		Collections.sort(out, Comparator.comparing(Tweet::getPostDate).reversed());
		model.addAttribute("tweets", out);
		return "timeline.html";
	}
	
	@GetMapping(value="/{handle}")
	public String account(@PathVariable String handle, Model model) throws IOException {
		model.addAttribute("fas", defaultaccounts);
		model.addAttribute("account", getTAcc(handle));
		model.addAttribute("acctweets", getAccTwts(handle));
		return "account.html";
	}
	
	@GetMapping(value="/{handle}/status/{id}")
	public String singleTweet(@PathVariable String handle, @PathVariable String id, Model model) throws IOException {
		model.addAttribute("fas", defaultaccounts);
		model.addAttribute("tweet", getTwt(handle, id));
		return "tweet.html";
	}
	
	public Tweet getTwt(String handle, String id) throws IOException {
		if(!(tweetRepository == null)) {
			for (Tweet existingTweet : tweetRepository.findAll())
				if(existingTweet.getHandle().equals(handle) && existingTweet.getId().equals(id)) {
					existingTweet.setAuthor(getTAcc(handle));
					return existingTweet;
				}
			
			Tweet out = scraper.getTweet(handle, id);
			out.setContent(out.getContent().replace("\n", "<br />"));
			tweetRepository.save(out);
			taccountRepository.save(out.getAuthor());
			return out;
		} else {
			Tweet out = scraper.getTweet(handle, id);
			out.setContent(out.getContent().replace("\n", "<br />"));
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
	
	public ArrayList<Tweet> getAccTwts(String handle) throws IOException {
		if(!(tweetRepository == null)) {
			ArrayList<Tweet> out = new ArrayList<>();
			ArrayList<String[]> tweetsinfo = scraper.getAccountTweetLinks(handle);
			log.info("loading " + tweetsinfo.size() + " tweets from @" + handle);
			for(String[] i : tweetsinfo) {
				out.add(getTwt(i[0], i[1]));
			}
			return out;
		} else {
			ArrayList<Tweet> out = scraper.getAccountTweets(handle);
			return out;
		}
	}
}
