
import java.util.Objects;

public class AtomicFormula implements Formula {

    private final String name;

    public AtomicFormula(String name) {
        this.name = name;
    }

    @Override
    public Formula toCNF() {
        return this;
    }

    @Override
    public boolean isAtomic() {
        return true;
    }

    @Override
    public void display() {
        System.out.print(name);
    }

    @Override
    public Formula get(int index) {
        //this is never called
        return this;
    }

    @Override
    public String getOp() {
        return "";
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AtomicFormula that = (AtomicFormula) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
