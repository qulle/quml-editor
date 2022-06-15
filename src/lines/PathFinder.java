package lines;

import chartobjects.Connector;
import java.awt.Rectangle;
import java.awt.geom.Line2D;

/**
 * Class responsible for generating the Line2D path between two connector objects
 */
final class PathFinder {
    private PathFinder() {}

    private static final int x = 0;
    private static final int y = 1;

    private static Line2D line = new Line2D.Double();

    private static ChartLine chartLine;
    private static Connector sC;
    private static Connector eC;
    private static Rectangle r1;
    private static Rectangle r2;

    private static int verticalDistance;
    private static int horizontalDistance;
    private static int negVertical;
    private static int negHorizontal;

    private static int[] sCOffset;
    private static int[] eCOffset;


    static Line2D[] findPath(ChartLine chartLine) {
        PathFinder.chartLine = chartLine;

        sC = chartLine.getStartConnector();
        eC = chartLine.getEndConnector();

        r1 = sC.getChartObject().getBounds();
        r2 = eC.getChartObject().getBounds();

        sCOffset = Connector.getOffsets(sC);
        eCOffset = Connector.getOffsets(eC);

        switch(chartLine.getLineType()) {
            case STRAIGHT:
                return straightPath();
            case SEMI:
                return semiPath();
            case BENT:
                return bentPath();
            default:
                return straightPath();
        }
    }

    private static boolean checkIfLineSegmentIsNull(Line2D[] path) {
        for(Line2D line : path) {
            if(line == null) {
                return true;
            }
        }

        return false;
    }

    private static Line2D[] getLine(final int n) {
        Line2D[] path = chartLine.getPath();

        if(path.length != n || checkIfLineSegmentIsNull(path)) {
            path = new Line2D.Double[n];

            for(int i = 0; i < path.length; ++i) {
                path[i] = new Line2D.Double();
            }
        }

        return path;
    }

    private static Line2D[] straightPath() {
        Line2D[] path = getLine(1);
        path[0].setLine(
            (float)(sC.getCenterX()),
            (float)(sC.getCenterY()),
            (float)(eC.getCenterX()),
            (float)(eC.getCenterY())
        );

        return path;
    }

    private static Line2D[] semiPath() {
        Line2D[] path = getLine(3);

        path[0].setLine(
            (float)(sC.getCenterX()),
            (float)(sC.getCenterY()),
            (float)(sC.getCenterX() + sCOffset[x]),
            (float)(sC.getCenterY() + sCOffset[y])
        );

        path[2].setLine(
            (float)(eC.getCenterX() + eCOffset[x]),
            (float)(eC.getCenterY() + eCOffset[y]),
            (float)(eC.getCenterX()),
            (float)(eC.getCenterY())
        );

        path[1].setLine(
            path[0].getX2(),
            path[0].getY2(),
            path[2].getX1(),
            path[2].getY1()
        );

        return path;
    }

    // Checks if it is two parallel sides that is to be connected
    private static boolean isParallel() {
        return Math.abs(sCOffset[x]) == Math.abs(eCOffset[x]);
    }
    private static boolean isParallel(int n) {
        return sCOffset[n] != 0 && eCOffset[n] != 0;
    }

    private static Line2D[] bentPath() {
        // Calc the distance between connectors
        horizontalDistance = Math.abs(sC.getX() - eC.getX());
        verticalDistance = Math.abs(sC.getY() - eC.getY());

        negHorizontal = ((sCOffset[x] < 0) || (eCOffset[x] < 0) ? -1 : 1);
        negVertical   = ((sCOffset[y] < 0) || (eCOffset[y] < 0) ? -1 : 1);

        Line2D[] path;
        // Check if 0,1 or 2 intersections
        line.setLine(
            (float)(sC.getCenterX()),
            (float)(sC.getCenterY()),
            (float)(eC.getCenterX()),
            (float)(eC.getCenterY())
        );

        if(line.intersects(r1) && line.intersects(r2)) {
            path = bentTwoIntersections();
        }else if(line.intersects(r1) || line.intersects(r2)) {
            // Hysteresis check
            if((horizontalDistance < 100 || verticalDistance < 100) && (!isParallel(x) && !isParallel(y))) {
                path = bentTwoIntersections();
            }else {
                path = bentOneIntersection();
            }
        }else {
            // Hysteresis check
            if(!isParallel(x) && !isParallel(y)) {
                if(verticalDistance < 50 || horizontalDistance < 50) {
                    path = bentOneIntersection();
                }else {
                    path = bentZeroIntersections();
                }
            }else {
                if((horizontalDistance < 100 && isParallel(x)) || (verticalDistance < 100 && isParallel(y))) {
                    path = bentTwoIntersections();
                }else {
                    path = bentZeroIntersections();
                }
            }
        }

        return path;
    }

    private static Line2D[] bentZeroIntersections() {
        Line2D[] path;

        int[] sCOffset = Connector.getOffsets(sC, horizontalDistance, verticalDistance);
        int[] eCOffset = Connector.getOffsets(eC, horizontalDistance, verticalDistance);

        if(isParallel()) {
            path = getLine(3);
            path[0].setLine(
                (float)(sC.getCenterX()),
                (float)(sC.getCenterY()),
                (float)(sC.getCenterX() + sCOffset[x] / 2),
                (float)(sC.getCenterY() + sCOffset[y] / 2)
            );

            path[2].setLine(
                (float)(eC.getCenterX() + eCOffset[x] / 2),
                (float)(eC.getCenterY() + eCOffset[y] / 2),
                (float)(eC.getCenterX()),
                (float)(eC.getCenterY())
            );

            path[1].setLine(
                path[0].getX2(),
                path[0].getY2(),
                path[2].getX1(),
                path[2].getY1()
            );
        }else {
            path = getLine(2);
            path[0].setLine(
                (float)(sC.getCenterX()),
                (float)(sC.getCenterY()),
                (float)(sC.getCenterX() + sCOffset[x]),
                (float)(sC.getCenterY() + sCOffset[y])
            );

            path[1].setLine(
                path[0].getX2(),
                path[0].getY2(),
                (float)(eC.getCenterX()),
                (float)(eC.getCenterY())
            );
        }

        return path;
    }

    private static Line2D[] fourBendsHelp() {
        Line2D[] path = getLine(4);
        path[0].setLine(
            (float)(sC.getCenterX()),
            (float)(sC.getCenterY()),
            (float)(sC.getCenterX() + sCOffset[x]),
            (float)(sC.getCenterY() + sCOffset[y])
        );

        path[3].setLine(
            (float)(eC.getCenterX() + eCOffset[x]),
            (float)(eC.getCenterY() + eCOffset[y]),
            (float)(eC.getCenterX()),
            (float)(eC.getCenterY())
        );

        path[1].setLine(
            path[0].getX2(),
            path[0].getY2(),
            sCOffset[x] != 0 ? path[0].getX2() : path[3].getX1(),
            sCOffset[y] != 0 ? path[0].getY2() : path[3].getY1()
        );

        path[2].setLine(
            path[1].getX2(),
            path[1].getY2(),
            path[3].getX1(),
            path[3].getY1()
        );
        return path;
    }

    private static Line2D[] bentOneIntersection() {
        // Check which was intersected
        Connector bottomC = (line.intersects(r1) ? sC : eC);
        Connector topC = (bottomC.equals(sC) ? eC : sC);

        Line2D[] path;

        if(isParallel()) {
            path = getLine(3);
            path[0].setLine(
                (float)(sC.getCenterX()),
                (float)(sC.getCenterY()),
                (float)(sC.getCenterX() + sCOffset[x] + (sC.equals(topC) && sCOffset[x] != 0 ? negHorizontal * horizontalDistance : 0)),
                (float)(sC.getCenterY() + sCOffset[y] + (sC.equals(topC) && sCOffset[y] != 0 ? negVertical * verticalDistance : 0))
            );

            path[2].setLine(
                (float)(eC.getCenterX() + eCOffset[x] + (eC.equals(topC) && eCOffset[x] != 0 ? negHorizontal * horizontalDistance : 0)),
                (float)(eC.getCenterY() + eCOffset[y] + (eC.equals(topC) && eCOffset[y] != 0 ? negVertical * verticalDistance : 0)),
                (float)(eC.getCenterX()),
                (float)(eC.getCenterY())
            );

            path[1].setLine(
                path[0].getX2(),
                path[0].getY2(),
                path[2].getX1(),
                path[2].getY1()
            );
        }else {
            path = fourBendsHelp();
        }

        return path;
    }

    private static Line2D[] bentTwoIntersections() {
        Line2D[] path;

        if(isParallel()) {
            path = getLine(5);
            path[0].setLine(
                (float)(sC.getCenterX()),
                (float)(sC.getCenterY()),
                (float)(sC.getCenterX() + sCOffset[x]),
                (float)(sC.getCenterY() + sCOffset[y])
            );

            path[4].setLine(
                (float)(eC.getCenterX() + eCOffset[x]),
                (float)(eC.getCenterY() + eCOffset[y]),
                (float)(eC.getCenterX()),
                (float)(eC.getCenterY())
            );

            path[1].setLine(
                path[0].getX2(),
                path[0].getY2(),
                sCOffset[x] != 0 ? path[0].getX2() : path[0].getX2() + (horizontalDistance / 2.0) * (sC.getX() > eC.getX() ? -1 : 1),
                sCOffset[y] != 0 ? path[0].getY2() : path[0].getY2() + (verticalDistance / 2.0) * (sC.getY() > eC.getY() ? -1 : 1)
            );

            path[3].setLine(
                sCOffset[x] != 0 ? path[4].getX1() : path[4].getX1() + (horizontalDistance / 2.0) * (eC.getX() > sC.getX() ? -1 : 1),
                sCOffset[y] != 0 ? path[4].getY1() : path[4].getY1() + (verticalDistance / 2.0) * (eC.getY() > sC.getY() ? -1 : 1),
                path[4].getX1(),
                path[4].getY1()
            );

            path[2].setLine(
                path[1].getX2(),
                path[1].getY2(),
                path[3].getX1(),
                path[3].getY1()
            );
        }else {
            path = fourBendsHelp();
        }

        return path;
    }
}