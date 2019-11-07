import lombok.AllArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class CifraBloco {

    @Setter
    private int[] textoSimples;

    @Setter
    private KeySchedule keySchedule;

    public List<Block> cifrar() {

        List<Block> blocos = quebraBloco();
        List<Block> blocoscifrados = new ArrayList<Block>();

        for (Block bloco: blocos) {

            bloco = etapa01(bloco);

            for (int i = 0; i < 10; i++) {
                bloco = etapa02(bloco);
                bloco = etapa03(bloco);
                bloco = etapa04(bloco);
                bloco = etapa05(bloco, i+1);
            }

            bloco = etapa02(bloco);
            bloco = etapa03(bloco);
            blocoscifrados.add(etapa05(bloco, 10));
        }

        return blocoscifrados;

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

            int[] aux = new int[i];

            for (int j = 0; j < 4; j++) {

                for (int k = 0; k < i; k++) {
                    aux[k] = bloco.getMatrizEstado()[k][i];
                }

                //TODO: Arrumar esta com erro :(
                bloco.getMatrizEstado()[0][i] = bloco.getMatrizEstado()[i][i];
                bloco.getMatrizEstado()[1][i] = aux.length > 2 ? aux[2] : bloco.getMatrizEstado()[1+ i][i];
                bloco.getMatrizEstado()[2][i] = aux.length > 1 ? aux[1] : bloco.getMatrizEstado()[2+ i][i];
                bloco.getMatrizEstado()[3][i] = aux[0];

            }
        }

        return bloco;
    }

    private Block etapa04(Block bloco) {
        int[][] newMatrix = new int[4][4];

        for (int i = 0; i < bloco.getMatrizEstado().length; i++) {
            for (int j = 0; j < MultiMatrix.MULTI_MATRIX.length; j++) {
                for (int x = 0; x < bloco.getMatrizEstado().length; x++) {
                    try {

                        int value = 0;

                        if (MultiMatrix.MULTI_MATRIX[x][j] == 1) {

                            value = bloco.getMatrizEstado()[i][x];

                        } else if (bloco.getMatrizEstado()[i][x] == 1) {

                            value = MultiMatrix.MULTI_MATRIX[x][j];

                        } else  if (MultiMatrix.MULTI_MATRIX[x][j] != 0 && bloco.getMatrizEstado()[i][x] != 0) {

                            value = (Galois.getGaloisEquivalent(((bloco.getMatrizEstado()[i][x] & 0xf0) >> 4), bloco.getMatrizEstado()[i][x] & 0x0f)
                                    + Galois.getGaloisEquivalent(((MultiMatrix.MULTI_MATRIX[x][j] & 0xf0) >> 4), MultiMatrix.MULTI_MATRIX[x][j] & 0x0f));
                        }

                        if (value > 0xff){
                            value = 0xff;
                        }

                        newMatrix[i][j] = value;

                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
        }


        for (int i = 0; i < newMatrix.length; i++) {
            for (int j = 0; j < newMatrix.length; j++) {
                try {
                    newMatrix[i][j] = ETable.getETableEquivalent(((newMatrix[i][j] & 0xf0) >> 4), newMatrix[i][j] & 0x0f);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
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
