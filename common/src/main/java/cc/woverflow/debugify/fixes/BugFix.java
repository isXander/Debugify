package cc.woverflow.debugify.fixes;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BugFix {
    String id();

    FixCategory category();

    Env env();

    boolean enabled() default true;

    String[] fabricConflicts() default {};

    enum Env {
        CLIENT("Client"),
        SERVER("Server");

        private final String displayName;

        Env(String displayName) {
            this.displayName = displayName;
        }

        public Text getDisplayName() {
            return new LiteralText(displayName);
        }
    }
}
