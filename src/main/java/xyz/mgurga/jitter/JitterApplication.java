package xyz.mgurga.jitter;

import java.util.ArrayList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import xyz.mgurga.jitter.utils.TAccount;

@SpringBootApplication
@Controller
public class JitterApplication {
	
	static ArrayList<TAccount> defaultaccounts = new ArrayList<TAccount>();
	
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
		return "home.html";
	}

}
