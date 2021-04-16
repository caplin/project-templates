package com.caplin.template.service;

import java.util.Objects;
import java.util.UUID;

public class TodoItem {

	private final UUID id = UUID.randomUUID();
	private String value;
	private long priority;

	TodoItem(final String value, final long priority) {
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
