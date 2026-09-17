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
	
	 @Value("${tvmaze.base-url}")
	 private String baseUrl;

	 @Bean
	    public WebClient tvMazeWebClient() {
	        HttpClient httpClient = HttpClient.create()
	                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5_000)
	                .responseTimeout(Duration.ofSeconds(10))
	                .doOnConnected(conn -> conn
	                        .addHandlerLast(new ReadTimeoutHandler(10, TimeUnit.SECONDS)));

	        return WebClient.builder()
	                .baseUrl(baseUrl)
	                .clientConnector(new ReactorClientHttpConnector(httpClient))
	                .build();
	    }
}
