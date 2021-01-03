package gsg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Basic vector manipulation, translated from Python.
 * <p>
 * <i>C'est MOI qui l'ai fait.</i>
 */
public class VectorUtils {

    /**
     * A 2D Vector
     */
    public static class Vector2D {
        private double x;
        private double y;

        public Vector2D() {
        }

        public Vector2D(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public Vector2D x(double x) {
            this.x = x;
            return this;
        }

        public Vector2D y(double y) {
            this.y = y;
            return this;
        }

        public double getX() {
            return this.x;
        }

        // For Polar coordinates
        public double getLength() {
            return this.x;
        }

        public double getY() {
            return this.y;
        }

        // For Polar coordinates
        public double getAngle() {
            return this.y;
        }

        @Override
        public String toString() {
            return (String.format("X:%f, Y:%f", this.x, this.y));
        }
    }

    //    def subtract(v1,v2):
//            return (v1[0] - v2[0], v1[1] - v2[1])
    public static Vector2D subtract(Vector2D one, Vector2D two) {
        return new Vector2D(one.getX() - two.getX(), one.getY() - two.getY());
    }

    //    def add(*vectors):
//            return (sum([v[0] for v in vectors]), sum([v[1] for v in vectors]))
    public static Vector2D add(List<Vector2D> vectors) {
        return new Vector2D(vectors.stream().mapToDouble(Vector2D::getX).sum(),
                vectors.stream().mapToDouble(Vector2D::getY).sum());
    }

    //    def length(v):
//            return sqrt(v[0]**2 + v[1]**2)
    public static double length(Vector2D v) {
        return Math.sqrt(Math.pow(v.getX(), 2) + Math.pow(v.getY(), 2));
    }

    //    def distance(v1,v2):
//            return length(subtract(v1,v2))
    public static double distance(Vector2D v1, Vector2D v2) {
        return length(subtract(v1, v2));
    }

    //    def perimeter(vectors):
//    distances = [distance(vectors[i], vectors[(i+1)%len(vectors)])
//            for i in range(0,len(vectors))]
//            return sum(distances)
    public static double perimeter(List<Vector2D> vectors) {
        double dist = 0d;
        for (int i = 0; i < vectors.size(); i++) {
            dist += distance(vectors.get(i), vectors.get((i + 1) % vectors.size()));
        }
        return dist;
    }

    //    def scale(scalar,v):
//            return (scalar * v[0], scalar * v[1])
    public static Vector2D scale(double scalar, Vector2D v) {
        return new Vector2D(scalar * v.getX(), scalar * v.getY());
    }

//    def to_cartesian(polar_vector):
//    length, angle = polar_vector[0], polar_vector[1]
//            return (length*cos(angle), length*sin(angle))

    /**
     * @param length as you can guess
     * @param angle  in <u><i>Radians</i></u>
     * @return the cartesian equivalent of the polar coordinates
     */
    public static Vector2D toCartesian(double length, double angle) {
        return new Vector2D(length * Math.cos(angle), length * Math.sin(angle));
    }
    public static Vector2D toCartesian(Vector2D vector) {
        return new Vector2D(vector.getLength() * Math.cos(vector.getAngle()), vector.getLength() * Math.sin(vector.getAngle()));
    }

    //    def translate(translation, vectors):
//            return [add(translation, v) for v in vectors]
    public static Vector2D translate(Vector2D translation, Vector2D vector) {
        return translate(translation, Arrays.asList(vector)).stream().findFirst().get();
    }

    public static List<Vector2D> translate(Vector2D translation, List<Vector2D> vectors) {
        List<Vector2D> translated = new ArrayList<>();
        vectors.forEach(v -> translated.add(add(Arrays.asList(translation, v))));
        return translated;
    }

    //    def to_polar(vector):
//        x, y = vector[0], vector[1]
//        angle = atan2(y,x)
//        return (length(vector), angle)
    public static Vector2D toPolar(Vector2D vector) {
        double angle = Math.atan2(vector.getY(), vector.getX()); // Warning! Y first, then X
        return new Vector2D(length(vector), angle);
    }

    //    def rotate(angle, vectors):
//      polars = [to_polar(v) for v in vectors]
//            return [to_cartesian((l, a+angle)) for l,a in polars]
    public static Vector2D rotate(double angle, Vector2D vector) {
        return rotate(angle, Arrays.asList(vector)).stream().findFirst().get();
    }

    public static List<Vector2D> rotate(double angle, List<Vector2D> vectors) {
        List<Vector2D> rotated = new ArrayList<>();
        vectors.forEach(v -> {
            Vector2D polar = toPolar(v);
            rotated.add(toCartesian(polar.getLength(), polar.getAngle() + angle));
        });
        return rotated;
    }

    public static class GraphicRange {
        private double minX;
        private double maxX;
        private double minY;
        private double maxY;

        public GraphicRange() {
            this.minX = 0;
            this.maxX = 0;
            this.minY = 0;
            this.maxY = 0;
        }

        public double getMinX() {
            return minX;
        }

        public void setMinX(double minX) {
            this.minX = minX;
        }

        public double getMaxX() {
            return maxX;
        }

        public void setMaxX(double maxX) {
            this.maxX = maxX;
        }

        public double getMinY() {
            return minY;
        }

        public void setMinY(double minY) {
            this.minY = minY;
        }

        public double getMaxY() {
            return maxY;
        }

        public void setMaxY(double maxY) {
            this.maxY = maxY;
        }

        public GraphicRange minX(double minX) {
            this.minX = minX;
            return this;
        }

        public GraphicRange maxX(double maxX) {
            this.maxX = maxX;
            return this;
        }

        public GraphicRange minY(double minY) {
            this.minY = minY;
            return this;
        }

        public GraphicRange maxY(double maxY) {
            this.maxY = maxY;
            return this;
        }
    }

    public static GraphicRange findGraphicRange(Vector2D v, Vector2D... more) {
        GraphicRange graphicRange = new GraphicRange().minX(0).maxX(0).minY(0).maxY(0);
        graphicRange.setMinX(Math.min(graphicRange.getMinX(), v.getX()));
        graphicRange.setMaxX(Math.max(graphicRange.getMaxX(), v.getX()));
        graphicRange.setMinY(Math.min(graphicRange.getMinY(), v.getY()));
        graphicRange.setMaxY(Math.max(graphicRange.getMaxY(), v.getY()));
        if (more != null) {
            for (Vector2D vv : more) {
                graphicRange.setMinX(Math.min(graphicRange.getMinX(), vv.getX()));
                graphicRange.setMaxX(Math.max(graphicRange.getMaxX(), vv.getX()));
                graphicRange.setMinY(Math.min(graphicRange.getMinY(), vv.getY()));
                graphicRange.setMaxY(Math.max(graphicRange.getMaxY(), vv.getY()));
            }
        }
        return graphicRange;
    }

    public static GraphicRange findGraphicRange(double[] x, double[] y) {
        assert (x.length == y.length);
        GraphicRange graphicRange = new GraphicRange().minX(0).maxX(0).minY(0).maxY(0);
        for (int i = 0; i < x.length; i++) {
            graphicRange.setMinX(Math.min(graphicRange.getMinX(), x[i]));
            graphicRange.setMaxX(Math.max(graphicRange.getMaxX(), x[i]));
            graphicRange.setMinY(Math.min(graphicRange.getMinY(), y[i]));
            graphicRange.setMaxY(Math.max(graphicRange.getMaxY(), y[i]));
        }
        return graphicRange;
    }

    public static GraphicRange findGraphicRange(List<Double> x, List<Double> y) {
        assert (x.size() == y.size());
        GraphicRange graphicRange = new GraphicRange().minX(0).maxX(0).minY(0).maxY(0);
        for (int i = 0; i < x.size(); i++) {
            graphicRange.setMinX(Math.min(graphicRange.getMinX(), x.get(i)));
            graphicRange.setMaxX(Math.max(graphicRange.getMaxX(), x.get(i)));
            graphicRange.setMinY(Math.min(graphicRange.getMinY(), y.get(i)));
            graphicRange.setMaxY(Math.max(graphicRange.getMaxY(), y.get(i)));
        }
        return graphicRange;
    }

    public static GraphicRange findGraphicRange(List<Vector2D> data) {
        GraphicRange graphicRange = new GraphicRange().minX(0).maxX(0).minY(0).maxY(0);
        data.forEach(v -> {
            graphicRange.setMinX(Math.min(graphicRange.getMinX(), v.getX()));
            graphicRange.setMaxX(Math.max(graphicRange.getMaxX(), v.getX()));
            graphicRange.setMinY(Math.min(graphicRange.getMinY(), v.getY()));
            graphicRange.setMaxY(Math.max(graphicRange.getMaxY(), v.getY()));
        });
        return graphicRange;
    }

    public static GraphicRange findGraphicRanges(List<List<Vector2D>> data) {
        GraphicRange graphicRange = new GraphicRange().minX(0).maxX(0).minY(0).maxY(0);
        data.forEach(vList -> {
                    vList.forEach(v -> {
                        graphicRange.setMinX(Math.min(graphicRange.getMinX(), v.getX()));
                        graphicRange.setMaxX(Math.max(graphicRange.getMaxX(), v.getX()));
                        graphicRange.setMinY(Math.min(graphicRange.getMinY(), v.getY()));
                        graphicRange.setMaxY(Math.max(graphicRange.getMaxY(), v.getY()));
                    });
        });
        return graphicRange;
    }

    /**
     * This is for tests.
     *
     * @param args Not used.
     */
    public static void main(String... args) {
        Vector2D one = new Vector2D(1, 2);
        Vector2D two = new Vector2D(3, 4);

        double perimeter = perimeter(Arrays.asList(one, two));
        System.out.println("Perimeter: " + perimeter);
        Vector2D vector = toPolar(one);
        System.out.println("To Polar:" + vector.toString());

        Vector2D translation = new Vector2D(5, 6);
        List<Vector2D> translated = translate(translation, Arrays.asList(one, two));
        translated.forEach(System.out::println);

        Vector2D backToBase = translate(new Vector2D().x(-5).y(-6), translated.get(0));
        System.out.println("Back to One: " + backToBase);

        System.out.println(String.format("Rotating 90 degrees %s and %s", one, two));
        List<Vector2D> rotated = rotate(Math.toRadians(90), Arrays.asList(one, two));
        rotated.forEach(v -> System.out.printf("Rotated: %s%n", v));

        Vector2D rotateOne = rotate(Math.PI * 2, one);
        System.out.println("Rotated one: " + rotateOne);

        Vector2D scaled = scale(4.56, one);
        System.out.println("Scaled: " + scaled);

        GraphicRange graphicRange = findGraphicRange(one, two, translation, scaled, rotateOne);
        System.out.println(String.format("X in [%.03f, %.03f], Y in [%.03f, %.03f]",
                graphicRange.getMinX(), graphicRange.getMaxX(),
                graphicRange.getMinY(), graphicRange.getMaxY()));
    }
}
