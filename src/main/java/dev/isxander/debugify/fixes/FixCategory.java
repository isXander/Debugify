/*
 * Copyright (C) 2026 The Debugify Contributors
 *
 * SPDX-License-Identifier: LGPL-3.0-or-later
 */
package dev.isxander.debugify.fixes;

public enum FixCategory {
	BASIC("debugify.basic"),
	GAMEPLAY("debugify.gameplay");

	private final String displayName;

	FixCategory(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return this.displayName;
	}
}
