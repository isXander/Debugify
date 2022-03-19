package cc.woverflow.debugify.forge;

import cc.woverflow.debugify.Debugify;
import net.minecraftforge.fml.common.Mod;

@Mod("debugify")
public class DebugifyMod {
    public DebugifyMod() {
        Debugify.onInitialize();
    }
}
