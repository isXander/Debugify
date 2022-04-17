package cc.woverflow.debugify.mixins.client.mc249021;

import cc.woverflow.debugify.fixes.BugFix;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.realms.dto.RealmsServer;
import net.minecraft.client.realms.gui.screen.RealmsMainScreen;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@BugFix(id = "MC-249021", env = BugFix.Env.CLIENT)
@Mixin(RealmsMainScreen.class)
public abstract class RealmsMainScreenMixin extends Screen {
    protected RealmsMainScreenMixin(Text title) {
        super(title);
    }

    @Shadow private RealmsMainScreen.PendingInvitesButton pendingInvitesButton;

    @Shadow private ButtonWidget newsButton;

    @Shadow private List<RealmsServer> realmsServers;

    @Inject(method = "updateButtonStates", at = @At("RETURN"))
    private void onAddButtons(@Nullable RealmsServer server, CallbackInfo ci) {
        // using shouldShowPopup leads to the news button never being shown
        if (!this.realmsServers.isEmpty()) {
            newsButton.visible = true;
            newsButton.active = true;
        }

        /*
         * set visible at all times as it appears that you can view
         * invitations before the servers have finished loading
         *
         * cannot fully test as I do not have any invitations
         */
        pendingInvitesButton.visible = true;
    }
}
