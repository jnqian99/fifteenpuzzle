package fifteenpuzzle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CheckAns {
    public static void main(String[] args) {

        args = new String[] {"board6.txt", "mysol6.txt"};
        try {
            check(args[0], args[1]);
        } catch(Exception e) {
        }
    }

    public static boolean check(String puzzle, String ans) throws IOException, IllegalMoveException {
        Puzzle p = null;
		try {
			p = new Puzzle(puzzle); 
		} catch(Exception e) {
			System.out.println("Exception! "+e.getMessage());
		}
        //System.out.println(p.toString());      

        BufferedReader in = new BufferedReader(new FileReader(ans));
        String s=null;
        while((s=in.readLine())!=null) {
            String[] ss = s.split(" ");
            int tile=Integer.parseInt(ss[0]);
            int dir=0;
            if(ss[1].equals("U")) {
                dir=0;
            } else if(ss[1].equals("D")) {
                dir=1;
            } else if(ss[1].equals("L")) {
                dir=2;
            } else if(ss[1].equals("R")) {
                dir=3;
            }
            p.makeMove(tile, dir);
        }
        in.close();
        return p.isSolved();
    }
}
