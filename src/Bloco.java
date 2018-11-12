import java.util.Arrays;

public class Bloco {
	private int i;
	private int f;
	private int tamanho;
	private int[] bloco;

	public Bloco(int i, int f) {
		this.i = i;
		this.f = f;
		this.tamanho = this.f - this.i;
		criaBloco(this.i, this.f, this.tamanho);
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
