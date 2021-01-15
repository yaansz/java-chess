package chess;

import boardgame.Board;
import chess.pieces.Rook;

public class ChessMatch {
	
	private Board board;
	
	public ChessMatch() {
		board = new Board(8, 8);
		InitialSetup();
		
	}
	
	/**
	 * 
	 * Place a new piece in board by the ChessPosition Logic.
	 * 
	 * @param column
	 * @param row
	 * @param piece
	 */
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
	}
	
	/**
	 * 
	 * Method to arranging chess pieces in their respective places.
	 * 
	 */
	private void InitialSetup() {
		this.placeNewPiece('b', 4, new Rook(board, Color.WHITE));
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
