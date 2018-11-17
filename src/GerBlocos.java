import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
			System.out.println(op);

			// Le mi
			mi = Integer.parseInt(br.readLine());
			this.setMi(mi);
			System.out.println(mi);
			// Le mf
			mf = Integer.parseInt(br.readLine());
			this.setMf(mf);
			System.out.println(mf);

			List<String> opList = new ArrayList<String>();
			List<Integer> valList = new ArrayList<Integer>();

			// le cada linha do arquivo com solicitacoes e liberacoes
			while ((linha = br.readLine()) != null) {

				System.out.println(linha);

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
