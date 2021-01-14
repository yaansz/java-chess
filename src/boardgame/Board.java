package boardgame;

public class Board {
	private final int rows;
	private final int columns;
	private Piece[][] pieces;
	
	
	public Board(int rows, int columns) {
		
		if(rows < 1 || columns < 1) throw new BoardException("Error creating board: there must be at least 1 row and 1 column.");
		
		this.rows = rows;
		this.columns = columns;
		
		pieces = new Piece[rows][columns];
	}
	
	
	/**
	 * 
	 * 	Function to get a specific piece on the board by the position.
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	public Piece piece(int row, int column) {
		
		if(!this.positionExists(row, column)) throw new BoardException("Position not on the board");
		
		return this.pieces[row][column];
	}
	
	public Piece piece(Position position) {
		return this.piece(position.getRow(), position.getColumn());
	}
	
	/**
	 * 	Function to put a piece on a board position
	 * 
	 * @param piece
	 * @param position
	 */
	public void placePiece(Piece piece, Position position) {
		
		if(this.thereIsAPiece(position)) throw new BoardException("There is already a piece on position " + position);
		
		this.pieces[position.getRow()][position.getColumn()] = piece;
		piece.position = position;
	}
	
	/**
	 * Method to checking whether a position is valid.
	 * 
	 * @param position
	 * @return
	 */
	public boolean positionExists(Position position) {
		return positionExists(position.getRow(), position.getColumn());
	}
	
	/**
	 * Auxiliary method to check if a position is valid.
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	private boolean positionExists(int row, int column) {
		return row >= 0 && row < this.rows 
				&& 
				column >= 0 && column < this.columns;
	}
	
	/**
	 * 	Checking if a position is already occupied
	 * 
	 * @param position
	 * @return
	 */
	public boolean thereIsAPiece(Position position) {
		if(!this.positionExists(position.getRow(), position.getColumn())) throw new BoardException("Position not on the board");
		
		return piece(position) != null;
	}
	
	
	
	/**
	 * 
	 * Getters and Setters
	 * 
	 */
	
	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}
	
}
