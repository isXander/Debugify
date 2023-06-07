package dev.isxander.debugify.client.mixins.basic.mc176559;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

@BugFix(id = "MC-176559", category = FixCategory.BASIC, env = BugFix.Env.CLIENT)
@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {
    @Shadow private ItemStack destroyingItem;

    @Shadow @Final private Minecraft minecraft;

    @ModifyReturnValue(method = "sameDestroyTarget", at = @At("RETURN"))
    private boolean setFlag(boolean value) {
        return !canCauseBlockBreakReset(destroyingItem, minecraft.player.getMainHandItem());
    }

    /**
     * Taken from <a href="https://github.com/MinecraftForge/MinecraftForge/blob/9d74a3520fa9d47db27fed74dcdd462956dd90ec/src/main/java/net/minecraftforge/common/extensions/IForgeItem.java">MinecraftForge</a>
     * under LGPLv2.1 license
     * <br>
     * It has been adapted into a mixin with yarn mappings for use in fabric
     *
     * @author BlueAgent
     */
    private boolean canCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
        try {
            if (!newStack.is(oldStack.getItem()))
                return true;

            if (!newStack.isDamageableItem() || !oldStack.isDamageableItem())
                return !ItemStack.matches(newStack, oldStack);

            CompoundTag newTag = newStack.getTag();
            CompoundTag oldTag = oldStack.getTag();

            if (newTag == null || oldTag == null)
                return !(newTag == null && oldTag == null);

            Set<String> newKeys = new HashSet<>(newTag.getAllKeys());
            Set<String> oldKeys = new HashSet<>(oldTag.getAllKeys());

            newKeys.remove(ItemStack.TAG_DAMAGE);
            oldKeys.remove(ItemStack.TAG_DAMAGE);

            if (!newKeys.equals(oldKeys))
                return true;

            return !newKeys.stream().allMatch(key -> Objects.equals(newTag.get(key), oldTag.get(key)));
        } catch (Throwable t) {
            t.printStackTrace();
            return true;
        }

    }
}
