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

		this.tabela.add(bloco_inicial);

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
						// coloca posicoes da memoria
						atual.setI(i);
						atual.setF(i + valor);

						for (int j = i; j < i + valor; j++) {
							array_bloco[j] = atual.getId();
						}

						// atualiza bloco inicial
						this.tabela.get(0).setI((i + valor) + 1);

						// atualiza bloco atual
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

		Bloco atual = this.getBlocoById(valor);
		if (atual == null)
			return false;

		for (int i = 0; i < array_bloco.length; i++) {

			if (array_bloco[i] == valor) {
				// System.out.println(array_bloco[i]);

				array_bloco[i] = 0;
				flag = true;
			}
		}

		this.tabela.add(bloco_inicial);

		if (flag == true) {
			System.out.println("Conseguiu liberar!");

			atual.setI(0);
			atual.setF(0);

			this.tabela.remove(atual);

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
		Arrays.sort(array_bloco);

		// for (int i = 0; i < array_bloco.length; i++) {
		// Arrays.sort(array_bloco);
		// }

		ArrayList<Integer> ids = new ArrayList<Integer>();
		boolean check = false;
		// Get id de cada bloco existente
		for (Bloco bb : this.tabela) {
			System.out.println("bloco id: " + bb.getId());
			for (Integer inte : ids) {
				if (inte == bb.getId()) {

					check = true;

				}
			}
			if (check == false)
				ids.add(bb.getId());
		}
		// remove duplicadas
		for (int j = 1; j < this.tabela.size(); j++) {
			if (this.tabela.get(j).getId() == 0) {
				this.tabela.remove(this.tabela.get(j));
			}
		}

		// atualiza tabela com valores após fragmentacao
		for (int i = 0; i < array_bloco.length; i++) {

			if (array_bloco[i] == 0) {

				this.bloco_inicial.setI(i);

				while (array_bloco[i] == 0) {
					i++;
				}
				this.bloco_inicial.setF(i);

			} else {

				for (int n : ids) {

					if (array_bloco[i] == n) {

						Bloco a = this.getBlocoById(n);
						a.setI(i);

						while (array_bloco[i] == n) {
							i++;
						}
						a.setF(i);

					}
				}
			}

		}
		System.out.println("bloco de livres: i:  " + this.bloco_inicial.getI() + " f: " + this.bloco_inicial.getF());
		System.out.println("DEPOIS DA FRAG:::::::");
		printTabela();

		bloco_inicial.setBloco(array_bloco);
		System.out.println(bloco_inicial.toString());
	}

	public void printTabela() {
		for (Bloco bloco : this.tabela) {
			System.out.println("Print bloco: ");
			System.out.println(bloco.getId() + " inicio: " + bloco.getI() + " fim: " + bloco.getF());
		}
	}

	public Bloco getBlocoById(int id) {
		for (Bloco b : this.tabela) {
			if (b.getId() == id) {
				return b;
			}
		}
		return null;
	}
}
