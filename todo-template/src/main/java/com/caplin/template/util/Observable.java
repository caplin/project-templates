package com.caplin.template.util;

public class Observable<T> {
	private Observer<T> observer;
	private T latestValue;

	public T getLatestValue() {
		return latestValue;
	}

	public void onNext(final T latestValue) {
		this.latestValue = latestValue;
		final Observer<T> observer = this.observer;
		if (observer != null) {
			observer.accept(latestValue);
		}
	}

	public ObservableSubscription subscribe(final Observer<T> observer) {
		this.observer = observer;
		return () -> this.observer = null;
	}
}
