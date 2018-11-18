import java.util.Arrays;

public class Bloco {
	private int id;

	// posicao inicial e final do bloco na memória
	private int i;
	private int f;

	private int tamanho;

	private int[] bloco;

	public Bloco(int i, int f, int t, int id) {
		this.i = i;
		this.f = f;
		this.tamanho = t;
		this.id = id;
		criaBloco(this.i, this.f, this.tamanho);
	}

	public void setTamanho(int tamanho) {
		this.tamanho = tamanho;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setI(int i) {
		this.i = i;
	}

	public void setF(int f) {
		this.f = f;
	}

	public void setBloco(int[] bloco) {
		this.bloco = bloco;
	}

	public int getId() {
		return id;
	}

	public int getI() {
		return i;
	}

	public int getF() {
		return f;
	}

	public int getTamanho() {
		return tamanho;
	}

	private void criaBloco(int i, int f, int tam) {
		bloco = new int[tam];
	}

	public int[] getBloco() {
		return bloco;
	}

	@Override
	public String toString() {
		return "Bloco [" + Arrays.toString(bloco) + "]";
	}

}
