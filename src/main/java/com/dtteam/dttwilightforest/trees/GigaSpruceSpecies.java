package com.dtteam.dttwilightforest.trees;

import com.dtteam.dynamictrees.api.registry.TypedRegistry;
import com.dtteam.dynamictrees.block.sapling.DynamicSaplingBlock;
import com.dtteam.dynamictrees.block.leaves.LeavesProperties;
import com.dtteam.dynamictrees.tree.family.Family;
import com.dtteam.dynamictrees.tree.species.Species;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public class GigaSpruceSpecies extends Species {

    public static final TypedRegistry.EntryType<Species> TYPE = createDefaultType(GigaSpruceSpecies::new);

    public GigaSpruceSpecies(ResourceLocation resourceLocation, Family family, LeavesProperties leavesProperties) {
        super(resourceLocation, family, leavesProperties);
    }

    //This species is just to fix a particular bug in DT. Will not be needed on 1.20.
    @Override
    public Optional<DynamicSaplingBlock> getSapling() {
        if (isMegaSpecies()){
            if (getPreMegaSpecies().isMegaSpecies()){
                return getPreMegaSpecies().getPreMegaSpecies().getSapling();
            }
            return getPreMegaSpecies().getSapling();
        }
        return super.getSapling();
    }
}
