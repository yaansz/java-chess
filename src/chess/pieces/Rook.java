package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Rook extends ChessPiece {

	public Rook(Board board, Color color) {
		super(board, color);
	}
	
	@Override
	public String toString() {
		return "R";
	}
	
	@Override
	public boolean[][] possibleMoves() {
		
		int rows = this.getBoard().getRows();
		int columns = this.getBoard().getColumns();
			
		boolean [][] canMove = new boolean[rows][columns];
		Position temp = new Position(0, 0);
		
		// ABOVE
		temp.setValues(position.getRow() - 1, position.getColumn());
		while(getBoard().positionExists(temp) && !this.getBoard().thereIsAPiece(temp)) {
			canMove[temp.getRow()][temp.getColumn()] = true;
			temp.setRow(temp.getRow() - 1);
		}
		
		if(getBoard().positionExists(temp) && this.isThereOpponentPiece(temp)) {
			canMove[temp.getRow()][temp.getColumn()] = true;
		}
		
		// BELOW
		temp.setValues(position.getRow() + 1, position.getColumn());
		while(getBoard().positionExists(temp) && !this.getBoard().thereIsAPiece(temp)) {
			canMove[temp.getRow()][temp.getColumn()] = true;
			temp.setRow(temp.getRow() + 1);
		}
		
		if(getBoard().positionExists(temp) && this.isThereOpponentPiece(temp)) {
			canMove[temp.getRow()][temp.getColumn()] = true;
		}

		// LEFT
		temp.setValues(position.getRow(), position.getColumn() - 1);
		while(getBoard().positionExists(temp) && !this.getBoard().thereIsAPiece(temp)) {
			canMove[temp.getRow()][temp.getColumn()] = true;
			temp.setColumn(temp.getColumn() - 1);
		}
		
		if(getBoard().positionExists(temp) && this.isThereOpponentPiece(temp)) {
			canMove[temp.getRow()][temp.getColumn()] = true;
		}
		
		// RIGHT
		temp.setValues(position.getRow(), position.getColumn() + 1);
		while(getBoard().positionExists(temp) && !this.getBoard().thereIsAPiece(temp)) {
			canMove[temp.getRow()][temp.getColumn()] = true;
			temp.setColumn(temp.getColumn() + 1);
		}
		
		if(getBoard().positionExists(temp) && this.isThereOpponentPiece(temp)) {
			canMove[temp.getRow()][temp.getColumn()] = true;
		}
		
		return canMove;
	}
}
