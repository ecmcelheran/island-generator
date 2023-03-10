package enricher;
 
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import adt.Land;
import map.CircularMapBuilder;
import map.Map;
// import ca.mcmaster.cas.se2aa4.a2.io.Mesh;
// import ca.mcmaster.cas.se2aa4.a2.io.Polygon;
// import ca.mcmaster.cas.se2aa4.a2.io.Vertex;

import java.util.ArrayList;
import java.util.List;

import com.google.protobuf.Struct;


public class LandEnricher implements Enricher{
    //idea: pass in mode, assign through contructor, use process() to make different land types via diff methods

    private String MODE;
    
    public LandEnricher(String MODE){
        this.MODE = MODE; // add config 
    }

    @Override
    public Structs.Mesh process(Structs.Mesh aMesh){
        Structs.Mesh enrichedMesh = aMesh;
        switch(MODE){
            case "lagoon":
                CircularMapBuilder circular = new CircularMapBuilder();
                Map circularMap = circular.build(aMesh);
                enrichedMesh = colorLand(aMesh, circularMap);
            break;
        }
        return enrichedMesh;
    }

    public Structs.Mesh colorLand(Structs.Mesh aMesh, Map landMap){
        ArrayList<Structs.Polygon> land =  landMap.getLand();
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