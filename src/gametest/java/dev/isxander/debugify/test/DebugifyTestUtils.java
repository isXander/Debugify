package dev.isxander.debugify.test;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Utilities for tests.
 *
 * @author Ampflower
 * @see DebugifyMc8187TestSuite
 * @since 1.20.1+1.2
 **/
public final class DebugifyTestUtils {
    public static final int TIME_OF_DAY_NOON = 6000;

    /**
     * Utility function to force-feed saplings bonemeal.
     */
    static void bonemealIndefinitely(GameTestHelper ctx, BlockPos pos) {
        final var player = ctx.makeMockPlayer();

        player.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BONE_MEAL, Item.MAX_STACK_SIZE));

        ctx.onEachTick(() -> ctx.useBlock(pos, player));
    }

    /**
     * Asserts that blocks in a given area are as expected.
     */
    static void assertBlockFill(GameTestHelper ctx, BlockPos start, BlockPos end, Predicate<Block> test, String message) {
        area(start, end, pos -> ctx.assertBlock(pos, test, message));
    }

    /**
     * Asserts that blockstates in a given area are as expected.
     */
    static void assertBlockStateFill(GameTestHelper ctx, BlockPos start, BlockPos end, Predicate<BlockState> test, String message) {
        area(start, end, pos -> ctx.assertBlockState(pos, test, () -> message));
    }

    /**
     * Fills the area with the given blockstate.
     */
    static void fill(GameTestHelper ctx, BlockPos start, BlockPos end, BlockState state) {
        area(start, end, pos -> ctx.setBlock(pos, state));
    }

    /**
     * Iterates over the entire box, sending the current position to the consumer.
     */
    private static void area(BlockPos start, BlockPos end, Consumer<BlockPos> consumer) {
        final var pos = new BlockPos.MutableBlockPos();

        for (int x = start.getX(); x <= end.getX(); x++) {
            for (int y = start.getY(); y <= end.getY(); y++) {
                for (int z = start.getZ(); z <= end.getZ(); z++) {
                    consumer.accept(pos.set(x, y, z));
                }
            }
        }
    }
}
