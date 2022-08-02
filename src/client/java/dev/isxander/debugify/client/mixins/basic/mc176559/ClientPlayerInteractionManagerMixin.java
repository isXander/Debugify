package dev.isxander.debugify.client.mixins.basic.mc176559;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@BugFix(id = "MC-176559", category = FixCategory.BASIC, env = BugFix.Env.CLIENT)
@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {
    @Shadow private ItemStack selectedStack;

    @Shadow @Final private MinecraftClient client;

    @ModifyVariable(method = "isCurrentlyBreaking", at = @At("STORE"), slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z")))
    @SuppressWarnings("InvalidInjectorMethodSignature")
    private boolean setFlag(boolean value) {
        return !canCauseBlockBreakReset(selectedStack, client.player.getMainHandStack());
    }

    /**
     * Taken from MinecraftForge
     * https://github.com/MinecraftForge/MinecraftForge/blob/9d74a3520fa9d47db27fed74dcdd462956dd90ec/src/main/java/net/minecraftforge/common/extensions/IForgeItem.java
     * under LGPLv2.1 license
     *
     * It has been adapted into a mixin with yarn mappings for use in fabric
     *
     * @author BlueAgent
     */
    private boolean canCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
        if (!newStack.isOf(oldStack.getItem()))
            return true;

        if (!newStack.isDamageable() || !oldStack.isDamageable())
            return !ItemStack.areNbtEqual(newStack, oldStack);

        NbtCompound newTag = newStack.getNbt();
        NbtCompound oldTag = oldStack.getNbt();

        if (newTag == null || oldTag == null)
            return !(newTag == null && oldTag == null);

        Set<String> newKeys = new HashSet<>(newTag.getKeys());
        Set<String> oldKeys = new HashSet<>(oldTag.getKeys());

        newKeys.remove(ItemStack.DAMAGE_KEY);
        oldKeys.remove(ItemStack.DAMAGE_KEY);

        if (!newKeys.equals(oldKeys))
            return true;

        return !newKeys.stream().allMatch(key -> Objects.equals(newTag.get(key), oldTag.get(key)));
    }
}
