$apiRoot = "C:\xampp\htdocs\petpulse_api"

Write-Host "Applying V3 fixes (STRICT Navigation & JSON) to PetPulse Backend at $apiRoot..." -ForegroundColor Cyan

# Ensure directories exist
New-Item -ItemType Directory -Force -Path "$apiRoot\utils" | Out-Null
New-Item -ItemType Directory -Force -Path "$apiRoot\pets" | Out-Null
New-Item -ItemType Directory -Force -Path "$apiRoot\uploads" | Out-Null

# ---------------------------------------------------------
# 1. FIXED db.php (Clean, no echo on success)
# ---------------------------------------------------------
$dbPhpContent = @'
<?php
header('Content-Type: application/json');

$servername = "localhost";
$username = "root";
$password = "";
$dbname = "petpulse";

$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    echo json_encode(["status" => "error", "message" => "Connection failed: " . $conn->connect_error]);
    exit();
}
?>
'@
Set-Content -Path "$apiRoot\utils\db.php" -Value $dbPhpContent -Encoding UTF8
Write-Host "✅ Fixed: utils/db.php" -ForegroundColor Green

# ---------------------------------------------------------
# 2. FIXED add_pet.php (NO ALTER TABLE, Multipart, Clean JSON)
# ---------------------------------------------------------
$addPetContent = @'
<?php
header('Content-Type: application/json');
error_reporting(0);
ini_set('display_errors', 0);

require '../utils/db.php';

$response = array();

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

    // STRICT: NO ALTER TABLE HERE. Assumes column exists.
    
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
        echo json_encode([
            "status" => "success",
            "pet" => [
                "pet_id" => $stmt->insert_id,
                "name" => $name,
                "image_url" => $image_url
            ]
        ]);
        exit;
    } else {
         echo json_encode([
            "status" => "error",
            "message" => "Database error: " . $stmt->error
        ]);
        exit;
    }
} else {
    echo json_encode([
        "status" => "error", 
        "message" => "Missing required fields (user_id, name)"
    ]);
    exit;
}
?>
'@
Set-Content -Path "$apiRoot\pets\add_pet.php" -Value $addPetContent -Encoding UTF8
Write-Host "✅ Fixed: pets/add_pet.php" -ForegroundColor Green

# ---------------------------------------------------------
# 3. FIXED list_pets.php (Clean, Return image_url)
# ---------------------------------------------------------
$listPetsContent = @'
<?php
header('Content-Type: application/json');
error_reporting(0);
ini_set('display_errors', 0);

require '../utils/db.php';

if (isset($_GET['user_id'])) {
    $user_id = $_GET['user_id'];
    
    $stmt = $conn->prepare("SELECT * FROM pets WHERE user_id = ?");
    $stmt->bind_param("i", $user_id);
    $stmt->execute();
    $result = $stmt->get_result();

    $pets = array();
    while ($row = $result->fetch_assoc()) {
        // Ensure photo field is present (aliasing image_url)
        if (isset($row['image_url']) && !empty($row['image_url'])) {
            $row['photo'] = $row['image_url'];
        } else {
            $row['photo'] = null;
        }
        $pets[] = $row;
    }

    echo json_encode([
        "status" => "success",
        "pets" => $pets
    ]);

    $stmt->close();
} else {
    echo json_encode([
        "status" => "error",
        "message" => "Missing user_id"
    ]);
}

$conn->close();
?>
'@
Set-Content -Path "$apiRoot\pets\list_pets.php" -Value $listPetsContent -Encoding UTF8
Write-Host "✅ Fixed: pets/list_pets.php" -ForegroundColor Green

Write-Host "---------------------------------------------------------"
Write-Host "🎉 BACKEND V3 APPLIED (STRICT MODE)!" -ForegroundColor Cyan
Write-Host "Please restart XAMPP Apache to ensure no locked files." -ForegroundColor Cyan
