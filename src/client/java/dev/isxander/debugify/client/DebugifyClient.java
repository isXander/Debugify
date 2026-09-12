/*
 * Copyright (C) 2026 The Debugify Contributors
 *
 * SPDX-License-Identifier: LGPL-3.0-or-later
 */
package dev.isxander.debugify.client;

import dev.isxander.debugify.Debugify;
import dev.isxander.debugify.client.utils.ClientUtils;

public class DebugifyClient {
	public static void onInitializeClient() {
		Debugify.inMultiplayerWorld = ClientUtils::isInMultiplayerWorld;
	}
}
