import java.util.ArrayList;
import java.util.Arrays;

/*
 * Gerencia solicitações de alocacoes e liberacoes. Quando falta espaco, realiza fragmentacao.
 * Uso do algoritmo First Fit para alocacoes.
 * 
 * 
 * */

public class GerMemoria {
	private int mi;
	private int mf;

	// 1. O gerente deve receber um bloco que representa a memória disponível do
	// endereço mi até o endereço mf.
	private Bloco bloco_inicial;

	private GerBlocos ger;

	private ArrayList<Bloco> fila_espera = new ArrayList<>();

	// Tabela que indica que partes da memoria estao livres e quais estao ocupadas
	private ArrayList<Bloco> tabela = new ArrayList<>();

	public GerMemoria(int mi, int mf, GerBlocos g, Bloco b) {
		this.mi = mi;
		this.mf = mf;
		this.ger = g;
		this.bloco_inicial = b;
	}

	public void gerenciador() {
		System.out.println("***GERENCIADOR DE MEMORIA***");

		// atribui identificador para cada bloco
		int id_cont = 1;

		int[] array_bloco = bloco_inicial.getBloco();

		System.out.println(this.bloco_inicial.getI());
		System.out.println(this.bloco_inicial.getF());
		System.out.println(this.bloco_inicial.getTamanho());

		// add bloco com memoria disponivel do endereco mi ate mf
		this.tabela.add(bloco_inicial);
		// printTabela();

		// recebe solicitacoes/liberacoes e seus respectivos valores do arquivo
		String[] operacoes = ger.getOperacoes();
		Integer[] valores = ger.getValores();

		// Gerencia todas operacoes solicitadas
		for (int i = 0; i < operacoes.length; i++) {
			System.out.println(operacoes[i] + " " + valores[i]);

			// verifica se foi realizada solicitacao de alocacao de memoria
			if (operacoes[i].contains("S")) {

				Bloco atual = new Bloco(this.bloco_inicial.getI(), this.bloco_inicial.getI() + (valores[i] - 1),
						valores[i], id_cont);

				// realiza alocacao
				int[] s = solicitacao(valores[i], atual);

				// verifica se alocacao conseguiu ser realizada
				if (s != null) {

					System.out.println("ALOCADO: " + s[0] + " , " + s[1]);
					System.out.println("Solicitação atendida!");
					printTabela();

				} else {

					System.out.println("Deve entrar na fila e aguardar liberação...");

					printTabela();
					System.out.println(bloco_inicial.toString());
					fragmentar();
					printTabela();

					int[] s2 = solicitacao(valores[i], atual);

					System.out.println("ALOCADO: " + s2[0] + " , " + s2[1]);

					printTabela();
					System.out.println(bloco_inicial.toString());
					// add processo que nao pode ser executado na fila de espera, aguardando
					// liberacao
					this.fila_espera.add(atual);
				}
				id_cont++;

				// verifica se foi realizada solicitacao de liberacao de memoria
			} else if (operacoes[i].contains("L")) {

				// realiza liberacao
				int[] l = liberacao(valores[i]);

				System.out.println("LIBERADO: " + l[0] + " , " + l[1]);

				// verifica se liberacao conseguiu ser realizada
				if (l != null) {
					System.out.println("Liberação realizada!!");

					// Verifica se solicitacao pode ser atendida no momento que a liberacao ocorreu
					executaFilaEspera();

				} else
					System.out.println("Não conseguiu liberar.");
			}
		}
		System.out.println("IMPRIME BLOCO: " + bloco_inicial.getF() + " LENGTH: " + bloco_inicial.getTamanho() + " "
				+ bloco_inicial.getBloco().length);
		System.out.println(bloco_inicial.toString());

	}

	public int[] solicitacao(int valor, Bloco atual) {

		System.out.println("SOLICITACAO de alocacao: " + valor);

		int[] array_bloco = bloco_inicial.getBloco();
		System.out.println("tamanho na solicitacao" +array_bloco.length);

		boolean flag = true;

		for (int i = this.bloco_inicial.getI(); i < array_bloco.length; i++) {
			try {
				// procura se ha espaco livre suficiente para alocar para o processo
				if (array_bloco[i] == 0) {
					for (int j = i; j < i + valor; j++) {
						if (array_bloco[j] == 0) {
							continue;
						} else {
							// nao conseguiu encontrar espaco livre suficiente
							flag = false;
							break;
						}

					}
					// se conseguiu realizar solicitacao, atualiza valores da posicao alocada
					if (flag == true) {
						System.out.println("atual.getId(): " + atual.getId());

						// coloca posicoes da memoria REVISAR
						atual.setI(i);
						atual.setF(i + valor);

						int[] id_area = new int[2];

						id_area[0] = i;

						id_area[1] = i + valor;

						for (int j = i; j < i + valor; j++) {
							array_bloco[j] = atual.getId();
						}

						// atualiza bloco inicial
						this.tabela.get(0).setI(i + valor);
						this.tabela.get(0).setTamanho(this.tabela.get(0).getF() - this.tabela.get(0).getI());

						// atualiza bloco atual
						this.tabela.add(atual);
						bloco_inicial.setBloco(array_bloco);

						// retorna identificador para area de memoria alocada
						return id_area;

					}
				}
			} catch (Exception e) {
				System.out.println("Não existe posiçao! + " + e.getMessage());
				return null;
			}
			flag = true;
		}
		return null;
	}

	public int[] liberacao(int valor) {
		System.out.println("************LIBERAÇÃO************: " + valor);

		boolean flag = false;

		int[] array_bloco = bloco_inicial.getBloco();

		Bloco atual = this.getBlocoById(valor);

		if (atual == null)
			return null;

		for (int i = atual.getI(); i < atual.getF(); i++) {
			// realiza liberacao do bloco solicitado
			if (array_bloco[i] == valor) {
				array_bloco[i] = 0;
				flag = true;
			}
		}

		System.out.println(bloco_inicial.toString());

		// this.tabela.add(bloco_inicial);
		// printTabela();

		if (flag == true) {
			System.out.println("Conseguiu liberar!");

			int[] id_area = new int[2];
			id_area[0] = atual.getI();
			id_area[1] = atual.getF();

			// this.tabela.remove(atual);

			// seta como bloco livre
			atual.setId(0);

			bloco_inicial.setBloco(array_bloco);
			// System.out.println(bloco_inicial.toString());
			printTabela();
			return id_area;
		} else
			return null;
	}

	public void fragmentar() {
		System.out.println("\n***FRAGMENTAÇÃO***\n");

		int[] array_bloco = bloco_inicial.getBloco();
		System.out.println(array_bloco.length);

		printTabela();
		System.out.println(bloco_inicial.toString());

		for (int i = 0; i < array_bloco.length; i++) {
			System.out.println("i: " + i + " " + array_bloco[i]);
		}

		System.out.println(this.bloco_inicial.getI());
		System.out.println(this.mi);

		Arrays.sort(array_bloco);

		ArrayList<Bloco> novatab = new ArrayList<Bloco>();
		
		Bloco teste =this.getBlocoById(0);
		this.tabela.remove(this.tabela.indexOf(teste));
		teste =this.getBlocoById(0);
		this.tabela.remove(this.tabela.indexOf(teste));
		teste =this.getBlocoById(0);
		this.tabela.remove(this.tabela.indexOf(teste));

		
		
		System.out.println("Tabela dentro de frag testing... " +array_bloco.length);
		for (Bloco bloco : this.tabela) {
			System.out.println(bloco.getId());

			try {
			for (int i = this.mi; i < array_bloco.length; i++) {
				if (array_bloco[i] == bloco.getId()) {
					int id = bloco.getId();
					bloco.setI(i);

					while (array_bloco[i] == id) {
						i++;
					}
					bloco.setF(i);

					System.out.println(
							"Bloco id: " + bloco.getId() + " inicio: " + bloco.getI() + " final: " + bloco.getF());
				}

			}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		/*
		 * Algoritmo de compactaçao: move todos os processos para um extremo da memória.
		 * Todos buracos se movem para o inicio do array, formando um grande buraco de
		 * memória disponível.
		 * 
		 * Precisa atualizar posição dos blocos na memória para cada um!
		 */

		// int[] array_bloco = bloco_inicial.getBloco();

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

		// System.out.println(this.bloco_inicial.getI());
		// System.out.println("this.mi " + this.mi);
		// System.out.println(this.mf);
		// System.out.println(this.bloco_inicial.getF());
		// System.out.println(array_bloco.length);

		int tamanhoteste = array_bloco.length + this.mi;
		int c = this.mi;

		// atualiza tabela com valores após fragmentacao
		for (int i = this.mi; i < array_bloco.length; i++) {

			if (array_bloco[i] == 0) {

				// this.bloco_inicial.setI(i);
				Bloco a = this.getBlocoById(0);
				// System.out.println(a.getId());
				a.setI(i);
				// System.out.println(this.bloco_inicial.getI());
				while (array_bloco[i] == 0) {
					i++;
				}
				// this.bloco_inicial.setF(i + 1);
				a.setF((i + c) + 1);
				// System.out.println(this.bloco_inicial.getF());
				a.setTamanho(this.bloco_inicial.getF() - this.bloco_inicial.getI());
				// this.bloco_inicial.setTamanho(this.bloco_inicial.getF() -
				// this.bloco_inicial.getI());
				// System.out.println(this.bloco_inicial.getTamanho());
			} else {

				for (int n : ids) {

					if (array_bloco[i] == n) {

						Bloco a = this.getBlocoById(n);
						a.setI(i + c);

						while (array_bloco[i] == n) {
							i++;
						}
						a.setF((i + c) + 1);

					}
				}
			}

		}
		// System.out.println("bloco de livres: i: " + this.bloco_inicial.getI() + " f:
		// " + this.bloco_inicial.getF());
		// System.out.println("DEPOIS DA FRAG:::::::");
		printTabela();

		bloco_inicial.setBloco(array_bloco);
		System.out.println(bloco_inicial.toString());
	}

	public void printTabela() {
		System.out.println("\n PRINT TABELA ");
		// System.out.println(" \nid: 0 => livres ");
		for (Bloco bloco : this.tabela) {
			System.out.println("\nPrint bloco: ");

			if (bloco.getId() == 0)
				System.out.println("id: LIVRE inicio: " + bloco.getI() + " fim: " + bloco.getF() + " (tamanho: "
						+ bloco.getTamanho() + ") ");
			else
				System.out.println("id: " + bloco.getId() + " inicio: " + bloco.getI() + " fim: " + bloco.getF()
						+ " (tamanho: " + bloco.getTamanho() + ") ");
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

	public void executaFilaEspera() {
		// depois que ocorre liberação, pesquisa se algum processo pode executar
		for (Bloco bloco : this.fila_espera) {

			this.solicitacao(bloco.getF() - bloco.getI(), bloco);

		}
	}

}
