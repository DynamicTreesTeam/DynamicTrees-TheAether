package maxhyper.dtaether.cells;

import com.ferreusveritas.dynamictrees.cell.MatrixCell;

public class BlightwillowLeafCell extends MatrixCell {

    public BlightwillowLeafCell(int value) {
        super(value, valMap);
    }

    static final byte[] valMap = {
    //hydro 0  1  2  3  4  5  6  7
            0, 0, 2, 0, 0, 0, 0, 0, //D Maps * -> 0
            0, 1, 2, 3, 4, 0, 0, 0, //U Maps * -> *
            0, 1, 2, 0, 2, 0, 0, 0, //N Maps 3 -> 0, 4 -> 2, * -> *
            0, 1, 2, 0, 2, 0, 0, 0, //S Maps 3 -> 0, 4 -> 2, * -> *
            0, 1, 2, 0, 2, 0, 0, 0, //W Maps 3 -> 0, 4 -> 2, * -> *
            0, 1, 2, 0, 2, 0, 0, 0  //E Maps 3 -> 0, 4 -> 2, * -> *
    };

}
