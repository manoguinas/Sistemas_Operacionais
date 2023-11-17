import java.util.concurrent.Semaphore;

class Mesa {
    private Semaphore[] garfos;

    public Mesa(int numFilosofos) {
        garfos = new Semaphore[numFilosofos];
        for (int i = 0; i < numFilosofos; i++) {
            garfos[i] = new Semaphore(1);
        }
    }

    public void pegarGarfos(int filosofoId) throws InterruptedException {
        garfos[filosofoId].acquire();
        garfos[(filosofoId + 1) % garfos.length].acquire();
    }

    public void liberarGarfos(int filosofoId) {
        garfos[filosofoId].release();
        garfos[(filosofoId + 1) % garfos.length].release();
    }
}

class Filosofo extends Thread {
    private int id;
    private Mesa mesa;

    public Filosofo(int id, Mesa mesa) {
        this.id = id;
        this.mesa = mesa;
    }

    public void run() {
        try {
            while (true) {
                pensar();
                mesa.pegarGarfos(id);
                comer();
                mesa.liberarGarfos(id);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void pensar() throws InterruptedException {
        System.out.println("O filósofo " + id + " está pensando.");
        Thread.sleep((long) (Math.random() * 1000));
    }

    private void comer() throws InterruptedException {
        System.out.println("O filósofo " + id + " está comendo.");
        Thread.sleep((long) (Math.random() * 1000));
    }
}

public class JantarDosFilosofos {
    public static void main(String[] args) {
        int numFilosofos = 5;
        Mesa mesa = new Mesa(numFilosofos);
        Filosofo[] filosofos = new Filosofo[numFilosofos];

        for (int i = 0; i < numFilosofos; i++) {
            filosofos[i] = new Filosofo(i, mesa);
            filosofos[i].start();
        }
    }
}
