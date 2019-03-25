/* Created by Anushree Deshpande */


package othello;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

public class othello {
	
	private int boardSize = 8;
	
	private int[] board;
	
	private int[] squaresOccupied;
	
	private boolean[] passes = {false, false};
	
	
	
	private othello() {
		board = new int[boardSize * boardSize];
		int i, j;
		for(j=0; j<boardSize * boardSize ; j++) {
			board[j] = 0;
		}
		
		// 1 is a black disc
		// -1 is a white disc
		
		board[27] = -1;
		board[28] = 1;
		board[35] = 1;
		board[36] = -1;
		
//		squaresOccupied = new int[64];
//		for(i=0 ; i<boardSize*boardSize ; i++)
//		{
//			squaresOccupied[i] = 0;
//		}
	}

	
	void displayBoard(int[] boardLocal) {
		System.out.println("  0 1 2 3 4 5 6 7");
		for(int i=0 ; i<64 ; i++)
		{
			if(i%8 == 0)
			{
				System.out.print(i/8 + " ");
			}
			if(boardLocal[i] == 0) {
				System.out.print(". ");
			}
			if(boardLocal[i] == 1) {
				System.out.print("B ");
			}
			if(boardLocal[i] == -1) {
				System.out.print("W ");
			}
			if((i+1)%8 == 0)
			{
				System.out.println();
			}
		}
	}
	
	int[] calculateLegalMoveInDirection(int index, int colour, int direction, int[] boardLocal)
	{
		int numflips = 0;
		int[] legalValue = {0,0, 0};
		while(index > 0 && index < 63)
		{
			index = index + direction;
			if(index >=0 && index <=63 ) {
				if(boardLocal[index] != -1* colour)
				{
					break;
				}
				numflips++;
			}
		}
		if(index >=0 && index <= 63)
		{
			if(boardLocal[index] == 0 && numflips!=0)
			{
				legalValue[0] = index;
				legalValue[1] = numflips;
				legalValue[2] = direction;
			}
		}
		return legalValue;
	}
	
	List<int []> computeLegalMoves(int colour, int[] boardLocal) {
		int [] temp = new int[3];
		List<int []> legalMoves=new ArrayList<int []> ();
		
		for(int i=0 ; i<64 ; i++)
		{
			if(boardLocal[i] == colour) {
				
				// rows
				temp = calculateLegalMoveInDirection(i, colour, -1, boardLocal);
				if(temp[0] !=0 && temp[1] != 0)
				{
					legalMoves.add(temp);
				}
				temp = calculateLegalMoveInDirection(i, colour, 1, boardLocal);
				if(temp[0] !=0 && temp[1] != 0)
				{
					legalMoves.add(temp);
				}
				
				//columns
				temp = calculateLegalMoveInDirection(i, colour, -8, boardLocal);
				if(temp[0] !=0 && temp[1] != 0)
				{
					legalMoves.add(temp);
				}
				
				temp = calculateLegalMoveInDirection(i, colour, 8, boardLocal);
				if(temp[0] !=0 && temp[1] != 0)
				{
					legalMoves.add(temp);
				}
				
				//diagonals
				temp = calculateLegalMoveInDirection(i, colour, -9, boardLocal);
				if(temp[0] !=0 && temp[1] != 0)
				{
					legalMoves.add(temp);
				}
				
				temp = calculateLegalMoveInDirection(i, colour, 9, boardLocal);
				if(temp[0] !=0 && temp[1] != 0)
				{
					legalMoves.add(temp);
				}
				
				temp = calculateLegalMoveInDirection(i, colour, -7, boardLocal);
				if(temp[0] !=0 && temp[1] != 0)
				{
					legalMoves.add(temp);
				}
				
				temp = calculateLegalMoveInDirection(i, colour, 7, boardLocal);
				if(temp[0] !=0 && temp[1] != 0)
				{
					legalMoves.add(temp);
				}
			}	
		}
		return legalMoves;
	}
	
	public void applyMove(int colour, int index, int[] boardLocal)
	{
		if(boardLocal[index] == 0)
		{
			boardLocal[index] = colour;
		}
	}
	
	public int promptPlayer(List<Integer> legalCoord) {
		System.out.println("Enter the coordinate where you want to place the disc");
		
		Scanner in = new Scanner(System.in);
		
		int x, y;
		
		x = in.nextInt();
		y = in.nextInt();
		
		int tempMove = x*8+y;
		
		if(! legalCoord.contains(tempMove))
		{
			System.out.println("Illegal Move. Play Again");
//			in.close();
			return promptPlayer(legalCoord);
		}
//		in.close();
		return tempMove;
	}
	
	public void flipValues(int colour, int direction, int index, int[] boardLocal) {
		
		while(index >= 0 && index <= 63) {
			index = index + direction;
			if(index >= 0 && index <= 63) {
				if(boardLocal[index] == colour) {
					break;
				}
				boardLocal[index] = colour;
			}
		}
		
	}
	
	public int calculateScore(int[] boardLocal)
	{
		int blackCount =0, whiteCount = 0;
		for(int i=0 ; i<64 ; i++)
		{
			if(boardLocal[i] == 1)
			{
				blackCount++;
			}
			else if(boardLocal[i] == -1)
			{
				whiteCount++;
			}
		}
		return (blackCount - whiteCount);
	}
	
	
	public int minimax( int depth, boolean isMaximisingPlayer, int alpha, int beta, int[] boardLocal, int colour)
	{
		if(depth == 8)
		{
			return calculateScore(boardLocal);
		}
		if(isMaximisingPlayer) {
			
			int best = Integer.MIN_VALUE;
			List<int[]> legalMoves = new ArrayList<int[]> ();
			legalMoves = computeLegalMoves(colour, boardLocal);
			
			for(int[] a: legalMoves) {
				int[] newBoard = new int[64];
				newBoard = boardLocal.clone();
				applyMove(colour, a[0], newBoard);
				//new colour for tile, direction, index
				flipValues(colour, -a[2], a[0], newBoard);
					
				int v = minimax(depth+1, !isMaximisingPlayer, alpha, beta, newBoard, -colour);
				
				best = Math.max(best, v); 
	            alpha = Math.max(alpha, best); 
	            
	            if (beta <= alpha) 
	                break;
			}
			
			return best;
		}
		else {
	
			int best = Integer.MAX_VALUE;
			List<int[]> legalMoves = new ArrayList<int[]> ();
			legalMoves = computeLegalMoves(colour, boardLocal);
			
			for(int[] a: legalMoves) {
				int[] newBoard = new int[64];
				newBoard = boardLocal.clone();
				applyMove(colour, a[0], newBoard);
				flipValues(colour, -a[2], a[0], newBoard);
				
				int v = minimax(depth+1, !isMaximisingPlayer, alpha, beta, newBoard, -colour);
				
				best = Math.min(best, v);
				beta = Math.min(beta, best);
				
				if (beta <= alpha) 
	                break; 
			}
			
			return best;
		}
	}
	
	public int computeMove(List<int []> legalMoves, int colour)
	{
		int d = 0, bestmove=-1, bestscore=Integer.MIN_VALUE, temp;
		boolean isMaximisingPlayer = true;
		for(int[] a:legalMoves) {
			int[] boardLocal = new int[64];
			boardLocal = board.clone();
			applyMove(colour, a[0], boardLocal);
			flipValues(colour,-a[2], a[0], boardLocal);
			temp = minimax(d, !isMaximisingPlayer, Integer.MIN_VALUE, Integer.MAX_VALUE, boardLocal, -colour);
			if(temp > bestscore)
			{
				bestscore = temp;
				bestmove = a[0];
			}
		}
		return bestmove;
	}
	
	public boolean checkGameOver() {
		if(passes[0] && passes[1]) {
			return true;
		}
		return false;
	}
	
	
	public static void main(String args[]) {
		
		othello o = new othello();
		boolean gameEnds = false;
		int player = 1, tempMove;
		List<int []> legalMoves=new ArrayList<int []> ();
		List<Integer> legalCoord = new ArrayList<Integer> ();
		
		//black plays first , player = 1 -> black is playing 
		//white is computer , player = -1 -> white is playing
		
		while(!gameEnds) {
			if(player == 1) {
				legalMoves.clear();
				//black to play 
				o.displayBoard(o.board);
				
				System.out.println("Black to play\n");
				
				System.out.println("Legal Moves are");
				
				legalMoves = o.computeLegalMoves(1, o.board);
				
				for(int[] a : legalMoves) {
					System.out.print("Coordinates : " + a[0]/8 + " "+ a[0]%8 + " ");
					legalCoord.add(a[0]);
					System.out.println("Number of discs flipped : " + a[1] );
				}
				
				if(legalMoves.size() == 0) {
					System.out.println("No legal move is possible!");
					if(o.passes[0] == false) {
						o.passes[0] = true;
					}
					else
					{
						o.passes[1] = true;
					}
				}
				else {
					tempMove = o.promptPlayer(legalCoord);
					o.applyMove(1, tempMove, o.board);
					
					for(int[] a : legalMoves) {
					 	if(a[0] == tempMove) {
							
							//new colour for tile, direction, index
							
							o.flipValues(1, -a[2], a[0], o.board);
						}
					}
				}
			}
			else if(player == -1) {
				legalMoves.clear();
				//white to play
				o.displayBoard(o.board);
				
				System.out.println("\nWhite to play\n");
				
				System.out.println("Legal Moves are");

				legalMoves = o.computeLegalMoves(-1, o.board);
				
				if(legalMoves.size() == 0) {
					System.out.println("No legal move is possible!");
					if(o.passes[0] == false) {
						o.passes[0] = true;
					}
					else
					{
						o.passes[1] = true;
					}
				}
				else {
					for(int[] a : legalMoves) {
						System.out.print("Coordinates : " + a[0]/8 + " "+ a[0]%8 + " ");
						legalCoord.add(a[0]);
						System.out.println("Number of discs flipped : " + a[1]);
					}
					
					tempMove = o.computeMove(legalMoves, -1);
					System.out.println("Computer chose to play at "+ tempMove/8 + " " + tempMove %8);
					o.applyMove(-1, tempMove, o.board);
					for(int[] a : legalMoves) {
						if(a[0] == tempMove) {
							
							//new colour for tile, direction, index
							
							o.flipValues(-1, -a[2], a[0], o.board);
						}
					}
				}
				
			}
			player = -player;
			gameEnds = o.checkGameOver();
		}
		
		int score = o.calculateScore(o.board);
		if(score > 0)
		{
			System.out.println("Black wins!");
		}
		else if(score < 0)
		{
			System.out.println("White wins!");
		}
		else
		{
			System.out.println("Tie!");
		}
	
	}

}
