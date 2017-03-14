using System;
using System.Windows;

/* Driver Class */
namespace Chess {
	class Driver {
			
		public static Chess.Board chessBoard;


		static void Main() {
			chessBoard = Chess.Board.getBoard();
			chessBoard.initBoard();

			Driver.playGame();

		}

		/* Initates game */
		public static void playGame() {

			Console.Out.WriteLine("STARTING GAME");

			/* Get Team Names*/
			Console.Out.WriteLine("Black: ");
			string BlackName = Console.ReadLine();
			Console.Out.WriteLine("White: ");
			string WhiteName = Console.ReadLine();

			/* Display Names */
			Console.Out.Write("Hello! ");
			Console.Out.Write(WhiteName);
			Console.Out.Write(", ");
			Console.Out.WriteLine(BlackName);

			/* Display INIT BOARD */
			Console.Out.WriteLine();
			chessBoard.displayBoard();

			int turnCount = 0;
			bool endGame = true;
			bool isCheck = false;
			bool blackTurn = true; // keeps track of whose turn it is for Check() function
			// Outer game loop - ends when game is over
			while (endGame) {

				bool endTurn = true;
				while (endTurn) {
					/* Print Turn Name */
					if (turnCount % 2 == 0) { // Black Turn
						Console.Write(BlackName); 
						Console.WriteLine(" turn");
						blackTurn = true;
					} else { // White Turn
						Console.Write(WhiteName); 
						Console.WriteLine(" turn");		
						blackTurn = false;	
					}

				
					/* Check if team is in CHECK! */
					int code = chessBoard.Check(blackTurn, true, 0, 0);

					if (isCheck == false && code == 1) {
						Console.Out.WriteLine("YUR TEAM IS IN CHECK - MUST REMOVE KING FROM DANGER");
						isCheck = true;
					} else if (code == 2) {

// TODO						/* MUST CALL CHECK AGAIN, 
							/*  THIS TIME WITH CHECKMATE OPTION
								- For all kings available moves, check if those moves are in check ! */

						endGame = false;
						break;
					}



					/* Get Piece and New Location */
					Console.Out.WriteLine("Enter Location to Move:");

					Console.Out.WriteLine("Old X");
					int oldX = Convert.ToInt32(Console.ReadLine());
					Console.Out.WriteLine("Old Y");
					int oldY = Convert.ToInt32(Console.ReadLine());
					Console.Out.WriteLine("New X");
					int newX = Convert.ToInt32(Console.ReadLine());
					Console.Out.WriteLine("New Y");
					int newY = Convert.ToInt32(Console.ReadLine());

					/* Get Piece to Move */
				   	chessPiece movePiece; 

				   	if (chessBoard.getPiece(oldX, oldY) == null) {
				   		Console.Out.WriteLine("Blank Spot");
				   		continue;
				   	}

				   	movePiece = chessBoard.getPiece(oldX, oldY);

				   	/* Check if valid piece to Move 
				   		- Check if it is the right color
				   	*/
				   	if (turnCount % 2 == 0) { // Black Turn
						if (!(movePiece.BlackorWhite())) {
							Console.Out.WriteLine("Invalid Team Piece - Try Again");
							continue;
						}
					} else { // White Turn
						if (movePiece.BlackorWhite()) {
							Console.Out.WriteLine("Invalid Team Piece - Try Again");
							continue;
						}
					}

					/* Validate Piece Movement */

					/* 1. Check all movements direcrions for clear path to new (x,y) position
					   2. Check in Board.cs if the last spot contians an enemy or teamate
					   3. Move piece */

					// Move Pieces on Board
					if (movePiece.validateMove(movePiece.location, newX, newY, chessBoard, false)) {
					
						int errorCode = chessBoard.movePiece(oldX, oldY, newX, newY, blackTurn);
						
						if (errorCode == 1) {
							Console.Out.WriteLine("New position contains teamate!! - Try Again");
							continue;
						} else if (errorCode == 2) {
							Console.Out.WriteLine("STILL IN CHECK!!!!!");
							continue;
						}

						// VALID MOVE!!!
						Console.Out.WriteLine("GOOD MOVEE~!!!!!!");
						Console.Out.WriteLine();
					} else  {
						Console.Out.WriteLine("Invalid Move!! - Try Again");
						continue;
					}

					// if code makes it here, you can assume team is no longer in check
					isCheck = false;

					// End this turn
					endTurn = false;

					/* Last Part of Turn*/
					// 1. Update movePiece's new Location
					movePiece.location.setX(newX);
					movePiece.location.setY(newY);

				}


					// Next players Turn - Increment counter to next team's turn
					turnCount++;
					chessBoard.displayBoard();

					/* DEBUG */
					// 	endGame = false;
			}

				Console.Out.WriteLine("See ya!!");
			
				






		}

	}
}
