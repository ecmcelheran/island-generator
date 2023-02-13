import java.util.ArrayList;

public class Vertex {
  private float xCord;
  private float yCord;
  private Colour colour;
  private int setPrecision;

  public Vertex(float xCord, float yCord, Colour colour, int setPrecision) {
    this.xCord = xCord;
    this.yCord = yCord;
    this.colour = colour;
    this.setPrecision = setPrecision;
  }

  public float getxCord() {
    return xCord;
  }

  public float getyCord() {
    return yCord;
  }

  public void setxCord(float xCord) {
    this.xCord = xCord;
  }

  public void setyCord(float yCord) {
    this.yCord = yCord;
  }

  public void setColour(Colour colour) {
    this.colour = colour;
  }

  public Colour getColour() {
    return colour;
  }
}

public class Segment {
  private Vertex v1;
  private Vertex v2;
  private Colour colour;

  public Segment(Vertex v1, Vertex v2, Colour colour) {
    this.v1 = v1;
    this.v2 = v2;
    this.colour = colour;
  }

  public Vertex getV1() {
    return v1;
  }

  public Vertex getV2() {
    return v2;
  }

  public void setV1(Vertex v1) {
    this.v1 = v1;
  }

  public void setV2(Vertex v2) {
    this.v2 = v2;
  }

  public void setColour(Colour colour) {
    this.colour = colour;
  }

  public Colour getColour() {
    return colour;
  }
}

public class Polygon {
  private ArrayList<Segment> segmentList;
  private Centroid centroid;
  private ArrayList<Polygon> neighbours;

  public Polygon(ArrayList<Segment> segmentList) {
    this.segmentList = segmentList;
  }

  public ArrayList<Segment> getSegmentList() {
    return segmentList;
  }

  public Centroid getCentroid() {
    return centroid;
  }

  public ArrayList<Polygon> getNeighbours() {
    return neighbours;
  }
}

public class Mesh {
  private ArrayList<Vertex> verticesList;
  private ArrayList<Segment> segmentList;
  private ArrayList<Polygon> polygonList;

  public Mesh(ArrayList<Vertex> verticesList, ArrayList<Segment> segmentList, ArrayList<Polygon> polygonList) {
    this.verticesList = verticesList;
    this.segmentList = segmentList;
    this.polygonList = polygonList;
  }

  public void minimizeVertices() {
    Map<String, Vertex> uniqueVertices = new HashMap<>();

    for (Vertex vertex : verticesList) {
        String key = vertex.getx() + ":" + vertex.gety();
        if (!uniqueVertices.containsKey(key)) {
            uniqueVertices.put(key, vertex);
        }
    }

    verticesList.clear();
    verticesList.addAll(uniqueVertices.values());
}
public void minimizeSegments() {
  Set<String> uniqueSegments = new HashSet<>();
  List<Segment> minimizedSegmentList = new ArrayList<>();

  for (Segment segment : segmentList) {
    Vertex v1 = segment.getV1();
    Vertex v2 = segment.getV2();
    int v1X = (int) (v1.getX() * precision);
    int v1Y = (int) (v1.getY() * precision);
    int v2X = (int) (v2.getX() * precision);
    int v2Y = (int) (v2.getY() * precision);
    String segmentString = v1X + ":" + v1Y + "-" + v2X + ":" + v2Y;
    String reversedSegmentString = v2X + ":" + v2Y + "-" + v1X + ":" + v1Y;
    if (!uniqueSegments.contains(segmentString) && !uniqueSegments.contains(reversedSegmentString)) {
      uniqueSegments.add(segmentString);
      minimizedSegmentList.add(segment);
    }
  }

  segmentList = minimizedSegmentList;
}
public void minimizePolygons() {
  for (Polygon polygon : PolygonList) {
      for (Segment segment : polygon.getSegmentList()) {
          int v1Idx = VerticesList.indexOf(segment.getV1());
          int v2Idx = VerticesList.indexOf(segment.getV2());
          segment.setV1Idx(v1Idx);
          segment.setV2Idx(v2Idx);
      }
  }
}

}
  