package maxhyper.dtaether.mushroomlogic;

import com.ferreusveritas.dynamictrees.api.configuration.ConfigurationProperty;
import com.ferreusveritas.dynamictreesplus.block.mushroom.DynamicCapCenterBlock;
import com.ferreusveritas.dynamictreesplus.systems.mushroomlogic.MushroomShapeConfiguration;
import com.ferreusveritas.dynamictreesplus.systems.mushroomlogic.context.MushroomCapContext;
import com.ferreusveritas.dynamictreesplus.systems.mushroomlogic.shapekits.MushroomShapeKit;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public class BallShape extends MushroomShapeKit {
    public static final ConfigurationProperty<Float> HORIZONTAL_STRETCH = ConfigurationProperty.floatProperty("chance_to_age");

    public BallShape(ResourceLocation registryName) {
        super(registryName);
    }

    public @NotNull MushroomShapeConfiguration getDefaultConfiguration() {
        return this.defaultConfiguration
                .with(CHANCE_TO_AGE, 0.75F)
                .with(MAX_CAP_AGE, 6)
                .with(HORIZONTAL_STRETCH, 0.88f);
    }

    protected void registerProperties() {
        this.register(CHANCE_TO_AGE, MAX_CAP_AGE, HORIZONTAL_STRETCH);
    }

    public List<BlockPos> getShapeCluster(MushroomShapeConfiguration configuration, MushroomCapContext context) {
        return this.placeRing(configuration, context, ringAction.GET);
    }

    public int getMaxCapAge(MushroomShapeConfiguration configuration) {
        return configuration.get(MAX_CAP_AGE);
    }

    public float getChanceToAge(MushroomShapeConfiguration configuration) {
        return configuration.get(CHANCE_TO_AGE);
    }

    public void generateMushroomCap(MushroomShapeConfiguration configuration, MushroomCapContext context) {
        this.placeRing(configuration, context, ringAction.PLACE);
    }

    public void clearMushroomCap(MushroomShapeConfiguration configuration, MushroomCapContext context) {
        this.placeRing(configuration, context, ringAction.CLEAR);
    }

    private List<BlockPos> placeRing(MushroomShapeConfiguration configuration, MushroomCapContext context, ringAction action) {
        DynamicCapCenterBlock centerBlock = context.species().getCapProperties().getDynamicCapCenterBlock().orElse(null);
        List<BlockPos> ringPositions = new LinkedList<>();
        if (centerBlock != null) {
            int age = context.age();

            float yOffset = age % 2 == 0 ? -0.5f : 0f;
            float semiRadius = age / 2f - 0.5f;
            float horizontalStretch = configuration.get(HORIZONTAL_STRETCH);

            int prevRad = 0;
            for (int i = 0; i < age; ++i) {
                int y = i - (int)Math.floor(semiRadius);
                int yDelta = Math.abs(y);
                int radius = Math.max((int)Math.ceil(horizontalStretch * Math.sqrt(Math.pow(semiRadius, 2) - Math.pow(y+yOffset,2))), 1);
                BlockPos pos = context.pos().below(y);
                BlockState posState = context.level().getBlockState(pos);
                if (action == ringAction.CLEAR) {
                    for (int j=1; j<=radius; j++){
                        centerBlock.clearRing(context.level(), pos, j);
                    }

                } else if (action == ringAction.PLACE) {
                    for (int j=1; j<=radius; j++){
                        if (DynamicCapCenterBlock.canCapReplace(posState)){
                            context.level().setBlock(pos, centerBlock.properties.getDynamicCapState(Math.max(yDelta, 1)), 3);
                        }
                        if (!centerBlock.placeRing(context.level(), pos, j, j+yDelta, radius != prevRad, false)) {
                            break;
                        }
                    }
                } else if (action == ringAction.GET) {
                    for (int j=1; j<=radius; j++){
                        ringPositions.addAll(centerBlock.getRing(context.level(), pos, j));
                        if (centerBlock.properties.isPartOfCap(posState)){
                            ringPositions.add(pos);
                        }
                    }
                }
                prevRad = radius;
            }
            ringPositions.add(context.pos());
        }
        return ringPositions;
    }

    enum ringAction {
        PLACE,
        CLEAR,
        GET;

        ringAction() {
        }
    }
}
