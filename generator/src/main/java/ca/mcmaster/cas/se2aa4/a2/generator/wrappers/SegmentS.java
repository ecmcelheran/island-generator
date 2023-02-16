package ca.mcmaster.cas.se2aa4.a2.generator.wrappers;

import java.awt.Color;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;


public class SegmentS{
    private int v1Idx;
    private int v2Idx;
    private int[] color;
  
    public SegmentS(int v1Idx, int v2Idx) {
      this.v1Idx = v1Idx;
      this.v2Idx = v2Idx;
      //this.color = color;
      //this.segment = makeSegment();
    }
    
    public Segment makeSegment(){
      return Segment.newBuilder().setV1Idx(v1Idx).setV2Idx(v2Idx).build();
    }

    // public void averageColour(){
    //   String colorStringI = verticiesList.get(s.getV1Idx()).getProperties(0).getValue();    
    //   String[] colorsI = colorStringI.split(",");
    //   String colorStringJ = verticiesList.get(s.getV2Idx()).getProperties(0).getValue();    
    //   String[] colorsJ = colorStringJ.split(",");
    //   int red = (Integer.parseInt(colorsI[0]) + Integer.parseInt(colorsJ[0])) / 2;
    //   int green = (Integer.parseInt(colorsI[1]) + Integer.parseInt(colorsJ[1])) / 2;
    //   int blue = (Integer.parseInt(colorsI[2]) + Integer.parseInt(colorsJ[2])) / 2;
    //   String colorCode = red + "," + green + "," + blue;
    //   Property color = Property.newBuilder().setKey("rgb_color").setValue(colorCode).build();
    // }

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

    public Color getColor() {
      return color;
    }
  
    public void setColor(Color color) {
      this.color = color;
    }
  }
  