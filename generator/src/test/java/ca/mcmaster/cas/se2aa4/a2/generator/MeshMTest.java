package ca.mcmaster.cas.se2aa4.a2.generator;
import ca.mcmaster.cas.se2aa4.a2.generator.wrappers.MeshM;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class MeshMTest {
    @Test
    public void meshExists(){
        MeshM meshMaker = new MeshM(20, 500, 500, 1);
        assertNotNull(meshMaker);
    }

}
