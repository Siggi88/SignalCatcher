package io.siggi.signalcatcher;

public class SignalCatcherException extends RuntimeException {

	public SignalCatcherException() {
		super();
	}

	public SignalCatcherException(String message) {
		super(message);
	}

	public SignalCatcherException(Throwable cause) {
		super(cause);
	}

	public SignalCatcherException(String message, Throwable cause) {
		super(message, cause);
	}
}
