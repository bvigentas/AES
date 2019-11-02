import java.io.*;

public class FileUtil {

    public static byte[] readFile(String filePath) {

        try{
            InputStream inputStream = new FileInputStream(filePath);

            long fileSize = new File(filePath).length();

            byte[] fileBytes = new byte[(int) fileSize];

            inputStream.read(fileBytes);

            return fileBytes;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void writeFile(String filePath, byte[] content) {

        try {
            OutputStream outputStream = new FileOutputStream(filePath);

            outputStream.write(content);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
