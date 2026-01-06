$apiRoot = "C:\xampp\htdocs\petpulse_api"

Write-Host "Applying fixes to PetPulse Backend at $apiRoot..." -ForegroundColor Cyan

# Ensure directories exist
New-Item -ItemType Directory -Force -Path "$apiRoot\utils" | Out-Null
New-Item -ItemType Directory -Force -Path "$apiRoot\pets" | Out-Null

# ---------------------------------------------------------
# 1. FIXED db.php (No 'Connected successfully' echo)
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
# 2. FIXED add_pet.php (JSON headers, input reading)
# ---------------------------------------------------------
$addPetContent = @'
<?php
header('Content-Type: application/json');
error_reporting(0);
ini_set('display_errors', 0);

require '../utils/db.php';

$response = array();

$input = json_decode(file_get_contents('php://input'), true);

if (isset($input['user_id']) && isset($input['name'])) {
    
    $user_id = $input['user_id'];
    $name    = $input['name'];
    $species = $input['species'] ?? 'Unknown';
    $breed   = $input['breed'] ?? 'Unknown';
    $age     = $input['age'] ?? 0;
    $weight  = $input['weight'] ?? 0.0;
    $gender  = $input['gender'] ?? 'Unknown';

    $stmt = $conn->prepare("INSERT INTO pets (user_id, name, species, breed, age, weight, gender) VALUES (?, ?, ?, ?, ?, ?, ?)");
    $stmt->bind_param("isssids", $user_id, $name, $species, $breed, $age, $weight, $gender);

    if ($stmt->execute()) {
        $response['status'] = 'success';
        $response['message'] = 'Pet added successfully';
    } else {
        $response['status'] = 'error';
        $response['message'] = 'Database insertion failed';
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
# 3. FIXED list_pets.php (JSON headers)
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

    $stmt = $conn->prepare("SELECT id, name, species, breed, age, weight, gender FROM pets WHERE user_id = ?");
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
Write-Host "🎉 ALL BACKEND FILES FIXED!" -ForegroundColor Cyan
Write-Host "Please restart XAMPP Apache to be sure, then run the app." -ForegroundColor Cyan
