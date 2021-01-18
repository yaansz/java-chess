package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Knight extends ChessPiece {

	public Knight(Board board, Color color) {
		super(board, color);
	}

	private boolean canMove(Position position) {
		ChessPiece p = (ChessPiece)getBoard().piece(position);
		return p == null || p.getColor() != getColor();
	}
	
	@Override
	public boolean[][] possibleMoves() {
		
		int rows = this.getBoard().getRows();
		int columns = this.getBoard().getColumns();
		boolean[][] canMove = new boolean[rows][columns];
		Position p = new Position(0, 0);
		
		int[] directions = {1, -1, 2, -2};
		
		for(int i = 0; i < directions.length; i++) {
			for(int j = 0; j < directions.length; j++) {
				if(Math.abs(directions[i]) == Math.abs(directions[j])) continue;
				
				p.setValues(position.getRow() + directions[i], position.getColumn() + directions[j]);
				if (getBoard().positionExists(p) && canMove(p)) canMove[p.getRow()][p.getColumn()] = true;
			}
		}
		
		return canMove;
	}

	@Override
	public String toString() {
		return "N";
	}
	
	
	
}
