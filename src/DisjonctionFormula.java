
import java.util.LinkedList;
import java.util.List;

public class DisjonctionFormula implements Formula {

    List<Formula> formulae = new LinkedList<>();

    public DisjonctionFormula(Formula... literals) {
        for (Formula f : literals) {
            if (!f.isAtomic()) {
                throw new IllegalArgumentException("The formulae in a DisjunctionFormula must all be of form p or ¬p");
            } else {
                if (!formulae.contains(f)) {
                    formulae.add(f);
                }
            }
        }
    }

    public List<Formula> getList() {
        return formulae;
    }

    @Override
    public Formula toCNF() {
        return this;
    }

    @Override
    public boolean isAtomic() {
        return formulae.size() == 1 && formulae.get(0).isAtomic();
    }

    @Override
    public void display() {
        System.out.print("(");
        for (int i = 0; i < formulae.size() - 1; i++) {
            formulae.get(i).display();
            System.out.println(" v ");
        }
        formulae.get(formulae.size() - 1).display();
        System.out.println(")");
    }

    @Override
    public Formula get(int index) {
        return this;
    }

    @Override
    public String getOp() {
        return "v";
    }
}
