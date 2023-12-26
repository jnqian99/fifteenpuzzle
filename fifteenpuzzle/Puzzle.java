package fifteenpuzzle;

import java.io.*;
import java.util.*;

public class Puzzle {


	public final static int UP = 0;
	public final static int DOWN = 1;
	public final static int LEFT = 2;
	public final static int RIGHT = 3;

	// size of board
	public int m_size;

	// board of the game
	private byte[] m_board;

	public byte[] getBoard() { return m_board; }

	public int getSize() { return m_size; }
	
	private int getIndex(int value) {
		for(int i=0; i<m_size*m_size; i++) {
			if(m_board[i]==value)
				return i;
		}
		return -1; // not found
	}
	/**
	 * @param fileName 
	 * @throws FileNotFoundException if file not found
	 * @throws BadBoardException if the board is incorrectly formatted
	 * Reads a board from file and creates the board
	 */
	public Puzzle(String fileName) throws IOException, BadBoardException {
		BufferedReader input = new BufferedReader(new FileReader(fileName));
		String ssize=input.readLine();
		m_size=-1;
		try {
			m_size = Integer.parseInt(ssize);
		} catch (Exception e) {
			input.close();
			throw new BadBoardException("size incorrect");
		}
		if(m_size<1 || m_size>9) {
			input.close();
			throw new BadBoardException("size of board must be between 1 to 9");
		}
		m_board = new byte[m_size*m_size];
		int idx=0;
		for(int i=0; i<m_size; i++) {
			String s = input.readLine();
			//System.out.println(s);
			if(s==null) {
				input.close();
				throw new BadBoardException("Too few lines");
			}
			if(s.length()!=(m_size*3-1)) {
				input.close();
				throw new BadBoardException("Invalid line length");
			}
			for(int j=0; j<s.length(); j++) {
				String s0=s.substring(j, j+1);
				if(!s0.equals(" ") && !(s0.compareTo("0")>=0 && s0.compareTo("9")<=0)) {
					input.close();
					throw new BadBoardException("Not number or space");
				}
			}
			// check space
			for(int j=0; j<(m_size-1); j++) {
				String s1 = s.substring(j*3+2,j*3+3);
				if(!s1.equals(" ")) {
					input.close();
					throw new BadBoardException("invalid space symbol");
				}
			}
			for(int j=0; j<m_size; j++) {
				String s4 = s.substring(j*3, j*3+2);
				if(s4.equals("  ")) {
					m_board[idx++]=0;
					continue;
				}
				int x=0;
				try {
					x=Integer.parseInt(s4.stripLeading());
				} catch (Exception e) {
					input.close();
					throw new BadBoardException("Number format error");
				}
				if(x<1 || x>(m_size*m_size-1)) {
					input.close();
					throw new BadBoardException("Contains illegal number "+x);
				}
				if(x>0 && x<10) {
					if(!s4.substring(0,1).equals(" ")) {
						input.close();
						throw new BadBoardException("Contains illegal number "+s4);
					}
				}
				m_board[idx++] = (byte)x;
			}
		}
		String se = input.readLine();
		while(se!=null) {
			if(se.strip().length()>0) {
				input.close();
				throw new BadBoardException("Extra lines");
			}
			se = input.readLine();
		}
		for(int i=0; i<m_size*m_size; i++) {
			int id = getIndex(i);
			if(id==-1) {
				input.close();
				throw new BadBoardException("Cannot find "+i);
			}
		}
		input.close();
		
		//System.out.println(toString());		
	}





	/**
	 * Get the number of the tile, and moves it to the specified direction
	 * 
	 * @throws IllegalMoveException if the move is illegal
	 */
	public void makeMove(int tile, int direction) throws IllegalMoveException {
		int idx = getIndex(tile);
		int emp = getIndex(0);
		if(direction==UP) {
			if((idx-emp)!=m_size)
				throw new IllegalMoveException("Wrong move");
		}
		if(direction==DOWN) {
			if((emp-idx)!=m_size) {
				throw new IllegalMoveException("Wrong move");
			}
		}
		if(direction==LEFT) {
			if((idx-emp)!=1 || ((idx % m_size) == 0) ) {
				throw new IllegalMoveException("Wrong move");
			}
		}
		if(direction==RIGHT) {
			if((emp-idx)!=1 || ((idx % m_size)==(m_size-1)) ) {
				throw new IllegalMoveException("Wrong move");
			}
		}
		m_board[emp]=(byte)tile;
		m_board[idx]=0;
	}

	
	/**
	 * @return true if and only if the board is solved,
	 * i.e., the board has all tiles in their correct positions
	 */
	public boolean isSolved() {
		for(int i=0; i<(m_size*m_size-1); i++) {
			if(m_board[i]!=(i+1))
				return false;
		}
		boolean b = (m_board[m_size*m_size-1]==0);
		return b;
	}
	
	@Override
	public String toString() {
		String s="";
		for(int i=0; i<m_size*m_size; i++) {
			if(m_board[i]==0)
				s+="  ";
			else if(m_board[i]>0 && m_board[i]<10) {
				s+=" "+m_board[i];
			} else {
				s+=m_board[i];
			}
			if(((i+1) % m_size)!=0) {
				s+=" ";
			} else {
				s+="\n";
			}
		}
		return s;
	}
}
