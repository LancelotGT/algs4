import java.awt.Color;

public class SeamCarver {

    private Picture picture;
    private int width;
    private int height;
    private final int BORDERENERGY = 195075;
    private Tuple[][] edgeTo;
    private double[][] distTo;
    private double[][] energy;
    private int[][] rgb;

    // implement a tuple embedded class for passing values
    private static class Tuple {
        private final int x;
        private final int y;
        private Tuple(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public String toString() {
            return "(" + x + "," + y + ")";
        }

        public boolean equals(Tuple that) {
            if (that == null) {
                return false;
            }
            if (that == this) {
                return true;
            }
            return that.x == this.x && that.y == this.y;
        }
    }

    // create a seam carver object based on the given picture    
    public SeamCarver(Picture picture) {
        this.picture = picture;
        this.width = picture.width();
        this.height = picture.height();
        energy = new double[height][width];
        rgb = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                energy[i][j] = energy(j, i);
                rgb[i][j] = this.picture.get(j, i).getRGB();
            }
        }
    }

    // current picture
    public Picture picture() {
        return picture;
    }                         

    // width of current picture    
    public     int width() {
        return this.width;
    }                           

    // height of current picture    
    public     int height() {
        return this.height;
    }                          

    // energy of pixel at column x and row y    
    public  double energy(int x, int y) {
        if (x < 0 || x > width - 1 || y < 0 || y > height - 1)
            throw new IndexOutOfBoundsException();
        if (x == 0 || x == (width() - 1) || y == 0 || y == (height() - 1)) {
            return BORDERENERGY;
        }
        return xGradient(x, y) + yGradient(x, y);
    }

    private int xGradient(int x, int y) {
        Color color1 = picture.get(x - 1, y);
        Color color2 = picture.get(x + 1, y);
        int RX = color1.getRed() - color2.getRed();
        int BX = color1.getBlue() - color2.getBlue();
        int GX = color1.getGreen() - color2.getGreen();
        return RX * RX + BX * BX + GX * GX;
    }

    private int yGradient(int x, int y) {
        Color color1 = picture.get(x, y - 1);
        Color color2 = picture.get(x, y + 1);
        int RY = color1.getRed() - color2.getRed();
        int BY = color1.getBlue() - color2.getBlue();
        int GY = color1.getGreen() - color2.getGreen();
        return RY * RY + BY * BY + GY * GY;
    }

    // sequence of indices for horizontal seam    
    public   int[] findHorizontalSeam() {
        edgeTo = new Tuple[height][width];
        distTo = new double[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                distTo[i][j] = Double.POSITIVE_INFINITY;
                edgeTo[i][j] = null;
            }
        }

        for (int i = 0; i < height; i++) {
            distTo[i][0] = energy(0, i);
        }

        for (int i = 1; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (j - 1 >= 0)
                    relax(new Tuple(i - 1, j - 1), new Tuple(i, j));
                relax(new Tuple(i - 1, j), new Tuple(i, j));
                if (j + 1 <= height - 1)
                    relax(new Tuple(i - 1, j + 1), new Tuple(i, j)); 
                // print
                // for (int n = 0; n < height; n++) {
                //     for (int m = 0; m < width; m++) {
                //         System.out.print(distTo[n][m] + " ");
                //     }
                //     System.out.println();
                // }
                // System.out.println();

                // for (int n = 0; n < height; n++) {
                //     for (int m = 0; m < width; m++) {
                //         System.out.print(edgeTo[n][m] + " ");
                //     }
                //     System.out.println();
                // }
                // System.out.println();
            }
        }

        int shortest = 0;
        for (int i = 0; i < height; i++) {
            if (distTo[i][width - 1] < distTo[shortest][width - 1]) {
                shortest = i;
            }
        }

        // reconstruct reverse shortest path
        int[] path = new int[width];
        Tuple current = new Tuple(width - 1, shortest);
        int k = 0;
        path[k++] = current.y;
        // System.out.println(current);
        while (edgeTo[current.y][current.x] != null) {
            // System.out.println(k);
            Tuple parent = edgeTo[current.y][current.x];
            // System.out.println(parent);
            path[k++] = parent.y;
            current = parent;
        }

        // reverse back path
        for (int i = 0; i <= (width - 1) / 2; i++) {
            int temp = path[i];
            path[i] = path[width - i - 1];
            path[width - i - 1] = temp;
        }

        return path;
    }

    // sequence of indices for vertical seam    
    public   int[] findVerticalSeam() {
        edgeTo = new Tuple[height][width];
        distTo = new double[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                distTo[i][j] = Double.POSITIVE_INFINITY;
                edgeTo[i][j] = null;
            }
        }

        for (int i = 0; i < width; i++) {
            distTo[0][i] = energy(i, 0);
        }

        for (int i = 1; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (j - 1 >= 0)
                    relax(new Tuple(j - 1, i - 1), new Tuple(j, i));
                relax(new Tuple(j, i - 1), new Tuple(j, i));
                if (j + 1 <= width - 1)
                    relax(new Tuple(j + 1, i - 1), new Tuple(j, i)); 

                // for (int n = 0; n < height; n++) {
                //     for (int m = 0; m < width; m++) {
                //         System.out.print(distTo[n][m] + " ");
                //     }
                //     System.out.println();
                // }
                // System.out.println();

                // for (int n = 0; n < height; n++) {
                //     for (int m = 0; m < width; m++) {
                //         System.out.print(edgeTo[n][m] + " ");
                //     }
                //     System.out.println();
                // }
                // System.out.println();
            }
        }

        int shortest = 0;
        for (int i = 0; i < width; i++) {
            if (distTo[height - 1][i] < distTo[height - 1][shortest]) {
                shortest = i;
            }
        }

        // reconstruct reverse shortest path
        int[] path = new int[height];
        Tuple current = new Tuple(shortest, height - 1);
        int k = 0;
        path[k++] = current.x;
        
        while (edgeTo[current.y][current.x] != null) {
            Tuple parent = edgeTo[current.y][current.x];
            path[k++] = parent.x;
            // System.out.println(parent);
            current = parent;
        }

        // for (int i = 0; i < path.length; i++) {
        //     System.out.print(path[i] + " ");
        // }
        // System.out.println();

        // reverse back path
        for (int i = 0; i <= (height - 1) / 2; i++) {
            int temp = path[i];
            path[i] = path[height - i - 1];
            path[height - i - 1] = temp;
        }

        // for (int i = 0; i < path.length; i++) {
        //     System.out.print(path[i] + " ");
        // }
        // System.out.println();        
        return path;
    }

    // relax edge between two points
    private void relax(Tuple from, Tuple to) {
        int v_x = from.x;
        int v_y = from.y;
        int w_x = to.x;
        int w_y = to.y;
        if (distTo[w_y][w_x] > distTo[v_y][v_x] + energy[v_y][v_x]) {
            distTo[w_y][w_x] = distTo[v_y][v_x] + energy[v_y][v_x];
            edgeTo[w_y][w_x] = from;
        }
    }

    // remove horizontal seam from current picture    
    public    void removeHorizontalSeam(int[] seam) {
        if (seam == null)
            throw new NullPointerException();
        if (width <= 1)
            throw new IllegalArgumentException();

        // copy the temp_rgb after removing to original rgb array
        int[][] temp_rgb = new int[height - 1][width];
        double[][] new_energy = new double[height - 1][width];
        for (int i = 0; i < width; i++) {
            if (seam[i] < 0 || seam[i] > height - 1)
                throw new IllegalArgumentException();

            int k = 0;
            for (int j = 0; j < height; j++) {
                if (j != seam[i]) {
                    temp_rgb[k][i] = rgb[j][i];
                    new_energy[k][i] = energy[j][i];
                    k++;
                }
            }
        }

        rgb = temp_rgb;

        // update picture
        picture = new Picture(width, height - 1);
        for (int i = 0; i < height - 1; i++) {
            for (int j = 0; j < width; j++) {
                picture.set(j, i, new Color(temp_rgb[i][j]));
            }
        }

        height -= 1;
        // update energy array
        for (int i = 0; i < width; i++) {
            if (seam[i] > 0) {
                new_energy[seam[i] - 1][i] = energy(i, seam[i] - 1);
            }
            if (seam[i] < height) {
                new_energy[seam[i]][i] = energy(i, seam[i]);
            }
        }
        energy = new_energy;
    }

    // remove vertical seam from current picture    
    public    void removeVerticalSeam(int[] seam) {
        if (seam == null)
            throw new NullPointerException();
        if (width <= 1)
            throw new IllegalArgumentException();

        // copy the temp_rgb after removing to original rgb array
        int[][] temp_rgb = new int[height][width - 1];
        double[][] new_energy = new double[height][width - 1];
        for (int i = 0; i < height; i++) {
            if (seam[i] < 0 || seam[i] > width - 1)
                throw new IllegalArgumentException();

            int k = 0;
            for (int j = 0; j < width; j++) {
                if (j != seam[i]) {
                    temp_rgb[i][k] = rgb[i][j];
                    new_energy[i][k] = energy[i][j];
                    k++;
                }
            }
        }
        rgb = temp_rgb;

        // update picture
        picture = new Picture(width - 1, height);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width - 1; j++) {
                picture.set(j, i, new Color(temp_rgb[i][j]));
            }
        }

        width -= 1;
        // update energy array
        for (int i = 0; i < height; i++) {
            if (seam[i] > 0)
                new_energy[i][seam[i] - 1] = energy(seam[i] - 1, i);
            if (seam[i] < width)
                new_energy[i][seam[i]] = energy(seam[i], i);
        }
        energy = new_energy;
    }
}