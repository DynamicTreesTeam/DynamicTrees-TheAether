package maxhyper.dtaether.growthlogic;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.configuration.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKitConfiguration;
import com.ferreusveritas.dynamictrees.growthlogic.MangroveRootsLogic;
import com.ferreusveritas.dynamictrees.growthlogic.context.DirectionManipulationContext;
import com.ferreusveritas.dynamictrees.growthlogic.context.DirectionSelectionContext;
import com.ferreusveritas.dynamictrees.growthlogic.context.PositionalSpeciesContext;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public class BlightwillowLogic extends GrowthLogicKit {

    public static final ConfigurationProperty<Float> HORIZONTAL_SPREAD = ConfigurationProperty.floatProperty("horizontal_spread");

    public BlightwillowLogic(ResourceLocation registryName) {
        super(registryName);
    }

    protected GrowthLogicKitConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(HORIZONTAL_SPREAD, 5f);
    }

    protected void registerProperties() {
        this.register(HORIZONTAL_SPREAD);
    }


    @Override
    public Direction selectNewDirection(GrowthLogicKitConfiguration configuration, DirectionSelectionContext context) {
        final Direction newDir = super.selectNewDirection(configuration, context);
        BlockPos d = context.signal().delta;
        boolean turningOutOfX = d.getZ() == 0 && d.getX() != 0 && newDir.getAxis() == Direction.Axis.Z;
        boolean turningOutOfZ = d.getX() == 0 && d.getZ() != 0 && newDir.getAxis() == Direction.Axis.X;
        boolean turningAway = !context.signal().isInTrunk() && newDir.getAxis() != Direction.Axis.Y && context.signal().energy % 2 == 0;
        if (turningOutOfX || turningOutOfZ //The branch was on an axis and is turning out of it
        || turningAway) { //the branch wants to go away when it should go up
            context.signal().energy = Math.min(context.signal().energy, 1);
        }
        return newDir;
    }

    @Override
    public int[] populateDirectionProbabilityMap(GrowthLogicKitConfiguration configuration, DirectionManipulationContext context) {
        int[] probMap = super.populateDirectionProbabilityMap(configuration, context);
        Direction fromDir = context.signal().dir.getOpposite();

        //Force branch as soon as minBranchHeight is met
        if (context.signal().isInTrunk()){
            probMap[Direction.UP.ordinal()] = 0;
        } else {
            double sideChance = context.signal().energy % 2 == 0 ? 1 : 8;
            double upChance = context.signal().energy % 2 == 0 ? 16 : 1;

            for (Direction dir : Direction.values()) {
                if (dir.getAxis() != Direction.Axis.Y){
                    float dot = context.signal().delta.getX() * dir.getNormal().getX() + context.signal().delta.getZ() * dir.getNormal().getZ();
                    probMap[dir.ordinal()] = dot < 0 ? 0 : (int)sideChance;
                }
                if (!dir.equals(fromDir) && probMap[dir.ordinal()] != 0) {
                    final BlockPos deltaPos = context.pos().relative(dir);
                    // Check probability for surrounding blocks.
                    // Typically, Air: 1, Leaves: 2, Branches: 2 + radius
                    final BlockState deltaBlockState = context.level().getBlockState(deltaPos);
                    probMap[dir.get3DDataValue()] += TreeHelper.getTreePart(deltaBlockState)
                            .probabilityForBlock(deltaBlockState, context.level(), deltaPos, context.branch());
                }
            }

            probMap[Direction.UP.ordinal()] = (int)upChance;
        }

        //we disable the "from" direction and the down direction
        probMap[fromDir.ordinal()] = probMap[Direction.DOWN.ordinal()] = 0;
        return probMap;
    }

}
