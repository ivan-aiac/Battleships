package battleship;

import java.util.Objects;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Battleships player1 = new Battleships("Player 1");
        Battleships player2 = new Battleships("Player 2");
        Battleships currentTurn = null;

        player1.setOpponent(player2);
        player2.setOpponent(player1);

        placeShips(player1, scanner);
        placeShips(player2,scanner);

        String msg;
        Coordinates projectileCoordinates;
        while (player1.isNotGameOver() || player2.isNotGameOver()) {
            currentTurn = Objects.equals(currentTurn, player1) ? player2 : player1;
            currentTurn.printGameBoard();
            System.out.printf("%s, it's your turn:\n", currentTurn.getPlayerName());
            while (true){
                try {
                    projectileCoordinates = new Coordinates(scanner.nextLine());
                    msg = currentTurn.shootAt(projectileCoordinates);
                    System.out.println(msg);
                    break;
                } catch (RuntimeException e){
                    System.out.printf("Error! %s Try Again:\n", e.getMessage());
                }
            }
            waitForEnterKey(scanner);
        }
    }

    private static void placeShips(Battleships board, Scanner scanner) {
        Coordinates a, b;
        String[] coordinates;

        System.out.printf("%s, place your ships on the game field\n", board.getPlayerName());

        board.printBoard(false);

        for (ShipType ship: ShipType.values()) {
            System.out.printf("Enter the coordinates of the %s (%d cells):\n", ship.getName(), ship.getSize());
            while (true) {
                try {
                    coordinates = scanner.nextLine().split(" ");
                    a = new Coordinates(coordinates[0]);
                    b = new Coordinates(coordinates[1]);
                    board.placeShip(ship, a, b);
                    board.printBoard(false);
                    break;
                } catch (RuntimeException e) {
                    System.out.printf("Error! %s Try Again:\n", e.getMessage());
                }
            }
        }
        waitForEnterKey(scanner);
    }

    private static void waitForEnterKey(Scanner scanner) {
        String k = "k";
        System.out.println("Press Enter and pass the move to another player\n...");
        while (!"".equals(k)) {
            k = scanner.nextLine();
        }
    }
}
