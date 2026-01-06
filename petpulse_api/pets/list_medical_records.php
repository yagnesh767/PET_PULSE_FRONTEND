<?php
header('Content-Type: application/json');
require '../utils/db.php';

$pet_id = $_GET['pet_id'] ?? null; // Changed to GET as per ApiService @GET

if (!$pet_id) {
    echo json_encode([
        "status" => "error",
        "message" => "pet_id required"
    ]);
    exit;
}

// Updated to match database_setup.sql schema
$stmt = $conn->prepare("
    SELECT record_id as id, pet_id, record_type, title, record_date,
           veterinarian, file_path, created_at
    FROM pet_medical_history
    WHERE pet_id = ?
    ORDER BY created_at DESC
");

$stmt->bind_param("i", $pet_id);
$stmt->execute();

$result = $stmt->get_result();

$records = [];
while ($row = $result->fetch_assoc()) {
    $records[] = $row;
}

echo json_encode([
    "status" => "success",
    "medical_records" => $records
]);

$stmt->close();
$conn->close();
?>
