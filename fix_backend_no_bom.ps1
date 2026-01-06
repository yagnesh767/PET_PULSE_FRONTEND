$apiRoot = "C:\xampp\htdocs\petpulse_api"
$utf8NoBom = New-Object System.Text.UTF8Encoding $false

Write-Host "Applying NO-BOM Fix to PetPulse Backend at $apiRoot..." -ForegroundColor Cyan

# ---------------------------------------------------------
# 1. FIXED db.php (NO BOM)
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
[System.IO.File]::WriteAllText("$apiRoot\utils\db.php", $dbPhpContent, $utf8NoBom)
Write-Host "✅ Fixed: utils/db.php (No BOM)" -ForegroundColor Green

# ---------------------------------------------------------
# 2. FIXED add_pet.php (NO BOM)
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
[System.IO.File]::WriteAllText("$apiRoot\pets\add_pet.php", $addPetContent, $utf8NoBom)
Write-Host "✅ Fixed: pets/add_pet.php (No BOM)" -ForegroundColor Green

# ---------------------------------------------------------
# 3. FIXED list_pets.php (NO BOM)
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
[System.IO.File]::WriteAllText("$apiRoot\pets\list_pets.php", $listPetsContent, $utf8NoBom)
Write-Host "✅ Fixed: pets/list_pets.php (No BOM)" -ForegroundColor Green

Write-Host "🎉 BACKEND FIXED (NO BOM)! Restart Apache." -ForegroundColor Cyan
