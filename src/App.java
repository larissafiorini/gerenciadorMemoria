import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
/*
 * Nome: Larissa Fiorini Martins
 * Data: 22/10/2018
 * 
 * Gerencia de memoria por particoes variaveis. Esse programa realiza uma solucao de software para 
 * gerenciar solicitacoes de alocacao e liberacao de processos na memoria. Por utilizar particoes variaveis,
 * pode acontecer fragmentacao externa no sistema. Quando ocorre fragmentacao, o sistema realiza a compactacao
 * do bloco de memoria.
 * 
 * */

public class App {
	public static void main(String[] args) throws IOException {

		 Scanner scan = new Scanner(System.in);
//		 System.out.println("Insira o nome do arquivo: ");
//		 String file_name = scan.nextLine();
//		 scan.close();
//		
		GerBlocos ger = new GerBlocos();
		ger.readFile("teste3.txt");

		// cria bloco inicial
		Bloco b = new Bloco(ger.getMi(), ger.getMf(),ger.getMf(),0);

		GerMemoria gerMem = new GerMemoria(ger.getMi(), ger.getMf(), ger, b);
		
		// realiza gerenciamento de memoria
		gerMem.gerenciador();

	}
}
