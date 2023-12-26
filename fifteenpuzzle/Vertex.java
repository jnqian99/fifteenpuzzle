package fifteenpuzzle;

public class Vertex implements Comparable<Vertex> {

    public final static int UP = 0;
	public final static int DOWN = 1;
	public final static int LEFT = 2;
	public final static int RIGHT = 3;

    public byte[] data=null;
    public Vertex parent=null;
    public int cost = 0;
    public int aplus = 0;
    private int hash=0;
    public Vertex(byte[] data) {
        this.data = new byte[data.length];
        System.arraycopy(data, 0, this.data, 0, data.length);
        hash=0;
        for(int i=0; i<data.length; i++) {
            hash+=hash*83+data[i];
        }
    }
    //public byte moved=0;

    private int getIndex(int value) {
		for(int i=0; i<data.length; i++) {
			if(data[i]==value)
				return i;
		}
		return -1; // not found
	}

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(this==obj)
            return true;
        if(obj==null)
            return false;
        if(!(obj instanceof Vertex))
            return false;
        Vertex v = (Vertex) obj;
        /*
        if(v.data==null && data==null)
            return false;
        if(data!=null && v.data==null)
            return false;
        if(data==null && v.data!=null)
            return false;
        if(this.data.length!=v.data.length)
            return false;
        */
        if(this.hashCode()!=v.hashCode())
            return false;
        for(int i=0; i<this.data.length; i++) {
            if(this.data[i]!=v.data[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int compareTo(Vertex o) {
        if(equals(o))
            return 0;
        return cost+aplus-o.cost-o.aplus;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for(int i=0; i<data.length; i++) {
            s.append(data[i]);
            s.append(" ");
        }
        return s.toString();
    }
}
