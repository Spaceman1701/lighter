package fun.connor.lighter.example;

import fun.connor.lighter.Lighter;

public class Main {

    public static void main(String[] args) {
        Lighter l = new Lighter(Class::newInstance);

        System.out.println("just testing the build");
    }
}