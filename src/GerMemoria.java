
public class GerMemoria {
	private int mi;
	private int mf;

	// 1. O gerente deve receber um bloco que representa a memória disponível do
	// endereço mi até o endereço mf.
	private Bloco bloco_inicial;

	private GerBlocos ger;

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

		int[] array_bloco = bloco_inicial.getBloco();
		System.out.println(array_bloco.length);

		String[] operacoes = ger.getOperacoes();
		int[] valores = ger.getValores();

		for (int i = 0; i < operacoes.length; i++) {
			System.out.println(operacoes[i]);
			if (operacoes[i].contains("S")) {
				solicitacao(valores[i]);
			} else if (operacoes[i].contains("L")) {
				liberacao(valores[i]);
			}
		}
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
	public void solicitacao(int valor) {
		System.out.println("SOLICITAÇÃO: " + valor);
	}

	public void liberacao(int valor) {
		System.out.println("LIBERAÇÃO: " + valor);
	}

}
