public class SAP {
   private Digraph G;
   private static final int INFINITY = Integer.MAX_VALUE;

   // constructor takes a digraph (not necessarily a DAG)
   public SAP(Digraph G) {
      this.G = new Digraph(G);
      // System.out.println(this.G.V());
      // System.out.println(this.G);
   }

   // length of shortest ancestral path between v and w; -1 if no such path
   public int length(int v, int w) {
      if (v > G.V() - 1 || v < 0 || w > G.V() - 1 || w < 0) throw new IndexOutOfBoundsException();
      return LA(v, w)[1];
   }

  // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
  public int ancestor(int v, int w) {
    if (v > G.V() - 1 || v < 0 || w > G.V() - 1 || w < 0) throw new IndexOutOfBoundsException();
    return LA(v, w)[0];
  }

   // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
   public int length(Iterable<Integer> v, Iterable<Integer> w) {
      if (v == null || w == null) throw new NullPointerException();
      return LA(v, w)[1];
   }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
      if (v == null || w == null) throw new NullPointerException();
      return LA(v, w)[0];
    }

    private int[] LA(int v, int w) {
      int[] LA = new int[2];
      int dist = INFINITY;
      int ancestor = -1;

      BreadthFirstDirectedPaths bfs1 = new BreadthFirstDirectedPaths(G, v);
      BreadthFirstDirectedPaths bfs2 = new BreadthFirstDirectedPaths(G, w);

      for (int vertex = 0; vertex < G.V(); vertex++) {
        if (bfs1.hasPathTo(vertex) && bfs2.hasPathTo(vertex)) {
          if (bfs1.distTo(vertex) + bfs2.distTo(vertex) < dist) {
            dist = bfs1.distTo(vertex) + bfs2.distTo(vertex);
            ancestor = vertex;
          }
        }
      }
      LA[0] = ancestor;
      LA[1] = (dist == INFINITY ? -1 : dist);
      return LA;
    }

    private int[] LA(Iterable<Integer> v, Iterable<Integer> w) {
      int[] LA = new int[2];
      int dist = INFINITY;
      int ancestor = -1;

      BreadthFirstDirectedPaths bfs1 = new BreadthFirstDirectedPaths(G, v);
      BreadthFirstDirectedPaths bfs2 = new BreadthFirstDirectedPaths(G, w);

      for (int vertex = 0; vertex < G.V(); vertex++) {
        if (bfs1.hasPathTo(vertex) && bfs2.hasPathTo(vertex)) {
          if (bfs1.distTo(vertex) + bfs2.distTo(vertex) < dist) {
            dist = bfs1.distTo(vertex) + bfs2.distTo(vertex);
            ancestor = vertex;
          }
        }
      }
      LA[0] = ancestor;
      LA[1] = (dist == INFINITY ? -1 : dist);
      return LA;
    }

   // do unit testing of this class
   public static void main(String[] args) {
       In in = new In(args[0]);
       Digraph G = new Digraph(in);
       SAP sap = new SAP(G);
       while (!StdIn.isEmpty()) {
           int v = StdIn.readInt();
           int w = StdIn.readInt();
           int length   = sap.length(v, w);
           int ancestor = sap.ancestor(v, w);
           StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
       }
   }
}