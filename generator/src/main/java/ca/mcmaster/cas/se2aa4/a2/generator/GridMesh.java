package ca.mcmaster.cas.se2aa4.a2.generator.wrappers;

import java.text.DecimalFormat;

import java.util.ArrayList;

import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Property;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Polygon;

public class GridMesh implements Meshh{
    private double square_size;
    private double width;
    private double height;
    private static final DecimalFormat df = new DecimalFormat("#.00");


    public GridMesh(double square_size, double width, double height){
        this.square_size = Double.parseDouble(df.format(square_size));
        this.width = Double.parseDouble(df.format(width));
        this.height = Double.parseDouble(df.format(height));
    }
    
    @Override
    public void makeMesh() {
        for(double x = 0; x < width+square_size; x += square_size) {
            for(double y = 0; y < height+square_size; y += square_size) {
              double round_x = Double.parseDouble(df.format(x));
              double round_y = Double.parseDouble(df.format(y));
              VertexV v = new VertexV(round_x,round_y); // make vertex object
             // VertexV v = new VertexV(x,y); // make vertex object
              verticesList.add(v);
            }
        }
        for(VertexV vI: verticesList){
            for(VertexV vJ: verticesList){
                if((vJ.getX() ==  vI.getX()+square_size && vJ.getY() == vI.getY()) ||(vJ.getX() ==  vI.getX() && vJ.getY() == vI.getY()+square_size) ){ // find vertex to right
                    SegmentS s = createSegment(vI, vJ);
                    segmentsList.add(s);
                }
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

    public void createPolygons(){
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

    @Override
    public void setCentroidIdx() {
        for(PolygonP polygon:polygonsList){
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
    }
    @Override
    public void setNeighboursIdx() {
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

    @Override
    public void consecutiveSegments() {
        for(PolygonP p: polygonsList){
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

    public Mesh buildGrid(){
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