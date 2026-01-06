$apiRoot = "C:\xampp\htdocs\petpulse_api"

Write-Host "Applying FINAL STRICT fixes (Output Buffering) to PetPulse Backend at $apiRoot..." -ForegroundColor Cyan

# Ensure directories exist
New-Item -ItemType Directory -Force -Path "$apiRoot\utils" | Out-Null
New-Item -ItemType Directory -Force -Path "$apiRoot\pets" | Out-Null
New-Item -ItemType Directory -Force -Path "$apiRoot\uploads" | Out-Null

# ---------------------------------------------------------
# 1. FIXED db.php (Output Buffered)
# ---------------------------------------------------------
$dbPhpContent = @'
<?php
// Ensure no output happens before we want it
ob_start();
header('Content-Type: application/json; charset=utf-8');

$servername = "localhost";
$username = "root";
$password = "";
$dbname = "petpulse";

$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    ob_clean(); // Clear buffer
    echo json_encode(["status" => "error", "message" => "Connection failed: " . $conn->connect_error]);
    exit();
}
?>
'@
Set-Content -Path "$apiRoot\utils\db.php" -Value $dbPhpContent -Encoding UTF8
Write-Host "✅ Fixed: utils/db.php" -ForegroundColor Green

# ---------------------------------------------------------
# 2. FIXED add_pet.php (Strict Output Control)
# ---------------------------------------------------------
$addPetContent = @'
<?php
ob_start();
header('Content-Type: application/json; charset=utf-8');

require '../utils/db.php';

$response = [];

// STRICT: Only use $_POST. No JSON fallback.
$user_id = $_POST['user_id'] ?? null;
$name = $_POST['name'] ?? null;
$species = $_POST['species'] ?? 'Unknown';
$breed = $_POST['breed'] ?? 'Unknown';
$age = $_POST['age'] ?? 0;
$weight = $_POST['weight'] ?? 0.0;
$gender = $_POST['gender'] ?? 'Unknown';

if ($user_id && $name) {

    $image_url = null;

    if (isset($_FILES['image']) && $_FILES['image']['error'] === UPLOAD_ERR_OK) {
        $uploadDir = __DIR__ . '/../uploads/';
        if (!is_dir($uploadDir)) {
            mkdir($uploadDir, 0777, true);
        }

        $fileName = uniqid('pet_', true) . '.jpg';
        $targetPath = $uploadDir . $fileName;

        if (move_uploaded_file($_FILES['image']['tmp_name'], $targetPath)) {
            $image_url = 'uploads/' . $fileName;
        }
    }

    $stmt = $conn->prepare("
        INSERT INTO pets (user_id, name, species, breed, age, weight, gender, image_url)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
    ");

    $stmt->bind_param(
        "isssidss",
        $user_id,
        $name,
        $species,
        $breed,
        $age,
        $weight,
        $gender,
        $image_url
    );

    if ($stmt->execute()) {
        $response = [
            "status" => "success",
            "pet" => [
                "pet_id" => (int)$stmt->insert_id,
                "name" => $name,
                "image_url" => $image_url
            ]
        ];
    } else {
        $response = [
            "status" => "error",
            "message" => "Database error: " . $stmt->error
        ];
    }
} else {
    $response = [
        "status" => "error", 
        "message" => "Missing required fields (user_id, name)"
    ];
}

ob_clean(); // Discard any previous output/warnings
echo json_encode($response, JSON_UNESCAPED_SLASHES);
exit;
?>
'@
Set-Content -Path "$apiRoot\pets\add_pet.php" -Value $addPetContent -Encoding UTF8
Write-Host "✅ Fixed: pets/add_pet.php" -ForegroundColor Green

Write-Host "---------------------------------------------------------"
Write-Host "🎉 BACKEND FINAL FIX APPLIED (OUTPUT BUFFERING)!" -ForegroundColor Cyan
Write-Host "Please restart XAMPP Apache." -ForegroundColor Cyan
