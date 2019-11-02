import java.util.List;

public class Core {

    public void start(int[] key) {
        AES aes = new AES(key);
        aes.generateMatriz();


        RoundKey roundKey = new RoundKey();
        roundKey.setMatrizEstado(aes.getMatrizEstado());

        KeySchedule keySchedule = new KeySchedule();
        keySchedule.add(roundKey);

        for (int i = 1; i < 11; i++) {

            int[][] matrizEstadoPassada = roundKey.getMatrizEstado();
            roundKey = new RoundKey();
            roundKey.setLastRoundKey(matrizEstadoPassada);
            roundKey.generateRoundKey(i);

            keySchedule.add(roundKey);
        }

        int[] textoSimples = {0x44, 0x45, 0x53, 0x45, 0x4e, 0x56, 0x4f, 0x4c, 0x56, 0x49, 0x4d, 0x45, 0x4e, 0x54, 0x4f, 0x21};

        CifraBloco cifraBloco = new CifraBloco(textoSimples, keySchedule);
        List<Block> blocosCifrados = cifraBloco.cifrar();
    }

}
