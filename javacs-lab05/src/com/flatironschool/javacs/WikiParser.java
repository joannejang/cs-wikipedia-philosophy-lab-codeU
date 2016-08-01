package com.flatironschool.javacs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import java.util.StringTokenizer;

public class WikiParser {

	static int parentheses = 0;
	static Elements paragraphs;

	public WikiParser(Elements paragraphs) {
		this.paragraphs = paragraphs;

	}

	public static Element getFirstUrlFromPage() {
		for (Element paragraph : paragraphs) {
			Element firstLink = getFirstUrlFromParagraph(paragraph);
			if (firstLink != null) {
				return firstLink;
			}

		}
		return null;
	}

	private static Element getFirstUrlFromParagraph(Node paragraph) {
		Iterable<Node> nodes = new WikiNodeIterable(paragraph);
		for (Node node : nodes) {
			if (node instanceof TextNode) {
				extractFromTextNode((TextNode) node);
			} else if (node instanceof Element) {
				Element firstLink = extractFromElement((Element) node);
				if (firstLink != null) return firstLink;
			}
		}
		return null;
	}

	private static void extractFromTextNode(TextNode tn) {
		StringTokenizer st = new StringTokenizer(tn.text(), " ()", true);
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (token.equals("(")) {
				parentheses++;
			} else if (token.equals(")")) {
				if (parentheses < 1) {
					System.err.println("Unbalanced parenthesis.");
				} else {
					parentheses--;
				}
			}
		}
	}

	private static Element extractFromElement(Element node) {
		if (!node.tagName().equals("a") || isItalic(node) || isInParentheses(node)) {
			return null;
		} else {
			return node;
		}

	}

	private static boolean isItalic(Element node) {
		Element elem = node;
		while (elem != null) {
			if (elem.tagName().equals("i") || elem.tagName().equals("em")) {
				return true;
			}
			elem = elem.parent();
		}
		return false;
	}

	private static boolean isInParentheses(Element node) {
		return parentheses > 0;
	}


}