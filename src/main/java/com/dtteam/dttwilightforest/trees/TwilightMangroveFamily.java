package com.dtteam.dttwilightforest.trees;

import com.dtteam.dynamictrees.api.registry.TypedRegistry;
import com.dtteam.dynamictrees.block.branch.BasicRootsBlock;
import com.dtteam.dynamictrees.block.branch.BranchBlock;
import com.dtteam.dynamictrees.tree.family.Family;
import com.dtteam.dynamictrees.tree.family.UndergroundRootsFamily;
import com.dtteam.dynamictrees.utility.Optionals;
import com.dtteam.dttwilightforest.blocks.TwilightMangroveRootsBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

public class TwilightMangroveFamily extends UndergroundRootsFamily {

    public static final TypedRegistry.EntryType<Family> TYPE = TypedRegistry.newType(TwilightMangroveFamily::new);

    public TwilightMangroveFamily(ResourceLocation name) {
        super(name);
    }

    @Override
    protected BranchBlock createRootsBlock(ResourceLocation name) {
        final BasicRootsBlock branch = new TwilightMangroveRootsBlock(name, this.getProperties());
        if (this.isFireProof()) branch.setFireSpreadSpeed(0).setFlammability(0);
        return branch;
    }

    private Block primitiveRootsGrassy;

    public void setPrimitiveRootsGrassy(Block primitiveRootsCovered) {
        this.primitiveRootsGrassy = primitiveRootsCovered;
    }

    public Optional<Block> getPrimitiveGrassyRoots() {
        return Optionals.ofBlock(primitiveRootsGrassy);
    }

    @Override
    public void setPrimitiveRoots(Block primitiveRoots) {
        //We set filled roots to the same, we will ignore filled roots anyway.
        super.setPrimitiveRoots(primitiveRoots);
        setPrimitiveRootsFilled(primitiveRoots);
    }

    private int grassSpreadRequiredLight = 9;

    public void setGrassSpreadRequiredLight(int grassSpreadRequiredLight) {
        this.grassSpreadRequiredLight = grassSpreadRequiredLight;
    }

    public int getGrassSpreadRequiredLight() {
        return grassSpreadRequiredLight;
    }
}
