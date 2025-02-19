package maxhyper.dtaether.trees;

import com.ferreusveritas.dynamictrees.DynamicTrees;
import com.ferreusveritas.dynamictrees.api.network.MapSignal;
import com.ferreusveritas.dynamictrees.api.network.NodeInspector;
import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.block.branch.BasicBranchBlock;
import com.ferreusveritas.dynamictrees.block.branch.BranchBlock;
import com.ferreusveritas.dynamictrees.data.provider.DTLootTableProvider;
import com.ferreusveritas.dynamictrees.systems.nodemapper.DestroyerNode;
import com.ferreusveritas.dynamictrees.systems.nodemapper.NetVolumeNode;
import com.ferreusveritas.dynamictrees.systems.nodemapper.SpeciesNode;
import com.ferreusveritas.dynamictrees.systems.nodemapper.StateNode;
import com.ferreusveritas.dynamictrees.tree.family.Family;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import com.ferreusveritas.dynamictrees.util.*;
import com.ferreusveritas.dynamictreesplus.block.mushroom.CapProperties;
import com.ferreusveritas.dynamictreesplus.block.mushroom.DynamicCapBlock;
import com.ferreusveritas.dynamictreesplus.block.mushroom.DynamicCapCenterBlock;
import com.ferreusveritas.dynamictreesplus.block.mushroom.MushroomBranchBlock;
import com.ferreusveritas.dynamictreesplus.systems.mushroomlogic.context.MushroomCapContext;
import com.ferreusveritas.dynamictreesplus.tree.HugeMushroomFamily;
import com.ferreusveritas.dynamictreesplus.tree.HugeMushroomSpecies;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import net.zepalesque.redux.block.ReduxBlocks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class DropLogsMushroomFamily extends HugeMushroomFamily {

    public static final TypedRegistry.EntryType<Family> TYPE = TypedRegistry.newType(DropLogsMushroomFamily::new);

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
            //THIS IS FOR CLOUDCAPS. CRUDE SOLUTION BUT IM LAZY
            @Override
            public BranchDestructionData destroyBranchFromNode(Level level, BlockPos cutPos, Direction toolDir, boolean wholeTree, @Nullable final LivingEntity entity) {
                final BlockState blockState = level.getBlockState(cutPos);
                final SpeciesNode speciesNode = new SpeciesNode();
                final MapSignal signal = analyse(blockState, level, cutPos, null, new MapSignal(speciesNode)); // Analyze entire tree network to find root node and species.
                final Species species = speciesNode.getSpecies(); // Get the species from the root node.

                // Analyze only part of the tree beyond the break point and map out the extended block states.
                // We can't destroy the branches during this step since we need accurate extended block states that include connections.
                StateNode stateMapper = new StateNode(cutPos);
                this.analyse(blockState, level, cutPos, wholeTree ? null : signal.localRootDir, new MapSignal(stateMapper));

                // Analyze only part of the tree beyond the break point and calculate it's volume, then destroy the branches.
                final NetVolumeNode volumeSum = new NetVolumeNode();
                final DestroyerNode destroyer = new DestroyerNode(species).setPlayer(entity instanceof Player ? (Player) entity : null);
                destroyMode = DynamicTrees.DestroyMode.HARVEST;
                this.analyse(blockState, level, cutPos, wholeTree ? null : signal.localRootDir, new MapSignal(volumeSum, destroyer));
                destroyMode = DynamicTrees.DestroyMode.SLOPPY;

                // Destroy all the leaves on the branch, store them in a map and convert endpoint coordinates from absolute to relative.
                List<BlockPos> endPoints = destroyer.getEnds();
                final Map<BlockPos, BlockState> destroyedLeaves = new HashMap<>();
                final List<ItemStackPos> leavesDropsList = new ArrayList<>();
                this.destroyMushroomCap(level, cutPos, species, entity == null ? ItemStack.EMPTY : entity.getMainHandItem(), endPoints, destroyedLeaves, leavesDropsList, entity);
                endPoints = endPoints.stream().map(p -> p.subtract(cutPos)).collect(Collectors.toList());

                // Calculate main trunk height.
                int trunkHeight = 1;
                for (BlockPos iter = new BlockPos(0, 1, 0); stateMapper.getBranchConnectionMap().containsKey(iter); iter = iter.above()) {
                    trunkHeight++;
                }

                Direction cutDir = signal.localRootDir;
                if (cutDir == null) {
                    cutDir = Direction.DOWN;
                }

                return new BranchDestructionData(species, stateMapper.getBranchConnectionMap(), destroyedLeaves, leavesDropsList, endPoints, volumeSum.getVolume(), cutPos, cutDir, toolDir, trunkHeight);
            }

            public void destroyMushroomCap(final @NotNull Level level, final @NotNull BlockPos cutPos, final @NotNull Species species, final @NotNull ItemStack tool, final @NotNull List<BlockPos> endPoints, final @NotNull Map<BlockPos, BlockState> destroyedCapBlocks, final @NotNull List<ItemStackPos> drops, Entity entity) {
                if (!(species instanceof final HugeMushroomSpecies mushSpecies)) return;
                if (!(species.getFamily() instanceof final HugeMushroomFamily family)) return;

                if (level.isClientSide || endPoints.isEmpty()) {
                    return;
                }

                // Make a bounding volume that holds all the endpoints and expand the volume for the leaves' radius.
                final BlockBounds bounds = getFamily().expandLeavesBlockBounds(new BlockBounds(endPoints));

                // Create a voxmap to store the leaf destruction map.
                final SimpleVoxmap capMap = new SimpleVoxmap(bounds);

                // For each of the endpoints add an expanded destruction volume around it.
                for (final BlockPos endPos : endPoints) {
                    int age = DynamicCapCenterBlock.getCapAge(level, endPos.above());
                    if (age >= 0){
                        for (final BlockPos findPos : mushSpecies.getMushroomShapeKit().getShapeCluster(new MushroomCapContext(level, endPos.above(), mushSpecies, age))) {
                            final BlockState findState = level.getBlockState(findPos);
                            if (family.isCompatibleCap(mushSpecies, findState, level, findPos)) { // Search for endpoints of the same tree family.
                                capMap.setVoxel(findPos.getX(), findPos.getY(), findPos.getZ(), (byte) 1); // Flag this position for destruction.
                            }
                        }
                        capMap.setVoxel(endPos, (byte) 0); // We know that the endpoint does not have a leaves block in it because it was a branch.
                    }
                    //DESTROY THE SPORES
                    for (CoordUtils.Surround surr : CoordUtils.Surround.values()){
                        BlockPos pos = endPos.offset(surr.getOffset());
                        BlockState state = level.getBlockState(pos);
                        if (level.getBlockState(pos).is(ReduxBlocks.CLOUDCAP_SPORES.get())){
                            if (entity instanceof Player){
                                BlockEntity te = level.getBlockEntity(pos);
                                state.getBlock().onDestroyedByPlayer(state, level, pos, (Player)entity, true, level.getFluidState(pos));
                                state.getBlock().playerDestroy(level, (Player)entity, pos, state, te, tool);
                            } else {
                                level.setBlock(pos, BlockStates.AIR, 0);
                            }
                        }
                    }
                }

                final List<ItemStack> dropList = new ArrayList<>();

                // Destroy all family compatible leaves.
                for (final SimpleVoxmap.Cell cell : capMap.getAllNonZeroCells()) {
                    final BlockPos.MutableBlockPos pos = cell.getPos();
                    final BlockState state = level.getBlockState(pos);
                    if (family.isCompatibleCap(mushSpecies, state, level, pos)) {
                        dropList.clear();
                        CapProperties cap = getCapProperties(state);
                        dropList.addAll(cap.getDrops(level, pos, tool, species));
                        final BlockPos imPos = pos.immutable(); // We are storing this so it must be immutable
                        final BlockPos relPos = imPos.subtract(cutPos);
                        level.setBlock(imPos, BlockStates.AIR, 3);
                        destroyedCapBlocks.put(relPos, state);
                        dropList.forEach(i -> drops.add(new ItemStackPos(i, relPos)));
                    }
                }
            }
            private CapProperties getCapProperties(BlockState state) {
                if (state.getBlock() instanceof DynamicCapBlock) {
                    return (CapProperties) Optional.of((DynamicCapBlock)state.getBlock()).map((block) -> {
                        return block.getProperties(state);
                    }).orElse(CapProperties.NULL);
                } else {
                    return state.getBlock() instanceof DynamicCapCenterBlock ? (CapProperties)Optional.of((DynamicCapCenterBlock)state.getBlock()).map((block) -> {
                        return block.getProperties(state);
                    }).orElse(CapProperties.NULL) : CapProperties.NULL;
                }
            }

        };
        if (this.isFireProof()) branch.setFireSpreadSpeed(0).setFlammability(0);
        return branch;
    }
}
