import java.util.ArrayList;
import java.util.Random;

class Page {
    int N;
    int I;
    int D;
    int R;
    int M;
    int T;

    public Page(int N, int I, int D, int T) {
        this.N = N;
        this.I = I;
        this.D = D;
        this.R = 0;
        this.M = 0;
        this.T = T;
    }
}

public class PageReplacementSimulator {
    public static void main(String[] args) {
        int ramSize = 10;
        int swapSize = 100;
        ArrayList<Page> ram = new ArrayList<>(ramSize);
        ArrayList<Page> swap = new ArrayList<>(swapSize);

        // Inicialize a matriz SWAP
        for (int i = 0; i < swapSize; i++) {
            int D = new Random().nextInt(50) + 1;
            swap.add(new Page(i, i + 1, D, new Random().nextInt(9900) + 100));
        }

        // Preencha a matriz RAM
        Random random = new Random();
        for (int i = 0; i < ramSize; i++) {
            int randomIndex = random.nextInt(swapSize);
            ram.add(new Page(i, swap.get(randomIndex).I, swap.get(randomIndex).D, swap.get(randomIndex).T));
        }

        // Imprima as MATRIZ RAM e MATRIZ SWAP no início da simulação
        System.out.println("MATRIZ RAM no início da simulação:");
        printMatrix(ram);
        System.out.println("\nMATRIZ SWAP no início da simulação:");
        printMatrix(swap);

        // Simulação de 1000 instruções
        for (int instructionCount = 0; instructionCount < 1000; instructionCount++) {
            int instructionNumber = random.nextInt(100) + 1;

            // Verifique se a instrução está na memória RAM
            Page page = null;
            for (Page p : ram) {
                if (p.I == instructionNumber) {
                    page = p;
                    break;
                }
            }

            if (page != null) {
                // A instrução está na RAM
                page.R = 1;

                if (random.nextDouble() <= 0.3) {
                    page.D = page.D + 1;
                    page.M = 1;
                }
            } else {
                // Implemente o algoritmo de substituição de página aqui (NRU, FIFO, FIFO-SC, RELÓGIO, WS-CLOCK)
                // Você deve remover uma página da RAM e adicionar a nova página da SWAP

                // Lógica de substituição de página aqui
            }

            if (instructionCount % 10 == 0) {
                // Zere o bit R a cada 10 instruções
                for (Page p : ram) {
                    p.R = 0;
                }
            }
        }

        // Salve as páginas modificadas na SWAP
        for (Page p : ram) {
            if (p.M == 1) {
                // Salve a página modificada na SWAP e defina M como 0
                swap.add(new Page(swap.size(), p.I, p.D, p.T));
                p.M = 0;
            }
        }

        // Imprima as MATRIZ RAM e MATRIZ SWAP no final da simulação
        System.out.println("\nMATRIZ RAM no final da simulação:");
        printMatrix(ram);
        System.out.println("\nMATRIZ SWAP no final da simulação:");
        printMatrix(swap);
    }

    // Método para imprimir a matriz
    public static void printMatrix(ArrayList<Page> matrix) {
        for (Page page : matrix) {
            System.out.printf("N=%d, I=%d, D=%d, R=%d, M=%d, T=%d\n",
                    page.N, page.I, page.D, page.R, page.M, page.T);
        }
    }
}
