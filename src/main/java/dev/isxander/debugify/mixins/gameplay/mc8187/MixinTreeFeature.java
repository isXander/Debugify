/*
 * Copyright (C) 2023, Ampflower
 *
 * This software is subject to the terms of the Zlib License.
 * If a copy was not distributed with this file, you can obtain one at
 * https://github.com/Modflower/8187/blob/trunk/LICENSE
 *
 * Source: https://github.com/Modflower/8187
 * SPDX-License-Identifier: Zlib
 */

package dev.isxander.debugify.mixins.gameplay.mc8187;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Fixes the offsets for 2x2 saplings at starting height
 * fixing <a href="https://bugs.mojang.com/browse/MC-8187">MC-8187</a>.
 * <p>
 * This code is provided as-is minus differences in mappings by Ampflower.
 * <p>
 * Original mod: <a href="https://modrinth.com/mod/8187">{@literal flwr-8187}</a>
 * <sup>(<a href="https://github.com/Modflower/8187">GitHub</a>)</sup>.
 *
 * @author Ampflower
 * @since 1.20.1+1.2
 **/
@BugFix(id = "MC-8187", category = FixCategory.GAMEPLAY, env = BugFix.Env.SERVER, modConflicts = "flwr-8187")
@Mixin(TreeFeature.class)
public class MixinTreeFeature {
	/**
	 * Corrects the starting value if the trunk's minimum size is {@literal 1}
	 * and the starting value is {@literal -1} by returning {@literal 0}.
	 * <p>
	 * I should note there: This isn't <em>an optimal bugfix</em> as it is possible for poorly implemented 3x3 trees to erroneously check only a 2x2.
	 */
	@SuppressWarnings("MethodMayBeStatic") // Not applicable
	@ModifyVariable(method = "getMaxFreeTreeHeight", at = @At(value = "STORE", ordinal = 0), allow = 1, index = 8)
	private int flwr8187$fixOffset(int startValue, LevelSimulatedReader world, int i, BlockPos pos, TreeConfiguration config) {
		//  Roughly height == 0 && trunk == 1
		if (startValue == -1 && config.minimumSize.getSizeAtHeight(i, 0) == 1) {
			return 0;
		}
		return startValue;
	}

	/**
	 * Redirects to {@link #flwr8187$fixOffset(int, LevelSimulatedReader, int, BlockPos, TreeConfiguration)}
	 */
	@ModifyVariable(method = "getMaxFreeTreeHeight", at = @At(value = "STORE", ordinal = 0), allow = 1, index = 9)
	private int flwr8187$fixOffset2(int v, LevelSimulatedReader world, int i, BlockPos pos, TreeConfiguration config) {
		return flwr8187$fixOffset(v, world, i, pos, config);
	}
}
