package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece {

	public King(Board board, Color color) {
		super(board, color);
	}
	
	private boolean canMove(Position position) {
		ChessPiece p = (ChessPiece) getBoard().piece(position);
		
		return p == null || p.getColor() != getColor();
	}
	
	
	@Override
	public String toString() {
		return "K";
	}

	@Override
	public boolean[][] possibleMoves() {
		
		int rows = this.getBoard().getRows();
		int columns = this.getBoard().getColumns();
				
		boolean[][] canMove = new boolean[rows][columns];
		Position p = new Position(0, 0);
		
		for(int x = -1; x <= 1; x++) {
			for(int y = -1; y <= 1; y++) {
				p.setValues(position.getRow() + x, position.getColumn() + y);
				if(getBoard().positionExists(p) && canMove(p))
					canMove[p.getRow()][p.getColumn()] = true;
			}
		}
				
		return canMove;
	}
}
