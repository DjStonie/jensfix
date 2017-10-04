import java.util.concurrent.Semaphore;

public class Main {
    public static Cake[] kager = new Cake[10];
    private static Semaphore hylde = new Semaphore(1);

    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < kager.length; i++) {
            kager[i] = null;
        }

        Baker bageren = new Baker();
        Thread bagerensTraad = new Thread(bageren);

        Consumer kunden = new Consumer();
        Thread kundensTraad = new Thread(kunden);

        Consumer kunden2 = new Consumer();
        Thread kunden2sTraad = new Thread(kunden2);

        Consumer kunden3 = new Consumer();
        Thread kunden3sTraad = new Thread(kunden3);

        bagerensTraad.start();
        kundensTraad.start();
        kunden2sTraad.start();
        kunden3sTraad.start();

    }

    public static void buyCake() {

    	int take = -1;
    	
    	try {
            hylde.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    	
        for (int j = 0; j < kager.length; j++) {
            if (kager[j] != null) {
            	take = j;
                break;
            }
        }
        if (take == -1){
        	hylde.release();
        	return;
        }

        int spistKage = take;

        System.out.println("Kage fra hyldeplads " + (spistKage+1) + " blev spist");

        kager[spistKage] = null;

        for (int i = 0; i < (kager.length); i++) {
            if (kager[i] != null) {
                System.out.println("hyldeplads nummer " + (i+1) + " har en " + kager[i].getName());
            } else {
                System.out.println("hyldeplads nummer " + (i+1) + " har IKKE en kage");
            }
        }

        System.out.println("_______________________________");

        take = take % kager.length;

        hylde.release();
        return;
    }

    public static void putCake(Cake cake) {
    	
    	int put = -1;
    	
    	try {
            hylde.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int j = 0; j < kager.length; j++) {
            if (kager[j] == null) {
            	put = j;
                break;
            }
        }
        if (put == -1){
        	hylde.release();
        	return;
        }



        kager[put] = cake;

        System.out.println(cake.getName() + " blev sat på hylde nummer: " + (put+1));

        for (int i = 0; i < (kager.length); i++) {
            if (kager[i] != null) {
                System.out.println("hyldeplads nummer " + (i+1) + " har en " + kager[i].getName());
            } else {
                System.out.println("hyldeplads nummer " + (i+1) + " har IKKE en kage");
            }
        }

        System.out.println("_______________________________");

        put = put % kager.length; // cyklisk indexering: foo = foo % bar.length();

        hylde.release();
    }
}
