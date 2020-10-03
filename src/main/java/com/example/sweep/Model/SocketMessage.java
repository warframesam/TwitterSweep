package com.example.sweep.Model;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "singleton")
public class SocketMessage {
	
	private String text;

	public SocketMessage(String text) {
		this.text = text;
	}

	public SocketMessage() {
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
