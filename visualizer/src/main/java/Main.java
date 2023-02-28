import ca.mcmaster.cas.se2aa4.a2.io.MeshFactory;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.visualizer.GraphicRenderer;
import ca.mcmaster.cas.se2aa4.a2.visualizer.MeshDump;
import ca.mcmaster.cas.se2aa4.a2.visualizer.SVGCanvas;

import java.awt.*;
import java.io.IOException;

import org.apache.commons.cli.*;

public class Main {

    public static void main(String[] args) throws IOException {
        //create command line parser
        CommandLineParser parser = new DefaultParser();
        boolean visualize = false;
        //create options
        Options options = new Options();
        options.addOption("X", false, "Visualization mode");
        options.addOption("h", "help", false, "Command line usage");

        try {
            CommandLine cmd = parser.parse(options, args);
            if(cmd.hasOption("h")){
                help(options);
            }
            else if(cmd.hasOption("X")){
                visualize = true;
            }
        }catch(Exception e){
            System.err.println("Parsing Failed");
            help(options);
        }

        // Extracting command line parameters
        String input = args[0];
        String output = args[1];
        // Getting width and height for the canvas
        Structs.Mesh aMesh = new MeshFactory().read(input);
        // double max_x = Double.MIN_VALUE;
        // double max_y = Double.MIN_VALUE;
        // for (Structs.Vertex v: aMesh.getVerticesList()) {
        //     max_x = (Double.compare(500, v.getX()) < 0? v.getX(): 500);
        //     max_y = (Double.compare(500, v.getY()) < 0? v.getY(): 500);
        // }
        int max_x = 500;
        int max_y = 500;
        
        // Creating the Canvas to draw the mesh
        Graphics2D canvas = SVGCanvas.build((int) Math.ceil(max_x), (int) Math.ceil(max_y));
        GraphicRenderer renderer = new GraphicRenderer();
        // Painting the mesh on the canvas
        renderer.render(aMesh, canvas, visualize);
        // Storing the result in an SVG file
        SVGCanvas.write(canvas, output);
        // Dump the mesh to stdout
        MeshDump dumper = new MeshDump();
        dumper.dump(aMesh);
    }

    //usage outline for command line
    public static void help(Options options){
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar visualizer.jar ../generator/filename.mesh [options]", options);
        System.out.println();
    }
}
