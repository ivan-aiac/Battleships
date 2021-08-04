package battleship;

public class Ship {

    private final Coordinates a;
    private final Coordinates b;
    private int hp;

    public Ship(ShipType shipType, Coordinates a, Coordinates b) {
        this.a = a;
        this.b = b;
        hp = shipType.getSize();
    }

    public boolean isSunk() {
        return hp == 0;
    }

    public boolean wasHitBy(Coordinates projectile) {
        // distance(a, c) + distance(c, b) = distance(a, b) if true means its in range of the ship coordinates
        int distance = Coordinates.distanceBetween(a, projectile) + Coordinates.distanceBetween(projectile, b);
        if (distance == Coordinates.distanceBetween(a, b)){
            hp--;
            return true;
        }else {
            return false;
        }
    }


}
