/*
 * Copyright (C) 2026 The Debugify Contributors
 *
 * SPDX-License-Identifier: LGPL-3.0-or-later
 */
package dev.isxander.debugify.test.suites;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.Mob;

public class MC30391 {

	@GameTest
	public void blaze(GameTestHelper ctx) {
		testWithEntity(ctx, EntityTypes.BLAZE);
	}

	@GameTest
	public void chicken(GameTestHelper ctx) {
		testWithEntity(ctx, EntityTypes.CHICKEN);
	}

	@GameTest
	public void wither(GameTestHelper ctx) {
		testWithEntity(ctx, EntityTypes.WITHER);
	}

	private <E extends Mob> void testWithEntity(GameTestHelper ctx, EntityType<E> entityType) {
		Mob mob = ctx.spawnWithNoFreeWill(entityType, new BlockPos(0, 4, 0));

		// TODO:
		ctx.runAfterDelay(50, ctx::succeed);
	}
}
