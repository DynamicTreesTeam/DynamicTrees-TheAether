package maxhyper.dtaether.compat;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.Comparator;

public class ReduxBlockStateProperties implements addonBlockStateProperties {

    @Override
    public BlockState setPrismaticness(BlockState state, int petal, int value) {
        Property<?> property = findProperty(state, "petal_" + petal);
        return property == null ? state : setIndexedValue(state, property, value);
    }

    @Override
    public boolean hasPrismaticness(BlockState state) {
        return findProperty(state, "petal_1") != null
                && findProperty(state, "petal_2") != null
                && findProperty(state, "petal_3") != null
                && findProperty(state, "petal_4") != null;
    }

    @Override
    public IntegerProperty getLeafPileLayersProperty(BlockState state) {
        return state.getProperties().stream()
                .filter(IntegerProperty.class::isInstance)
                .map(IntegerProperty.class::cast)
                .filter(property -> property.getName().equals("layers") || property.getName().equals("leaf_layers"))
                .findFirst()
                .orElse(null);
    }

    private static Property<?> findProperty(BlockState state, String name) {
        return state.getProperties().stream()
                .filter(property -> property.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static BlockState setIndexedValue(BlockState state, Property property, int index) {
        Comparable value = (Comparable) property.getPossibleValues().stream()
                .sorted(Comparator.comparing(Object::toString))
                .skip(Math.max(0, index))
                .findFirst()
                .orElse(null);
        return value == null ? state : state.setValue(property, value);
    }

}
