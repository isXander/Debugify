package cc.woverflow.debugify.utils;

import cc.woverflow.debugify.Debugify;
import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import me.fallenbreath.conditionalmixin.api.mixin.RestrictiveMixinConfigPlugin;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class MixinPlugin extends RestrictiveMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {
        MixinExtrasBootstrap.init();
        Debugify.onPreInitialize();
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }


    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        String[] split = mixinClassName.split("\\.");
        String bugPackage = split[split.length - 2];
        if (bugPackage.startsWith("mc")) {
            String bug = "MC-" + bugPackage.substring(2);
            Debugify.config.registerBugFix(bug);
            return Debugify.config.isBugFixEnabled(bug) && super.shouldApplyMixin(targetClassName, mixinClassName);
        }
        return super.shouldApplyMixin(targetClassName, mixinClassName);
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() {
        return null;
    }
}
