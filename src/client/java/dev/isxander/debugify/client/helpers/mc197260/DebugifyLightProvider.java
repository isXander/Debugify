/*
 * Copyright (C) 2026 The Debugify Contributors
 *
 * SPDX-License-Identifier: LGPL-3.0-or-later
 */
package dev.isxander.debugify.client.helpers.mc197260;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public interface DebugifyLightProvider<S extends LivingEntityRenderState> {
	default void debugify$modifyLightCoords(S livingEntityState) {

	}
}
