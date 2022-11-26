package MainClasses;

import ClassesPrinc.OrdenTopologTempos;


public class TempoMain {
    public static void main(String args[]) {

        OrdenTopologTempos ord = new OrdenTopologTempos();

        for (int i = 0; i <= 2; i++) {
            System.out.println(("\nTeste " + (i + 1) + ": "));
            long t0 = System.nanoTime();
            if (!ord.executa(10))
                System.out.println("O conjunto não é parcialmente ordenado.");
            else
                System.out.println("O conjunto é parcialmente ordenado.");

            long t1 = System.nanoTime();
            long tempoProcessamento2 = t1 - t0;
            System.out.println("Tempo de execução (executa()): " + tempoProcessamento2 + " ns");
        }
    }
}

