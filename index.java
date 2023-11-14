import java.util.Arrays;
import java.util.Random;

public class SimuladorSubstituicaoPaginas {
    public static void main(String[] args) {
        int linhasSwap = 100;
        int colunasSwap = 6;
        int linhasRam = 10;
        int colunasRam = 6;

        int[][] matrizSwap = new int[linhasSwap][colunasSwap];
        int[][] matrizRam = new int[linhasRam][colunasRam];

        Random random = new Random();
        for (int i = 0; i < linhasSwap; i++) {
            matrizSwap[i][0] = i; // N
            matrizSwap[i][1] = i + 1; // I
            matrizSwap[i][2] = random.nextInt(50) + 1; // D
            matrizSwap[i][3] = 0; // R
            matrizSwap[i][4] = 0; // M
            matrizSwap[i][5] = random.nextInt(9900) + 100; // T
        }

        for (int i = 0; i < linhasRam; i++) {
            int indiceSwap = random.nextInt(linhasSwap);
            matrizRam[i] = Arrays.copyOf(matrizSwap[indiceSwap], colunasRam);
        }

        System.out.println("Matriz RAM Inicial:");
        imprimirMatriz(matrizRam);
        System.out.println("\nMatriz SWAP Inicial:");
        imprimirMatriz(matrizSwap);

        for (int instrucao = 1; instrucao <= 1000; instrucao++) {
            int instrucaoRequisitada = random.nextInt(100) + 1;

            boolean instrucaoNaRam = false;
            int indiceNaRam = -1;
            for (int i = 0; i < linhasRam; i++) {
                if (matrizRam[i][1] == instrucaoRequisitada) {
                    instrucaoNaRam = true;
                    indiceNaRam = i;
                    break;
                }
            }

            if (instrucaoNaRam) {
                matrizRam[indiceNaRam][3] = 1;
                if (random.nextDouble() < 0.3) { 
                    matrizRam[indiceNaRam][2]++; 
                    matrizRam[indiceNaRam][4] = 1; 
                }
            } else {
                // Algoritmo de substituição NRU
                int classe = -1;

                for (int i = 0; i < linhasRam; i++) {
                    if (matrizRam[i][3] == 0 && matrizRam[i][4] == 0) {
                        classe = 0; 
                        break;
                    }
                }

                if (classe == -1) {
                    for (int i = 0; i < linhasRam; i++) {
                        if (matrizRam[i][3] == 0 && matrizRam[i][4] == 1) {
                            classe = 1; 
                            break;
                        }
                    }
                }

                if (classe == -1) {
                    for (int i = 0; i < linhasRam; i++) {
                        if (matrizRam[i][3] == 1 && matrizRam[i][4] == 0) {
                            classe = 2; 
                            break;
                        }
                    }
                }

                if (classe == -1) {
                    for (int i = 0; i < linhasRam; i++) {
                        if (matrizRam[i][3] == 1 && matrizRam[i][4] == 1) {
                            classe = 3; 
                            break;
                        }
                    }
                }

                int indiceSubstituir = -1;
                Random randomClasse = new Random();
                while (indiceSubstituir == -1) {
                    int candidato = randomClasse.nextInt(linhasRam);
                    int candidatoClasse = -1;

                    if (matrizRam[candidato][3] == 0 && matrizRam[candidato][4] == 0) {
                        candidatoClasse = 0;
                    } else if (matrizRam[candidato][3] == 0 && matrizRam[candidato][4] == 1) {
                        candidatoClasse = 1;
                    } else if (matrizRam[candidato][3] == 1 && matrizRam[candidato][4] == 0) {
                        candidatoClasse = 2;
                    } else if (matrizRam[candidato][3] == 1 && matrizRam[candidato][4] == 1) {
                        candidatoClasse = 3;
                    }

                    if (candidatoClasse == classe) {
                        indiceSubstituir = candidato;
                    }
                }

                int indiceNaSwap = matrizRam[indiceSubstituir][0];
                matrizRam[indiceSubstituir] = Arrays.copyOf(matrizSwap[indiceNaSwap], colunasRam);
            } else {
                // Algoritmo de substituição FIFO
                int indiceSubstituir = instrucao % linhasRam;
                int indiceNaSwap = matrizRam[indiceSubstituir][0];
                matrizRam[indiceSubstituir] = Arrays.copyOf(matrizSwap[indiceNaSwap], colunasRam);
            }

            // Algoritmo de substituição FIFO-SC
            int indiceSubstituir = -1;

            for (int i = 0; i < linhasRam; i++) {
                if (matrizRam[i][3] == 0) {
                    indiceSubstituir = i;
                    break;
                }
            }

            if (indiceSubstituir == -1) {
                for (int i = 0; i < linhasRam; i++) {
                    if (matrizRam[i][3] == 1) {
                        matrizRam[i][3] = 0; // Zera o bit R
                    }
                }
            }

            int indiceNaSwap = matrizRam[indiceSubstituir][0];
            matrizRam[indiceSubstituir] = Arrays.copyOf(matrizSwap[indiceNaSwap], colunasRam);

            // Algoritmo de substituição Relógio
            int indiceSubstituirRelogio = -1;

            while (indiceSubstituirRelogio == -1) {
                for (int i = 0; i < linhasRam; i++) {
                    if (matrizRam[i][3] == 0) {
                        indiceSubstituirRelogio = i;
                        break;
                    }
                }

                if (indiceSubstituirRelogio == -1) {
                    for (int i = 0; i < linhasRam; i++) {
                        if (matrizRam[i][3] == 1) {
                            matrizRam[i][3] = 0; 
                        }
                    }
                }
            }

            int indiceNaSwapRelogio = matrizRam[indiceSubstituirRelogio][0];
            matrizRam[indiceSubstituirRelogio] = Arrays.copyOf(matrizSwap[indiceNaSwapRelogio], colunasRam);

            // Algoritmo de substituição WS-Clock
            int tempoLimiteJanelaTrabalho = 5000;
            int indiceSubstituirWSClock = -1;

            while (indiceSubstituirWSClock == -1) {
                for (int i = 0; i < linhasRam; i++) {
                    if (matrizRam[i][3] == 0) {
                        indiceSubstituirWSClock = i;
                        break;
                    } else {
                        matrizRam[i][3] = 0; 
                        if (matrizRam[i][5] > tempoLimiteJanelaTrabalho) {
                            indiceSubstituirWSClock = i;
                            break;
                        }
                    }
                }

                if (indiceSubstituirWSClock == -1) {
                    int ponteiro = (indiceSubstituirWSClock + 1) % linhasRam;
                    matrizRam[ponteiro][5] = random.nextInt(9900) + 100;
                }
            }

            int indiceNaSwapWSClock = matrizRam[indiceSubstituirWSClock][0];
            matrizRam[indiceSubstituirWSClock] = Arrays.copyOf(matrizSwap[indiceNaSwapWSClock], colunasRam);

            if (instrucao % 10 == 0) {
                for (int i = 0; i < linhasRam; i++) {
                    matrizRam[i][3] = 0; 
                }
            }
        }

        System.out.println("\nMatriz RAM Final:");
        imprimirMatriz(matrizRam);
        System.out.println("\nMatriz SWAP Final:");
        imprimirMatriz(matrizSwap);
    }

    private static void imprimirMatriz(int[][] matriz) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                System.out.print(matriz[i][j] + "\t");
            }
            System.out.println();
        }
    }
}
