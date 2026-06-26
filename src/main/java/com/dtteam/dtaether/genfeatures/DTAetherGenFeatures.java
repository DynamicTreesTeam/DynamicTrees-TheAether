package com.dtteam.dtaether.genfeatures;

import com.dtteam.dynamictrees.api.registry.Registry;
import com.dtteam.dynamictrees.systems.genfeature.GenFeature;

import com.dtteam.dtaether.DynamicTreesAether;

public class DTAetherGenFeatures {

    public static final GenFeature SEASONAL_CONDITION = new SeasonalConditionGenFeature(DynamicTreesAether.location("seasonal_condition"));
    public static final GenFeature HOLIDAY_DECORATION = new HolidayDecorationGenFeature(DynamicTreesAether.location("holiday_decoration"));
    public static final GenFeature NEGATIVE_BIOME_PREDICATE = new NegativeBiomePredicateGenFeature(DynamicTreesAether.location("negative_biome_predicate"));

    public static void register(final Registry<GenFeature> registry) {
        registry.registerAll(SEASONAL_CONDITION, HOLIDAY_DECORATION,
                NEGATIVE_BIOME_PREDICATE
        );
    }

}
