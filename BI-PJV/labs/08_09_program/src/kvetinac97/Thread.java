package kvetinac97;

import kvetinac97.thread.Dodavatel;
import kvetinac97.thread.Konzument;

import java.util.ArrayList;
import java.util.List;

public class Thread {

    private static List<String> products = new ArrayList<>();

    public static synchronized void addProduct ( String product ) {
        products.add(product);
        // tady bychom dali notifyAll kdybychom nebyli ve static
    }

    public static synchronized boolean getProduct ( String product ) {
        if ( products.contains(product) ) {
            products.remove(product);
            return true;
        }
        // tady bychom dali wait kdybychom nebyli ve static
        return false;
    }

    public static void main(String[] args) {
	    // Priority vláken záleží na systému
        // určitě je safe použít MIN_PRIORITY, NORM_PRIORITY, MAX_PRIORITY
        java.lang.Thread t = new java.lang.Thread();
        t.setPriority(java.lang.Thread.MIN_PRIORITY);

        // Některá vlákna můžou být i démoni (daemon)
        // normální vlákno se ukončí až když skončí všechna nedémonická vlákna
        t.setDaemon(true);

        // Vláknu můžeme říct, že nebude dělat nic jiného,
        // dokud jiné vlákno neukončí svoji činnost
        java.lang.Thread t2 = new java.lang.Thread(() -> {
            try {
                t.join();
            }
            catch ( InterruptedException e ) {
                e.printStackTrace();
            }
        });
        t2.start();

        // Jednou z typických situací může být i Consumer x Producer
        // producenti/consumeři se snaží dostat věci do/z skladu, který má kapacitu
        // producer/consumer čeká na (volné místo ve skladu)/(produkt ve skladu)
        // jak ale udělat, aby se produkt neodebral 2x?

        // Pomocí metod notify() a wait().
        // Musíme ale dávat pozor, aby nedošlo k nekonzistenci
        // a nebezpečné metody musíme dát do synchronized

        Konzument karel = new Konzument("Karel", "Novák", 22, "MacBook");
        Konzument karol = new Konzument("Karel", "Novotný", 20, "MacBook");
        Konzument josef = new Konzument("Josef", "Novotný", 47, "PC");
        Konzument marta = new Konzument("Marta", "Nová", 13, "iPhone");

        //Dodavatel alza = new Dodavatel("Alza", "Express", 20, "iPhone");
        Dodavatel czc = new Dodavatel("CZC", ".cz", 15, "MacBook");

        new java.lang.Thread(karel).start();
        new java.lang.Thread(karol).start();
        /*new Thread(josef).start();
        new Thread(marta).start();*/

        //new Thread(alza).start();
        new java.lang.Thread(czc).start();

    }

}
