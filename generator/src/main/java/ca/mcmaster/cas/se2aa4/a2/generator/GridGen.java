package ca.mcmaster.cas.se2aa4.a2.generator;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.Random;

import java.lang.String;
//import java.lang.Integer;


import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Property;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;


public class GridGen {

    //attributes 
    public final int square_size = 20;
    //

    public Mesh addSquares(Mesh mesh){

        Set<Segment> segments = new HashSet<>();
        
        List<Vertex> vertices = mesh.getVerticesList(); // get list of vertices from inputed mesh

        // Distribute colors randomly. Vertices are immutable, need to enrich them
        for(Vertex vI: vertices){
            String colorStringI = vI.getProperties(0).getValue();    
            String[] colorsI = colorStringI.split(",");
            for(Vertex vJ: vertices){
                if(vJ.getX() ==  vI.getX()+20 || vJ.getY() ==  vI.getY()+20){ // find vertex to right and below  
                    // create and append new segment between vertecies 
                    Segment s = Segment.newBuilder().setV1Idx(vertices.indexOf(vI)).setV2Idx(vertices.indexOf(vJ)).build(); 
                    // colour the segment: parse the old string color codes, take average of 2 to make new color code
                    String colorStringJ = vJ.getProperties(0).getValue();    
                    String[] colorsJ = colorStringJ.split(",");
                    int red = (Integer.parseInt(colorsI[0]) + Integer.parseInt(colorsJ[0])) / 2;
                    int green = (Integer.parseInt(colorsI[1]) + Integer.parseInt(colorsJ[1])) / 2;
                    int blue = (Integer.parseInt(colorsI[2]) + Integer.parseInt(colorsJ[2])) / 2;
                    String colorCode = red + "," + green + "," + blue;
                    Property color = Property.newBuilder().setKey("rgb_color").setValue(colorCode).build();
                    Segment coloured = Segment.newBuilder(s).addProperties(color).build();
                    segments.add(coloured);
                } //else if(vJ.getY() ==  vI.getY()+20){ // find vertex below
                //     Segment s = Segment.newBuilder().setV1Idx(vertices.indexOf(vI)).setV2Idx(vertices.indexOf(vJ)).build(); 
                //     segments.add(s);
                // }
            }
        }
        //return mesh.toBuilder().addAllSegments(segments).build(); // add Sements to old mesh 
        return Mesh.newBuilder().addAllSegments(segments).build();
    } 
}
