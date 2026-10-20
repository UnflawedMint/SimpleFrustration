package game;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

// Main entry point for the application
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("SimpleFrustration Game");
            System.out.println("1. Interactive Custom Play");
            System.out.println("2. Automated Demo with 10 Scenarios");
            System.out.println("3. Exit");
            System.out.print("Enter your choice (1-3): ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    playInteractive(scanner);
                    break;
                case "2":
                    playAutomated(scanner);
                    break;
                case "3":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
            System.out.println();
        }
        scanner.close();
    }

    // Run the automated test sequences
    private static void playAutomated(Scanner scanner) {
        System.out.println("Automated demo ready");
        System.out.println("Press ENTER to execute all scenarios");
        scanner.nextLine();

        runScenario1();
        runScenario2();
        runScenario3();
        runScenario4();
        runScenario5();
        runScenario6();
        runScenario7();
        runScenario8();
        runScenario9();
        runScenario10();

        System.out.println("\nAll scenarios executed successfully");
    }

    // Interactive mode allowing users to select rule combinations
    private static void playInteractive(Scanner scanner) {
        System.out.println("\n--- Game Rule Configuration ---");

        System.out.print("Select Board Size (1: Basic 18/3, 2: Large 36/6): ");
        boolean isLargeBoard = scanner.nextLine().equals("2");

        System.out.print("Select Player Count (1: Two Players, 2: Four Players): ");
        boolean isFourPlayers = scanner.nextLine().equals("2");

        System.out.print("Select Win Rule (1: Allow Overshoot, 2: Exact Win): ");
        WinRuleStrategy winStrategy = scanner.nextLine().equals("2") ? new ExactWinStrategy() : new AllowOvershootWinStrategy();

        System.out.print("Select Hit Rule (1: Ignore Hit, 2: Send Home): ");
        HitRuleStrategy hitStrategy = scanner.nextLine().equals("2") ? new SendHomeHitStrategy() : new IgnoreHitStrategy();

        System.out.print("Select Dice Type (1: Two Dice, 2: Single Die): ");
        DiceShaker shaker = scanner.nextLine().equals("2") ? new RandomSingleDiceShaker() : new RandomDoubleDiceShaker();

        // Setup board
        int mainPos = isLargeBoard ? 36 : 18;
        int tailPos = isLargeBoard ? 6 : 3;
        Board board = new Board(mainPos, tailPos);

        GameManager game = new GameManager(board);
        game.setWinRuleStrategy(winStrategy);
        game.setHitRuleStrategy(hitStrategy);
        game.addObserver(new ConsoleLogger());

        // Setup players and home indices
        addPlayerToGame(game, board, PlayerColor.RED, 1);
        addPlayerToGame(game, board, PlayerColor.BLUE, isLargeBoard ? 19 : 10);

        if (isFourPlayers) {
            addPlayerToGame(game, board, PlayerColor.GREEN, isLargeBoard ? 19 : 19);
            addPlayerToGame(game, board, PlayerColor.YELLOW, isLargeBoard ? 28 : 28);
        }

        System.out.println("\nGame started");

        boolean gameOver = false;
        int turnIndex = 0;
        List<Player> players = game.getPlayers();

        // Main interactive game loop
        while (!gameOver) {
            Player currentPlayer = players.get(turnIndex % players.size());
            System.out.print("\n" + currentPlayer.getName() + "'s turn. Press ENTER to roll or type 'undo': ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("undo")) {
                game.undoLastTurn();
                if (turnIndex > 0) {
                    turnIndex--;
                }
            } else {
                game.simulateTurn(currentPlayer, shaker);

                // Check win condition
                if (currentPlayer.getCurrentPosition().getType() == PositionType.END) {
                    System.out.println(currentPlayer.getName() + " wins in " + currentPlayer.getTurnCount() + " moves");
                    gameOver = true;
                } else {
                    turnIndex++;
                }
            }
        }
    }

    // Helper to add player and set home index
    private static void addPlayerToGame(GameManager game, Board board, PlayerColor color, int homeIndex) {
        board.setHomeIndex(color, homeIndex);
        game.addPlayer(new Player(color, new BoardPosition(PositionType.HOME, homeIndex)));
    }

    // Format scenario output header
    private static void printScenarioHeader(String title, String config) {
        System.out.println("================================================================================");
        System.out.println(title);
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("Configuration: " + config);
        System.out.println("--------------------------------------------------------------------------------\n");
    }

    // Simulate a specific number of turns sequentially
    private static void playTurns(GameManager game, DiceShaker shaker, int totalTurns) {
        List<Player> players = game.getPlayers();
        for (int i = 0; i < totalTurns; i++) {
            Player currentPlayer = players.get(i % players.size());
            game.simulateTurn(currentPlayer, shaker);
        }
    }

    // Scenario 1: Basic mechanics and correct termination
    private static void runScenario1() {
        printScenarioHeader("SCENARIO 1: Basic Game - Blue wins in 2 moves",
                "Board=18, Tail=3, Players={Red, Blue}, Rules: Allow Overshoot, Ignore Hits, Dice={12,12,7,8}");

        Board board = new Board(18, 3);
        GameManager game = new GameManager(board);
        addPlayerToGame(game, board, PlayerColor.RED, 1);
        addPlayerToGame(game, board, PlayerColor.BLUE, 10);

        game.setWinRuleStrategy(new AllowOvershootWinStrategy());
        game.setHitRuleStrategy(new IgnoreHitStrategy());
        game.addObserver(new ConsoleLogger());

        DiceShaker shaker = new FixedDiceShaker(new int[]{12, 12, 7, 8});
        playTurns(game, shaker, 4);
        System.out.println("\n================================================================================\n");
    }

    // Scenario 2: Multiple players sharing the same position
    private static void runScenario2() {
        printScenarioHeader("SCENARIO 2: Basic Game - Hit is ignored",
                "Board=18, Tail=3, Players={Red, Blue}, Rules: Allow Overshoot, Ignore Hits, Dice={8,2,3,4,9}");

        Board board = new Board(18, 3);
        GameManager game = new GameManager(board);
        addPlayerToGame(game, board, PlayerColor.RED, 1);
        addPlayerToGame(game, board, PlayerColor.BLUE, 10);

        game.setWinRuleStrategy(new AllowOvershootWinStrategy());
        game.setHitRuleStrategy(new IgnoreHitStrategy());
        game.addObserver(new ConsoleLogger());

        DiceShaker shaker = new FixedDiceShaker(new int[]{8, 2, 3, 4, 9});
        playTurns(game, shaker, 5);
        System.out.println("\n================================================================================\n");
    }

    // Scenario 3: Exceeding tail length allowed in basic game
    private static void runScenario3() {
        printScenarioHeader("SCENARIO 3: Basic Game - Overshoot to win",
                "Board=18, Tail=3, Players={Red, Blue}, Rules: Allow Overshoot, Ignore Hits, Dice={12,12,7,11}");

        Board board = new Board(18, 3);
        GameManager game = new GameManager(board);
        addPlayerToGame(game, board, PlayerColor.RED, 1);
        addPlayerToGame(game, board, PlayerColor.BLUE, 10);

        game.setWinRuleStrategy(new AllowOvershootWinStrategy());
        game.setHitRuleStrategy(new IgnoreHitStrategy());
        game.addObserver(new ConsoleLogger());

        DiceShaker shaker = new FixedDiceShaker(new int[]{12, 12, 7, 11});
        playTurns(game, shaker, 4);
        System.out.println("\n================================================================================\n");
    }

    // Scenario 4: Overshoot backoff calculation
    private static void runScenario4() {
        printScenarioHeader("SCENARIO 4: Variation - Exact Win Backoff",
                "Board=18, Tail=3, Players={Red, Blue}, Rules: Exact Win, Send HOME, Dice={12,12,7,11,3,3}");

        Board board = new Board(18, 3);
        GameManager game = new GameManager(board);
        addPlayerToGame(game, board, PlayerColor.RED, 1);
        addPlayerToGame(game, board, PlayerColor.BLUE, 10);

        game.setWinRuleStrategy(new ExactWinStrategy());
        game.setHitRuleStrategy(new SendHomeHitStrategy());
        game.addObserver(new ConsoleLogger());

        DiceShaker shaker = new FixedDiceShaker(new int[]{12, 12, 7, 11, 3, 3});
        playTurns(game, shaker, 6);
        System.out.println("\n================================================================================\n");
    }

    // Scenario 5: Hit player sent to home position
    private static void runScenario5() {
        printScenarioHeader("SCENARIO 5: Variation - Send HOME when HIT",
                "Board=18, Tail=3, Players={Red, Blue}, Rules: Exact Win, Send HOME, Dice={8,2,3,4,9}");

        Board board = new Board(18, 3);
        GameManager game = new GameManager(board);
        addPlayerToGame(game, board, PlayerColor.RED, 1);
        addPlayerToGame(game, board, PlayerColor.BLUE, 10);

        game.setWinRuleStrategy(new ExactWinStrategy());
        game.setHitRuleStrategy(new SendHomeHitStrategy());
        game.addObserver(new ConsoleLogger());

        DiceShaker shaker = new FixedDiceShaker(new int[]{8, 2, 3, 4, 9});
        playTurns(game, shaker, 5);
        System.out.println("\n================================================================================\n");
    }

    // Scenario 6: Movement logic using single die
    private static void runScenario6() {
        printScenarioHeader("SCENARIO 6: Variation - Single Die",
                "Board=18, Tail=3, Players={Red, Blue}, Rules: Allow Overshoot, Ignore Hits, Dice={6,6,6,6,3,4,3,4}");

        Board board = new Board(18, 3);
        GameManager game = new GameManager(board);
        addPlayerToGame(game, board, PlayerColor.RED, 1);
        addPlayerToGame(game, board, PlayerColor.BLUE, 10);

        game.setWinRuleStrategy(new AllowOvershootWinStrategy());
        game.setHitRuleStrategy(new IgnoreHitStrategy());
        game.addObserver(new ConsoleLogger());

        DiceShaker shaker = new FixedDiceShaker(new int[]{6, 6, 6, 6, 3, 4, 3, 4});
        playTurns(game, shaker, 8);
        System.out.println("\n================================================================================\n");
    }

    // Scenario 7: Mathematical routing on larger board size
    private static void runScenario7() {
        printScenarioHeader("SCENARIO 7: Variation - Large Board",
                "Board=36, Tail=6, Players={Red, Blue}, Rules: Allow Overshoot, Ignore Hits, Dice={12,12,12,12,12,12,4,5}");

        Board board = new Board(36, 6);
        GameManager game = new GameManager(board);
        addPlayerToGame(game, board, PlayerColor.RED, 1);
        addPlayerToGame(game, board, PlayerColor.BLUE, 19);

        game.setWinRuleStrategy(new AllowOvershootWinStrategy());
        game.setHitRuleStrategy(new IgnoreHitStrategy());
        game.addObserver(new ConsoleLogger());

        DiceShaker shaker = new FixedDiceShaker(new int[]{12, 12, 12, 12, 12, 12, 4, 5});
        playTurns(game, shaker, 8);
        System.out.println("\n================================================================================\n");
    }

    // Scenario 8: Integration of multiple advanced features
    private static void runScenario8() {
        printScenarioHeader("SCENARIO 8: Advanced - Four Players",
                "Board=36, Tail=6, Players={Red, Blue, Green, Yellow}, Rules: Exact Win, Send HOME, Dice(20 rolls)");

        Board board = new Board(36, 6);
        GameManager game = new GameManager(board);
        addPlayerToGame(game, board, PlayerColor.RED, 1);
        addPlayerToGame(game, board, PlayerColor.BLUE, 10);
        addPlayerToGame(game, board, PlayerColor.GREEN, 19);
        addPlayerToGame(game, board, PlayerColor.YELLOW, 28);

        game.setWinRuleStrategy(new ExactWinStrategy());
        game.setHitRuleStrategy(new SendHomeHitStrategy());
        game.addObserver(new ConsoleLogger());

        DiceShaker shaker = new FixedDiceShaker(new int[]{12, 5, 2, 12, 7, 4, 2, 12, 3, 5, 2, 12, 5, 4, 4, 8, 2, 2, 12, 3});
        playTurns(game, shaker, 20);
        System.out.println("\n================================================================================\n");
    }

    // Scenario 9: Command pattern state restoration
    private static void runScenario9() {
        printScenarioHeader("SCENARIO 9: Advanced - Complex Hit Undo",
                "Board=18, Tail=3, Players={Red, Blue}, Rules: Exact Win, Send HOME");

        Board board = new Board(18, 3);
        GameManager game = new GameManager(board);
        addPlayerToGame(game, board, PlayerColor.RED, 1);
        addPlayerToGame(game, board, PlayerColor.BLUE, 10);

        game.setWinRuleStrategy(new ExactWinStrategy());
        game.setHitRuleStrategy(new SendHomeHitStrategy());
        game.addObserver(new ConsoleLogger());

        DiceShaker shaker = new FixedDiceShaker(new int[]{8, 2, 3});

        System.out.println(">>> Normal execution of 3 turns (Red hits Blue):");
        playTurns(game, shaker, 3);

        System.out.println("\n>>> Initiating undo sequence:");
        game.undoLastTurn();

        System.out.println("\n================================================================================\n");
    }

    // Scenario 10: Extreme boundary test
    private static void runScenario10() {
        printScenarioHeader("SCENARIO 10: All Features Combined",
                "Board=36, Tail=6, Players=4(Red,Blue,Green,Yellow), Rules: Exact Win, Send HOME");

        Board board = new Board(36, 6);
        GameManager game = new GameManager(board);

        addPlayerToGame(game, board, PlayerColor.RED, 1);
        addPlayerToGame(game, board, PlayerColor.BLUE, 10);
        addPlayerToGame(game, board, PlayerColor.GREEN, 19);
        addPlayerToGame(game, board, PlayerColor.YELLOW, 28);

        game.setWinRuleStrategy(new ExactWinStrategy());
        game.setHitRuleStrategy(new SendHomeHitStrategy());
        game.addObserver(new ConsoleLogger());

        DiceShaker shaker = new FixedDiceShaker(new int[]{12, 3, 5, 30, 35, 12});

        System.out.println(">>> Phase 1: Blue hits Red");
        playTurns(game, shaker, 2);

        System.out.println("\n>>> Phase 2: Undo the Hit");
        game.undoLastTurn();

        System.out.println("\n>>> Phase 3: Green hits Red");
        playTurns(game, shaker, 4);

        System.out.println("\n================================================================================\n");
    }
}