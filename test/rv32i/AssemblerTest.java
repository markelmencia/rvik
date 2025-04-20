package rv32i;

import org.junit.Test;
import java.util.BitSet;

import static org.junit.Assert.assertEquals;

public class AssemblerTest {

    @Test
    public void testFetchInstruction() {
        String[] luiSplit = {"lui", "2", "3"};
        assertEquals(BitSet.valueOf(new long[]{0b00000000000000000011000100110111}), Assembler.splitToBitSet.get(luiSplit[0]).apply(luiSplit));
        String[] auipcSplit = {"auipc", "2", "3"};
        assertEquals(BitSet.valueOf(new long[]{0b00000000000000000011000100010111}), Assembler.splitToBitSet.get(auipcSplit[0]).apply(auipcSplit));
        String[] jalSplit = {"jal", "2", "4"};
        assertEquals(BitSet.valueOf(new long[]{0b00000000010000000000000101101111}), Assembler.splitToBitSet.get(jalSplit[0]).apply(jalSplit));
        String[] jalrSplit = {"jalr", "2", "3", "4"};
        assertEquals(BitSet.valueOf(new long[]{0b00000000001100100000000101100111}), Assembler.splitToBitSet.get(jalrSplit[0]).apply(jalrSplit));
        String[] beqSplit = {"beq", "1", "2", "4"};
        assertEquals(BitSet.valueOf(new long[]{0b00000000001000001000001001100011}), Assembler.splitToBitSet.get(beqSplit[0]).apply(beqSplit));
        String[] bneSplit = {"bne", "1", "2", "4"};
        assertEquals(BitSet.valueOf(new long[]{0b00000000001000001001001001100011}), Assembler.splitToBitSet.get(bneSplit[0]).apply(bneSplit));
        String[] bltSplit = {"blt", "1", "2", "4"};
        assertEquals(BitSet.valueOf(new long[]{0b00000000001000001100001001100011}), Assembler.splitToBitSet.get(bltSplit[0]).apply(bltSplit));
        String[] bltuSplit = {"bltu", "1", "2", "4"};
        assertEquals(BitSet.valueOf(new long[]{0b00000000001000001110001001100011}), Assembler.splitToBitSet.get(bltuSplit[0]).apply(bltuSplit));
        String[] bgeuSplit = {"bgeu", "1", "2", "4"};
        assertEquals(BitSet.valueOf(new long[]{0b00000000001000001111001001100011}), Assembler.splitToBitSet.get(bgeuSplit[0]).apply(bgeuSplit));
        String[] lbSplit = {"lb", "1", "2", "4"};
        assertEquals(BitSet.valueOf(new long[]{0b00000000001000100000000010000011}), Assembler.splitToBitSet.get(lbSplit[0]).apply(lbSplit));
        String[] lhSplit = {"lh", "1", "2", "4"};
        assertEquals(BitSet.valueOf(new long[]{0b00000000001000100001000010000011}), Assembler.splitToBitSet.get(lhSplit[0]).apply(lhSplit));
        String[] lwSplit = {"lw", "1", "2", "4"};
        assertEquals(BitSet.valueOf(new long[]{0b00000000001000100010000010000011}), Assembler.splitToBitSet.get(lwSplit[0]).apply(lwSplit));
        String[] lbuSplit = {"lbu", "1", "2", "4"};
        assertEquals(BitSet.valueOf(new long[]{0b00000000001000100100000010000011}), Assembler.splitToBitSet.get(lbuSplit[0]).apply(lbuSplit));
        String[] lhuSplit = {"lhu", "1", "2", "4"};
        assertEquals(BitSet.valueOf(new long[]{0b00000000001000100101000010000011}), Assembler.splitToBitSet.get(lhuSplit[0]).apply(lhuSplit));
        String[] sbSplit = {"sb", "1", "2", "4"};
        assertEquals(BitSet.valueOf(new long[]{0b00000000000100100000000100100011}), Assembler.splitToBitSet.get(sbSplit[0]).apply(sbSplit));
        String[] shSplit = {"sh", "1", "2", "4"};
        assertEquals(BitSet.valueOf(new long[]{0b00000000000100100001000100100011}), Assembler.splitToBitSet.get(shSplit[0]).apply(shSplit));
        String[] swSplit = {"sw", "1", "2", "4"};
        assertEquals(BitSet.valueOf(new long[]{0b00000000000100100010000100100011}), Assembler.splitToBitSet.get(swSplit[0]).apply(swSplit));
        String[] addiSplit = {"addi", "1", "2", "4"};
        assertEquals(BitSet.valueOf(new long[]{0b00000000010000010000000010010011}), Assembler.splitToBitSet.get(addiSplit[0]).apply(addiSplit));
        String[] sltiSplit = {"slti", "1", "2", "4"};
        assertEquals(BitSet.valueOf(new long[]{0b00000000010000010010000010010011}), Assembler.splitToBitSet.get(sltiSplit[0]).apply(sltiSplit));
        String[] sltiuSplit = {"sltiu", "1", "2", "4"};
        assertEquals(BitSet.valueOf(new long[]{0b00000000010000010011000010010011}), Assembler.splitToBitSet.get(sltiuSplit[0]).apply(sltiuSplit));
        String[] xoriSplit = {"xori", "1", "2", "4"};
        assertEquals(BitSet.valueOf(new long[]{0b00000000010000010100000010010011}), Assembler.splitToBitSet.get(xoriSplit[0]).apply(xoriSplit));
        String[] oriSplit = {"ori", "1", "2", "4"};
        assertEquals(BitSet.valueOf(new long[]{0b00000000010000010110000010010011}), Assembler.splitToBitSet.get(oriSplit[0]).apply(oriSplit));
        String[] andiSplit = {"andi", "1", "2", "4"};
        assertEquals(BitSet.valueOf(new long[]{0b00000000010000010111000010010011}), Assembler.splitToBitSet.get(andiSplit[0]).apply(andiSplit));
        String[] slliSplit = {"slli", "1", "2", "4"};
        assertEquals(BitSet.valueOf(new long[]{0b00000000010000010001000010010011}), Assembler.splitToBitSet.get(slliSplit[0]).apply(slliSplit));
        String[] srliSplit = {"srli", "1", "2", "4"};
        assertEquals(BitSet.valueOf(new long[]{0b00000000010000010101000010010011}), Assembler.splitToBitSet.get(srliSplit[0]).apply(srliSplit));
        String[] sraiSplit = {"srai", "1", "2", "4"};
        assertEquals(BitSet.valueOf(new long[]{0b01000000010000010101000010010011}), Assembler.splitToBitSet.get(sraiSplit[0]).apply(sraiSplit));
        String[] addSplit = {"add", "1", "1", "2"};
        assertEquals(BitSet.valueOf(new long[]{0b00000000001000001000000010110011}), Assembler.splitToBitSet.get(addSplit[0]).apply(addSplit));
        String[] subSplit = {"sub", "1", "1", "2"};
        assertEquals(BitSet.valueOf(new long[]{0b01000000001000001000000010110011}), Assembler.splitToBitSet.get(subSplit[0]).apply(subSplit));
        String[] sllSplit = {"sll", "1", "1", "2"};
        assertEquals(BitSet.valueOf(new long[]{0b00000000001000001001000010110011}), Assembler.splitToBitSet.get(sllSplit[0]).apply(sllSplit));
        String[] sltSplit = {"slt", "1", "1", "2"};
        assertEquals(BitSet.valueOf(new long[]{0b00000000001000001010000010110011}), Assembler.splitToBitSet.get(sltSplit[0]).apply(sltSplit));
        String[] sltuSplit = {"sltu", "1", "1", "2"};
        assertEquals(BitSet.valueOf(new long[]{0b00000000001000001011000010110011}), Assembler.splitToBitSet.get(sltuSplit[0]).apply(sltuSplit));
        String[] xorSplit = {"xor", "1", "1", "2"};
        assertEquals(BitSet.valueOf(new long[]{0b00000000001000001100000010110011}), Assembler.splitToBitSet.get(xorSplit[0]).apply(xorSplit));
        String[] srlSplit = {"srl", "1", "1", "2"};
        assertEquals(BitSet.valueOf(new long[]{0b00000000001000001101000010110011}), Assembler.splitToBitSet.get(srlSplit[0]).apply(srlSplit));
        String[] sraSplit = {"sra", "1", "1", "2"};
        assertEquals(BitSet.valueOf(new long[]{0b01000000001000001101000010110011}), Assembler.splitToBitSet.get(sraSplit[0]).apply(sraSplit));
        String[] orSplit = {"or", "1", "1", "2"};
        assertEquals(BitSet.valueOf(new long[]{0b00000000001000001110000010110011}), Assembler.splitToBitSet.get(orSplit[0]).apply(orSplit));
        String[] andSplit = {"and", "1", "1", "2"};
        assertEquals(BitSet.valueOf(new long[]{0b00000000001000001111000010110011}), Assembler.splitToBitSet.get(andSplit[0]).apply(andSplit));
    }
}
