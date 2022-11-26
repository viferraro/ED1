package ClassesPrinc;

import java.util.Random;

public class OrdenacaoTopologica {

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

    public OrdenacaoTopologica() {
        prim = null;
        n = 0;
    }

    //Método responsável pela leitura do arquivo de entrada.
    public void realizaLeitura(int[][] edge) {   // Complexidade O(n)

        Elo p = null, q = null;
        int x = 0, y = 0;
        for (int i = 0; i < edge.length; i++) {
            x = edge[i][0];             //  Passa os valores da aresta gerada para inteiros (x,y)
            y = edge[i][1];

            if (busca(x)) {
                p = retornaElo(x);       // Se o x já está na lista.
            } else {
                p = insereFinal(x);   // Se não está, insere no final da lista.
                n++;      // Incrementa n (elementos na lista/vértices).
            }
            if (busca(y)) {
                q = retornaElo(y);       // Se o y já está na lista.
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
                q = insereFinal(y);         // Se não está, insere no final da lista.
                n++;                 // Incrementa n (elementos na lista/vértices).
                q.contador++;        // Incrementa contador (predecessores).
                Elosuc Ep = new Elosuc(q);   // Cria um Elosuc (sucessor), com o id (elo) q.
                Elosuc s;   // Elosuc auxiliar.

                if (p.listSuc == null)
                    p.listSuc = Ep;  // Se a lista de sucessores está vazia, o elo p recebe o novo Elosuc.
                else {
                    s = p.listSuc;  // Se não, adiciona no início da lista de sucessores de p.
                    p.listSuc = Ep;
                    Ep.prox = s;
                }
            }
        }
    }

    // Imprime na tela a estrutura de dados resultante.
    public void debug() {    // Complexidade O(nxm)
        Elo p;
        System.out.println("Debug");
        for (p = prim; p != null; p = p.prox) {
            System.out.print(p.chave + " Predecessores: "
                    + p.contador + ", sucessores: ");
            ElosucImprime(p);
        }
        System.out.println();
    }

    // Chama os métodos necessários para a execução do programa e faz a ordenação topológica.
    public boolean executa(int tam) {  // Complexidade O(nxm)

        int[][] edge = geraDAG(tam);
        realizaLeitura(edge);
        debug();
        System.out.println();
        Elo p = prim;
        prim = null;   // Utiliza a lista principal para armazenar a nova sequencia.
        while (p != null) {
            Elo q = p;
            p = q.prox;
            if (q.contador == 0) {   // Se tiver 0 antecessores.
                q.prox = prim;     // Inclui no início da nova lista.
                prim = q;
            }
        }
        Elo s = prim;    // Recebe a nova lista (elementos com 0 antecessores)
        System.out.print("Ordenação topológica:");
        while (s != null) {
            System.out.print(s.chave + " ");
            this.n--;    // Decrementa n (no. de elementos)
            prim = s.prox;
            Elosuc t = s.listSuc;   // Acessa lista de sucessores do elemento.
            while (t != null) {
                t.id.contador--;    // Decrementa o valor do contador dos elos remanescentes
                if (t.id.contador == 0) {
                    t.id.prox = prim;   // Se o contador é 0, inclui no início da lista.
                    prim = t.id;
                }
                s.listSuc = s.listSuc.prox;
                t = s.listSuc;
            }
            s = prim;   // Atualiza o Elo s.
        }
        System.out.println();
        if (n == 0) {   // Se o processo termina sem elementos.
            return true;
        } else {
            return false;   // Se não, o grafo não é DAG.
        }


    }
    // Método auxiliares

    // Imprime a lista de sucessores.
    private void ElosucImprime(Elo p) {  // Complexidade O(n)
        Elosuc s = p.listSuc;
        while (s != null) {
            System.out.print(s.id.string() + " -> ");
            s = s.prox;
        }
        System.out.print(" Null\n");
    }

    // Busca (retorna booleano).
    public boolean busca(int elem) {  // Complexidade O(n)
        Elo p;
        for (p = prim; p != null; p = p.prox) {
            if (p.chave == elem) return true;
        }
        return false;
    }

    // Busca (retorna o elo buscado).
    public Elo retornaElo(int elem) {   // Complexidade O(n)
        Elo p;
        for (p = prim; p != null; p = p.prox) {
            if (p.chave == elem) return p;
        }
        return null;
    }

    // Inserção de elementos na lista
    public Elo insereFinal(int elem) {     // Complexidade O(n).
        Elo p, q;
        q = new Elo(elem);   // Cria o elo
        p = prim;
        if (p != null) {     // Se a lista não está vazia
            while (p.prox != null) {
                p = p.prox;
            }
            p.prox = q;
            q.prox = null;
        } else {
            prim = q;     // Se a lista está vazia
        }
        return q;
    }

    public int Tamanho() {   // Complexidade O(1)
        return this.n;
    }

    // Métodos adaptados de algoritmo gerador encontrado na internet.

    public static int[][] geraDAG(int e) {

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

        // In case, if the path ends then reassign the
        // vertexes visited in that path to false again
        check[v] = false;

        if (i == 0)
            return true;
        return true;
    }
}

