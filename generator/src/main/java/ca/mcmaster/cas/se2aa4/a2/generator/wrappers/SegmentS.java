package ca.mcmaster.cas.se2aa4.a2.generator.wrappers;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;
//import java.text.DecimalFormat;

public class SegmentS{
    private int v1Idx;
    private int v2Idx;
 /*   private double x1;
    private double x2;
    private double y1;
    private double y2;
*/
    private String color;
  
    public SegmentS(int v1Idx, int v2Idx) {
      this.v1Idx = v1Idx;
      this.v2Idx = v2Idx;
      //this.color = color;
      //this.segment = makeSegment();
    }
    //private static final DecimalFormat df = new DecimalFormat("#.00");
    //public Segment makeSegment(VertexV v1, VertexV v2, double x1, double x2, double y1, double y2){
      public Segment makeSegment(){
     /* double x1 = Math.round(v1.getX() * 100)/100.0;
     
      double y1 = Math.round(v1.getY() * 100)/100.0;
      double x2 = Math.round(v1.getX() * 100)/100.0;
      double y2 = Math.round(v1.getY() * 100)/100.0;

      this.x1 = Math.round(v1.getX() * 100)/100.0;
      this.y1 = Math.round(v1.getY() * 100)/100.0;
      this.x2 = Math.round(v1.getX() * 100)/100.0;
      this.y2 = Math.round(v1.getY() * 100)/100.0;*/

     // return Segment.newBuilder().setV1Idx(v1Idx).setV2Idx(v2Idx).setX1(x1).setX2(x2).setY1(y1).setY2(y2).build();
     // return Segment.newBuilder().setV1Idx(v1Idx).setV2Idx(v2Idx).setX(x1).setY(y1).setX(x2).setY(y2).build();
     return Segment.newBuilder().setV1Idx(v1Idx).setV2Idx(v2Idx).build();
      /*.setX1(Math.round(v1.getX() * 100)/100.0)
      .setY1(Math.round(v1.getY() * 100)/100.0)
      .setX2(Math.round(v1.getX() * 100)/100.0)
      .setY2(Math.round(v1.getY() * 100)/100.0)*/
      //.setX1(x1).setX2(x2).setY1(y1).setY2(y2).build();
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
  /*  public void setX1(double x1) {
      this.x1 = Double.parseDouble(df.format(x1));
     //double x1 = Math.round(v1.getX() * 100)/100.0;
  }
    public void setX2(double x2) {
       this.x2 = Double.parseDouble(df.format(x1));
  } 

    public void setY1(double y1) {
        this.y1 = Double.parseDouble(df.format(x1));
    }
    public void setY2(double y2) {
      this.y2 = Double.parseDouble(df.format(x1));
  }*/
  public String getColor() {
      return color;
    }
  
    public void setColor(String color) {
      this.color = color;
    }
  }
  