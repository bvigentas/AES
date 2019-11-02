import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.binary.Hex;

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

    public void generateMatriz() {
        //char[] keyHex = (Hex.encodeHex(key));
        int keyIndex = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                matrizEstado[j][i] = key[keyIndex];
                keyIndex++;
            }
        }

        System.out.println("teste");
    }
}
