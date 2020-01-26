package io.siggi.signalcatcher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Catch POSIX signals on 64 bit Linux and Mac OS X.
 * <p>
 * SIGKILL, SIGSTOP, SIGCONT cannot be caught. Some signals are only available
 * on some operating systems.
 * <p>
 * Mac-only signals:
 * <ul>
 * <li>SIGEMT</li>
 * <li>SIGINFO</li>
 * </ul>
 * <p>
 * Linux-only signals:
 * <ul>
 * <li>SIGSTKFLT</li>
 * <li>SIGPWR</li>
 * <li>SIGRTMIN</li>
 * <li>SIGRTMINp1</li>
 * <li>SIGRTMINp2</li>
 * <li>SIGRTMINp3</li>
 * <li>SIGRTMINp4</li>
 * <li>SIGRTMINp5</li>
 * <li>SIGRTMINp6</li>
 * <li>SIGRTMINp7</li>
 * <li>SIGRTMINp8</li>
 * <li>SIGRTMINp9</li>
 * <li>SIGRTMINp10</li>
 * <li>SIGRTMINp11</li>
 * <li>SIGRTMINp12</li>
 * <li>SIGRTMINp13</li>
 * <li>SIGRTMINp14</li>
 * <li>SIGRTMINp15</li>
 * <li>SIGRTMAXm14</li>
 * <li>SIGRTMAXm13</li>
 * <li>SIGRTMAXm12</li>
 * <li>SIGRTMAXm11</li>
 * <li>SIGRTMAXm10</li>
 * <li>SIGRTMAXm9</li>
 * <li>SIGRTMAXm8</li>
 * <li>SIGRTMAXm7</li>
 * <li>SIGRTMAXm6</li>
 * <li>SIGRTMAXm5</li>
 * <li>SIGRTMAXm4</li>
 * <li>SIGRTMAXm3</li>
 * <li>SIGRTMAXm2</li>
 * <li>SIGRTMAXm1</li>
 * <li>SIGRTMAX</li>
 * </ul>
 *
 * @author Siggi
 */
public class SignalCatcher {

	private static final Logger logger = Logger.getLogger(SignalCatcher.class.getName());

	public static final int SIGHUP;
	public static final int SIGINT;
	public static final int SIGQUIT;
	public static final int SIGILL;
	public static final int SIGTRAP;
	public static final int SIGABRT;
	public static final int SIGEMT;
	public static final int SIGFPE;
	public static final int SIGKILL;
	public static final int SIGBUS;
	public static final int SIGSEGV;
	public static final int SIGSYS;
	public static final int SIGPIPE;
	public static final int SIGALRM;
	public static final int SIGTERM;
	public static final int SIGURG;
	public static final int SIGSTOP;
	public static final int SIGTSTP;
	public static final int SIGCONT;
	public static final int SIGCHLD;
	public static final int SIGTTIN;
	public static final int SIGTTOU;
	public static final int SIGIO;
	public static final int SIGXCPU;
	public static final int SIGXFSZ;
	public static final int SIGVTALRM;
	public static final int SIGPROF;
	public static final int SIGWINCH;
	public static final int SIGINFO;
	public static final int SIGUSR1;
	public static final int SIGUSR2;

	public static final int SIGSTKFLT;
	public static final int SIGPWR;
	public static final int SIGRTMIN;
	public static final int SIGRTMINp1;
	public static final int SIGRTMINp2;
	public static final int SIGRTMINp3;
	public static final int SIGRTMINp4;
	public static final int SIGRTMINp5;
	public static final int SIGRTMINp6;
	public static final int SIGRTMINp7;
	public static final int SIGRTMINp8;
	public static final int SIGRTMINp9;
	public static final int SIGRTMINp10;
	public static final int SIGRTMINp11;
	public static final int SIGRTMINp12;
	public static final int SIGRTMINp13;
	public static final int SIGRTMINp14;
	public static final int SIGRTMINp15;
	public static final int SIGRTMAXm14;
	public static final int SIGRTMAXm13;
	public static final int SIGRTMAXm12;
	public static final int SIGRTMAXm11;
	public static final int SIGRTMAXm10;
	public static final int SIGRTMAXm9;
	public static final int SIGRTMAXm8;
	public static final int SIGRTMAXm7;
	public static final int SIGRTMAXm6;
	public static final int SIGRTMAXm5;
	public static final int SIGRTMAXm4;
	public static final int SIGRTMAXm3;
	public static final int SIGRTMAXm2;
	public static final int SIGRTMAXm1;
	public static final int SIGRTMAX;

	private static final Object lock = new Object();
	private static final boolean[] registeredSignals;
	private static boolean registered = false;

	static {
		boolean isMac = false;
		boolean isArm = false;
		if (System.getProperty("os.name").toLowerCase().contains("mac")) {
			isMac = true;
		} else if (System.getProperty("os.arch").toLowerCase().contains("arm")) {
			isArm = true;
		}
		if (isMac) {
			registeredSignals = new boolean[32];
			// taken from El Capitan:
			// kill -l

			SIGHUP = 1;
			SIGINT = 2;
			SIGQUIT = 3;
			SIGILL = 4;
			SIGTRAP = 5;
			SIGABRT = 6;
			SIGEMT = 7;
			SIGFPE = 8;
			SIGKILL = 9;
			SIGBUS = 10;
			SIGSEGV = 11;
			SIGSYS = 12;
			SIGPIPE = 13;
			SIGALRM = 14;
			SIGTERM = 15;
			SIGURG = 16;
			SIGSTOP = 17;
			SIGTSTP = 18;
			SIGCONT = 19;
			SIGCHLD = 20;
			SIGTTIN = 21;
			SIGTTOU = 22;
			SIGIO = 23;
			SIGXCPU = 24;
			SIGXFSZ = 25;
			SIGVTALRM = 26;
			SIGPROF = 27;
			SIGWINCH = 28;
			SIGINFO = 29;
			SIGUSR1 = 30;
			SIGUSR2 = 31;

			SIGSTKFLT = -1;
			SIGPWR = -1;
			SIGRTMIN = -1;
			SIGRTMINp1 = -1;
			SIGRTMINp2 = -1;
			SIGRTMINp3 = -1;
			SIGRTMINp4 = -1;
			SIGRTMINp5 = -1;
			SIGRTMINp6 = -1;
			SIGRTMINp7 = -1;
			SIGRTMINp8 = -1;
			SIGRTMINp9 = -1;
			SIGRTMINp10 = -1;
			SIGRTMINp11 = -1;
			SIGRTMINp12 = -1;
			SIGRTMINp13 = -1;
			SIGRTMINp14 = -1;
			SIGRTMINp15 = -1;
			SIGRTMAXm14 = -1;
			SIGRTMAXm13 = -1;
			SIGRTMAXm12 = -1;
			SIGRTMAXm11 = -1;
			SIGRTMAXm10 = -1;
			SIGRTMAXm9 = -1;
			SIGRTMAXm8 = -1;
			SIGRTMAXm7 = -1;
			SIGRTMAXm6 = -1;
			SIGRTMAXm5 = -1;
			SIGRTMAXm4 = -1;
			SIGRTMAXm3 = -1;
			SIGRTMAXm2 = -1;
			SIGRTMAXm1 = -1;
			SIGRTMAX = -1;
		} else {
			registeredSignals = new boolean[65];
			// taken from Ubuntu Linux:
			// kill -l

			SIGHUP = 1;
			SIGINT = 2;
			SIGQUIT = 3;
			SIGILL = 4;
			SIGTRAP = 5;
			SIGABRT = 6;
			SIGBUS = 7;
			SIGFPE = 8;
			SIGKILL = 9;
			SIGUSR1 = 10;
			SIGSEGV = 11;
			SIGUSR2 = 12;
			SIGPIPE = 13;
			SIGALRM = 14;
			SIGTERM = 15;
			SIGSTKFLT = 16;
			SIGCHLD = 17;
			SIGCONT = 18;
			SIGSTOP = 19;
			SIGTSTP = 20;
			SIGTTIN = 21;
			SIGTTOU = 22;
			SIGURG = 23;
			SIGXCPU = 24;
			SIGXFSZ = 25;
			SIGVTALRM = 26;
			SIGPROF = 27;
			SIGWINCH = 28;
			SIGIO = 29;
			SIGPWR = 30;
			SIGSYS = 31;
			SIGRTMIN = 34;
			SIGRTMINp1 = 35;
			SIGRTMINp2 = 36;
			SIGRTMINp3 = 37;
			SIGRTMINp4 = 38;
			SIGRTMINp5 = 39;
			SIGRTMINp6 = 40;
			SIGRTMINp7 = 41;
			SIGRTMINp8 = 42;
			SIGRTMINp9 = 43;
			SIGRTMINp10 = 44;
			SIGRTMINp11 = 45;
			SIGRTMINp12 = 46;
			SIGRTMINp13 = 47;
			SIGRTMINp14 = 48;
			SIGRTMINp15 = 49;
			SIGRTMAXm14 = 50;
			SIGRTMAXm13 = 51;
			SIGRTMAXm12 = 52;
			SIGRTMAXm11 = 53;
			SIGRTMAXm10 = 54;
			SIGRTMAXm9 = 55;
			SIGRTMAXm8 = 56;
			SIGRTMAXm7 = 57;
			SIGRTMAXm6 = 58;
			SIGRTMAXm5 = 59;
			SIGRTMAXm4 = 60;
			SIGRTMAXm3 = 61;
			SIGRTMAXm2 = 62;
			SIGRTMAXm1 = 63;
			SIGRTMAX = 64;

			SIGEMT = -1;
			SIGINFO = -1;
		}
		try {
			String suffix = isMac ? ".jnilib" : (isArm ? "_raspbian_arm.so" : ".so");
			File file = File.createTempFile("libsignalcatcher", suffix);
			file.deleteOnExit();
			InputStream in = SignalCatcher.class.getResourceAsStream("/libsignalcatcher" + suffix);
			FileOutputStream out = new FileOutputStream(file);
			byte[] b = new byte[4096];
			int c;
			while ((c = in.read(b, 0, b.length)) != -1) {
				out.write(b, 0, c);
			}
			out.close();
			System.load(file.getAbsolutePath());
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Could not load library.", e);
		}
	}

	private static final ArrayList<SignalListener> listeners = new ArrayList<SignalListener>();

	private native static int doKill(int pID, int signal);

	private native static void startHandlingSignal(int signalID);

	/**
	 * This method is called from native code.
	 *
	 * @param result signal number
	 */
	private static void callback(int result) {
		SignalListener[] signalListeners;
		synchronized (listeners) {
			signalListeners = listeners.toArray(new SignalListener[listeners.size()]);
		}
		for (SignalListener listener : signalListeners) {
			try {
				listener.receivedSignal(result);
			} catch (Throwable t) {
			}
		}
	}

	/**
	 * Adds a SignalListener.
	 *
	 * @param listener the listener to add
	 */
	public static void addListener(SignalListener listener) {
		synchronized (listeners) {
			if (!listeners.contains(listener)) {
				listeners.add(listener);
			}
		}
	}

	/**
	 * Removes a SignalListener.
	 *
	 * @param listener the listener to remove
	 */
	public static void removeListener(SignalListener listener) {
		synchronized (listeners) {
			if (listeners.contains(listener)) {
				listeners.remove(listener);
			}
		}
	}

	/**
	 * Start catching a signal
	 *
	 * @param signal the signal to start catching.
	 */
	public static void catchSignal(int signal) {
		if (signal == -1) {
			throw new SignalCatcherException("Signal not available on this operating system.");
		}
		if (signal == SIGKILL) {
			throw new SignalCatcherException("Cannot catch SIGKILL");
		}
		if (signal == SIGSTOP) {
			throw new SignalCatcherException("Cannot catch SIGSTOP");
		}
		if (signal == SIGCONT) {
			throw new SignalCatcherException("Cannot catch SIGCONT");
		}
		if (signal < 0 || signal >= registeredSignals.length) {
			return;
		}
		synchronized (lock) {
			if (!registeredSignals[signal]) {
				registeredSignals[signal] = true;
				catchSignal0(signal);
			}
		}
	}

	/**
	 * Start catching all signals.
	 */
	public static void catchAllSignals() {
		for (int i = 1; i < registeredSignals.length; i++) {
			if (i == SIGKILL || i == SIGSTOP || i == SIGCONT) {
				continue;
			}
			catchSignal(i);
		}
	}

	private static void catchSignal0(final int signal) {
		Runnable runnable = new Runnable() {
			public void run() {
				startHandlingSignal(signal);
			}
		};
		Thread t = new Thread(runnable);
		t.setDaemon(true);
		t.setPriority(Thread.MAX_PRIORITY);
		t.start();
		registered = true;
	}

	/**
	 * Send a signal to another process.
	 *
	 * @param pID process to send to
	 * @param signal signal to send
	 * @return result of syscall kill(pID, signal), usually 0 on success,
	 * negative on failure, may behave differently on different platforms.
	 * @throws SignalCatcherException if the requested signal is not available
	 * on this OS.
	 */
	public static int kill(int pID, int signal) {
		if (signal == -1) {
			throw new SignalCatcherException("Signal not available on this operating system.");
		}
		return doKill(pID, signal);
	}
}
