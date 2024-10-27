import java.util.ArrayList; // see https://docs.oracle.com/en/java/javase/22/docs/api/java.base/java/util/ArrayList.html (has contains(), add(), remove())
import java.util.Collections;
import java.util.List;
import java.lang.String; // https://docs.oracle.com/javase/8/docs/api/java/lang/String.html


public class CurrencyTrading {

	public static class vrtx {
	    final private String l; // vertex label
	    final private java.util.ArrayList<edg> I; // incoming neighbors
	    private int color;
	    private double d;
	    private vrtx p;
	    public vrtx(String l) { // see https://docs.oracle.com/en/java/javase/22/docs/api/java.base/java/util/ArrayList.html (has contains(), add(), remove())
	        this.l = l;
	        I = new java.util.ArrayList<>();
	        color = 0;
	        d = 0;
	        p = null;  
	    }
	    /** @return vertex label */
	    final public String getLabel() { return l; }
	    /** @param e edge to be added */
	    final public void add(edg e) { if(! I.contains(e)) { I.add(e); } }
	    final public java.util.ArrayList<edg> getNeighbors() { return I; }
	    /** @param color color of the vertex */
	    final public void setColor(int color) { this.color = color; }
	    final public int getColor() { return color; }
	    /** @param d distance to the vertex */
	    final public void setDistance(double d) { this.d = d; }
	    final public double getDistance() { return d; }
	    /** @param p parent of the vertex */
	    final public void setParent(vrtx p) { this.p = p; }
	    final public vrtx getParent() { return p; }
	}

	public static class edg {
	    final private vrtx vF, vT;
	    private double w;
	    /**
	     * @param vF vertex which influences
	     * @param vT influenced vertex
	     * @param w the intensities by which vertex influences by this edge
	     */
	    public edg(vrtx vF,vrtx vT,double w) {
	        this.vF = vF; this.vT = vT; this.w = w;
	    }
	    /**
	     * @return vertex from which edge projects
	     */
	    public final vrtx getVertexF() { return vF; }
	    /**
	     * @return vertex to which edge projects
	     */
	    public final vrtx getVertexT() { return vT; }
	    /**
	     * @return edge strength
	     */
	    public final double getWeight() { return w; }
	    /**
	     * @param w weight
	     */
	    public final void setWeight(double w) { this.w = w; }
	}
	
	private static ArrayList<vrtx> graph;
	private static ArrayList<edg> edges;
	private static int n;
	
	
	public CurrencyTrading(ArrayList<vrtx> g, ArrayList<edg> e) {
		graph = g;
		edges = e;
		n = g.size();
	}
	
	private static void initSingleSource(vrtx src) {
		for (vrtx v: graph) {
			v.setDistance(Double.POSITIVE_INFINITY);
			v.setParent(null);
		}
		src.setDistance(0);
	}
	
	private static void relax(vrtx u, vrtx v, double w) {
		if (u.getDistance() + w < v.getDistance()) {
			v.setDistance(u.getDistance() + w);
			v.setParent(u);
		}
	}
	
	private static List<vrtx> bellmanFord(vrtx src) {
		initSingleSource(src);
		for (int i = 1; i < n; i++) {
			for (edg e: edges) {
				relax(e.getVertexF(),e.getVertexT(),e.getWeight());
			}
		}
		for (edg e: edges) {
			if (e.getVertexF().getDistance() + e.getWeight() < e.getVertexT().getDistance()) {
				List<vrtx> cycle = new ArrayList<>();
				vrtx v = e.getVertexF();
        for (int i = 0; i < n; i++) {
            v = v.getParent();
        }
        vrtx start = v;
        do {
            cycle.add(v);
            v = v.getParent();
        } while (v != start);
        cycle.add(start);
        Collections.reverse(cycle);
        return cycle;
			 }
		}
		return null;
	}
	
	public static void findSolution(vrtx src) {
		List<vrtx> cycle = bellmanFord(src);
		if (cycle != null) {
			System.out.println("Sure profit sequence of trades:");
			for (int i = 0; i < cycle.size(); i++) {
			    System.out.print(cycle.get(i).getLabel());
			    if (i < cycle.size() - 1) {
			        System.out.print(" -> ");
			    }
			}
			System.out.println();
  } else {
      System.out.println("No sure profit. Least expensive paths:");
      for (vrtx v : graph) {
          if (v != src) {
              List<String> path = new ArrayList<>();
              vrtx current = v;
              while (current != null) {
                  path.add(current.getLabel());
                  current = current.getParent();
              }
              Collections.reverse(path);
              System.out.println(String.join(" -> ", path));
          }
      }
  }
		
	}
	
	public static void main(String args[]) {
		
			ArrayList<vrtx> graph = new ArrayList<vrtx>();
		
			//create vertex objects
			vrtx d = new vrtx("dollar");
			vrtx e = new vrtx("euro");
			vrtx y = new vrtx("yen");
			vrtx pe = new vrtx("peso");
			vrtx po = new vrtx("pound");
			
			
			//create edge objects 
			//we take the negative log of each currency exchange weight as the weight so that the problem is changed into a shortest path problem
			edg edge1 = new edg(d, e, -Math.log(0.925));
			edg edge2 = new edg(d, y, -Math.log(156.2));
			edg edge3 = new edg(d, po, -Math.log(0.781));
			edg edge4 = new edg(d, pe, -Math.log(18.4));
			edg edge5 = new edg(e, d, -Math.log(1.08));
			edg edge6 = new edg(e, y, -Math.log(169.04));
			edg edge7 = new edg(e, po, -Math.log(0.85));
			edg edge8 = new edg(e, pe, -Math.log(19.91));
			edg edge9 = new edg(y, e, -Math.log(0.0059));
			edg edge10 = new edg(y, d, -Math.log(0.0064));
			edg edge11 = new edg(y, po, -Math.log(0.005));
			edg edge12 = new edg(y, pe, -Math.log(0.117));
			edg edge13 = new edg(po, e, -Math.log(1.17));
			edg edge14 = new edg(po, y, -Math.log(199.05));
			edg edge15 = new edg(po, d, -Math.log(1.27));
			edg edge16 = new edg(po, pe, -Math.log(23.2));
			edg edge17 = new edg(pe, e, -Math.log(0.05));
			edg edge18 = new edg(pe, y, -Math.log(8.49));
			edg edge19 = new edg(pe, po, -Math.log(0.043));
			edg edge20 = new edg(pe, d, -Math.log(0.054));
			
			
			//create arraylist of edges
			
			ArrayList<edg> edges = new ArrayList<edg>();
			
			edges.add(edge20);
			edges.add(edge19);
			edges.add(edge18);
			edges.add(edge17);
			edges.add(edge16);
			edges.add(edge15);
			edges.add(edge14);
			edges.add(edge13);
			edges.add(edge12);
			edges.add(edge11);
			edges.add(edge10);
			edges.add(edge9);
			edges.add(edge8);
			edges.add(edge7);
			edges.add(edge6);
			edges.add(edge5);
			edges.add(edge4);
			edges.add(edge3);
			edges.add(edge2);
			edges.add(edge1);
			
			//add edges to edge list of each vertex
			d.add(edge1);
			d.add(edge2);
			d.add(edge3);
			d.add(edge4);
			e.add(edge5);
			e.add(edge6);
			e.add(edge7);
			e.add(edge8);
			y.add(edge9);
			y.add(edge10);
			y.add(edge11);
			y.add(edge12);
			po.add(edge13);
			po.add(edge14);
			po.add(edge15);
			po.add(edge16);
			pe.add(edge17);
			pe.add(edge18);
			pe.add(edge19);
			pe.add(edge20);
			
			graph.add(d);
			graph.add(e);
			graph.add(y);
			graph.add(po);
			graph.add(pe);
			
			//initialize adjacency list
			CurrencyTrading CT = new CurrencyTrading(graph, edges);
			
			findSolution(d);
			
	}
}
