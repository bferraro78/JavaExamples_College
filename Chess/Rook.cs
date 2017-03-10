using System;
using System.Collections.Generic;

namespace Chess {
	class Rook : chessPiece {
		
		protected static string type = "R";

		public Rook(bool isBlack, int locX, int locY) : base(isBlack, locX, locY) {
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

		public override bool validateMove(Coord oldLoc, int newX, int newY, Board board) {
			bool validMove = allvalidMoves(board, oldLoc.getX(), oldLoc.getY(), newX, newY, true);

			// Was the newX/newY avaible on any of the Pieces valid paths
			return validMove;
		}

		public override bool allvalidMoves(Board board, int startX, int startY, int newX, int newY, bool isBlack) {

			/* Gather Directional Movements */
			/* Left */
			for (int i = startY-1; i >= 0; i--) {
				if (board.getPiece(startX, i) == null) {
					if (i == newX && startY == newY) {
						return true;
					}
				} else { // piece there
					if (startX == newX && i == newY) {
						return true;
					} else {
						break;
					}
				}
			}

			/* Right */
			for (int i = startY+1; i < 8; i++) {
				if (board.getPiece(startX, i) == null) {
					if (i == newX && startY == newY) {
						return true;
					}
				} else { // piece there
					if (startX == newX && i == newY) {
						return true;
					} else {
						break;
					}
				}
			}

			/* Down */
			for (int i = startX+1; i < 8; i++) {
				if (board.getPiece(i, startY) == null) {
					if (i == newX && startY == newY) {
						return true;
					}
				} else { // piece there
					if (i == newX && startY == newY) {
						return true;
					} else {
						break;
					}
				}
			}

			/* Up */
			for (int i = startX-1; i >= 0; i--) {
				if (board.getPiece(i, startY) == null) {
					if (i == newX && startY == newY) {
						return true;
					}
				} else { // piece there
					if (i == newX && startY == newY) {
						return true;
					} else {
						break;
					}
				}
			}

			return false;
		}

	}
}