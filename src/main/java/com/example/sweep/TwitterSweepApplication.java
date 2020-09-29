package com.example.sweep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class TwitterSweepApplication {
	
	public static ConfigurableApplicationContext context;
	
	private static TwitterConfig twitterConfig;
	private static Filter filter;
	private static WebClient webClient;
	
	private static Tweet tweet;
	
	private static String activeRules;
	
	public static void main(String[] args) throws Exception{
		TwitterSweepApplication.context = SpringApplication.run(TwitterSweepApplication.class, args);
		TwitterSweepApplication.init();
	}
	
	public static void init() throws Exception {

		TwitterSweepApplication.twitterConfig = context.getBean(TwitterConfig.class);
		TwitterSweepApplication.filter = context.getBean(Filter.class);
		TwitterSweepApplication.tweet = context.getBean(Tweet.class);
		
		TwitterSweepApplication.initializeWebClient();
		TwitterSweepApplication.startStream();
	}
	
	public static void initializeWebClient() throws Exception {
		
		String url = "https://api.twitter.com/2/tweets/search/stream";
		
		TwitterSweepApplication.webClient = WebClient.builder()
				.baseUrl(url)
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.ALL_VALUE)
				.defaultHeader("Authorization","Bearer " + twitterConfig.getBearerToken())
				.build();
	}
	
	public static void startStream() throws Exception{
		try {
			Flux<String> tweetStream = TwitterSweepApplication.webClient
			.get()
			.retrieve()
			.bodyToFlux(String.class);
			
			tweetStream.subscribe(t -> {
				//System.out.println("****RECIEVED****" + t);
				TwitterSweepApplication.tweet.setData(t);
				});
		}
		catch(Exception e) {
			System.out.println("Twitter API CONNECT FAILURE:" + e.getMessage());
		}
	}
	
	public static void getActiveRules() throws Exception {
		
		try {
		String responseActiveRules = TwitterSweepApplication.webClient
				.get()
				.uri("/rules")
				.retrieve()
				.bodyToMono(String.class)
				.block();
		
			TwitterSweepApplication.activeRules = responseActiveRules;
			System.out.println("\n****ACTIVE RULES****");
			System.out.println(activeRules);
		}
		catch(Exception e) {
			System.out.println("Twitter API CONNECT FAILURE:" + e.getMessage());
		}
			
	}
	
	public static void deleteActiveRules() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		
		JsonNode rootRulesJsonNode = objectMapper.readTree(TwitterSweepApplication.activeRules);
		JsonNode dataJsonNode = rootRulesJsonNode.path("data");
		
		ObjectNode rootIdsObjectNode = objectMapper.createObjectNode();
		ObjectNode deleteObjectNode = objectMapper.createObjectNode();
		ArrayNode idsArrayNode = objectMapper.createArrayNode();
		
		if(!dataJsonNode.isEmpty()) {
			
			for(JsonNode rule : dataJsonNode) {
				idsArrayNode.add(rule.path("id"));
			}
			
			deleteObjectNode.putPOJO("ids", idsArrayNode);
			rootIdsObjectNode.putPOJO("delete", deleteObjectNode);
			
			System.out.println("\n****DELETE IDs****");
			System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootIdsObjectNode));
			
			try {
			String responseDeletedRules = TwitterSweepApplication.webClient
					.post()
					.uri("/rules")
					.contentType(MediaType.APPLICATION_JSON)
					.bodyValue(rootIdsObjectNode)
					.retrieve()
					.bodyToMono(String.class)
					.block();
			
			System.out.println("\n****DELETED RULES****");
			System.out.println(responseDeletedRules);
			}
			catch(Exception e) {
				System.out.println("Twitter API CONNECT FAILURE:" + e.getMessage());
			}
		}
		else
			System.out.println("\n****NO ACTIVE RULES TO DELETE****");
	}
	
	public static void updateFilter() throws Exception{
		
		System.out.println("\n----------------INITIATED----------------");
		getActiveRules();
		deleteActiveRules();
		
		if(filter.getUsernames().length!=0 || filter.getKeywords().length!=0) {
			  
			  StringBuffer usernameBuffer = new StringBuffer();
			  for(String username : filter.getUsernames()) {
				  usernameBuffer.append("from:" + username);
				  if(username != filter.getUsernames()[filter.getUsernames().length-1])
					  usernameBuffer.append(" OR ");
			  }
			  
			  StringBuffer keywordBuffer = new StringBuffer();
			  for(String keyword : filter.getKeywords()) {
				  keywordBuffer.append(keyword);
				  if(keyword != filter.getKeywords()[filter.getKeywords().length-1])
					  keywordBuffer.append(" OR ");
			  }
			
			ObjectMapper objectMapper = new ObjectMapper();
			
			ObjectNode rootRulesObjectNode = objectMapper.createObjectNode();
			ArrayNode addArrayNode = objectMapper.createArrayNode();
			
			ObjectNode usernameRulesObjectNode = objectMapper.createObjectNode();
			ObjectNode keywordRulesObjectNode = objectMapper.createObjectNode();
			
			usernameRulesObjectNode.put("value", usernameBuffer.toString());
			usernameRulesObjectNode.put("tag", "usernames");
			
			keywordRulesObjectNode.put("value", keywordBuffer.toString());
			keywordRulesObjectNode.put("tag", "keywords");
			
			
			if(usernameBuffer.length()!=0)
				addArrayNode.add(usernameRulesObjectNode);
			if(keywordBuffer.length()!=0)
				addArrayNode.add(keywordRulesObjectNode);
			
			rootRulesObjectNode.putPOJO("add",addArrayNode);
			
			System.out.println("\n****ADD RULES****");
			System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootRulesObjectNode));
			
			try {
			String responseConfiguredRules= TwitterSweepApplication.webClient
			.post()
			.uri("/rules")
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(rootRulesObjectNode)
			.retrieve()
			.bodyToMono(String.class)
			.block();
			
			System.out.println("\n****CONFIGURED RULES****");
			System.out.println(responseConfiguredRules);
			}
			catch(Exception e) {
				System.out.println("Twitter API CONNECT FAILURE:" + e.getMessage());
			}
		}
		else 
			System.out.println("\n****FILTER EMPTY****");
	}
	
}
