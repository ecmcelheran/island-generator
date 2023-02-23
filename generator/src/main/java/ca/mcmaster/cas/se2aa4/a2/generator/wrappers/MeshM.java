package ca.mcmaster.cas.se2aa4.a2.generator.wrappers;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Property;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Polygon;

import java.text.DecimalFormat;
//import java.util.List;
import java.util.ArrayList;
//import java.util.Map;
//import java.util.HashMap;
import java.util.Set;
//import java.util.HashSet;
//import java.io.IOException;
//import java.util.Random;

import javax.lang.model.util.ElementScanner14;
import javax.swing.SizeSequence;

public class MeshM {

  private final double square_size;
  private final double width;
  private final double height;
  private ArrayList<VertexV> verticesList;
  private ArrayList<SegmentS> segmentsList;
  private ArrayList<PolygonP> polygonsList;
  private ArrayList<Segment> built_segments;
  private ArrayList<Vertex> built_vertices;
  private ArrayList<Polygon> built_polygons;
 // private int precision;
  private static final DecimalFormat df = new DecimalFormat("#.00"); 


  public MeshM(float square_size, int width, int height, int precision){
   // this.square_size = square_size;
    //this.square_size = String.format("%.2f", square_size);
    this.square_size = Double.parseDouble(df.format(square_size));
    //this.width = width;
    this.width = Double.parseDouble(df.format(width));

    //this.round_width = String.format("%.2f", width);
    this.height = Double.parseDouble(df.format(height));
   // this.round_height = String.format("%.2f", width);
    //this.precision = precision;
    this.verticesList = new ArrayList<VertexV>();
    this.segmentsList = new ArrayList<SegmentS>();
    this.polygonsList = new ArrayList<PolygonP>();
    this.built_segments = new ArrayList<Segment>();
    this.built_vertices = new ArrayList<Vertex>();
    this.built_polygons = new ArrayList<Polygon>();
    
  }
  
  public void makeGrid(){
//    DecimalFormat df = new DecimalFormat("#.##");
    //double square_size = Double.parseDouble(df.format(square_size));
    for(double x = 0; x < width+square_size; x += square_size) {
      for(double y = 0; y < height+square_size; y += square_size) {
        double round_x = Double.parseDouble(df.format(x));
        double round_y = Double.parseDouble(df.format(y));
        VertexV v = new VertexV(round_x,round_y); // make vertex object 
       // VertexV v = new VertexV(x,y); // make vertex object 
        verticesList.add(v);
      }
    }        
    // Create segments between adjacent vertices
    for(VertexV vI: verticesList){
        for(VertexV vJ: verticesList){
            if((vJ.getX() ==  vI.getX()+square_size && vJ.getY() == vI.getY()) ||(vJ.getX() ==  vI.getX() && vJ.getY() == vI.getY()+square_size) ){ // find vertex to right 
                // create and append new segment between vertices
                SegmentS s = new SegmentS(verticesList.indexOf(vI), verticesList.indexOf(vJ));
                // colour the segment: parse the old string color codes, take average of 2 to make new color code
                String colorStringI = verticesList.get(s.getV1Idx()).getColor();    
                String[] colorsI = colorStringI.split(",");
                String colorStringJ = verticesList.get(s.getV2Idx()).getColor();    
                String[] colorsJ = colorStringJ.split(",");
                int red = (Integer.parseInt(colorsI[0]) + Integer.parseInt(colorsJ[0])) / 2;
                int green = (Integer.parseInt(colorsI[1]) + Integer.parseInt(colorsJ[1])) / 2;
                int blue = (Integer.parseInt(colorsI[2]) + Integer.parseInt(colorsJ[2])) / 2;
                int alpha = 128; //50% opaque
                String colorCode = red + "," + green + "," + blue  + "," + alpha;
                s.setColor(colorCode);
                segmentsList.add(s);
            } 
        }
    }
  }

  public void createPolygons(){
    System.out.println("entered createPolygons");
    for(int w = 0; w<width; w+=square_size){      
      for(int h = 0; h<height; h+=square_size){
        ArrayList<Integer> groupedSegments = new ArrayList<Integer>();
        for(SegmentS s : segmentsList){
          if(verticesList.get(s.getV1Idx()).getX() == w && verticesList.get(s.getV2Idx()).getX() == w+square_size){
            if(verticesList.get(s.getV1Idx()).getY() == h && verticesList.get(s.getV2Idx()).getY() == h){
              groupedSegments.add(segmentsList.indexOf(s));
            } else if(verticesList.get(s.getV1Idx()).getY() == h+square_size && verticesList.get(s.getV2Idx()).getY() == h+square_size){
              groupedSegments.add(segmentsList.indexOf(s));
            }
          } 
          if(verticesList.get(s.getV1Idx()).getY() == h && verticesList.get(s.getV2Idx()).getY() == h+square_size){
            if(verticesList.get(s.getV1Idx()).getX() == w && verticesList.get(s.getV2Idx()).getX() == w){
              groupedSegments.add(segmentsList.indexOf(s));
            } else if(verticesList.get(s.getV1Idx()).getX() == w+square_size && verticesList.get(s.getV2Idx()).getX() == w+square_size){
              groupedSegments.add(segmentsList.indexOf(s));
            }
          }
          if(groupedSegments.size() == 4){
            PolygonP polygon  = new PolygonP(groupedSegments);
            System.out.println("Polygon segment index count in createPolygons: "+polygon.getSegmentIdxs().size());
            polygonsList.add(polygon); 
            break;
          }
        }
      }
    }
  }

  public void createAllCentroids(){
    for(PolygonP polygon : polygonsList){
      createCentroid(polygon);
    }
  }

  public void createCentroid(PolygonP polygon){
    double vx = 0;
    double vy = 0;
    for(int seg_id : polygon.segment_idxs){
      SegmentS seg = segmentsList.get(seg_id);
      vx = vx + verticesList.get(seg.getV1Idx()).getX() +  verticesList.get(seg.getV2Idx()).getX();
      vy = vy + verticesList.get(seg.getV1Idx()).getY() +  verticesList.get(seg.getV2Idx()).getY();
    }
    double avgX = vx/polygon.segment_idxs.size();
    double avgY = vy/polygon.segment_idxs.size();
    VertexV centroid = new VertexV(avgX, avgY); 
    verticesList.add(centroid);
    polygon.setCentroidIdx(verticesList.indexOf(centroid));
  }

  public Mesh buildGrid(){
    for(VertexV v : verticesList){
      //Vertex uncoloredV = v.makeVertex();
      Property color = Property.newBuilder().setKey("rgb_color").setValue(v.getColor()).build();
      Vertex coloredV = Vertex.newBuilder(v.makeVertex()).addProperties(0, color).build();
      built_vertices.add(coloredV);
    }
    for(SegmentS s : segmentsList){
      // parse color strings - avg colour for segment
      Property color = Property.newBuilder().setKey("rgb_color").setValue(s.getColor()).build();
      Segment coloredS = Segment.newBuilder(s.makeSegment()).addProperties(color).build();
      // add io Struct segment 
      built_segments.add(coloredS);
    }

    for(PolygonP p : polygonsList){
      built_polygons.add(p.makePolygon());
    }

    Mesh mesh = Mesh.newBuilder().addAllSegments(built_segments).addAllVertices(built_vertices).addAllPolygons(built_polygons).build();
    //Mesh mesh = Mesh.newBuilder().addAllSegments(built_segments).addAllVertices(built_vertices).build();   
    return mesh;
  }

  public void findNeighbourhoods(){
    for(PolygonP centerPolygon: polygonsList){
      VertexV centerCentroid = verticesList.get(centerPolygon.getCentroidIdx());
      ArrayList<Integer> neighbourIdxs = new ArrayList<Integer>();
      for(PolygonP neighbour : polygonsList){
        VertexV neighbCentroid = verticesList.get(neighbour.getCentroidIdx());
        if(neighbCentroid.getX() == centerCentroid.getX() && neighbCentroid.getY() == centerCentroid.getY()+square_size){
          neighbourIdxs.add(verticesList.indexOf(neighbCentroid));
        } else if(neighbCentroid.getX() == centerCentroid.getX() && neighbCentroid.getY() == centerCentroid.getY()- square_size){
          neighbourIdxs.add(verticesList.indexOf(neighbCentroid));
        } else if(neighbCentroid.getX() == centerCentroid.getX()+square_size && neighbCentroid.getY() == centerCentroid.getY()){
          neighbourIdxs.add(verticesList.indexOf(neighbCentroid));
        } else if(neighbCentroid.getX() == centerCentroid.getX()-square_size && neighbCentroid.getY() == centerCentroid.getY()){
          neighbourIdxs.add(verticesList.indexOf(neighbCentroid));
        }  
      }
      centerPolygon.setNeighboursIdx(neighbourIdxs);
    }

}
}
