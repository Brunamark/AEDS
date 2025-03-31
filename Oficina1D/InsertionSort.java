
import java.util.Comparator;

public class InsertionSort<T extends Comparable<T>> implements IOrdenator<T> {

	private T[] dadosOrdenados;
	private long comparacoes;
	private long movimentacoes;
	private long inicio;
	private long termino;
	
	public InsertionSort() {
		
		comparacoes = 0;
		movimentacoes = 0;
	}
	

	@Override
	public T[] ordenar(T[] dados) {

		return ordenar(dados, T::compareTo);
	}
	@Override
	public T[] ordenar(T[] dados, Comparator<T> comparador) {
	
		dadosOrdenados = dados;
		
		comparacoes = 0;
		movimentacoes = 0;
		iniciar();
		
		for (int i = 1; i < dadosOrdenados.length; i++) {
			T item = dadosOrdenados[i];
			int j = i - 1;

			while ((j >= 0) && (comparador.compare(dadosOrdenados[j], item) >0)) {
				comparacoes++;
				
				movimentacoes++;
				dadosOrdenados[j + 1] = dadosOrdenados[j];
				j--;
			}
			movimentacoes++;
			dadosOrdenados[j + 1] = item;
		}
		
		terminar();
		
		return dadosOrdenados;
	}
	
	@Override
	public long getComparacoes() {
		return comparacoes;
	}
	
	@Override
	public long getMovimentacoes() {
		return movimentacoes;
	}
	
	private void iniciar() {
		inicio = System.nanoTime();
	}
	
	private void terminar() {
		termino = System.nanoTime();
	}
	
	@Override
	public double getTempoOrdenacao() {
		
		double tempoTotal;
		
	    tempoTotal = (termino - inicio) / 1_000_000;
	    return tempoTotal;
	}
}