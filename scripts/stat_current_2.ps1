$json = Get-Content -Path 'status_current.json' | ConvertFrom-Json
$json.color

Write-Output $json.color

Start-Process run-set-color.bat $json.color