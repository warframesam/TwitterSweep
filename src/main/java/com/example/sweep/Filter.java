package com.example.sweep;

import java.util.Arrays;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "singleton")
public class Filter {
	
	private String usernames[];
	private String keywords[];
	
	public Filter() {
	}

	public Filter(String[] usernames, String[] keywords) {
		this.usernames = usernames;
		this.keywords = keywords;
	}

	public String[] getUsernames() {
		return usernames;
	}

	public void setUsernames(String[] usernames) {
		this.usernames = usernames;
	}

	public String[] getKeywords() {
		return keywords;
	}

	public void setKeywords(String[] keywords) {
		this.keywords = keywords;
	}

	@Override
	public String toString() {
		return "Filter [usernames=" + Arrays.toString(usernames) + ", keywords=" + Arrays.toString(keywords) + "]";
	}
	
}
