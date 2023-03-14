import configuration.Configuration;
import enricher.LandEnricher;
import ca.mcmaster.cas.se2aa4.a2.io.MeshFactory;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException{ 
        Configuration config = new Configuration(args);
        
        Structs.Mesh mesh = new MeshFactory().read(config.export(Configuration.INPUT));
        mesh = new LandEnricher(config).process(mesh);

        // if(config.export().containsKey(Configuration.SHAPE)) {
        //     mesh = new LandEnricher(config.export(Configuration.SHAPE)).process(mesh);
        // }
        new MeshFactory().write(mesh, config.export(Configuration.OUTPUT));

    }
}
