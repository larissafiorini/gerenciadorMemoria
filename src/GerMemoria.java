import java.util.ArrayList;
import java.util.Arrays;

/*
 * Gerencia solicitações e liberações. Quando falta espaço, realiza fragmentação.
 * Uso do algoritmo First Fit para alocacoes.
 * 
 * 
 * FALTA:: indicar para cada bloco em que endereço está sendo armazenando na memória.
 * Talvez criar tabela para manter indice de onde cada processo se encontra na memória.
 * */

public class GerMemoria {
	private int mi;
	private int mf;

	// 1. O gerente deve receber um bloco que representa a memória disponível do
	// endereço mi até o endereço mf.
	private Bloco bloco_inicial;

	private GerBlocos ger;

	private ArrayList<Bloco> fila_espera = new ArrayList<>();

	private ArrayList<Bloco> tabela = new ArrayList<>();

	public GerMemoria(int mi, int mf, GerBlocos g, Bloco b) {
		this.mi = mi;
		this.mf = mf;
		this.ger = g;
		this.bloco_inicial = b;
	}

	/*
	 * 6. Não é preciso controlar tempo, as alocações e liberações são realizadas na
	 * ordem que chegarem e puderem ser atendidas. Se uma alocação não puder ser
	 * atendida, deve ser verificado se ela pode ser atendida no momento que uma
	 * liberação acontecer.
	 * 
	 * 
	 * 
	 */

	public void gerenciador() {
		System.out.println("***GERENCIADOR DE MEMÓRIA***");

		int cont = 1; // DEVE SER 1
		/*
		 * P/ BLOCO 7 FUNCIONAR, TEM Q FRAGMENTAR
		 */
		int[] array_bloco = bloco_inicial.getBloco();
		System.out.println(array_bloco.length);

		String[] operacoes = ger.getOperacoes();
		int[] valores = ger.getValores();

		for (int i = 0; i < operacoes.length; i++) {
			System.out.println(operacoes[i]);
			if (operacoes[i].contains("S")) {
				System.out.println("cont : " + cont);
				Bloco atual = new Bloco(0, valores[i] - 1, cont);
				boolean s = solicitacao(valores[i], atual);
				if (s == true) {
					System.out.println("Solicitação atendida!");
					printTabela();
				} else {
					System.out.println("Deve entrar na fila e aguardar liberação...");
					fragmentar();
					System.out.println("Após fragmentaÇao:::::::::::::::::");
					// CRIAR FILA DE ESPERA AQUI P/ PROCESSOS AGUARDANDO LIBERACAO
					this.fila_espera.add(atual);

					boolean s2 = solicitacao(valores[i], atual);
					System.out.println("Conseguiu alocar agora? " + s2);
				}
				cont++;
			} else if (operacoes[i].contains("L")) {
				boolean l = liberacao(valores[i]);
				if (l == true) {
					System.out.println("Liberação realizada!!");

					// PESQUISA NA FILA DE ESPERA SE AGORA PROCESSO CABE, SENÃO FORMATA MSM
				} else
					System.out.println("Não conseguiu liberar.");
			}
			printTabela();
		}
		System.out.println(bloco_inicial.toString());

		//
		// for (String op : ger.getOperacoes()) {
		// System.out.println(op);
		//
		//
		// }
	}

	/*
	 * 3. A solicitação de memória deve retornar um identificador para a área de
	 * memória que foi alocada, enquanto o comando de liberação de memória envia o
	 * identificador recebido durante a alocação.
	 * 
	 */
	public boolean solicitacao(int valor, Bloco atual) {

		System.out.println("SOLICITAÇÃO: " + valor);

		int[] array_bloco = bloco_inicial.getBloco();

		boolean flag = false;

		for (int i = 0; i < bloco_inicial.getBloco().length; i++) {
			// System.out.println("I ATUAL : " + i);
			try {

				if (array_bloco[i] == 0) {
					for (int j = i; j < i + valor; j++) {
						if (array_bloco[j] == 0) {
							// System.out.println("i: " + i + " j: " + j + " array pos: " + array_bloco[j]);
							continue;
						} else {
							flag = true;
							break;
						}

					}
					if (flag == false) {
						System.out.println("atual.getId(): " + atual.getId());
						atual.setI(i);
						atual.setF(i + valor);
						for (int j = i; j < i + valor; j++) {
							array_bloco[j] = atual.getId();
						}
						this.tabela.add(atual);
						bloco_inicial.setBloco(array_bloco);

						// System.out.println(bloco_inicial.toString());
						return true;

					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Não existe posiçao! + " + e.getMessage());
			}
			flag = false;
		}
		return false;
	}

	public boolean liberacao(int valor) {
		System.out.println("************LIBERAÇÃO************: " + valor);

		boolean flag = false;

		int[] array_bloco = bloco_inicial.getBloco();
		for (int i = 0; i < array_bloco.length; i++) {
			if (array_bloco[i] == valor) {
				// System.out.println(array_bloco[i]);
				array_bloco[i] = 0;
				flag = true;
			}
		}

		if (flag == true) {
			System.out.println("Conseguiu liberar!");
			bloco_inicial.setBloco(array_bloco);
			// System.out.println(bloco_inicial.toString());
			return true;
		} else
			return false;
	}

	public void fragmentar() {
		System.out.println("***FRAGMENTAÇÃO***");
		/*
		 * Algoritmo de compactaçao: move todos os processos para um extremo da memória.
		 * Todos buracos se movem para o inicio do array, formando um grande buraco de
		 * memória disponível.
		 * 
		 * Precisa atualizar posição dos blocos na memória para cada um!
		 */
		int[] array_bloco = bloco_inicial.getBloco();
		for (int i = 0; i < array_bloco.length; i++) {
			Arrays.sort(array_bloco);
		}
		bloco_inicial.setBloco(array_bloco);
		System.out.println(bloco_inicial.toString());
	}

	public void printTabela() {
		for (Bloco bloco : this.tabela) {
			System.out.println("Print bloco> ");
			System.out.println(bloco.getId() + " inicio: " + bloco.getI() + " fim: " + bloco.getF());
		}
	}
}
