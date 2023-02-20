package ca.mcmaster.cas.se2aa4.a2.generator.wrappers;
//import ca.mcmaster.cas.se2aa4.a2.io.Structs.Builder;
//import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
//import ca.mcmaster.cas.se2aa4.a2.io.Structs.Property;
//import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Polygon.Builder;
//import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Polygon;
//import ca.mcmaster.cas.se2aa4.a2.io.Structs.PolygonOrBuilder;

//import java.util.List;
import java.util.ArrayList;
//import java.util.Map;
//import java.util.HashMap;
//import java.util.Set;
//import java.util.HashSet;
//import java.io.IOException;
//import java.lang.reflect.Array;
//import java.util.Random;

public class PolygonP{

    //protected ArrayList<Integer> segment_idxs;
    protected ArrayList<Integer> segment_idxs;
    private int centroid_idx;
    private ArrayList<Integer> neighbours_idxs;
    
    public PolygonP(ArrayList<Integer> segment_idxs){
        this.segment_idxs = segment_idxs;
        this.neighbours_idxs =  new ArrayList<Integer>();
    }

    public PolygonP(ArrayList<Integer> segment_idxs, int centroid_idx, ArrayList<Integer> neighbours_idxs) {
        this.segment_idxs = segment_idxs;
        this.centroid_idx = centroid_idx;
        this.neighbours_idxs = neighbours_idxs;
    }

    public Polygon makePolygon(){
        Polygon polygon = Polygon.newBuilder().setCentroidIdx(centroid_idx).addAllSegmentIdxs(segment_idxs).build();
        return polygon; 
    }



    public ArrayList<Integer> getNeighboursIdxs() {
        return neighbours_idxs;
    }

    public ArrayList<Integer> getSegmentIdxs() {
        return segment_idxs;
    }

    public int getCentroidIdx() {
        return centroid_idx;
    }
    public void setCentroidIdx(int id) {
        this.centroid_idx = id;
    }


}