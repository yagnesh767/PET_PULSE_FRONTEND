$apiRoot = "C:\xampp\htdocs\petpulse_api"

Write-Host "Applying V2 fixes (Photo Uploads + Pet ID Return) to PetPulse Backend at $apiRoot..." -ForegroundColor Cyan

# Ensure directories exist
New-Item -ItemType Directory -Force -Path "$apiRoot\utils" | Out-Null
New-Item -ItemType Directory -Force -Path "$apiRoot\pets" | Out-Null
New-Item -ItemType Directory -Force -Path "$apiRoot\uploads" | Out-Null

# ---------------------------------------------------------
# 1. FIXED db.php
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
# 2. FIXED add_pet.php (With Image Upload AND Pet Object Return)
# ---------------------------------------------------------
$addPetContent = @'
<?php
header('Content-Type: application/json');
error_reporting(0);
ini_set('display_errors', 0);

require '../utils/db.php';

$response = array();

// Helper to get input (Multipart or JSON)
$user_id = $_POST['user_id'] ?? null;
$name = $_POST['name'] ?? null;
$species = $_POST['species'] ?? 'Unknown';
$breed = $_POST['breed'] ?? 'Unknown';
$age = $_POST['age'] ?? 0;
$weight = $_POST['weight'] ?? 0.0;
$gender = $_POST['gender'] ?? 'Unknown';

// Handle JSON fallback if not POST/Multipart (just in case)
if (!$user_id) {
    $input = json_decode(file_get_contents('php://input'), true);
    if ($input) {
        $user_id = $input['user_id'] ?? null;
        $name    = $input['name'] ?? null;
        $species = $input['species'] ?? 'Unknown';
        $breed   = $input['breed'] ?? 'Unknown';
        $age     = $input['age'] ?? 0;
        $weight  = $input['weight'] ?? 0.0;
        $gender  = $input['gender'] ?? 'Unknown';
    }
}

if ($user_id && $name) {
    
    // Handle Image Upload
    $image_url = "";
    if (isset($_FILES['image']) && $_FILES['image']['error'] === UPLOAD_ERR_OK) {
        $uploadDir = '../uploads/';
        if (!is_dir($uploadDir)) {
            mkdir($uploadDir, 0777, true);
        }
        
        $fileName = time() . '_' . basename($_FILES['image']['name']);
        $targetPath = $uploadDir . $fileName;
        
        if (move_uploaded_file($_FILES['image']['tmp_name'], $targetPath)) {
            $image_url = "uploads/" . $fileName;
        }
    }

    // Ensure column exists


    $stmt = $conn->prepare("INSERT INTO pets (user_id, name, species, breed, age, weight, gender, image_url) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
    $stmt->bind_param("isssidss", $user_id, $name, $species, $breed, $age, $weight, $gender, $image_url);

    if ($stmt->execute()) {
        $pet_id = $stmt->insert_id;
        
        $response = [
            'status'   => 'success',
            'message'  => 'Pet added successfully',
            'pet' => [
                'pet_id'  => $pet_id,
                'name'    => $name,
                'species' => $species,
                'breed'   => $breed,
                'age'     => (int)$age,
                'weight'  => (float)$weight,
                'gender'  => $gender,
                'photo'   => $image_url,
                'is_default' => 1
            ]
        ];
    } else {
        $response['status'] = 'error';
        $response['message'] = 'Database insertion failed: ' . $stmt->error;
    }
    $stmt->close();
} else {
    $response['status'] = 'error';
    $response['message'] = 'Missing required fields';
}

$conn->close();
echo json_encode($response);
?>
'@
Set-Content -Path "$apiRoot\pets\add_pet.php" -Value $addPetContent -Encoding UTF8
Write-Host "✅ Fixed: pets/add_pet.php" -ForegroundColor Green

# ---------------------------------------------------------
# 3. FIXED list_pets.php (With Image URL)
# ---------------------------------------------------------
$listPetsContent = @'
<?php
header('Content-Type: application/json');
error_reporting(0);
ini_set('display_errors', 0);

require '../utils/db.php';

$response = array();

if (isset($_GET['user_id'])) {
    $user_id = $_GET['user_id'];

    // Ensure column exists just in case
 
    
    $stmt = $conn->prepare("SELECT * FROM pets WHERE user_id = ?");
    $stmt->bind_param("i", $user_id);
    $stmt->execute();
    $result = $stmt->get_result();

    $pets = array();
    while ($row = $result->fetch_assoc()) {
        $pets[] = $row;
    }

    $response['status'] = 'success';
    $response['pets'] = $pets;

    $stmt->close();
} else {
    $response['status'] = 'error';
    $response['message'] = 'Missing user_id';
    $response['pets'] = [];
}

$conn->close();
echo json_encode($response);
?>
'@
Set-Content -Path "$apiRoot\pets\list_pets.php" -Value $listPetsContent -Encoding UTF8
Write-Host "✅ Fixed: pets/list_pets.php" -ForegroundColor Green

Write-Host "---------------------------------------------------------"
Write-Host "🎉 BACKEND V2 APPLIED (Photo + Navigation Fix)!" -ForegroundColor Cyan
Write-Host "Please restart XAMPP Apache." -ForegroundColor Cyan
