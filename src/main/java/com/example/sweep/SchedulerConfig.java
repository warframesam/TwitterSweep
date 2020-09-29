package com.example.sweep;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@ConditionalOnProperty(value = "scheduling.enable", havingValue = "true", matchIfMissing = true)
@Configuration
@EnableScheduling
public class SchedulerConfig {
	
	@Autowired
	Tweet tweet;
	
	@Autowired
	SimpMessagingTemplate simpMessagingTemplate;
	
	private String previousText = ""; 

	@Scheduled(fixedDelay = 500)
	public void scheduledMessage() throws Exception{
		if(tweet.getData()!=null && !tweet.getData().equals("")) {
			
			ObjectMapper objectMapper = new ObjectMapper();
			
			JsonNode rootNode = objectMapper.readTree(tweet.getData());
			JsonNode text = rootNode.path("data").path("text");
			
			//System.out.println("****TWEET****" + text);
			if(!text.asText().equals("") && !previousText.equals(text.asText())) {
				simpMessagingTemplate.convertAndSend("/twittersweep/tweets",new SocketMessage(text.asText()));
				previousText = text.asText();
			}
		}
	} 
}
