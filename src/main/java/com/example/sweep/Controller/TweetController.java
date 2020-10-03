package com.example.sweep.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.example.sweep.TwitterSweepApplication;
import com.example.sweep.Model.Filter;
import com.example.sweep.Model.SocketMessage;

@Controller
public class TweetController {
	
	@Autowired
	Filter filter;

	  @MessageMapping("/filter")
	  @SendTo("/twittersweep/tweets")
	  public SocketMessage getFilter(Filter filter) throws Exception {
		  
		  synchronized(this) {
			  this.filter.setUsernames(filter.getUsernames());
			  this.filter.setKeywords(filter.getKeywords());
			  TwitterSweepApplication.updateFilter();
		  
		  return new SocketMessage("SUCCESS");
		  }
	}

}