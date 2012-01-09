package ca.mcgill.music.ddmal.meisearch;

import java.util.List;

import ca.mcgill.music.ddmal.mei.MeiElement;

// Origin is upper left

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
     * A set of elements MeiElements, find the max/min x/y coordinates to completely
     * surround them
     * @param start
     * @param end
     * @return
     */
    public static Response makeResponse(List<MeiElement> els) {
        return null;
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
