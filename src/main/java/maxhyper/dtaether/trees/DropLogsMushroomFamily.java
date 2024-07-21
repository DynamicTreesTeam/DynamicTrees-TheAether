package maxhyper.dtaether.trees;

import com.ferreusveritas.dynamictrees.block.branch.BasicBranchBlock;
import com.ferreusveritas.dynamictrees.block.branch.BranchBlock;
import com.ferreusveritas.dynamictrees.data.provider.DTLootTableProvider;
import com.ferreusveritas.dynamictreesplus.block.mushroom.MushroomBranchBlock;
import com.ferreusveritas.dynamictreesplus.tree.HugeMushroomFamily;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;

public class DropLogsMushroomFamily extends HugeMushroomFamily {

    public DropLogsMushroomFamily(ResourceLocation name) {
        super(name);
    }

    @Override
    protected BranchBlock createBranchBlock(ResourceLocation name) {
        BasicBranchBlock branch = new MushroomBranchBlock(name, this.getProperties()){
            @Override
            public LootTable.Builder createBranchDrops() {
                return DTLootTableProvider.BlockLoot.createBranchDrops(this.getPrimitiveLog().get(), getFamily().getStick(1).getItem());
            }
        };
        if (this.isFireProof()) branch.setFireSpreadSpeed(0).setFlammability(0);
        return branch;
    }
}
