package maxhyper.dtaether.mushroomlogic;

import com.ferreusveritas.dynamictrees.api.configuration.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import com.ferreusveritas.dynamictreesplus.block.mushroom.CapProperties;
import com.ferreusveritas.dynamictreesplus.block.mushroom.DynamicCapCenterBlock;
import com.ferreusveritas.dynamictreesplus.systems.mushroomlogic.MushroomShapeConfiguration;
import com.ferreusveritas.dynamictreesplus.systems.mushroomlogic.context.MushroomCapContext;
import com.ferreusveritas.dynamictreesplus.systems.mushroomlogic.shapekits.MushroomShapeKit;
import com.ferreusveritas.dynamictreesplus.tree.HugeMushroomSpecies;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public class BallShape extends MushroomShapeKit {
    public static final ConfigurationProperty<Integer> CURVE_POWER = ConfigurationProperty.integer("curve_power");
    public static final ConfigurationProperty<Float> CURVE_HEIGHT_OFFSET = ConfigurationProperty.floatProperty("curve_height_offset");
    public static final ConfigurationProperty<Float> MIN_AGE_CURVE_FACTOR = ConfigurationProperty.floatProperty("min_age_curve_factor");
    public static final ConfigurationProperty<Float> MAX_AGE_CURVE_FACTOR = ConfigurationProperty.floatProperty("max_age_curve_factor");
    public static final ConfigurationProperty<Float> CURVE_FACTOR_VARIATION = ConfigurationProperty.floatProperty("curve_factor_variation");
    public static final ConfigurationProperty<Integer> POINTED_TIP_AGE = ConfigurationProperty.integer("pointed_tip_max_age");

    public BallShape(ResourceLocation registryName) {
        super(registryName);
    }

    public @NotNull MushroomShapeConfiguration getDefaultConfiguration() {
        return this.defaultConfiguration.with(CHANCE_TO_AGE, 0.75F).with(MAX_CAP_AGE, 6).with(CURVE_POWER, 3).with(CURVE_HEIGHT_OFFSET, 0.0F).with(MIN_AGE_CURVE_FACTOR, 2.0F).with(MAX_AGE_CURVE_FACTOR, 0.5F).with(CURVE_FACTOR_VARIATION, 0.1F).with(POINTED_TIP_AGE, 0);
    }

    protected void registerProperties() {
        this.register(CHANCE_TO_AGE, MAX_CAP_AGE, CURVE_POWER, CURVE_HEIGHT_OFFSET, MIN_AGE_CURVE_FACTOR, MAX_AGE_CURVE_FACTOR, CURVE_FACTOR_VARIATION, POINTED_TIP_AGE);
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
            float height_offset = configuration.get(CURVE_HEIGHT_OFFSET);
            int power = configuration.get(CURVE_POWER);
            float fac = this.calculateFactor(configuration, context);
            int age = context.age();
            int y = 0;
            int radius = 1;

            for (int i = 1; i <= age; ++i) {
                int nextY = fac == 0.0F ? 0 : (int) Math.floor(Math.pow((fac * (float) radius), power) - (double) height_offset);
                boolean moveY = i == 1 ? age <= configuration.get(POINTED_TIP_AGE) : nextY != y;
                if (moveY) {
                    y += (int) Math.signum(fac);
                }

                BlockPos pos = context.pos().below(y);
                if (action == ringAction.CLEAR) {
                    for (int j=1; j<=radius; j++){
                        centerBlock.clearRing(context.level(), pos, j);
                    }

                } else if (action == ringAction.PLACE) {
                    for (int j=1; j<=radius; j++){
                        if (!centerBlock.placeRing(context.level(), pos, j, j, moveY, fac < 0.0F && i < age)) {
                            break;
                        }
                    }

                } else if (action == ringAction.GET) {
                    for (int j=1; j<=radius; j++){
                        ringPositions.addAll(centerBlock.getRing(context.level(), pos, j));
                    }
                }

                if (i >= nextY) {
                    ++radius;
                }
            }

            ringPositions.add(context.pos());
        }
        return ringPositions;
    }

    private float calculateFactor(MushroomShapeConfiguration configuration, MushroomCapContext context) {
        HugeMushroomSpecies species = context.species();
        CapProperties properties = species.getCapProperties();
        int age = context.age();
        if (age == 0) {
            return 0.0F;
        } else {
            float factorMin = configuration.get(MIN_AGE_CURVE_FACTOR);
            float factorMax = configuration.get(MAX_AGE_CURVE_FACTOR);
            float factorVariation = configuration.get(CURVE_FACTOR_VARIATION);
            float rand = (float) CoordUtils.coordHashCode(new BlockPos(context.pos().getX(), 0, context.pos().getZ()), 2) / 65535.0F;
            float var = rand * factorVariation * 2.0F - factorVariation;
            return (float)age / (float)properties.getMaxAge(species) * (factorMax - factorMin) + factorMin + var;
        }
    }

    enum ringAction {
        PLACE,
        CLEAR,
        GET;

        ringAction() {
        }
    }
}
