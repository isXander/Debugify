package cc.woverflow.debugify.utils;

import cc.woverflow.debugify.Debugify;
import cc.woverflow.debugify.fixes.BugFix;
import cc.woverflow.debugify.fixes.BugFixData;
import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import me.fallenbreath.conditionalmixin.api.mixin.RestrictiveMixinConfigPlugin;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.util.Annotations;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
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
        Optional<BugFixData> bugFixOptional = getBugFixForMixin(mixinClassName);

        if (bugFixOptional.isEmpty())
            return super.shouldApplyMixin(targetClassName, mixinClassName);

        BugFixData bugFix = bugFixOptional.get();
        Debugify.config.registerBugFix(bugFix);
        return Debugify.config.isBugFixEnabled(bugFix) && super.shouldApplyMixin(targetClassName, mixinClassName);
    }

    private Optional<BugFixData> getBugFixForMixin(String mixinClassName) {
        AnnotationNode annotationNode;

        try {
            ClassNode classNode = MixinService.getService().getBytecodeProvider().getClassNode(mixinClassName);
            annotationNode = Annotations.getVisible(classNode, BugFix.class);
        }
        catch (ClassNotFoundException | IOException e) {
            annotationNode = null;
        }

        if (annotationNode == null)
            return Optional.empty();

        String id = Annotations.getValue(annotationNode, "id");
        BugFix.Env env = getAnnotationEnumValue(annotationNode, "env", BugFix.Env.class);
        boolean enabledByDefault = Annotations.getValue(annotationNode, "enabled", Boolean.valueOf(true));

        return Optional.of(new BugFixData(id, env, enabledByDefault));
    }

    private static <T extends Enum<T>> T getAnnotationEnumValue(AnnotationNode annotation, String key, Class<T> enumClass) {
        String[] value = Annotations.getValue(annotation, key);
        return Enum.valueOf(enumClass, value[1]);
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() {
        return null;
    }
}
