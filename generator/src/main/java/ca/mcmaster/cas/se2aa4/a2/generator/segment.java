package ca.mcmaster.cas.se2aa4.a2.generator;

public class Segment {
    private Vertex v1;
    private Vertex v2;
    private Color color;
  
    public Segment(Vertex v1, Vertex v2, Color color) {
      this.v1 = v1;
      this.v2 = v2;
      this.color = color;
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
  
    public Color getColor() {
      return color;
    }
  
    public void setColor(Color color) {
      this.color = color;
    }
  }
  