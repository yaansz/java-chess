package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece {
	
	private ChessMatch chessMatch;
	
	public King(Board board, Color color, ChessMatch chessMatch) {
		super(board, color);
		
		this.chessMatch = chessMatch;
	}
	
	private boolean canMove(Position position) {
		ChessPiece p = (ChessPiece) getBoard().piece(position);
		
		return p == null || p.getColor() != getColor();
	}
	
	private boolean testRookCastling(Position position) {
		ChessPiece p = (ChessPiece) getBoard().piece(position);
		
		return p != null 
				&& p instanceof Rook 
				&& p.getColor() == getColor() 
				&& p.getMoveCount() == 0;
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
		
		// Default movements
		for(int x = -1; x <= 1; x++) {
			for(int y = -1; y <= 1; y++) {
				p.setValues(position.getRow() + x, position.getColumn() + y);
				if(getBoard().positionExists(p) && canMove(p))
					canMove[p.getRow()][p.getColumn()] = true;
			}
		}
		
		// castling - roque
		if(getMoveCount() == 0 && !chessMatch.getCheck()) {
			// castling kingside rook 
			Position posT1 = new Position(position.getRow(), position.getColumn() + 3);
			
			if(this.testRookCastling(posT1)) {
				Position p1 = new Position(position.getRow(), position.getColumn() + 1);
				Position p2 = new Position(position.getRow(), position.getColumn() + 2);
				
				if(this.getBoard().piece(p1) == null && this.getBoard().piece(p2) == null) {
					canMove[position.getRow()][position.getColumn() + 2] = true;
				}
			}
			
			// castling queenside rook 
			Position posT2 = new Position(position.getRow(), position.getColumn() - 4);
			
			if(this.testRookCastling(posT2)) {
				Position p1 = new Position(position.getRow(), position.getColumn() - 1);
				Position p2 = new Position(position.getRow(), position.getColumn() - 2);
				Position p3 = new Position(position.getRow(), position.getColumn() - 3);
				
				if(this.getBoard().piece(p1) == null 
						&& this.getBoard().piece(p2) == null
						&& this.getBoard().piece(p3) == null) {
					canMove[position.getRow()][position.getColumn() - 2] = true;
				}
			}
			
		}
				
		return canMove;
	}
}
