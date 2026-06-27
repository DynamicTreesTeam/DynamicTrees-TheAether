package com.dtteam.dtaether.data;

import com.dtteam.dynamictrees.data.Generator;
import com.dtteam.dynamictrees.data.DTDataProvider;
import com.dtteam.dynamictrees.data.provider.DTLangProvider;

public class DTAetherExtraLang implements Generator<DTDataProvider.Language, String> {
    public DTAetherExtraLang() {
    }

    public void generate(DTDataProvider.Language provider, String input, Generator.Dependencies dependencies) {
        if (provider instanceof DTLangProvider langProvider) {
            langProvider.add("block.dtaether.imbued_skyroot_branch", "Skyroot Tree");
        }
    }

    public Generator.Dependencies gatherDependencies(String input) {
        return new Generator.Dependencies();
    }
}
