import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
/*
 * Nome: Larissa Fiorini Martins
 * Data: 22/10/2018
 * 
 * Gerencia de memoria por particoes variaveis. Esse programa realiza uma solucao de software para 
 * gerenciar solicitacoes de alocacao e liberacao de processos na memoria. Por utilizar particoes variaveis,
 * pode acontecer fragmentacao externa no sistema. Quando ocorre fragmentacao, o sistema realiza a compactacao
 * do bloco de memoria.
 * 
/*
 * Classe GerMemoria: Gerencia solicitações de alocacoes e liberacoes de memoria. Quando acontece fragmentacao
 * externa, realiza compactacao do bloco de memoria.
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
		System.out.println("***GERENCIADOR DE MEMORIA***\n");

		// atribui identificador para cada bloco
		int id_cont = 1;

		// add bloco com memoria disponivel do endereco mi ate mf
		this.tabela.add(bloco_inicial);

		// recebe solicitacoes/liberacoes e seus respectivos valores do arquivo
		String[] operacoes = ger.getOperacoes();
		Integer[] valores = ger.getValores();

		// Gerencia todas operacoes solicitadas
		for (int i = 0; i < operacoes.length; i++) {

			// verifica se foi realizada solicitacao de alocacao de memoria
			if (operacoes[i].contains("S")) {

				Bloco atual = new Bloco(this.bloco_inicial.getI(), this.bloco_inicial.getI() + (valores[i]), valores[i],
						id_cont);

				// realiza alocacao
				int[] s = alocacao(valores[i], atual);

				// verifica se alocacao conseguiu ser realizada
				if (s != null) {

					System.out.println("Solicitação de alocacao atendida! ALOCADO: " + s[0] + "-" + s[1]);
					// printTabela();

				} else {

					System.out.println("Deve entrar na fila e aguardar liberação...");

					if (testaMemoriaInsuficiente(valores[i]))
						System.out.println("\n--Nao tem memoria suficiente!--\n");
					else
						fragmentacao();

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

				// verifica se liberacao conseguiu ser realizada
				if (l != null) {
					System.out.println("Solicitação de liberacao atendida! LIBERADO: " + l[0] + "-" + l[1]);
					juntaLivres();

					// Verifica se solicitacao pode ser atendida no momento que a liberacao ocorreu
					executaFilaEspera();
				}
			}
		}
		Collections.sort(this.tabela, (p1, p2) -> p1.getI() - p2.getI());
		System.out.println("\nTabela final: ");
		juntaLivres();
		printTabela();
		System.out.println("\nMemoria final: ");
		System.out.println(bloco_inicial.toString());
	}

	public int[] alocacao(int valor, Bloco atual) {
		System.out.println("ALOCACAO: " + valor);

		int[] array_bloco = bloco_inicial.getBloco();

		boolean aloca = true;

		for (int i = this.mi; i < array_bloco.length; i++) {
			try {
				// procura se ha espaco livre suficiente para alocar para o processo
				if (array_bloco[i] == 0) {
					for (int j = i; j < i + valor; j++) {
						if (array_bloco[j] == 0) {
							continue;
						} else {
							// nao conseguiu encontrar espaco livre suficiente
							aloca = false;
							break;
						}
					}
					// se conseguiu realizar solicitacao, atualiza valores da posicao alocada
					if (aloca == true) {

						// atualiza alocacoes na memoria
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

						if ((this.tabela.get(0).getF() - this.tabela.get(0).getI()) < 0)
							this.tabela.remove(0);
						else
							this.tabela.get(0).setTamanho(this.tabela.get(0).getF() - this.tabela.get(0).getI());

						juntaLivres();
						// atualiza bloco atual
						this.tabela.add(atual);
						bloco_inicial.setBloco(array_bloco);

						// retorna identificador para area de memoria alocada
						return id_area;
					}
				}
			} catch (Exception e) {
				return null;
			}
			aloca = true;
		}
		return null;
	}

	public int[] liberacao(int valor) {
		System.out.println("LIBERACAO: " + valor);

		boolean libera = false;

		int[] array_bloco = bloco_inicial.getBloco();

		Bloco atual = this.getBlocoById(valor);

		if (atual == null)
			return null;

		for (int i = this.mi; i < array_bloco.length; i++) {
			// realiza liberacao do bloco solicitado
			if (array_bloco[i] == valor) {
				array_bloco[i] = 0;
				libera = true;
			}
		}
		if (libera == true) {
			int[] id_area = new int[2];
			id_area[0] = atual.getI();
			id_area[1] = atual.getF();

			// seta como bloco livre
			atual.setId(0);

			juntaLivres();

			bloco_inicial.setBloco(array_bloco);
			return id_area;
		} else {
			System.out.println("Nao encontrou o ID do bloco para liberar.");
			return null;
		}
	}

	public void fragmentacao() {
		/*
		 * Algoritmo de compactaçao: move todos os processos para um extremo da memória.
		 * Todos buracos se movem para o inicio do array, formando um grande buraco de
		 * memória disponivel.
		 * 
		 */

		System.out.println("\n------------FRAGMENTACAO EXTERNA------------\n");
		int[] array_bloco = bloco_inicial.getBloco();

		printTabela();

		Arrays.sort(array_bloco);

		bloco_inicial.setBloco(array_bloco);
		System.out.println(bloco_inicial.toString());

		array_bloco = this.bloco_inicial.getBloco();

		// agrupa campos livres na tabela
		for (int i = 1; i < this.tabela.size(); i++) {
			if (this.tabela.get(i).getId() == 0)
				this.tabela.remove(i);
		}

		// atualiza tabela com valores de blocos na memória
		for (Bloco b : this.tabela) {
			for (int i = this.mi; i < this.bloco_inicial.getBloco().length; i++) {
				try {
					if (array_bloco[i] == b.getId()) {
						b.setI(i);
						while (array_bloco[i] == b.getId()) {
							i++;
						}
						b.setF(i);
						b.setTamanho(b.getF() - b.getI());
						break;
					}
				} catch (Exception e) {
					b.setF(this.bloco_inicial.getBloco().length);
				}
			}
		}
		Set<Bloco> hs = new LinkedHashSet<>();
		hs.addAll(this.tabela);
		this.tabela.clear();
		this.tabela.addAll(hs);
		Collections.sort(this.tabela, (p1, p2) -> p1.getI() - p2.getI());
		printTabela();
	}

	// mostra para o usuario como estao alocados os blocos na memoria
	public void printTabela() {
		Set<Bloco> hs = new LinkedHashSet<>();
		hs.addAll(this.tabela);
		this.tabela.clear();
		this.tabela.addAll(hs);

		for (int i = 0; i < this.tabela.size(); i++) {
			for (int j = i + 1; j < this.tabela.size(); j++) {
				if (this.tabela.get(i).getI() == this.tabela.get(j).getI()) {
					this.tabela.remove(0);
				}
			}
		}

		System.out.println("\n**********************************************************************");
		System.out.println("TABELA ");
		for (Bloco bloco : this.tabela) {
			if (bloco.getId() == 0)
				System.out.println(
						"\n" + bloco.getI() + "-" + bloco.getF() + "    LIVRE (tamanho: " + bloco.getTamanho() + ") ");
			else
				System.out.println("\n" + bloco.getI() + "-" + bloco.getF() + "    Bloco " + bloco.getId()
						+ " (tamanho: " + bloco.getTamanho() + ") ");
		}
		System.out.println("**********************************************************************\n");
	}

	// busca bloco na tabela pelo seu identificador
	public Bloco getBlocoById(int id) {
		for (Bloco b : this.tabela) {
			if (b.getId() == id) {
				return b;
			}
		}
		return null;
	}

	// quando ocorre liberacao, procura se processos que estavam esperando podem
	// agora executar
	public void executaFilaEspera() {
		for (Bloco bloco : this.fila_espera) {
			int[] s = this.alocacao(bloco.getF() - bloco.getI(), bloco);

			if (s != null) {

				System.out.println("Solicitação de alocacao atendida! ALOCADO: " + s[0] + "-" + s[1]);
				// printTabela();

			} else {

				System.out.println("Deve entrar na fila e aguardar liberação...");

				fragmentacao();

				// System.out.println(bloco_inicial.toString());

			}
		}

	}

	// se dois blocos livres forem vizinhos, junta os dois e vira um só bloco
	public void juntaLivres() {
		for (int i = 0; i < this.tabela.size(); i++) {
			for (int j = i + 1; j < this.tabela.size(); j++) {
				if (this.tabela.get(i).getId() == 0 && this.tabela.get(j).getId() == 0) {
					if ((this.tabela.get(i).getF() == this.tabela.get(j).getI())) {
						this.tabela.get(i).setF(this.tabela.get(j).getF());
						this.tabela.get(i).setTamanho(this.tabela.get(i).getF() - this.tabela.get(i).getI());
						this.tabela.remove(j);
					}
				}

			}
		}
	}

	// verifica se a soma dos blocos livres consegue atender a solicitacao de
	// alocacao. Caso nao consiga
	// retorna true para memoria insuficiente
	public boolean testaMemoriaInsuficiente(Integer valor_solicitacao) {
		printTabela();
		int[] array_bloco = bloco_inicial.getBloco();
		int cont = 0;
		int ctam = 0;
		for (int j = this.mi; j < this.bloco_inicial.getBloco().length; j++) {
			try {
				if (array_bloco[j] == 0) {
					while (array_bloco[j] == 0) {
						j++;
						ctam++;
					}
					cont++;
				}
			} catch (Exception e) {
				cont++;
			}
		}
		if (ctam < valor_solicitacao)
			return true;
		else
			return false;

	}
}
