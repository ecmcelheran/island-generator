import configuration.Configuration;
import enricher.LandEnricher;
import ca.mcmaster.cas.se2aa4.a2.io.MeshFactory;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException{ 
        Configuration config = new Configuration(args);
        //Buildable specification = SpecificationFactory.create(config);
        //Mesh theMesh = specification.build();
        //Structs.Mesh exported = new Exporter().run(theMesh);
        Structs.Mesh plainMesh = new MeshFactory().read(config.export(Configuration.INPUT));
        //if(config.export().containsKey(Configuration.MODE)) {
        Structs.Mesh land = new LandEnricher(config.export(Configuration.MODE)).process(plainMesh);
        //}
        new MeshFactory().write(land, config.export(Configuration.OUTPUT));

    }
}
