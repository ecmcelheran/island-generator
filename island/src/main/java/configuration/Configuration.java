package configuration;

import org.apache.commons.cli.*;

import java.util.HashMap;
import java.util.Map;

public class Configuration {

    public static final String MODE = "m";
    public static final String INPUT = "i";
    public static final String OUTPUT = "o";
    public static final String SHAPE = "shape";
    public static final String HELP = "help";



    private CommandLine cli;

    public Configuration(String[] args) {
        try {
            this.cli = parser().parse(options(), args);
            if (cli.hasOption(HELP)) {
                help();
            }
        } catch (ParseException pe) {
            throw new IllegalArgumentException(pe);
        }
    }

    private void help() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar island.jar", options());
        System.exit(0);
    }

    public Map<String, String> export() {
        Map<String, String> result = new HashMap<>();
        for(Option o: cli.getOptions()){
            result.put(o.getOpt(), o.getValue(""));
        }
        return result;
    }

    public String export(String key) {
        return cli.getOptionValue(key);
    }

    private CommandLineParser parser() {
        return new DefaultParser();
    }

    private Options options() {
        Options options = new Options();
        options.addOption(new Option(MODE, true, "Island mode"));
        options.addOption(new Option(INPUT, true, "Input mesh name"));
        options.addOption(new Option(OUTPUT, true, "Output mesh name"));
        options.addOption(new Option(SHAPE, true, "Island border shape"));

        // Demo mode (filling the mesh with random properties
        //options.addOption(new Option(DEMO, false, "activate DEMO mode"));
        // Global help
        options.addOption(new Option(HELP, false, "print help message"));
        return options;
    }

}
