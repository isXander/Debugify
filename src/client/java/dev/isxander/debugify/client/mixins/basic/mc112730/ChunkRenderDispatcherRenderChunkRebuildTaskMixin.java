package dev.isxander.debugify.client.mixins.basic.mc112730;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.entity.BlockEntity;

@BugFix(id = "MC-112730", category = FixCategory.BASIC, env = BugFix.Env.CLIENT)
@Mixin(targets = "net.minecraft.client.renderer.chunk.ChunkRenderDispatcher$RenderChunk$RebuildTask")
public class ChunkRenderDispatcherRenderChunkRebuildTaskMixin {
    /**
     * Extremely annoying to make because:
     * <ol type="A">
     *     <li>You can't append else statements to ifs</li>
     *     <li>You can't use WrapWithCondition because {@link List#add(Object)} returns boolean</li>
     * </ol>
     * <br>
     * Bytecode is also super funky because &lt;E extends BlockEntity&gt; but
     * the param wants an Object instead of E making way for weird unnecessary casting
     */
    @SuppressWarnings("unchecked")
    @Redirect(method = "handleBlockEntity", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0))
    private <E extends BlockEntity> boolean elseAlternative(List<BlockEntity> list, Object blockEntity) {
        E be = (E) blockEntity;
        if (!Minecraft.getInstance().getBlockEntityRenderDispatcher().getRenderer(be).shouldRenderOffScreen(be)) {
            return list.add(be);
        }
        return false;
    }
}
