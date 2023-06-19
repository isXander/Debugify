package dev.isxander.debugify.test;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Dedicated test suite for <a href="https://bugs.mojang.com/browse/MC-8187">MC-8187</a>.
 * <p>
 * This is likely well over-engineered with asserting that the test will always work.
 *
 * @author Ampflower
 * @since 1.20.1+1.2
 **/
public class DebugifyMc8187TestSuite implements FabricGameTest {
    private static final String TEMPLATE = "debugify:mc-8187";

    // Adjust this if you move the center, as the tests assume this is accurate.
    private static final int SIZE = 18;
    // Subtracting one to get NW center.
    private static final int CENTER = SIZE / 2 - 1;

    private static final BlockPos CORNER = new BlockPos(CENTER, 1, CENTER);
    private static final BlockPos START_CORNER = CORNER.above();
    private static final BlockPos END_CORNER = CORNER.offset(1, 1, 1);

    /**
     * Tests that the tree will grow under random ticks.
     * <p>
     * Side effect: Sets the time to day.
     */
    private static void testTreeRandomTick(GameTestHelper ctx, Block sapling, Block log, BlockPos growCorner, boolean small) {
        // Ensure that parameters are correct.
        ctx.assertTrue(sapling instanceof SaplingBlock, sapling + " is not a sapling");
        testTreeParamCommon(ctx, sapling, log, growCorner, true);

        // Setup
        final BlockState saplingState = sapling.defaultBlockState().setValue(SaplingBlock.STAGE, 1);

        ctx.onEachTick(() -> ctx.randomTick(growCorner));

        testTreeCommon(ctx, saplingState, log, growCorner, small);
    }

    /**
     * Tests that the tree will grow with bonemeal.
     */
    private static void testTreeBonemeal(GameTestHelper ctx, Block sapling, Block log, BlockPos growCorner, boolean small) {
        // Ensure that parameters are correct.
        ctx.assertTrue(sapling instanceof SaplingBlock, sapling + " is not a sapling");
        testTreeParamCommon(ctx, sapling, log, growCorner, false);

        // Setup
        final BlockState saplingState = sapling.defaultBlockState().setValue(SaplingBlock.STAGE, 1);

        DebugifyTestUtilites.bonemealIndefinitely(ctx, growCorner);

        testTreeCommon(ctx, saplingState, log, growCorner, small);
    }

    /**
     * Tests that the azalea will grow with bonemeal.
     */
    private static void testAzalea(GameTestHelper ctx, Block sapling, Block log, BlockPos growCorner) {
        // Ensure that parameters are correct.
        ctx.assertTrue(sapling instanceof AzaleaBlock, sapling + " is not a sapling");
        testTreeParamCommon(ctx, sapling, log, growCorner, false);

        DebugifyTestUtilites.bonemealIndefinitely(ctx, growCorner);

        testTreeCommon(ctx, sapling.defaultBlockState(), log, growCorner, true);
    }

    /**
     * Asserts that the given parameters are correct.
     * <p>
     * This is to catch errors quickly as the input is sensitive to off by 1 errors.
     */
    private static void testTreeParamCommon(GameTestHelper ctx, Block sapling, Block log, BlockPos growCorner, boolean requiresDay) {
        ctx.assertTrue(log instanceof RotatedPillarBlock, log + " does not appear like a log");
        ctx.assertTrue(growCorner.getY() == START_CORNER.getY(),
                "Incorrect Y, got " + growCorner.getY() + ", expected " + START_CORNER.getY());

        // Ensure that the structure is correctly set up.
        assertSuitable(ctx, requiresDay);
    }

    /**
     * Continuously checks whether the tree has grown.
     */
    private static void testTreeCommon(GameTestHelper ctx, BlockState sapling, Block log, BlockPos growCorner, boolean small) {
        DebugifyTestUtilites.fill(ctx, START_CORNER, END_CORNER, sapling);

        if (small) {
            ctx.succeedWhen(() -> ctx.assertBlock(growCorner, block -> block == log, "Incorrect log, expected " + log));
        } else {
            ctx.succeedWhen(() -> DebugifyTestUtilites.assertBlockFill(ctx, START_CORNER, END_CORNER, block -> block == log,
                    "Incorrect log, expected " + log));
        }
    }

    @GameTest(template = TEMPLATE, batch = "daylight")
    public void oak_randomTick_NW(GameTestHelper ctx) {
        testTreeRandomTick(ctx, Blocks.OAK_SAPLING, Blocks.OAK_LOG, START_CORNER, true);
    }

    @GameTest(template = TEMPLATE, batch = "daylight")
    public void birch_randomTick_NW(GameTestHelper ctx) {
        testTreeRandomTick(ctx, Blocks.BIRCH_SAPLING, Blocks.BIRCH_LOG, START_CORNER, true);
    }

    @GameTest(template = TEMPLATE, batch = "daylight")
    public void spruce_randomTick_NW(GameTestHelper ctx) {
        testTreeRandomTick(ctx, Blocks.SPRUCE_SAPLING, Blocks.SPRUCE_LOG, START_CORNER, false);
    }

    @GameTest(template = TEMPLATE, batch = "daylight")
    public void jungle_randomTick_NW(GameTestHelper ctx) {
        testTreeRandomTick(ctx, Blocks.JUNGLE_SAPLING, Blocks.JUNGLE_LOG, START_CORNER, false);
    }

    @GameTest(template = TEMPLATE, batch = "daylight")
    public void acacia_randomTick_NW(GameTestHelper ctx) {
        testTreeRandomTick(ctx, Blocks.ACACIA_SAPLING, Blocks.ACACIA_LOG, START_CORNER, true);
    }

    @GameTest(template = TEMPLATE, batch = "daylight")
    public void darkOak_randomTick_NW(GameTestHelper ctx) {
        testTreeRandomTick(ctx, Blocks.DARK_OAK_SAPLING, Blocks.DARK_OAK_LOG, START_CORNER, false);
    }

    @GameTest(template = TEMPLATE, batch = "daylight")
    public void cherry_randomTick_NW(GameTestHelper ctx) {
        testTreeRandomTick(ctx, Blocks.CHERRY_SAPLING, Blocks.CHERRY_LOG, START_CORNER, true);
    }


    @GameTest(template = TEMPLATE)
    public void oak_bonemeal_NW(GameTestHelper ctx) {
        testTreeBonemeal(ctx, Blocks.OAK_SAPLING, Blocks.OAK_LOG, START_CORNER, true);
    }

    @GameTest(template = TEMPLATE)
    public void birch_bonemeal_NW(GameTestHelper ctx) {
        testTreeBonemeal(ctx, Blocks.BIRCH_SAPLING, Blocks.BIRCH_LOG, START_CORNER, true);
    }

    @GameTest(template = TEMPLATE)
    public void spruce_bonemeal_NW(GameTestHelper ctx) {
        testTreeBonemeal(ctx, Blocks.SPRUCE_SAPLING, Blocks.SPRUCE_LOG, START_CORNER, false);
    }

    @GameTest(template = TEMPLATE)
    public void jungle_bonemeal_NW(GameTestHelper ctx) {
        testTreeBonemeal(ctx, Blocks.JUNGLE_SAPLING, Blocks.JUNGLE_LOG, START_CORNER, false);
    }

    @GameTest(template = TEMPLATE)
    public void acacia_bonemeal_NW(GameTestHelper ctx) {
        testTreeBonemeal(ctx, Blocks.ACACIA_SAPLING, Blocks.ACACIA_LOG, START_CORNER, true);
    }

    @GameTest(template = TEMPLATE)
    public void darkOak_bonemeal_NW(GameTestHelper ctx) {
        testTreeBonemeal(ctx, Blocks.DARK_OAK_SAPLING, Blocks.DARK_OAK_LOG, START_CORNER, false);
    }

    @GameTest(template = TEMPLATE)
    public void cherry_bonemeal_NW(GameTestHelper ctx) {
        testTreeBonemeal(ctx, Blocks.CHERRY_SAPLING, Blocks.CHERRY_LOG, START_CORNER, true);
    }

    // The following two needs to be special-cased.
    @GameTest(template = TEMPLATE)
    public void azaleaNW(GameTestHelper ctx) {
        testAzalea(ctx, Blocks.AZALEA, Blocks.OAK_LOG, START_CORNER);
    }

    // @GameTest(template = TEMPLATE)
    public void mangroveNW(GameTestHelper ctx) {
        testTreeRandomTick(ctx, Blocks.MANGROVE_PROPAGULE, Blocks.MANGROVE_LOG, START_CORNER, true);
    }

    /**
     * Asserts that the input parameters are suitable for use.
     */
    private static void assertSuitable(GameTestHelper ctx, boolean requiresDay) {
        // Force it to be day for random-tick-based tests.
        if (requiresDay) {
            ctx.setDayTime(DebugifyTestUtilites.TIME_OF_DAY_NOON);
        }

        // For some reason, + 1 is required.
        final var pos = CORNER;
        DebugifyTestUtilites.assertBlockStateFill(ctx, pos, pos.offset(1, 0, 1), b -> b.is(BlockTags.DIRT), "Invalid template; not dirt");
        DebugifyTestUtilites.assertBlockFill(ctx, pos.above(), pos.offset(1, 1, 1), b -> b == Blocks.AIR, "Invalid template; not air");
        ctx.assertBlockState(pos.offset(-1, 1, -1), b -> !b.canBeReplaced(), () -> "Invalid template; NW block not solid");
    }
}
