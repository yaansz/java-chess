package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class Pawn extends ChessPiece {
	
	private ChessMatch chessMatch;
	
	public Pawn(Board board, Color color, ChessMatch chessMatch) {
		super(board, color);
		
		this.chessMatch = chessMatch;
	}

	@Override
	public boolean[][] possibleMoves() {
		
		int rows = this.getBoard().getRows();
		int columns = this.getBoard().getColumns();
		
		int direction = this.getColor() == Color.WHITE ? -1 : 1;
		
		boolean[][] canMove = new boolean[rows][columns];
		Position p = new Position(0, 0);
		Position p2 = new Position(0, 0);
		
		// General Rule
		p.setValues(position.getRow() + direction, position.getColumn());
		if(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) 
			canMove[p.getRow()][p.getColumn()] = true;
		
		// First Movement
		p.setValues(position.getRow() + 2 * direction, position.getColumn());
		p2.setValues(position.getRow() + direction, position.getColumn());
		if(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p) &&
		getBoard().positionExists(p2) && !getBoard().thereIsAPiece(p2) &&
		getMoveCount() == 0) 
			canMove[p.getRow()][p.getColumn()] = true;
		
		// EATING PIECES
		// LEFT
		p.setValues(position.getRow() + direction, position.getColumn() - 1);
		if(getBoard().positionExists(p) && this.isThereOpponentPiece(p)) 
			canMove[p.getRow()][p.getColumn()] = true;
		
		// RIGHT
		p.setValues(position.getRow() + direction, position.getColumn() + 1);
		if(getBoard().positionExists(p) && this.isThereOpponentPiece(p)) 
			canMove[p.getRow()][p.getColumn()] = true;
		
		
		// EN PASSANT
		// WHITE
		
		
		if(position.getRow() == 3 && this.getColor() == Color.WHITE) {
			Position left = new Position(position.getRow(), position.getColumn() - 1);
			
			if(getBoard().positionExists(left) && this.isThereOpponentPiece(left) 
					&& getBoard().piece(left) == chessMatch.getEnPassantVulnerable()) {
				canMove[left.getRow() -1][left.getColumn()] = true;
			}
			
			Position right = new Position(position.getRow(), position.getColumn() + 1);
			
			if(getBoard().positionExists(right) && this.isThereOpponentPiece(right) 
					&& getBoard().piece(right) == chessMatch.getEnPassantVulnerable()) {
				canMove[right.getRow() -1][right.getColumn()] = true;
			}
		}
		
		if(position.getRow() == 4 && this.getColor() == Color.BLACK) {
			Position left = new Position(position.getRow(), position.getColumn() - 1);
			
			if(getBoard().positionExists(left) && this.isThereOpponentPiece(left) 
					&& getBoard().piece(left) == chessMatch.getEnPassantVulnerable()) {
				canMove[left.getRow() + 1][left.getColumn()] = true;
			}
			
			Position right = new Position(position.getRow(), position.getColumn() + 1);
			
			if(getBoard().positionExists(right) && this.isThereOpponentPiece(right) 
					&& getBoard().piece(right) == chessMatch.getEnPassantVulnerable()) {
				canMove[right.getRow() + 1][right.getColumn()] = true;
			}
		}
		
		return canMove;
	}

	@Override
	public String toString() {
		return "P";
	}
	
	
}
