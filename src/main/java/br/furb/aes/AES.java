package br.furb.aes;

import br.furb.aes.bloco.CifraBloco;
import br.furb.aes.key.KeySchedule;
import br.furb.aes.key.RoundKey;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AES {

    private int[] key;

    private int[][] matrizEstado = new int[4][4];

    private int[][] keySchedule = new int[4][44];

    private int[][] lastRoundKey;

    public AES(int[] key) {
        this.key = key;
    }

    private void generateMatriz() {
        //char[] keyHex = (Hex.encodeHex(key));
        int keyIndex = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                matrizEstado[j][i] = key[keyIndex];
                keyIndex++;
            }
        }
    }

    public byte[] start(int[] textoSimples) {

        generateMatriz();

        RoundKey roundKey = new RoundKey();
        roundKey.setMatrizEstado(getMatrizEstado());

        KeySchedule keySchedule = new KeySchedule();
        keySchedule.add(roundKey);

        for (int i = 1; i < 11; i++) {

            int[][] matrizEstadoPassada = roundKey.getMatrizEstado();
            roundKey = new RoundKey();
            roundKey.setLastRoundKey(matrizEstadoPassada);
            roundKey.generateRoundKey(i);

            keySchedule.add(roundKey);
        }

        //int[] textoSimples = {0x44, 0x45, 0x53, 0x45, 0x4e, 0x56, 0x4f, 0x4c, 0x56, 0x49, 0x4d, 0x45, 0x4e, 0x54, 0x4f, 0x21};

        CifraBloco cifraBloco = new CifraBloco(textoSimples, keySchedule);

        return cifraBloco.cifrar();
    }
}
