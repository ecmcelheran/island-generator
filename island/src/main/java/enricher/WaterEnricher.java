package enricher;

import java.util.ArrayList;
import java.util.List;


import com.google.protobuf.Struct;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import configuration.Configuration;
import map.Map;

public class WaterEnricher implements Enricher{
    private int LAKES;
    //private String SHAPE;
    

    public WaterEnricher(Configuration config){
        if(config.export().containsKey(Configuration.LAKES)) 
            this.LAKES = Integer.parseInt(config.export(Configuration.LAKES));
        else
            this.LAKES = 100;
    }


    @Override
    public Mesh process(Mesh aMesh) {


        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'process'");
    }
    //public Structs.Mesh colorLand(Structs.Mesh aMesh, Map map){
    //     ArrayList<Structs.Polygon> ocean =  map.getOcean(aMesh);
    //     Structs.Mesh.Builder clone = Structs.Mesh.newBuilder();
    //     clone.addAllVertices(aMesh.getVerticesList());
    //     clone.addAllSegments(aMesh.getSegmentsList());
    //     String color;
    //     for(Structs.Polygon poly: aMesh.getPolygonsList()) {
    //         Structs.Polygon.Builder pc = Structs.Polygon.newBuilder(poly);
    //         if(lagoon.contains(poly)){
    //             color = "173,216,230";
    //         }
    //         else if (land.contains(poly)){
    //             color = "194,201,123";
    //         } else if(ocean.contains(poly)){
    //             color = "8,6,148";
    //         } else{
    //             color = "65,156,209";
    //         }
    //         Structs.Property c = Structs.Property.newBuilder()
    //                     .setKey("rgb_color")
    //                     .setValue(color)
    //                     .build();
    //         pc.addProperties(c);
    //         clone.addPolygons(pc);
    //     }
    //     return clone.build();
    //     for(Structs.Polygon p : ocean){


    //     }
    // }

    // public void countLakes(Mesh aMesh, Map map){
    //     ArrayList<Structs.Polygon> land = map.getLand();
    //     ArrayList<Structs.Polygon> ocean = map.getLand();

    //     for(Structs.Polygon p : aMesh.getPolygonsList()){
    //         if(!)
    //     }
    //     ArrayList<Structs.Polygon> land = map.getLand();
        

    // }


    
}
