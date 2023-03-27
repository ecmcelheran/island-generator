package enricher;
 
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Polygon;
import map.elevation.Elevation;
import map.elevation.FlatBuilder;
import map.elevation.MountainBuilder;
import map.elevation.PeakBuilder;
import map.elevation.PlateauBuilder;
import map.shape.CircularMapBuilder;
import map.shape.IrregularMapBuilder;
import map.Map;
import map.soil.Absorption;
import map.waterBodies.AquafierBuilder;
import map.waterBodies.LakeBuilder;
import map.waterBodies.RiverBuilder;
import configuration.Configuration;
import Biomes.Biome;
import Biomes.Whittaker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class LandEnricher implements Enricher{

    private String SHAPE;
    private int LAKES;
    private int AQUAF;
    private int RIVER;
    private String ELEVATION;
    private String SOIL;
    private String BIOME;
    private String SEED;
    private HashMap<Integer, String> polygonData;

    private Biome thisBiome;
    // private Biome canada;
    // private Biome australia;
    // private Biome latvia;

    public LandEnricher(Configuration config){
        if(config.export().containsKey(Configuration.SHAPE)) 
            this.SHAPE = config.export(Configuration.SHAPE);
        else
            this.SHAPE = "circle";
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
        if(config.export().containsKey(Configuration.BIOME))
            this.BIOME = config.export(Configuration.BIOME); // add config
        else
            this.BIOME = "canada";
        if(config.export().containsKey(Configuration.SEED))
            this.SEED = config.export(Configuration.SEED);
        else
            this.SEED = "none";
     
    }

    @Override
    public Structs.Mesh process(Structs.Mesh aMesh){
        Structs.Mesh enrichedMesh = aMesh;
        ArrayList<Map> lagoonMaps = new ArrayList<>();
        Map map = new Map();

        long seed;
        if(SEED.equals("none")){
            Random rand = new Random();
            seed = rand.nextLong();
            System.out.println("If you would like to reproduce this island, next time please input -seed "+seed);
        }else{
            seed = Long.parseLong(SEED);
        }

        switch (SHAPE) {
            case "circle" ->{
                CircularMapBuilder circular = new CircularMapBuilder();
                map = circular.build(aMesh, 200, seed);

            }
            case "irreg" ->{
                IrregularMapBuilder irreg = new IrregularMapBuilder();
                map = irreg.build(aMesh, 250, seed);
            }
        }
        switch(ELEVATION){
            case "mountain" ->{
                MountainBuilder m = new MountainBuilder();
                m.assignElevations(map, aMesh, seed);
            }
            case "plateau"->{
                PlateauBuilder p = new PlateauBuilder();
                p.assignElevations(map, aMesh, seed);
            }
            case "peak"->{
                PeakBuilder p = new PeakBuilder();
                p.setNum(3);
                p.assignElevations(map,aMesh, seed);
            }
            case "flat"->{
                FlatBuilder f = new FlatBuilder();
                f.assignElevations(map,aMesh, seed);
            }
        }switch(BIOME){
            case("canada")->{
                this.thisBiome = new Biome("Canada", -3, 300);
                break;
            } case("australia")->{
                this.thisBiome = new Biome("Australia", 15, 250);
                break;
            } case("latvia")->{
                this.thisBiome = new Biome("Latvia", 5, 50);
                break;
            }
        }
            map = addWaterBodies(aMesh, map, LAKES, AQUAF, RIVER, seed);
            Absorption a = new Absorption(SOIL);
            a.defineAbsorption(map,aMesh);
            enrichedMesh = colorLand(aMesh, map, thisBiome);
            return enrichedMesh;
    }

    public Map addWaterBodies(Structs.Mesh aMesh, Map map, int lakes, int aquafs, int rivers, long seed){
        if(lakes> 0){
            LakeBuilder lakeBuild = new LakeBuilder();
            map = lakeBuild.build(aMesh, map, lakes, seed);
        }
        if(aquafs>0){
            AquafierBuilder aquafBuild = new AquafierBuilder();
            map = aquafBuild.build(aMesh, map, aquafs, seed);
        }
        if(rivers>0){
            RiverBuilder riverBuild = new RiverBuilder();
            map = riverBuild.build(aMesh, map, rivers, seed);
        }
        return map;
    }



    public Structs.Mesh colorLand(Structs.Mesh aMesh, Map map, Biome biome){
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
            double discharge = Math.random() * 2 + 1;
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
                Structs.Property d = Structs.Property.newBuilder()
                        .setKey("discharge")
                        .setValue(Double.toString(discharge))
                        .build();
                sc.addProperties(c);
                sc.addProperties(r);
                sc.addProperties(t);
                sc.addProperties(d);
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
            color = "162,168,130";
            Structs.Polygon.Builder pc = Structs.Polygon.newBuilder(poly);
            // if(border.contains(poly)){
            //     color = "135,99,41";
            // }
            if (beach.contains(poly)){
                color = "255,255,153";
            }
            else if (land.contains(poly)){
                //for (Polygon polygon : aMesh.getPolygonsList()) {
                    int polygonIndex = aMesh.getPolygonsList().indexOf(poly);
                   List<Double> results = Whittaker.whittaker(polygonIndex, map, biome);
                   double temperature = results.get(0);
                   double moisture = results.get(1);
                   if(temperature<=-5){
                    color = "191,214,209";
                }else if(temperature<=5){
                    if(moisture<= 50){ //temp desert
                        color = "224,180,67";
                    }else if(moisture<=200){//temp taiga
                        color = "33,87,25";
                    }
                }else if(temperature<=20){
                    if(moisture<=25){
                        //subtrop dessert
                    }else if(moisture<=125){
                        color = "224,180,67";
                    }else if(moisture<=250){
                        color ="127,153,96";
                    }else if(moisture<=325){
                        color = "110,184,118";
                    }
                }else if(temperature>20){
                    if(moisture<=50){
                        //stdes
                    }else if(moisture<=250){
                        color = "118,191,44";
                    }else{
                        color = "69,227,16";
                    }
                }else{
                    color = "162,168,130";
                }

            }else if(ocean.contains(poly)){
                color = "8,6,148";
            } else if(lakes.contains(poly)){
                color = "65,156,209";
            }else {
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