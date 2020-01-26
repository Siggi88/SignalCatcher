#include "hk_siggi_signalcatcher_SignalCatcher.h"
#include <signal.h>
#include <unistd.h>
#include <pthread.h>

pthread_mutex_t mut = PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t con = PTHREAD_COND_INITIALIZER;
int didStartHandler = 0;

int signal_received;

void signal_handler(int signal_value) {
	pthread_mutex_lock(&mut);
	signal_received = signal_value;
	pthread_cond_signal(&con);
	pthread_mutex_unlock(&mut);
	//signal(signal_value, signal_handler);
}

JNIEXPORT jint JNICALL
Java_hk_siggi_signalcatcher_SignalCatcher_doKill (JNIEnv *env, jclass cls, jint pID, jint signal) {
	return (jint) kill(pID, signal);
}

JNIEXPORT void JNICALL
Java_hk_siggi_signalcatcher_SignalCatcher_startHandlingSignal (JNIEnv *env, jclass cls, jint signal_jint) {
	jmethodID mid = (*env)->GetStaticMethodID(env, cls, "callback", "(I)V");

	int signal_to_listen_for = (int) signal_jint;

	signal(signal_to_listen_for, signal_handler);

	pthread_mutex_lock(&mut);
	if (didStartHandler == 0) {
		didStartHandler = 1;
		signal_received = -1;
		while (1) {
			pthread_cond_wait(&con, &mut);
			if (signal_received != -1) {
				(*env)->CallStaticVoidMethod(env, cls, mid, (jint) signal_received);
				signal_received = -1;
			}
		}
	}
	pthread_mutex_unlock(&mut);
}
