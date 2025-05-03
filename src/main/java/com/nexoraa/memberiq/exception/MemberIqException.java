package com.nexoraa.memberiq.exception;

import java.io.Serial;

public class MemberIqException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = 1L;

	public MemberIqException() {
		super();
	}

	public MemberIqException(String message) {
		super(message);
	}

	public MemberIqException(String message, Throwable cause) {
		super(message, cause);
	}
}