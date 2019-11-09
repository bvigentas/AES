import br.furb.aes.cmd.CmdInterface;

public class Main {

    /**
     * Dados para teste:
     *
     * Arquivo entrada: C:\temp\teste.txt   --> Deve conter o conteúdo 'DESENVOLVIMENTO!'
     * Arquivo saída: C:\temp\result.txt
     * Chave: 65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80
     */
    public static void main(String[] args) {

        CmdInterface cmdInterface = new CmdInterface();
        cmdInterface.run();
    }



}
