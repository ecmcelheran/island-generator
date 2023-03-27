import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import elevation.MountainBuilder;
import elevation.PeakBuilder;
import elevation.PlateauBuilder;
import map.Map;
import map.shape.CircularMapBuilder;
import map.waterBodies.AquafierBuilder;
import soil.Absorption;
import map.waterBodies.LakeBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MapTest {
    static Map map;
    static Structs.Mesh mesh;
    @BeforeAll
    public static void setContext(){
        mesh = buildRandomMesh();
        map = buildRandomMap();
    }
    @Test
    public void land(){
        //check that inner land and borders makes up land
        ArrayList<Structs.Polygon> inner = map.getInnerLand();
        ArrayList<Structs.Polygon> border = map.getBorder();
        inner.addAll(border);
        assertEquals(inner,map.getLand());
    }
    @Test
    public void meshComponents(){
        //check that lakes, ocean, and land make up the entire mesh
        ArrayList<Structs.Polygon> land = map.getInnerLand();
        land.addAll(map.getOcean());
        land.addAll(map.getLakes());
        assertEquals(land,mesh.getPolygonsList());
    }

    //check that for elevation all the land tiles are assigned a value
    @Test
    public void createMountain(){
        MountainBuilder m = new MountainBuilder();
        m.assignElevations(map,mesh,0);
        assertEquals(map.getElevation().size(),map.getLand().size());
    }
    @Test
    public void createPeaks(){
        PeakBuilder p = new PeakBuilder();
        p.assignElevations(map,mesh,0);
        assertEquals(map.getElevation().size(),map.getLand().size());
    }
    @Test
    public void createPlateau(){
        PlateauBuilder p = new PlateauBuilder();
        p.assignElevations(map,mesh,0);
        assertEquals(map.getElevation().size(),map.getLand().size());
    }

    @Test
    public void createAbsorption(){
        Absorption a = new Absorption("sand");
        Map map = buildRandomMap();
        Structs.Mesh mesh = buildRandomMesh();
        a.defineAbsorption(map,mesh);
        assertEquals(map.getAbsorption().size(),map.getLand().size());
    }

    @Test
    public void createLakes(){
        LakeBuilder l = new LakeBuilder();
        int num = 4;
        l.build(mesh,map,num,0);
        assertNotNull(map.getLakes());
    }

    @Test
    public void createAquifers(){
        AquafierBuilder a = new AquafierBuilder();
        int num = 4;
        a.build(mesh,map,num,0);
        assertNotNull(map.getAquaf());
    }

    @Test
    public void separateWater(){
        ArrayList<Structs.Polygon> ocean = map.getOcean();
        ArrayList<Structs.Polygon> lakes = map.getLakes();
        for(Structs.Polygon p : ocean){
            assertFalse(lakes.contains(p));
        }
    }

    private static Map buildRandomMap(){
        long seed = 0;
        CircularMapBuilder circular = new CircularMapBuilder();
        return circular.build(buildRandomMesh(), 200, seed);
    }

    private static Structs.Mesh buildRandomMesh() {
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
