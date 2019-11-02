import lombok.Getter;
import lombok.Setter;


public class RoundKey {

    @Getter
    @Setter
    private byte[][] matrizEstado = new byte[4][4];

    @Setter
    private byte[][] lastRoundKey;

    private byte[] newWord;

    private byte[] roundConstant;

    public void generateRoundKey(int round) {

        generateFirstWord(round);
        matrizEstado[0] = newWord;
        generateOtherWords();

    }

    public void generateFirstWord(int round) {

        makeCopy();
        rotWord();
        subWord();
        generateRoundConstant(round);
        makeXorRoundConstant();
        makeXorFirstWordLastRoundKey();

    }

    public void generateOtherWords() {

        byte[] newWord = new byte[4];

        for (int i = 1; i < 4; i++) {

            for (int j = 0; j < 4; j++) {
                newWord[j] = (byte) (lastRoundKey[i][j] ^ matrizEstado[i -1][j]);
            }

            matrizEstado[i] = newWord;

        }

    }

    private void makeCopy() {
        newWord = lastRoundKey[3];

    }

    private void rotWord() {

        byte aux = newWord[0];

        for (int i = 0; i < newWord.length; i++) {
            if (i != newWord.length-1)
                newWord[i] = newWord[i+1];
            else
                newWord[i] = aux;
        }

    }

    private void subWord() {

        for (int i = 0; i < newWord.length; i++) {

            byte b = newWord[i];

            try {
                byte sboxEquivalent = (byte)SBox.getSboxEquivalent((b & 0xf0) >> 4, (b & 0x0f));
                newWord[i] = sboxEquivalent;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

    }

    private void generateRoundConstant(int round) {

        this.roundConstant = RoundConstant.generate(round);

    }

    private void makeXorRoundConstant() {

        for (int i = 0; i < newWord.length; i++) {
            newWord[i] = (byte) (newWord[i] ^ roundConstant[i]);
        }

    }

    private void makeXorFirstWordLastRoundKey() {
        for (int i = 0; i < newWord.length; i++) {
            newWord[i] = (byte) (newWord[i] ^ lastRoundKey[0][i]);
        }
    }
}
