package maxhyper.dtaether.data;

import com.ferreusveritas.dynamictrees.api.data.Generator;
import com.ferreusveritas.dynamictrees.data.provider.DTLangProvider;

public class DTAetherExtraLang implements Generator<DTLangProvider, String> {
    public DTAetherExtraLang() {
    }

    public void generate(DTLangProvider provider, String input, Generator.Dependencies dependencies) {
        provider.add("block.dtaether.imbued_skyroot_branch", "Skyroot Tree");
        provider.add("block.dtaether.imbued_blightwillow_branch", "Blightwillow Tree");
    }

    public Generator.Dependencies gatherDependencies(String input) {
        return new Generator.Dependencies();
    }
}
