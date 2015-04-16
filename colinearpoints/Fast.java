/*************************************************************************
 * Name: Ning Wang
 * Email: glningwang@gmail.com
 *
 * Compilation:  javac Fast.java
 * Execution: java Fast.java input.txt
 * Dependencies: StdDraw.java
 *
 * Description: Take some points as input and use a fast algorithm to check whether four points
 * are in the same line.
 *
 *************************************************************************/

import java.util.Arrays;
public class Fast {
    
    public static void main(String[] args) {
        In in = new In(args[0]); // input file
        int N = in.readInt(); // N points
        Point[] points = new Point[N];
        
        // rescale coordinates and turn on animation mode
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.show(0);
        StdDraw.setPenRadius(0.01);  // make the points a bit larger

        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            Point p = new Point(x, y);
            p.draw();
            points[i] = p;
        }
        
        Arrays.sort(points);
        Point[] aux = new Point[N];
        
        for (int i = 0; i < N - 3; i++) {
           for (int j = 0; j < N; j++) aux[j] = points[j];
           Arrays.sort(aux, aux[i].SLOPE_ORDER);
           int head = 1;
           int tail = 2;
        
           while (tail < N) {
               while (tail < N && aux[0].slopeTo(aux[head]) == aux[0].slopeTo(aux[tail])) tail++;
               if (tail - head >= 3 && aux[0].compareTo(aux[head]) < 0) {
                   aux[0].drawTo(aux[tail - 1]);
                   StdOut.print(aux[0]);
                   for (int k = head; k < tail; k++) StdOut.print(" -> " + aux[k]);
                   StdOut.println("");
               }
               head = tail;
               tail = tail + 1;
           }
        }
        StdDraw.show(0);
        StdDraw.setPenRadius();
    }
}