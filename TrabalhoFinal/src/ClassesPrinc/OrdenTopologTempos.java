package ClassesPrinc;

// Classe utilizada apenas para medição de tempo dos métodos.

import java.util.Random;

public class OrdenTopologTempos {
    private Elo prim;
    private int n;

    private class Elo {
        public int chave;
        public Elosuc listSuc;
        public int contador;
        public Elo prox;

        public Elo(int chave) {
            this.chave = chave;
            prox = null;
            contador = 0;
            listSuc = null;
        }

        public int string() {
            return chave;
        }
    }

    private class Elosuc {
        public Elo id;
        public Elosuc prox;

        public Elosuc(Elo id) {
            this.id = id;
            prox = null;
        }
    }

    public OrdenTopologTempos() {
        prim = null;
        n = 0;
    }

    public void realizaLeitura(int[][] par) {

        long t0 = System.nanoTime();
        Elo p = null, q = null;
        int x = 0, y = 0;
        for (int i = 0; i < par.length; i++) {
            x = par[i][0];
            y = par[i][1];

            if (busca(x)) {
                p = retornaElo(x);
            } else {
                p = insereFinal(x);
                n++;
            }
            if (busca(y)) {
                q = retornaElo(y);
                q.contador++;
                Elosuc Ep = new Elosuc(q);
                Elosuc s;
                if (p.listSuc == null) p.listSuc = Ep;
                else {
                    s = p.listSuc;
                    p.listSuc = Ep;
                    Ep.prox = s;
                }
            } else {
                q = insereFinal(y);
                n++;
                q.contador++;
                Elosuc Ep = new Elosuc(q);
                Elosuc s;

                if (p.listSuc == null) p.listSuc = Ep;
                else {
                    s = p.listSuc;
                    p.listSuc = Ep;
                    Ep.prox = s;
                }
            }
        }
        long t1 = System.nanoTime();
        long tempoProcessamento2 = t1 - t0;
        System.out.println("Tempo de execução (Método realizaLeitura()): " + tempoProcessamento2 + " ns");
    }

    public void debug() {
        long t0 = System.nanoTime();
        Elo p;
        System.out.println("Debug");
        for (p = prim; p != null; p = p.prox) {
            System.out.print(p.chave + " Predecessores: "
                    + p.contador + ", sucessores: ");
            ElosucImprime(p);
        }
        System.out.println();
        long t1 = System.nanoTime();
        long tempoProcessamento2 = t1 - t0;
        System.out.println("Tempo de execução (Método debug()): " + tempoProcessamento2 + " ns");
    }

    public boolean executa(int tam) {

        int[][] edge = geraDAG(tam);
        realizaLeitura(edge);
        debug();
        System.out.println();
        Elo p = prim;
        prim = null;
        while (p != null) {
            Elo q = p;
            p = q.prox;
            if (q.contador == 0) {
                q.prox = prim;
                prim = q;
            }
        }
        Elo s = prim;
        System.out.print("Ordenação topológica:");
        while (s != null) {
            System.out.print(s.chave + " ");
            this.n--;
            prim = s.prox;
            Elosuc t = s.listSuc;
            while (t != null) {
                t.id.contador--;
                if (t.id.contador == 0) {
                    t.id.prox = prim;
                    prim = t.id;
                }
                s.listSuc = s.listSuc.prox;
                t = s.listSuc;
            }
            s = prim;
        }
        System.out.println();
        if (n == 0) {
            return true;
        } else {
            return false;
        }


    }

    private void ElosucImprime(Elo p) {
        Elosuc s = p.listSuc;
        while (s != null) {
            System.out.print(s.id.string() + " -> ");
            s = s.prox;
        }
        System.out.print(" Null\n");
    }

    public boolean busca(int elem) {
        Elo p;
        for (p = prim; p != null; p = p.prox) {
            if (p.chave == elem) return true;
        }
        return false;
    }

    public Elo retornaElo(int elem) {
        Elo p;
        for (p = prim; p != null; p = p.prox) {
            if (p.chave == elem) return p;
        }
        return null;
    }

    public Elo insereFinal(int elem) {
        Elo p, q;
        q = new Elo(elem);
        p = prim;
        if (p != null) {
            while (p.prox != null) {
                p = p.prox;
            }
            p.prox = q;
            q.prox = null;
        } else {
            prim = q;
        }
        return q;
    }

    public int Tamanho() {
        return this.n;
    }

    public static int[][] geraDAG(int e) {
        long t0 = System.nanoTime();
        int i = 0, j = 0, count = 0;
        int[][] edge = new int[e][2];
        boolean[] check = new boolean[((e * 2) + 1)];
        Random rand = new Random();

        // Build a connection between two random vertex
        while (i < e) {

            edge[i][0] = rand.nextInt((e * 2)) + 1;
            edge[i][1] = rand.nextInt((e * 2)) + 1;

            for (j = 1; j <= (e * 2); j++)
                check[j] = false;

            if (checkAcyclic(edge, i, check, edge[i][0]) == true)

                i++;

            // Check for cycle and if found discard this
            // edge and generate random vertex pair again
        }
        long t1 = System.nanoTime();
        long tempoProcessamento2 = t1 - t0;
        System.out.println("Tempo de execução (Método geraDAG()): " + tempoProcessamento2 + " ns");
        return edge;
    }

    public static boolean checkAcyclic(int[][] edge, int ed,
                                       boolean[] check, int v) {
        int i;
        boolean value;

        // If the current vertex is visited already, then
        // the graph contains cycle

        if (check[v] == true)

            return false;

        else {

            check[v] = true;

            // For each vertex, go for all the vertex
            // connected to it
            for (i = ed; i >= 0; i--) {

                if (edge[i][0] == v)

                    return checkAcyclic(edge, ed, check, edge[i][1]);
            }
        }
        check[v] = false;

        if (i == 0)
            return true;
        return true;
    }
}

