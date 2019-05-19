

public class A {


    private int i = 0;
    public A() {
        this.i = 1;
    }


    public int get() {
        return this.i;
    }

    public static void main(String[] args) {
        A xiaoA = new A();
        xiaoA.get();
    }


}
