<?php
header('Content-Type: application/json');
require '../utils/db.php';

$user_id = $_POST['user_id'] ?? '';
$name = $_POST['pet_name'] ?? '';
$species = $_POST['species'] ?? '';
$breed = $_POST['breed'] ?? '';
$gender = $_POST['gender'] ?? '';
$birth_date = $_POST['birth_date'] ?? ''; // App must now send birth_date (YYYY-MM-DD)
$weight = $_POST['weight'] ?? '';

if (empty($user_id) || empty($name)) {
    echo json_encode(['status' => 'error', 'message' => 'User ID and Pet Name required']);
    exit;
}

$image_url = null;
if (!empty($_FILES['image']['name'])) {
    $target_dir = "../uploads/pets/";
    if (!is_dir($target_dir)) mkdir($target_dir, 0777, true);
    
    $file_name = time() . '_' . basename($_FILES['image']['name']);
    $target_file = $target_dir . $file_name;
    
    if (move_uploaded_file($_FILES['image']['tmp_name'], $target_file)) {
        $image_url = "uploads/pets/" . $file_name;
    }
}

$stmt = $conn->prepare("INSERT INTO pets (user_id, pet_name, species, breed, gender, birth_date, weight, image_url) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
$stmt->bind_param("isssssds", $user_id, $name, $species, $breed, $gender, $birth_date, $weight, $image_url);

if ($stmt->execute()) {
    echo json_encode(['status' => 'success', 'message' => 'Pet added successfully', 'pet_id' => $stmt->insert_id]);
} else {
    echo json_encode(['status' => 'error', 'message' => 'Failed to add pet: ' . $stmt->error]);
}
?>
