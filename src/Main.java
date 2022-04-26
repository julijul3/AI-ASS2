
import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);
    static BeliefBase BB = new BeliefBase();

    public static void main(String[] args) {

        //testResolution();
        testAGMPostulates();
        //fill the belief base with input from the console
        fillBB();

        //get new clause phi
        System.out.println("Enter a new clause phi");
        Formula phi = getInput();

        //check entailment (point two)
        boolean checkEntailment = BB.checkEntailment(phi);
        System.out.print("phi is ");
        if (!checkEntailment) {
            System.out.print("not");
        }
        System.out.println("entailed by the Belief Base");

        //contraction of the belief base
        System.out.println("adding phi to the Belief base: ");
        if (checkEntailment) {
            BB.addFormula(phi);
        } else {
            BB.leviId(phi);
        }
    }

    public static void fillBB() {
        System.out.println("Enter input for the Belief Base: one clause per line, empty line when done");
        String input;
        do {
            input = sc.nextLine();
            Formula f = createF(input);
            BB.addFormula(f);
        } while (!input.isEmpty());
    }

    public static Formula createF(String input) {
        if (input.startsWith("(")) { //complex formula
            //find the operator (not a letter, not inside pair of parenthesis)
            String op = "";
            int indexOp = 0;
            int numberOfP = 0;
            for (int i = 1; i < input.length(); i++) {
                char c = input.charAt(i);
                if (c == '(') {
                    numberOfP++;
                } else if (c == ')') {
                    numberOfP--;
                } else if (!(Character.isAlphabetic(c) && c != 'v') && numberOfP == 0 && c != ' ') {
                    //this is our operator :)
                    if (c == '<') {
                        op = "<->";
                    } else if (c == '-') {
                        op = "->";
                    } else {
                        op = String.valueOf(c);
                    }
                    indexOp = i;

                    Formula f1 = createF(input.substring(1, indexOp - 1));
                    Formula f2 = createF(input.substring(indexOp + op.length() + 1, input.length() - 1));
                    // first formula is from after the parenthesis, until two before the index (because of space)
                    //the second is from one space after the end of op, until one before last (parenthesis)
                    return new ComplexFormula(op, f1, f2);
                }
            }

            System.out.println("Could not find operator: probably a wrong parenthesis somewhere");
            return null;

        } else { //either atomic either negation

            if (input.startsWith("¬")) {
                return new ComplexFormula("¬", new AtomicFormula(input.substring(1)));
            } else {
                return new AtomicFormula(input);
            }
        }
    }

    public static Formula getInput() {
        String input = sc.nextLine();
        return createF(input);
    }

    public static void testCNF() {

        System.out.println("tests CNF");

        AtomicFormula p = new AtomicFormula("p");
        AtomicFormula q = new AtomicFormula("q");
        // p<->q == ((!p) v q) & ((!q) v p)
        Formula toConvert = new ComplexFormula("<->", p, q);
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
    }

    public static void testResolution() {
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

    public static void testAGMPostulates() {
        System.out.println("testing AGM Postulates");

        //test for success
        BB.empty();
        AtomicFormula p = new AtomicFormula("p");
        AtomicFormula q = new AtomicFormula("q");

        BB.addFormula(p);
        BB.addFormula(new ComplexFormula("^", p, q));

        BB.display();

        //System.out.println(BB.checkEntailment(new ComplexFormula("¬", q)));
        BB.leviId(new ComplexFormula("¬", q));
        System.out.println("Should print the following belief base: p, ¬q");
        BB.display();
    }
}
