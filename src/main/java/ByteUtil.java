public class ByteUtil {

    /**
     *
     * Método responsável por criar um array de int[] apartir de um byte[]
     *
     * @param byteArray - byte[]
     * @return intArray = int[]
     */
    public static int[] toIntArray(byte[] byteArray) {

        int[] intArray = new int[byteArray.length];

        for (int i = 0; i < byteArray.length; i++) {

            Byte bObj = new Byte(byteArray[i]);

            intArray[i] = bObj.intValue();

        }

        return intArray;
    }

    public static int xor(int left, int right) {
        return left ^right;
    }

    public static int getLeftByte(int b) {
        return (b & 0xf0) >> 4;
    }

    public static  int getRightByte(int b) {
        return (b & 0x0f);
    }

}
