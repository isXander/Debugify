package dev.isxander.debugify.mixins.basic.mc219981;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@BugFix(id = "MC-219981", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "If zombies spawned with max health over 20 (leader zombie bonus), they will have 20 health instead of their max health")
@Mixin(Zombie.class)
public class ZombieMixin extends Monster {
    protected ZombieMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Definition(id = "getAttribute", method = "Lnet/minecraft/world/entity/monster/zombie/Zombie;getAttribute(Lnet/minecraft/core/Holder;)Lnet/minecraft/world/entity/ai/attributes/AttributeInstance;")
    @Definition(id = "MAX_HEALTH", field = "Lnet/minecraft/world/entity/ai/attributes/Attributes;MAX_HEALTH:Lnet/minecraft/core/Holder;")
    @Definition(id = "addOrReplacePermanentModifier", method = "Lnet/minecraft/world/entity/ai/attributes/AttributeInstance;addOrReplacePermanentModifier(Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;)V")
    @Definition(id = "LEADER_ZOMBIE_BONUS_ID", field = "Lnet/minecraft/world/entity/monster/zombie/Zombie;LEADER_ZOMBIE_BONUS_ID:Lnet/minecraft/resources/Identifier;")
    @Definition(id = "AttributeModifier", type = AttributeModifier.class)
    @Expression("this.getAttribute(MAX_HEALTH).addOrReplacePermanentModifier(new AttributeModifier(LEADER_ZOMBIE_BONUS_ID, ?, ?))")
    @WrapOperation(method = "handleAttributes", at = @At("MIXINEXTRAS:EXPRESSION"))
    private void fixLeaderZombieHealth(AttributeInstance instance, AttributeModifier modifier, Operation<Void> original) {
        float damageTaken = this.getMaxHealth() - this.getHealth();

        // apply attribute, max health will increase
        original.call(instance, modifier);

        this.setHealth(this.getMaxHealth() - damageTaken);
    }
}
