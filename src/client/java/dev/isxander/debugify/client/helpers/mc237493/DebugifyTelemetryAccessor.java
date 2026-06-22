/*
 * Copyright (C) 2026 The Debugify Contributors
 *
 * SPDX-License-Identifier: LGPL-3.0-or-later
 */
package dev.isxander.debugify.client.helpers.mc237493;

import net.minecraft.client.OptionInstance;

public interface DebugifyTelemetryAccessor {
	OptionInstance<DebugifyTelemetry> getTelemetryOption();
}
