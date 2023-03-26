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

// import ca.mcmaster.cas.se2aa4.a2.io.Mesh;
// import ca.mcmaster.cas.se2aa4.a2.io.Polygon;
// import ca.mcmaster.cas.se2aa4.a2.io.Vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class LandEnricher implements Enricher{

    private String MODE;
    private String SHAPE;
    private int LAKES;
    private int AQUAF;
    private int RIVER;
    private String ELEVATION;
    private String SOIL;
    private String BIOME;
    private String SEED;
    private HashMap<Integer, String> polygonData;

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
        }

       
        
            map = addWaterBodies(aMesh, map, LAKES, AQUAF, RIVER, seed);
            Absorption a = new Absorption(SOIL);
            a.defineAbsorption(map,aMesh);
            enrichedMesh = colorLand(aMesh, map);
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

    /*public double computeMoisture() {
       / Biome canada = new Biome("Canada", -3, 300);
        double precipitation = canada.getPrecipitation();
        Biome latvia = new Biome("Latvia", 5, 50);
        double precipitation = latvia.getPrecipitation();
        Biome australia = new Biome("Australia", 15, 250);
        double precipitation = australia.getPrecipitation();
        layers = Absorption.getLayers();
        double moisture = precipitation + (layers*10);
        return moisture; 
       
    }

    

    public int computeTemperature(Polygon polygon) {
        int temperature = 0;
    
        // Get the temperature from the biome object of the polygon
        String biomeName = polygon.getBiomeName();
        Biome biome = biomes.get(biomeName);
        temperature += biome.getTemperature();
    
        // Add the temperature modifier based on the elevation of the polygon
        int elevation = polygon.getElevation();
        if (elevation == 0) {
            temperature += 10;
        } else if (elevation == 1) {
            temperature += 5;
        } else if (elevation == 2) {
            temperature -= 2;
        } else if (elevation == 3) {
            temperature -= 7;
        }
    
        return temperature;
    }
    
    public void computeMoisture(Mesh aMesh, HashMap<Integer, Biome> biomes, HashMap<Integer, Double> elevation) {
        int layers = Absorption.getLayers();
        HashMap<Integer, String> polygonData = new HashMap<Integer, String>();
    
        for (Polygon p : aMesh.getPolygonsList()) {
            double precipitation = 0;
            
                Biome currentBiome = biomes.getBiomeName();
                precipitation = currentBiome.getPrecipitation();
            }
    
            double moisture = precipitation + (layers * 10);
    
            int temperature = 0;
            if (biomes.containsKey(p.getBiomeIndex())) {
                Biome currentBiome = biomes.get(p.getBiomeIndex());
                temperature = currentBiome.getTemperature();
            }
    
            double polyElevation = elevation.get(aMesh.getPolygonsList().indexOf(p));
            if (polyElevation == Elevation.FLAT) {
                temperature += 10;
            } else if (polyElevation == Elevation.PLATEAU) {
                temperature += 5;
            } else if (polyElevation == Elevation.MOUNTAIN) {
                temperature -= 2;
            } else if (polyElevation == Elevation.PEAK) {
                temperature -= 7;
            }
    
            String polygonValue = temperature + ":" + moisture;
            polygonData.put(aMesh.getPolygonsList().indexOf(p), polygonValue);
        }
    }
    
    public void computeMoisture(Mesh aMesh) {
        int layers = Absorption.getLayers();
        HashMap<Integer, String> polygonData = new HashMap<Integer, String>();
    
        for (Polygon p : aMesh.getPolygonsList()) {
            double precipitation = 0;
            if (biomes.containsKey(p.getBiome())) {
                Biome currentBiome = biomes.get(p.getBiome());
                precipitation = currentBiome.getPrecipitation();
            }
    
            double moisture = precipitation + (layers * 10);
    
            int temperature = 0;
            if (biomes.containsKey(p.getBiome())) {
                Biome currentBiome = biomes.get(p.getBiome());
                temperature = currentBiome.getTemperature();
            }
    
            if (p.getElevation() == Elevation.FLAT) {
                temperature += 10;
            } else if (p.getElevation() == Elevation.PLATEAU) {
                temperature += 5;
            } else if (p.getElevation() == Elevation.MOUNTAIN) {
                temperature -= 2;
            } else if (p.getElevation() == Elevation.PEAK) {
                temperature -= 7;
            }
    
            String polygonValue = temperature + ":" + moisture;
            polygonData.put(aMesh.getPolygonsList().indexOf(p), polygonValue);
        }
    }
    */

    public void computeB(Mesh aMesh){

        HashMap<Integer, String> polygonData = new HashMap<Integer, String>();
       
        int temperature = 0;
        // return moisture;
        //int temperature = Elevation.assignTemp(0)+ Biome.getTemperature();

        switch(ELEVATION){
            case "mountain" ->{
                MountainBuilder m = new MountainBuilder();
                 temperature = m.assignTemp(0);
            }
            case "plateau"->{
                PlateauBuilder pl = new PlateauBuilder();
                 temperature =  pl.assignTemp(0);
            }
            case "peak"->{
                PeakBuilder pe = new PeakBuilder();
                pe.setNum(3);
                 temperature = pe.assignTemp(0);
            }
            case "flat"->{
                FlatBuilder f = new FlatBuilder();
                 temperature = f.assignTemp(0);
            }
        }
        for (Polygon p : aMesh.getPolygonsList()) {
            int moisture = Absorption.getHumidity() + Biome.getPrecipitation();
            String polygonValue = temperature + ":" + moisture;
            polygonData.put(aMesh.getPolygonsList().indexOf(p), polygonValue);}
        
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
            color = "0,0,0";
            Structs.Polygon.Builder pc = Structs.Polygon.newBuilder(poly);
            // if(border.contains(poly)){
            //     color = "135,99,41";
            // }
            if (beach.contains(poly)){
                color = "255,255,153";
            }
            else if (land.contains(poly)){
                for (Polygon polygon : aMesh.getPolygonsList()) {
                    int polygonIndex = aMesh.getPolygonsList().indexOf(polygon);
                   // <List<Double> polygonValues = Whittaker.whittaker(polygonIndex, map, BIOME);           
                    //double temperature = polygonValues.get(0);
                   // double moisture = polygonValues.get(1);
                   List<List<Double>> results = Whittaker.whittaker(polygonIndex, map, BIOME);
                   double temperature = results.get(polygonIndex).get(0);
                   double moisture = results.get(polygonIndex).get(1);
                   
                    if (temperature < -10 && moisture >= 100) {
                        color = "13,247,255";
                        // vegetation type is tundra
                    }
                    else if (temperature >= -10 && temperature < 0 && moisture >= 100 && moisture <= 300) {
                        color = "33,79,56";
                        // vegetation type is Taiga
                    }
                    else  if (temperature >= 0 && temperature < 20 && moisture >= 200 && moisture <= 400) {
                        color = "105,255,180";
                        // vegetation type is temperate rainforest
                    }
                    else  if (temperature >= 5 && temperature < 20 && moisture >= 400) {
                        color = "77,255,225";
                        // vegetation type is temperate deciduous forest
                    }
                    else  if (temperature >= 0 && temperature < 20 && moisture >= 100 && moisture <= 400) {
                        color = "233,255,38";
                        // vegetation type is temperate grassland
                    }
                    else if (temperature >= 10 && temperature < 20 && moisture >= 200 && moisture <= 400) {
                        color = "106,255,89";
                    }
                    else if (moisture < 100) {
                        color = "255,244,171";
                    }            
                    else if (temperature >= 20 && moisture >= 100 && moisture < 300) {
                        color = "134,255,110";
                    }
                    else if (temperature >= 20 && moisture >= 300) {
                        color = "53,255,38";
                        // vegetation type is tropical rainforest
                    }
                    
                    
                /*
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
                }*/
                else{
                    color = "166,176,72";
                }
               
            }
         }
            
          else if(ocean.contains(poly)){
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
            rgb = color.split(",");
            blue = Integer.parseInt(rgb[2]);
            blue+=(absorption.get(aMesh.getPolygonsList().indexOf(poly)));
            if(blue>255){
                blue = 255;
            }
            color = (rgb[0] + "," + rgb[1] + "," + blue);
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