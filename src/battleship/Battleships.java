package battleship;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Battleships {

    private static final char MISSED_TOKEN = 'M';
    private static final char STRIKE_TOKEN = 'X';
    private static final char FOW_TOKEN = '~';
    private static final char SHIP_TOKEN = 'O';
    private static final int ROWS = 10;
    private static final int COLS = 10;

    private final char[][] playerBoard;
    private final char[][] fowBoard;
    private boolean gameOver;
    private final List<Ship> ships;
    private Battleships opponent;
    private final String playerName;

    public Battleships(String playerName) {
        this.playerName = playerName;
        playerBoard = new char[ROWS][COLS];
        fowBoard = new char[ROWS][COLS];
        for (char[] row : playerBoard) {
            Arrays.fill(row, FOW_TOKEN);
        }
        for (char[] row : fowBoard) {
            Arrays.fill(row, FOW_TOKEN);
        }
        ships = new ArrayList<>(5);
        gameOver = false;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setOpponent(Battleships opponent) {
        this.opponent = opponent;
    }

    public boolean isNotGameOver() {
        return !gameOver;
    }

    public void placeShip(ShipType ship, Coordinates c1, Coordinates c2) {
        if (isOutOfBounds(c1) || isOutOfBounds(c2)) {
            throw new RuntimeException("Wrong ship location!");
        }
        // Same x = Horizontal ship, same y = Vertical ship otherwise error
        if (c1.getX() != c2.getX() && c1.getY() != c2.getY()) {
            throw new RuntimeException("Wrong ship location!");
        }
        if (Coordinates.distanceBetween(c1, c2) != ship.getSize() - 1) {
            throw new RuntimeException(String.format("Wrong length of the %s!", ship.getName()));
        }

        // Direction to place the ship tokens from left to right, right to left, up to down or down to up
        Coordinates direction = c1.directionTo(c2);
        int x = c1.getX();
        int y = c1.getY();
        int shipSize = ship.getSize();
        List<Coordinates> coordinates = new ArrayList<>();
        while (shipSize != 0) {
            if (isCloseToAnotherShip(x, y)) {
                throw new RuntimeException("You placed it too close to another one.");
            }
            coordinates.add(new Coordinates(x,y));
            x += direction.getX();
            y += direction.getY();
            shipSize--;
        }
        coordinates.forEach(c -> playerBoard[c.getX()][c.getY()] = 'O');
        ships.add(new Ship(ship, c1, c2));
    }

    public String shootAt(Coordinates projectile) {
        return opponent.takeShotAt(projectile);
    }

    private String takeShotAt(Coordinates projectile) {
        if (isOutOfBounds(projectile)) {
            throw new RuntimeException("You entered the wrong coordinates!");
        }
        char targetCell = playerBoard[projectile.getX()][projectile.getY()];
        String msg;
        if (targetCell == FOW_TOKEN) {
            playerBoard[projectile.getX()][projectile.getY()] = MISSED_TOKEN;
            fowBoard[projectile.getX()][projectile.getY()] = MISSED_TOKEN;
            msg = "You missed!";
        } else if(targetCell == SHIP_TOKEN) {
            playerBoard[projectile.getX()][projectile.getY()] = STRIKE_TOKEN;
            fowBoard[projectile.getX()][projectile.getY()] = STRIKE_TOKEN;
            // Look for the ship that was hit
            Ship damagedShip = ships.stream().filter(ship -> ship.wasHitBy(projectile)).findFirst().get();
            if (damagedShip.isSunk()) {
                ships.remove(damagedShip);
                if (ships.size() == 0) {
                    gameOver = true;
                    msg = "You sank the last ship. You won. Congratulations!";
                } else {
                    msg = "You sank a ship! Specify a new target:";
                }
            } else {
                msg = "You hit a ship!";
            }
        } else {
            msg = "Already shot here.";
        }
        return msg;
    }

    public void printGameBoard() {
        opponent.printBoard(true);
        System.out.print("---------------------");
        printBoard(false);
    }

    public void printBoard(boolean isFowEnabled) {
        char[][] board = isFowEnabled ? fowBoard : playerBoard;
        StringBuilder sb = new StringBuilder();
        sb.append("\n  1 2 3 4 5 6 7 8 9 10\n");
        for (int i = 0; i < 10; i++) {
            sb.append((char) (i + 65));
            for (int j = 0; j < 10; j++) {
                sb.append(" ").append(board[i][j]);
            }
            sb.append("\n");
        }
        System.out.print(sb);
    }

    private boolean isCloseToAnotherShip(int x, int y) {
        int adjacentCells = 0;
        // Sum of Left, Right, Up and Down adjacent cells, 4 * '~' = 504
        adjacentCells += x - 1 < 0 ? FOW_TOKEN : playerBoard[x - 1][y];
        adjacentCells += x + 1 >= COLS ? FOW_TOKEN : playerBoard[x + 1][y];
        adjacentCells += y + 1 >= ROWS ? FOW_TOKEN : playerBoard[x][y + 1];
        adjacentCells += y - 1 < 0 ? FOW_TOKEN : playerBoard[x][y - 1];
        return adjacentCells != 504;
    }

    private boolean isOutOfBounds(Coordinates c) {
        return c.getX() < 0 || c.getX() >= COLS || c.getY() < 0 || c.getY() >= ROWS;
    }

}
