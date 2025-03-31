
import java.util.Comparator;

public class ComparadorPorCodigo implements Comparator<Produto>   {

    @Override
    public int compare(Produto o1, Produto o2) {
        if(o1.hashCode() == o2.hashCode())
            return 0;
        else if (o1.hashCode() < o2.hashCode())
            return -1;
        else 
            return 1;
    }
    
}
