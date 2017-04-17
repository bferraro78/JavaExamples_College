/* Chess Piece Class */
using System;
using System.Collections.Generic;

namespace Chess {
	public abstract class chessPiece {
		
		public bool isBlack;
		public Coord location;

		public struct Coord
		{
    		public int locationX;
    		public int locationY;

    		public Coord(int locX, int locY) {
    			locationX = locX;
    			locationY = locY;
    		}

    		public int getX() {
    			return locationX;
    		}

    		public int getY() {
    			return locationY;
    		}

    		public int setX(int locX) {
    			return locationX = locX;
    		}

    		public int setY(int locY) {
    			return locationY = locY;
    		}


		}

		public chessPiece (bool isBlack, int locX, int locY) {
			this.isBlack = isBlack;
			location = new Coord(locX, locY);
		}

		public bool BlackorWhite() {
			return isBlack;
		}
		
		public abstract string getType();

		public int getX() {
			return location.getX();
		}
		public int getY() {
			return location.getY();
		}

		
		public abstract bool validateMove(Coord oldLoc, int newX, int newY, Board board, bool check);

		/* Tracks if the new coordinates exist in all of the valid moves of a particular piece */
		// bool check - used for determining if the new X/Y is allowed to be a blank space
		// bool isBlack - determines which team (and direction) a Pawn can move / check for check
		public abstract List<Coord> allvalidMoves(Board board, int startX, int startY, int newX, int newY, bool isBlack, bool check);

	} 
}