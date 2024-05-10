package maxhyper.dtaether.genfeatures;

import com.ferreusveritas.dynamictrees.api.registry.Registry;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeature;
import maxhyper.dtaether.DynamicTreesAether;

public class DTAetherGenFeatures {

    public static final GenFeature ALTERNATIVE_BRANCH = new AlternativeBranchGenFeature(DynamicTreesAether.location("alt_branch"));
    public static final GenFeature SEASONAL_CONDITION = new SeasonalConditionGenFeature(DynamicTreesAether.location("seasonal_condition"));
    public static final GenFeature HOLIDAY_DECORATION = new SeasonalConditionGenFeature(DynamicTreesAether.location("holiday_decoration"));

    public static void register(final Registry<GenFeature> registry) {
        registry.registerAll(
                ALTERNATIVE_BRANCH, SEASONAL_CONDITION, HOLIDAY_DECORATION
        );
    }

}
