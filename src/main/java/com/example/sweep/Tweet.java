package com.example.sweep;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "singleton")
public class Tweet {
	
	private String data;

	public Tweet(String data) {
		this.data = data;
	}

	public Tweet() {
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Tweet [data=" + data + "]";
	}

}
