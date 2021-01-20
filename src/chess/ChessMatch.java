package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Queen;
import chess.pieces.Rook;

public class ChessMatch {
	
	private Board board;
	private int turn;
	private Color currentPlayer;
	private boolean check;
	private boolean checkMate;
	
	private ChessPiece enPassantVulnerable;
	
	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();
	
	public ChessMatch() {
		
		turn = 1;
		currentPlayer = Color.WHITE;
		
		board = new Board(8, 8);
		InitialSetup();
		
	}
	
	/**
	 * 
	 * Method to increase +1 in turn and change player's color...
	 * 
	 */
	private void nextTurn() {
		turn += 1;
		currentPlayer = currentPlayer == Color.WHITE ? Color.BLACK : Color.WHITE;
	}
	
	/**
	 * Method to check all possible moves for a piece
	 * 
	 * @param sourcePosition
	 * @return a boolean matrix
	 */
	public boolean[][] possibleMoves(ChessPosition sourcePosition) {
		
		Position position = sourcePosition.toPosition();
		
		validateSourcePosition(position);
		
		return board.piece(position).possibleMoves();
		
	}
	
	/**
	 * Change from a ChessPosition source to a ChessPosition destination.
	 * 
	 * @param sourcePosition
	 * @param targetPosition
	 * @return
	 */
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		
		validateSourcePosition(source);
		validateTargetPosition(source, target);
		
		Piece capturedPiece = makeMove(source, target);
	
		// did u put yourself in check?
		if(this.testCheck(this.currentPlayer)) {
			this.undoMove(source, target, capturedPiece);
			
			throw new ChessException("You can't put yourself in check!");
		}
		
		// en passant
		ChessPiece movedPiece = (ChessPiece) this.board.piece(target);
		
		// did u put your opponent in check? :D
		check = this.testCheck(this.opponent(this.currentPlayer));
		
		// did u put your opponent in check-mate? :DDDDDDD
		checkMate = this.testCheckMate(this.opponent(currentPlayer));
		
		// if i'm not in a check-mate I can play the next turn
		if(!checkMate) nextTurn();
		
		// en passant part 2
		this.enPassantVulnerable = movedPiece instanceof Pawn && Math.abs(target.getRow() - source.getRow()) == 2 ? movedPiece : null;
		
		return (ChessPiece) capturedPiece;
	}
	
	/**
	 * 
	 * Move piece on source to target.
	 * 
	 * @param source
	 * @param target
	 * @return Possible piece on target position, if there's nothing it just return null
	 */
	private Piece makeMove(Position source, Position target) {
		ChessPiece catcher = (ChessPiece) board.removePiece(source);
		catcher.increaseMoveCount();
		Piece captured = board.removePiece(target);
		
		
		board.placePiece(catcher, target);
		
		if(captured != null) {
			this.piecesOnTheBoard.remove(captured);
			this.capturedPieces.add(captured);
		}
		
		// castling kingside rook
		if(catcher instanceof King && target.getColumn() == source.getColumn() + 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
			Position targetT = new Position(source.getRow(), source.getColumn() + 1);
			
			ChessPiece rook = (ChessPiece) board.removePiece(sourceT);
			board.placePiece(rook, targetT);
			
			rook.increaseMoveCount();
			
		}
		
		// castling queenside rook
		if(catcher instanceof King && target.getColumn() == source.getColumn() - 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
			Position targetT = new Position(source.getRow(), source.getColumn() - 1);
			
			ChessPiece rook = (ChessPiece) board.removePiece(sourceT);
			board.placePiece(rook, targetT);
			
			rook.increaseMoveCount();
			
		}
		
		// en passant
		if(catcher instanceof Pawn && source.getColumn() != target.getColumn() && captured == null) {
			Position pawnPosition;
			
			if(catcher.getColor() == Color.WHITE)
				pawnPosition = new Position(target.getRow() + 1, target.getColumn());
			else
				pawnPosition = new Position(target.getRow() - 1, target.getColumn());
			
			captured = board.removePiece(pawnPosition);
			this.piecesOnTheBoard.remove(captured);
			this.capturedPieces.add(captured);
		}
		
		
		return captured;
	}
	
	private void undoMove(Position source, Position target, Piece capturedPiece) {
		ChessPiece p = (ChessPiece) board.removePiece(target);
		p.decreaseMoveCount();
		board.placePiece(p, source);
		
		if(capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			this.piecesOnTheBoard.add(capturedPiece);
			this.capturedPieces.remove(capturedPiece);
		}
		
		
		// castling kingside rook
		if(p instanceof King && target.getColumn() == source.getColumn() + 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
			Position targetT = new Position(source.getRow(), source.getColumn() + 1);
			
			ChessPiece rook = (ChessPiece) board.removePiece(targetT);
			board.placePiece(rook, sourceT);
			
			rook.decreaseMoveCount();
			
		}
		
		// castling queenside rook
		if(p instanceof King && target.getColumn() == source.getColumn() - 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
			Position targetT = new Position(source.getRow(), source.getColumn() - 1);
			
			ChessPiece rook = (ChessPiece) board.removePiece(targetT);
			board.placePiece(rook, sourceT);
			
			rook.decreaseMoveCount();		
		}
		
		// en passant
		if(p instanceof Pawn && source.getColumn() != target.getColumn() && capturedPiece == this.enPassantVulnerable) {
			Position pawnPosition;
			ChessPiece pawn = (ChessPiece) board.removePiece(target);
			
			if(p.getColor() == Color.WHITE)
				pawnPosition = new Position(3, target.getColumn());
			else
				pawnPosition = new Position(4, target.getColumn());
			
			board.placePiece(pawn, pawnPosition);
		}
	}
	
	private void validateSourcePosition(Position position) {
		if(!board.thereIsAPiece(position)) throw new ChessException("There is no piece on source position");
		if(currentPlayer != ((ChessPiece) this.board.piece(position)).getColor()) throw new ChessException("The chosen piece is not yours.");
		if(!board.piece(position).isThereAnyPossibleMove()) throw new ChessException("There is no possible moves for the chosen piece.");
	}
	
	private void validateTargetPosition(Position source, Position target) {		
		if(!this.board.piece(source).possibleMove(target)) throw new ChessException("The chosen piece can't move to target position");
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
		piecesOnTheBoard.add(piece);
	}
	
	/**
	 * 
	 * Method with the logic to check if a piece is in check.
	 * 
	 * @param color
	 * @return
	 */
	private boolean testCheck(Color color) {
		Position kingPosition = this.king(color).getChessPosition().toPosition();
		
		List<Piece> opponentPieces = 
				this.piecesOnTheBoard.stream().
				filter(x -> ((ChessPiece) x).getColor() == this.opponent(color)).
				collect(Collectors.toList()); 
		
		for(Piece p : opponentPieces) if(p.possibleMove(kingPosition)) return true;
		
		return false;
	}
	
	private boolean testCheckMate(Color color) {
		
		if(!testCheck(color)) return false;
		
		List<Piece> list = 
				this.piecesOnTheBoard.stream().
				filter(x -> ((ChessPiece) x).getColor() == color).
				collect(Collectors.toList()); 
		
		// Checking if there is a movement to remove the check.
		for(Piece p : list) {
			boolean[][] possibleMoves = p.possibleMoves();
			
			for(int i = 0; i < possibleMoves.length; i++) {
				for(int j = 0; j < possibleMoves.length; j++) {
					
					if(possibleMoves[i][j]) {
						Position source = ((ChessPiece) p).getChessPosition().toPosition();
						Position target = new Position(i, j);
						Piece capturedPiece = makeMove(source, target);
						
						boolean testCheck = testCheck(color);
						undoMove(source, target, capturedPiece);
						
						if(!testCheck) return false;
					}
					
				}
			}
		}
		
		return true;
	}
	
	
	/**
	 * Method for returning the opponent of the current color.
	 * 
	 * @param color
	 * @return
	 */
	private Color opponent(Color color) {
		return color == Color.WHITE ? Color.BLACK : Color.WHITE;
	}
	
	/**
	 * 	Method to find the king of the selected color
	 * 
	 * @param color
	 * @return
	 */
	private ChessPiece king(Color color) {
		List<Piece> list = this.piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color).collect(Collectors.toList());
		
		for(Piece p : list)
			if(p instanceof King) return (ChessPiece) p;
		throw new IllegalStateException("There is no " + color + "king on the board");
	}
	
	/**
	 * 
	 * Method to arranging chess pieces in their respective places.
	 * 
	 */
	private void InitialSetup() {
		
		placeNewPiece('a', 1, new Rook(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE, this));
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));
        placeNewPiece('a', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('b', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('c', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('d', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('e', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('f', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('g', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('h', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('b', 1, new Knight(board, Color.WHITE));
        placeNewPiece('g', 1, new Knight(board, Color.WHITE));
        placeNewPiece('d', 1, new Queen(board, Color.WHITE));

        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
        placeNewPiece('e', 8, new King(board, Color.BLACK, this));
        placeNewPiece('h', 8, new Rook(board, Color.BLACK));
        placeNewPiece('a', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('b', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('c', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('d', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('e', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('f', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('g', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('h', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('b', 8, new Knight(board, Color.BLACK));
        placeNewPiece('g', 8, new Knight(board, Color.BLACK));
        placeNewPiece('d', 8, new Queen(board, Color.BLACK));
	}
	
	/**
	 * 
	 *  Getters and Setters
	 * 
	 */
	
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

	public int getTurn() {
		return turn;
	}
	
	public boolean getCheck() {
		return check;
	}
	
	public boolean getCheckMate() {
		return checkMate;
	}

	public Color getCurrentPlayer() {
		return currentPlayer;
	}
	
	public ChessPiece getEnPassantVulnerable() {
		return this.enPassantVulnerable;
	}
}
