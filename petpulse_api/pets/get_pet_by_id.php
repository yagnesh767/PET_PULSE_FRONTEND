<?php
header('Content-Type: application/json');
require '../utils/db.php';

$pet_id = $_GET['pet_id'] ?? '';

if (empty($pet_id)) {
    echo json_encode(['status' => 'error', 'message' => 'Pet ID required']);
    exit;
}

$stmt = $conn->prepare("SELECT * FROM pets WHERE pet_id = ?");
$stmt->bind_param("i", $pet_id);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows > 0) {
    echo json_encode($result->fetch_assoc()); // Direct object return as per common Retrofit pattern, or verify if wrapper needed.
    // ApiService calls this returns ResponseBody, so raw JSON is fine.
} else {
    echo json_encode(['status' => 'error', 'message' => 'Pet not found']);
}
?>
