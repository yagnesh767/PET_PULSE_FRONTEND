$apiRoot = "C:\xampp\htdocs\petpulse_api"

Write-Host "--- CHECKING: $apiRoot\utils\db.php ---"
if (Test-Path "$apiRoot\utils\db.php") {
    Get-Content "$apiRoot\utils\db.php" -TotalCount 20
}
else {
    Write-Host "MISSING!"
}

Write-Host "`n--- CHECKING: $apiRoot\pets\add_pet.php ---"
if (Test-Path "$apiRoot\pets\add_pet.php") {
    Get-Content "$apiRoot\pets\add_pet.php"
}
else {
    Write-Host "MISSING!"
}
