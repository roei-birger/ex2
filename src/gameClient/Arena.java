package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;
import gameClient.util.Range2Range;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a multi Agents Arena which move on a graph - grabs Pokemons .
 */
public class Arena {
    public static final double EPS1 = 0.001, EPS2 = EPS1 * EPS1, EPS = EPS2;
    private directed_weighted_graph _gg;
    private List<CL_Agent> _agents;
    private static List<CL_Pokemon> _pokemons;
    private List<String> _info;
    private static Point3D MIN = new Point3D(0, 100, 0);
    private static Point3D MAX = new Point3D(0, 100, 0);
    private static int count = 0;

    /**
     * a default constructor
     */
    public Arena() {

        _info = new ArrayList<String>();
    }

    /**
     * Constructs an Arena from received data.
     *
     * @param g,r,p
     */
    private Arena(directed_weighted_graph g, List<CL_Agent> r, List<CL_Pokemon> p) {
        _gg = g;
        this.setAgents(r);
        this.setPokemons(p);
    }

    /**
     * sets the _pokemons parameter of the Arena.
     *
     * @param f
     */
    public void setPokemons(List<CL_Pokemon> f) {

        this._pokemons = f;
    }

    /**
     * sets the _agents parameter of the Arena.
     *
     * @param f
     */
    public void setAgents(List<CL_Agent> f) {

        this._agents = f;
    }

    /**
     * sets the _gg parameter of the Arena.
     *
     * @param g
     */
    public void setGraph(directed_weighted_graph g) {
        this._gg = g;
    }

    /**
     * @return the _agents parameter of the Arena.
     */
    public List<CL_Agent> getAgents() {

        return _agents;
    }

    /**
     * @return the _pokemons parameter of the Arena.
     */
    public List<CL_Pokemon> getPokemons() {

        return _pokemons;
    }

    /**
     * @return the _gg parameter of the Arena.
     */
    public directed_weighted_graph getGraph() {

        return _gg;
    }

    /**
     * @return the _info parameter of the Arena.
     */
    public List<String> get_info() {

        return _info;
    }

    /**
     * set's the _info parameter of the Arena.
     *
     * @param _info
     */
    public void set_info(List<String> _info) {

        this._info = _info;
    }


    /**
     * @param aa
     * @param gg
     * @return the list of agent's parameters that were created from JSON file.
     */
    public static List<CL_Agent> getAgents(String aa, directed_weighted_graph gg) {
        ArrayList<CL_Agent> ans = new ArrayList<CL_Agent>();
        try {
            JSONObject ttt = new JSONObject(aa);
            JSONArray ags = ttt.getJSONArray("Agents");
            for (int i = 0; i < ags.length(); i++) {
                CL_Agent c = new CL_Agent(gg, 0);
                c.update(ags.get(i).toString());
                ans.add(c);
            }
            //= getJSONArray("Agents");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ans;
    }

    /**
     * @param fs
     * @return the list of Pokemon's parameters that were created from JSON file.
     */
    public static ArrayList<CL_Pokemon> json2Pokemons(String fs) {
        ArrayList<CL_Pokemon> ans = new ArrayList<CL_Pokemon>();
        try {
            JSONObject ttt = new JSONObject(fs);
            JSONArray ags = ttt.getJSONArray("Pokemons");
            for (int i = 0; i < ags.length(); i++) {
                JSONObject pp = ags.getJSONObject(i);
                JSONObject pk = pp.getJSONObject("Pokemon");
                int t = pk.getInt("type");
                double v = pk.getDouble("value");
                //double s = 0;//pk.getDouble("speed");
                String p = pk.getString("pos");
                CL_Pokemon f = new CL_Pokemon(new Point3D(p), t, v, 0, null);
                ans.add(f);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ans;
    }

    /**
     * Updates the Pokemon's edge
     *
     * @param fr
     * @param g
     */
    public static void updateEdge(CL_Pokemon fr, directed_weighted_graph g) {
        Iterator<node_data> itr = g.getV().iterator();
        while (itr.hasNext()) {
            node_data v = itr.next();
            Iterator<edge_data> iter = g.getE(v.getKey()).iterator();
            while (iter.hasNext()) {
                edge_data e = iter.next();
                boolean f = isOnEdge(fr.getLocation(), e, fr.getType(), g);
                if (f) {
                    fr.set_edge(e);
                }
            }
        }
    }

    /**
     * Makes sure that the Pokemon is on the edge.
     *
     * @param p
     * @param src
     * @param dest
     * @return true if the Pokemon is on the edge.
     */
    private static boolean isOnEdge(geo_location p, geo_location src, geo_location dest) {

        boolean ans = false;
        double dist = src.distance(dest);
        double d1 = src.distance(p) + p.distance(dest);
        if (dist > d1 - EPS2) {
            ans = true;
        }
        return ans;
    }
    /**
     * Makes sure that the Pokemon is on the edge.
     *
     * @param p
     * @param d
     * @param g
     * @param s
     * @return true if the Pokemon is on the edge.
     */
    private static boolean isOnEdge(geo_location p, int s, int d, directed_weighted_graph g) {
        geo_location src = g.getNode(s).getLocation();
        geo_location dest = g.getNode(d).getLocation();
        return isOnEdge(p, src, dest);
    }
    /**
     * make sure that the Pokemon is on the edge.
     *
     * @param p
     * @param e
     * @param g
     * @param type
     * @return true if the Pokemon is on the edge.
     */
    private static boolean isOnEdge(geo_location p, edge_data e, int type, directed_weighted_graph g) {
        int src = g.getNode(e.getSrc()).getKey();
        int dest = g.getNode(e.getDest()).getKey();
        if (type < 0 && dest > src) {
            return false;
        }
        if (type > 0 && src > dest) {
            return false;
        }
        return isOnEdge(p, src, dest, g);
    }

    /**
     * @param g
     * @return the range of the received graph.
     */
    private static Range2D GraphRange(directed_weighted_graph g) {
        Iterator<node_data> itr = g.getV().iterator();
        double x0 = 0, x1 = 0, y0 = 0, y1 = 0;
        boolean first = true;
        while (itr.hasNext()) {
            geo_location p = itr.next().getLocation();
            if (first) {
                x0 = p.x();
                x1 = x0;
                y0 = p.y();
                y1 = y0;
                first = false;
            } else {
                if (p.x() < x0) {
                    x0 = p.x();
                }
                if (p.x() > x1) {
                    x1 = p.x();
                }
                if (p.y() < y0) {
                    y0 = p.y();
                }
                if (p.y() > y1) {
                    y1 = p.y();
                }
            }
        }
        Range xr = new Range(x0, x1);
        Range yr = new Range(y0, y1);
        return new Range2D(xr, yr);
    }

    /**
     *
     * @param g
     * @param frame
     * @return the graph frame Range2Range.
     */
    public static Range2Range w2f(directed_weighted_graph g, Range2D frame) {
        Range2D world = GraphRange(g);
        Range2Range ans = new Range2Range(world, frame);
        return ans;
    }

}
