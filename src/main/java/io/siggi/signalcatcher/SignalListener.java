package io.siggi.signalcatcher;

@FunctionalInterface
public interface SignalListener {

	/**
	 * Called when a signal was received. Do not block in this method.
	 * @param signal The signal that was received.
	 */
	public void receivedSignal(int signal);
}
