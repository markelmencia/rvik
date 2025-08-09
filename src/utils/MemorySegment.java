package utils;

import java.util.BitSet;

public class MemorySegment {
    private final int value;

    public int getValue() {
        return value;
    }

    public MemorySegment(int address, BitSet bitset) {
        value = getSegmentLong(address, bitset);
    }

    public static int getSegmentLong(int address, BitSet bitSet) {
        int value = 0;
        int o = 0;
        for (int i = address; i < address + 32; i++) {
            value += bitSet.get(i) ? (1 << o) : 0;
            o++;
        }
        return value;
    }
}
