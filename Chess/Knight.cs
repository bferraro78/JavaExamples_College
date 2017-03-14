using System;
using System.Collections.Generic;

namespace Chess {
	class Knight : chessPiece {
		
		protected static string type = "Kn";

		public Knight(bool isBlack, int locX, int locY) : base(isBlack, locX, locY) {
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
			// In this case, last boolean parameter does not matter
			List<Coord> validMove = allvalidMoves(board, oldLoc.getX(), oldLoc.getY(), newX, newY, true, check);

			/* Check new coords agaisnt List of Valid Moves */
			foreach (Coord c in validMove) {
				if (c.getX() == newX && c.getY() == newY) {
					// New move exsits within list of old moves
					return true;
				}
			}

			return false;

		}

		public override List<Coord> allvalidMoves(Board board, int startX, int startY, int newX, int newY, bool isBlack, bool check) {
			List<Coord> ret = new List<Coord>();

			/* Up-Left */
			int x = startX;
			int y = startY;
			if (x-1 >= 0 && y-2 >= 0){
				ret.Add(new Coord(x-1, y-2));
			}

			x = startX;
			y = startY;
			if (x-2 >= 0 && y-1 >= 0){
				ret.Add(new Coord(x-2, y-1));
			}

			/* Up-Right */
			x = startX;
			y = startY;
			if (x-2 >= 0 && y+1 < 8){
				ret.Add(new Coord(x-2, y+1));
			}

			x = startX;
			y = startY;
			if (x-1 >= 0 && y+2 < 8){
				ret.Add(new Coord(x-1, y+2));
			}

			/* Down-Right */
			x = startX;
			y = startY;
			if (x+2 < 8 && y+1 < 8){
				ret.Add(new Coord(x+2, y+1));
			}

			x = startX;
			y = startY;
			if (x+1 < 8 && y+2 < 8){
				ret.Add(new Coord(x+1, y+2));
			}


			/* Down-Left */
			x = startX;
			y = startY;
			if (x+2 < 8 && y-1 >= 0){
				ret.Add(new Coord(x+2, y-1));
			}

			x = startX;
			y = startY;
			if (x+1 < 8 && y-2 >= 0){
				ret.Add(new Coord(x+1, y-2));
			}

			return ret; // No valid move
		}

	}
}