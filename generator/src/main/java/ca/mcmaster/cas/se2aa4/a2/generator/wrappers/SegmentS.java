package ca.mcmaster.cas.se2aa4.a2.generator.wrappers;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;
import java.awt.Color;

//import java.text.DecimalFormat;

public class SegmentS{
    private int v1Idx;
    private int v2Idx;
    private String color;
    private int alpha; 
  
    public SegmentS(int v1Idx, int v2Idx) {
      this.v1Idx = v1Idx;
      this.v2Idx = v2Idx;
      //this.color = color;
      //this.segment = makeSegment();
    }
      public Segment makeSegment(){
     return Segment.newBuilder().setV1Idx(v1Idx).setV2Idx(v2Idx).build();
    }

 
    public int getV1Idx() {
      return v1Idx;
    }
  
    public int getV2Idx() {
      return v2Idx;
    }
  
    public void setV1Idx(int v1) {
      this.v1Idx = v1;
    }
  
    
    public void setV2Idx(int v2) {
      this.v2Idx = v2;
    }
  public String getColor() {
      return color;
    }
  
    //public void setColor(String color, int alpha) {
      //String rgba = color + "," + alpha;
      //this.color = rgba;

    public void setColor(String color) {
        this.color = color;
     }

   /* public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }*/

  }
  