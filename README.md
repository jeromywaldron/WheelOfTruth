<!-- Nikita Kouevda and Jenny Shen -->
<!-- 2012/03/27 -->

# Wheel of Fortune

Wheel of Fortune implemented in Java.

### To compile:

    javac wof/game/*.java wof/gui/*.java

or (JAR):

    javac wof/game/*.java wof/gui/*.java
    jar cfm WheelOfFortune.jar MANIFEST.MF wof/game/*.class wof/gui/*.class wof/images/*.png wof/phrases/phrases.txt wof/sounds/*.wav

### To run:

    java wof/gui/WheelOfFortuneFrame

or (JAR):

    java -jar WheelOfFortune.jar
