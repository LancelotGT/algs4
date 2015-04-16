import java.util.Scanner;
import java.util.Comparator;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;

public class KdTree {

    /**
     * Compares two points by x-coordinate.
     */
    private static final Comparator<Point2D> X_ORDER = new XOrder();

    /**
     * Compares two points by y-coordinate.
     */
    private static final Comparator<Point2D> Y_ORDER = new YOrder();

	private static class Node{
		private Point2D p;
		private RectHV rect;
		private Node left;
		private Node right;

		private Node(Point2D p) {
			this.p = p;
		}
	}

    // compare points according to their x-coordinate
    private static class XOrder implements Comparator<Point2D> {
        public int compare(Point2D p, Point2D q) {
            if (p.x() < q.x()) return -1;
            if (p.x() > q.x()) return +1;
            return 0;
        }
    }

    // compare points according to their y-coordinate
    private static class YOrder implements Comparator<Point2D> {
        public int compare(Point2D p, Point2D q) {
            if (p.y() < q.y()) return -1;
            if (p.y() > q.y()) return +1;
            return 0;
        }
    }

	private Node root;
	private int size;

	// construct an empty set of points
    // public KdTree() {
    // }

    // is the set empty?
    public boolean isEmpty() {
    	return size() == 0;
    }

    // number of points in the set
    public int size() {
        return this.size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (contains(p)) return;
    	root = insert(root, p, 0);
        size++;
    }

    // a private helper method for insertion
    private Node insert(Node x, Point2D p, int height) {
        if (x == null && height == 0) {
            Node node = new Node(p);
            node.rect = new RectHV(0, 0, 1, 1);
            return node;
        }

        if (x == null) {
            return new Node(p);
        }

        if (height % 2 == 0) {
            int cmp = less(KdTree.X_ORDER, p, x.p);
            if (cmp < 0) {
                x.left = insert(x.left, p, ++height);
                x.left.rect = new RectHV(x.rect.xmin(), x.rect.ymin(), x.p.x(), x.rect.ymax());
            } else {
                x.right = insert(x.right, p, ++height);
                x.right.rect = new RectHV(x.p.x(), x.rect.ymin(), x.rect.xmax(), x.rect.ymax());
            }
        } else {
            int cmp = less(KdTree.Y_ORDER, p, x.p);
            if (cmp < 0) {
                x.left = insert(x.left, p, ++height);
                x.left.rect = new RectHV(x.rect.xmin(), x.rect.ymin(), x.rect.xmax(), x.p.y());
            } else {
                x.right = insert(x.right, p, ++height);
                x.right.rect = new RectHV(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.rect.ymax());
            }
        }

        return x;
    }

    private int less(Comparator c, Point2D p, Point2D q) {
        return c.compare(p, q);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return contains(root, p, 0);
    }

    // private helper method for contains
    private boolean contains(Node x, Point2D p, int height) {
        if (x == null) return false;
        if (height % 2 == 0) {
            int cmp = less(KdTree.X_ORDER, p, x.p);
            if (cmp < 0) return contains(x.left, p, ++height);
            else if (cmp > 0) return contains(x.right, p, ++height);
            else {
                if (less(KdTree.Y_ORDER, p, x.p) == 0) return true;
                else return contains(x.right, p, ++height);
            }
        } else {
            int cmp = less(KdTree.Y_ORDER, p, x.p);
            if (cmp < 0) return contains(x.left, p, ++height);
            else if (cmp > 0) return contains(x.right, p, ++height);
            else {
                if (less(KdTree.X_ORDER, p, x.p) == 0) return true;
                else return contains(x.right, p, ++height);
            }
        }
    }
   
   	// draw all points to standard draw
    public void draw() {
        root.rect.draw();
        draw(root, 0);
    }

    private void draw(Node x, int height) {
        if (x == null) return;
        System.out.println(x.p);
        System.out.printf("Height: %d\n", height);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        x.p.draw();
        StdDraw.setPenRadius();
        if (height % 2 == 1) {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());
        } else {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());
        }
        height++;
        draw(x.left, height);
        draw(x.right, height);
    }

    public Iterable<Point2D> range(RectHV rect) {
    	//all points that are inside the rectangle
        List<Point2D> r = new ArrayList<Point2D>();
        range(root, rect, r);
        return r;
    }

    private void range(Node x, RectHV rect, List<Point2D> r) {
        if (x == null) return;
        if (rect.contains(x.p)) r.add(x.p);
        if (x.left != null && x.left.rect.intersects(rect)) {
            range(x.left, rect, r);
        }
        if (x.right != null && x.right.rect.intersects(rect)) {
            range(x.right, rect, r);
        }
    }

    private boolean horizontalIntersection(Node x, RectHV rect) {
        if (x.p.y() > rect.ymax() || x.p.y() < rect.ymin()) return false;
        if (x.rect.xmax() < rect.xmin()) return false;
        if (x.rect.xmin() > rect.xmax()) return false;
        return true;
    }

    private boolean verticalIntersection(Node x, RectHV rect) {
        if (x.p.x() > rect.xmax() || x.p.x() < rect.xmin()) return false;
        if (x.rect.ymax() < rect.ymin()) return false;
        if (x.rect.ymin() > rect.ymax()) return false;
        return true;
    }

    private Point2D nearestPoint;
    // private int steps;
    //a nearest neighbor in the set to point p; null if the set is empty 
    public Point2D nearest(Point2D p) {
        if (root == null) return null;
        nearestPoint = root.p;
        nearest(root, p);
        return nearestPoint;
    }

    private void nearest(Node x, Point2D p) {
        if (x == null) return;
        
        if (x.p.distanceSquaredTo(p) < nearestPoint.distanceSquaredTo(p)) {
            nearestPoint = x.p;
        }

        if (x.left == null && x.right == null) return;
        else if (x.left == null) {
            nearest(x.right, p);
        } else if (x.right == null) {
            nearest(x.left, p);
        } else {
            double nearestDist = p.distanceSquaredTo(nearestPoint);
            double leftDist = x.left.rect.distanceSquaredTo(p);
            double rightDist = x.right.rect.distanceSquaredTo(p);
            // prune left or right subtree
            if (rightDist > nearestDist) {
                nearest(x.left, p);
            } else if (leftDist > nearestDist) {
                nearest(x.right, p);
            } else {
                if (x.left.rect.contains(p)) {
                    nearest(x.left, p);
                    nearest(x.right, p);
                } else {
                    nearest(x.right, p);
                    nearest(x.left, p);
                }
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
    	// unit testing of the methods (optional) 
        File file = new File(args[0]);
        Scanner sc = new Scanner(file);
        KdTree pset = new KdTree();
        while (true) {
            double n1 = 0;
            double n2 = 0;
            if (sc.hasNext()) n1 = sc.nextDouble();
            if (sc.hasNext()) n2 = sc.nextDouble();
            else break;
            Point2D p = new Point2D(n1, n2);
            System.out.println(p);
            pset.insert(p);
        }
        sc.close();

        Scanner sc2 = new Scanner(file);
        while (true) {
            double n1 = 0;
            double n2 = 0;
            if (sc2.hasNext()) n1 = sc2.nextDouble();
            if (sc2.hasNext()) n2 = sc2.nextDouble();
            else break;
            Point2D p = new Point2D(n1, n2);
            System.out.println(p);
            System.out.println(pset.contains(p));
        }
        System.out.println(pset.contains(new Point2D(1, 1)));
        pset.draw();
        System.out.println(pset.nearest(new Point2D(0.81, 0.30)));
    }

    // public static void main(String[] args) {
    //     KdTree tree = new KdTree();
    //     tree.insert(new Point2D(0.7, 0.2));
    //     tree.insert(new Point2D(0.5, 0.4));
    //     tree.insert(new Point2D(0.2, 0.3));
    //     tree.insert(new Point2D(0.4, 0.7));
    //     tree.insert(new Point2D(0.9, 0.6));
    //     tree.draw();
    // }
}
