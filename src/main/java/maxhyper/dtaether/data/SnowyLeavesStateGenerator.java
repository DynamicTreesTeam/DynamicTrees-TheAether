package maxhyper.dtaether.data;

import com.ferreusveritas.dynamictrees.api.data.LeavesStateGenerator;
import com.ferreusveritas.dynamictrees.block.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.data.provider.DTBlockStateProvider;
import maxhyper.dtaether.blocks.SnowyLeavesBlock;
import maxhyper.dtaether.blocks.SnowyScruffyLeavesProperties;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.registries.ForgeRegistries;

public class SnowyLeavesStateGenerator extends LeavesStateGenerator {

    @Override
    public void generate(DTBlockStateProvider provider, LeavesProperties input, Dependencies dependencies) {
        provider.getVariantBuilder(dependencies.get(LEAVES)).partialState()
                .with(SnowyLeavesBlock.SNOWY, false)
                .addModels(new ConfiguredModel(
                        provider.models().getExistingFile(
                                input.getModelPath(LeavesProperties.LEAVES).orElse(
                                        provider.block(ForgeRegistries.BLOCKS.getKey(dependencies.get(PRIMITIVE_LEAVES)))
                                )
                        )
                )).partialState()
                .with(SnowyLeavesBlock.SNOWY, true)
                .addModels(new ConfiguredModel(
                        provider.models().getExistingFile(
                                input.getModelPath("snowy_leaves").orElse(
                                        provider.block(ForgeRegistries.BLOCKS.getKey(dependencies.get(PRIMITIVE_LEAVES)))
                                )
                        )
                ));
    }

}
