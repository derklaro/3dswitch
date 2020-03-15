# 3DSwitch [![Discord](https://img.shields.io/discord/499666347337449472.svg?color=7289DA&label=discord)](https://discord.gg/uskXdVZ)

3DSwitch is a school project which provides a simple tool which is build to print out 3D files. You can
simply upload the files to a **discord server** or to the integrated **web module**. By default the files
will get added to a **slice queue** then sliced with the **prusa slicer**. After the process the sliced file
will get added to the **print queue** which works with the **octoprint** api and starts a new print when
octoprint says it's available to print **now**.

# Support our work
If you like 3DSwitch and want to support our work you can **star** :star2: the project or join our 
[Discord](https://discord.gg/uskXdVZ).

But the best support for our work is very simple: ***use the system!***

# Build this project
Windows:
```
git clone https://github.com/derklaro/3dswitch.git
cd 3dswitch/
gradlew.bat clean shadowJar
```

Linux/OsX:
```
git clone https://github.com/derklaro/3dswitch.git
cd 3dswitch/
gradlew clean shadowJar
```

# Open Source Libraries
| Library                                                             | Author                                                | License                                                                                                       |
|---------------------------------------------------------------------|-------------------------------------------------------|---------------------------------------------------------------------------------------------------------------|
| [Javalin](https://github.com/tipsy/javalin/)                        | [tipsy](https://github.com/tipsy/)                    | [Apache Licence 2.0](https://github.com/tipsy/javalin/blob/master/LICENSE)                                    |
| [Gson](https://github.com/google/gson/)                             | [Google](https://github.com/google/)                  | [Apache License 2.0](https://github.com/google/gson/blob/master/LICENSE)                                      |
| [JLine 3](https://github.com/jline/jline3/)                         | [JLine](https://github.com/jline/)                    | [The 3-Clause BSD License](https://github.com/jline/jline3/blob/master/LICENSE.txt)                           |
| [java-annotations](https://github.com/JetBrains/java-annotations/)  | [JetBrains](https://github.com/JetBrains/)            | [Apache Licence 2.0](https://github.com/JetBrains/java-annotations/blob/master/LICENSE.txt)                   |
| [JDA](https://github.com/DV8FromTheWorld/JDA)                       | [DV8FromTheWorld](https://github.com/DV8FromTheWorld/)| [Apache License 2.0](https://github.com/DV8FromTheWorld/JDA/blob/master/LICENSE)                              |
| [Octprint Java Lib](https://github.com/robweber/octoprint-java-lib) | [robweber](https://github.com/robweber/)              | [MIT License](https://github.com/robweber/octoprint-java-lib/blob/master/LICENSE)                             |
| [H2](https://github.com/h2database/h2database/)                     | [h2database](https://github.com/h2database/)          | [Dual Licenced (MPL 2.0/EPL 1.0](https://github.com/h2database/h2database/blob/master/LICENSE.txt)            |