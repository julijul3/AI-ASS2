public interface Formula {

    public Formula toCNF();
    public boolean isAtomic();

    public void display();

    public Formula get(int index);
    public String getOp();


}
