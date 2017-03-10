using System;
using System.Collections.Generic;

namespace Chess {
	class Queen : chessPiece {
		
		protected static string type = "Q";

		public Queen(bool isBlack, int locX, int locY) : base(isBlack, locX, locY) {
		}		

		public override string getType() {
			return type;
		}

		public override bool BlackorWhite() {
			return isBlack;
		}

		public override int getX() {
			return location.getX();
		}

		public override int getY() {
			return location.getY();
		}

		public override bool validateMove(Coord oldLoc, int newX, int newY, Board board) {
			bool validMove = allvalidMoves(board, oldLoc.getX(), oldLoc.getY(), newX, newY, true);

			// Was the newX/newY avaible on any of the Pieces valid paths
			return validMove;

		}


		public override bool allvalidMoves(Board board, int startX, int startY, int newX, int newY, bool isBlack) {

			/* Gather Directional Movements */

			/* Same Plane Movement */
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

			/* Diagonals */
			/* Down-Right */
			int tmpX = startX+1;
			int tmpY = startY+1;
			while (tmpX < 8 && tmpY < 8) {
				if (board.getPiece(tmpX, tmpY) == null) { 
					if (tmpX == newX && tmpY == newY) {
						return true;
					}
						tmpX++;
						tmpY++;
				} else { // piece there
					if (tmpX == newX && tmpY == newY) {
						return true;
					} else { // not king, don't add
						break;
					}
				}
			}

			/* Down-Left */
			tmpX = startX+1;
			tmpY = startY-1;
			while (tmpX < 8 && tmpY >= 0) {
				if (board.getPiece(tmpX, tmpY) == null) { 
					if (tmpX == newX && tmpY == newY) {
						return true;
					}
						tmpX++;
						tmpY--;
				} else { // piece there
					if (tmpX == newX && tmpY == newY) {
						return true;
					} else { // not king, don't add
						break;
					}
				}	
			}
		
			/* Up-Right */
			tmpX = startX-1;
			tmpY = startY+1;
			while (tmpX >= 0 && tmpY < 8) {
				if (board.getPiece(tmpX, tmpY) == null) {
					if (tmpX == newX && tmpY == newY) {
						return true;
					}
						tmpX--;
						tmpY++;
				} else { // piece there
					if (tmpX == newX && tmpY == newY) {
						return true;
					} else { // not king, don't add
						break;
					}
				}
			}
			/* Up-Left */
			tmpX = startX-1;
			tmpY = startY-1;
			while (tmpX >= 0 && tmpY >= 0) {
				if (board.getPiece(tmpX, tmpY) == null) { 
					if (tmpX == newX && tmpY == newY) {
						return true;
					}
						tmpX--;
						tmpY--;
				} else { // piece there
					if (tmpX == newX && tmpY == newY) {
						return true;
					} else { // not king, don't add
						break;
					}
				}
			}


			return false;
		}

	}
}