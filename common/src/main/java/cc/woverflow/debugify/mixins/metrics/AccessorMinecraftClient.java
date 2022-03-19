package cc.woverflow.debugify.mixins.metrics;

import com.mojang.authlib.minecraft.UserApiService;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MinecraftClient.class)
public interface AccessorMinecraftClient {
    @Accessor
    UserApiService getUserApiService();
}
