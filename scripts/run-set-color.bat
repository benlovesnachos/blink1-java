@echo off

cd /D "%~dp0"

java ^
 -Djava.awt.headless=true ^
  -cp ..\blink1-library\target\blink1-library-jar-with-dependencies.jar;..\blink1-java-examples\target\blink1-java-examples.jar ^
   com.thingm.blink1.OnOffColor %1 %2 %3 %4
