using System;
using System.Collections.Generic;

namespace Chess {
	class Pawn : chessPiece {
		
		protected static string type = "P";

		public Pawn(bool isBlack, int locX, int locY) : base(isBlack, locX, locY) {
		}		

		public override string getType() {
			return type;
		}

		public override bool BlackorWhite() {
			return isBlack;
		}

		public override int getX() {
			return location.locationX;
		}

		public override int getY() {
			return location.locationY;
		}

		public override bool validateMove(Coord oldLoc, int newX, int newY, Board board, bool check) {
			List<Coord> validList = validMoves(board, oldLoc.getX(), oldLoc.getY(), newX, newY, check);

			/* Check new coords agaisnt List of Valid Moves, while checking if path is clear
			   on the way to the new Coords */
			foreach (Coord c in validList) {
				if (c.getX() == newX && c.getY() == newY) {
					// Valid Move
					return true;
				}
				
			}
			return false;
		}

		private List<Coord> validMoves(Board board, int startX, int startY, int newX, int newY, bool check) { 
			List<Coord> ret = new List<Coord>();
			
			/* Check 3 movements - if in-bounds and the spot is empty 
			   or has an enemy */

			if (startX > newX) { // To check which direction a team can move
				/* White Team moving up board */
				if (!board.getPiece(startX, startY).BlackorWhite()) {
					// Can't attack straight on, spot must be empty && in bounds       
					if (startX-1 >= 0 && board.getPiece(startX-1, startY) == null) {
						ret.Add(new Coord(startX-1, startY));
					}

					// Check attackting directions - can't move unless an enemy is there
					/* Up-Left */
					if (startX-1 >= 0 && startY-1 >= 0 && board.getPiece(startX-1, startY-1) != null) {
						if (board.getPiece(startX, startY).BlackorWhite() != board.getPiece(startX-1, startY-1).BlackorWhite()) {
							ret.Add(new Coord(startX-1, startY-1));
						}
					}

					/* Up-Right */
					if (startX-1 >= 0 && startY+1 < 8 && board.getPiece(startX-1, startY+1) != null) {
						if (board.getPiece(startX, startY).BlackorWhite() != board.getPiece(startX-1, startY+1).BlackorWhite()) {
							ret.Add(new Coord(startX-1, startY+1));
						}
					}
				}
			} else {
				/* Black Team moving down board */
				if (board.getPiece(startX, startY).BlackorWhite()) {
					// Can't attack straight on, spot must be empty && in bounds
					if (startX+1 < 8 && board.getPiece(startX+1, startY) == null) {
						ret.Add(new Coord(startX+1, startY));
					}

					// Check attackting directions - can't move unless an enemy is there
					/* Down-Left */
					if (startX+1 >= 0 && startY-1 >= 0 && board.getPiece(startX+1, startY-1) != null) {
						if (board.getPiece(startX, startY).BlackorWhite() != board.getPiece(startX+1, startY-1).BlackorWhite()) {
							ret.Add(new Coord(startX+1, startY-1));
						}
					}

					/* Down-Right */
					if (startX+1 < 8 && startY+1 < 8 && board.getPiece(startX+1, startY+1) != null) {
						if (board.getPiece(startX, startY).BlackorWhite() != board.getPiece(startX+1, startY+1).BlackorWhite()) {
							ret.Add(new Coord(startX+1, startY+1));
						}
					}
				}
			}
			return ret;
		}

		/* FUNC FOR CHECK checking PURPOSES */
		public override List<Coord> allvalidMoves(Board board, int startX, int startY, int newX, int newY, bool isBlack, bool check) { 
			List<Coord> ret = new List<Coord>();
			
			/* Check Directions */
			/* isBlack role is reversed -- if blackTurn, check white piece movement */
			if (isBlack) {
				/* White Team moving up board */
				if (!board.getPiece(startX, startY).BlackorWhite()) {
					// Can't attack straight on, spot must be empty && in bounds       
					if (startX-1 >= 0 && board.getPiece(startX-1, startY) == null) {
						ret.Add(new Coord(startX-1, startY));
					}

					// Check attackting directions - can't move unless an enemy is there
					/* Up-Left */
					if (startX-1 >= 0 && startY-1 >= 0 && board.getPiece(startX-1, startY-1) != null) {
						if (board.getPiece(startX, startY).BlackorWhite() != board.getPiece(startX-1, startY-1).BlackorWhite()) {
							ret.Add(new Coord(startX-1, startY-1));
						}
					}

					/* Up-Right */
					if (startX-1 >= 0 && startY+1 < 8 && board.getPiece(startX-1, startY+1) != null) {
						if (board.getPiece(startX, startY).BlackorWhite() != board.getPiece(startX-1, startY+1).BlackorWhite()) {
							ret.Add(new Coord(startX-1, startY+1));
						}
					}
				}
			} else {
				/* Black Team moving down board */
				if (board.getPiece(startX, startY).BlackorWhite()) {
					// Can't attack straight on, spot must be empty && in bounds
					if (startX+1 < 8 && board.getPiece(startX+1, startY) == null) {
						ret.Add(new Coord(startX+1, startY));
					}

					// Check attackting directions - can't move unless an enemy is there
					/* Down-Left */
					if (startX+1 >= 0 && startY-1 >= 0 && board.getPiece(startX+1, startY-1) != null) {
						if (board.getPiece(startX, startY).BlackorWhite() != board.getPiece(startX+1, startY-1).BlackorWhite()) {
							ret.Add(new Coord(startX+1, startY-1));
						}
					}

					/* Down-Right */
					if (startX+1 < 8 && startY+1 < 8 && board.getPiece(startX+1, startY+1) != null) {
						if (board.getPiece(startX, startY).BlackorWhite() != board.getPiece(startX+1, startY+1).BlackorWhite()) {
							ret.Add(new Coord(startX+1, startY+1));
						}
					}
				}
			}

			return ret; 
		}


	}
}