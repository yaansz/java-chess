package application;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

public class Program {

	public static void main(String[] args) {
		
		ChessMatch chessMatch = new ChessMatch();
		Scanner scan = new Scanner(System.in);
		List<ChessPiece> captured =  new ArrayList<>();
		
		while(!chessMatch.getCheckMate()) {
			
			try {
				UI.clearScreen();
				UI.printMatch(chessMatch, captured);
				
				System.out.println();
				System.out.print("Source: ");
				ChessPosition source = UI.readChessPosition(scan);
				
				
				boolean[][] possibleMoves = chessMatch.possibleMoves(source);
				UI.clearScreen();
				UI.printBoard(chessMatch.getPieces(), possibleMoves);
				
				System.out.println();
				System.out.println("Target: ");
				ChessPosition target = UI.readChessPosition(scan);
				
				ChessPiece capturedPiece = chessMatch.performChessMove(source, target);		
				if(capturedPiece != null) captured.add(capturedPiece);
				if(chessMatch.getPromoted() != null) {
					System.out.print("Enter piece for promotion (B/N/R/Q): ");
					String type = scan.nextLine().toUpperCase();
					
					final Set<String> possiblePieces = Set.of(
						    "B","N","R","Q"
					);
					
					while(!possiblePieces.contains(type)) {
						System.out.print("Invalid value! Enter piece for promotion (B/N/R/Q): ");
						type = scan.nextLine().toUpperCase();
					}
					
					chessMatch.replacePromotedPiece(type);
				}
			}
			catch(ChessException e) {
				System.out.println(e.getMessage());
				scan.nextLine();
			}
			catch(InputMismatchException e) {
				System.out.println(e.getMessage());
				scan.nextLine();
			}
		}
		
		UI.clearScreen();
		UI.printMatch(chessMatch, captured);

	}

}
