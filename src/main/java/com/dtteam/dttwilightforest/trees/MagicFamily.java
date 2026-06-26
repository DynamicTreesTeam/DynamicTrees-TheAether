package com.dtteam.dttwilightforest.trees;

import com.dtteam.dynamictrees.api.registry.RegistryHandler;
import com.dtteam.dynamictrees.api.registry.TypedRegistry;
import com.dtteam.dynamictrees.block.branch.BasicBranchBlock;
import com.dtteam.dynamictrees.block.branch.BranchBlock;
import com.dtteam.dynamictrees.block.branch.ThickBranchBlock;
import com.dtteam.dynamictrees.tree.family.Family;
import com.dtteam.dynamictrees.utility.Optionals;
import com.dtteam.dynamictrees.utility.ResourceLocationUtils;
import com.dtteam.dttwilightforest.blocks.MagicCoreBranchBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Optional;
import java.util.function.Supplier;

import static com.dtteam.dynamictrees.utility.ResourceLocationUtils.suffix;

public class MagicFamily extends Family {

    public static final TypedRegistry.EntryType<Family> TYPE = TypedRegistry.newType(MagicFamily::new);

    private Supplier<BranchBlock> coreBranch;
    private Block primitiveCoreLog = Blocks.AIR;

    public MagicFamily(ResourceLocation name) {
        super(name);
    }

    @Override
    public void setupBlocks() {
        super.setupBlocks();
        this.setCoreBranch(this.createCoreBranch(ResourceLocationUtils.suffix(this.getRegistryName(), "_core")));
    }

    protected Family setCoreBranch(final Supplier<BranchBlock> branch) {
        this.coreBranch = this.setupBranch(branch, false);
        return this;
    }

    protected Supplier<BranchBlock> createCoreBranch(final ResourceLocation name) {
        return RegistryHandler.addBlock(suffix(name, getBranchNameSuffix()), () -> createCoreBranchBlock(name));
    }

    protected BranchBlock createCoreBranchBlock(ResourceLocation name) {
        final BasicBranchBlock branch = new MagicCoreBranchBlock(name, this.getProperties(),
                MagicCoreBranchBlock.coreType.valueOf(this.getRegistryName().getPath().toUpperCase())
        );
        if (this.isFireProof()) {
            branch.setFireSpreadSpeed(0).setFlammability(0);
        }
        return branch;
    }

    public Family setPrimitiveCoreLog(Block primitiveCoreLog) {
        this.primitiveCoreLog = primitiveCoreLog;

        if (this.coreBranch != null) {
            this.coreBranch.get().setPrimitiveLogDrops(new ItemStack(primitiveCoreLog));
        }

        return this;
    }

    public Optional<Block> getPrimitiveCoreLog() {
        return Optionals.ofBlock(primitiveCoreLog);
    }

    public Optional<BranchBlock> getCoreBranch() {
        return Optionals.ofBlock(this.coreBranch);
    }

}
