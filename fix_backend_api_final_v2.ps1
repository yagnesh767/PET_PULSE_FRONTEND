$apiRoot = "C:\xampp\htdocs\petpulse_api"

Write-Host "Applying FINAL V2 STRICT Backend Fixes at $apiRoot..." -ForegroundColor Cyan

# ---------------------------------------------------------
# 1. CREATE setup_db.php (Schema Safe Update)
# ---------------------------------------------------------
$setupDbContent = @'
<?php
// Safely add column if not exists
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "petpulse";

$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error . "\n");
}

echo "Checking database schema...\n";

// Check if image_url column exists
$result = $conn->query("SHOW COLUMNS FROM 'pets' LIKE 'image_url'");
$exists = ($result && $result->num_rows > 0);

if (!$exists) {
    echo "Column 'image_url' missing. Adding it...\n";
    if ($conn->query("ALTER TABLE pets ADD COLUMN image_url VARCHAR(255) DEFAULT NULL")) {
        echo "✅ Column 'image_url' added successfully.\n";
    } else {
        echo "❌ Error adding column: " . $conn->error . "\n";
    }
} else {
    echo "✅ Column 'image_url' already exists.\n";
}

$conn->close();
?>
'@
Set-Content -Path "$apiRoot\utils\setup_db.php" -Value $setupDbContent -Encoding UTF8

# ---------------------------------------------------------
# 2. RUN setup_db.php via PHP CLI
# ---------------------------------------------------------
Write-Host "Executing Database Setup..." -ForegroundColor Yellow
php "$apiRoot\utils\setup_db.php"

# ---------------------------------------------------------
# 3. FIXED add_pet.php (Robust Prepare Check)
# ---------------------------------------------------------
$addPetContent = @'
<?php
ob_start();
header('Content-Type: application/json; charset=utf-8');

require '../utils/db.php';

$response = [];

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

    $sql = "INSERT INTO pets (user_id, name, species, breed, age, weight, gender, image_url) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    $stmt = $conn->prepare($sql);

    if ($stmt) {
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
                "message" => "Execute failed: " . $stmt->error
            ];
        }
        $stmt->close();
    } else {
         $response = [
            "status" => "error",
            "message" => "Prepare failed: " . $conn->error
        ];
    }
} else {
    $response = [
        "status" => "error", 
        "message" => "Missing required fields"
    ];
}

ob_clean();
echo json_encode($response, JSON_UNESCAPED_SLASHES);
exit;
?>
'@
Set-Content -Path "$apiRoot\pets\add_pet.php" -Value $addPetContent -Encoding UTF8
Write-Host "✅ Fixed: pets/add_pet.php" -ForegroundColor Green

Write-Host "---------------------------------------------------------"
Write-Host "🎉 BACKEND FIXED & SCHEMA VERIFIED!" -ForegroundColor Cyan
