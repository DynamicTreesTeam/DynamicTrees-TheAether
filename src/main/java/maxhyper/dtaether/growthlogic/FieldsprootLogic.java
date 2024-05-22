package maxhyper.dtaether.growthlogic;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.configuration.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.block.branch.BranchBlock;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKitConfiguration;
import com.ferreusveritas.dynamictrees.growthlogic.MangroveRootsLogic;
import com.ferreusveritas.dynamictrees.growthlogic.context.DirectionManipulationContext;
import com.ferreusveritas.dynamictrees.growthlogic.context.PositionalSpeciesContext;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.LinkedList;
import java.util.List;

public class FieldsprootLogic extends GrowthLogicKit {

    public static final ConfigurationProperty<Integer> SPLIT_ENDS_ENERGY = ConfigurationProperty.integer("split_ends_energy");
    public static final ConfigurationProperty<Integer> FIRST_TWIST_LENGTH = ConfigurationProperty.integer("first_twist_length");
    public static final ConfigurationProperty<Integer> LAST_TWIST_LENGTH = ConfigurationProperty.integer("last_twist_length");

    public FieldsprootLogic(ResourceLocation registryName) {
        super(registryName);
    }

    protected GrowthLogicKitConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(SPLIT_ENDS_ENERGY, 4)
                .with(FIRST_TWIST_LENGTH, 4)
                .with(LAST_TWIST_LENGTH, 4)
                .with(HEIGHT_VARIATION, 3);
    }

    protected void registerProperties() {
        this.register(SPLIT_ENDS_ENERGY, FIRST_TWIST_LENGTH, LAST_TWIST_LENGTH, HEIGHT_VARIATION);
    }


    @Override
    public int[] populateDirectionProbabilityMap(GrowthLogicKitConfiguration configuration, DirectionManipulationContext context) {
        int[] probMap = super.populateDirectionProbabilityMap(configuration, context);
        Level level = context.level();
        Direction fromDir = context.signal().dir.getOpposite();
        List<Direction> validDirs = getBranchesAround(level, context.pos(), fromDir);
        if (context.signal().energy > configuration.get(SPLIT_ENDS_ENERGY)){
            if (validDirs.size() >= 1){
                int[] newProb = new int[6];
                validDirs.forEach((d)->newProb[d.ordinal()]=1);
                probMap = newProb;
            } else {
                int canopyEnergy = configuration.get(SPLIT_ENDS_ENERGY) + 2;
                int lowestBranch = configuration.getLowestBranchHeight(new PositionalSpeciesContext(context.level(), context.signal().rootPos, context.species()));
                int firstTwist = lowestBranch + configuration.get(FIRST_TWIST_LENGTH);
                int lastTwist = canopyEnergy + configuration.get(LAST_TWIST_LENGTH);

                float energy = context.signal().energy;
                int steps = context.signal().numSteps;
                boolean goUp = steps == firstTwist || steps == firstTwist+1 || energy == lastTwist || energy == lastTwist+1;
                if (goUp || context.signal().energy <= canopyEnergy)
                    return new int[]{0,1,0,0,0,0};

                boolean blocked = true;
                for (Direction dir : Direction.Plane.HORIZONTAL){
                    if (probMap[dir.ordinal()] != 0){
                        BlockPos pos = context.pos().offset(dir.getNormal());
                        BlockState offState = level.getBlockState(pos);
                        if ((TreeHelper.isLeaves(offState) || level.isEmptyBlock(pos) || offState.getMaterial().isReplaceable())
                                && !BranchBlock.isNextToBranch(level, pos, dir.getOpposite())){
                            blocked = false;
                            break;
                        }
                    }
                }
                if (!blocked)
                    probMap[Direction.UP.ordinal()] = 0;
            }
        }


        probMap[fromDir.ordinal()] = probMap[Direction.DOWN.ordinal()] = 0;
        return probMap;
    }

    private List<Direction> getBranchesAround (Level level, BlockPos pos, Direction fromDir){
        List<Direction> validDirs = new LinkedList<>();
        for (Direction dir : Direction.values()){
            if (dir != fromDir && TreeHelper.isBranch(level.getBlockState(pos.offset(dir.getNormal())))){
                validDirs.add(dir);
            }
        }
        return validDirs;
    }

    @Override
    public int getLowestBranchHeight(GrowthLogicKitConfiguration configuration, PositionalSpeciesContext context) {
        return (int)(super.getLowestBranchHeight(configuration, context) + getHashVariation(configuration, context));
    }

    protected float getHashVariation (GrowthLogicKitConfiguration configuration, PositionalSpeciesContext context){
        long day = context.level().getGameTime() / 24000L;
        int month = (int) day / 30;//Change the hashs every in-game month

        return  (CoordUtils.coordHashCode(context.pos().above(month), 2) % configuration.get(HEIGHT_VARIATION));
    }
}
