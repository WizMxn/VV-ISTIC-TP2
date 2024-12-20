package fr.istic.vv.example;

public class Max {

    public int max(int a, int b, int c) {
        if (a > b) {
            if (a > c) {
                return a;
            } else {
                return c;
            }
        } else {
            if (b > c) {
                return b;
            } else {
                return c;
            }
        }
    }

    void foo(int a, int b, int c) {
        if (a > b) {
            if (a > c) {
                System.out.println(a);
            } else {
                System.out.println(b);
            }
        } else {
            if (b > c) {
                System.out.println(c);
            } else {
                System.out.println(b);
            }
        }
    }
}
