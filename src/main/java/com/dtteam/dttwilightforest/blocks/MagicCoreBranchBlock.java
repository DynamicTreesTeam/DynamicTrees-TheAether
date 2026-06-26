package com.dtteam.dttwilightforest.blocks;

import com.dtteam.dynamictrees.block.branch.ThickBranchBlock;
import com.dtteam.dttwilightforest.trees.MagicFamily;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.chunk.PalettedContainerRO;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.network.PacketDistributor;
import twilightforest.config.TFConfig;
import twilightforest.data.tags.EntityTagGenerator;
import twilightforest.init.TFBiomes;
import twilightforest.init.TFParticleType;
import twilightforest.init.TFSounds;
import twilightforest.item.OreMagnetItem;
import twilightforest.network.ParticlePacket;
import twilightforest.util.WorldUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MagicCoreBranchBlock extends ThickBranchBlock {

    protected static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public enum coreType {
        TIME,
        TRANSFORMATION,
        SORTING,
        MINING
    }
    private final coreType type;

    public static int tickRate = 20;

    public MagicCoreBranchBlock(ResourceLocation name, Properties properties, coreType type) {
        super(name, properties);
        this.type = type;
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(ACTIVE));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        InteractionResult result = super.useWithoutItem(state, world, pos, player, hit);

        if (result != InteractionResult.SUCCESS){
            if (!state.getValue(ACTIVE)) {
                world.setBlockAndUpdate(pos, state.setValue(ACTIVE, true));
                world.scheduleTick(pos, this, tickRate);
                return InteractionResult.SUCCESS;
            } else if (state.getValue(ACTIVE)) {
                world.setBlockAndUpdate(pos, state.setValue(ACTIVE, false));
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.PASS;
            }
        }
        return result;
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource rand) {
        if (!world.isClientSide && state.getValue(ACTIVE)) {
            this.playSound(world, pos, rand);
            this.performTreeEffect(world, pos, rand);
            world.scheduleTick(pos, this, tickRate);
        }
    }

    protected void performTreeEffect(Level world, BlockPos pos, RandomSource rand){
        switch (type){
            case TIME -> performTimeEffect(world, pos, rand);
            case MINING -> performMineEffect(world, pos, rand);
            case SORTING -> performSortEffect(world, pos, rand);
            case TRANSFORMATION -> performTransEffect(world, pos, rand);
        }
    };

    protected void playSound(Level level, BlockPos pos, RandomSource rand) {
        switch (type){
            case TIME -> level.playSound(null, pos, TFSounds.TIME_CORE.get(), SoundSource.BLOCKS, 0.1F, 0.5F);
            case TRANSFORMATION -> level.playSound(null, pos, TFSounds.TRANSFORMATION_CORE.get(), SoundSource.BLOCKS, 0.1F, rand.nextFloat() * 2F);
        }
    }

    protected void performMineEffect(Level level, BlockPos pos, RandomSource rand){
        BlockPos dPos = WorldUtil.randomOffset(rand, pos, 32);
        int moved = OreMagnetItem.doMagnet(level, pos, dPos, false);
        if (moved > 0) {
            level.playSound(null, pos, TFSounds.MAGNET_GRAB.get(), SoundSource.BLOCKS, 0.1F, 1.0F);
        }
    }
    protected void performSortEffect(Level level, BlockPos pos, RandomSource rand){
        Map<IItemHandler, Vec3> inputHandlers = new HashMap<>();
        Map<IItemHandler, Vec3> outputHandlers = new HashMap<>();

        for (BlockPos blockPos : WorldUtil.getAllAround(pos, 16)) {
            if (!blockPos.equals(pos)) {
                BlockEntity blockEntity = level.getBlockEntity(blockPos);

                if (blockEntity != null) {
                    IItemHandler iItemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, blockPos, null);
                    if (iItemHandler != null) {
                        if (Math.abs(blockPos.getX() - pos.getX()) <= 2 && Math.abs(blockPos.getY() - pos.getY()) <= 2 && Math.abs(blockPos.getZ() - pos.getZ()) <= 2) {
                            inputHandlers.put(iItemHandler, Vec3.upFromBottomCenterOf(blockPos, 1.9D));
                        } else outputHandlers.put(iItemHandler, Vec3.upFromBottomCenterOf(blockPos, 1.9D));
                    }
                }
            }
        }

        level.getEntities((Entity)null, (new AABB(pos)).inflate(2.0), (entity) -> entity.isAlive() && entity.getType().is(EntityTagGenerator.SORTABLE_ENTITIES)).forEach((entity) -> {
            IItemHandler iItemHandler = entity.getCapability(Capabilities.ItemHandler.ENTITY, null);
            if (iItemHandler != null) {
                inputHandlers.put(iItemHandler, entity.position().add(0.0, (double)entity.getBbHeight() + 0.9, 0.0));
            }
        });
        if (!inputHandlers.isEmpty()) {
            level.getEntities((Entity)null, (new AABB(pos)).inflate(16.0), (entity) -> entity.isAlive() && entity.getType().is(EntityTagGenerator.SORTABLE_ENTITIES)).forEach((entity) -> {
                IItemHandler iItemHandler = entity.getCapability(Capabilities.ItemHandler.ENTITY, null);
                if (iItemHandler != null) {
                    if (!inputHandlers.containsKey(iItemHandler)) {
                        outputHandlers.put(iItemHandler, entity.position().add(0.0, (double)entity.getBbHeight() + 0.9, 0.0));
                    }
                }
            });
            if (!outputHandlers.isEmpty()) {
                for (IItemHandler inputIItemHandler : inputHandlers.keySet()){
                    for(int i = 0; i < inputIItemHandler.getSlots(); ++i) {
                        ItemStack inputStack = inputIItemHandler.extractItem(i, 1, true);
                        if (!inputStack.isEmpty()) {
                            boolean transferred = false;
                            Map<Integer, IItemHandler> outputsByCount = new HashMap<>();

                            int firstProperStack;
                            ItemStack newStack;
                            for (IItemHandler outputIItemHandler : outputHandlers.keySet()) {
                                int count = 0;

                                for(firstProperStack = 0; firstProperStack < outputIItemHandler.getSlots(); ++firstProperStack) {
                                    newStack = outputIItemHandler.getStackInSlot(firstProperStack);
                                    if (newStack.is(inputStack.getItem())) {
                                        count += newStack.getCount();
                                    }
                                }

                                if (count > 0) {
                                    outputsByCount.put(count, outputIItemHandler);
                                }
                            }


                            for (IItemHandler outputIItemHandler : outputsByCount.values()){
                                firstProperStack = -1;

                                for(int j = 0; j < outputIItemHandler.getSlots(); ++j) {
                                    ItemStack outputStack = outputIItemHandler.getStackInSlot(j);
                                    if (firstProperStack == -1 && outputStack.isEmpty()) {
                                        firstProperStack = j;
                                    } else if (ItemStack.isSameItemSameComponents(inputStack, outputStack) && outputStack.getCount() < outputStack.getMaxStackSize() && outputStack.getCount() < outputIItemHandler.getSlotLimit(j)) {
                                        firstProperStack = j;
                                        break;
                                    }
                                }

                                if (firstProperStack != -1) {
                                    newStack = inputIItemHandler.extractItem(i, 1, false);
                                    if (!newStack.isEmpty() && outputIItemHandler.insertItem(firstProperStack, newStack, true).isEmpty()) {
                                        outputIItemHandler.insertItem(firstProperStack, newStack, false);
                                        transferred = true;
                                        Vec3 xyz = outputHandlers.get(outputIItemHandler);
                                        Vec3 diff = inputHandlers.get(inputIItemHandler).subtract(xyz);

                                        for (ServerPlayer serverplayer : ((ServerLevel)level).players()){
                                            if (serverplayer.distanceToSqr(xyz) < 4096.0) {
                                                ParticlePacket particlePacket = new ParticlePacket();
                                                double x = diff.x - 0.25 + rand.nextDouble() * 0.5;
                                                double y = diff.y - 1.75 + rand.nextDouble() * 0.5;
                                                double z = diff.z - 0.25 + rand.nextDouble() * 0.5;
                                                particlePacket.queueParticle(TFParticleType.SORTING_PARTICLE.get(), false, xyz, (new Vec3(x, y, z)).scale(1.0 / diff.length()));
                                                PacketDistributor.sendToPlayer(serverplayer, particlePacket);
                                            }
                                        }
                                    }
                                }
                            }
                            if (transferred) {
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
    protected void performTimeEffect(Level world, BlockPos pos, RandomSource rand){
        int numticks = 8 * 3 * tickRate;

        for (int i = 0; i < numticks; i++) {

            BlockPos dPos = WorldUtil.randomOffset(rand, pos, 16);

            BlockState state = world.getBlockState(dPos);

            if (state.isRandomlyTicking()) {
                state.randomTick((ServerLevel) world, dPos, rand);
            }

            BlockEntity entity = world.getBlockEntity(dPos);
            if (entity != null) {
                BlockEntityTicker<BlockEntity> ticker = state.getTicker(world, (BlockEntityType<BlockEntity>) entity.getType());
                if (ticker != null)
                    ticker.tick(world, dPos, state, entity);
            }
        }
    }
    protected void performTransEffect(Level level, BlockPos pos, RandomSource rand){
        ResourceKey<Biome> target = TFBiomes.ENCHANTED_FOREST;
        Holder<Biome> biome = level.registryAccess().registryOrThrow(Registries.BIOME).getHolderOrThrow(target);
        int range = TFConfig.transformationCoreRange;

        for(int i = 0; i < 16; ++i) {
            BlockPos dPos = WorldUtil.randomOffset(rand, pos, range, 0, range);
            if (!(dPos.distSqr(pos) > 256.0) && !level.getBiome(dPos).is(target)) {
                int minY = QuartPos.fromBlock(level.getMinBuildHeight());
                int maxY = minY + QuartPos.fromBlock(level.getHeight()) - 1;
                int x = QuartPos.fromBlock(dPos.getX());
                int z = QuartPos.fromBlock(dPos.getZ());
                LevelChunk chunkAt = level.getChunk(dPos.getX() >> 4, dPos.getZ() >> 4);
                LevelChunkSection[] var14 = chunkAt.getSections();
                int var15 = var14.length;

                for(int var16 = 0; var16 < var15; ++var16) {
                    LevelChunkSection section = var14[var16];

                    for(int sy = 0; sy < 16; sy += 4) {
                        int y = Mth.clamp(QuartPos.fromBlock(chunkAt.getMinSection() + sy), minY, maxY);
                        if (!section.getBiomes().get(x & 3, y & 3, z & 3).is(target)) {
                            PalettedContainerRO<Holder<Biome>> var21 = section.getBiomes();
                            if (var21 instanceof PalettedContainer) {
                                PalettedContainer<Holder<Biome>> container = (PalettedContainer)var21;
                                container.set(x & 3, y & 3, z & 3, biome);
                            }
                        }
                    }
                }

                if (level instanceof ServerLevel) {
                    ServerLevel server = (ServerLevel)level;
                    if (!chunkAt.isUnsaved()) {
                        chunkAt.setUnsaved(true);
                    }

                    server.getChunkSource().chunkMap.resendBiomesForChunks(List.of(chunkAt));
                }
                break;
            }
        }

    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return 15;
    }

    //The drops must be capped to just one core. The rest are turned to the default branch.
    public float getPrimitiveLogs(float volumeIn, List<ItemStack> drops) {
        float ret = super.getPrimitiveLogs(volumeIn, drops);

        Block primitiveLog;
        if (getFamily().getPrimitiveLog().isPresent())
            primitiveLog = getFamily().getPrimitiveLog().get();
        else return ret;
        Block primitiveCoreLog;
        if (getFamily() instanceof MagicFamily magicFamily && magicFamily.getPrimitiveCoreLog().isPresent())
            primitiveCoreLog = magicFamily.getPrimitiveCoreLog().get();
        else return ret;

        int addLogs = 0;
        for (ItemStack stack : drops){
            if (stack.is(new ItemStack(primitiveCoreLog).getItem())){
                int logCount = stack.getCount();
                if (logCount > 1){
                    stack.setCount(0);
                    addLogs = logCount-1;
                }
                break;
            }
        }
        if (addLogs > 0){
            drops.add(new ItemStack(primitiveLog, addLogs));
        }
        //52 6 8 -> 57 1 8
        return ret;
    }

}
