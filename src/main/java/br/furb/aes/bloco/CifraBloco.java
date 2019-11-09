package br.furb.aes.bloco;

import br.furb.aes.key.KeySchedule;
import br.furb.aes.table.ETable;
import br.furb.aes.table.LTable;
import br.furb.aes.table.MultiMatrix;
import br.furb.aes.table.SBox;
import br.furb.aes.util.ByteUtil;
import lombok.AllArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
public class CifraBloco {

    @Setter
    private int[] textoSimples;

    @Setter
    private KeySchedule keySchedule;

    public byte[] cifrar() {

        List<Block> blocos = quebraBloco();
        List<Block> blocoscifrados = new ArrayList<Block>();

        for (Block bloco: blocos) {

            bloco = xorMatrix(bloco);

            for (int i = 1; i < 10; i++) {
                bloco = subBytes(bloco);
                bloco = shiftRows(bloco);
                bloco = mixColumns(bloco);
                bloco = addRoundKey(bloco, i);
            }

            bloco = subBytes(bloco);
            bloco = shiftRows(bloco);
            blocoscifrados.add(addRoundKey(bloco, 10));
        }

        return uniteBlocks(blocoscifrados);

    }

    private byte[] uniteBlocks(List<Block> blocks) {

        byte[] arrayFinal = new byte[blocks.size() * 16];

        int x = 0;

        for (Block block: blocks) {
            for (int i = 0; i < block.getMatrizEstado().length; i++) {

                for (int j = 0; j < block.getMatrizEstado().length; j++) {

                    arrayFinal[x] = (byte) block.getMatrizEstado()[j][i];
                    x++;
                }

            }

        }

        return arrayFinal;

    }

    private List<Block> quebraBloco() {

        List<Block> blocos = new ArrayList<Block>();

        int keyIndex = 0;

        while (keyIndex < textoSimples.length) {
            int[][] matrizEstado = new int[4][4];
            int indexCount = 0;


            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (keyIndex < textoSimples.length) {
                        matrizEstado[j][i] = textoSimples[keyIndex];
                        indexCount++;
                    } else {
                        matrizEstado[j][i] = new Integer(16 - indexCount).byteValue();
                    }
                    keyIndex++;
                }
            }

            blocos.add(new Block(matrizEstado));
        }

        return blocos;

    }

    private Block xorMatrix(Block bloco) {

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                bloco.getMatrizEstado()[j][i] = ByteUtil.xor(bloco.getMatrizEstado()[j][i],keySchedule.get(0).getMatrizEstado()[j][i]);
            }
        }

        return bloco;

    }

    private Block subBytes(Block bloco) {

        for (int i = 0; i < 4; i++) {

            for (int j = 0; j < bloco.getMatrizEstado().length; j++) {

                int sboxEquivalent = SBox.get(ByteUtil.getLeftByte(bloco.getMatrizEstado()[j][i]), ByteUtil.getRightByte(bloco.getMatrizEstado()[j][i]));
                bloco.getMatrizEstado()[j][i] = sboxEquivalent;

            }

        }

        return bloco;

    }

    private Block shiftRows(Block bloco) {

        for (int i = 0; i < 4; i++) {

            if (i == 0) continue;

            List<Integer> aux= new ArrayList<Integer>();

            for (int k = 0; k < i; k++) {
                aux.add(bloco.getMatrizEstado()[i][k]);
            }

            Collections.reverse(aux);

            bloco.getMatrizEstado()[i][0] = bloco.getMatrizEstado()[i][i];
            bloco.getMatrizEstado()[i][1] = aux.size() > 2 ? aux.get(2) : bloco.getMatrizEstado()[i][1+ i];
            bloco.getMatrizEstado()[i][2] = aux.size() > 1 ? aux.get(1) : bloco.getMatrizEstado()[i][2+ i];
            bloco.getMatrizEstado()[i][3] = aux.get(0);
        }

        return bloco;
    }

    private Block mixColumns(Block bloco) {
        int[][] newMatrix = new int[4][4];

        for (int i = 0; i < bloco.getMatrizEstado().length; i++) {

            for (int j = 0; j < MultiMatrix.MULTI_MATRIX.length; j++) {

                int[] auxToXor = new int[4];

                for (int x = 0; x < bloco.getMatrizEstado().length; x++) {

                    try {

                        int value = 0;

                        if (bloco.getMatrizEstado()[x][i] == 1) {

                            value = MultiMatrix.MULTI_MATRIX[j][x];

                        } else if (MultiMatrix.MULTI_MATRIX[j][x] == 1) {

                            value = bloco.getMatrizEstado()[x][i];

                        } else  if (MultiMatrix.MULTI_MATRIX[x][j] != 0 && bloco.getMatrizEstado()[x][i] != 0) {

                            value =  (LTable.get(ByteUtil.getLeftByte(bloco.getMatrizEstado()[x][i]),
                                                 ByteUtil.getRightByte(bloco.getMatrizEstado()[x][i]))
                                    + LTable.get(ByteUtil.getLeftByte(MultiMatrix.MULTI_MATRIX[j][x]),
                                                 ByteUtil.getRightByte(MultiMatrix.MULTI_MATRIX[j][x])));

                            if (value > 0xff){
                                value = value - 0xff;
                            }

                            value = ETable.get(ByteUtil.getLeftByte(value), ByteUtil.getRightByte(value));
                        }

                        auxToXor[x] = value;


                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

                newMatrix[j][i] = auxToXor[0] ^ auxToXor[1] ^ auxToXor[2] ^ auxToXor[3];
            }
        }

        bloco.setMatrizEstado(newMatrix);

        return bloco;
    }

    private Block addRoundKey(Block bloco, int round) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                bloco.getMatrizEstado()[j][i] = ByteUtil.xor(bloco.getMatrizEstado()[j][i], keySchedule.get(round).getMatrizEstado()[j][i]);
            }
        }

        return bloco;
    }
}
