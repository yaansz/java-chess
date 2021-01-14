package chess;

import boardgame.Board;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {
	
	private Board board;
	
	public ChessMatch() {
		board = new Board(8, 8);
		InitialSetup();
		
	}
	
	/**
	 * 
	 * Method to arranging chess pieces in their respective places.
	 * 
	 */
	private void InitialSetup() {
		board.placePiece(new Rook(board, Color.WHITE), new Position(2, 1));
		board.placePiece(new King(board, Color.BLACK), new Position(2, 1));
	}
	
	/**
	 * 	Function to get all available pieces in the board
	 * 
	 * @return
	 */
	public ChessPiece[][] getPieces() {
		ChessPiece[][] chessPieces = new ChessPiece[board.getRows()][board.getColumns()];
		
		for(int i = 0; i < board.getRows(); i++)
			for(int j = 0; j < board.getColumns(); j++)
				chessPieces[i][j] = (ChessPiece) board.piece(i, j);
		
		return chessPieces;
	}
}
