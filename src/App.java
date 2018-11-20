import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
/*
 * Nome: Larissa Fiorini Martins
 * 
 * Gerencia de memoria por particoes variaveis
 * 
 * */

public class App {
	public static void main(String[] args) throws IOException {

		// Scanner scan = new Scanner(System.in);
		// System.out.println("Insira o nome do arquivo: ");
		// String file_name = scan.nextLine();
		// scan.close();
		
		GerBlocos ger = new GerBlocos();
		// ger.readFile(file_name);
		ger.readFile("teste2.txt");

		// cria bloco inicial
		Bloco b = new Bloco(ger.getMi(), ger.getMf(),ger.getMf(),0);

		GerMemoria gerMem = new GerMemoria(ger.getMi(), ger.getMf(), ger, b);
		
		// realiza gerenciamento de memoria
		gerMem.gerenciador();

	}
}
