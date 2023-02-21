import ca.mcmaster.cas.se2aa4.a2.generator.DotGen;
import ca.mcmaster.cas.se2aa4.a2.io.MeshFactory;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import ca.mcmaster.cas.se2aa4.a2.generator.wrappers.MeshM;
import java.io.IOException;

import javax.sound.sampled.SourceDataLine;

public class Main {

    public static void main(String[] args) throws IOException {
        DotGen generator = new DotGen();
        Mesh myMesh = generator.generate();
        MeshFactory factory = new MeshFactory();
        factory.write(myMesh, args[0]);
        System.out.println("low level Mesh: ");
        System.out.println("|Vertices| = "+ myMesh.getVerticesList().size());
        System.out.println("|Segments| = "+ myMesh.getSegmentsList().size());
        //System.out.println("|Polygons| = "+ myMesh.getPolygonsList().size());
        
        
        System.out.println("In main...");
        MeshM meshMaker = new MeshM(20, 500, 500, 1);
        System.out.println("passed constructor...");
        meshMaker.makeGrid();
        System.out.println("made grid..."); 
        meshMaker.createPolygons();// stuck
        System.out.println("made polygons...");
        meshMaker.createAllCentroids();
        System.out.println("added centroids...");
        Mesh myMesh1 = meshMaker.buildGrid();
        System.out.println("built grid");

        // MeshFactory factory1 = new MeshFactory();
        // factory1.write(myMesh, args[0]);
        System.out.println("Mesh ADT: ");
        System.out.println("|Vertices| = "+ myMesh1.getVerticesList().size());
        System.out.println("|Segments| = "+ myMesh1.getSegmentsList().size());
        //System.out.println("|Polygons| = "+ myMesh1.getPolygonsList().size());
    }

}
