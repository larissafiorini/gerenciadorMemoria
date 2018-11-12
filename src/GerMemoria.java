
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
	 * 3. A solicita��o de mem�ria deve retornar um identificador para a �rea de
	 * mem�ria que foi alocada, enquanto o comando de libera��o de mem�ria envia o
	 * identificador recebido durante a aloca��o.
	 * 
	 */
	public void solicitacao(int valor) {
		System.out.println("SOLICITA��O: " + valor);
	}

	public void liberacao(int valor) {
		System.out.println("LIBERA��O: " + valor);
	}

}
