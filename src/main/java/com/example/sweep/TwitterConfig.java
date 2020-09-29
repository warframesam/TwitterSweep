package com.example.sweep;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "singleton")
public class TwitterConfig {

	private String bearerToken;

	public TwitterConfig(@Value("${twitter.authorization.bearer}")String bearerToken) {
		this.bearerToken = bearerToken;
	}

	public String getBearerToken() {
		return bearerToken;
	}

}

