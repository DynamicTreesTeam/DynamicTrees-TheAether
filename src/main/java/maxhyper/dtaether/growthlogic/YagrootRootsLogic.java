package maxhyper.dtaether.growthlogic;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.configuration.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKitConfiguration;
import com.ferreusveritas.dynamictrees.growthlogic.MangroveRootsLogic;
import com.ferreusveritas.dynamictrees.growthlogic.context.DirectionManipulationContext;
import com.ferreusveritas.dynamictrees.growthlogic.context.DirectionSelectionContext;
import com.ferreusveritas.dynamictrees.growthlogic.context.PositionalSpeciesContext;
import com.ferreusveritas.dynamictrees.tree.species.MangroveSpecies;
import com.ferreusveritas.dynamictrees.util.MathHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public class YagrootRootsLogic extends MangroveRootsLogic {

    public static final ConfigurationProperty<Integer> HORIZONTAL_PROBABILITY = ConfigurationProperty.integer("horizontal_probability");

    public YagrootRootsLogic(ResourceLocation registryName) {
        super(registryName);
    }

    protected GrowthLogicKitConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(HORIZONTAL_PROBABILITY, 4)
                ;
    }

    protected void registerProperties() {
        this.register(HORIZONTAL_PROBABILITY);
    }


    @Override
    public int[] populateDirectionProbabilityMap(GrowthLogicKitConfiguration configuration,
                                                 DirectionManipulationContext context) {
        final int[] probMap = context.probMap();
        final Direction originDir = context.signal().dir.getOpposite();
        final Direction defaultDir = context.signal().defaultDir; //usually UP

        BlockPos downPos = context.pos().offset(defaultDir.getNormal());
        boolean isOverGround = context.level().getBlockState(downPos).isFaceSturdy(context.level(), downPos, defaultDir.getOpposite());

        for (Direction dir : Direction.values()) {
            if (!dir.equals(originDir)) {

                if (dir.getAxis().isHorizontal()) probMap[dir.get3DDataValue()] = isOverGround ? configuration.get(HORIZONTAL_PROBABILITY) : 0;
                //Main direction use up probability
                if (dir.equals(defaultDir)) probMap[dir.get3DDataValue()] = 1;
                //Never go against main dir
                if (dir.equals(defaultDir.getOpposite())) probMap[dir.get3DDataValue()] = 0;

                final BlockPos deltaPos = context.pos().relative(dir);
                // Check probability for surrounding blocks.
                // Typically, Air: 1, Leaves: 2, Branches: 2 + radius
                final BlockState deltaBlockState = context.level().getBlockState(deltaPos);
                probMap[dir.get3DDataValue()] += TreeHelper.getTreePart(deltaBlockState)
                        .probabilityForBlock(deltaBlockState, context.level(), deltaPos, context.branch());
            }
        }

        if (context.signal().isInTrunk())
            probMap[defaultDir.ordinal()] =  0;

        probMap[defaultDir.getOpposite().ordinal()] = 0;
        probMap[originDir.ordinal()] = 0;

        return probMap;
    }
}
