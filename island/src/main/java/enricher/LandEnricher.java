package enricher;
 
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
// import ca.mcmaster.cas.se2aa4.a2.io.Mesh;
// import ca.mcmaster.cas.se2aa4.a2.io.Polygon;
// import ca.mcmaster.cas.se2aa4.a2.io.Vertex;

import java.util.ArrayList;
import java.util.List;


public class LandEnricher implements Enricher{
    //idea: pass in mode, assign through contructor, use process() to make different land types via diff methods

    private String MODE;
    
    public LandEnricher(String MODE){
        this.MODE = MODE;
    }

    @Override
    public Structs.Mesh process(Structs.Mesh aMesh){
        int outterR = 100; 
        List<Structs.Polygon> land = new ArrayList<Structs.Polygon>();
        List<Structs.Vertex> verts = aMesh.getVerticesList();
        double max_x = Double.MIN_VALUE;
        double max_y = Double.MIN_VALUE;
        for (Structs.Vertex v: verts) {
            max_x = (Double.compare(max_x, v.getX()) < 0? v.getX(): max_x);
            max_y = (Double.compare(max_y, v.getY()) < 0? v.getY(): max_y);
         }
        double centerX = max_x/2;
        double centerY = max_y/2;
        double xDiff, yDiff;
        for(Structs.Polygon p : aMesh.getPolygonsList()){
            Structs.Vertex centroid = verts.get(p.getCentroidIdx());
            if(centroid.getX() > centerX){
                xDiff = centroid.getX() - centerX;
            }else{
                xDiff = centerX - centroid.getX();
            }
            if(centroid.getY() > centerY){
                yDiff = centroid.getY() - centerY;
            }else{
                yDiff = centerY - centroid.getY();
            }
            if(Math.sqrt(Math.pow(xDiff, 2)+Math.pow(yDiff, 2))<= outterR)//compare to radius
            {
                System.out.println("found land!");
                land.add(p);
            } 
        }

        Structs.Mesh.Builder clone = Structs.Mesh.newBuilder();
        clone.addAllVertices(aMesh.getVerticesList());
        clone.addAllSegments(aMesh.getSegmentsList());
        String color;
                
        for(Structs.Polygon poly: aMesh.getPolygonsList()) {
            Structs.Polygon.Builder pc = Structs.Polygon.newBuilder(poly);
            if (land.contains(poly)){
                color ="194,201,123";
            } else {
                color = "65,156,209";
            }

            Structs.Property c = Structs.Property.newBuilder()
                        .setKey("rgb_color")
                        .setValue(color)
                        .build();
            pc.addProperties(c);
            clone.addPolygons(pc);
        }

        return clone.build();
    }
}