package ca.mcmaster.cas.se2aa4.a2.generator.wrappers;

import java.util.ArrayList;

public interface Meshh{
    public ArrayList<VertexV> verticesList = new ArrayList<>();
    public ArrayList<SegmentS> segmentsList = new ArrayList<>();
    public ArrayList<PolygonP> polygonsList = new ArrayList<>();

    public void makeMesh();
    public void setCentroidIdx();
    public void setNeighboursIdx();
    public void consecutiveSegments();
    
}