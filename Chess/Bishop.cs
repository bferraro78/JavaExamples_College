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

		public override bool validateMove(Coord oldLoc, int newX, int newY, Board board, bool check) {
			/* Same x or y not an available move */
			if (oldLoc.getX() == newX || oldLoc.getY() == newY) { 
				return false;
			} else { 
				List<Coord> validMove = allvalidMoves(board, oldLoc.getX(), oldLoc.getY(), newX, newY, true, check);

				// Was the newX/newY avaible on any of the Pieces valid paths
				if (validMove.Count != 0) {
					return true;
				}
					return false;
				
			}
		}

		public override List<Coord> allvalidMoves(Board board, int startX, int startY, int newX, int newY, bool isBlack, bool check) {
			List<Coord> ret = new List<Coord>();

			/* Check Diagonal Directions, as far as piece can go w/o bumping into another piece */
			/* Down-Right */
			int tmpX = startX+1;
			int tmpY = startY+1;
			while (tmpX < 8 && tmpY < 8) {
				if (board.getPiece(tmpX, tmpY) == null) { 
					if (tmpX == newX && tmpY == newY && !check) {
						ret.Add(new Coord(tmpX, tmpY));
						break;
					}
					if (check) {
						// add space don't break
						ret.Add(new Coord(tmpX, tmpY));
					}
						tmpX++;
						tmpY++;
				} else { // piece there
					if (tmpX == newX && tmpY == newY) {
						ret.Add(new Coord(tmpX, tmpY));
						break;
					} else { 
						break;
					}
				}
			}

			/* Down-Left */
			tmpX = startX+1;
			tmpY = startY-1;
			while (tmpX < 8 && tmpY >= 0) {
				if (board.getPiece(tmpX, tmpY) == null) { 
					if (tmpX == newX && tmpY == newY && !check) {
						ret.Add(new Coord(tmpX, tmpY));
						break;
					}
					if (check) {
						// add space don't break
						ret.Add(new Coord(tmpX, tmpY));
					}
						tmpX++;
						tmpY--;
				} else { // piece there
					if (tmpX == newX && tmpY == newY) {
						ret.Add(new Coord(tmpX, tmpY));
						break;
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
					if (tmpX == newX && tmpY == newY && !check) {
						ret.Add(new Coord(tmpX, tmpY));
						break;
					}
					if (check) {
						// add space don't break
						ret.Add(new Coord(tmpX, tmpY));
					}
						tmpX--;
						tmpY++;
				} else { // piece there
					if (tmpX == newX && tmpY == newY) {
						ret.Add(new Coord(tmpX, tmpY));
						break;
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
					if (tmpX == newX && tmpY == newY && !check) {
						ret.Add(new Coord(tmpX, tmpY));
						break;
					}
					if (check) {
						// add space don't break
						ret.Add(new Coord(tmpX, tmpY));
					}
						tmpX--;
						tmpY--;
				} else { // piece there
					if (tmpX == newX && tmpY == newY) {
						ret.Add(new Coord(tmpX, tmpY));
						break;
					} else { // not king, don't add
						break;
					}
				}
			}
				
			return ret;
		}

	}
}