<?php
header('Content-Type: application/json');
require '../utils/db.php';

$user_id = $_GET['user_id'] ?? '';

if (empty($user_id)) {
    echo json_encode(['status' => 'error', 'message' => 'User ID required']);
    exit;
}

$stmt = $conn->prepare("SELECT id, pet_name as name, species, breed, gender, birth_date as age, weight, image_url FROM pets WHERE user_id = ? ORDER BY created_at DESC");
$stmt->bind_param("i", $user_id);
$stmt->execute();
$result = $stmt->get_result();

$pets = [];
while ($row = $result->fetch_assoc()) {
    $pets[] = $row;
}

echo json_encode(['status' => 'success', 'data' => $pets]);
?>
