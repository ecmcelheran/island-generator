# Island Generator (Assignment #3 Walkthrough)

  - Author: Emily McElheran
  - Author: Rawan
  - Author: Luna

It creates two jars:

  1. `generator/generator.jar` to generate meshes
  2. `visualizer/visualizer.jar` to visualize such meshes as SVG files

## Examples of execution

### Generating a mesh, grid or irregular

```
mosser@azrael A2 % java -jar generator/generator.jar -k grid -h 1080 -w 1920 -p 1000 -s 20 -o img/grid.mesh
mosser@azrael A2 % java -jar generator/generator.jar -k irregular -h 1080 -w 1920 -p 1000 -s 20 -o img/irregular.mesh
```

One can run the generator with `-help` as option to see the different command line arguments that are available

### Visualizing a mesh, (regular or debug mode)

```
mosser@azrael A2 % java -jar visualizer/visualizer.jar -i img/grid.mesh -o img/grid.svg          
mosser@azrael A2 % java -jar visualizer/visualizer.jar -i img/grid.mesh -o img/grid_debug.svg -x
mosser@azrael A2 % java -jar visualizer/visualizer.jar -i img/irregular.mesh -o img/irregular.svg   
mosser@azrael A2 % java -jar visualizer/visualizer.jar -i img/irregular.mesh -o img/irregular_debug.svg -x
```

Note: PDF versions of the SVG files were created with `rsvg-convert`.


### A3 - Using Command Line to Make Islands
The generator subproject can be used to generate a mesh. In general, our island subproject can support any sized mesh, but one should generate relatively small meshes in order to keep the runtime of our program to be acceptable. A well sized mesh can be implemented using the following command: 
 java -jar generator.jar -k irregular -h 500 -w 500 -p 1000 -s 15 -o ../img/input.mesh

Island shapes can be implemented via the tag "-shape" followed by one of our implemented shapes, including circle and irregular (irreg for CLA). Water bodies as added via the commands -rivers -lakes - auqifers and the input is the number of units of that water body implemented in the map(-rivers 5 means 5 rivers are added). Elevation types can be added via "-elevations" tag to implement mountains, peaks (peak), plateaus (plateau) and flat lands (flat). Soil absorption for the island can be defined using the "-soil" tag and entering the desired soil type. These types can be sand, silt, or clay, which all absorb water differently. Finally, the "-biome" tag can be used to define a biome.

Example command that operated on the mesh created above:
java -jar island.jar -i ../img/input.mesh -o ../img/ouput.mesh -shape irreg -elevation mountain -lakes 5 -rivers 4 -auqifers 0 -soil sand -biome canada

To visualize this mesh, run the command below:
java -jar visualizer.jar -i ../img/output.mesh -o ../img/output.svg

### Summary of Options for Island Generation
Option : Argument
- i : input mesh
- o : output mesh
- shape : circle/irreg
- lakes : integer
- rivers : integer
- aquifers : integer
- elevation : peak/mountain/plateau/flat
- soil : silt/sand/clay
- biome : canada/australia/latvia

Note: The only mandatory arguments are the input and output mesh, failing to specify any other arguments will result in the use of default settings. This means that a flat circular island is generated without any water bodies, silt as the soil type and Canada as the biome.

### Backlog
| Id | Feature title | Who? | Start | End | Status |
|:--:|---------------|------|-------|-----|--------|
|  F01  | Generate circular land surrounded by oceans | Rawan | 03/09/23 | 03/10/23 |    D    |
|  F01.1  | Differentiate ocean and internal water bodies | Rawan | 03/20/23 | 03/20/23 |    D    |
|  F02  | Generate internal lagoons | Emily | 03/13/23 | 03/15/23 |    D    |
|  F03  | Generate beaches | Luna | 03/10/23 | 03/23/23 |    D    |
|  F04  | Generate irregular shaped islands | Rawan | 03/02/23 | 03/04/23 |    D    |
|  F05  | Implement altimetric profiles | Emily | 03/20/23 | 03/22/23 |    D    |
|  F06  | Generate lakes | Rawan | 02/02/23 | 02/04/23 |    D    |
|  F06.1  | Generate lakes at low elevations | Luna | 03/25/23 | 03/25/23 |    D    |
|  F07  | Generate rivers | Rawan | 03/21/23 | 03/23/23 |    D    |
|  F10  | Generate Aquifers | Rawan | 03/21/23 | 03/21/23 |    D    |
|  F11  | Implement varying soil absorptions | Emily | 03/22/23 | 03/24/23 |    D    |
|  F12  | Define different biomes| Luna | 03/24/23 | 03/25/23 |    D    |
|  F13  | Implement biome distributions | Luna | 03/25/23 | 03/26/23 |    D    |
|  F14  | Allow user to reproduce maps | Rawan | 03/02/23 | 03/04/23 |    D    |

Note: To distinguish the way features were developed there are a couple places in the backlog with a feature (i.e. F01) and an extension of this feature (F01.1). For the purposes of code theses are considered the same feature within our commit messages and only the original feature number is used.

