package dev.isxander.debugify.mixins.basic.mc147784;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.FletchingTableBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@BugFix(id = "MC-147784", category = FixCategory.BASIC, env = BugFix.Env.SERVER, description = "Fletching table flashes crafting table's GUI for about a second upon right-clicking it in spectator mode")
@Mixin(FletchingTableBlock.class)
public class FletchingTableBlockMixin extends CraftingTableBlock {
    public FletchingTableBlockMixin(Properties properties) {
        super(properties);
    }

    /**
     * CraftingTableBlock provides a MenuProvider, which we need to override
     * Despite IntelliJ's cries for help, getMenuProvider is nullable
     */
    @Override
    protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return null;
    }
}
