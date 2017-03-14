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

		public override bool validateMove(Coord oldLoc, int newX, int newY, Board board, bool check) {
			List<Coord> validMove = allvalidMoves(board, oldLoc.getX(), oldLoc.getY(), newX, newY, true, check);

			// Was the newX/newY avaible on any of the Pieces valid paths
			if (validMove.Count != 0) {
				return true;
			}
				return false;

		}

		public override List<Coord> allvalidMoves(Board board, int startX, int startY, int newX, int newY, bool isBlack, bool check) {
			List<Coord> ret = new List<Coord>();

			/* Gather Same Plane Directional Movements */
			/* Left */
			for (int i = startY-1; i >= 0; i--) {
				if (board.getPiece(startX, i) == null) {
					if (startX == newX && i == newY && !check) {
						ret.Add(new Coord(startX, i));
						break;
					}
					if (check) {
						// add space don't break
						ret.Add(new Coord(startX, i));
					}
				} else { // piece there
					if (startX == newX && i == newY) {
						ret.Add(new Coord(startX, i));
						break;
					} else {
						break;
					}
				}
			}

			/* Right */
			for (int i = startY+1; i < 8; i++) {
				if (board.getPiece(startX, i) == null) {
					if (startX == newX && i == newY && !check) {
						ret.Add(new Coord(startX, i));
						break;
					}
					if (check) {
						// add space don't break
						ret.Add(new Coord(startX, i));
					}
				} else { // piece there
					if (startX == newX && i == newY) {
						ret.Add(new Coord(startX, i));
						break;
					} else {
						break;
					}
				}
			}

			/* Down */
			for (int i = startX+1; i < 8; i++) {
				if (board.getPiece(i, startY) == null) {
					if (i == newX && startY == newY && !check) {
						ret.Add(new Coord(i, startY));
						break;
					}

					if (check) {
						// add space don't break
						ret.Add(new Coord(i, startY));
					}
				} else { // piece there
					if (i == newX && startY == newY) {
						ret.Add(new Coord(i, startY));
						break;
					} else {
						break;
					}
				}
			}

			/* Up */
			for (int i = startX-1; i >= 0; i--) {
				if (board.getPiece(i, startY) == null) {
					if (i == newX && startY == newY && !check) {
						ret.Add(new Coord(i, startY));
						break;
					}
					if (check) {
						// add space don't break
						ret.Add(new Coord(i, startY));
					}
				} else { // piece there
					if (i == newX && startY == newY) {
						ret.Add(new Coord(i, startY));
						break;
					} else {
						break;
					}
				}
			}

			return ret;
		}

	}
}