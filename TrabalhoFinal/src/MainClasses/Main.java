package MainClasses;

import ClassesPrinc.OrdenacaoTopologica;

import java.util.Scanner;

public class Main {
    public static void main(String args[]) {

        OrdenacaoTopologica ord = new OrdenacaoTopologica();
        Scanner scan = new Scanner(System.in);
        System.out.println("Número de arestas do grafo:");
        int tam = scan.nextInt();

        if (!ord.executa(tam))
            System.out.println("O conjunto não é parcialmente ordenado.");
        else
            System.out.println("O conjunto é parcialmente ordenado.");
    }
}

