<?php
header('Content-Type: application/json');
require '../utils/db.php';

if (!isset($_GET['pet_id'])) {
    echo json_encode(['status' => 'error', 'message' => 'Pet ID required']);
    exit;
}

$pet_id = $_GET['pet_id'];

// Get basic pet info (weight)
$stmt = $conn->prepare("SELECT weight, age, breed FROM pets WHERE id = ?");
$stmt->bind_param("i", $pet_id);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows > 0) {
    $pet = $result->fetch_assoc();
    
    // Calculate Health Score (Mock logic for now)
    // In a real app, this could be based on vaccine compliance + activity levels
    $health_score = 92; // Default high score
    
    // Check vaccines to adjust score?
    $vacStmt = $conn->prepare("SELECT COUNT(*) as count FROM vaccines WHERE pet_id = ?");
    $vacStmt->bind_param("i", $pet_id);
    $vacStmt->execute();
    $vacResult = $vacStmt->get_result()->fetch_assoc();
    
    if ($vacResult['count'] > 0) {
        $health_score = 95;
    } else {
        $health_score = 80;
    }

    echo json_encode([
        'status' => 'success',
        'weight' => $pet['weight'] ?? 'N/A',
        'health_score' => (string)$health_score,
        'pet_id' => $pet_id
    ]);
} else {
    echo json_encode(['status' => 'error', 'message' => 'Pet not found']);
}
?>
