import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GerBlocos {

	// array operacoes
	private String operacoes[];
	// array Valores
	private int valores[];

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

	public int[] getValores() {
		return this.valores;
	}

	public GerBlocos() {

	}

	/* Le arquivo com informações dos blocos */
	public void readFile(String file_name) throws IOException {

		int mi;
		int mf;

		int i = 0;

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

			// array de operacoes
			this.operacoes = new String[9];

			// array de valores
			this.valores = new int[9];

			// while (br.readLine() != null) {
			for (int j = 0; j < 9; j++) {
				// le a linha
				String linha = br.readLine();

				System.out.println(linha);

				String[] linhas = linha.split(" ");
				//
				this.operacoes[j] = linhas[0];
				this.valores[j] = Integer.parseInt(linhas[1]);
				//
				// i++;
			}

		}

	}
}
