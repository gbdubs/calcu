package calculus.utilities;

import org.jsoup.*;
import org.jsoup.safety.Whitelist;

public class Cleaner {

	// ACCEPTS:
	//    - a, b, blockquote, br, cite, code, dd, dl, dt, em, i, li, ol, p, pre, q, small, span, strike, strong, sub, sup, u, ul
	//    - Image Tags with http:// or https:// src elements
	
	public static String cleanHtml(String original){
		
		String result = Jsoup.clean(original, Whitelist.basicWithImages());
		return result;
	}
	
}
