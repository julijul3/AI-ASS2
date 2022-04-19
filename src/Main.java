public class Main {

    public static void main(String[] args) {
        System.out.println("tests");

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
        
    }
    

}
