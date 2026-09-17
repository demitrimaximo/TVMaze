package com.api.tvmaze.domain.exception;

public class ShowNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ShowNotFoundException(Long showId) {
		super("No se encontro show con el siguiente id: " + showId);
		
	}
}
