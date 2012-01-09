package ca.mcgill.music.ddmal.meisearch;

import java.util.List;

import ca.mcgill.music.ddmal.mei.MeiDocument;
import ca.mcgill.music.ddmal.mei.MeiElement;

// Origin is upper left

/**
 * A single response has 4 points.
 */
public class Response {

    /** The document this are is in. */
    public final MeiDocument doc;
    /** Location of the area. */
    public final Point ul;
    public final Point lr;

    public Response(MeiDocument doc, Point ul, Point lr) {
        this.doc = doc;
        this.ul = ul;
        this.lr = lr;
    }

    /**
     * A set of elements MeiElements, find the max/min x/y coordinates to completely
     * surround them
     * @return
     */
    public static Response makeResponse(MeiDocument doc, List<MeiElement> els) {
        int maxulx = 0;
        int maxuly = 0;
        int maxlrx = 0;
        int maxlry = 0;
        for (MeiElement e : els) {
            MeiElement neume = e.getAncestor("neume");
            String facsid = neume.getAttribute("facs");
            MeiElement facs = doc.getElementById(facsid);
            maxulx = Integer.parseInt(facs.getAttribute("ulx"));
            maxuly = Integer.parseInt(facs.getAttribute("uly"));
            maxlrx = Integer.parseInt(facs.getAttribute("lrx"));
            maxlry = Integer.parseInt(facs.getAttribute("lry"));
        }
        return new Response(doc, new Point(maxulx, maxuly), new Point(maxlrx, maxlry));
    }

    @Override
    public String toString() {
        return ul + ", " + lr;
    }

    /**
     * x,y
     */
    public static class Point {
        public final int x;
        public final int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return new StringBuilder().append("(").append(x).append(",").append(y).append(")").toString();
        }

        @Override
        public boolean equals(Object other) {
            if (other == null) {
                return false;
            }
            if (other == this) {
                return true;
            }
            if (other.getClass() != getClass()) {
                return false;
            }
            Point rhs = (Point)other;
            return rhs.x == x && rhs.y == y;
        }
    }
}
