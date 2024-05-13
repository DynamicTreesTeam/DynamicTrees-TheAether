package maxhyper.dtaether.trees;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.api.resource.loading.preparation.JsonRegistryResourceLoader;
import com.ferreusveritas.dynamictrees.block.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.deserialisation.JsonDeserialisers;
import com.ferreusveritas.dynamictrees.resources.loader.SpeciesResourceLoader;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeatureConfiguration;
import com.ferreusveritas.dynamictrees.tree.family.Family;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;

public class ModDependentSpecies extends Species {

    public static final TypedRegistry.EntryType<Species> TYPE = createDefaultType(ModDependentSpecies::new);

    Family original_family;

    public ModDependentSpecies(ResourceLocation name, Family family, LeavesProperties leavesProperties) {
        super(name, family, leavesProperties);
        original_family = getFamily();
    }

    protected static String ONLY_IF_LOADED = "only_if_loaded";
    protected static String FAMILY = "family";
    protected static String FEATURES = "features";
    public void setLoadVariantProperties (JsonObject object){
        String modId = object.get(ONLY_IF_LOADED).getAsString();
        if (ModList.get().isLoaded(modId)){
            if (object.has(FAMILY)){
                ResourceLocation familyRes = new ResourceLocation(object.get(FAMILY).getAsString());
                Family family = Family.REGISTRY.get(familyRes);
                this.setFamily(family);
                setSaplingName(this.getRegistryName().getPath()+"_sapling_"+modId);
            }
        }
    }
    public void setReloadVariantProperties (JsonObject object){
        String modId = object.get(ONLY_IF_LOADED).getAsString();
        if (ModList.get().isLoaded(modId)){
            if (object.has(FEATURES)){
                JsonArray array = object.getAsJsonArray(FEATURES);
                array.forEach((elem)->{
                            if (elem.isJsonObject())
                                JsonDeserialisers.CONFIGURED_GEN_FEATURE.deserialiseIfValid(elem, (res)-> addGenFeature(res.get()));
                            else if (elem.isJsonPrimitive())
                                JsonDeserialisers.GEN_FEATURE.deserialiseIfValid(elem, (res)-> addGenFeature(res.get()));
                        }
                );
            }
        }
    }

}
