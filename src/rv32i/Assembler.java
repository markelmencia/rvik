package rv32i;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.BitSet;

/*	TESTED INSTRUCTIONS
	
	lui: WORKING
	auipc: WORKING 
	jal: WORKING
	jalr: WORKING
	
	add: WORKING
	sub: WORKING
	sll: WORKING
	slt: WORKING
	sltu: WORKING
	xor: WORKING
	srl: WORKING
	sra: WORKING
	or: WORKING
	and: WORKING
	
	addi: WORKING
	slti: WORKING
	sltiu: WORKING
	xori: WORKING
	ori: WORKING
	andi: WORKING
	slli: WORKING
	srli: WORKING
	srai: WORKING
	
	sb: WORKING
	sh: WORKING
	sw: WORKING
	
	lb: WORKING
	lh: WORKING
	lw: WORKING
	lbu: WORKING
	lhu: WORKING
	
	beq: WORKING
	bne: WORKING
	blt: WORKING
	bge: WORKING
	bltu: WORKING
	bgeu: WORKING
	
	csrrw: TODO
	csrrs: TODO
	csrrc: TODO
	csrrwi: TODO
	csrrsi: TODO
	csrrci: TODO
	
*/

public class Assembler {

	
	public static BitSet itba(int num, int length) {
		
		// Integer to Binary Array
		
		BitSet result = new BitSet(length);
		String numString = Integer.toBinaryString(num);
		int stringLength = numString.length();
		for (int i = 0; i < stringLength; i++) {
			result.set(i, numString.charAt(stringLength - 1 - i) == '1');
		}
		return result;
	}
	
	
	public static BitSet fetchInstruction(String[] instructionSplit) {

		BitSet result = new BitSet(32); 
		int j;
		
		if (instructionSplit[0].equals("lui")) {
			// opcode
			result.set(0, true);
			result.set(1, true);
			result.set(2, true);
			result.set(3, false);
			result.set(4, true);
			result.set(5, true);
			result.set(6, false);
			
			// rd
			int rd = Integer.parseInt(instructionSplit[1]);
			BitSet rdArray = itba(rd, 5);
			j = 0;
			for (int i = 7; i < 12; i++) {
				result.set(i, rdArray.get(j));
				j++;
			}
			
			// imm20
			int imm20 = Integer.parseInt(instructionSplit[2]);
			BitSet imm20Array = itba(imm20, 20);
			j = 0;
			for (int i = 12; i < 32; i++) {
				result.set(i, imm20Array.get(j));
				j++;
			}
		}
		if (instructionSplit[0].equals("auipc")) {
			// opcode
			result.set(0, true);
			result.set(1, true);
			result.set(2, true);
			result.set(3, false);
			result.set(4, true);
			result.set(5, false);
			result.set(6, false);
			
			// rd
			int rd = Integer.parseInt(instructionSplit[1]);
			BitSet rdArray = itba(rd, 5);
			j = 0;
			for (int i = 7; i < 12; i++) {
				result.set(i, rdArray.get(j));
				j++;
			}
			
			// imm20
			int imm20 = Integer.parseInt(instructionSplit[2]);
			BitSet imm20Array = itba(imm20, 20);
			j = 0;
			for (int i = 12; i < 32; i++) {
				result.set(i, imm20Array.get(j));
				j++;
			}			
		}
		if (instructionSplit[0].equals("jal")) {
			// opcode
			result.set(0, true);
			result.set(1, true);
			result.set(2, true);
			result.set(3, true);
			result.set(4, false);
			result.set(5, true);
			result.set(6, true);
			
			// rd
			int rd = Integer.parseInt(instructionSplit[1]);
			BitSet rdArray = itba(rd, 5);
			j = 0;
			for (int i = 7; i < 12; i++) {
				result.set(i, rdArray.get(j));
				j++;
			}
			
			// imm21
			int imm21 = Integer.parseInt(instructionSplit[2]);
			BitSet imm20Array = itba(imm21, 20);
			
			result.set(12, imm20Array.get(12));
			result.set(13, imm20Array.get(13));		
			result.set(14, imm20Array.get(14));		
			result.set(15, imm20Array.get(15));		
			result.set(16, imm20Array.get(16));		
			result.set(17, imm20Array.get(17));		
			result.set(18, imm20Array.get(18));		
			result.set(19, imm20Array.get(19));		
			result.set(20, imm20Array.get(11));		
			result.set(21, imm20Array.get(1));		
			result.set(22, imm20Array.get(2));		
			result.set(23, imm20Array.get(3));		
			result.set(24, imm20Array.get(4));		
			result.set(25, imm20Array.get(5));		
			result.set(26, imm20Array.get(6));		
			result.set(27, imm20Array.get(7));		
			result.set(28, imm20Array.get(8));		
			result.set(29, imm20Array.get(9));		
			result.set(30, imm20Array.get(10));		
			result.set(31, imm20Array.get(20));		
		}
			
		if (instructionSplit[0].equals("jalr")) {
			
			// opcode
			result.set(0, true);
			result.set(1, true);
			result.set(2, true);
			result.set(3, false);
			result.set(4, false);
			result.set(5, true);
			result.set(6, true);
			
			// rd
			int rd = Integer.parseInt(instructionSplit[1]);
			BitSet rdArray = itba(rd, 5);
			
			j = 0;
			for (int i = 7; i < 12; i++) {
				result.set(i, rdArray.get(j));
				j++;
			}
			
			// funct3
			
			// (nothing needed)
			
			// rs1
			int rs1 = Integer.parseInt(instructionSplit[3]);
			BitSet rs1Array = itba(rs1, 5);
			
			j = 0;
			for (int i = 15; i < 20; i++) {
				result.set(i, rs1Array.get(j));
				j++;
			}
			
			// imm12
			int imm12 = Integer.parseInt(instructionSplit[2]);
			BitSet imm12Array = itba(imm12, 12);
			
			j = 0;
			for (int i = 20; i < 32; i++) {
				result.set(i, imm12Array.get(j));
				j++;
			}
		}
		if (instructionSplit[0].equals("beq")) {
			
			// codeop
			result.set(0, true);
			result.set(1, true);
			result.set(2, false);
			result.set(3, false);
			result.set(4, false);
			result.set(5, true);
			result.set(6, true);
			
			int imm12 = Integer.parseInt(instructionSplit[3]);
			BitSet imm12Array = itba(imm12, 12);
			
			// imm5
			result.set(7, imm12Array.get(11));
			result.set(8, imm12Array.get(1));
			result.set(9, imm12Array.get(2));
			result.set(10, imm12Array.get(3));
			result.set(11, imm12Array.get(4));
			
			// funct3
			
			// nothing needed
			
			// rs1
			
			int rs1 = Integer.parseInt(instructionSplit[1]);
			BitSet rs1Array = itba(rs1, 5);
			
			j = 0;
			for (int i = 15; i < 20; i++) {
				result.set(i, rs1Array.get(j));
				j++;
			}
			
			// rs2
			
			int rs2 = Integer.parseInt(instructionSplit[2]);
			BitSet rs2Array = itba(rs2, 5);
			
			j = 0;
			for (int i = 20; i < 25; i++) {
				result.set(i, rs2Array.get(j));
				j++;
			}
			
			// imm7
			
			result.set(25, imm12Array.get(5));
			result.set(26, imm12Array.get(6));
			result.set(27, imm12Array.get(7));
			result.set(28, imm12Array.get(8));
			result.set(29, imm12Array.get(9));
			result.set(30, imm12Array.get(10));
			result.set(31, imm12Array.get(12));
			
		}
		
		if (instructionSplit[0].equals("bne")) {
			
			// codeop
			result.set(0, true);
			result.set(1, true);
			result.set(2, false);
			result.set(3, false);
			result.set(4, false);
			result.set(5, true);
			result.set(6, true);
			
			int imm12 = Integer.parseInt(instructionSplit[3]);
			BitSet imm12Array = itba(imm12, 12);
			
			// imm5
			result.set(7, imm12Array.get(11));
			result.set(8, imm12Array.get(1));
			result.set(9, imm12Array.get(2));
			result.set(10, imm12Array.get(3));
			result.set(11, imm12Array.get(4));
			
			// funct3
			
			result.set(12, true);
			
			// rs1
			
			int rs1 = Integer.parseInt(instructionSplit[1]);
			BitSet rs1Array = itba(rs1, 5);
			
			j = 0;
			for (int i = 15; i < 20; i++) {
				result.set(i, rs1Array.get(j));
				j++;
			}
			
			// rs2
			
			int rs2 = Integer.parseInt(instructionSplit[2]);
			BitSet rs2Array = itba(rs2, 5);
			
			j = 0;
			for (int i = 20; i < 24; i++) {
				result.set(i, rs2Array.get(j));
				j++;
			}
			
			// imm7
			
			result.set(25, imm12Array.get(5));
			result.set(26, imm12Array.get(6));
			result.set(27, imm12Array.get(7));
			result.set(28, imm12Array.get(8));
			result.set(29, imm12Array.get(9));
			result.set(30, imm12Array.get(10));
			result.set(31, imm12Array.get(12));
				
		}
		if (instructionSplit[0].equals("blt")) {
			
			// codeop
			
			result.set(0, true);
			result.set(1, true);
			result.set(2, false);
			result.set(3, false);
			result.set(4, false);
			result.set(5, true);
			result.set(6, true);
			
			int imm12 = Integer.parseInt(instructionSplit[3]);
			BitSet imm12Array = itba(imm12, 12);
			
			// imm5
			result.set(7, imm12Array.get(11));
			result.set(8, imm12Array.get(1));
			result.set(9, imm12Array.get(2));
			result.set(10, imm12Array.get(3));
			result.set(11, imm12Array.get(4));
			
			// funct3
			
			result.set(14, true);
			
			// rs1
			
			int rs1 = Integer.parseInt(instructionSplit[1]);
			BitSet rs1Array = itba(rs1, 5);
			
			j = 0;
			for (int i = 15; i < 20; i++) {
				result.set(i, rs1Array.get(j));
				j++;
			}
			
			// rs2
			
			int rs2 = Integer.parseInt(instructionSplit[2]);
			BitSet rs2Array = itba(rs2, 5);
			
			j = 0;
			for (int i = 20; i < 24; i++) {
				result.set(i, rs2Array.get(j));
				j++;
			}
			
			// imm7
			
			result.set(25, imm12Array.get(5));
			result.set(26, imm12Array.get(6));
			result.set(27, imm12Array.get(7));
			result.set(28, imm12Array.get(8));
			result.set(29, imm12Array.get(9));
			result.set(30, imm12Array.get(10));
			result.set(31, imm12Array.get(12));
						
		}
		if (instructionSplit[0].equals("bge")) {
			
			// codeop
			result.set(0, true);
			result.set(1, true);
			result.set(2, false);
			result.set(3, false);
			result.set(4, false);
			result.set(5, true);
			result.set(6, true);
			
			int imm12 = Integer.parseInt(instructionSplit[3]);
			BitSet imm12Array = itba(imm12, 12);
			
			// imm5
			result.set(7, imm12Array.get(11));
			result.set(8, imm12Array.get(1));
			result.set(9, imm12Array.get(2));
			result.set(10, imm12Array.get(3));
			result.set(11, imm12Array.get(4));
			
			// funct3
			
			result.set(12, true);
			result.set(14, true);
			
			// rs1
			
			int rs1 = Integer.parseInt(instructionSplit[1]);
			BitSet rs1Array = itba(rs1, 5);
			
			j = 0;
			for (int i = 15; i < 20; i++) {
				result.set(i, rs1Array.get(j));
				j++;
			}
			
			// rs2
			
			int rs2 = Integer.parseInt(instructionSplit[2]);
			BitSet rs2Array = itba(rs2, 5);
			
			j = 0;
			for (int i = 20; i < 24; i++) {
				result.set(i, rs2Array.get(j));
				j++;
			}
			
			// imm7
			
			result.set(25, imm12Array.get(5));
			result.set(26, imm12Array.get(6));
			result.set(27, imm12Array.get(7));
			result.set(28, imm12Array.get(8));
			result.set(29, imm12Array.get(9));
			result.set(30, imm12Array.get(10));
			result.set(31, imm12Array.get(12));
			
		}
		if (instructionSplit[0].equals("bltu")) {
			
			// codeop
			result.set(0, true);
			result.set(1, true);
			result.set(2, false);
			result.set(3, false);
			result.set(4, false);
			result.set(5, true);
			result.set(6, true);
			
			int imm12 = Integer.parseInt(instructionSplit[3]);
			BitSet imm12Array = itba(imm12, 12);
			
			// imm5
			result.set(7, imm12Array.get(11));
			result.set(8, imm12Array.get(1));
			result.set(9, imm12Array.get(2));
			result.set(10, imm12Array.get(3));
			result.set(11, imm12Array.get(4));
			
			// funct3
			
			result.set(14, true);
			result.set(13, true);
			
			// rs1
			
			int rs1 = Integer.parseInt(instructionSplit[1]);
			BitSet rs1Array = itba(rs1, 5);
			
			j = 0;
			for (int i = 15; i < 20; i++) {
				result.set(i, rs1Array.get(j));
				j++;
			}
			
			// rs2
			
			int rs2 = Integer.parseInt(instructionSplit[2]);
			BitSet rs2Array = itba(rs2, 5);
			
			j = 0;
			for (int i = 20; i < 24; i++) {
				result.set(i, rs2Array.get(j));
				j++;
			}
			
			// imm7
			
			result.set(25, imm12Array.get(5));
			result.set(26, imm12Array.get(6));
			result.set(27, imm12Array.get(7));
			result.set(28, imm12Array.get(8));
			result.set(29, imm12Array.get(9));
			result.set(30, imm12Array.get(10));
			result.set(31, imm12Array.get(12));
			
		}
		if (instructionSplit[0].equals("bgeu")) {
			
			// codeop
			result.set(0, true);
			result.set(1, true);
			result.set(2, false);
			result.set(3, false);
			result.set(4, false);
			result.set(5, true);
			result.set(6, true);
			
			// rd
			
			
			
			int imm12 = Integer.parseInt(instructionSplit[3]);
			BitSet imm12Array = itba(imm12, 12);
			
			// imm5
			result.set(7, imm12Array.get(11));
			result.set(8, imm12Array.get(1));
			result.set(9, imm12Array.get(2));
			result.set(10, imm12Array.get(3));
			result.set(11, imm12Array.get(4));
			
			// funct3
			
			result.set(12, true);
			result.set(13, true);
			result.set(14, true);
			
			// rs1
			
			int rs1 = Integer.parseInt(instructionSplit[1]);
			BitSet rs1Array = itba(rs1, 5);
			
			j = 0;
			for (int i = 15; i < 20; i++) {
				result.set(i, rs1Array.get(j));
				j++;
			}
			
			// rs2
			
			int rs2 = Integer.parseInt(instructionSplit[2]);
			BitSet rs2Array = itba(rs2, 5);
			
			j = 0;
			for (int i = 20; i < 24; i++) {
				result.set(i, rs2Array.get(j));
				j++;
			}
			
			// imm7
			
			result.set(25, imm12Array.get(5));
			result.set(26, imm12Array.get(6));
			result.set(27, imm12Array.get(7));
			result.set(28, imm12Array.get(8));
			result.set(29, imm12Array.get(9));
			result.set(30, imm12Array.get(10));
			result.set(31, imm12Array.get(12));
						
		}
		if (instructionSplit[0].equals("lb")) {
			// codeop
			
			result.set(0, true);
			result.set(1, true);
			
			// rd
			int rd = Integer.parseInt(instructionSplit[1]);
			BitSet rdArray = itba(rd, 5);
			
			j = 0;
			for (int i = 7; i < 12; i++) {
				result.set(i, rdArray.get(j));
				j++;
			}
			
			int imm12 = Integer.parseInt(instructionSplit[2]);
			BitSet imm12Array = itba(imm12, 12);

			j = 0;
			for (int i = 20; i < 32; i++) {
				result.set(i, imm12Array.get(j));
				j++;
			}
			
			// funct3
			
			// Nothing needed
			
			// rs1
			
			if (instructionSplit.length != 3) {
				int rs1 = Integer.parseInt(instructionSplit[3]);
				BitSet rs1Array = itba(rs1, 5);
				
				j = 0;
				for (int i = 15; i < 20; i++) {
					result.set(i, rs1Array.get(j));
					j++;
				}
			}	
		}
		
		if (instructionSplit[0].equals("lh")) {
			
			// codeop
			
			result.set(0, true);
			result.set(1, true);
			
			// rd
			
			int rd = Integer.parseInt(instructionSplit[1]);
			BitSet rdArray = itba(rd, 5);
			
			j = 0;
			for (int i = 7; i < 12; i++) {
				result.set(i, rdArray.get(j));
				j++;
			}
			
			int imm12 = Integer.parseInt(instructionSplit[2]);
			BitSet imm12Array = itba(imm12, 12);

			j = 0;
			for (int i = 20; i < 32; i++) {
				result.set(i, imm12Array.get(j));
				j++;
			}
			
			// funct3
			
			result.set(12, true);
			
			// rs1
			
			if (instructionSplit.length != 3) {
				int rs1 = Integer.parseInt(instructionSplit[3]);
				BitSet rs1Array = itba(rs1, 5);
				
				j = 0;
				for (int i = 15; i < 20; i++) {
					result.set(i, rs1Array.get(j));
					j++;
				}
			}		
		}
		if (instructionSplit[0].equals("lw")) {
			
			// codeop
			
			result.set(0, true);
			result.set(1, true);
			
			// rd
			
			int rd = Integer.parseInt(instructionSplit[1]);
			BitSet rdArray = itba(rd, 5);
			
			j = 0;
			for (int i = 7; i < 12; i++) {
				result.set(i, rdArray.get(j));
				j++;
			}
			
			int imm12 = Integer.parseInt(instructionSplit[2]);
			BitSet imm12Array = itba(imm12, 12);

			j = 0;
			for (int i = 20; i < 32; i++) {
				result.set(i, imm12Array.get(j));
				j++;
			}
			
			// funct3
			
			result.set(13, true);
			
			// rs1
			
			int rs1 = Integer.parseInt(instructionSplit[3]);
			BitSet rs1Array = itba(rs1, 5);
			
			j = 0;
			for (int i = 15; i < 20; i++) {
				result.set(i, rs1Array.get(j));
				j++;
			}		
		}		
		if (instructionSplit[0].equals("lbu")) {

			
			// codeop
			
			result.set(0, true);
			result.set(1, true);
			
			// rd
			
			int rd = Integer.parseInt(instructionSplit[1]);
			BitSet rdArray = itba(rd, 5);
			
			j = 0;
			for (int i = 7; i < 12; i++) {
				result.set(i, rdArray.get(j));
				j++;
			}
			
			// funct3
			
			result.set(14, true);

			String[] immReg = new String[2];
			if (!instructionSplit[2].contains("\\(")) {
				immReg[0] = instructionSplit[2];
				immReg[1] = "0";
			} else {
				immReg = instructionSplit[2].split("\\(");
				immReg[1] = immReg[1].replaceAll("\\)", "");
			}
			
			// rs1
			
			int rs1 = Integer.parseInt(immReg[1]);
			BitSet rs1Array = itba(rs1, 5);
			
			j = 0;
			for (int i = 15; i < 20; i++) {
				result.set(i, rs1Array.get(j));
				j++;
			}
			
			// imm12
			
			int imm12 = Integer.parseInt(immReg[0]);
			BitSet imm12Array = itba(imm12, 12);
			
			j = 0;
			for (int i = 20; i < 32; i++) {
				result.set(i, imm12Array.get(j));
				j++;
			}
		}
		if (instructionSplit[0].equals("lhu")) {
			
			// codeop
			
			result.set(0, true);
			result.set(1, true);
			
			// rd
			
			int rd = Integer.parseInt(instructionSplit[1]);
			BitSet rdArray = itba(rd, 5);
			
			j = 0;
			for (int i = 7; i < 12; i++) {
				result.set(i, rdArray.get(j));
				j++;
			}
			
			int imm12 = Integer.parseInt(instructionSplit[2]);
			BitSet imm12Array = itba(imm12, 12);

			j = 0;
			for (int i = 20; i < 32; i++) {
				result.set(i, imm12Array.get(j));
				j++;
			}
			
			// funct3
			
			result.set(12, true);
			result.set(14, true);
			
			// rs1
			
			if (instructionSplit.length != 3) {
				int rs1 = Integer.parseInt(instructionSplit[3]);
				BitSet rs1Array = itba(rs1, 5);
				
				j = 0;
				for (int i = 15; i < 20; i++) {
					result.set(i, rs1Array.get(j));
					j++;
				}
			}
		}
		
		if (instructionSplit[0].equals("sb")) {
			
			// codeop
			
			result.set(0, true);
			result.set(1, true);
			result.set(5, true);
			
			int imm12 = Integer.parseInt(instructionSplit[2]);
			BitSet imm12Array = itba(imm12, 12);
			result.set(7, imm12Array.get(0));
			result.set(8, imm12Array.get(1));
			result.set(9, imm12Array.get(2));
			result.set(10, imm12Array.get(3));
			result.set(11, imm12Array.get(4));
			
			// funct3
			
			// Nothing needed
			
			// rs1
			if (instructionSplit.length != 3) {
				int rs1 = Integer.parseInt(instructionSplit[3]);
				BitSet rs1Array = itba(rs1, 5);
				
				j = 0;
				for (int i = 15; i < 20; i++) {
					result.set(i, rs1Array.get(j));
					j++;
				}
			}
			
			// rs2
			
			int rs2 = Integer.parseInt(instructionSplit[1]);
			BitSet rs2Array = itba(rs2, 5);
			
			j = 0;
			for (int i = 20; i < 25; i++) {
				result.set(i, rs2Array.get(j));
				j++;
			}
			
			// imm7
			
			result.set(25, imm12Array.get(5));
			result.set(26, imm12Array.get(6));
			result.set(27, imm12Array.get(7));
			result.set(28, imm12Array.get(8));
			result.set(29, imm12Array.get(9));
			result.set(30, imm12Array.get(10));
			result.set(31, imm12Array.get(11));
					
		}
		if (instructionSplit[0].equals("sh")) {
			
			// codeop
			
			result.set(0, true);
			result.set(1, true);
			result.set(5, true);
			
			int imm12 = Integer.parseInt(instructionSplit[2]);
			BitSet imm12Array = itba(imm12, 12);
			result.set(7, imm12Array.get(0));
			result.set(8, imm12Array.get(1));
			result.set(9, imm12Array.get(2));
			result.set(10, imm12Array.get(3));
			result.set(11, imm12Array.get(4));
			
			// funct3
			
			result.set(12, true);
			
			// rs1
			if (instructionSplit.length != 3) {
				int rs1 = Integer.parseInt(instructionSplit[3]);
				BitSet rs1Array = itba(rs1, 5);
				
				j = 0;
				for (int i = 15; i < 20; i++) {
					result.set(i, rs1Array.get(j));
					j++;
				}
			}
			
			
			// rs2
			
			int rs2 = Integer.parseInt(instructionSplit[1]);
			BitSet rs2Array = itba(rs2, 5);
			
			j = 0;
			for (int i = 20; i < 25; i++) {
				result.set(i, rs2Array.get(j));
				j++;
			}
			
			// imm7
			
			result.set(25, imm12Array.get(5));
			result.set(26, imm12Array.get(6));
			result.set(27, imm12Array.get(7));
			result.set(28, imm12Array.get(8));
			result.set(29, imm12Array.get(9));
			result.set(30, imm12Array.get(10));
			result.set(31, imm12Array.get(11));
					
		}
		if (instructionSplit[0].equals("sw")) {
			
			// codeop
			
			result.set(0, true);
			result.set(1, true);
			result.set(5, true);
			
			int imm12 = Integer.parseInt(instructionSplit[2]);
			BitSet imm12Array = itba(imm12, 12);
			result.set(7, imm12Array.get(0));
			result.set(8, imm12Array.get(1));
			result.set(9, imm12Array.get(2));
			result.set(10, imm12Array.get(3));
			result.set(11, imm12Array.get(4));
			
			// funct3
			
			result.set(13, true);
			
			// rs1
			if (instructionSplit.length != 3) {
				int rs1 = Integer.parseInt(instructionSplit[3]);
				BitSet rs1Array = itba(rs1, 5);
				
				j = 0;
				for (int i = 15; i < 20; i++) {
					result.set(i, rs1Array.get(j));
					j++;
				}
			}
			
			
			// rs2
			
			int rs2 = Integer.parseInt(instructionSplit[1]);
			BitSet rs2Array = itba(rs2, 5);
			
			j = 0;
			for (int i = 20; i < 25; i++) {
				result.set(i, rs2Array.get(j));
				j++;
			}
			
			// imm7
			
			result.set(25, imm12Array.get(5));
			result.set(26, imm12Array.get(6));
			result.set(27, imm12Array.get(7));
			result.set(28, imm12Array.get(8));
			result.set(29, imm12Array.get(9));
			result.set(30, imm12Array.get(10));
			result.set(31, imm12Array.get(11));
					
		}
		if (instructionSplit[0].equals("addi")) {
			
			// codeop
			
			result.set(0, true);
			result.set(1, true);
			result.set(4, true);
			
			// rd
			int rd = Integer.parseInt(instructionSplit[1]);
			BitSet rdArray = itba(rd, 5);
			
			j = 0;
			for (int i = 7; i < 12; i++) {
				result.set(i, rdArray.get(j));
				j++;
			}
			
			// funct3
			
			// (nothing needed)
			
			// rs1
			int rs1 = Integer.parseInt(instructionSplit[2]);
			BitSet rs1Array = itba(rs1, 5);
			
			j = 0;
			for (int i = 15; i < 20; i++) {
				result.set(i, rs1Array.get(j));
				j++;
			}
			
			// imm12
			int imm12 = Integer.parseInt(instructionSplit[3]);
			BitSet imm12Array = itba(imm12, 12);
			
			j = 0;
			for (int i = 20; i < 32; i++) {
				result.set(i, imm12Array.get(j));
				j++;
			}
		}
		if (instructionSplit[0].equals("slti")) {
			
			// codeop
			
			result.set(0, true);
			result.set(1, true);
			result.set(4, true);
			
			// rd
			int rd = Integer.parseInt(instructionSplit[1]);
			BitSet rdArray = itba(rd, 5);
			
			j = 0;
			for (int i = 7; i < 12; i++) {
				result.set(i, rdArray.get(j));
				j++;
			}
			
			// funct3
			
			result.set(13, true);

			
			// rs1
			int rs1 = Integer.parseInt(instructionSplit[2]);
			BitSet rs1Array = itba(rs1, 5);
			
			j = 0;
			for (int i = 15; i < 20; i++) {
				result.set(i, rs1Array.get(j));
				j++;
			}
			
			// imm12
			int imm12 = Integer.parseInt(instructionSplit[3]);
			BitSet imm12Array = itba(imm12, 12);
			
			j = 0;
			for (int i = 20; i < 32; i++) {
				result.set(i, imm12Array.get(j));
				j++;
			}
		}
		if (instructionSplit[0].equals("sltiu")) {

			// codeop
			
			result.set(0, true);
			result.set(1, true);
			result.set(4, true);
			
			// rd
			int rd = Integer.parseInt(instructionSplit[1]);
			BitSet rdArray = itba(rd, 5);
			
			j = 0;
			for (int i = 7; i < 12; i++) {
				result.set(i, rdArray.get(j));
				j++;
			}
			
			// funct3
			
			result.set(12, true);
			result.set(13, true);

			
			// rs1
			int rs1 = Integer.parseInt(instructionSplit[2]);
			BitSet rs1Array = itba(rs1, 5);
			
			j = 0;
			for (int i = 15; i < 20; i++) {
				result.set(i, rs1Array.get(j));
				j++;
			}
			
			// imm12
			int imm12 = Integer.parseInt(instructionSplit[3]);
			BitSet imm12Array = itba(imm12, 12);
			
			j = 0;
			for (int i = 20; i < 32; i++) {
				result.set(i, imm12Array.get(j));
				j++;
			}
		}
		if (instructionSplit[0].equals("xori")) {

			// codeop
			
			result.set(0, true);
			result.set(1, true);
			result.set(4, true);
			
			// rd
			int rd = Integer.parseInt(instructionSplit[1]);
			BitSet rdArray = itba(rd, 5);
			
			j = 0;
			for (int i = 7; i < 12; i++) {
				result.set(i, rdArray.get(j));
				j++;
			}
			
			// funct3
			
			result.set(14, true);
			
			// rs1
			int rs1 = Integer.parseInt(instructionSplit[2]);
			BitSet rs1Array = itba(rs1, 5);
			
			j = 0;
			for (int i = 15; i < 20; i++) {
				result.set(i, rs1Array.get(j));
				j++;
			}
			
			// imm12
			int imm12 = Integer.parseInt(instructionSplit[3]);
			BitSet imm12Array = itba(imm12, 12);
			
			j = 0;
			for (int i = 20; i < 32; i++) {
				result.set(i, imm12Array.get(j));
				j++;
			}			
		}
		if (instructionSplit[0].equals("ori")) {
	
			// codeop
			
			result.set(0, true);
			result.set(1, true);
			result.set(4, true);
			
			// rd
			int rd = Integer.parseInt(instructionSplit[1]);
			BitSet rdArray = itba(rd, 5);
			
			j = 0;
			for (int i = 7; i < 12; i++) {
				result.set(i, rdArray.get(j));
				j++;
			}
			
			// funct3
			
			result.set(14, true);
			result.set(13, true);

			
			// rs1
			int rs1 = Integer.parseInt(instructionSplit[2]);
			BitSet rs1Array = itba(rs1, 5);
			
			j = 0;
			for (int i = 15; i < 20; i++) {
				result.set(i, rs1Array.get(j));
				j++;
			}
			
			// imm12
			int imm12 = Integer.parseInt(instructionSplit[3]);
			BitSet imm12Array = itba(imm12, 12);
			
			j = 0;
			for (int i = 20; i < 32; i++) {
				result.set(i, imm12Array.get(j));
				j++;
			}
		}
		if (instructionSplit[0].equals("andi")) {

			// codeop
			
			result.set(0, true);
			result.set(1, true);
			result.set(4, true);
			
			// rd
			int rd = Integer.parseInt(instructionSplit[1]);
			BitSet rdArray = itba(rd, 5);
			
			j = 0;
			for (int i = 7; i < 12; i++) {
				result.set(i, rdArray.get(j));
				j++;
			}
			
			// funct3
			
			result.set(12, true);
			result.set(13, true);
			result.set(14, true);

			
			// rs1
			int rs1 = Integer.parseInt(instructionSplit[2]);
			BitSet rs1Array = itba(rs1, 5);
			
			j = 0;
			for (int i = 15; i < 20; i++) {
				result.set(i, rs1Array.get(j));
				j++;
			}
			
			// imm12
			int imm12 = Integer.parseInt(instructionSplit[3]);
			BitSet imm12Array = itba(imm12, 12);
			
			j = 0;
			for (int i = 20; i < 32; i++) {
				result.set(i, imm12Array.get(j));
				j++;
			}
		}
		if (instructionSplit[0].equals("slli")) {

			// codeop
			
			result.set(0, true);
			result.set(1, true);
			result.set(4, true);
			
			// rd
			int rd = Integer.parseInt(instructionSplit[1]);
			BitSet rdArray = itba(rd, 5);
			
			j = 0;
			for (int i = 7; i < 12; i++) {
				result.set(i, rdArray.get(j));
				j++;
			}
			
			// funct3
			
			result.set(12, true);

			
			// rs1
			int rs1 = Integer.parseInt(instructionSplit[2]);
			BitSet rs1Array = itba(rs1, 5);
			
			j = 0;
			for (int i = 15; i < 20; i++) {
				result.set(i, rs1Array.get(j));
				j++;
			}
			
			// imm12
			int imm12 = Integer.parseInt(instructionSplit[3]);
			BitSet imm12Array = itba(imm12, 12);
			
			j = 0;
			for (int i = 20; i < 25; i++) {
				result.set(i, imm12Array.get(j));
				j++;
			}
			
			
		}
		if (instructionSplit[0].equals("srli")) {

			// codeop
			
			result.set(0, true);
			result.set(1, true);
			result.set(4, true);
			
			// rd
			int rd = Integer.parseInt(instructionSplit[1]);
			BitSet rdArray = itba(rd, 5);
			
			j = 0;
			for (int i = 7; i < 12; i++) {
				result.set(i, rdArray.get(j));
				j++;
			}
			
			// funct3
			
			result.set(12, true);
			result.set(14, true);
			
			// rs1
			int rs1 = Integer.parseInt(instructionSplit[2]);
			BitSet rs1Array = itba(rs1, 5);
			
			j = 0;
			for (int i = 15; i < 20; i++) {
				result.set(i, rs1Array.get(j));
				j++;
			}
			
			// imm12
			int imm12 = Integer.parseInt(instructionSplit[3]);
			BitSet imm12Array = itba(imm12, 12);
			
			j = 0;
			for (int i = 20; i < 25; i++) {
				result.set(i, imm12Array.get(j));
				j++;
			}
		}
		
		if (instructionSplit[0].equals("srai")) {

			// codeop
			
			result.set(0, true);
			result.set(1, true);
			result.set(4, true);
			
			// rd
			int rd = Integer.parseInt(instructionSplit[1]);
			BitSet rdArray = itba(rd, 5);
			
			j = 0;
			for (int i = 7; i < 12; i++) {
				result.set(i, rdArray.get(j));
				j++;
			}
			
			// funct3
			
			result.set(12, true);
			result.set(14, true);
			
			// rs1
			int rs1 = Integer.parseInt(instructionSplit[2]);
			BitSet rs1Array = itba(rs1, 5);
			
			j = 0;
			for (int i = 15; i < 20; i++) {
				result.set(i, rs1Array.get(j));
				j++;
			}
			
			// imm12
			int imm12 = Integer.parseInt(instructionSplit[3]);
			BitSet imm12Array = itba(imm12, 12);
			
			j = 0;
			for (int i = 20; i < 25; i++) {
				result.set(i, imm12Array.get(j));
				j++;
			}
			
			// instr30
			result.set(30, true);
			
		}
		if (instructionSplit[0].equals("add")) {
			
			// codeop
			
			result.set(0, true);
			result.set(1, true);
			result.set(4, true);
			result.set(5, true);
			
			// rd
			
			int rd = Integer.parseInt(instructionSplit[1]);
			BitSet rdArray = itba(rd, 5);
			
			j = 0;
			for (int i = 7; i < 12; i++) {
				result.set(i, rdArray.get(j));
				j++;
			}
			
			// funct3
			
			// Nothing needed
			
			// rs1
			
			int rs1 = Integer.parseInt(instructionSplit[2]);
			BitSet rs1Array = itba(rs1, 5);
			
			j = 0;
			for (int i = 15; i < 20; i++) {
				result.set(i, rs1Array.get(j));
				j++;
			}
			
			// rs2
			
			int rs2 = Integer.parseInt(instructionSplit[3]);
			BitSet rs2Array = itba(rs2, 5);
			
			j = 0;
			for (int i = 20; i < 25; i++) {
				result.set(i, rs2Array.get(j));
				j++;
			}
			
			// instr30
			
			// nothing needed
		}
		
		if (instructionSplit[0].equals("sub")) {

			
			// codeop
			
			result.set(0, true);
			result.set(1, true);
			result.set(4, true);
			result.set(5, true);
			
			// rd
			
			int rd = Integer.parseInt(instructionSplit[1]);
			BitSet rdArray = itba(rd, 5);
			
			j = 0;
			for (int i = 7; i < 12; i++) {
				result.set(i, rdArray.get(j));
				j++;
			}
			
			// funct3
			
			// Nothing needed
			
			// rs1
			
			int rs1 = Integer.parseInt(instructionSplit[2]);
			BitSet rs1Array = itba(rs1, 5);
			
			j = 0;
			for (int i = 15; i < 20; i++) {
				result.set(i, rs1Array.get(j));
				j++;
			}
			
			// rs2
			
			int rs2 = Integer.parseInt(instructionSplit[3]);
			BitSet rs2Array = itba(rs2, 5);
			
			j = 0;
			for (int i = 20; i < 25; i++) {
				result.set(i, rs2Array.get(j));
				j++;
			}
			
			// instr30

			result.set(30, true);
		}
		
		if (instructionSplit[0].equals("sll")) {

			
			// codeop
			
			result.set(0, true);
			result.set(1, true);
			result.set(4, true);
			result.set(5, true);
			
			// rd
			
			int rd = Integer.parseInt(instructionSplit[1]);
			BitSet rdArray = itba(rd, 5);
			
			j = 0;
			for (int i = 7; i < 12; i++) {
				result.set(i, rdArray.get(j));
				j++;
			}
			
			// funct3
			
			result.set(12, true);
			
			// rs1
			
			int rs1 = Integer.parseInt(instructionSplit[2]);
			BitSet rs1Array = itba(rs1, 5);
			
			j = 0;
			for (int i = 15; i < 20; i++) {
				result.set(i, rs1Array.get(j));
				j++;
			}
			
			// rs2
			
			int rs2 = Integer.parseInt(instructionSplit[3]);
			BitSet rs2Array = itba(rs2, 5);
			
			j = 0;
			for (int i = 20; i < 25; i++) {
				result.set(i, rs2Array.get(j));
				j++;
			}
			
			// instr30
			
			// nothing needed
			
		}
		if (instructionSplit[0].equals("slt")) {
	
			
			// codeop
			
			result.set(0, true);
			result.set(1, true);
			result.set(4, true);
			result.set(5, true);
			
			// rd
			
			int rd = Integer.parseInt(instructionSplit[1]);
			BitSet rdArray = itba(rd, 5);
			
			j = 0;
			for (int i = 7; i < 12; i++) {
				result.set(i, rdArray.get(j));
				j++;
			}
			
			// funct3
			
			result.set(13, true);
			
			// rs1
			
			int rs1 = Integer.parseInt(instructionSplit[2]);
			BitSet rs1Array = itba(rs1, 5);
			
			j = 0;
			for (int i = 15; i < 20; i++) {
				result.set(i, rs1Array.get(j));
				j++;
			}
			
			// rs2
			
			int rs2 = Integer.parseInt(instructionSplit[3]);
			BitSet rs2Array = itba(rs2, 5);
			
			j = 0;
			for (int i = 20; i < 25; i++) {
				result.set(i, rs2Array.get(j));
				j++;
			}
			
			// instr30
			
			// nothing needed
		}
		if (instructionSplit[0].equals("sltu")) {

			
			// codeop
			
			result.set(0, true);
			result.set(1, true);
			result.set(4, true);
			result.set(5, true);
			
			// rd
			
			int rd = Integer.parseInt(instructionSplit[1]);
			BitSet rdArray = itba(rd, 5);
			
			j = 0;
			for (int i = 7; i < 12; i++) {
				result.set(i, rdArray.get(j));
				j++;
			}
			
			// funct3
			
			result.set(12, true);
			result.set(13, true);
			
			// rs1
			
			int rs1 = Integer.parseInt(instructionSplit[2]);
			BitSet rs1Array = itba(rs1, 5);
			
			j = 0;
			for (int i = 15; i < 20; i++) {
				result.set(i, rs1Array.get(j));
				j++;
			}
			
			// rs2
			
			int rs2 = Integer.parseInt(instructionSplit[3]);
			BitSet rs2Array = itba(rs2, 5);
			
			j = 0;
			for (int i = 20; i < 25; i++) {
				result.set(i, rs2Array.get(j));
				j++;
			}
			
			// instr30
			
			// nothing needed
		}
		if (instructionSplit[0].equals("xor")) {

			// codeop
			
			result.set(0, true);
			result.set(1, true);
			result.set(4, true);
			result.set(5, true);
			
			// rd
			
			int rd = Integer.parseInt(instructionSplit[1]);
			BitSet rdArray = itba(rd, 5);
			
			j = 0;
			for (int i = 7; i < 12; i++) {
				result.set(i, rdArray.get(j));
				j++;
			}
			
			// funct3
			
			result.set(14, true);
			
			// rs1
			
			int rs1 = Integer.parseInt(instructionSplit[2]);
			BitSet rs1Array = itba(rs1, 5);
			
			j = 0;
			for (int i = 15; i < 20; i++) {
				result.set(i, rs1Array.get(j));
				j++;
			}
			
			// rs2
			
			int rs2 = Integer.parseInt(instructionSplit[3]);
			BitSet rs2Array = itba(rs2, 5);
			
			j = 0;
			for (int i = 20; i < 25; i++) {
				result.set(i, rs2Array.get(j));
				j++;
			}
			
			// instr30
			
			// nothing needed
		}
		if (instructionSplit[0].equals("srl")) {

			
			// codeop
			
			result.set(0, true);
			result.set(1, true);
			result.set(4, true);
			result.set(5, true);
			
			// rd
			
			int rd = Integer.parseInt(instructionSplit[1]);
			BitSet rdArray = itba(rd, 5);
			
			j = 0;
			for (int i = 7; i < 12; i++) {
				result.set(i, rdArray.get(j));
				j++;
			}
			
			// funct3
			
			result.set(12, true);
			result.set(14, true);
			
			// rs1
			
			int rs1 = Integer.parseInt(instructionSplit[2]);
			BitSet rs1Array = itba(rs1, 5);
			
			j = 0;
			for (int i = 15; i < 20; i++) {
				result.set(i, rs1Array.get(j));
				j++;
			}
			
			// rs2
			
			int rs2 = Integer.parseInt(instructionSplit[3]);
			BitSet rs2Array = itba(rs2, 5);
			
			j = 0;
			for (int i = 20; i < 25; i++) {
				result.set(i, rs2Array.get(j));
				j++;
			}
			
			// instr30
			
			// nothing needed
		}
		if (instructionSplit[0].equals("sra")) {
			
			// codeop
			
			result.set(0, true);
			result.set(1, true);
			result.set(4, true);
			result.set(5, true);
			
			// rd
			
			int rd = Integer.parseInt(instructionSplit[1]);
			BitSet rdArray = itba(rd, 5);
			
			j = 0;
			for (int i = 7; i < 12; i++) {
				result.set(i, rdArray.get(j));
				j++;
			}
			
			// funct3
			
			result.set(12, true);
			result.set(14, true);
			
			// rs1
			
			int rs1 = Integer.parseInt(instructionSplit[2]);
			BitSet rs1Array = itba(rs1, 5);
			
			j = 0;
			for (int i = 15; i < 20; i++) {
				result.set(i, rs1Array.get(j));
				j++;
			}
			
			// rs2
			
			int rs2 = Integer.parseInt(instructionSplit[3]);
			BitSet rs2Array = itba(rs2, 5);
			
			j = 0;
			for (int i = 20; i < 25; i++) {
				result.set(i, rs2Array.get(j));
				j++;
			}
			
			// instr30
			
			result.set(30, true);	
		}
		if (instructionSplit[0].equals("or")) {
			
			// codeop

			result.set(0, true);
			result.set(1, true);
			result.set(4, true);
			result.set(5, true);
			
			// rd
			
			int rd = Integer.parseInt(instructionSplit[1]);
			BitSet rdArray = itba(rd, 5);
			
			j = 0;
			for (int i = 7; i < 12; i++) {
				result.set(i, rdArray.get(j));
				j++;
			}
			
			// funct3
			
			result.set(14, true);
			result.set(13, true);
			
			// rs1
			
			int rs1 = Integer.parseInt(instructionSplit[2]);
			BitSet rs1Array = itba(rs1, 5);
			
			j = 0;
			for (int i = 15; i < 20; i++) {
				result.set(i, rs1Array.get(j));
				j++;
			}
			
			// rs2
			
			int rs2 = Integer.parseInt(instructionSplit[3]);
			BitSet rs2Array = itba(rs2, 5);
			
			j = 0;
			for (int i = 20; i < 25; i++) {
				result.set(i, rs2Array.get(j));
				j++;
			}
			
			// instr30
			
			// nothing needed	
		}
		if (instructionSplit[0].equals("and")) {

			
			// codeop
			
			result.set(0, true);
			result.set(1, true);
			result.set(4, true);
			result.set(5, true);
			
			// rd
			
			int rd = Integer.parseInt(instructionSplit[1]);
			BitSet rdArray = itba(rd, 5);
			
			j = 0;
			for (int i = 7; i < 12; i++) {
				result.set(i, rdArray.get(j));
				j++;
			}
			
			// funct3
			
			result.set(12, true);
			result.set(13, true);
			result.set(14, true);
			
			// rs1
			
			int rs1 = Integer.parseInt(instructionSplit[2]);
			BitSet rs1Array = itba(rs1, 5);
			
			j = 0;
			for (int i = 15; i < 20; i++) {
				result.set(i, rs1Array.get(j));
				j++;
			}
			
			// rs2
			
			int rs2 = Integer.parseInt(instructionSplit[3]);
			BitSet rs2Array = itba(rs2, 5);
			
			j = 0;
			for (int i = 20; i < 25; i++) {
				result.set(i, rs2Array.get(j));
				j++;
			}
			
			// instr30
			
			// nothing needed
		}
		return result;
	}
	
	public static BitSet assembleString(String instructionString) {
		BitSet result;
		String[] instructionSplit = instructionString.split("[ (]");
		
		for (int i = 1; i < instructionSplit.length; i++) {
			instructionSplit[i] = instructionSplit[i].replaceAll("[x,)]", "");
			instructionSplit[i] = instructionSplit[i].trim();
			if (instructionSplit[i].equals("")) {
				instructionSplit[i] = "0";
			}
		}
		
		result = fetchInstruction(instructionSplit);
		
		return result;
		
	}
	
	public static void assembleFile(String FILE_DIR) {
		
		File file = new File(FILE_DIR);
		int j = 0;
		try {
			Scanner scanner = new Scanner(file);
			BitSet instruction;
			Main.output += "Program file is: " + FILE_DIR + "\n";
			
			Main.output += "\nPROGRAM INSTRUCTIONS\n--------------------------";
			
			while (scanner.hasNextLine()) {
				String instructionString = scanner.nextLine();
				Main.output += "\n    " + instructionString;
				instruction = assembleString(instructionString);
				for (int i = 0; i < 32; i++) {
					Compiler.pm.set(j, instruction.get(i));
					j++;
				}
			}
			Main.output += "\n--------------------------\n";
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
