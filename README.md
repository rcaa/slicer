slicer
======
Running the slicer:

1- Export a jar as slicer.jar with all the source code

2- Place the jar inside "lib" folder of your jpf-core project

3- Go to jpf.properties within jpf-core and add "${jpf-core}/lib/slicer.jar;" to the jpf-core.native_classpath

4- Build the projects

5- Open the jpf properties file of you application (this is not the jpf.properties mentioned at step 3) and configure all the settings

6- Given the exampleApp.jpf is opened, run-JPF.

Requirements and recommendations:

1- Eclipse version: Juno Service Release 2, Build id: 20130225-0426

2- jpf-core: https://github.com/rcaa/jpf-core

3- jre 1.6

Running the slicer with the sudoku application, which is provided in the examples package in the slicer source code:

1- Export a jar as slicer.jar with all the source code

2- Place the jar inside "lib" folder of your jpf-core project

3- Go to jpf.properties within jpf-core and add "${jpf-core}/lib/slicer.jar;" to the jpf-core.native_classpath

4- Build the projects

5- Open the Sudoku_p4_test3.se.t3.jpf file (slicer/examples) and change the following properties to your directory: classpath, sourcepath, save.sliceIdIn, target_args, output.slices, and ck.

6- Given the Sudoku_p4_test3.se.t3.jpf file is opened, run-JPF. Example: http://s11.postimg.org/pt08sm7lf/runjpf.png

More info:
http://pan.cin.ufpe.br/spl/
