import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/*
 * Nome: Larissa Fiorini Martins
 * Data: 22/10/2018
 * 
 * Gerencia de memoria por particoes variaveis. Esse programa realiza uma solucao de software para 
 * gerenciar solicitacoes de alocacao e liberacao de processos na memoria. Por utilizar particoes variaveis,
 * pode acontecer fragmentacao externa no sistema. Quando ocorre fragmentacao, o sistema realiza a compactacao
 * do bloco de memoria.
 * 
 * Classe GerBlocos: realiza a leitura do arquivo, armazena as solicitacoes que serao recebidas pelo gerenciador de memoria.
 * 
 * */

public class GerBlocos {

	// array operacoes
	private String operacoes[];
	// array Valores
	private Integer valores[];

	public String[] getOperacoes() {
		return this.operacoes;
	}

	private int mi;
	private int mf;

	public int getMi() {
		return mi;
	}

	public void setMi(int mi) {
		this.mi = mi;
	}

	public int getMf() {
		return mf;
	}

	public void setMf(int mf) {
		this.mf = mf;
	}

	public Integer[] getValores() {
		return this.valores;
	}

	public GerBlocos() {

	}

	/* Le arquivo com informações dos blocos */
	public void readFile(String file_name) throws IOException {

		int mi;
		int mf;
		String linha;

		try (BufferedReader br = new BufferedReader(new FileReader(file_name))) {

			// Le primeira linha com opcao fixo/aleatorio (nao vai ser usada)
			String op = br.readLine();

			// Le mi
			mi = Integer.parseInt(br.readLine());
			this.setMi(mi);

			// Le mf
			mf = Integer.parseInt(br.readLine());
			this.setMf(mf);

			List<String> opList = new ArrayList<String>();
			List<Integer> valList = new ArrayList<Integer>();

			// le cada linha do arquivo com solicitacoes e liberacoes
			while ((linha = br.readLine()) != null) {

				String[] linhas = linha.split(" ");

				opList.add(linhas[0]);
				valList.add(Integer.parseInt(linhas[1]));

			}
			// preenche array com operacoes de solicitacao/liberacao
			this.operacoes = new String[opList.size()];
			this.operacoes = opList.toArray(this.operacoes);

			this.valores = new Integer[valList.size()];
			this.valores = valList.toArray(this.valores);
		}
	}
}
