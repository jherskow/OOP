import java.util.Scanner;

/**
 * The Competition class represents a Nim competition between two players, consisting of a given number of
 * rounds. It also keeps track of the number of victories of each player.
 * @author Jherskow
 */
public class Competition {

	private static final int GAMEOVER = 1;
	private static final int CONTINUE = 0;
	private static final int ID1=1, ID2 = 2;
	private static final int numPlayers = 2;
	// scoreTally is 1 larger to allow easy access to score using player's position.
	private int[] scoreTally = new int[numPlayers+1];
	private Board board;
	private boolean shouldPrint;
	private Player player1;
	private Player player2;

	/**
	 * Receives two Player objects, representing the two competing opponents,
	 * and a flag determining whether messages should be displayed.
	 * @param player1  The Player object representing the first player.
	 * @param player2 The Player objects representing the second player.
	 * @param displayMessage  A flag indicating whether game play messages should be printed to the console.
	 */
	public Competition(Player player1, Player player2, boolean displayMessage){
		shouldPrint = displayMessage;
		this.player1 = player1;
		this.player2 = player2;
	}

	/**
	 * The method runs a Nim competition between two players according to the three user-specified arguments. 
	 * (1) The type of the first player, which is a positive integer between 1 and 4: 1 for a Random computer
	 *     player, 2 for a Heuristic computer player, 3 for a Smart computer player and 4 for a human player.
	 * (2) The type of the second player, which is a positive integer between 1 and 4.
	 * (3) The number of rounds to be played in the competition.
	 * @param args an array of string representations of the three input arguments, as detailed above.
	 */
	public static void main(String[] args) {
		int p1Type = Integer.parseInt(args[0]);
		int p2Type = Integer.parseInt(args[1]);
		int numGames = Integer.parseInt(args[2]);

		Scanner scanner = new Scanner(System.in);
		Player playerOne = new Player(p1Type,ID1,scanner);
		Player playerTwo = new Player(p2Type,ID2,scanner);

		boolean isHumanPlayer = (playerOne.getPlayerType() == Player.HUMAN ||
				playerTwo.getPlayerType() == Player.HUMAN);

		System.out.println("Starting a Nim competition of " + numGames + " rounds between a " +
				playerOne.getTypeName() + " player and a " + playerTwo.getTypeName() + " player.");

		Competition competition = new Competition(playerOne, playerTwo, isHumanPlayer);
		competition.playMultipleRounds(numGames);
		scanner.close();
	}

	/**
	 * Returns the current score of the Nth player.
	 * @param playerPosition The number identifying the player.
	 * @return That player's score.
	 */
	int getPlayerScore(int playerPosition){
		return scoreTally[playerPosition];
	}

	/**
	 * Run the game for the given number of rounds.
	 * @param numberOfRounds number of rounds to play.
	 */
	void playMultipleRounds(int numberOfRounds){
		for (int i=0; i<numberOfRounds; i++){
			playRound(player1, player2);
		}
		System.out.println("The results are "+getPlayerScore(ID1)+":"+getPlayerScore(ID2));
	}

	/**
	 * Run a single round.
	 * @param playerOne The first player.
	 * @param playerTwo The second player.
	 */
	private void playRound(Player playerOne, Player playerTwo){
		board = new Board();
		if (shouldPrint){System.out.println("Welcome to the sticks game!");}
		while (true) {
			if (playTurn(playerOne) == GAMEOVER) break;
			if (playTurn(playerTwo) == GAMEOVER) break;
		}
	}

	/**
	 * Runs a turn of a single player.
	 * The method first checks to see if the previous player has lost the game.
	 * If not, the method then prompts the player for a move, and loops until a legal move is provided.
	 * @param player the player
	 * @return GAMEOVER (1) if game has been won by the player, CONTINUE (0) otherwise.
	 */
	private int playTurn(Player player){
		int playerId = player.getPlayerId();
		if (board.getNumberOfUnmarkedSticks() == 0){
			if (shouldPrint){ System.out.println("Player "+playerId+" won!");}
			scoreTally[playerId]++;
			return GAMEOVER;
		}
		if (shouldPrint){ System.out.println("Player "+ playerId+", it is now your turn!");}
		boolean isMoveDone = false;
		while (! isMoveDone){
			Move move = player.produceMove(board);
			int moveResult = board.markStickSequence(move);
			if (moveResult != 0){
				if (shouldPrint){ System.out.println("Invalid move. Enter another:");}
			}else{
				if (shouldPrint){ System.out.println("Player "+playerId+" made the move: "+move);}
				isMoveDone = true;
			}
		}
		return CONTINUE;
	}
}
