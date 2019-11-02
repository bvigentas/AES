public class KeyUtil {

    public static byte[] getKeyFromString(String keyString) {

        byte[] key = new byte[16];

        String[] splitedKey = keyString.split(",");

        for (int i = 0; i < splitedKey.length; i++) {
            key[i] = new Integer(splitedKey[i]).byteValue();
        }



        return key;
    }

}
