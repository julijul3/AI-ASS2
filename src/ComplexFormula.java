
import java.util.ArrayList;
import java.util.Arrays;

public class ComplexFormula implements Formula {

    public String operator;
    public ArrayList<Formula> formula = new ArrayList<>();

    public ComplexFormula(String operator, Formula... formulae) {
        this.operator = operator;
        formula.addAll(Arrays.asList(formulae));
    }

    @Override
    public boolean isAtomic() {
        return operator.equals("¬") && formula.get(0).isAtomic();
    }

    @Override
    public Formula get(int index) {
        return formula.get(index);
    }

    @Override
    public String getOp() {
        return operator;
    }

    @Override
    public Formula toCNF() {

        if (operator.equals("<->")) { //p iff q == (p -> q) and (q -> p)
            return new ComplexFormula("^", new ComplexFormula("->", formula.get(0), formula.get(1)),
                    new ComplexFormula("->", formula.get(1), formula.get(0))).toCNF();
        }
        if (operator.equals("->")) { //p -> q == ¬p v q
            return new ComplexFormula("v", new ComplexFormula("¬", formula.get(0)),
                    formula.get(1)).toCNF();
        }
        if (operator.equals("¬")) { //use De Morgan
            Formula A = formula.get(0);
            if (A.isAtomic()) { //¬A
                return this;
            }
            if (A.getOp().equals("¬")) { //¬¬a
                return A.get(0).toCNF();
            }
            if (A.getOp().equals("^")) { //¬(a^b) == ¬a v ¬b
                return new ComplexFormula("v", new ComplexFormula("¬", (Formula) A.get(0)),
                        new ComplexFormula("¬", (Formula) A.get(1))).toCNF();
            }
            if (A.getOp().equals("v")) { //¬(avb) == ¬a ^ ¬b
                return new ComplexFormula("^", new ComplexFormula("¬", (Formula) A.get(0)),
                        new ComplexFormula("¬", (Formula) A.get(1))).toCNF();
            }
        }
        if (operator.equals("v")) {
            Formula A = formula.get(0);
            Formula B = formula.get(1);
            if (A.isAtomic() && B.isAtomic()) { //a v b
                if (A.equals(B)) { //A v A
                    return new AtomicFormula(((AtomicFormula) A).getName()); //Factorise
                }
                return this;
            } else if (A.getOp().equals("¬") || B.getOp().equals("¬")) {
                return new ComplexFormula("v", A.toCNF(), B.toCNF());
            } else if (A.isAtomic()) { //a v (b ^ c) == (a v b) ^ (a v c)
                return new ComplexFormula("^", new ComplexFormula("v", A, B.get(0).toCNF()),
                        new ComplexFormula("v", A, B.get(1).toCNF())).toCNF();
            } else if (B.isAtomic()) { //(a ^b) v B == (a v B) ^ (b v B)
                return new ComplexFormula("^", new ComplexFormula("v", A.get(0).toCNF(), B),
                        new ComplexFormula("v", A.get(1).toCNF(), B)).toCNF();
            } else { //(a ^b) v (c ^ d) == ((a v c) ^ (a v d)) ^ ((b v c) ^ (b v d))
                return new ComplexFormula("^",
                        new ComplexFormula("^", new ComplexFormula("v", A.get(0).toCNF(), B.get(0).toCNF()),
                                new ComplexFormula("v", A.get(0).toCNF(), B.get(1).toCNF())),
                        new ComplexFormula("^", new ComplexFormula("v", A.get(1).toCNF(), B.get(0).toCNF()),
                                new ComplexFormula("v", A.get(1).toCNF(), B.get(1).toCNF()))
                ).toCNF();
            }
        }
        if (operator.equals("^")) {
            Formula A = formula.get(0);
            Formula B = formula.get(1);
            if (A.isAtomic() && B.isAtomic()) { //a ^ b
                if (A.equals(B)) {
                    return new AtomicFormula(((AtomicFormula) A).getName()); //Factorise
                }
            }
            return new ComplexFormula("^", A.toCNF(), B.toCNF());
        }

        //if here there was an error
        System.out.println("error in converting to CNF: there is an undefined operator " + operator);
        return null;
    }

    @Override
    public void display() {
        if (!operator.equals("¬")) {
            System.out.print("(");
            formula.get(0).display();
            System.out.print(" " + operator + " ");
            formula.get(1).display();
            System.out.print(")");
        } else {
            System.out.print(operator);
            formula.get(0).display();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComplexFormula that = (ComplexFormula) o;
        return operator.equals(that.operator)
                && formula.equals(that.formula);
    }
}
