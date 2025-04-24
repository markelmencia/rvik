package utils;

import rv32i.Compiler;

import java.util.BitSet;

public class MemorySegment {
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public MemorySegment(int address) {
        value = getSegmentLong(address);
    }

    public static int getSegmentLong(int address) {
        int value = 0;
        int o = 0;
        for (int i = address; i < address + 32; i++) {
            value += Compiler.mem.get(i) ? (1 << o) : 0;
            o++;
        }
        return value;
    }
}
