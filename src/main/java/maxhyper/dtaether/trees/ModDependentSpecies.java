package maxhyper.dtaether.trees;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.block.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.deserialisation.JsonDeserialisers;
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
    protected static String ONLY_IF_NOT_LOADED = "only_if_not_loaded";
    protected static String FAMILY = "family";
    protected static String FEATURES = "features";
    protected static String PRIMITIVE_SAPLINGS = "primitive_saplings";
    protected static String DROP_SEEDS = "drop_seeds";
    public void setLoadVariantProperties (JsonObject object){
        String modId = object.get(ONLY_IF_LOADED).getAsString();
        if (ModList.get().isLoaded(modId) && (!object.has(ONLY_IF_NOT_LOADED) || !ModList.get().isLoaded(object.get(ONLY_IF_NOT_LOADED).getAsString()))){
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
        if (ModList.get().isLoaded(modId) && (!object.has(ONLY_IF_NOT_LOADED) || !ModList.get().isLoaded(object.get(ONLY_IF_NOT_LOADED).getAsString()))){
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
            if (object.has(PRIMITIVE_SAPLINGS)){
                JsonArray array = object.getAsJsonArray(PRIMITIVE_SAPLINGS);
                array.forEach((elem)->
                        JsonDeserialisers.SEED_SAPLING_RECIPE.deserialiseIfValid(elem, (res)-> addPrimitiveSaplingRecipe(res.get()))
                );
            }
            if (object.has(DROP_SEEDS)){
                setDropSeeds(object.get(DROP_SEEDS).getAsBoolean());
            }
        }
    }

}
