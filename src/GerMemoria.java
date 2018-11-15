import java.util.Arrays;

/*
 * Gerencia solicita��es e libera��es. Quando falta espa�o, realiza fragmenta��o.
 * Uso do algoritmo First Fit para alocacoes.
 * 
 * 
 * FALTA:: indicar para cada bloco em que endere�o est� sendo armazenando na mem�ria.
 * */

public class GerMemoria {
	private int mi;
	private int mf;

	// 1. O gerente deve receber um bloco que representa a mem�ria dispon�vel do
	// endere�o mi at� o endere�o mf.
	private Bloco bloco_inicial;

	private GerBlocos ger;

	public GerMemoria(int mi, int mf, GerBlocos g, Bloco b) {
		this.mi = mi;
		this.mf = mf;
		this.ger = g;
		this.bloco_inicial = b;
	}

	/*
	 * 6. N�o � preciso controlar tempo, as aloca��es e libera��es s�o realizadas na
	 * ordem que chegarem e puderem ser atendidas. Se uma aloca��o n�o puder ser
	 * atendida, deve ser verificado se ela pode ser atendida no momento que uma
	 * libera��o acontecer.
	 * 
	 * 
	 * 
	 */

	public void gerenciador() {
		System.out.println("***GERENCIADOR DE MEM�RIA***");

		int cont = 0; // DEVE SER 1
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
					System.out.println("Solicita��o atendida!");
				} else {
					System.out.println("Deve entrar na fila e aguardar libera��o...");
					fragmentar();
				}
				if (cont == 5)
					fragmentar();
				cont++;
			} else if (operacoes[i].contains("L")) {
				boolean l = liberacao(valores[i]);
				if (l == true) {
					System.out.println("Libera��o realizada!!");
				} else
					System.out.println("N�o conseguiu liberar.");
			}

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
	 * 3. A solicita��o de mem�ria deve retornar um identificador para a �rea de
	 * mem�ria que foi alocada, enquanto o comando de libera��o de mem�ria envia o
	 * identificador recebido durante a aloca��o.
	 * 
	 */
	public boolean solicitacao(int valor, Bloco atual) {

		System.out.println("SOLICITA��O: " + valor);

		int[] array_bloco = bloco_inicial.getBloco();

		boolean flag = false;

		for (int i = 0; i < bloco_inicial.getBloco().length; i++) {
			// System.out.println("I ATUAL : " + i);
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
					for (int j = i; j < i + valor; j++) {
						array_bloco[j] = atual.getId();
					}
					bloco_inicial.setBloco(array_bloco);
					// System.out.println(bloco_inicial.toString());
					return true;

				}
			}
			flag = false;
		}
		return false;
	}

	public boolean liberacao(int valor) {
		System.out.println("************LIBERA��O************: " + valor);

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
		System.out.println("***FRAGMENTA��O***");
		/*
		 * Algoritmo de compacta�ao: move todos os processos para um extremo da mem�ria.
		 * Todos buracos se movem para o inicio do array, formando um grande buraco de
		 * mem�ria dispon�vel.
		 */
		int[] array_bloco = bloco_inicial.getBloco();
		for (int i = 0; i < array_bloco.length; i++) {
			Arrays.sort(array_bloco);
		}
		bloco_inicial.setBloco(array_bloco);
		System.out.println(bloco_inicial.toString());
	}

}
