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

            bloco = etapa01(bloco);

            for (int i = 1; i < 10; i++) {
                bloco = etapa02(bloco);
                bloco = etapa03(bloco);
                bloco = etapa04(bloco);
                bloco = etapa05(bloco, i);
            }

            bloco = etapa02(bloco);
            bloco = etapa03(bloco);
            blocoscifrados.add(etapa05(bloco, 10));
        }

        return uniteBlocks(blocoscifrados);

    }

    private byte[] uniteBlocks(List<Block> blocks) {

        byte[] arrayFinal = new byte[blocks.size() * 16];

        for (Block block: blocks) {
            int x = 0;
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

    private Block etapa01(Block bloco) {

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                bloco.getMatrizEstado()[j][i] = (bloco.getMatrizEstado()[j][i] ^ keySchedule.get(0).getMatrizEstado()[j][i]);
            }
        }

        return bloco;

    }

    private Block etapa02(Block bloco) {

        for (int i = 0; i < 4; i++) {

            for (int j = 0; j < bloco.getMatrizEstado().length; j++) {

                int b = bloco.getMatrizEstado()[j][i];

                try {
                    int sboxEquivalent = SBox.getSboxEquivalent((b & 0xf0) >> 4, (b & 0x0f));
                    bloco.getMatrizEstado()[j][i] = sboxEquivalent;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }

        }

        return bloco;

    }

    private Block etapa03(Block bloco) {

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

    private Block etapa04(Block bloco) {
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

                            value = (LTable.get(((bloco.getMatrizEstado()[x][i] & 0xf0) >> 4), bloco.getMatrizEstado()[x][i] & 0x0f)
                                    + LTable.get(((MultiMatrix.MULTI_MATRIX[j][x] & 0xf0) >> 4), MultiMatrix.MULTI_MATRIX[j][x] & 0x0f));

                            if (value > 0xff){
                                value = value - 0xff;
                            }

                            value = ETable.get(((value & 0xf0) >> 4), value & 0x0f);
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

    private Block etapa05(Block bloco, int round) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                bloco.getMatrizEstado()[j][i] = (bloco.getMatrizEstado()[j][i] ^ keySchedule.get(round).getMatrizEstado()[j][i]);
            }
        }

        return bloco;
    }
}
