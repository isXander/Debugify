package dev.isxander.debugify.test;

import com.mojang.authlib.GameProfile;
import dev.isxander.debugify.Debugify;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageRecord;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

import java.util.UUID;

public class DebugifyServerTestSuite implements FabricGameTest {
    @GameTest(templateName = EMPTY_STRUCTURE)
    public void mc72151(TestContext ctx) {
        BlockPos wolfPos = new BlockPos(4, 0, 4);
        WolfEntity wolf = ctx.spawnMob(EntityType.WOLF, wolfPos);
        wolf.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.0);
        SnowballEntity snowball = ctx.spawnEntity(EntityType.SNOWBALL, 0, 0, 4);
        double d = wolf.getEyeY() - 1.1F;
        double e = wolf.getX() - snowball.getX();
        double f = d - snowball.getY();
        double g = wolf.getZ() - snowball.getZ();
        double h = Math.sqrt(e * e + g * g) * 0.2F;
        snowball.setVelocity(e, f + h, g, 1.6F, 0f);

        ctx.addFinalTask(() -> ctx.assertTrue(wolf.getHealth() == wolf.getMaxHealth(), "Wolf shouldn't be damaged."));
    }

    @GameTest(templateName = EMPTY_STRUCTURE)
    public void mc93018(TestContext ctx) {
        PlayerEntity player = ctx.createMockCreativePlayer();
        player.setStackInHand(Hand.MAIN_HAND, Items.BONE.getDefaultStack());
        ctx.getWorld().spawnEntity(player);

        WolfEntity wolf = ctx.spawnEntity(EntityType.WOLF, 2, 0, 0);
        wolf.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.0);

        wolf.interactMob(player, Hand.MAIN_HAND);
        ctx.addFinalTask(() -> ctx.assertTrue(wolf.getLoveTicks() <= 0, "Wolf shouldn't love player"));
    }


}
