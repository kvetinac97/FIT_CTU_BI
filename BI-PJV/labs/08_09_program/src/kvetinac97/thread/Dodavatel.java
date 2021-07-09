package kvetinac97.thread;

import kvetinac97.Thread;

public class Dodavatel extends Osoba implements Runnable {

    private String supplies;

    public Dodavatel(String first, String last, int age, String supplies) {
        super(first, last, age);
        this.supplies = supplies;
    }

    @Override
    public void run() {
        while ( true ) {
            try {
                java.lang.Thread.sleep(1000);
            } catch (InterruptedException e) {}

            Thread.addProduct(supplies);
            System.out.println(this + " supplied " + supplies);
        }
    }

}
