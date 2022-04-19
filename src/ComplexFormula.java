import java.util.ArrayList;
import java.util.Arrays;

public class ComplexFormula {

    private ArrayList<Object> formula = new ArrayList<>();

    public ComplexFormula(String operator, Formula... formulae){
        formula.add(operator);
        formula.addAll(Arrays.asList(formulae));
    }
}
