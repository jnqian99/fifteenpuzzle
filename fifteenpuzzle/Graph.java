package fifteenpuzzle;

import java.util.*;

public class Graph {
    public final static int UP = 0;
	public final static int DOWN = 1;
	public final static int LEFT = 2;
	public final static int RIGHT = 3;

    private int m_size;
    private int m_bsize;

    //public Vertex returnTarget() { return m_target; }

    public Graph(int size, byte[] data) {
        m_size = size;
        m_bsize = size*size;
    }

    /*
    public static int[] randomDir() {
        int[] arr = {0,1,2,3};
        Random rand = new Random();
        for (int i = arr.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
        return arr;
    }
    */

    /**
	 * 
	 * @param size: size of the board
	 * @return target Vertex
	 */
	public static Vertex getTarget(int size) {
        byte[] arr = new byte[size*size];
        for(int i=0; i<(size*size-1); i++)
            arr[i] = (byte)(i+1);
        arr[size*size-1]=0;
        return new Vertex(arr);
    }

    public static int getIndex(Vertex v, int value) {
		for(int i=0; i<v.data.length; i++) {
			if(v.data[i]==value)
				return i;
		}
		return -1; // not found
	}

    private static void exchange(byte[] arr, int i1, int i2) {
        byte tmp = arr[i1];
        arr[i1]=arr[i2];
        arr[i2]=tmp;
    }



    private Vertex action3(Vertex v, int act, int[] avoid, int level, Byte[] moved) {
        byte b[] = v.data.clone();
        int emp = getIndex(v, 0);
        int idx = 0;
        if(act == UP) {
            idx=emp+m_size;
            if(idx>=m_bsize)
                return null;
        } else if(act == DOWN) {
            idx=emp-m_size;
            if((emp/m_size)<=level)
                return null;
        } else if(act==LEFT) {
            if(emp % m_size == (m_size-1))
                return null;
            idx=emp+1;
        } else if(act==RIGHT) {
            if(emp % m_size<=level)
                return null;
            idx=emp-1;
        }
        //System.out.println("act = " + act+ " v = " + v.toString());
        for(int i=0; i<avoid.length; i++) {
            if(avoid[i]<0)
                break;            
            if((avoid[i]-1)==idx || (avoid[i]-1)==emp)
                return null;
        }
        moved[0]=b[idx];
        exchange(b, emp, idx);
        Vertex v2 = new Vertex(b);
        return v2;
    }

    private Vertex action2(Vertex v, int act, int level) {
        byte b[] = v.data.clone();
        int emp = getIndex(v, 0);
        int idx = 0;
        if(act == UP) {
            idx=emp+m_size;
            if(idx>=m_bsize)
                return null;
        } else if(act == DOWN) {
            idx=emp-m_size;
            if((emp/m_size)<=level)
                return null;
        } else if(act==LEFT) {
            if(emp % m_size == (m_size-1))
                return null;
            idx=emp+1;
        } else if(act==RIGHT) {
            if(emp % m_size<=level)
                return null;
            idx=emp-1;
        }
        //System.out.println("act = " + act+ " v = " + v.toString());
        exchange(b, emp, idx);
        Vertex v2 = new Vertex(b);
        return v2;
    }


    public void calAstar(Vertex v, Vertex target) {
        int aplus=0;
        for(int i=0; i<m_size*m_size; i++) {
            int a=0;
            int idx1=getIndex(v, i);
            int idx2=getIndex(target, i);
            a+=Math.abs(idx1/m_size-idx2/m_size);
            a+=Math.abs((idx1 % m_size) - (idx2 % m_size));   
            aplus+=a;
        }
        double[] input = new double[m_size*m_size];
        for(int i=0; i<m_size*m_size; i++)
            input[i] = (double)(v.data[i]);
        //double[] a = ann.predict(input);
        //v.aplus = aplus+(int)(a[0]);
        //System.out.println(v.aplus);
    }


    private boolean equalsTarget(Vertex v, Vertex t, int level) {
        for(int i=0; i<m_size*m_size; i++) {
            if(i/m_size==level || (i % m_size)==level) {
                if(v.data[i]!=t.data[i])
                    return false;
            }            
        }
        return true;
    }


    public Vertex findTarget_level(Vertex start, int level) {
        HashMap<Vertex,Vertex> vertices = new HashMap<>();
        PriorityQueue<Vertex> queue = new PriorityQueue<>();
        Vertex target = Graph.getTarget(m_size);
        /*
        System.out.println("start = "+start.toString());
        System.out.println("level = "+level);
        */
        if(equalsTarget(start, target, level)) {
            return start;
        }
        vertices.put(start, start);
        queue.add(start);
        while(!queue.isEmpty()) {
            Vertex v = queue.remove();
            //int[] da=randomDir();
            for(int k=0; k<4; k++) {
                int dir=k;//da[k];
                Vertex v2 = action2(v,dir,level);
                if(v2==null)
                    continue;
                //System.out.println(v2.toString() + " c="+v2.cost+ " m=" + moved[0]+ " p=" + v2.aplus + " q=" + queue.size() +" s=" + vertices.size());
                v2.parent=v;
                v2.cost=v.cost+1;
                if(equalsTarget(v2,target,level)) {
                    return v2;
                }
                boolean visited = false;
                boolean modified = false;
                calAstar_level(v2, level);
                Vertex v3=v2;
                if(vertices.containsKey(v2)) {
                    visited=true;
                    v3=vertices.get(v2);
                    if(v3.cost>v2.cost) {
                        vertices.put(v2, v2);
                        v3=v2;       
                        modified=true;                 
                    }
                } else {
                    vertices.put(v3, v3);
                }
                if(!visited) {
                    queue.add(v3);
                } else {
                    if(modified) {
                        if(queue.contains(v3)) {
                            queue.remove(v3);
                            queue.add(v3);
                        } else {
                            queue.add(v3);
                        }
                    }
                }
            }
		}
        return null;
    }

    public void calAstar_level(Vertex v, int level) {
        v.aplus = 0;
        manhattanDist(v, level, 5);
        linearConflict(v, level, 6);
        //levelAdjust(v,level,m_size/2);
        //levelAdjust2(v, level);
    }
    
    public void levelAdjust(Vertex v, int level, int times) {
        int aplus = v.aplus;
        int emp = getIndex(v, 0);
        int eX=emp%m_size;
        int eY=emp%m_size;
        int d1 = Math.abs(eX-level) + Math.abs(eY-(m_size-1));
        int d2 = Math.abs(eX-(m_size-1)) + Math.abs(eY-level);
        aplus+=(d1+d2)*times;
        v.aplus=aplus;
    }

    private boolean equalsTarget_single(Vertex v, byte b, byte gIdx) {
        return (v.data[gIdx]==b);
    }

    public Vertex findTarget_single(Vertex start, int bt, int gIdx, int[] avoid, int level) {
        HashMap<Vertex,Vertex> vertices = new HashMap<>();
        PriorityQueue<Vertex> queue = new PriorityQueue<>();
        if(equalsTarget_single(start, (byte)bt, (byte)gIdx)) {
            return start;
        }
        /*
        System.out.println("start = "+start.toString());
        System.out.println("bt = "+bt + " level = "+level);
        for(int i=0; i<avoid.length; i++) {
            System.out.print(avoid[i]+ " ");
        }
        System.out.println("");
        */
        vertices.put(start, start);
        queue.add(start);
        Byte[] moved = new Byte[1];
        while(!queue.isEmpty()) {
            Vertex v = queue.remove();
            //int[] da=randomDir();
            for(int k=0; k<4; k++) {
                int dir=k;//da[k];
                Vertex v2 = action3(v,dir, avoid, level, moved);
                if(v2==null)
                    continue;
                v2.parent=v;
                v2.cost=v.cost+1;
                if(equalsTarget_single(v2,(byte)bt,(byte)gIdx)) {
                    return v2;
                }
                boolean visited = false;
                boolean modified = false;
                calAstar_single(v2, (byte)bt, (byte)gIdx);
                //System.out.println(v2.toString() + " c="+v2.cost+ " m=" + moved[0]+ " p=" + v2.aplus + " q=" + queue.size() +" s=" + vertices.size());                
                Vertex v3=v2;
                if(vertices.containsKey(v2)) {
                    visited=true;
                    v3=vertices.get(v2);
                    if(v3.cost>v2.cost) {
                        vertices.put(v2, v2);
                        v3=v2;       
                        modified=true;                 
                    }
                } else {
                    vertices.put(v3, v3);
                }
                if(!visited) {
                    queue.add(v3);
                } else {
                    if(modified) {
                        if(queue.contains(v3)) {
                            queue.remove(v3);
                            queue.add(v3);
                        } else {
                            queue.add(v3);
                        }
                    }
                }
            }
		}
        return null;
    }

    public boolean equalsTarget_3(Vertex v, byte b1, byte b2, byte b3) {
        int idx1=getIndex(v, b1);
        int idx2=getIndex(v, b2);
        int idx3=getIndex(v, b3);
        boolean e1=(idx1==(b1-1));
        boolean e2=(idx2==(b2-1) && idx3==(b3-1));
        boolean e3=(idx3==(b2-1) && idx2==(b3-1));
        return (e1 && e2);
    }

    public Vertex findTarget_3(Vertex start, byte b1, byte b2, byte b3, int[] avoid, int level) {
        HashMap<Vertex,Vertex> vertices = new HashMap<>();
        PriorityQueue<Vertex> queue = new PriorityQueue<>();
        /*
        System.out.println("start = "+start.toString());
        System.out.println("b1="+b1+" b2="+b2 + " b3="+b3+" level="+level);
        for(int i=0; i<avoid.length; i++) {
            System.out.print(avoid[i]+ " ");
        }
        System.out.println("");
        */
        if(equalsTarget_3(start, b1, b2, b3)) {
            return start;
        }
        vertices.put(start, start);
        queue.add(start);
        Byte[] moved=new Byte[1];        
        while(!queue.isEmpty()) {
            Vertex v = queue.remove();
            //int[] da=randomDir();
            for(int k=0; k<4; k++) {
                int dir=k;//da[k];
                Vertex v2 = action3(v, dir, avoid, level, moved);
                if(v2==null)
                    continue;
                v2.parent=v;
                v2.cost=v.cost+1;
                if(equalsTarget_3(v2, b1, b2, b3)) {
                    return v2;
                }
                boolean visited = false;
                boolean modified = false;
                calAstar_3(v2, b1, b2, b3, dir, moved[0]);
                //System.out.println(v2.toString() + " c="+v2.cost+ " m=" + moved[0]+ " p=" + v2.aplus + " q=" + queue.size() +" s=" + vertices.size());
                Vertex v3=v2;
                if(vertices.containsKey(v2)) {
                    visited=true;
                    v3=vertices.get(v2);
                    if(v3.cost>v2.cost) {
                        vertices.put(v2, v2);
                        v3=v2;       
                        modified=true;                 
                    }
                } else {
                    vertices.put(v3, v3);
                }
                if(!visited) {
                    queue.add(v3);
                } else {
                    if(modified) {
                        if(queue.contains(v3)) {
                            queue.remove(v3);
                            queue.add(v3);
                        } else {
                            queue.add(v3);
                        }
                    }
                }
            }
		}
        return null;
    }


    public void calAstar_single(Vertex v, byte b, byte gIdx) {
        v.aplus=moveOne(v, b, gIdx);
    }

    public int moveOne(Vertex v, byte b, int gIdx) {
        int emp=getIndex(v, 0);
        int idx=getIndex(v, b);
        int a = 0;
        int cX = idx % m_size;
        int cY = idx / m_size;
        int gX = gIdx % m_size;
        int gY = gIdx / m_size;
        int eX = emp % m_size;
        int eY = emp / m_size;
        int md = Math.abs(cX-gX)+Math.abs(cY-gY);
        if(md==0)
            return 0;
        if(cX!=gX) {
            a+=(Math.abs(cX-gX)-1)*5+1;
        }
        if(cY!=gY) {
            a+=(Math.abs(cY-gY)-1)*5+1;
        }
        if(gX<cX) {
            // move left
            a+=Math.abs(eX-(cX-1));
            if(eX>cX && eY==cY)
                a+=2;
        } else if(gX>cX) {
            // move right
            a+=Math.abs(eX-(cX+1));
            if(eX<cX && eY==cY)
                a+=2;
        } else {
            a+=Math.abs(eX-cX);
        }
        if(gY<cY) {
            // move up
            a+=Math.abs(eY-(cY-1));
            if(eY>cY && eX==cX)
                a+=2;
        } else if(gY>cY) {
            // move down
            a+=Math.abs(eY-(cY+1));
            if(eY<cY && eX==cX)
                a+=2;
        } else {
            a+=Math.abs(eY-cY);
        }
        return a;
    }



    public void calAstar_3(Vertex v, byte b1, byte b2, byte b3, int dir, byte moved) {
        //int times=m_size*4;
        //manhattanDist(v,b1,times);
        //manhattanDist(v,b2,times);
        //manhattanDist(v,b3,times);
        proximityBoost(v,b1,b2,b3);
        //linearConflict(v, b1, b2, b3, 2);
    }

    public int manhattan(int idx1, int idx2) {
        int x1=idx1%m_size;
        int y1=idx1/m_size;
        int x2=idx2%m_size;
        int y2=idx2/m_size;
        return Math.abs(x1-x2) + Math.abs(y1-y2);
    }

    /*
     * whether it is beneficial for moving b toward its goal
     * @param idx - index of b
     * 
     */
    public int movingNum(Vertex v, byte b, int idx, int emp, int dir, byte moved) {
        int gX=(b-1)%m_size;
        int gY=(b-1)/m_size;
        int cX=idx%m_size;
        int cY=idx/m_size;
        int eX=emp%m_size;
        int eY=emp/m_size;
        int a=0;
        if(gX<cX) {
            if(moved!=b) {
                //a+=1;
                if(eX>cX && dir==LEFT)
                    a+=1;
                if(eX<cX && dir==RIGHT)                
                    a+=1;
            } else {
                if(dir==RIGHT)
                    a+=2;           
            }
        } else if(gX>cX) {
            if(moved!=b) {
                //a+=1;
                if(eX>cX && dir==LEFT)
                    a+=1;
                if(eX<cX && dir==RIGHT)                
                    a+=1;
            } else {
                if(dir==LEFT)
                    a+=2;
            }
        }

        if(gY<cY) {
            if(moved!=b) {
                //a+=1;
                if(eY>cY && dir==UP)
                    a+=1;
                if(eY<cY && dir==DOWN)                
                    a+=1;
            } else {
                if(dir==DOWN)
                    a+=2;           
            }
        } else if(gY>cY){
            if(moved!=b) {
                //a+=1;
                if(eY>cY && dir==UP)
                    a+=1;
                if(eY<cY && dir==DOWN)                
                    a+=1;
            } else {
                if(dir==UP)
                    a+=2;
            }
        }
        return a;
    }

    public void proximityBoost(Vertex v, byte b1, byte b2, byte b3) {
        int a=0;
        int emp=getIndex(v, 0);
        int idx1=getIndex(v, b1);
        int idx2=getIndex(v, b2);
        int idx3=getIndex(v, b3);
        int d1=manhattan(idx1, b1-1);
        int d2=manhattan(idx2, b2-1);
        int d3=manhattan(idx3, b3-1);
        if(b1/m_size==b2/m_size) {
            // same row
            a+=2*(manhattan(emp, b1-1+m_size)+manhattan(emp, b2-1+m_size)+manhattan(emp, b3-1+m_size));
            a+=5*(d1+d2+d3);
            v.aplus+=a;
        } else {
            // same col
            a+=2*(manhattan(emp, b1-1+1)+manhattan(emp, b2-1+1)+manhattan(emp, b3-1+1));
            a+=5*(d1+d2+d3);
            v.aplus+=a;
        }
    }

    public void proximityBoost(Vertex v, int pos1, int pos2, int times) {
        int aplus=v.aplus;
        int a=0;
        int emp=getIndex(v, 0);
        int eX = emp % m_size;
        int eY = emp / m_size;
        int x1 = pos1 % m_size;
        int y1 = pos1 / m_size;
        int x2 = pos2 % m_size;
        int y2 = pos2 / m_size;
        a += Math.abs(eX-x1) + Math.abs(eY-y1);
        a += Math.abs(eX-x2) + Math.abs(eY-y2);
        a *= times;
        v.aplus = aplus+a;
    }

    public int manhattanDist(Vertex v, byte b, int times) {
        int aplus = v.aplus;
        int gX = (b-1)%m_size;
        int gY = (b-1)/m_size;
        int idx = getIndex(v, b);
        int x = idx%m_size;
        int y = idx/m_size;
        int a = 0;
        a += Math.abs(gX-x);
        a += Math.abs(gY-y);
        a *= times;        
        v.aplus=aplus+a;
        return a;
    }

    public int manhattanDist(byte[] data) {
        int aplus=0;
        // manhattan distance
        for(int i=0; i<m_size*m_size; i++) {
            int a=0;
            int d = data[i];
            int gX = (d-1) % m_size;
            int gY = (d-1) / m_size;
            int x = i % m_size;
            int y = i / m_size;
            aplus+=Math.abs(x-gX);
            aplus+=Math.abs(y-gY);   
        }
        return aplus;
    }

    public void manhattanDist(Vertex v, int level, int times) {
        int aplus=v.aplus;
        // manhattan distance
        for(int i=0; i<m_size*m_size; i++) {
            int a=0;
            int d = v.data[i];
            int gX = (d-1) % m_size;
            int gY = (d-1) / m_size;
            int x = i % m_size;
            int y = i / m_size;
            a+=Math.abs(x-gX);
            a+=Math.abs(y-gY);   
            a*=times;
            if(gX==level || gY==level) {
                aplus+=a;
            }
        }
        v.aplus=aplus;
    }

    public int linearConflict(byte[] data) {
        int con=0;
        int inc=2;
        for(int i=0; i<m_size*m_size; i++) {
            int d = data[i];
            int gX = (d-1) % m_size;
            int gY = (d-1) / m_size;
            int x = i % m_size;
            int y = i / m_size;
            if(gX==x && gY==y)
                continue;
            if(gY==y) {
                for(int j=i+1; j<x*m_size; j++) {
                    int d2=data[j];
                    int x2=j % m_size;
                    int y2=j / m_size;
                    int gX2=(d2-1) % m_size;
                    int gY2=(d2-1) / m_size;
                    if(gY2==y2 && gX2<gX)
                        con+=inc;
                }
            }
            if(gX==x) {
                for(int j=i+m_size; j<m_size*m_size; j+=m_size) {
                    int d2=data[j];
                    int x2=j % m_size;
                    int y2=j / m_size;
                    int gX2=(d2-1) % m_size;
                    int gY2=(d2-1) / m_size;
                    if(gX2==x2 && gY2<gY)
                        con+=inc;
                }
            }
        }
        return con;
    }

    public void linearConflict(Vertex v, int level, int times) {
        int aplus = v.aplus;
        int inc=times;
        for(int i=0; i<m_size*m_size; i++) {
            int d = v.data[i];
            int gX = (d-1) % m_size;
            int gY = (d-1) / m_size;
            int x = i % m_size;
            int y = i / m_size;
            if(gX==x && gY==y)
                continue;
            if(gY==y) {
                for(int j=i+1; j<x*m_size; j++) {
                    int d2=v.data[j];
                    int x2=j % m_size;
                    int y2=j / m_size;
                    int gX2=(d2-1) % m_size;
                    int gY2=(d2-1) / m_size;
                    if(gY2==y2 && gX2<gX) {
                        if(gY==level)
                            aplus+=inc;
                    }
                }
            }
            if(gX==x) {
                for(int j=i+m_size; j<m_size*m_size; j+=m_size) {
                    int d2=v.data[j];
                    int x2=j % m_size;
                    int y2=j / m_size;
                    int gX2=(d2-1) % m_size;
                    int gY2=(d2-1) / m_size;
                    if(gX2==x2 && gY2<gY) {
                        if(gX==level)
                            aplus+=inc;
                    }
                }
            }
        }
        v.aplus = aplus;
    }

    public void linearConflict(Vertex v, byte b1, byte b2, byte b3, int times) {
        int aplus=v.aplus;
        int inc=times;
        int idx1=getIndex(v, b1);
        int idx2=getIndex(v, b2);
        int idx3=getIndex(v, b3);
        int x1=idx1 % m_size;
        int y1=idx1 / m_size;
        int x2=idx2 % m_size;
        int y2=idx2 / m_size;
        int x3=idx3 % m_size;
        int y3=idx3 / m_size;

        int gX1=(b1-1)%m_size;
        int gY1=(b1-1)/m_size;
        int gX2=(b2-1)%m_size;
        int gY2=(b2-1)/m_size;
        int gX3=(b3-1)%m_size;
        int gY3=(b3-1)/m_size;
        if(gY1==gY2) {
            // in X direction
            if(x1<gX1)
                aplus+=inc;
            if(x2<gX2)
                aplus+=inc;
            if(x3<gX3)
                aplus+=inc;
        } else {
            if(y1<gY1)
                aplus+=inc;
            if(y2<gY2)
                aplus+=inc;
            if(y3<gY3)
                aplus+=inc;
        }
        v.aplus = aplus;
    }


    public static String solToStr(Vertex target, int size) {
        // from m_target to m_start
        String s = "";
        if(target==null)
            return s;
        Vertex v = target;
        while(v.parent!=null) {
            int emp1 = getIndex(v.parent, 0);
            int emp2 = getIndex(v, 0);
            int n = v.parent.data[emp2];
            String d = "";
            if((emp1-emp2)==size)
                d="D";
            if((emp2-emp1)==size)
                d="U";
            if((emp1-emp2)==1)
                d="R";
            if((emp2-emp1)==1)
                d="L";
            s=""+n+" "+d+"\n"+s;
            v=v.parent;
        }
        return s;
    }
}


