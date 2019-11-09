public class KeyUtil {

    /**
     * Método responsável por criar um array de ints com a chave em String disponibilizada pelo usuário.
     *
     * @param keyString - Chave em String
     * @return key - Chave em int[]
     */
    public static int[] getKeyFromString(String keyString) {

        int[] key = new int[16];

        String[] splitedKey = keyString.split(",");

        for (int i = 0; i < splitedKey.length; i++) {
            key[i] = new Integer(splitedKey[i]).byteValue();
        }

        return key;
    }

}
