import java.util.Scanner;

public class CmdInterface {

    public void run() {

//        Scanner scanner = new Scanner(System.in);
//
//        System.out.println("Informe o arquivo a ser criptografado no padrão C:\\caminho\\para\\arquivo:");
//        String filePath = scanner.nextLine();
//        System.out.println("Informe o local onde o nome arquivo será gerado no padrão C:\\caminho\\para\\arquivo:");
//        String newFilePath = scanner.nextLine();
//        System.out.println("Informe a chave de criptografia:");
//        String keyString = scanner.nextLine();
//
//        byte[] fileBytes = FileUtil.readFile(filePath);

//        byte[] key = KeyUtil.getKeyFromString(keyString);
//        int key[] = {0x41, 0x45, 0x49, 0x4d, 0x42, 0x46, 0x4a, 0x4e, 0x43, 0x47, 0x4b, 0x4f, 0x44, 0x48, 0x4c, 0x50};
        int key[] ={0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48, 0x49, 0x4a, 0x4b, 0x4c, 0x4d, 0x4e, 0x4f, 0x50};

        Core core = new Core();
        core.start(key);

    }

}
