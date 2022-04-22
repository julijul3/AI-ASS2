public class Main {

    public static void main(String[] args) {
        System.out.println("tests CNF");

        AtomicFormula p = new AtomicFormula("p");
        AtomicFormula q = new AtomicFormula("q");
        // p<->q == ((!p) v q) & ((!q) v p)
        Formula toConvert = new ComplexFormula("<->",p,q);
        Formula cnf = toConvert.toCNF();
        cnf.display();

        System.out.println();
        AtomicFormula r = new AtomicFormula("r");
        Formula pqr = new ComplexFormula("->", new ComplexFormula("v", p, q), r);
        pqr.toCNF().display();

        System.out.println();
        Formula aa = new ComplexFormula("v", p, p);
        aa.toCNF().display();

        System.out.println();
        Formula anona = new ComplexFormula("^", p, new ComplexFormula("¬", p));
        anona.toCNF().display();

        //testing for resolution
        System.out.println("Test resolution");
        AtomicFormula reussi = new AtomicFormula("reussi");
        AtomicFormula prepared = new AtomicFormula("prepared");
        AtomicFormula lucky = new AtomicFormula("lucky");
        
        ComplexFormula kb1 = new ComplexFormula("<->", reussi, new ComplexFormula("v", prepared, lucky));
        ComplexFormula kb2 = new ComplexFormula("¬", reussi);

        ComplexFormula phi = new ComplexFormula("¬", prepared);

        BeliefBase BB = new BeliefBase(kb1, kb2);
        System.out.println(BB.checkEntailment(phi));


        
        
    }
    

}
