//package com.dtteam.dttwilightforest.nodes;
//
//import com.dtteam.dynamictrees.tree.TreeHelper;
//import com.dtteam.dynamictrees.block.branch.BranchBlock;
//import com.dtteam.dynamictrees.systems.BranchConnectables;
//import com.dtteam.dynamictrees.systems.nodemapper.FindEndsNode;
//import com.dtteam.dynamictrees.utility.BlockStates;
//import com.dtteam.dttwilightforest.blocks.BasicRootsBlock;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.LevelAccessor;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.block.state.properties.BlockStateProperties;
//
//import javax.annotation.Nullable;
//import java.util.LinkedList;
//
///**
// * Destroys all branches on a tree and the surrounding leaves.
// *
// * @author ferreusveritas
// */
//public class SolidRootsNode extends FindEndsNode {
//
//    private final LinkedList<BlockPos> solidRoots = new LinkedList<>();
//
//    public SolidRootsNode() {
//        super();
//    }
//
//    public LinkedList<BlockPos> getSolidRoots() {
//        return new LinkedList<>(solidRoots);
//    }
//
//    @Override
//    public boolean run(BlockState state, LevelAccessor level, BlockPos pos, @Nullable Direction fromDir) {
//        if (state.getBlock() instanceof BasicRootsBlock rootsBlock){
//            if (rootsBlock.isSolid(state)) solidRoots.add(pos);
//        }
//        return super.run(state, level, pos, fromDir);
//    }
//
//    @Override
//    public boolean returnRun(BlockState state, LevelAccessor level, BlockPos pos, Direction fromDir) {
//        return super.returnRun(state, level, pos, fromDir);
//    }
//}
