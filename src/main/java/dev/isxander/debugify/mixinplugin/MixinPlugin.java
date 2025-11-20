package dev.isxander.debugify.mixinplugin;

import dev.isxander.debugify.Debugify;
import dev.isxander.debugify.api.DebugifyApi;
import dev.isxander.debugify.error.DebugifyErrorHandler;
import dev.isxander.debugify.fixes.BugFixData;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.*;

public class MixinPlugin implements IMixinConfigPlugin {

    @Override
    public void onLoad(String mixinPackage) {
        Debugify.onPreInitialize();

        var entrypoints = FabricLoader.getInstance().getEntrypointContainers("debugify", DebugifyApi.class);
        entrypoints.forEach(container -> {
            DebugifyApi api = container.getEntrypoint();
            String containerModId = container.getProvider().getMetadata().getId();
            for (String bugId : api.getDisabledFixes()) {
                BugFixData.registerApiConflict(containerModId, bugId);
            }

            api.getProvidedDisabledFixes().forEach((modId, bugs) -> {
                if (FabricLoader.getInstance().isModLoaded(modId)) {
                    bugs.forEach(bugId -> BugFixData.registerApiConflict(modId, bugId));
                }
            });
        });
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }


    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        Optional<BugFixData> bugFixOptional = BugFixDataCache.getOrResolve(mixinClassName);

        if (bugFixOptional.isEmpty())
            return true;

        BugFixData bugFix = bugFixOptional.get();
        var multipleMixins = Debugify.CONFIG.getBugFixes().containsKey(bugFix);
        Debugify.CONFIG.registerBugFix(bugFix);

        if (DebugifyErrorHandler.hasErrored(bugFix)) {
            Debugify.LOGGER.warn("Preventing loading of {} mixin, {} because another mixin for the same bug fix failed to apply.", bugFix.bugId(), mixinClassName);
            return false;
        }

        Set<String> conflicts = bugFix.getActiveConflicts();
        if (!conflicts.isEmpty()) {
            if (Debugify.CONFIG.isBugFixEnabled(bugFix) && !multipleMixins)
                Debugify.LOGGER.warn("Force disabled {} because it's conflicting with: {}", bugFix.bugId(), String.join(", ", conflicts));
            return false;
        } else if (!bugFix.satisfiesOSRequirement()) {
            if (Debugify.CONFIG.isBugFixEnabled(bugFix) && !multipleMixins)
                Debugify.LOGGER.warn("Force disabled {} because it only applies to OS: {}", bugFix.bugId(), bugFix.requiredOs().name());
            return false;
        }

        return Debugify.CONFIG.isBugFixEnabled(bugFix);
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}
