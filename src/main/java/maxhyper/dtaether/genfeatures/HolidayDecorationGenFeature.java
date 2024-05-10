package maxhyper.dtaether.genfeatures;
import com.aetherteam.aether.AetherConfig;
import com.aetherteam.aether.block.AetherBlocks;
import com.aetherteam.aether.world.treedecorator.HolidayTreeDecorator;
import com.ferreusveritas.dynamictrees.api.configuration.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeatureConfiguration;
import com.ferreusveritas.dynamictrees.systems.genfeature.context.FullGenerationContext;
import com.ferreusveritas.dynamictrees.systems.genfeature.context.PostGenerationContext;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import com.ferreusveritas.dynamictrees.worldgen.GenerationContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;

import java.util.Calendar;
import java.util.Random;

/**
 * @author Max Hyper
 */
public class HolidayDecorationGenFeature extends GenFeature {

    public static final ConfigurationProperty<Float> GIFT_CHANCE = ConfigurationProperty.floatProperty("gift_chance");

    public HolidayDecorationGenFeature(ResourceLocation registryName) {
        super(registryName);
    }

    public GenFeatureConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(GIFT_CHANCE, 0.1f);
    }

    @Override
    protected void registerProperties() {
        this.register(GIFT_CHANCE);
    }

    @Override
    protected boolean postGenerate(GenFeatureConfiguration configuration, PostGenerationContext context) {
        return super.postGenerate(configuration, context);
    }

    private void placeCircle(GenFeatureConfiguration configuration, TreeDecorator.Context context, BlockPos pos) {
        LevelSimulatedReader level = context.level();
        RandomSource random = context.random();
        this.placeBlockAt(configuration, context, level, random, pos, 0.0F);
        int radius = 10;

        for(int z = 1; z < radius; ++z) {
            for(int x = 0; x < radius; ++x) {
                if (Mth.square(x) + Mth.square(z) <= Mth.square(radius)) {
                    float distance = (float)Math.sqrt((double)(Mth.square(x) + Mth.square(z))) / (float)Mth.square(radius);
                    this.placeBlockAt(configuration, context, level, random, pos.offset(x, 0, z), distance);
                    this.placeBlockAt(configuration, context, level, random, pos.offset(-x, 0, -z), distance);
                    this.placeBlockAt(configuration, context, level, random, pos.offset(-z, 0, x), distance);
                    this.placeBlockAt(configuration, context, level, random, pos.offset(z, 0, -x), distance);
                }
            }
        }

    }

    /**
     * Code shamelessly stolen from {@link HolidayTreeDecorator}
     */
    private void placeBlockAt(GenFeatureConfiguration configuration, TreeDecorator.Context context, LevelSimulatedReader level, RandomSource random, BlockPos pos, float distance) {
        for(int i = 9; i >= -4; --i) {
            BlockPos blockPos = pos.above(i);
            if (context.isAir(blockPos.above()) && (level.isStateAtPosition(blockPos, HolidayDecorationGenFeature::isAetherGrass) || level.isStateAtPosition(blockPos, HolidayDecorationGenFeature::isLeaves) || Feature.isGrassOrDirt(level, blockPos)) && context.isAir(blockPos.above(4)) && distance <= random.nextFloat() / 2.0F * (1.0F - distance)) {
                if (level.isStateAtPosition(blockPos, HolidayDecorationGenFeature::isLeaves)) {
                    context.setBlock(blockPos.above(), Blocks.SNOW.defaultBlockState());
                } else {
                    context.setBlock(blockPos.above(), getSnowOrGift(random, blockPos, configuration.get(GIFT_CHANCE)));
                }
            }
        }

    }

    private static BlockState getSnowOrGift(RandomSource random, BlockPos pos, float chance){
        return random.nextFloat() < chance ? Blocks.SNOW.defaultBlockState() : AetherBlocks.PRESENT.get().defaultBlockState();
    }

    private static boolean isAetherGrass(BlockState state) {
        return state.is(com.aetherteam.aether.AetherTags.Blocks.AETHER_DIRT);
    }

    private static boolean isLeaves(BlockState state) {
        return state.is(BlockTags.LEAVES);
    }


}