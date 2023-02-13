package ca.mcmaster.cas.se2aa4.a2.generator;
import java.util.ArrayList;

public class Polygon {
    private ArrayList<Segment> segmentList;
    private Vertex centroid;
    private ArrayList<Polygon> neighbours;

    public Polygon() {
        segmentList = new ArrayList<>();
        neighbours = new ArrayList<>();
    }

    public void makePolygon(ArrayList<Segment> segments) {
        //this.segmentList = segments;
        //calculateCentroid();
        this.segmentList = new ArrayList<>(segments);
        this.centroid = calculateCentroid();
        this.neighbours = findNeighbours();
}
    }

    private void calculateCentroid() {
        int xSum = 0;
        int ySum = 0;
        int n = 0;
        for (Segment segment : segmentList) {
            Vertex v1 = segment.getV1();
            Vertex v2 = segment.getV2();
            xSum += v1.getX() + v2.getX();
            ySum += v1.getY() + v2.getY();
            n += 2;
        }
        float xCord = xSum / n;
        float yCord = ySum / n;
        centroid = new Vertex(xCord, yCord);
    }
  /*  private Centroid calculateCentroid() {
        float sumX = 0, sumY = 0;
        for (Segment segment : segmentList) {
            sumX += segment.getV1().getX();
            sumX += segment.getV2().getX();
            sumY += segment.getV1().getY();
            sumY += segment.getV2().getY();
        }
        int vertexCount = segmentList.size() * 2;
        return new Centroid(sumX / vertexCount, sumY / vertexCount);
    }*/

    public ArrayList<Segment> getSegmentList() {
        return segmentList;
    }

    public Vertex getCentroid() {
        return centroid;
    }

   /* public ArrayList<Polygon> getNeighbours() {
        ArrayList<Polygon> neighbours = new ArrayList<>();
        for (Segment segment : segmentList) {
            for (Polygon polygon : PolygonList) {
                if (polygon.segmentList.contains(segment) && polygon != this) {
                    neighbours.add(polygon);
                }
            }
        }
        return neighbours;
    }
    */
    private ArrayList<Polygon> getNeighbours() {
        ArrayList<Polygon> neighbours = new ArrayList<>();
        for (Segment segment : segmentList) {
            Vertex v1 = segment.getV1();
            Vertex v2 = segment.getV2();
            for (Polygon polygon : Mesh.PolygonList) {
                if (polygon == this) continue;
                if (polygon.hasVertex(v1) && polygon.hasVertex(v2)) {
                    neighbours.add(polygon);
                    break;
                }
            }
        }
        return neighbours;
    }
    
    public boolean hasVertex(Vertex v) {
        for (Segment segment : segmentList) {
            if (segment.hasVertex(v)) return true;
        }
        return false;
    }

    public void addNeighbour(Polygon polygon) {
        neighbours.add(polygon);
    }
}
