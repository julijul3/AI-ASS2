
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class BeliefBase {

    private final Set<Formula> BB = new HashSet<>();

    public BeliefBase(Formula... formulae) {
        BB.addAll(Arrays.asList(formulae));
    }

    public void addFormula(Formula f){
        BB.add(f);
    }

    public boolean checkEntailment(Formula phi) {

        // we want a set of formula containing the clauses of BB ^ ¬phi in their CNF form
        Set<DisjonctionFormula> set = new HashSet<>();
        BB.forEach(f -> {
            addClauses(f.toCNF(), set);
        });

        Formula nonPhi = new ComplexFormula("¬", phi);
        addClauses(nonPhi.toCNF(), set);

        // now we resolve by finding pairs of complimentary literals
        Set<DisjonctionFormula> toCompare;
        Set<DisjonctionFormula> toIterate;
        boolean hasChanged;
        boolean emptyClause;

        do {
            hasChanged = false;
            toCompare = new HashSet<>(set);
            toIterate = new HashSet<>(set);
            for (DisjonctionFormula a : toIterate) {
                for (DisjonctionFormula b : toIterate) {
                    if (a != b) {
                        emptyClause = !resolve(a, b, set);
                        if (emptyClause) {
                            return true;
                        }
                        hasChanged = set.equals(toCompare);
                        if (hasChanged) {
                            break;
                        }
                    }
                }
                if (hasChanged) {
                    break;
                }
            }
        } while (hasChanged);

        return false;
    }

    private void addClauses(Formula f, Set<DisjonctionFormula> set) {
        if (f.getOp().equals("^")) {
            addClauses(f.get(0), set);
            addClauses(f.get(1), set);
        } else {
            //extract all formulas here
            List<Formula> list = new LinkedList<>();
            toDisjonctiveF(f, list);
            DisjonctionFormula df = new DisjonctionFormula(list.toArray(new Formula[0]));
            set.add(df);
        }
    }

    private void toDisjonctiveF(Formula f, List<Formula> list) {
        if (f.isAtomic()) {
            list.add(f);
        } else {
            toDisjonctiveF(f.get(0), list);
            toDisjonctiveF(f.get(1), list);
        }
    }

    private boolean resolve(DisjonctionFormula a, DisjonctionFormula b, Set<DisjonctionFormula> set) {
        for (Formula f1 : a.getList()) {
            for (Formula f2 : b.getList()) {
                if ((f1.getOp().equals("¬") && f1.get(0).equals(b))
                        || f2.getOp().equals("¬") && f2.get(0).equals(a)) { // f1 and f2 are complementary

                    // if f1 and f2 were the only literals here -> it yields the emptyClause!
                    if (a.isAtomic() && b.isAtomic()) {
                        return true;
                    } else {
                        //we create a new Formula with all the other literals and remove f1 f2
                        DisjonctionFormula newA = new DisjonctionFormula(a.getList().toArray(new Formula[0]));
                        newA.getList().addAll(b.getList());
                        newA.getList().remove(f1);
                        newA.getList().remove(f2);

                        set.remove(a);
                        set.remove(b);
                        set.add(newA);
                    }
                    return false;
                }
            }
        }
        //no complementary literals in these two formula
        return false;
    }

    public void leviId(Formula f) { // B ∗ φ := (B ÷ ¬φ) + φ
        BB.remove(new ComplexFormula("¬", f));
        BB.add(f);
    }

    public void display(){
        for (Formula f : BB) {
            f.display();
            System.out.println();
        }
    }
}
