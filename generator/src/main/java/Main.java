import ca.mcmaster.cas.se2aa4.a2.generator.DotGen;
import ca.mcmaster.cas.se2aa4.a2.io.MeshFactory;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;

import ca.mcmaster.cas.se2aa4.a2.generator.wrappers.MeshM;
import org.apache.commons.cli.*;

import java.io.IOException;

import javax.sound.sampled.SourceDataLine;
import java.util.List;
public class Main {

    public static void main(String[] args) throws IOException {
        // DotGen generator = new DotGen();
        // Mesh myMesh = generator.generate();
        // MeshFactory factory = new MeshFactory();
        // factory.write(myMesh, args[0]);
        // System.out.println("low level Mesh: ");
        // System.out.println("|Vertices| = "+ myMesh.getVerticesList().size());
        // System.out.println("|Segments| = "+ myMesh.getSegmentsList().size());
        //System.out.println("|Polygons| = "+ myMesh.getPolygonsList().size())

        //create command line parser
        CommandLineParser parser = new DefaultParser();
        //create options
        Options options = new Options();
        options.addOption("I", "irregular", false, "Make irregular mesh");
        options.addOption("G", "grid", false, "Make regular grid");
        options.addOption("h", "help", false, "Command line usage");
        options.addOption("r", "relaxation", true, "Relaxation level");
       // options.addOption("w", "width", true, "Width of grid");
        //options.addOption("h", "height", true, "Height of grid");
        options.addOption("p", true, "Number of polygons, height, and width (separated by commas)");

        try {
            CommandLine cmd = parser.parse(options, args);
            if(cmd.hasOption("h")){
                help(options);
            }
            else {
                int height = 500;
                int width = 500;
                double square_size = 20.00;
                int relaxationLevel = 10;
                if(cmd.hasOption("p")){
                    String polygonsStr = cmd.getOptionValue("p");
                    if (polygonsStr != null) {
                        String[] inputs = polygonsStr.split(",");
                        int numPolygons = Integer.parseInt(inputs[0]);
                         height = Integer.parseInt(inputs[0]);
                         width = Integer.parseInt(inputs[0]);
                         square_size = Math.sqrt((height*width)/(numPolygons));


                   // numPolygons = Integer.parseInt(cmd.getOptionValue("p"));
                }
                     //else {
                       // double square_size = 20.00;
                       // int height = 500;
                        //int width = 500;
             //   }
                
            
     //       if(cmd.hasOption("p")){
       //         numPolygons = Integer.parseInt(cmd.getOptionValue("p"));
         //   }
     //       if(cmd.hasOption("r")){
       //         relaxationLevel = Integer.parseInt(cmd.getOptionValue("r"));
        //    }
          //  MeshM meshMaker = null;
           // Mesh myMesh = null;


            if(cmd.hasOption("I")){
               
                if(cmd.hasOption("r")){
                    relaxationLevel = Integer.parseInt(cmd.getOptionValue("r"));
                }
                System.out.println("In main...");
                MeshM meshMaker = new MeshM(square_size, width, height, 1);
                System.out.println("passed constructor...");
                meshMaker.makeIrregularGrid();
                System.out.println("made grid...");

                //meshMaker.createAllCentroids();
                //System.out.println("added centroids...");
                //meshMaker.findNeighbourhoods();
                //System.out.println("found neighourhoods");
                Mesh myMesh = meshMaker.buildGrid();
                System.out.println("built grid");

                MeshFactory factory1 = new MeshFactory();
                factory1.write(myMesh, args[0]);
                System.out.println("Mesh ADT: ");
                System.out.println("|Vertices| = "+ myMesh.getVerticesList().size());
                System.out.println("|Segments| = "+ myMesh.getSegmentsList().size());
                System.out.print("|Polygons| = ");
                System.out.println(myMesh.getPolygonsList().size());
                //System.out.println("|Segments for first polygon in list| = " + myMesh.getPolygons(600).getSegmentIdxsCount());
            //    if (cmd.hasOption("r")) {
              //      relaxationLevel = Integer.parseInt(cmd.getOptionValue("r")); // Assign relaxationLevel 
               // }
            }
            else if(cmd.hasOption("G")){
             
                if(cmd.hasOption("r")){
                    relaxationLevel = Integer.parseInt(cmd.getOptionValue("r"));
                }
                System.out.println("In main...");
                MeshM meshMaker = new MeshM(square_size, width, height, 1);
                System.out.println("passed constructor...");
                meshMaker.makeGrid();
                System.out.println("made grid...");
                meshMaker.createPolygons();// stuck
                System.out.println("made polygons...");
                meshMaker.createAllCentroids();
                System.out.println("added centroids...");
                meshMaker.findNeighbourhoods();
                System.out.println("found neighourhoods");
                Mesh myMesh = meshMaker.buildGrid();
                System.out.println("built grid");

                MeshFactory factory1 = new MeshFactory();
                factory1.write(myMesh, args[0]);
                System.out.println("Mesh ADT: ");
                System.out.println("|Vertices| = "+ myMesh.getVerticesList().size());
                System.out.println("|Segments| = "+ myMesh.getSegmentsList().size());
                System.out.print("|Polygons| = ");
                System.out.println(myMesh.getPolygonsList().size());
                System.out.println("|Segments for first polygon in list| = " + myMesh.getPolygons(0).getSegmentIdxsCount());
            }

        }
    }
}
        catch(Exception e){
            System.err.println("Parsing Failed");
            System.out.println(e);
            help(options);
        }
    

        
        /*
        System.out.println("In main...");
        MeshM meshMaker = new MeshM(20, 500, 500, 1);
        System.out.println("passed constructor...");
        meshMaker.makeGrid();
        System.out.println("made grid..."); 
        meshMaker.createPolygons();// stuck
        System.out.println("made polygons...");
        meshMaker.createAllCentroids();
        System.out.println("added centroids...");
        meshMaker.findNeighbourhoods();
        System.out.println("found neighourhoods");
        Mesh myMesh = meshMaker.buildGrid();
        System.out.println("built grid");

        MeshFactory factory1 = new MeshFactory();
        factory1.write(myMesh, args[0]);
        System.out.println("Mesh ADT: ");
        System.out.println("|Vertices| = "+ myMesh.getVerticesList().size());
        System.out.println("|Segments| = "+ myMesh.getSegmentsList().size());
        System.out.print("|Polygons| = ");
        System.out.println(myMesh.getPolygonsList().size());
        System.out.println("|Segments for first polygon in list| = " + myMesh.getPolygons(600).getSegmentIdxsCount());

         */
    }

    //usage outline for command line
    public static void help(Options options){
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar generator.jar filename.mesh [options]", options);
        System.out.println();
    }

}
