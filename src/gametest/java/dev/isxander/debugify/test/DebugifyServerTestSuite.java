package dev.isxander.debugify.test;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.Items;

public class DebugifyServerTestSuite implements FabricGameTest {
    @GameTest(template = EMPTY_STRUCTURE)
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

        ctx.succeedIf(() -> ctx.assertTrue(wolf.getHealth() == wolf.getMaxHealth(), "Wolf shouldn't be damaged."));
    }

    @GameTest(template = EMPTY_STRUCTURE)
    public void mc93018(GameTestHelper ctx) {
        Player player = ctx.makeMockPlayer();
        player.setItemInHand(InteractionHand.MAIN_HAND, Items.BONE.getDefaultInstance());
        ctx.getLevel().addFreshEntity(player);

        Wolf wolf = ctx.spawn(EntityType.WOLF, 2, 0, 0);
        wolf.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.0);

        wolf.mobInteract(player, InteractionHand.MAIN_HAND);
        ctx.succeedIf(() -> ctx.assertTrue(wolf.getInLoveTime() <= 0, "Wolf shouldn't love player"));
    }


}
