package com.flatironschool.javacs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import org.jsoup.select.Elements;

public class WikiPhilosophy {
	
	final static WikiFetcher wf = new WikiFetcher();
	
	/**
	 * Tests a conjecture about Wikipedia and Philosophy.
	 * 
	 * https://en.wikipedia.org/wiki/Wikipedia:Getting_to_Philosophy
	 * 
	 * 1. Clicking on the first non-parenthesized, non-italicized link
     * 2. Ignoring external links, links to the current page, or red links
     * 3. Stopping when reaching "Philosophy", a page with no links or a page
     *    that does not exist, or when a loop occurs
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		
		String philosophyUrl = "https://en.wikipedia.org/wiki/Philosophy";
		String url = "https://en.wikipedia.org/wiki/Java_(programming_language)";

		//keep track of visited links
		ArrayList<String> visitedUrls = new ArrayList<String>();


		while (!url.equals(philosophyUrl)) { //conditions to stop simulation
			if (visitedUrls.contains(url)) {
				System.out.println("Error: Loop");
				System.exit(0);
			}
			//add current link
			visitedUrls.add(url);

			Elements paragraphs = wf.fetchWikipedia(url);
			Element firstPara = paragraphs.get(0);
			
			//keep track of parenetheses
			int parenthesisCount = 0;

			//find first link
			Iterable<Node> iter = new WikiNodeIterable(firstPara);
			for (Node node: iter) {
				//System.out.println(node);
				parenthesisCount += countParenthesis(node.toString());
				if (isValid(node) && parenthesisCount == 0) {
					url = node.absUrl("href");
					break;
				}
	        }
	    }
	    System.out.println("Success");
	}

	public static boolean isValid(Node node) {
		if (node instanceof Element && node.hasAttr("href")) {
			Element elementNode = (Element) node; //typecast node to element to use element functions
			if (!elementNode.parent().tagName().contains("sup")) {
				return true;
			}
		}
		return false;
	}

	public static int countParenthesis(String s) {
		int counter = 0;
		for (int i=0; i<s.length(); i++) {
    		if(s.charAt(i) == '(') {
        		counter++;
   			} 
   			else if (s.charAt(i) == ')') {
   				counter--;
   			}
		}
		return counter;
	}
}
