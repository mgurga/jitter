package Scraper;

public class Parser {
	public int parseStr(String str) {
		String out = str;
		out = out.replace(",", "");
		out = out.replace(".", "");
		out = out.replace("K", "00");
		out = out.replace("M", "00000");
		return Integer.parseInt(out);
	}
	
	public String parseTweet(String raw) {
		raw = raw.replace("…", "");
		String[] rawparts = raw.split("\n");
		String out = "";
		
		if(rawparts.length == 1)
			return raw;
		
		out += rawparts[0];
		for(int i = 1; i < rawparts.length; i++) {
			if(rawparts[i].contains("@") || rawparts[i - 1].contains("@"))
				out += rawparts[i];
			else if(rawparts[i - 1].contains("#"))
				out += rawparts[i].replaceFirst(" ", "") + "\n";
			else if(i == rawparts.length - 1)
				out += rawparts[i];
			else
				out += rawparts[i] + "\n";
		}
		
		return out;
	}
}
