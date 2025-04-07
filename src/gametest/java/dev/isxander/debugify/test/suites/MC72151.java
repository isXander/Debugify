package dev.isxander.debugify.test.suites;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.projectile.Snowball;

public class MC72151 {
    @GameTest
    public void mc72151(GameTestHelper ctx) {
        BlockPos wolfPos = new BlockPos(4, 0, 4);
        Wolf wolf = ctx.spawnWithNoFreeWill(EntityType.WOLF, wolfPos);
        wolf.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.0);
        Snowball snowball = ctx.spawn(EntityType.SNOWBALL, 0, 0, 4);
        double d = wolf.getEyeY() - 1.1F;
        double e = wolf.getX() - snowball.getX();
        double f = d - snowball.getY();
        double g = wolf.getZ() - snowball.getZ();
        double h = Math.sqrt(e * e + g * g) * 0.2F;
        snowball.shoot(e, f + h, g, 1.6F, 0f);

        ctx.succeedIf(() -> ctx.assertTrue(wolf.getHealth() == wolf.getMaxHealth(), Component.literal("Wolf shouldn't be damaged.")));
    }
}
