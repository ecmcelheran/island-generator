package enricher;
 
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import map.elevation.FlatBuilder;
import map.elevation.MountainBuilder;
import map.elevation.PeakBuilder;
import map.elevation.PlateauBuilder;
import map.CircularMapBuilder;
import map.IrregularMapBuilder;
import map.Map;
import map.OvularMapBuilder;
import map.RadialMapBuilder;
import map.soil.Absorption;
import map.waterBodies.AquafierBuilder;
import map.waterBodies.LakeBuilder;
import map.waterBodies.RiverBuilder;
import configuration.Configuration;
// import ca.mcmaster.cas.se2aa4.a2.io.Mesh;
// import ca.mcmaster.cas.se2aa4.a2.io.Polygon;
// import ca.mcmaster.cas.se2aa4.a2.io.Vertex;

import java.util.ArrayList;
import java.util.HashMap;


public class LandEnricher implements Enricher{

    private String MODE;
    private String SHAPE;
    private int LAKES;
    private int AQUAF;
    private int RIVER;
    private String ELEVATION;
    private String SOIL;



    

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
        if(config.export().containsKey(Configuration.ELEVATION))
            this.ELEVATION = config.export(Configuration.ELEVATION); // add config
        else
            this.ELEVATION = "flat";
        if(config.export().containsKey(Configuration.RIVER))
            this.RIVER = Integer.parseInt(config.export(Configuration.RIVER)); // add config
        else
            this.RIVER = 0;
        if(config.export().containsKey(Configuration.SOIL))
            this.SOIL = config.export(Configuration.SOIL);
        else
            this.SOIL = "silt";
    }

    @Override
    public Structs.Mesh process(Structs.Mesh aMesh){
        Structs.Mesh enrichedMesh = aMesh;
        ArrayList<Map> lagoonMaps = new ArrayList<>();
        Map map = new Map();
        switch (SHAPE) {
            case "circle" ->{
                CircularMapBuilder circular = new CircularMapBuilder();
                map = circular.build(aMesh, 200);
                //map = addWaterBodies(aMesh, map, LAKES, AQUAF, RIVER);

            }
            case "irreg" ->{
                IrregularMapBuilder irreg = new IrregularMapBuilder();
                map = irreg.build(aMesh, 250);
                //map = addWaterBodies(aMesh, map, LAKES, AQUAF, RIVER);

            }
            case "oval" ->{
                OvularMapBuilder ovalBuild = new OvularMapBuilder();
                map = ovalBuild.build(aMesh, 100);
                //map = addWaterBodies(aMesh, map, LAKES, AQUAF, RIVER);
                //enrichedMesh = colorLand(aMesh, ovalMap, lagoonMaps);
            }
            
            case "radial" ->{
                RadialMapBuilder radBuild = new RadialMapBuilder();
                map = radBuild.build(aMesh, 200);
                //enrichedMesh = colorLand(aMesh, radialMap, lagoonMaps);
                // CircularMapBuilder circleBuilder = new CircularMapBuilder();
                // Map circleMap = circleBuilder.build(aMesh, 250);
                // Map radialMap = circleBuilder.radial(aMesh, circleMap); 
                // enrichedMesh = colorLand(aMesh, radialMap, lagoonMaps);
            }

        }
        switch(ELEVATION){
            case "mountain" ->{
                MountainBuilder m = new MountainBuilder();
                m.assignElevations(map, aMesh);
            }
            case "plateau"->{
                PlateauBuilder p = new PlateauBuilder();
                p.assignElevations(map, aMesh);
            }
            case "peak"->{
                PeakBuilder p = new PeakBuilder();
                p.setNum(3);
                p.assignElevations(map,aMesh);
            }
            case "flat"->{
                FlatBuilder f = new FlatBuilder();
                f.assignElevations(map,aMesh);
            }
        }
        map = addWaterBodies(aMesh, map, LAKES, AQUAF, RIVER);
        Absorption a = new Absorption(SOIL);
        a.defineAbsorption(map,aMesh);
        enrichedMesh = colorLand(aMesh, map);
        return enrichedMesh;
    }

    public Map addWaterBodies(Structs.Mesh aMesh, Map map, int lakes, int aquafs, int rivers){
        if(lakes> 0){
            LakeBuilder lakeBuild = new LakeBuilder();
            map = lakeBuild.build(aMesh, map, lakes);
        }
        if(aquafs>0){
            AquafierBuilder aquafBuild = new AquafierBuilder();
            map = aquafBuild.build(aMesh, map, aquafs);
        }
        if(rivers>0){
            RiverBuilder riverBuild = new RiverBuilder();
            map = riverBuild.build(aMesh, map, rivers);
        }
        return map;
    }

    public Structs.Mesh colorLand(Structs.Mesh aMesh, Map map){
        int blue;
        String[] rgb;
        HashMap<Integer,Double> absorption = map.getAbsorption();
        HashMap<Integer,Double> elevation = map.getElevation();
        ArrayList<Structs.Polygon> land =  map.getLand();
        ArrayList<Structs.Polygon> ocean =  map.getOcean();
        ArrayList<Structs.Polygon> lakes = map.getLakes();
        ArrayList<Structs.Polygon> beach =  map.getBorder();
        //ArrayList<Structs.Polygon> aquafiers = map.getAquaf();
        ArrayList<ArrayList<Integer>> rivers = map.getRivers();
        Structs.Mesh.Builder clone = Structs.Mesh.newBuilder();
        clone.addAllVertices(aMesh.getVerticesList());
        clone.addAllSegments(aMesh.getSegmentsList());
        String color;
        //process color of segments
        for(ArrayList<Integer> river : rivers){
            double thickness = 1.5;
            for(int i=0; i<river.size()-1; i++){
                Structs.Segment.Builder sc = Structs.Segment.newBuilder();
                sc.setV1Idx(river.get(i));
                sc.setV2Idx(river.get(i+1));
                color = "3,90,252";
                Structs.Property c = Structs.Property.newBuilder()
                        .setKey("rgb_color")
                        .setValue(color)
                        .build();
                Structs.Property r = Structs.Property.newBuilder()
                        .setKey("river?")
                        .setValue("true")
                        .build();
                Structs.Property t = Structs.Property.newBuilder()
                        .setKey("thickness")
                        .setValue(Double.toString(thickness))
                        .build();
                sc.addProperties(c);
                sc.addProperties(r);
                sc.addProperties(t);
                clone.addSegments(sc);
                if(thickness>0.7){
                    thickness -= 0.2;
                }
            }
        }
        for(Structs.Segment seg : aMesh.getSegmentsList()){
            Structs.Segment.Builder sc = Structs.Segment.newBuilder(seg);
            color = "79,76,87";
            Structs.Property c = Structs.Property.newBuilder()
                        .setKey("rgb_color")
                        .setValue(color)
                        .build();
            Structs.Property r = Structs.Property.newBuilder()
                        .setKey("river?")
                        .setValue("false")
                        .build();
            Structs.Property t = Structs.Property.newBuilder()
                    .setKey("thickness")
                    .setValue("0.05")
                    .build();
            sc.addProperties(c);
            sc.addProperties(r);
            sc.addProperties(t);
            clone.addSegments(sc);
        }
        for(Structs.Polygon poly: aMesh.getPolygonsList()) {
            Structs.Polygon.Builder pc = Structs.Polygon.newBuilder(poly);
            // if(border.contains(poly)){
            //     color = "135,99,41";
            // }
            if (beach.contains(poly)){
                color = "255,255,153";
            }
            else if (land.contains(poly)){
                System.out.println("land poly");
                if(elevation.get(aMesh.getPolygonsList().indexOf(poly))>150){
                    color = "255,255,255";
                }
                else if(elevation.get(aMesh.getPolygonsList().indexOf(poly))>100){
                    color = "219,223,177";
                }
                else if(elevation.get(aMesh.getPolygonsList().indexOf(poly))>50){
                    color = "202,208,141";
                }
                else if(elevation.get(aMesh.getPolygonsList().indexOf(poly))>25){
                    color = "186,194,105";
                }
                else{
                    color = "166,176,72";
                }
                rgb = color.split(",");
                blue = Integer.parseInt(rgb[2]);
                try{
                    blue+=(absorption.get(aMesh.getPolygonsList().indexOf(poly)));
                }catch(Exception e){
                    System.out.print("This one doesn't work");
                }
                if(blue>255){
                    blue = 255;
                }
                color = (rgb[0] + "," + rgb[1] + "," + blue);
            } else if(ocean.contains(poly)){
                color = "8,6,148";
            } else if(lakes.contains(poly)){
                color = "65,156,209";
            } 
            //else if(aquifers.contains(poly)){ // for debug
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