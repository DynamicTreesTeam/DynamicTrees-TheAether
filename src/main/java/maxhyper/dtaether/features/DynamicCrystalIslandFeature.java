package maxhyper.dtaether.features;

import com.aetherteam.aether.data.resources.AetherFeatureStates;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.systems.poissondisc.UniversalPoissonDiscProvider;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import com.ferreusveritas.dynamictrees.util.LevelContext;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import com.ferreusveritas.dynamictrees.worldgen.DynamicTreeFeature;
import com.ferreusveritas.dynamictrees.worldgen.GenerationContext;
import com.mojang.serialization.Codec;
import maxhyper.dtaether.DynamicTreesAether;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public class DynamicCrystalIslandFeature extends Feature<NoneFeatureConfiguration> {

    public DynamicCrystalIslandFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    public boolean place(@NotNull FeaturePlaceContext<NoneFeatureConfiguration> context) {
        LevelContext levelContext = LevelContext.create(context.level());
        ChunkPos chunkPos = context.level().getChunk(context.origin()).getPos();
        AtomicReference<Double> closestRingDistance = new AtomicReference<>((double) 1024);
        AtomicReference<BlockPos> closestRing = new AtomicReference<>(context.origin());
        DynamicTreeFeature.DISC_PROVIDER.getPoissonDiscs(levelContext, chunkPos).forEach((disc) -> {
            BlockPos ringPos = new BlockPos(disc.x, context.origin().getY(), disc.z);
            double dist = context.origin().distSqr(ringPos);
            if (dist < closestRingDistance.get()){
                closestRingDistance.set(dist);
                closestRing.set(ringPos);
            }
        });
        BlockPos newOrigin = closestRing.get();
        Species crystalSpecies = Species.REGISTRY.get(DynamicTreesAether.location("crystal"));
        setBlock(context.level(), newOrigin, AetherFeatureStates.AETHER_GRASS_BLOCK);
        if (crystalSpecies.isValid() && crystalSpecies.generate(new GenerationContext(levelContext, crystalSpecies, newOrigin, newOrigin.mutable(), context.level().getBiome(newOrigin), Direction.Plane.HORIZONTAL.getRandomDirection(context.random()), 8, SafeChunkBounds.ANY_WG))){
            for(int i = 0; i < 3; ++i) {
                BlockState state;
                if (i == 0) {
                    state = AetherFeatureStates.AETHER_GRASS_BLOCK;
                } else {
                    state = AetherFeatureStates.HOLYSTONE;
                }

                if (i != 0)
                    this.setBlock(context.level(), newOrigin.below(i), state);
                int finalI = i;
                Arrays.stream(Direction.values()).toList().subList(2, 6).forEach((direction) -> {
                    this.setBlock(context.level(), newOrigin.relative(direction).below(finalI), state);
                    if (finalI != 2) {
                        this.setBlock(context.level(), newOrigin.relative(direction, 2).below(finalI), state);
                        this.setBlock(context.level(), newOrigin.relative(direction).relative(direction.getClockWise()).below(finalI), state);
                    }

                });
            }
            return true;
        } else return false;
    }
}
