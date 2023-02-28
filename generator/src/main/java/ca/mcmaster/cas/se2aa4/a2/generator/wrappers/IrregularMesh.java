package ca.mcmaster.cas.se2aa4.a2.generator.wrappers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.lang.model.util.ElementScanner14;
import javax.swing.SizeSequence;
import org.locationtech.jts.geom.Coordinate;

import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Property;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Polygon;

import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.algorithm.Length;
import org.locationtech.jts.algorithm.ConvexHull;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.triangulate.DelaunayTriangulationBuilder;
import org.locationtech.jts.triangulate.VoronoiDiagramBuilder;
import org.locationtech.jts.geom.impl.CoordinateArraySequenceFactory;

public class IrregularMesh implements Meshh{
    private ArrayList<Geometry> irregPolygons;
    private ArrayList<VertexV> centroids;
    private ArrayList<VertexV> croppedVertices;

    private double width;
    private double height;
    
    public IrregularMesh(double width, double height){
        this.width = width;
        this.height = height;
    }


    @Override
    public void makeMesh() {
      ArrayList<Coordinate> coordinates = new ArrayList<>();
    centroids = new ArrayList<>();
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
    public void relaxMesh(double relaxationLevel){
        GeometryFactory factory = new GeometryFactory(CoordinateArraySequenceFactory.instance());
        //after initial irregular mesh: apply Lloyd relaxation:
        for(int iter=0; iter<relaxationLevel; iter++){
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
          centroids.clear();
    
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
        s.setColor(red + "," + green + "," + blue  + "," + alpha);
        return s;
    }


    @Override
    public void setCentroidIdx() {
    }

    public void setIrregCentroids(Object o, PolygonP p){
        VertexV v = new VertexV (((Geometry) o).getCentroid().getX(), ((Geometry) o).getCentroid().getY());
        verticesList.add(v);
        p.setCentroidIdx(verticesList.indexOf(v));
        centroids.add(v);
    }

    @Override
    public void setNeighboursIdx() {
      GeometryFactory factory = new GeometryFactory(CoordinateArraySequenceFactory.instance());
      DelaunayTriangulationBuilder triangulationBuilder = new DelaunayTriangulationBuilder();
      ArrayList<Coordinate> c = new ArrayList<>();
      System.out.println(centroids.size());
      for(VertexV v: centroids){
          c.add(new Coordinate(v.getX(),v.getY()));
      }
      triangulationBuilder.setSites(c);

      Geometry tri = triangulationBuilder.getTriangles(factory);
      ArrayList<Geometry> triangulations = new ArrayList<>();
      for (int i = 0; i < tri.getNumGeometries(); i++) {
          triangulations.add(tri.getGeometryN(i));
      }
      System.out.println(triangulations.size());
      Envelope cropEnvelope = new Envelope(0, width, 0, height);

      for (Object o : triangulations) {
          String[] p1, p2;
          String newString = o.toString();
          newString = newString.substring(10, newString.length() - 2);
          String[] n = newString.split(",");

          boolean skipTriangle = false;
          for (int i = 0; i < n.length; i++) {
              if (i < n.length - 1) {
                  p1 = (i == 0 ? n[i].split(" ") : n[i].substring(1).split(" "));
                  p2 = n[i + 1].substring(1).split(" ");
              } else {
                  p1 = (i == 0 ? n[i].split(" ") : n[i].substring(1).split(" "));
                  p2 = n[0].split(" ");
              }
              VertexV v1 = new VertexV(Double.parseDouble(p1[0]), Double.parseDouble(p1[1]));
              VertexV v2 = new VertexV(Double.parseDouble(p2[0]), Double.parseDouble(p2[1]));
              if (!cropEnvelope.contains(v1.getX(), v1.getY()) || !cropEnvelope.contains(v2.getX(), v2.getY())) {
                skipTriangle = true;
                break;
              }

              for(PolygonP p: polygonsList){
                  if(Double.compare(verticesList.get(p.getCentroidIdx()).getX(), v1.getX()) == 0 && Double.compare(verticesList.get(p.getCentroidIdx()).getY(), v1.getY()) == 0 ) {
                      for(PolygonP poly: polygonsList){
                          if(Double.compare(verticesList.get(poly.getCentroidIdx()).getX(), v2.getX()) == 0 && Double.compare(verticesList.get(poly.getCentroidIdx()).getY(), v2.getY()) == 0){
                              p.addNeighbourIdx(poly.getCentroidIdx());
                              break;
                          }
                      }
                      break;
                  }
              }

          }
          if (skipTriangle) {
            continue;
          }
        }
    }

    @Override
    public void consecutiveSegments() {
        ArrayList<Geometry> newPolygons = new ArrayList<>();
        for (Geometry irrpolygon : irregPolygons){
            ConvexHull convexHull = new ConvexHull(irrpolygon);
            Geometry hull = convexHull.getConvexHull();
            Coordinate[] hullCoords = hull.getCoordinates();
          
            List<Coordinate> hullCoordsList = new ArrayList<>(Arrays.asList(hullCoords));
            Coordinate lastCoord = hullCoordsList.get(hullCoordsList.size()-1);
            Coordinate firstCoord = hullCoordsList.get(0);
            if (!firstCoord.equals2D(lastCoord)) {
              hullCoordsList.add(firstCoord);
            }
            LinearRing linearRing = irrpolygon.getFactory().createLinearRing(hullCoordsList.toArray(new Coordinate[hullCoordsList.size()]));
        
            // Create a new polygon from the reordered segments
            GeometryFactory gf = new GeometryFactory();
            Geometry newPolygon = gf.createPolygon(linearRing, null);
            newPolygons.add(newPolygon);
          }
          irregPolygons = newPolygons;
    }
    public Mesh buildMesh(){
        ArrayList<Vertex> built_vertices = new ArrayList<Vertex>();
        ArrayList<Segment> built_segments = new ArrayList<Segment>();
        ArrayList<Polygon> built_polygons = new ArrayList<Polygon>();
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
          System.out.println("num neighbours: "+p.getNeighboursIdxs().size());
        }
          System.out.println("Polygons built");
        Mesh mesh = Mesh.newBuilder().addAllSegments(built_segments).addAllVertices(built_vertices).addAllPolygons(built_polygons).build();
        return mesh;
      }
    
}