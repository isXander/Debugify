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

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.isxander.debugify.Debugify;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;

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
@BugFix(id = "MC-8187", category = FixCategory.GAMEPLAY, env = BugFix.Env.SERVER, modConflicts = "flwr-8187", description = "Two-by-two arrangements of jungle or spruce saplings cannot grow when there are adjacent blocks located north or west of the sapling formation")
@Mixin(TreeFeature.class)
public class TreeFeatureMixin {
    /**
     * Corrects the starting value if the trunk's minimum size is {@literal 1}
     * and the starting value is {@literal -1} by returning {@literal 0}.
     * <p>
     * I should note there: This isn't <em>an optimal bugfix</em> as it is possible for poorly implemented 3x3 trees to erroneously check only a 2x2.
     */
    // 2026 Xander: holy moly i just expressionified the hell out of this mixin you're so welcome
    @Definition(id = "x", local = @Local(type = int.class, name = "x"))
    @Definition(id = "z", local = @Local(type = int.class, name = "z"))
    @Definition(id = "r", local = @Local(type = int.class, name = "r"))
    @Expression({
            "x = @(-r)",
            "z = @(-r)"
    })
    @ModifyExpressionValue(method = "getMaxFreeTreeHeight", at = @At("MIXINEXTRAS:EXPRESSION"))
    private int fixOffsetNew(
            int start,
            @Local(argsOnly = true, name = "maxTreeHeight") int maxTreeHeight,
            @Local(argsOnly = true, name = "config") TreeConfiguration config
    ) {
        //  Roughly height == 0 && trunk == 1
        if (Debugify.isGameplayFixesEnabled() && start == -1 && config.minimumSize.getSizeAtHeight(maxTreeHeight, 0) == 1) {
            return 0;
        }
        return start;
	}
}
