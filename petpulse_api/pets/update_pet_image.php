<?php
header('Content-Type: application/json');
require '../utils/db.php';

$pet_id = $_POST['pet_id'] ?? '';

if (empty($pet_id) || empty($_FILES['image']['name'])) {
    echo json_encode(['status' => 'error', 'message' => 'Pet ID and Image required']);
    exit;
}

$target_dir = "../uploads/pets/";
if (!is_dir($target_dir)) mkdir($target_dir, 0777, true);

$file_name = time() . '_' . basename($_FILES['image']['name']);
$target_file = $target_dir . $file_name;

if (move_uploaded_file($_FILES['image']['tmp_name'], $target_file)) {
    $image_url = "uploads/pets/" . $file_name;
    
    $stmt = $conn->prepare("UPDATE pets SET image_url = ? WHERE pet_id = ?");
    $stmt->bind_param("si", $image_url, $pet_id);
    
    if ($stmt->execute()) {
        echo json_encode(['status' => 'success', 'message' => 'Image updated', 'image_url' => $image_url]);
    } else {
        echo json_encode(['status' => 'error', 'message' => 'Database update failed']);
    }
} else {
    echo json_encode(['status' => 'error', 'message' => 'File upload failed']);
}
?>
