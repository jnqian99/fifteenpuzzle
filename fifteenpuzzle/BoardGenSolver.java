package fifteenpuzzle;

import java.io.*;
import java.util.*;

public class BoardGenSolver {
    private static void exchange(int[] arr, int i1, int i2) {
        int tmp = arr[i1];
        arr[i1]=arr[i2];
        arr[i2]=tmp;
    }

    public static String createBoard(int size, int steps) {
		int[] board = new int[size*size];
		for(int i=0; i<(size*size-1); i++)
			board[i]=i+1;
		board[size*size-1]=0;
		Random ran = new Random();
		int emp=size*size-1;
		for(int i=0; i<steps; i++) {
			// 0 UP 1 DOWN 2 LEFT 3 RIGHT - all move empty!!!
			int d = ran.nextInt(0,4);
			if(d==0) {	// UP
				if(emp/size!=0) {
					exchange(board, emp, emp-size);
					emp-=size;
				}
			} else if(d==1) { // DOWN
				if(emp/size!=(size-1)) {
					exchange(board, emp, emp+size);
					emp+=size;
				}
			} else if(d==2) { // LEFT
				if((emp % size)!=0) {
					exchange(board, emp, emp-1);
					emp-=1;
				}
			} else if(d==3) { // RIGHT
				if((emp % size)!=(size-1)) {
					exchange(board, emp, emp+1);
					emp+=1;
				}
			}
		}
		String s=size+"\n";
		for(int i=0; i<size; i++) {
			for(int j=0; j<size; j++) {
				int idx=i*size+j;
				if(board[idx]==0) {
					s+="  ";
				} else if(board[idx]>=0 && board[idx]<=9) {
					s+=" "+board[idx];
				} else {
					s+=board[idx];
				}
				if(j<(size-1))
					s+=" ";
			}
			s+="\n";
		}
		return s;
	}

    public static void main(String[] args) {
        long t1 = System.currentTimeMillis();
        int solved=0;
        int cnt=1;
		boolean custom=false;
		if(custom) {
			cnt=100;
			for(int i=0; i<cnt; i++) {
				String input="tboard"+i+".txt";
				String output="tsol"+i+".txt";
				String s=createBoard(9, 10000000);
				System.out.println(s);
				try {
					BufferedWriter out = new BufferedWriter(new FileWriter(input));
					out.write(s);
					out.close();
				} catch (IOException e) {
				}
				Solver.solve(input,output);
				boolean bSolved=false;
				try {
					bSolved=CheckAns.check(input, output);
				} catch(Exception e) {
				}
				if(bSolved)
					solved++;
			} 
		} else {
			cnt=40;
			for(int i=1; i<=cnt; i++) {
				StringBuilder s = new StringBuilder();
				s.append("board");
				if(i<10)
					s.append("0");
				s.append(i);
				s.append(".txt");
				String input=s.toString();
				String output="tsol"+i+".txt";
				Solver.solve(input,output);
				boolean bSolved=false;
				try {
					bSolved=CheckAns.check(input, output);
				} catch(Exception e) {
				}
				if(bSolved)
					solved++;
			}
		}
		
        System.out.println("Total=" + cnt + " Solved="+solved);
        long t2 = System.currentTimeMillis();
        System.out.println("time in msec: "+(t2-t1) + " Time per board:" + (t2-t1)/cnt);
    }
}
