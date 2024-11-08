@@echo off

curl -o status_current.json https://servingnachos.com/status/stat.php

powershell.exe -file stat_current_2.ps1