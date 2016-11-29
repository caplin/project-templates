package com.caplin.template.service;

import java.util.Objects;
import java.util.UUID;

public class TodoItem implements Comparable<TodoItem> {

	private final UUID id = UUID.randomUUID();
	private String value;
	private long priority;

	public TodoItem(final String value, final long priority) {
		this.value = value;
		this.priority = priority;
	}

	public UUID getId() {
		return id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(final String value) {
		this.value = value;
	}

	public long getPriority() {
		return priority;
	}

	public void setPriority(final long priority) {
		this.priority = priority;
	}

	// Sort them first by priority, then by value.
	// 1 is higher priority than 2 etc.
	@Override
	public int compareTo(final TodoItem o) {
		final int priorityComparison = Long.compare(o.priority, priority);
		if (priorityComparison == 0) {
			return value.compareToIgnoreCase(o.value);
		}
		else {
			return priorityComparison;
		}
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		final TodoItem todoItem = (TodoItem) o;
		return priority == todoItem.priority &&
				Objects.equals(id, todoItem.id) &&
				Objects.equals(value, todoItem.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, value, priority);
	}

	@Override
	public String toString() {
		return "TodoItem{" + "id=" + id +
				", value='" + value + '\'' +
				", priority=" + priority +
				'}';
	}
}
