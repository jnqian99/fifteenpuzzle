package fifteenpuzzle;

import java.io.*;
import java.lang.invoke.MethodHandles;

public class Solver {

	public static void main(String[] args) {
//		System.out.println("number of arguments: " + args.length);
//		for (int i = 0; i < args.length; i++) {
//			System.out.println(args[i]);
//		}

		//args = new String[] {"board3.txt", "mysol3.txt"};

		if (args.length < 2) {
			System.out.println("File names are not specified");
			System.out.println("usage: java " + MethodHandles.lookup().lookupClass().getName() + " input_file output_file");
			return;
		}

		solve(args[0], args[1]);
	}

	public static void solve(String input, String output) {
		Puzzle p = null;
		try {
			p = new Puzzle(input); 
		} catch(Exception e) {
			System.out.println("Exception! "+e.getMessage());
			return;
		}

		int size=p.getSize();

		if(size<1 || size>9) {
			System.out.println("Board Size must be a positive integer between 1 and 9");
		}
		Graph g = new Graph(p.getSize(), p.getBoard());
		Vertex start = new Vertex(p.getBoard());
		Vertex target = start;

		for(int level=0; level<size-3; level++) {
			int lsize=size-level;
			int[] avoid = new int[lsize*lsize];
			for(int i=0; i<lsize*lsize; i++)
				avoid[i]=-1;
			int aidx=0;	
			// fit row
			byte bi=0;
			for(int i=level*size+level; i<level*size+size-2; i++) {
				bi = (byte)Graph.getIndex(target, i+1);
				target = g.findTarget_single(target,i+1,(bi/size)*size+(i%size), avoid, level);
				target = g.findTarget_single(target,i+1,i, avoid, level);
				avoid[aidx++]=i+1;
			}
			byte b1=(byte)(level*size+size-3+1);
			byte b2=(byte)(level*size+size-2+1);
			byte b3=(byte)(level*size+size-1+1);
			bi = (byte)Graph.getIndex(target, b2);
			target = g.findTarget_single(target,b2,(bi/size)*size+(size-2), avoid, level);
			target = g.findTarget_single(target,b2,b2-1, avoid, level);
			bi = (byte)Graph.getIndex(target, b3);		
			target = g.findTarget_single(target,b3,(bi/size)*size+(size-1), avoid, level);
			target = g.findTarget_single(target,b3,b3-1, avoid, level);
			// fit rest 3 numbers
			avoid[--aidx]=-1;
			for(int i=(level+2)*size+level; i<(level+2)*size+size; i++) {
				avoid[aidx++]=i+1;
			}
			target = g.findTarget_3(target,b1,b2,b3,avoid,level);
			for(int i=(level+2)*size+level; i<(level+2)*size+size; i++) {
				avoid[--aidx]=-1;
			}			
			avoid[aidx++]=b1;
			avoid[aidx++]=b2;
			avoid[aidx++]=b3;			

			// fit column
			for(int i=level*size+level+size; i<(size-2)*size+level; i+=size) {
				bi = (byte)Graph.getIndex(target, i+1);
				target = g.findTarget_single(target, i+1, (i/size)*size+(bi%size), avoid, level);
				target = g.findTarget_single(target, i+1, i, avoid, level);
				avoid[aidx++]=i+1;
			}
			b1=(byte)((size-3)*size+level+1);
			b2=(byte)((size-2)*size+level+1);
			b3=(byte)((size-1)*size+level+1);
			bi = (byte)Graph.getIndex(target, b2);
			target = g.findTarget_single(target,b2,(size-2)*size+(bi%size), avoid, level);
			target = g.findTarget_single(target,b2,b2-1, avoid, level);
			bi = (byte)Graph.getIndex(target, b3);
			target = g.findTarget_single(target,b3,(size-1)*size+(bi%size), avoid, level);
			target = g.findTarget_single(target,b3,b3-1, avoid, level);
			// fit rest 3 numbers
			avoid[--aidx]=-1;
			for(int i=level*size+level+2; i<(size-1)*size+level+2; i+=size) {
				avoid[aidx++]=i+1;
			}			
			target = g.findTarget_3(target,b1,b2,b3,avoid,level);
			for(int i=level*size+level+2; i<(size-1)*size+level+2; i+=size) {
				avoid[--aidx]=-1;
			}						
			avoid[aidx++]=b1;
			avoid[aidx++]=b2;
			avoid[aidx++]=b3;
		}
		for(int i=Math.max(0, size-3); i<size-1; i++) {
			target = g.findTarget_level(target, i);
		}

		/*
		Vertex v = target;
		while(true) {
			System.out.println("solve: "+v);
			if(v.parent==null)
				break;
			v = v.parent;
		}
		*/
		
		String s=Graph.solToStr(target,size);
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(output));
			out.write(s);
			out.close();
		}catch(IOException e) {

		}

	}

}
