package maxhyper.dtaether.world;

import com.aetherteam.aether.world.structure.GoldDungeonStructure;
import com.aetherteam.aether.world.structurepiece.golddungeon.GoldIsland;
import com.aetherteam.aether.world.structurepiece.golddungeon.GoldStub;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import maxhyper.dtaether.DynamicTreesAether;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;

//to-do
public class DynamicGoldDungeonStructure extends GoldDungeonStructure {

    public DynamicGoldDungeonStructure(StructureSettings settings, int stubIslandCount, int belowTerrain, int minY, int rangeY) {
        super(settings, stubIslandCount, belowTerrain, minY, rangeY);
    }

    @Override
    public void afterPlace(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBox, ChunkPos chunkPos, PiecesContainer pieces) {
        for (StructurePiece piece : pieces.pieces()) {
            if (piece instanceof GoldIsland island) {
                placeDynamicGoldenOaks(level, generator, random, island.getBoundingBox(), chunkBox, 48, 2, 1);
            } else if (piece instanceof GoldStub stub) {
                placeDynamicGoldenOaks(level, generator, random, stub.getBoundingBox(), chunkBox, 64, 1, 0);
            }
        }

    }

    private Species goldenOakSpecies = Species.NULL_SPECIES;
    private Species getGoldenOakSpecies (){
        if (!goldenOakSpecies.isValid())
            goldenOakSpecies = Species.REGISTRY.get(DynamicTreesAether.location("golden_oak"));
        return goldenOakSpecies;
    }

    private void placeDynamicGoldenOaks(WorldGenLevel level, ChunkGenerator generator, RandomSource random, BoundingBox boundingBox, BoundingBox chunkBox, int randomBounds, int treeWeight, int flowerWeight) {

        //to-do
    }

}
