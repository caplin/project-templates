package com.caplin.template.service;

import java.util.UUID;
import java.util.function.Consumer;

public interface TodoItemDeleteListener extends Consumer<UUID> {}
