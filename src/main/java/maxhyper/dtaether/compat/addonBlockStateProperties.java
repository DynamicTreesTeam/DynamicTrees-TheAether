package maxhyper.dtaether.compat;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public interface addonBlockStateProperties {

    BlockState setPrismaticness (BlockState state, int petal, int value);
    boolean hasPrismaticness (BlockState state);

    IntegerProperty getLeafPileLayersProperty ();

}
