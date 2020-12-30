package chap02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Basic vector manipulation
 *
 * C'est moi qui l'ai fait.
 */
public class VectorUtils {

    /**
     * A 2D Vector
     */
    public static class Vector {
        private double x;
        private double y;
        public Vector() {}
        public Vector(double x, double y) {
            this.x = x;
            this.y = y;
        }
        public Vector x(double x) {
            this.x = x;
            return this;
        }
        public Vector y(double y) {
            this.y = y;
            return this;
        }
        public double getX() {
            return this.x;
        }
        public double getY() {
            return this.y;
        }

        @Override
        public String toString() {
            return(String.format("X:%f, Y:%f", this.x, this.y));
        }
    }

//    def subtract(v1,v2):
//            return (v1[0] - v2[0], v1[1] - v2[1])
    public static Vector subtract(Vector one, Vector two) {
        return new Vector(one.getX() - two.getX(), one.getY() - two.getY());
    }

//    def add(*vectors):
//            return (sum([v[0] for v in vectors]), sum([v[1] for v in vectors]))
    public static Vector add(List<Vector> vectors) {
        return new Vector(vectors.stream().mapToDouble(Vector::getX).sum(),
                          vectors.stream().mapToDouble(Vector::getY).sum());
    }

//    def length(v):
//            return sqrt(v[0]**2 + v[1]**2)
    public static double length(Vector v) {
        return Math.sqrt(Math.pow(v.getX(), 2) + Math.pow(v.getY(), 2));
    }

//    def distance(v1,v2):
//            return length(subtract(v1,v2))
    public static double distance(Vector v1, Vector v2) {
        return length(subtract(v1, v2));
    }

//    def perimeter(vectors):
//    distances = [distance(vectors[i], vectors[(i+1)%len(vectors)])
//            for i in range(0,len(vectors))]
//            return sum(distances)
    public static double perimeter(List<Vector> vectors) {
        double dist = 0d;
        for (int i=0; i<vectors.size(); i++) {
            dist += distance(vectors.get(i), vectors.get((i+1) % vectors.size()));
        }
        return dist;
    }

//    def scale(scalar,v):
//            return (scalar * v[0], scalar * v[1])
    public static Vector scale(double scalar, Vector v) {
        return new Vector(scalar * v.getX(), scalar * v.getY());
    }

//    def to_cartesian(polar_vector):
//    length, angle = polar_vector[0], polar_vector[1]
//            return (length*cos(angle), length*sin(angle))
    /**
     *
     * @param length as you can guess
     * @param angle in <u><i>Radians</i></u>
     * @return the cartesian equivalent of the polar coordinates
     */
    public static Vector toCartesian(double length, double angle) {
        return new Vector(length * Math.cos(angle), length * Math.sin(angle));
    }

//    def translate(translation, vectors):
//            return [add(translation, v) for v in vectors]
    public static Vector translate(Vector translation, Vector vector) {
        return translate(translation, Arrays.asList(vector)).stream().findFirst().get();
    }
    public static List<Vector> translate(Vector translation, List<Vector> vectors) {
        List<Vector> translated = new ArrayList<>();
        vectors.forEach(v -> translated.add(add(Arrays.asList(translation, v))));
        return translated;
    }

//    def to_polar(vector):
//        x, y = vector[0], vector[1]
//        angle = atan2(y,x)
//        return (length(vector), angle)
    public static Vector toPolar(Vector vector) {
        double angle = Math.atan2(vector.getY(), vector.getX()); // Warning! Y first, then X
        return new Vector(length(vector), angle);
    }

//    def rotate(angle, vectors):
//      polars = [to_polar(v) for v in vectors]
//            return [to_cartesian((l, a+angle)) for l,a in polars]
    public static Vector rotate(double angle, Vector vector) {
        return rotate(angle, Arrays.asList(vector)).stream().findFirst().get();
    }
    public static List<Vector> rotate(double angle, List<Vector> vectors) {
        List<Vector> rotated = new ArrayList<>();
        vectors.forEach(v -> rotated.add(toCartesian(v.getX(), v.getY() + angle)));
        return rotated;
    }

    /**
     * This is for tests.
     * @param args Not used.
     */
    public static void main(String... args) {
        Vector one = new Vector(1, 2);
        Vector two = new Vector(3, 4);

        double perimeter = perimeter(Arrays.asList(one, two));
        System.out.println("Perimeter: " + perimeter);
        Vector vector = toPolar(one);
        System.out.println("To Polar:" + vector.toString());

        Vector translation = new Vector(5, 6);
        List<Vector> translated = translate(translation, Arrays.asList(one, two));
        translated.forEach(System.out::println);

        Vector backToBase = translate(new Vector().x(-5).y(-6), translated.get(0));
        System.out.println("Back to One: " + backToBase);

        List<Vector> rotated = rotate(Math.toRadians(45), Arrays.asList(one, two));
        rotated.forEach(v -> System.out.printf("Rotated: %s%n", v));

        Vector rotateOne = rotate(Math.PI * 2, one);
        System.out.println("Rotated one: " + rotateOne);

        Vector scaled = scale(4.56, one);
        System.out.println("Scaled: " + scaled);
    }
}
