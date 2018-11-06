import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GerBlocos {

	public GerBlocos() {

	}

	/* Le arquivo com informações dos blocos */
	public void readFile(String file_name) throws IOException {

		int mi;
		int mf;

		int i = 0;
		String linha = "";

		try (BufferedReader br = new BufferedReader(new FileReader(file_name))) {

			// Le primeira linha com opcao fixo/aleatorio (nao vai ser usada)
			br.readLine();

			// Le mi
			mi = Integer.parseInt(br.readLine());
			// Le mf
			mf = Integer.parseInt(br.readLine());
			
			// array de solicitacoes
			String s[] = new String[mf - mi];
			// array de valores
			int v[] = new int[mf - mi];

			while (br.readLine() != null) {
				// le a linha
				linha = br.readLine();
				
				System.out.println(linha);
				
//				String[] linhas = linha.split(" ");
//
//				s[i] = linhas[0];
//				System.out.println( linhas[0]);
//				v[i] = Integer.parseInt(linhas[1]);
//				System.out.println(linhas[1]);
//				
				i++;
			}

		}

	}
}
