package kvetinac97.thread;

import kvetinac97.Thread;

public class Konzument extends Osoba implements Runnable {

    private String wants;

    public Konzument(String first, String last, int age, String wants) {
        super(first, last, age);
        this.wants = wants;
    }

    @Override
    public void run() {
        while ( true ) {
            try {
                java.lang.Thread.sleep(1000);
            } catch (InterruptedException e) {}
            if ( Thread.getProduct(wants) )
                System.out.println(this + " bought " + wants);
        }
    }

}
