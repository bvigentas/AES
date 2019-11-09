import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Scanner;

public class CmdInterface {

    /**
     *
     * Dados para teste:
     *
     * Arquivo entrada: C:\temp\teste.txt   --> Deve conter o conteúdo 'DESENVOLVIMENTO!'
     *
     * Arquivo saída: C:\temp\result.txt
     *
     * Chave: 65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80
     */
    public void run() {

        Scanner scanner = new Scanner(System.in);
//
        System.out.println("Informe o arquivo a ser criptografado no padrão C:\\caminho\\para\\arquivo:");
        String filePath = scanner.nextLine();
        System.out.println("Informe o local onde o nome arquivo será gerado no padrão C:\\caminho\\para\\arquivo:");
        String newFilePath = scanner.nextLine();
        System.out.println("Informe a chave de criptografia:");
        String keyString = scanner.nextLine();



        try {

            InputStream inputStream = new FileInputStream(filePath);
            byte[] bytes = IOUtils.toByteArray(inputStream);

            int[] key = KeyUtil.getKeyFromString(keyString);
            // int key[] ={0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48, 0x49, 0x4a, 0x4b, 0x4c, 0x4d, 0x4e, 0x4f, 0x50};

            AES aes = new AES(key);
            byte[] textoCifrado = aes.start(toIntArray(bytes));

            File newFile = new File(newFilePath);
            newFile.createNewFile();
            FileUtils.writeByteArrayToFile(newFile, textoCifrado);

            System.out.println("Arquivo cifrado criado com sucesso em: " + newFilePath);

        } catch (FileNotFoundException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    private int[] toIntArray(byte[] byteArray) {

        int[] intArray = new int[byteArray.length];

        for (int i = 0; i < byteArray.length; i++) {

            Byte bObj = new Byte(byteArray[i]);

            intArray[i] = bObj.intValue();

        }

        return intArray;
    }

}
