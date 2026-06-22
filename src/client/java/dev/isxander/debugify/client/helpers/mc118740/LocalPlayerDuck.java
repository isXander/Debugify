/*
 * Copyright (C) 2026 The Debugify Contributors
 *
 * SPDX-License-Identifier: LGPL-3.0-or-later
 */
package dev.isxander.debugify.client.helpers.mc118740;

/**
 * Taken from MoulberrysTweaks
 * https://github.com/Moulberry/MoulberrysTweaks
 * under MIT license
 *
 * @author Moulberry
 */
public interface LocalPlayerDuck {
	float debugify$getVisualAttackStrengthScale(float partialTick);
	void debugify$resetVisualAttackStrengthScale();
	void debugify$incrementVisualAttackStrengthScale();
}
