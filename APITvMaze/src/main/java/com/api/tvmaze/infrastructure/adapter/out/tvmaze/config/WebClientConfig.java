package com.api.tvmaze.infrastructure.adapter.out.tvmaze.config;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {
	
    private final String baseUrl;
    private final int connectTimeoutMs;
    private final int readTimeoutSeconds;
    
    public WebClientConfig(
            @Value("${tvmaze.base-url}") String baseUrl,
            @Value("${tvmaze.timeout.connect-ms:5000}") int connectTimeoutMs,
            @Value("${tvmaze.timeout.read-seconds:10}") int readTimeoutSeconds) {
        this.baseUrl = baseUrl;
        this.connectTimeoutMs = connectTimeoutMs;
        this.readTimeoutSeconds = readTimeoutSeconds;
    }

	 @Bean
	 public WebClient tvMazeWebClient() {
	        HttpClient httpClient = HttpClient.create()
	                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeoutMs)
	                .responseTimeout(Duration.ofSeconds(readTimeoutSeconds))
	                .doOnConnected(conn -> conn.
	                		addHandlerLast(new ReadTimeoutHandler(readTimeoutSeconds, TimeUnit.SECONDS)));

	        return WebClient.builder()
	                .baseUrl(baseUrl)
	                .clientConnector(new ReactorClientHttpConnector(httpClient))
	                .build();
	    }
}
