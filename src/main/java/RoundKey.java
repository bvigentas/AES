import lombok.Getter;
import lombok.Setter;


public class RoundKey {

    @Getter
    @Setter
    private int[][] matrizEstado = new int[4][4];

    @Setter
    private int[][] lastRoundKey;

    private int[] newWord = new int[4];

    private int[] roundConstant;

    public void generateRoundKey(int round) {

        generateFirstWord(round);
        for (int i = 0; i < 4; i++) {
            matrizEstado[i][0] = newWord[i];
        }
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

        int[] newWord = new int[4];

        for (int i = 1; i < 4; i++) {

            for (int j = 0; j < 4; j++) {
                newWord[j] = (lastRoundKey[j][i] ^ matrizEstado[j][i -1]);
            }
            for (int j = 0; j < 4; j++) {
                matrizEstado[j][i] = newWord[j];
            }

        }

    }

    private void makeCopy() {

        for (int i = 0; i < 4; i++) {
            newWord[i] = lastRoundKey[i][3];
        }

    }

    private void rotWord() {

        int aux = newWord[0];

        for (int i = 0; i < newWord.length; i++) {
            if (i != newWord.length-1)
                newWord[i] = newWord[i+1];
            else
                newWord[i] = aux;
        }

    }

    private void subWord() {

        for (int i = 0; i < newWord.length; i++) {

            int b = newWord[i];

            try {
                int sboxEquivalent = SBox.getSboxEquivalent((b & 0xf0) >> 4, (b & 0x0f));
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
            newWord[i] = (newWord[i] ^ roundConstant[i]);
        }

    }

    private void makeXorFirstWordLastRoundKey() {
        for (int i = 0; i < newWord.length; i++) {
            newWord[i] = (newWord[i] ^ lastRoundKey[i][0]);
        }
    }
}
