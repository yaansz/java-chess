package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Queen extends ChessPiece {

	public Queen(Board board, Color color) {
		super(board, color);
	}

	@Override
	public boolean[][] possibleMoves() {
		int rows = this.getBoard().getRows();
		int columns = this.getBoard().getColumns();
		boolean[][] canMove = new boolean[rows][columns];
		Position p = new Position(0, 0);
		
		int[] directions = {1, 0, -1};
		
		for(int i = 0; i < directions.length; i++) {
			for(int j = 0; j < directions.length; j++) {
				
				if(directions[i] == 0 && directions[j] == 0) continue;
				
				p.setValues(position.getRow() + directions[i], position.getColumn() + directions[j]);
				while(getBoard().positionExists(p) && !this.getBoard().thereIsAPiece(p)) {
					canMove[p.getRow()][p.getColumn()] = true;
					
					p.setRow   (p.getRow()    + directions[i]);
					p.setColumn(p.getColumn() + directions[j]);
				}
				
				if(getBoard().positionExists(p) && this.isThereOpponentPiece(p)) {
					canMove[p.getRow()][p.getColumn()] = true;
				}
			}
		}
			
		return canMove;
	}

	@Override
	public String toString() {
		return "Q";
	}
}
