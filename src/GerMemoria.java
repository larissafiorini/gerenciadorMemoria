
public class GerMemoria {
	private int mi;
	private int mf;

	// 1. O gerente deve receber um bloco que representa a memória disponível do
	// endereço mi até o endereço mf.
	private Bloco bloco_inicial;

	private GerBlocos ger;

	public GerMemoria(int mi, int mf, GerBlocos g, Bloco b) {
		this.mi = mi;
		this.mf = mf;
		this.ger = g;
		this.bloco_inicial = b;
	}

	public void solicitacao() {

	}

	public void liberacao() {

	}

}
