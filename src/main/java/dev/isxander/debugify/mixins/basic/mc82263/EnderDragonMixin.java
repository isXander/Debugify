package dev.isxander.debugify.mixins.basic.mc82263;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@BugFix(id = "MC-82263", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "Ender dragon produces regular hurt sound on final hit")
@Mixin(EnderDragon.class)
public class EnderDragonMixin extends Mob {
    protected EnderDragonMixin(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    // The death sound plays regardless of what this is set to, it's not set in the target class and instead uses LivingEntity's GENERIC_DEATH
    @Override
    protected SoundEvent getDeathSound() {
        return null;
    }
}
