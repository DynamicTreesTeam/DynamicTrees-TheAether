package maxhyper.dtaether.data;

import com.dtteam.dynamictrees.data.generator.LeavesStateGenerator;
import com.dtteam.dynamictrees.block.leaves.LeavesProperties;
import com.dtteam.dynamictrees.data.DTDataProvider;
import com.dtteam.dynamictrees.data.provider.DTBlockStateProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import maxhyper.dtaether.blocks.SnowyLeavesBlock;
import maxhyper.dtaether.blocks.SnowyScruffyLeavesProperties;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;

public class SnowyLeavesStateGenerator extends LeavesStateGenerator {

    @Override
    public void generate(DTDataProvider.BlockState prov, LeavesProperties input, Dependencies dependencies) {
        if (!(prov instanceof DTBlockStateProvider provider)) {
            return;
        }
        provider.getVariantBuilder(dependencies.get(LEAVES)).partialState()
                .with(SnowyLeavesBlock.SNOWY, false)
                .addModels(new ConfiguredModel(
                        provider.models().getExistingFile(
                                input.getModelPath(LeavesProperties.LEAVES).orElse(
                                        provider.block(BuiltInRegistries.BLOCK.getKey(dependencies.get(PRIMITIVE_LEAVES)))
                                )
                        )
                )).partialState()
                .with(SnowyLeavesBlock.SNOWY, true)
                .addModels(new ConfiguredModel(
                        provider.models().getExistingFile(
                                input.getModelPath("snowy_leaves").orElse(
                                        provider.block(BuiltInRegistries.BLOCK.getKey(dependencies.get(PRIMITIVE_LEAVES)))
                                )
                        )
                ));
    }

}
