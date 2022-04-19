public class Main {

    public static void main(String[] args) {
        System.out.println("test");

        AtomicFormula p = new AtomicFormula("p");
        AtomicFormula q = new AtomicFormula("q");
        // p<->q == ((!p) v q) & ((!q) v p)
        Formula toConvert = new ComplexFormula("<->",p,q);
        Formula cnf = toConvert.toCNF();
        cnf.display();
        
    }
 

}
