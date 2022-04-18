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
        CLIENT(new LiteralText("Client")),
        SERVER(new LiteralText("Server"));

        private final Text displayName;

        Env(Text displayName) {
            this.displayName = displayName;
        }

        public Text getDisplayName() {
            return displayName;
        }
    }
}
