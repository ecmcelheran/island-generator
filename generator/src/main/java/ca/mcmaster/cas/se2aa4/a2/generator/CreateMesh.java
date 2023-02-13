package ca.mcmaster.cas.se2aa4.a2.generator;

import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.lang.FdLibm.Pow;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Property;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Polygon;



public class CreateMesh {
    private List<Vertex> vertices;
    private List<Segment> segments;
    private List<Polygon> polygons;

    public Mesh(List<Vertex> vertices, List<Segment>segments){
        this.vertices= vertices;
        this.segments = segments;
        this.polygons = new ArrayList<>();
        initializePolygons();
        addNeighbouringPolygons();
        addCentroidVertices();
    }

    public List<Vertex> getVertices(){
        return vertices;
    }

    public List<Segment> getSegments(){
        return segments;
    }

    public List<Polygon> getPolygons(){
        return polygons;
    }

    private void initializePolygons(){
        Set<Set<Integer>> polygonSegments = new HashSet<>();
        for (Segment segment : segments){
            int v1IDx = segment.getV1Idx();
            int v2IDx = segment.getV2Idx();
            Set<Integer> segmentSet = new HashSet<>();
            segmentSet.add(v1IDx);
            segmentSet.add(v2IDx);
            polygonSegments.add(segmentSet);
        }

        Map<Set<Integer>, Integer> polygonMap = new HashMap<>(); 
        int polygonIndex = 0;
        for (Set<Integer> segment : polygonSegments){
            if (!polygonMap.containsKey(segment)){
                polygonMap.put(segment, polygonIndex++);
                Polygon polygon = new Polygon();
                polygon.addSegment(segment);
                polygons.add(polygon);
            } else {
                int index = polygonMap.get(segment);
                polygonMap.put(segment, polygonIndex++);
                polygonMap.remove(segment);
                Polygon polygon = polygons.get(index);
                polygon.addSegment(segment);
            }
                
            }

        }
        private void addNeighbouringPolygons(){
            for (Polygon polygon : polygons){
                Set<Integer> segment = polygon.getSegments().iterator().next();
                for (Polygon comparePolygon.equals(polygon)){
                    continue;
                }

                Set<Integer> compareSegment = comparePolygon.getSegments().iterator().next();
                Set<Integer> intersection = new HashSet<>(segment);
                intersection.retainAll(compareSegment);
                if (intersection.size() ==1){
                    polygon.addNeighbouringPolygons(comparePolygon);
                }
            }
        }
    }   

    private void addCentroidVertices() {
        for (Polygon polygon : polygons){
            Set<Integer> segment = polygon.getSegments().iterator().next();
            List<Integer> vertices = new ArrayList<>(segment);
            Vertex v1 = vertices.get(0);
            Vertex v2 = vertices.get(1);
            double x = (v1.getX() + v2.getX())/2;
            double y = (v1.getY() + v2.getY())/2;
            int vertexIndex = vertices.get(0);
            Vertex centroid = new Vertex(x, y, vertexIndex);
            polygon.setCentroidIdx(centroid);
            vertices.add(vertexIndex, centroid);
        }
    }
    
}
