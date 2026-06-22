/*
 * Copyright (C) 2026 The Debugify Contributors
 *
 * SPDX-License-Identifier: LGPL-3.0-or-later
 */
package dev.isxander.debugify.client.mixins.basic.mc4490;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.renderer.entity.FishingHookRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-4490", category = FixCategory.BASIC, env = BugFix.Env.CLIENT, description = "Fishing line not attached to fishing rod in third person while crouching")
@Mixin(FishingHookRenderer.class)
public class FishingHookRendererMixin {
	@ModifyExpressionValue(
			method = "getPlayerHandPos",
			at = @At(
					value = "CONSTANT",
					args = "floatValue=-0.1875"
			)
	)
	private float renderSneakOffset(float constant) {
		return -0.2875F;
	}
}
