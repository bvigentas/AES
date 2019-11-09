public class KeyUtil {

    public static int[] getKeyFromString(String keyString) {

        int[] key = new int[16];

        String[] splitedKey = keyString.split(",");

        for (int i = 0; i < splitedKey.length; i++) {
            key[i] = new Integer(splitedKey[i]).byteValue();
        }



        return key;
    }

}
