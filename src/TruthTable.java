
import java.util.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Raquel
 */
public class TruthTable {

    String formula;
    int index, ch;
    List<Character> variables;
    Map<Character, Boolean> map;
    Bool bool;

    public int get() {
        return ch = index < formula.length() ? formula.charAt(index++) : -1;
    }

    boolean match(int expect) {
        if (ch == expect) {
            get();
            return true;
        }
        return false;
    }

    public Bool element() {
        Bool b;
        if (match('(')) {
            b = expression();
            if (!match(')')) {
                throw new RuntimeException("')' expected");
            }
        } else if (Character.isAlphabetic(ch)) {
            char v = (char) ch;
            get();
            if (!variables.contains(v)) {
                variables.add(v);
            }
            b = () -> map.get(v);
        } else {
            throw new RuntimeException("unknown char: " + (char) ch);
        }
        return b;
    }

    public Bool factor() {
        if (match('¬')) {
            return element().not();
        }
        return element();
    }

    public Bool term() {
        Bool b = factor();
        while (match('^')) {
            b = b.and(factor());
        }
        return b;
    }

    public Bool expression() {
        Bool b = term();
        while (match('v')) {
            b = b.or(term());
        }
        return b;
    }

    public String str(boolean b) {
        return b ? "T" : "F";
    }

    public void print() {
        variables.forEach(v -> {
            System.out.print(str(map.get(v)) + " ");
        });
        System.out.println(str(bool.get()));
    }

    public boolean consistencyTest(int i) {
        boolean result;
        if (i >= variables.size()) {
            if ("T".equals(str(bool.get()))) {
                // we just want to know if there is one time in which it is true
                return true;
            }
        } else {
            // im not sure about this part
            char c = variables.get(i);
            map.put(c, true);
            result = consistencyTest(i + 1);
            if (result) {
                return result;
            }
            map.put(c, false);
            result = consistencyTest(i + 1);
            if (result) {
                return result;
            }
        }
        return false;
    }

    public boolean consistency(Formula left, Formula rigth) {
        // we have 2 formulas and we want to see if they are consistent (if exists
        // a way in which both are true) so we combine them with an "and", if
        // there is a moment in which in the truth table this combination is true
        // they are consistent, if not, they are inconsistent.
        ComplexFormula combination = new ComplexFormula("v", left, rigth);
        formula = combination.toCNF().toString();
        this.formula = formula.replaceAll("\\s", "");

        index = 0;
        variables = new ArrayList<>();
        map = new HashMap<>();
        get();
        bool = expression();

        if (ch != -1) {
            throw new RuntimeException(
                    "extra string '" + formula.substring(index - 1) + "'");
        }
        return consistencyTest(0);
    }
}
