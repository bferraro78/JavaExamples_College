using System;
using System.Collections.Generic;

namespace Chess {
	class Bishop : chessPiece {
		
		protected static string type = "B";

		public Bishop(bool isBlack, int locX, int locY) : base(isBlack, locX, locY) {
		}		

		public override string getType () {
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
			/* Same x or y not an available move */
			if (oldLoc.getX() == newX || oldLoc.getY() == newY) { 
				return false;
			} else { 
				bool validMove = allvalidMoves(board, oldLoc.getX(), oldLoc.getY(), newX, newY, true);

				// Was the newX/newY avaible on any of the Pieces valid paths
				return validMove;
				
			}
		}

		public override bool allvalidMoves(Board board, int startX, int startY, int newX, int newY, bool isBlack) {

			/* Check Diagonal Directions, as far as piece can go w/o bumping into another piece */
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