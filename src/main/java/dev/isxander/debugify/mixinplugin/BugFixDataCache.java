package dev.isxander.debugify.mixinplugin;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.BugFixData;
import dev.isxander.debugify.fixes.FixCategory;
import dev.isxander.debugify.fixes.OS;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.util.Annotations;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BugFixDataCache {
    private static final Map<String, ResolvedBugFixData> bugFixDataByMixinClass = new HashMap<>();

    public static Optional<BugFixData> getOrResolve(String mixinClassName) {
        return Optional.ofNullable(
                bugFixDataByMixinClass.computeIfAbsent(mixinClassName, BugFixDataCache::resolve)
                        .data()
        );
    }

    public static Optional<BugFixData> getIfResolved(String mixinClassName) {
        return Optional.ofNullable(bugFixDataByMixinClass.get(mixinClassName))
                .flatMap(resolved -> Optional.ofNullable(resolved.data()));
    }

    private static ResolvedBugFixData resolve(String mixinClassName) {
        ClassNode classNode = getClassNode(mixinClassName);
        if (classNode == null) {
            return new ResolvedBugFixData(null);
        }

        AnnotationNode annotationNode = Annotations.getVisible(classNode, BugFix.class);

        if (annotationNode == null) {
            return new ResolvedBugFixData(null);
        }

        String id = Annotations.getValue(annotationNode, "id");
        FixCategory category = getAnnotationEnumValue(annotationNode, "category", FixCategory.class);
        BugFix.Env env = getAnnotationEnumValue(annotationNode, "env", BugFix.Env.class);
        boolean enabledByDefault = Annotations.getValue(annotationNode, "enabled", Boolean.valueOf(true));
        List<String> conflicts = Annotations.getValue(annotationNode, "modConflicts", true);
        OS requiredOS = Annotations.getValue(annotationNode, "os", OS.class, OS.UNKNOWN);
        String description = Annotations.getValue(annotationNode, "description");
        if ("".equals(description)) {
            description = null;
        }

        return new ResolvedBugFixData(new BugFixData(id, category, env, enabledByDefault, conflicts, requiredOS, description));
    }

    private static ClassNode getClassNode(String className) {
        try {
            return MixinService.getService().getBytecodeProvider().getClassNode(className);
        } catch (ClassNotFoundException | IOException e) {
            return null;
        }
    }

    private static <T extends Enum<T>> T getAnnotationEnumValue(AnnotationNode annotation, String key, Class<T> enumClass) {
        String[] value = Annotations.getValue(annotation, key);
        return Enum.valueOf(enumClass, value[1]);
    }

    record ResolvedBugFixData(@Nullable BugFixData data) {}
}
