package maxhyper.dtaether.cells;

import com.ferreusveritas.dynamictrees.util.SimpleVoxmap;
import net.minecraft.core.BlockPos;

public class DTAetherLeafClusters {

    public static final SimpleVoxmap BLIGHTWILLOW = new SimpleVoxmap(5, 3, 5, new byte[]{

            //Layer 0(Bottom)
            0, 0, 0, 0, 0,
            0, 0, 1, 0, 0,
            0, 1, 2, 1, 0,
            0, 0, 1, 0, 0,
            0, 0, 0, 0, 0,

            //Layer 1(Middle)
            0, 0, 0, 0, 0,
            0, 1, 2, 1, 0,
            0, 2, 0, 2, 0,
            0, 1, 2, 1, 0,
            0, 0, 0, 0, 0,

            //Layer 1 (Top)
            0, 0, 0, 0, 0,
            0, 0, 1, 0, 0,
            0, 1, 2, 1, 0,
            0, 0, 1, 0, 0,
            0, 0, 0, 0, 0

    }).setCenter(new BlockPos(2, 1, 2));

}
