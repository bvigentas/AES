public class RoundConstant {

    private static final int[] RC_TABLE = {0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1B, 0x36};

    public static int[] generate(int index) {

        int[] roundConstant = {RC_TABLE[index-1], 0, 0 ,0};
        return roundConstant;
    }
}
