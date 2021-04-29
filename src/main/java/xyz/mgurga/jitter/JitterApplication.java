package xyz.mgurga.jitter;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import Scraper.Scraper;
import xyz.mgurga.jitter.utils.TAccount;

@SpringBootApplication
@Controller
public class JitterApplication {
	
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
	public String account(@PathVariable String handle, Model model) {
		model.addAttribute("fas", defaultaccounts);
		model.addAttribute("account", scraper.getAccountInfo(handle));
		return "account.html";
	}
	
	@GetMapping(value="/{handle}/status/{id}")
	public String singleTweet(@PathVariable String handle, @PathVariable String id, Model model) throws IOException {
		model.addAttribute("fas", defaultaccounts);
		model.addAttribute("tweet", scraper.getTweet(handle, id));
		return "tweet.html";
	}

}
