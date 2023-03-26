import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import map.Map;
import map.elevation.*;
import map.shape.CircularMapBuilder;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ElevationTest {
    @Test
    public void createMountain(){
        MountainBuilder m = new MountainBuilder();
        Map mountainMap = buildRandomMap();
        Structs.Mesh mountainMesh = buildRandomMesh();
        m.assignElevations(mountainMap,mountainMesh,0);
        assertNotNull(mountainMap.getElevation());
    }
    @Test
    public void createPeaks(){
        PeakBuilder p = new PeakBuilder();
        Map peakMap = buildRandomMap();
        Structs.Mesh peakMesh = buildRandomMesh();
        p.assignElevations(peakMap,peakMesh,0);
        assertNotNull(peakMap.getElevation());
    }
    @Test
    public void createPlateau(){
        PlateauBuilder p = new PlateauBuilder();
        Map plateauMap = buildRandomMap();
        Structs.Mesh plateauMesh = buildRandomMesh();
        p.assignElevations(plateauMap,plateauMesh,0);
        assertNotNull(plateauMap.getElevation());
    }
    private Map buildRandomMap(){
        long seed = 0;
        CircularMapBuilder circular = new CircularMapBuilder();
        return circular.build(buildRandomMesh(), 200, seed);
    }

    private Structs.Mesh buildRandomMesh() {
        Random bag = new Random();
        // random vertices:
        List<Structs.Vertex> vertices = new ArrayList<>();
        for (int i = 0; i < bag.nextInt(10,50); i++){
            Structs.Vertex v = Structs.Vertex.newBuilder()
                    .setX(bag.nextDouble(0.0, 1000.0))
                    .setY(bag.nextDouble(0.0, 1000.0))
                    .build();
            vertices.add(v);
        }
        // random segments:
        List<Structs.Segment> segments = new ArrayList<>();
        for (int i = 0; i < bag.nextInt(10,50); i++) {
            int v1_idx = bag.nextInt(vertices.size());
            int v2_idx = bag.nextInt(vertices.size());
            Structs.Segment s = Structs.Segment.newBuilder().setV1Idx(v1_idx).setV2Idx(v2_idx).build();
            segments.add(s);
        }

        return Structs.Mesh.newBuilder()
                .addAllVertices(vertices)
                .addAllSegments(segments)
                .build();
    }
}
