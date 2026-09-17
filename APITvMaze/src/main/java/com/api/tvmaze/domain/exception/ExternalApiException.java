package com.api.tvmaze.domain.exception;

public class ExternalApiException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExternalApiException(String menssage, Throwable cause) {
		super(menssage, cause);
	}
}
