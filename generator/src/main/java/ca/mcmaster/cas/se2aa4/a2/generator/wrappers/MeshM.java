package ca.mcmaster.cas.se2aa4.a2.generator.wrappers;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Property;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Polygon;

import java.text.DecimalFormat;
//import java.util.List;
import java.util.*;
//import java.util.Map;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.io.IOException;

//import java.util.Random;
import java.awt.Color;

import javax.lang.model.util.ElementScanner14;
import javax.swing.SizeSequence;

import org.locationtech.jts.algorithm.Length;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;
import org.locationtech.jts.geom.util.GeometryMapper;
import org.locationtech.jts.triangulate.VoronoiDiagramBuilder;
import org.locationtech.jts.geom.impl.CoordinateArraySequenceFactory;

public class MeshM {

  private final double square_size;
  private final double width;
  private final double height;
  private ArrayList<VertexV> verticesList;
  private ArrayList<SegmentS> segmentsList;
  private ArrayList<PolygonP> polygonsList;
  private ArrayList<Geometry> irregPolygons;
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
                SegmentS s = createSegment(vI, vJ);
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
    double avgX = vx/8;
    double avgY = vy/8;
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
    System.out.println("Vertices built");
    for(SegmentS s : segmentsList){
      // parse color strings - avg colour for segment
      Property color = Property.newBuilder().setKey("rgb_color").setValue(s.getColor()).build();
      Segment coloredS = Segment.newBuilder(s.makeSegment()).addProperties(color).build();
      // add io Struct segment
      built_segments.add(coloredS);
    }
      System.out.println("Segments built");
    for(PolygonP p : polygonsList){
      built_polygons.add(p.makePolygon());
      System.out.println("num nieghbours: "+p.getNeighboursIdxs().size());
    }
      System.out.println("Polygons built");
    Mesh mesh = Mesh.newBuilder().addAllSegments(built_segments).addAllVertices(built_vertices).addAllPolygons(built_polygons).build();
    //Mesh mesh = Mesh.newBuilder().addAllSegments(built_segments).addAllVertices(built_vertices).build();
    return mesh;
  }

    public SegmentS createSegment(VertexV vI, VertexV vJ){
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
        //int alpha = 10;
        //  String colorCode = red + "," + green + "," + blue  + "," + alpha;
        System.out.println(red + "," + green + "," + blue  + "," + 10);
        //Color newcolor = new Color(red, green, blue, alpha);
        s.setColor(red + "," + green + "," + blue  + "," + alpha);
        return s;
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

  public void orderSegments(){
    for(PolygonP p : polygonsList){
      ArrayList<Integer> copiedIds = p.getSegmentIdxs();
      for(int cI : copiedIds){
        for(int cJ : copiedIds){
          if(cI == cJ+1){
            int s1v1ID = segmentsList.get(cI).getV1Idx();
            int s1v2ID = segmentsList.get(cI).getV2Idx();
            int s2v1ID = segmentsList.get(cJ).getV1Idx();
            int s2v2ID = segmentsList.get(cJ).getV2Idx();
            if(!(s1v1ID == s2v1ID || s1v1ID == s2v2ID || s1v2ID == s2v1ID || s1v2ID == s2v2ID)){
              for(int cK : copiedIds){
                int s3v1ID = segmentsList.get(cK).getV1Idx();
                int s3v2ID = segmentsList.get(cK).getV2Idx();
                if(copiedIds.get(cI) != copiedIds.get(cK) && (s1v1ID == s3v1ID || s1v1ID == s3v2ID || s1v2ID == s3v1ID || s1v2ID == s3v2ID)){
                  int temp = copiedIds.get(cJ);
                  copiedIds.set(cJ, copiedIds.get(cK));
                  copiedIds.set(cK, temp);
                  break;
                }
              }
            }
          }
        }
      }
    }
  }

  public void makeIrregularGrid(){
    
    ArrayList<Coordinate> coordinates = new ArrayList<>();
    GeometryFactory factory = new GeometryFactory(CoordinateArraySequenceFactory.instance());
    Random r = new Random();
    for(int i=0; i<500; i++){
      double randomX = 0 + r.nextDouble() * (500);
      double randomY = 0 + r.nextDouble() * (500);
      Coordinate coord = new Coordinate(randomX, randomY);
      coordinates.add(coord);
      VertexV vertex = new VertexV(randomX, randomY);
      verticesList.add(vertex);
    }

    VoronoiDiagramBuilder diagramBuilder = new VoronoiDiagramBuilder();
    diagramBuilder.setSites(coordinates);
    Geometry polygons = diagramBuilder.getDiagram(factory);
    irregPolygons = new ArrayList<>();

    for (int i = 0; i < polygons.getNumGeometries(); i++) {
        irregPolygons.add(polygons.getGeometryN(i));
    }

    for (Object o : irregPolygons) {
      ArrayList<Integer> segmentGroup = new ArrayList<>();
      String[] p1,p2;
      String newString = o.toString();
      newString = newString.substring(10, newString.length()-2);
      String[] n = newString.split(",");

      for(int i=0; i<n.length; i++) {
        if(i<n.length-1){
          p1 = (i==0? n[i].split(" ") : n[i].substring(1).split(" "));
          p2 = n[i+1].substring(1).split(" ");
          }
        else{
          p1 = (i==0? n[i].split(" ") : n[i].substring(1).split(" "));
          p2 = n[0].split(" ");
        }
        VertexV v1 = new VertexV(Double.parseDouble(p1[0]), Double.parseDouble(p1[1]));
        VertexV v2 = new VertexV(Double.parseDouble(p2[0]), Double.parseDouble(p2[1]));
        verticesList.add(v1);
        verticesList.add(v2);

        SegmentS s = createSegment(v1,v2);
        segmentsList.add(s);
         segmentGroup.add(segmentsList.indexOf(s));
      }
      PolygonP polygon = new PolygonP(segmentGroup);
      polygonsList.add(polygon);
      setIrregCentroids(o,polygon);
    }
    
  }

  public void setIrregCentroids(Object o, PolygonP p){
      VertexV v = new VertexV (((Geometry) o).getCentroid().getX(), ((Geometry) o).getCentroid().getY());
      verticesList.add(v);
      p.setCentroidIdx(verticesList.indexOf(v));
  }

  public void relaxIrregularMesh(int iterations){
    GeometryFactory factory = new GeometryFactory(CoordinateArraySequenceFactory.instance());
    //after initial irregular mesh: apply Lloyd relaxation:
    for(int iter=0; iter<iterations; iter++){
      //get new coords - centroids of old polygons
      ArrayList<Coordinate> lloydCoords = new ArrayList<>();
      for(PolygonP p: polygonsList){
        double X = verticesList.get(p.getCentroidIdx()).getX(); 
        double Y = verticesList.get(p.getCentroidIdx()).getY(); 
        Coordinate coord = new Coordinate(X, Y);
        lloydCoords.add(coord);
      }
      // clear grid for newer realxed grid
      polygonsList.clear();
      verticesList.clear();
      segmentsList.clear();

      VoronoiDiagramBuilder diagramRebuilder = new VoronoiDiagramBuilder();
      diagramRebuilder.setSites(lloydCoords);
      Geometry newPolygons = diagramRebuilder.getDiagram(factory); 
      irregPolygons = new ArrayList<>();
      for (int i = 0; i < newPolygons.getNumGeometries(); i++) {
          irregPolygons.add(newPolygons.getGeometryN(i));
      }

      for (Object o : irregPolygons) {
        ArrayList<Integer> segmentGroup = new ArrayList<>();
        String[] p1,p2;
        String newString = o.toString();
        newString = newString.substring(10, newString.length()-2);
        String[] n = newString.split(",");

        for(int i=0; i<n.length; i++) {
          if(i<n.length-1){
            p1 = (i==0? n[i].split(" ") : n[i].substring(1).split(" "));
            p2 = n[i+1].substring(1).split(" ");
            }
          else{
            p1 = (i==0? n[i].split(" ") : n[i].substring(1).split(" "));
            p2 = n[0].split(" ");
          }
          VertexV v1 = new VertexV(Double.parseDouble(p1[0]), Double.parseDouble(p1[1]));
          VertexV v2 = new VertexV(Double.parseDouble(p2[0]), Double.parseDouble(p2[1]));
          verticesList.add(v1);
          verticesList.add(v2);

          SegmentS s = createSegment(v1,v2);
          segmentsList.add(s);
          segmentGroup.add(segmentsList.indexOf(s));
        }
        PolygonP polygon = new PolygonP(segmentGroup);
        polygonsList.add(polygon);
        setIrregCentroids(o,polygon);
      }
    } 
  }
}
