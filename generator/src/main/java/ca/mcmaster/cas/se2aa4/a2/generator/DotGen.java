package ca.mcmaster.cas.se2aa4.a2.generator;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Property;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;

public class DotGen {

    // attributes of grid
    private final int width = 500;
    private final int height = 500;
    private final int square_size = 20;

    public Mesh generate() {
        Set<Vertex> vertices = new HashSet<>();
        // Create all the vertices
        for(int x = 0; x < width; x += square_size) {
            for(int y = 0; y < height; y += square_size) {
                vertices.add(Vertex.newBuilder().setX((double) x).setY((double) y).build());
                vertices.add(Vertex.newBuilder().setX((double) x+square_size).setY((double) y).build());
                vertices.add(Vertex.newBuilder().setX((double) x).setY((double) y+square_size).build());
                vertices.add(Vertex.newBuilder().setX((double) x+square_size).setY((double) y+square_size).build());
            }
        }
        // Distribute colors randomly. Vertices are immutable, need to enrich them
        Set<Vertex> verticesWithColors = new HashSet<>();
        Random bag = new Random();
        for(Vertex v: vertices){
            int red = bag.nextInt(255);
            int green = bag.nextInt(255);
            int blue = bag.nextInt(255);
            String colorCode = red + "," + green + "," + blue;
            Property color = Property.newBuilder().setKey("rgb_color").setValue(colorCode).build();
            Vertex colored = Vertex.newBuilder(v).addProperties(0, color).build();
            verticesWithColors.add(colored);
        }
        // Create segments between adjacent vertices
        Set<Segment> segments = new HashSet<>();
        List<Vertex> verticiesList = new ArrayList<Vertex>(verticesWithColors);
        for(Vertex vI: verticiesList){
            for(Vertex vJ: verticiesList){
                if((vJ.getX() ==  vI.getX()+square_size && vJ.getY() == vI.getY()) ||(vJ.getX() ==  vI.getX() && vJ.getY() == vI.getY()+square_size) ){ // find vertex to right 
                    // create and append new segment between vertecies 
                    Segment s = Segment.newBuilder().setV1Idx(verticiesList.indexOf(vI)).setV2Idx(verticiesList.indexOf(vJ)).build(); 
                    // colour the segment: parse the old string color codes, take average of 2 to make new color code
                    segments.add(s);
                } 
            }
        }
        // Distribute colours by averaging vertices
        Set<Segment> segmentsWithColors = new HashSet<>(); 
        for(Segment s: segments){
            String colorStringI = verticiesList.get(s.getV1Idx()).getProperties(0).getValue();    
            String[] colorsI = colorStringI.split(",");
            String colorStringJ = verticiesList.get(s.getV2Idx()).getProperties(0).getValue();    
            String[] colorsJ = colorStringJ.split(",");
            int red = (Integer.parseInt(colorsI[0]) + Integer.parseInt(colorsJ[0])) / 2;
            int green = (Integer.parseInt(colorsI[1]) + Integer.parseInt(colorsJ[1])) / 2;
            int blue = (Integer.parseInt(colorsI[2]) + Integer.parseInt(colorsJ[2])) / 2;
            String colorCode = red + "," + green + "," + blue;
            Property color = Property.newBuilder().setKey("rgb_color").setValue(colorCode).build();
            Segment colored = Segment.newBuilder(s).addProperties(color).build();
            segmentsWithColors.add(colored);
        }
        return Mesh.newBuilder().addAllSegments(segmentsWithColors).addAllVertices(verticesWithColors).build();
    }

}
