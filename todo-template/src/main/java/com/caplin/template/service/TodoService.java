package com.caplin.template.service;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TodoService {

	private static final List<String> TODO_VALUES = Arrays.asList(
			"Walk the dog",
			"Pick up milk",
			"Dentist appointment",
			"Clean the car",
			"Mow the lawn",
			"Water plants",
			"Renew oyster card",
			"Call dad");

	private final ScheduledExecutorService scheduledExecutorService = newSingleThreadScheduledExecutor();
	private final Random random = new Random();

	public TodoSubscription subscribeForItems(@SuppressWarnings("unused") final String username, final TodoItemInsertUpdateListener insertUpdateListener,
			final TodoItemDeleteListener deleteListener) {

		final List<TodoItem> items = new LinkedList<>();

		final ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(() -> {

			if (items.size() < 4) {
				final TodoItem todoItem = createTodoItem();
				items.add(todoItem);
				insertUpdateListener.accept(todoItem);
			}
			else if (items.size() > 10) {
				final TodoItem todoItem = items.remove(random.nextInt(items.size()));
				deleteListener.accept(todoItem.getId());
			}
			else {
				if (random.nextInt(1) == 0) {
					final TodoItem todoItem = createTodoItem();
					items.add(todoItem);
					insertUpdateListener.accept(todoItem);
				} else {
					final TodoItem todoItem = items.remove(random.nextInt(items.size()));
					deleteListener.accept(todoItem.getId());
				}
			}

		}, 5, 5, TimeUnit.SECONDS);

		return () -> scheduledFuture.cancel(false);
	}

	private TodoItem createTodoItem() {
		return new TodoItem(TODO_VALUES.get(random.nextInt(TODO_VALUES.size())), random.nextInt(5));
	}
}
