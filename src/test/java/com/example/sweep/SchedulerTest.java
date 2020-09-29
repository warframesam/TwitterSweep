package com.example.sweep;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

@TestPropertySource(properties = "scheduling.enable=true")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SchedulerTest {

	@LocalServerPort
	private int port;
	
	@Autowired
	private ConfigurableApplicationContext context;

	private SockJsClient sockJsClient;

	private WebSocketStompClient stompClient;

	private final WebSocketHttpHeaders headers = new WebSocketHttpHeaders();

	@BeforeEach
	public void setup() throws Exception {
		List<Transport> transports = new ArrayList<>();
		transports.add(new WebSocketTransport(new StandardWebSocketClient()));
		this.sockJsClient = new SockJsClient(transports);

		this.stompClient = new WebSocketStompClient(sockJsClient);
		this.stompClient.setMessageConverter(new MappingJackson2MessageConverter());
		
		TwitterSweepApplication.context = this.context;
		
		TwitterSweepApplication.init();
		TwitterSweepApplication.initializeWebClient();
		TwitterSweepApplication.startStream();
	}

	@Test
	public void getScheduledSocketMessage() throws Exception {

		final CountDownLatch latch = new CountDownLatch(1);
		final AtomicReference<Throwable> failure = new AtomicReference<>();

		StompSessionHandler handler = new TestSessionHandler(failure) {

			@Override
			public void afterConnected(final StompSession session, StompHeaders connectedHeaders) {
				session.subscribe("/twittersweep/tweets", new StompFrameHandler() {
					@Override
					public Type getPayloadType(StompHeaders headers) {
						return SocketMessage.class;
					}

					@Override
					public void handleFrame(StompHeaders headers, Object payload) {
						SocketMessage responseMessage = (SocketMessage)payload;
						try {
							System.out.println("\n\n********SOCKET MESSAGE******** : " + responseMessage.getText());
							System.out.println("\n\n********ASSERTION******** : " + !responseMessage.getText().equals(null) + "\n\n");
							assertNotNull(responseMessage.getText());
						} catch (Throwable t) {
							failure.set(t);
						} finally {
							session.disconnect();
							latch.countDown();
						}
					}
				});
				
			}
		};

		this.stompClient.connect("ws://localhost:{port}/twittersweep-websocket", this.headers, handler, this.port);

		if (latch.await(20, TimeUnit.SECONDS)) {
			if (failure.get() != null) {
				throw new AssertionError("", failure.get());
			}
		}
		else {
			fail("Socket Message not received");
		}

	}

	private class TestSessionHandler extends StompSessionHandlerAdapter {

		private final AtomicReference<Throwable> failure;

		public TestSessionHandler(AtomicReference<Throwable> failure) {
			this.failure = failure;
		}

		@Override
		public void handleFrame(StompHeaders headers, Object payload) {
			this.failure.set(new Exception(headers.toString()));
		}

		@Override
		public void handleException(StompSession s, StompCommand c, StompHeaders h, byte[] p, Throwable ex) {
			this.failure.set(ex);
		}

		@Override
		public void handleTransportError(StompSession session, Throwable ex) {
			this.failure.set(ex);
		}
	}
}