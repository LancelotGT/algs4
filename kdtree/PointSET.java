import java.util.*;

public class PointSET {

    private RectHV rect;
    private SET<Point2D> set;

    public PointSET() {
        // construct an empty set of points 
	    this.rect = new RectHV(0, 0, 1, 1);
	    this.set = new SET<Point2D>();
    }   

    public boolean isEmpty() {
    	// is the set empty?
        return set.size() == 0;
    }

    public int size() {
   	    // number of points in the set
        return set.size();
    }

    public void insert(Point2D p) {
        // add the point to the set (if it is not already in the set)
        if (p == null)
            throw new RuntimeException("called insert() with null.");
        if (!rect.contains(p))
            throw new RuntimeException("The rectangle does not contain this point.");
        set.add(p);
    }             
    public boolean contains(Point2D p) {
    	// does the set contain point p?
        return set.contains(p);
    }            
   
    public void draw() {
        // draw all points to standard draw
        this.rect.draw();
        for (Point2D p : set)
            p.draw();
    }

    public Iterable<Point2D> range(RectHV rect) {
    	// all points that are inside the rectangle
        List<Point2D> r = new ArrayList<Point2D>();
        for (Point2D p : set) {
            if (rect.contains(p))
                r.add(p);
        }
        return r;
    }

    public Point2D nearest(Point2D p) {
    	// a nearest neighbor in the set to point p; null if the set is empty 
        if (set.isEmpty())
            return null;

        Iterator<Point2D> iter = set.iterator();
        Point2D minPoint = iter.next();
        double minDist = minPoint.distanceTo(p);
        
        while (iter.hasNext()) {
            Point2D p2 = iter.next();
            double dist = p2.distanceTo(p);
            if (dist < minDist) {
                minPoint = p2;
                minDist = dist;
            }
        }

        return minPoint;
    }

    public static void main(String[] args) {
    	// unit testing of the methods (optional) 
        Scanner sc = new Scanner(System.in);
        PointSET pset = new PointSET();
        while (true) {
            double n1 = 0;
            double n2 = 0;
            if (sc.hasNext()) n1 = sc.nextDouble();
            if (sc.hasNext()) n2 = sc.nextDouble();
            else break;
            pset.insert(new Point2D(n1, n2));
        }
        pset.draw();
        Point2D p4 = new Point2D(0.5, 0.5);
        Point2D p = pset.nearest(p4);
        System.out.println(p.toString());
    }
}
