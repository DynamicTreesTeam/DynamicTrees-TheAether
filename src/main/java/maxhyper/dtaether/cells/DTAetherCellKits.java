package maxhyper.dtaether.cells;

import com.ferreusveritas.dynamictrees.api.cell.Cell;
import com.ferreusveritas.dynamictrees.api.cell.CellKit;
import com.ferreusveritas.dynamictrees.api.cell.CellNull;
import com.ferreusveritas.dynamictrees.api.cell.CellSolver;
import com.ferreusveritas.dynamictrees.api.registry.Registry;
import com.ferreusveritas.dynamictrees.cell.*;
import com.ferreusveritas.dynamictrees.util.SimpleVoxmap;
import maxhyper.dtaether.DynamicTreesAether;

public class DTAetherCellKits {

    public static void register(final Registry<CellKit> registry) {
        registry.registerAll(BLIGHTWILLOW);
    }
    public static final CellKit BLIGHTWILLOW = new CellKit(DynamicTreesAether.location("blightwillow")) {

        private final Cell blightwillowBranch = new NormalCell(3);
        private final Cell coniferTopBranch = new ConiferTopBranchCell();

        private final Cell[] coniferLeafCells = {
                CellNull.NULL_CELL,
                new BlightwillowLeafCell(1),
                new BlightwillowLeafCell(2),
                new BlightwillowLeafCell(3),
                new BlightwillowLeafCell(4),
                new BlightwillowLeafCell(5),
                new BlightwillowLeafCell(6),
                new BlightwillowLeafCell(7)
        };

        private final CellKits.BasicSolver blightwillowSolver = new CellKits.BasicSolver(new short[]{
                0x0514, 0x0413, 0x0312, 0x0221
        });

        @Override
        public Cell getCellForLeaves(int hydro) {
            return coniferLeafCells[hydro];
        }

        @Override
        public Cell getCellForBranch(int radius, int meta) {
            if (meta == MetadataCell.TOP_BRANCH) {
                return coniferTopBranch;
            } else if (radius == 1) {
                return blightwillowBranch;
            } else {
                return CellNull.NULL_CELL;
            }
        }

        @Override
        public SimpleVoxmap getLeafCluster() {
            return DTAetherLeafClusters.BLIGHTWILLOW;
        }

        @Override
        public CellSolver getCellSolver() {
            return blightwillowSolver;
        }

        @Override
        public int getDefaultHydration() {
            return 4;
        }

    };

}
