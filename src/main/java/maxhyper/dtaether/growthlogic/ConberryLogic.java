package maxhyper.dtaether.growthlogic;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.configuration.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKitConfiguration;
import com.ferreusveritas.dynamictrees.growthlogic.context.DirectionManipulationContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.LinkedList;
import java.util.List;

public class ConberryLogic extends GrowthLogicKit {

    public static final ConfigurationProperty<Integer> MINIMUM_TRUNK_BRANCHES = ConfigurationProperty.integer("minimum_trunk_branches");
    public static final ConfigurationProperty<Integer> SPLIT_ENDS_ENERGY = ConfigurationProperty.integer("split_ends_energy");

    public ConberryLogic(ResourceLocation registryName) {
        super(registryName);
    }

    protected GrowthLogicKitConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(MINIMUM_TRUNK_BRANCHES, 2)
                .with(SPLIT_ENDS_ENERGY, 3);
    }

    protected void registerProperties() {
        this.register(MINIMUM_TRUNK_BRANCHES, SPLIT_ENDS_ENERGY);
    }

    @Override
    public int[] populateDirectionProbabilityMap(GrowthLogicKitConfiguration configuration, DirectionManipulationContext context) {
        int[] probMap = super.populateDirectionProbabilityMap(configuration, context);
        Level level = context.level();
        Direction fromDir = context.signal().dir.getOpposite();
        List<Direction> validDirs = getBranchesAround(level, context.pos(), fromDir);
        if ((context.signal().isInTrunk() && validDirs.size() >= configuration.get(MINIMUM_TRUNK_BRANCHES)) ||
                (!context.signal().isInTrunk() && validDirs.size() >= 1 && context.signal().energy > configuration.get(SPLIT_ENDS_ENERGY))){
            int[] newProb = new int[6];
            validDirs.forEach((d)->newProb[d.ordinal()]=1);
            probMap = newProb;
        }
        if (context.signal().isInTrunk()) {
            probMap[0] = probMap[1] = 0;
        }
        //we disable the "from" direction and the down direction
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
}
