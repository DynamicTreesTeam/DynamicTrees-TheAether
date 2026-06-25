package maxhyper.dtaether.trees;

import com.dtteam.dynamictrees.api.registry.TypedRegistry;
import com.dtteam.dynamictrees.systems.nodemapper.NetVolumeNode;
import com.dtteam.dynamictrees.tree.family.Family;
import com.dtteam.dynamictrees.tree.species.Species;
import com.dtteam.dynamictreesplus.block.mushroom.CapProperties;
import com.dtteam.dynamictreesplus.tree.HugeMushroomSpecies;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.LinkedList;
import java.util.List;

public class DropLogsMushroomSpecies extends HugeMushroomSpecies {

    public static final TypedRegistry.EntryType<Species> TYPE = createDefaultMushroomType(DropLogsMushroomSpecies::new);

    public DropLogsMushroomSpecies(ResourceLocation name, Family family, CapProperties capProperties) {
        super(name, family, capProperties);
    }

    @Override
    public LogsAndSticks getLogsAndSticks(NetVolumeNode.Volume volume, boolean silkTouch, int fortuneLevel) {
        List<ItemStack> logsList = new LinkedList<>();
        int[] volArray = volume.getRawVolumesArray();
        float stickVol = 0.0F;

        int i;
        for(i = 0; i < volArray.length; ++i) {
            float vol = (float)volArray[i] / 4096.0F;
            if (vol > 0.0F) {
                stickVol += this.getFamily().getValidBranchBlock(i).getPrimitiveLogs(vol, logsList);
            }
        }

        i = (int)(stickVol * 8.0F);
        return new LogsAndSticks(logsList, i);
    }
}
