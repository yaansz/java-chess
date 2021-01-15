package boardgame;

public abstract class Piece {
	protected Position position;
	private Board board;
	
	public Piece(Board board) {
		this.board = board;
		position = null;
	}

	protected Board getBoard() {
		return board;
	}
	
	/**
	 * Method to know which positions we can move to...
	 * 
	 * @return boolean matrix about allow moves.
	 */
	public abstract boolean[][] possibleMoves();
	
	/**
	 * Method to validate a move
	 * 
	 * @return boolean matrix about allow moves.
	 */
	public boolean possibleMove(Position position) {
		return possibleMoves()[position.getRow()][position.getColumn()];
	}
	
	public boolean isThereAnyPossibleMove() {
		boolean[][] mat = possibleMoves();
		
		for(int i = 0; i < mat.length; i++) {
			for(int j = 0; j < mat.length; j++) {
				if(mat[i][j])
					return true;
			}
		}
		
		return false;
	}
}
