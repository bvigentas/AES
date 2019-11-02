import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.binary.Hex;

@Setter
@Getter
public class AES {

    private byte[] key;

    private byte[][] matrizEstado = new byte[4][4];

    private byte[][] keySchedule = new byte[4][44];

    private byte[][] lastRoundKey;

    public AES(byte[] key) {
        this.key = key;
    }

    public void generateMatriz() {
        //char[] keyHex = (Hex.encodeHex(key));
        int keyIndex = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                matrizEstado[i][j] = key[keyIndex];
                keyIndex++;
            }
        }
    }
}
