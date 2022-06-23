import java.util.Random;
import java.util.Scanner;

public class HW2_GilLevkovitch {
	// Gil Levkovich ID:312496821

	static boolean BoardWithNeighbors[][], NewBoard[][];
	static int NumberOfSteps = 1;
	static int X = 0;
	static int MaxNumberOfBoards = 0;

	// Function to init the board with random numbers.
	// the result is matrix X*X with RANDOM true/false numbers
	public static void InitBoard() {
		for (int i = 0; i < NewBoard.length; i++)
			for (int j = 0; j < NewBoard.length; j++) {
				// we get a random number between 0-1. so we "cut in the middle", to decide if
				// it true of false
				double n = Math.random();
				if (n >= 0.5) {
					NewBoard[i][j] = true;
				} else {
					NewBoard[i][j] = false;
				}
			}
	}

	// a Function to set the board with its neighbors according to the roles in the
	// exercis
	// create LARGER (X+2)(X+2) matrix with all the Neighbors
	// and fill its values according to the Current Matrix
	// padding the matrix with FALSE
	public static boolean SetBoard(boolean[][] board) {
		BoardWithNeighbors = new boolean[X + 2][X + 2];
		// STEP 1: simpliy copy the board to the middle of the destination matrix
		// _____________->0 0 0 1 0 0 0
		// 1 1 1 1 1____->1 1 1 1 1 1 1
		// 1 1 0 1 0____->0 1 1 0 1 0 1
		// 0 0 0 0 0____->0 0 0 0 0 0 0
		// 1 1 1 1 1____->1 1 1 1 1 1 1
		// 0 0 1 0 0____->0 0 0 1 0 0 0
		// _____________->1 1 1 1 1 1 1
		for (int i = 0; i < X; i++)
			for (int j = 0; j < X; j++) {
				BoardWithNeighbors[i + 1][j + 1] = board[i][j];
			}

		// STEP 2: copy left + right + top + bottom matrix colum/rows values
		for (int i = 1; i <= X; i++) {
			BoardWithNeighbors[0][i] = board[X - 1][i - 1];
			BoardWithNeighbors[X + 1][i] = board[0][i - 1];
			BoardWithNeighbors[i][0] = board[i - 1][X - 1];
			BoardWithNeighbors[i][X + 1] = board[i - 1][0];
		}
		// STEP 3: fill the board corners
		BoardWithNeighbors[0][0] = board[X - 1][X - 1];
		BoardWithNeighbors[X + 1][X + 1] = board[0][0];
		BoardWithNeighbors[0][X + 1] = board[X - 1][0];
		BoardWithNeighbors[X + 1][0] = board[0][X - 1];

		// for debug purpose optional printing the result
		// PrintBoard(BoardWithNeighbors, "Filled Board");

		return true;
	}

	// Function to calculate the next step of the game.
	// the finction recives the number of steps to calculate and returns the
	// resulting Board
	// the calculation is performed on each cell of the BoardWithNeighbors - a board
	// that we have calculate previously, Which contains the Original board + its
	// neighbors.
	// at the End of the scaning we compare the result with the previous board
	// (PreviousBoard). If they are the same we exit the function and return.
	// Other wise we check to see it we exided the number of steps. if so we return.
	// otherwise we continue.
	public static boolean[][] MoveTimeBy(int MaxNumberOfSteps) {
		boolean[][] PreviousBoard = new boolean[X][X];

		while (NumberOfSteps < MaxNumberOfSteps) {
			CopyBoard(NewBoard, PreviousBoard);
			// updating the status of the board one step at a time
			for (int i = 1; i < BoardWithNeighbors.length - 1; i++) {
				for (int j = 1; j < BoardWithNeighbors.length - 1; j++) {
					// iterating over the Neighbors of cell [i][j]
					int count = 0;
					for (int l = 0; l < 3; l++) {
						for (int m = 0; m < 3; m++) {
							if (!(l == 1 && m == 1)) // skip on the current cell
								count += (BoardWithNeighbors[i - 1 + l][j - 1 + m] == true) ? 1 : 0;
						}
					}
					if (count > 4 || count < 2)
						NewBoard[i - 1][j - 1] = false;
					if (BoardWithNeighbors[i][j] == false && count == 3)
						NewBoard[i - 1][j - 1] = true;
				}
			}
			if (!CheckEquality(NewBoard, PreviousBoard)) {
				PrintBoard(NewBoard, "Step " + NumberOfSteps);
				System.out.println("\nBoard Changed.");
				SetBoard(NewBoard);
				NumberOfSteps++;

			} else {
				return NewBoard;
			}
			// PrintBoard(BoardWithNeighbors, "BoardWithNeighbors ");
		}
		return NewBoard;
	}

	// Utility Function to print the boars according to the exercise instruction.
	public static void PrintBoard(boolean Board[][], String msg) {
		System.out.print("\n" + msg);
		for (int i = 0; i < Board.length; i++) {
			System.out.println("");
			for (int j = 0; j < Board.length; j++) {
				if (Board[i][j])
					System.out.print("1 ");
				else
					System.out.print("0 ");
			}
		}
	}

	// function: Check if two Matrices are Equal. if Equal return true end vise
	// versa.
	public static boolean CheckEquality(boolean[][] a, boolean[][] b) {
		if (a.length != b.length)
			return false;
		else {
			for (int i = 0; i < a.length; i++)
				for (int j = 0; j < a.length; j++) {
					if (a[i][j] != b[i][j])
						return false;
				}
			return true;
		}
	}

	// Function: copy one matrix Another (source => destination)
	public static boolean CopyBoard(boolean[][] Source, boolean[][] Dest) {
		if (Source.length != Dest.length)
			return false;
		else {
			for (int i = 0; i < Source.length; i++)
				for (int j = 0; j < Source.length; j++)
					Dest[i][j] = Source[i][j];
		}
		return true;
	}

	// The main Function ....
	public static void main(String[] args) {
		System.out.println("************** Game Of Life X * X **************");
		System.out.print("Enter X size of board: ");
		Scanner S = new Scanner(System.in);
		X = S.nextInt();
		System.out.print("Enter Max number of Boards: ");
		MaxNumberOfBoards = S.nextInt();
		S.close();
		System.out.println("");

		// setting the starting point for the board
		NewBoard = new boolean[X][X];
		BoardWithNeighbors = new boolean[X + 2][X + 2];

		InitBoard();
		PrintBoard(NewBoard, "Start Board");

		SetBoard(NewBoard);

		// now for the Game!!!! Claculate moves
		NewBoard = MoveTimeBy(MaxNumberOfBoards);
		// Print the final result
		PrintBoard(NewBoard, "Finished!!");
		System.out.println("\nBoard Changed " + NumberOfSteps + " Times.");

	}
}