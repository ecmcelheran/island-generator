package ca.mcmaster.cas.se2aa4.a2.generator.wrappers;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Property;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Polygon;

import ca.mcmaster.cas.se2aa4.a2.generator.wrappers.VertexV;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.io.IOException;
import java.util.Random;

public class MeshM {

  private final float square_size;
  private final int width;
  private final int height;
  private ArrayList<VertexV> verticesList;
  private ArrayList<SegmentS> segmentsList;
  private ArrayList<PolygonP> polygonsList;
  private int precision;

  public MeshM(float square_size, int width, int height, int precision){
    this.square_size = square_size;
    this.width = width;
    this.height = height;
    this.precision = precision;
  }
  
  public void makeGrid(){
    // round squares by precision
    for(int x = 0; x < width; x += square_size) {
      for(int y = 0; y < height; y += square_size) {
        float xP = (float) x; // instead of type cast implement precision
        float yP = (float) y;
        VertexV v1 = new VertexV(xP,yP); // make vertex object 
        verticesList.add(v1);
        VertexV v2 = new VertexV(xP+square_size, yP);
        verticesList.add(v2);
        VertexV v3 = new VertexV(xP,  yP+square_size);
        verticesList.add(v3);
        VertexV v4 = new VertexV(xP+square_size, yP+square_size);
        verticesList.add(v4);
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
                String colorCode = red + "," + green + "," + blue;
                s.setColor(colorCode);
                segmentsList.add(s);
            } 
        }
    }
  }

  public boolean isCorner(SegmentS s1, SegmentS s2){
    if(s1.getV1Idx() == s2.getV1Idx() && //share vertex 
      verticesList.get( s1.getV2Idx()).getX() != verticesList.get(s2.getV2Idx()).getX() && //but not on same X or Y 
      verticesList.get( s1.getV2Idx()).getY() != verticesList.get(s2.getV2Idx()).getY())
      { return true; } 
    else if(s1.getV1Idx() == s2.getV2Idx() && //share vertex 
      verticesList.get( s1.getV2Idx()).getX() != verticesList.get(s2.getV1Idx()).getX() && //but not on same X or Y 
      verticesList.get( s1.getV2Idx()).getY() != verticesList.get(s2.getV1Idx()).getY())
      { return true; } 
    else if(s1.getV2Idx() == s2.getV1Idx() && //share vertex 
      verticesList.get( s1.getV1Idx()).getX() != verticesList.get(s2.getV2Idx()).getX() && //but not on same X or Y 
      verticesList.get( s1.getV1Idx()).getY() != verticesList.get(s2.getV2Idx()).getY())
      { return true; } 
    else if(s1.getV2Idx() == s2.getV2Idx() && //share vertex 
      verticesList.get( s1.getV1Idx()).getX() != verticesList.get(s2.getV1Idx()).getX() && //but not on same X or Y 
      verticesList.get( s1.getV1Idx()).getY() != verticesList.get(s2.getV1Idx()).getY())
      { return true;}  
    else { return false;}
}

  public void createPolygons(){
    ArrayList<Integer> groupedSegments = new ArrayList<Integer>();
    //ArrayList<Set<Integer>> listOfSets = new  ArrayList<Set<Integer>>();
    for(SegmentS s1 : segmentsList){
      for(SegmentS s2 : segmentsList){
        // if segments share vertex
        if(isCorner(s1, s2)){
          for(SegmentS s3 : segmentsList){
            if(s3 != s1 && isCorner(s3, s2)){
              for(SegmentS s4 : segmentsList){
                if(s4 != s2 && isCorner(s3, s4)){
                  groupedSegments.add(segmentsList.indexOf(s1)); // add the segments that make up the sqaure to group 
                  groupedSegments.add(segmentsList.indexOf(s2));
                  groupedSegments.add(segmentsList.indexOf(s3));
                  groupedSegments.add(segmentsList.indexOf(s4));
                  PolygonP polygon  = new PolygonP(groupedSegments);
                  polygonsList.add(polygon);
                  // if(!listOfSets.contains(groupedSegments)){ // make sure the set is unique do avoid duplicate polygons
                  //   //listOfSets.add(groupedSegments);

                  // }   
                  groupedSegments.clear();
                }
              }
            }
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
    float vx = 0;
    float vy = 0;
    for(int seg_id : polygon.segment_idxs){
      SegmentS seg = segmentsList.get(seg_id);
      vx = vx + verticesList.get(seg.getV1Idx()).getX() +  verticesList.get(seg.getV2Idx()).getX();
      vy = vy + verticesList.get(seg.getV1Idx()).getY() +  verticesList.get(seg.getV2Idx()).getY();
    }
    float avgX = vx/polygon.segment_idxs.size();
    float avgY = vy/polygon.segment_idxs.size();
    VertexV centroid = new VertexV(avgX, avgY); 
    verticesList.add(centroid);
    polygon.setCentroidIdx(verticesList.indexOf(centroid));
  }

  public Mesh buildGrid(){
    ArrayList<Segment> built_segments = new ArrayList<Segment>();
    ArrayList<Vertex> built_vertices = new ArrayList<Vertex>();
    ArrayList<Polygon> built_polygons = new ArrayList<Polygon>();
    
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
    return mesh;
  }

  public void addToSegments(SegmentS seg){
    segmentsList.add(seg);
  }

  public void addToVertices(VertexV vert){
    verticesList.add(vert);
  }

  public void addToPolygons(PolygonP poly){
    polygonsList.add(poly);
  }


}
