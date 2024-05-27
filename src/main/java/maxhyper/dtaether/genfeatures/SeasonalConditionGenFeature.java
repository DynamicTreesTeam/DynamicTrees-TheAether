package maxhyper.dtaether.genfeatures;
import com.aetherteam.aether.AetherConfig;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.configuration.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.api.network.MapSignal;
import com.ferreusveritas.dynamictrees.api.network.NodeInspector;
import com.ferreusveritas.dynamictrees.block.branch.BranchBlock;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeatureConfiguration;
import com.ferreusveritas.dynamictrees.systems.genfeature.context.FullGenerationContext;
import com.ferreusveritas.dynamictrees.systems.genfeature.context.PostGenerationContext;
import com.ferreusveritas.dynamictrees.systems.genfeature.context.PostGrowContext;
import com.ferreusveritas.dynamictrees.systems.genfeature.context.PreGenerationContext;
import com.ferreusveritas.dynamictrees.tree.family.Family;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import com.ferreusveritas.dynamictrees.worldgen.GenerationContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;

import java.util.Calendar;
import java.util.stream.Collectors;

/**
 * @author Max Hyper
 */
public class SeasonalConditionGenFeature extends GenFeature {

    public static final ConfigurationProperty<Species> ALTERNATIVE_SPECIES = ConfigurationProperty.property("alternative_species", Species.class);

    public SeasonalConditionGenFeature(ResourceLocation registryName) {
        super(registryName);
    }

    public boolean generateSeasonalTrees (){
        Calendar calendar = Calendar.getInstance();
        boolean isChristmas = calendar.get(Calendar.MONTH) == Calendar.DECEMBER || calendar.get(Calendar.MONTH) == Calendar.JANUARY;
        return AetherConfig.SERVER.generate_holiday_tree_always.get() || (AetherConfig.SERVER.generate_holiday_tree_seasonally.get() && isChristmas);
    }

    public GenFeatureConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(ALTERNATIVE_SPECIES, Species.NULL_SPECIES);
    }

    @Override
    protected void registerProperties() {
        this.register(ALTERNATIVE_SPECIES);
    }

    @Override
    protected boolean generate(GenFeatureConfiguration configuration, FullGenerationContext context) {
        //If seasonal trees should generate then the replacement is NOT activated;
        if (generateSeasonalTrees()) return false;
        //If it's not time for seasonal trees, then the alternative tree is placed instead.
        //We return true because we are custom handing the generation.
        Species alternate = configuration.get(ALTERNATIVE_SPECIES);
        alternate.generate(new GenerationContext(context.levelContext(), alternate, context.pos(), context.pos().mutable(),context.biome(), Direction.Plane.HORIZONTAL.getRandomDirection(context.random()),context.radius(), context.bounds()));
        return true;
    }
}