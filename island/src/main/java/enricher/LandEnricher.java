package enricher;
 
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import adt.Land;
import map.CircularMapBuilder;
import map.InnerCircularMap;
import map.IrregularMapBuilder;
import map.Map;
import map.OvularMapBuilder;
import map.RadialMapBuilder;
import map.waterBodies.AquafierBuilder;
import map.waterBodies.LakeBuilder;
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
    private int LAKES;
    private int AQUAF;


    

    public LandEnricher(Configuration config){
        if(config.export().containsKey(Configuration.SHAPE)) 
            this.SHAPE = config.export(Configuration.SHAPE);
        else
            this.SHAPE = "circle";
        if(config.export().containsKey(Configuration.MODE)) 
            this.MODE = config.export(Configuration.MODE); // add config 
        else
            this.MODE = "none";
        if(config.export().containsKey(Configuration.LAKES)) 
            this.LAKES = Integer.parseInt(config.export(Configuration.LAKES)); // add config 
        else
            this.LAKES = 0 ;
        if(config.export().containsKey(Configuration.AQUAF)) 
            this.AQUAF = Integer.parseInt(config.export(Configuration.AQUAF)); // add config 
        else
            this.AQUAF = 0 ;
    }

    @Override
    public Structs.Mesh process(Structs.Mesh aMesh){
        Structs.Mesh enrichedMesh = aMesh;
        ArrayList<Map> lagoonMaps = new ArrayList<>();
        switch (SHAPE) {
            case "circle" ->{
                CircularMapBuilder circular = new CircularMapBuilder();
                Map circularMap = circular.build(aMesh, 200);
                switch(MODE){    
                    case "lagoon" -> {
                        InnerCircularMap innerMap = new InnerCircularMap();
                        for (int i = 0; i < 5; i++) {
                            lagoonMaps.add(innerMap.build(aMesh, 20, circularMap));
                        }
                    }
                }
                circularMap = addWaterBodies(aMesh, circularMap, LAKES, AQUAF);   
                enrichedMesh = colorLand(aMesh, circularMap, lagoonMaps);
            }
            case "irreg" ->{
                IrregularMapBuilder irreg = new IrregularMapBuilder();
                Map irregMap = irreg.build(aMesh, 250);
                irregMap = addWaterBodies(aMesh, irregMap, LAKES, AQUAF);   
                enrichedMesh = colorLand(aMesh, irregMap, lagoonMaps);
            }
            case "oval" ->{
                OvularMapBuilder ovalBuild = new OvularMapBuilder();
                Map ovalMap = ovalBuild.build(aMesh, 100);
                ovalMap = addWaterBodies(aMesh, ovalMap, LAKES, AQUAF);
                enrichedMesh = colorLand(aMesh, ovalMap, lagoonMaps);
            }
            case "radial" ->{
                RadialMapBuilder radBuild = new RadialMapBuilder();
                Map radialMap = radBuild.build(aMesh, 200);
                enrichedMesh = colorLand(aMesh, radialMap, lagoonMaps);
                // CircularMapBuilder circleBuilder = new CircularMapBuilder();
                // Map circleMap = circleBuilder.build(aMesh, 250);
                // Map radialMap = circleBuilder.radial(aMesh, circleMap); 
                // enrichedMesh = colorLand(aMesh, radialMap, lagoonMaps);
            }
        }
        switch (MODE) {
            case "lagoon" -> {
                CircularMapBuilder circular = new CircularMapBuilder();
                InnerCircularMap innerMap = new InnerCircularMap();
                Map circularMap = circular.build(aMesh, 200);
                for (int i = 0; i < 5; i++) {
                    lagoonMaps.add(innerMap.build(aMesh, 20, circularMap));
                }
                enrichedMesh = colorLand(aMesh, circularMap, lagoonMaps);
            }
            case "irreg" -> {
                IrregularMapBuilder irreg = new IrregularMapBuilder();
                InnerCircularMap innerMap = new InnerCircularMap();
                Map irregMap = irreg.build(aMesh, 250);
                for (int i = 0; i < 5; i++) {
                    lagoonMaps.add(innerMap.build(aMesh, 20, irregMap));
                }
                enrichedMesh = colorLand(aMesh, irregMap, lagoonMaps);
            }
        }
        return enrichedMesh;
    }

    public Map addWaterBodies(Structs.Mesh aMesh, Map map, int lakes, int aquafs){
        if(lakes> 0){
            LakeBuilder lakeBuild = new LakeBuilder();
            map = lakeBuild.build(aMesh, map, lakes);
        }
        if(aquafs>0){
            AquafierBuilder aquafBuild = new AquafierBuilder();
            map = aquafBuild.build(aMesh, map, aquafs);
        }
        return map;
    }

    public Structs.Mesh colorLand(Structs.Mesh aMesh, Map map, ArrayList<Map> lagoonMap){
        ArrayList<Structs.Polygon> land =  map.getLand();
        ArrayList<Structs.Polygon> ocean =  map.getOcean();
        //ArrayList<Structs.Polygon> border =  map.getBorder();
        ArrayList<Structs.Polygon> lakes = map.getLakes();
        ArrayList<Structs.Polygon> aquafiers = map.getAquaf();

    

        ArrayList<Structs.Polygon> lagoon = new ArrayList<>();
        for(Map m: lagoonMap){
            ArrayList<Structs.Polygon> lagoonTiles = m.getLand();
            lagoon.addAll(lagoonTiles);
        }
        Structs.Mesh.Builder clone = Structs.Mesh.newBuilder();
        clone.addAllVertices(aMesh.getVerticesList());
        clone.addAllSegments(aMesh.getSegmentsList());
        String color;
        for(Structs.Polygon poly: aMesh.getPolygonsList()) {
            Structs.Polygon.Builder pc = Structs.Polygon.newBuilder(poly);
            // if(border.contains(poly)){
            //     color = "135,99,41";
            // } // for debuging border
            if(lagoon.contains(poly)){
                color = "173,216,230";
            }
            else if (land.contains(poly)){
                color = "194,201,123";
            } else if(ocean.contains(poly)){
                color = "8,6,148";
            } else if(lakes.contains(poly)){
                color = "65,156,209";
            } 
            //else if(aquafiers.contains(poly)){ // for debug
            //     color = "0,0,0";
            // }
            else {
                color = "0,0,0";
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