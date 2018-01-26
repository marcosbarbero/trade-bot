package com.marcosbarbero.bux.tradebot.config.listener;

import com.marcosbarbero.bux.tradebot.config.TradeBotProperties;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;

import java.net.URI;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Marcos Barbero
 */
@Slf4j
//@Configuration
@RequiredArgsConstructor
public class StartUpListener implements InitializingBean {

    private final WebSocketConnectionManager connectionManager;

    private final TradeBotProperties tradeBotProperties;

    private final WebSocketClient client;

    private final WebSocketHandler webSocketHandler;

    private URI uri;

    @EventListener
    public void handleContextStarted(final ContextRefreshedEvent event) {

//        wsConnect();
    }

    // TODO: Add max-retry in the startup
    private void wsConnect() {
//        while (!isServerUp()) {
            while (!this.connectionManager.isRunning()) {
                this.connectionManager.start();
            }
//            this.connectionManager
//        }
    }

    private boolean isServerUp() {
        ListenableFuture<WebSocketSession> future =
                this.client.doHandshake(this.webSocketHandler,
                        new WebSocketHttpHeaders(this.connectionManager.getHeaders()), this.uri);

        future.addCallback(new ListenableFutureCallback<WebSocketSession>() {
            @Override
            public void onSuccess(WebSocketSession result) {
//                webSocketSession = result;
//                logger.info("Successfully connected");
            }
            @Override
            public void onFailure(Throwable ex) {
//                logger.error("Failed to connect", ex);
            }
        });
        return false;
    }

    //TODO: fix that replaceAll
    @Override
    public void afterPropertiesSet() throws Exception {
        String subscription = this.tradeBotProperties.getEndpoints().getSubscription();
        subscription = subscription.replaceAll("ws://", "http://");
        this.uri = URI.create(subscription);
    }
}
