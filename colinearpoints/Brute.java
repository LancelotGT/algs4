/*************************************************************************
 * Name: Ning Wang
 * Email: glningwang@gmail.com
 *
 * Compilation:  javac Brute.java
 * Execution: java Brute.java input.txt
 * Dependencies: StdDraw.java
 *
 * Description: Take some points as input and use brute-force algorithm to check whether four points
 * are in the same line.
 *
 *************************************************************************/

import java.util.Arrays;

public class Brute {   
    
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
        
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                for (int m = 0; m < N; m++) {
                    for (int n = 0; n < N; n++) {
                        if (i == j || i == m || i == n || j == m || j == n || m == n) continue;
                        if ((points[i].slopeTo(points[j]) == points[i].slopeTo(points[m])) && (points[i].slopeTo(points[j]) == points[i].slopeTo(points[n]))) {
                            if (points[i].compareTo(points[j]) < 0 && points[j].compareTo(points[m]) < 0 && points[m].compareTo(points[n]) < 0) {
                                points[i].drawTo(points[n]);
                                StdOut.println(points[i] + " -> " + points[j] + " -> " + points[m] + " -> " + points[n]);
                            }
                        }
                    }
                }
            }
        }
        
        StdDraw.show(0);
        StdDraw.setPenRadius();
    }
}