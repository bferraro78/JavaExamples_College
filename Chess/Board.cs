using System;
using System.Collections.Generic;

/* Board Class */
namespace Chess {
	public sealed class Board {

		private static readonly Board theOne = new Board();
		public static chessPiece[,] chessBoard = new chessPiece[8,8];
		private int BlackKingLocX;
		private int BlackKingLocY;
		private int WhiteKingLocX;
		private int WhiteKingLocY;

		/* Constructor with no parameters, so another class can not create a instance */
		private Board () {

		}

		private void setKingLocation(bool isBlack, int x, int y) {
			Console.Out.WriteLine(x);
			 Console.Out.WriteLine(y); 
			 Console.Out.WriteLine(isBlack);
			if (isBlack) {
				BlackKingLocX = x;
				BlackKingLocY = y;
			} else {
				WhiteKingLocX = x;
				WhiteKingLocY = y;
			}
		}

		public static Board getBoard() {
			return theOne;
		}

		/* Return ches piece on the spot - returns null if spot is blank */
		public chessPiece getPiece(int oldX, int oldY) {
			if (chessBoard[oldX, oldY] == null) {
				return null;
			} else { 
			return chessBoard[oldX, oldY];
			}
		}


		public bool Check(bool blackTurn) {
				Console.Out.WriteLine("Checking CHECK...");
				bool tmp = false;

			/* - Check all spots on board for a black/white piece 
			   - Get all Valid moves */
			for (int x = 0; x < 8; x++) {
				for (int y = 0; y < 8; y++) {
					if (chessBoard[x, y] != null) {
						if (blackTurn) { // Only check White Pieces Moves
							if (!(chessBoard[x, y].BlackorWhite())) {
								// pass in black king location
								tmp = chessBoard[x, y].allvalidMoves(getBoard(), x, y, BlackKingLocX, BlackKingLocY, blackTurn);

								if (tmp) {
									return true;
								}
							}
						} else { // only check Black Pieces Moves
							if (chessBoard[x, y].BlackorWhite()) {
								// pass in white king location
								tmp = chessBoard[x, y].allvalidMoves(getBoard(), x, y, WhiteKingLocX, WhiteKingLocY, blackTurn);

								if (tmp) {
									return true;
								}
							}
						}
					}
				}
			} // END OUTER FOR LOOP
			return false;
		}

		public int movePiece(int oldX, int oldY, int newX, int newY, bool blackTurn) {
			bool isKing = false;

			// Last check, if the new spot contains an enemey or friendly player
			//		if same team - can not eliminate - invalid move
			if (chessBoard[newX, newY] != null) {
				if (chessBoard[newX, newY].BlackorWhite() == chessBoard[oldX, oldY].BlackorWhite()) { 
					return 1;
				}
			}
			/* Store backup of both spots in question just in case we must revert changes */
			chessPiece backUpNew = chessBoard[newX, newY];
			chessPiece backUpOld = chessBoard[oldX, oldY];

			// Update kings location on board - if nessicary
			if (chessBoard[oldX, oldY].getType().Equals("Ki")) {
				setKingLocation(blackTurn, newX, newY);
				isKing = true;
			}

			// Move Piece - old Piece eliminates player or
			// put into null spot (empty spot)
			chessBoard[newX, newY] = chessBoard[oldX, oldY];
			chessBoard[oldX, oldY] = null;

			if (Check(blackTurn)) {  
				/* STILL IN CHECK!! */
				// REVERT CHANGES, RETURN FALSE
				 chessBoard[newX, newY] = backUpNew;
				 chessBoard[oldX, oldY] = backUpOld;

				if (isKing) {
					setKingLocation(blackTurn, oldX, oldY);
				}
				return 2;
			}

			return 0;
		}

		public void initBoard() {
			Console.WriteLine("INIT Board");

			/* Queens */
			chessPiece QueenW = new Queen(false, 7, 3);
			chessBoard[QueenW.getX(), QueenW.getY()] = QueenW;
			chessPiece QueenB = new Queen(true, 0, 3);
			chessBoard[QueenB.getX(), QueenB.getY()] = QueenB;

			// /* Kings */
			chessPiece KingW = new King(false, 7, 4);
			chessBoard[KingW.getX(), KingW.getY()] = KingW;
				setKingLocation(false, 7, 4);
			chessPiece KingB = new King(true, 0, 4);
			chessBoard[KingB.getX(), KingB.getY()] = KingB;
				setKingLocation(true, 0, 4);

			// /* Bishops */
			chessPiece BishW1 = new Bishop(false, 7, 2);
			chessBoard[BishW1.getX(), BishW1.getY()] = BishW1;
			chessPiece BishW2 = new Bishop(false, 7, 5);
			chessBoard[BishW2.getX(), BishW2.getY()] = BishW2;
			// 	// Blacks
			chessPiece BishB1 = new Bishop(true, 0, 2);
			chessBoard[BishB1.getX(), BishB1.getY()] = BishB1;
			chessPiece BishB2 = new Bishop(true, 0, 5);
			chessBoard[BishB2.getX(), BishB2.getY()] = BishB2;

			// /* Knights */
			chessPiece KnW1 = new Knight(false, 7, 1);
			chessBoard[KnW1.getX(), KnW1.getY()] = KnW1;
			chessPiece KnW2 = new Knight(false, 7, 6);
			chessBoard[KnW2.getX(), KnW2.getY()] = KnW2;
				// Blacks
			chessPiece KnB1 = new Knight(true, 0, 1);
			chessBoard[KnB1.getX(), KnB1.getY()] = KnB1;
			chessPiece KnB2 = new Knight(true, 0, 6);
			chessBoard[KnB2.getX(), KnB2.getY()] = KnB2;

			/* Rooks */
			chessPiece RookW1 = new Rook(false, 7, 0);
			chessBoard[RookW1.getX(), RookW1.getY()] = RookW1;
			chessPiece RookW2 = new Rook(false, 7, 7);
			chessBoard[RookW2.getX(), RookW2.getY()] = RookW2;
				// Blacks
			chessPiece RookB1 = new Rook(true, 0, 0);
			chessBoard[RookB1.getX(), RookB1.getY()] = RookB1;
			chessPiece RookB2 = new Rook(true, 0, 7);
			chessBoard[RookB2.getX(), RookB2.getY()] = RookB2;

			/* Pawns */
			// chessPiece PawnW1 = new Pawn(false, 4, 4);
			// chessBoard[PawnW1.getX(), PawnW1.getY()] = PawnW1;
			// chessPiece PawnW2 = new Pawn(false, 6, 1);
			// chessBoard[PawnW2.getX(), PawnW2.getY()] = PawnW2;
			// chessPiece PawnW3 = new Pawn(false, 6, 2);
			// chessBoard[PawnW3.getX(), PawnW3.getY()] = PawnW3;
			// chessPiece PawnW4 = new Pawn(false, 6, 3);
			// chessBoard[PawnW4.getX(), PawnW4.getY()] = PawnW4;
			// chessPiece PawnW5 = new Pawn(false, 6, 4);
			// chessBoard[PawnW5.getX(), PawnW5.getY()] = PawnW5;
			// chessPiece PawnW6 = new Pawn(false, 6, 5);
			// chessBoard[PawnW6.getX(), PawnW6.getY()] = PawnW6;
			// chessPiece PawnW7 = new Pawn(false, 6, 6);
			// chessBoard[PawnW7.getX(), PawnW7.getY()] = PawnW7;
			// chessPiece PawnW8 = new Pawn(false, 6, 7);
			// chessBoard[PawnW8.getX(), PawnW8.getY()] = PawnW8;
			// 	// Blacks
			// chessPiece PawnB1 = new Pawn(true, 1, 0);
			// chessBoard[PawnB1.getX(), PawnB1.getY()] = PawnB1;
			// chessPiece PawnB2 = new Pawn(true, 1, 1);
			// chessBoard[PawnB2.getX(), PawnB2.getY()] = PawnB2;
			// chessPiece PawnB3 = new Pawn(true, 1, 2);
			// chessBoard[PawnB3.getX(), PawnB3.getY()] = PawnB3;
			// chessPiece PawnB4 = new Pawn(true, 1, 3);
			// chessBoard[PawnB4.getX(), PawnB4.getY()] = PawnB4;
			// chessPiece PawnB5 = new Pawn(true, 1, 4);
			// chessBoard[PawnB5.getX(), PawnB5.getY()] = PawnB5;
			// chessPiece PawnB6 = new Pawn(true, 1, 5);
			// chessBoard[PawnB6.getX(), PawnB6.getY()] = PawnB6;
			// chessPiece PawnB7 = new Pawn(true, 1, 6);
			// chessBoard[PawnB7.getX(), PawnB7.getY()] = PawnB7;
			// chessPiece PawnB8 = new Pawn(true, 1, 7);
			// chessBoard[PawnB8.getX(), PawnB8.getY()] = PawnB8;

		}

		public void displayBoard() {
			Console.WriteLine("Displaying Board");
			Console.Out.WriteLine("    " +0+"     "+1+"     "+2+"     "+3+"     "+4+"     "+5+"     "+6+"     "+7);
			Console.Out.WriteLine("  -------------------------------------------------");
			for (int x = 0; x < 8; x++) {
				Console.Out.Write(x + " | ");
				for (int y = 0; y < 8; y++) {
					if (chessBoard[x, y] != null) {
						if (chessBoard[x, y].BlackorWhite()) { // Check Teams
							Console.Out.Write(" " + chessBoard[x, y].getType());
							if (!(chessBoard[x, y].getType().Equals("Kn")) && !(chessBoard[x, y].getType().Equals("Ki"))) {
								Console.Out.Write(" ");
							}
								Console.Out.Write(" | ");

						} else {
							Console.Out.Write(" " + chessBoard[x, y].getType());
							if (!(chessBoard[x, y].getType().Equals("Kn")) && !(chessBoard[x, y].getType().Equals("Ki"))) {
								Console.Out.Write(" ");
							}
								Console.Out.Write(" | ");
						}
						
					} else {
						Console.Out.Write("   ");
						Console.Out.Write(" | ");
					}
				}
					Console.Out.WriteLine();
					Console.Out.WriteLine("  -------------------------------------------------");
			}

		}

	}
}