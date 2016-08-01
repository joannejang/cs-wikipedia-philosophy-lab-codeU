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
	final static List<String> visited = new ArrayList<String>();
	final static String philosophy = "https://en.wikipedia.org/wiki/Philosophy";
	
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

		String start = "https://en.wikipedia.org/wiki/Java_(programming_language)";
		tryGettingToDestination(start, philosophy, 10);

	}

	public static void tryGettingToDestination(String start, String dest, int maxTries) throws IOException {
		String curr = start;
		for (int i = 0; i < maxTries; i++) {
			if (visited.contains(curr)) {
				System.err.println("Loop detected.");
				return;
			} else {
				visited.add(curr);
			}

			Element element = getFirstUrlFromPage(curr);
			if (element == null) {
				System.err.println("Dead-end detected.");
				return;
			}
			System.out.println("- " + element.text());
			curr = element.attr("abs:href");

			if (curr.equals(dest)) {
				System.out.println("Reached Philosophy!");
				break;
			}
		}

	}

	public static Element getFirstUrlFromPage(String url) throws IOException {
		Elements paragraphs = wf.fetchWikipedia(url);
		WikiParser wp = new WikiParser(paragraphs);
		Element element = wp.getFirstUrlFromPage();
		return element;
	}
		
}
