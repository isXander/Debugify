package dev.isxander.debugify.test.suites;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;

public class MC100991 {
    @GameTest
    public void statTrackRodKill(GameTestHelper ctx) {
        Mob targetMob = ctx.spawnWithNoFreeWill(EntityType.CREEPER, new BlockPos(4, 0, 4));

        Mob attacker = ctx.spawnWithNoFreeWill(EntityType.HUSK, new BlockPos(1, 0, 1));
        ItemStack rodStack = new ItemStack(Items.FISHING_ROD);
        attacker.setItemInHand(InteractionHand.MAIN_HAND, rodStack);

        FishingHook hook = ctx.spawn(EntityType.FISHING_BOBBER, targetMob.blockPosition().above(1));

        Player mockPlayer = ctx.makeMockPlayer(GameType.CREATIVE);
        mockPlayer.setItemInHand(InteractionHand.MAIN_HAND, rodStack);
        mockPlayer.setPos(attacker.position());
        hook.setOwner(mockPlayer);

        ctx.runAfterDelay(20, () -> {
            // yank entity
            hook.retrieve(rodStack);

            ctx.runAfterDelay(5, () -> {
                System.out.println(targetMob.getCombatTracker().getDeathMessage().getString());
                System.out.println(targetMob.getCombatTracker().getDeathMessage());
            });
        });
    }
}
