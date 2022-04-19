
public class AtomicFormula implements Formula{

    private String name;

    public AtomicFormula(String name){
        this.name = name;
    }

    @Override
    public Formula toCNF() {
        return this;
    }

    public boolean isAtomic(){
        return true;
    }

    public void display(){
        System.out.print(" " + name + " ");
    }

    public Formula get(int index){
        //this is never called
        return this;
    }

    public String getOp(){
        return "";
    }
}
