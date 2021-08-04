package battleship;

public class Coordinates {

    private static final String letters = "ABCDEFGHIJ";

    private final int x;
    private final int y;

    public Coordinates(String coordinatesXY) {
        String[] coordinates = coordinatesXY.split("", 2);
        this.x = letters.indexOf(coordinates[0]);
        this.y = Integer.parseInt(coordinates[1]) - 1;
    }

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Coordinates directionTo(Coordinates coordinates){
        // To: Up(0, -1), Down(0, 1), Right(1, 0), Left(-1, 0)
        return new Coordinates(Integer.signum(coordinates.getX() - x), Integer.signum(coordinates.getY() - y));
    }

    public static int distanceBetween(Coordinates c1, Coordinates c2) {
        // Distance between two vectors
        int x12 = Math.abs(c1.getX() - c2.getX());
        int y12 = Math.abs(c1.getY() - c2.getY());
        return x12 + y12;
    }

}
