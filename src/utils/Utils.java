package utils;

import java.util.BitSet;

public class Utils {

    /**
     * Converts a binary array to a base-10 integer (unsigned)
     */
    public static int btiu(BitSet segment) {

        // Binary to int unsigned.

        int result = 0;

        for (int i = 0; i < segment.length(); i++) {
            if (segment.get(i)) {
                result |= (1 << i);
            }
        }
        return result;
    }

    /**
     * Converts a binary array to a base-10 integer (unsigned)
     */
    public static int btis(BitSet segment, int size) {

        // Binary to int signed. Due to BitSize having
        // a dinamic length depending on the last bit set,
        // a size parameter is needed.

        int result = 0;

        for (int i = 0; i < size; i++) {
            if (segment.get(i)) {
                result |= (1 << i);
            }
        }
        if (segment.get(size - 1)) {
            result -= (1 << size);
        }
        return result;
    }

    /**
     * Extends a bit array to a desired bit ammount.
     * @param segment The instruction array that will be filtered.
     * @param resultBits The desired bit length.
     * @return The extended BitSet.
     */
    public static BitSet bitExtension(BitSet segment, int resultBits) {
        BitSet result = new BitSet(resultBits);

        for (int i = 0; i < segment.length(); i++) {
            result.set(i, segment.get(i));
        }
        return result;
    }

    /**
     * Gets an instruction array and a bit range and returns an array filtering
     * out the rest of the instruction. Used in fillInstr() for simplicity purposes.
     * @param instr The instruction array that will be filtered.
     * @param first the first bit of the range.
     * @param last The last bit of the range.
     * @return An array of the size of the range, with the bits that haven't been filtered out.
     */
    public static BitSet fillSegment(BitSet instr, int first, int last) {
        int length = last - first + 1;
        BitSet result = new BitSet(length);

        int o = 0;
        for (int i = first; i < last + 1; i++) {
            result.set(o, instr.get(i));
            o++;
        }
        return result;
    }
}
