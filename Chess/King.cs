using System;
using System.Collections.Generic;

namespace Chess {
	class King : chessPiece {
		
		protected static string type = "Ki";

		public King(bool isBlack, int locX, int locY) : base(isBlack, locX, locY) {
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
			// In this case, last boolean parameter does not matter
			bool validMove = allvalidMoves(board, oldLoc.getX(), oldLoc.getY(), newX, newY, true);

			// Was the newX/newY avaible on any of the Pieces valid paths
			return validMove;
		}


		public override bool allvalidMoves(Board board, int startX, int startY, int newX, int newY, bool isBlack) {
			List<Coord> ret = new List<Coord>();

			/* Up */
			if (startX-1 >= 0) {
				ret.Add(new Coord(startX-1, startY));
			}

			/* Up-Right */
			if (startX-1 >= 0 && startY+1 < 8) {
				ret.Add(new Coord(startX-1, startY+1));
			}

			/* Right */
			if (startY+1 < 8) {
				ret.Add(new Coord(startX, startY+1));
			}

			/* Down-Right */
			if (startX+1 < 8 && startY+1 < 8) {
				ret.Add(new Coord(startX+1, startY+1));
			}

			/* Down */
			if (startX+1 < 8) {
				ret.Add(new Coord(startX+1, startY));
			}

			/* Down-Left */
			if (startX+1 < 8 && startY-1 >= 0) {
				ret.Add(new Coord(startX+1, startY-1));
			}

			/* Left */
			if (startY-1 >= 0) {
				ret.Add(new Coord(startX, startY-1));
			}

			/* Up-Left */
			if (startX-1 >= 0 && startY-1 >= 0) {
				ret.Add(new Coord(startX-1, startY-1));
			}

			/* Check new coords agaisnt List of Valid Moves */
			foreach (Coord c in ret) {
				if (c.getX() == newX && c.getY() == newY) {
					// New move exsits within list of old moves
					return true;
				}
			}

			return false; // No move was valid
		}

	}
}