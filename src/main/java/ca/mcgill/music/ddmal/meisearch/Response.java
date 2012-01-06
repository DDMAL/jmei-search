package ca.mcgill.music.ddmal.meisearch;

/**
 * A single response has 4 points.
 */
public class Response {

    public final Point ul;
    public final Point ll;
    public final Point ur;
    public final Point lr;

    public Response(Point ul, Point ll, Point ur, Point lr) {
        this.ul = ul;
        this.ll = ll;
        this.ur = ur;
        this.lr = lr;
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
    }
}
