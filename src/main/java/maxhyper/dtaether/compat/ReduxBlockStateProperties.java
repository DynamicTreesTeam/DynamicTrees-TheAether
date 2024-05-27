package maxhyper.dtaether.compat;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.zepalesque.redux.block.util.state.ReduxStates;
import net.zepalesque.redux.block.util.state.enums.PetalPrismaticness;

public class ReduxBlockStateProperties implements addonBlockStateProperties {

    @Override
    public BlockState setPrismaticness(BlockState state, int petal, int value) {
        PetalPrismaticness val = PetalPrismaticness.getFromIndex(value);
        Property<PetalPrismaticness> prop = null;
        switch (petal){
            case 1 -> prop = ReduxStates.PETAL_1;
            case 2 -> prop = ReduxStates.PETAL_2;
            case 3 -> prop = ReduxStates.PETAL_3;
            case 4 -> prop = ReduxStates.PETAL_4;
        }
        if (prop == null) return state;
        return state.setValue(prop, val);
    }

    @Override
    public boolean hasPrismaticness(BlockState state) {
        return state.hasProperty(ReduxStates.PETAL_1)
                && state.hasProperty(ReduxStates.PETAL_2)
                && state.hasProperty(ReduxStates.PETAL_3)
                && state.hasProperty(ReduxStates.PETAL_4);
    }

    @Override
    public IntegerProperty getLeafPileLayersProperty() {
        return ReduxStates.LEAF_LAYERS;
    }


}
