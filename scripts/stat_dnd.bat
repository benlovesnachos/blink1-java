@echo off

:: Read the config.txt file and set variables
for /f "tokens=1,2 delims== " %%A in (config.txt) do set %%A=%%B

java ^
 -Djava.awt.headless=true ^
  -cp ..\blink1-library\target\blink1-library-jar-with-dependencies.jar;..\blink1-java-examples\target\blink1-java-examples.jar ^
   com.thingm.blink1.OnOffColor ff0000

:: Use the variables in decisions
if "%remotePostEnabled%"=="true" (
    echo Remote post is enabled.
    curl -X POST -H "Content-Type: application/json" -d @status_dnd.json -k https://servingnachos.com/status/stat.php
) else (
    echo Remote post is disabled.
)
