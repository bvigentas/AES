import java.util.List;

public class Main {

    public static void main(String[] args) {

        byte[] key = {0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48, 0x49, 0x4a, 0x4b, 0x4c, 0x4d, 0x4e, 0x4f, 0x50};

        AES aes = new AES(key);
        aes.generateMatriz();


        RoundKey roundKey = new RoundKey();
        roundKey.setMatrizEstado(aes.getMatrizEstado());

        KeySchedule keySchedule = new KeySchedule();
        keySchedule.add(roundKey);

        for (int i = 1; i < 11; i++) {

            byte[][] matrizEstadoPassada = roundKey.getMatrizEstado();
            roundKey = new RoundKey();
            roundKey.setLastRoundKey(matrizEstadoPassada);
            roundKey.generateRoundKey(i);

            keySchedule.add(roundKey);
        }

        CifraBloco cifraBloco = new CifraBloco("texto simples".getBytes(), keySchedule);
        List<Block> blocosCifrados = cifraBloco.cifrar();
    }

}
