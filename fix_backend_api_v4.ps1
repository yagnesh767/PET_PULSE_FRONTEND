$apiRoot = "C:\xampp\htdocs\petpulse_api"

Write-Host "Applying V4 fixes (Get Pet By ID) to PetPulse Backend at $apiRoot..." -ForegroundColor Cyan

# ---------------------------------------------------------
# 1. FIXED get_pet_by_id.php (Fetch single pet)
# ---------------------------------------------------------
$getPetByIdContent = @'
<?php
header('Content-Type: application/json');
error_reporting(0);
ini_set('display_errors', 0);

require '../utils/db.php';

if (isset($_GET['pet_id'])) {
    $pet_id = $_GET['pet_id'];
    
    $stmt = $conn->prepare("SELECT * FROM pets WHERE id = ?");
    $stmt->bind_param("i", $pet_id);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($row = $result->fetch_assoc()) {
        // Alias image_url to photo consistent with list_pets.php
        if (isset($row['image_url']) && !empty($row['image_url'])) {
            $row['photo'] = $row['image_url'];
        } else {
            $row['photo'] = null;
        }

        echo json_encode([
            "status" => "success",
            "pet" => $row
        ]);
    } else {
         echo json_encode([
            "status" => "error",
            "message" => "Pet not found"
        ]);
    }

    $stmt->close();
} else {
    echo json_encode([
        "status" => "error",
        "message" => "Missing pet_id"
    ]);
}

$conn->close();
?>
'@
Set-Content -Path "$apiRoot\pets\get_pet_by_id.php" -Value $getPetByIdContent -Encoding UTF8
Write-Host "✅ Created: pets/get_pet_by_id.php" -ForegroundColor Green

Write-Host "---------------------------------------------------------"
Write-Host "🎉 BACKEND V4 APPLIED!" -ForegroundColor Cyan
