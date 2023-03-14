package enricher;
 
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import adt.Land;
import map.CircularMapBuilder;
import map.IrregularMapBuilder;
import map.Map;
import configuration.Configuration;
// import ca.mcmaster.cas.se2aa4.a2.io.Mesh;
// import ca.mcmaster.cas.se2aa4.a2.io.Polygon;
// import ca.mcmaster.cas.se2aa4.a2.io.Vertex;

import java.util.ArrayList;
import java.util.List;

import com.google.protobuf.Struct;


public class LandEnricher implements Enricher{

    private String MODE;
    private String SHAPE;
    
    // public LandEnricher(String MODE, String SHAPE){
    //     this.MODE = MODE; // add config 
    //     this.SHAPE = SHAPE;    }
    public LandEnricher(Configuration config){
        if(config.export().containsKey(Configuration.SHAPE)) 
            this.SHAPE = config.export(Configuration.SHAPE);
        else
            this.SHAPE = "circle";
        if(config.export().containsKey(Configuration.SHAPE)) 
            this.MODE = config.export(Configuration.MODE); // add config 
        }

    @Override
    public Structs.Mesh process(Structs.Mesh aMesh){
        Structs.Mesh enrichedMesh = aMesh;
        switch(SHAPE){
            case "circle":
                CircularMapBuilder circular = new CircularMapBuilder();
                Map circularMap = circular.build(aMesh);
                enrichedMesh = colorLand(aMesh, circularMap);
            break;
            case "random":
                IrregularMapBuilder irreg = new IrregularMapBuilder();
                Map irregMap = irreg.build(aMesh);
                enrichedMesh = colorLand(aMesh, irregMap);
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