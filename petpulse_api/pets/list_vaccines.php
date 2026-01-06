<?php
header('Content-Type: application/json');
require '../utils/db.php';

$pet_id = $_GET['pet_id'] ?? null;

if (!$pet_id) {
    echo json_encode([
        "status" => "error",
        "message" => "pet_id required"
    ]);
    exit;
}

// Updated to use 'vaccines' table (canonical)
// Mapping columns to match Android Vaccine model:
// id, pet_id, vaccine_name, date_administered, next_due_date, vet_name
$stmt = $conn->prepare("
    SELECT vaccine_id as id, pet_id, vaccine_name, date_administered, next_due_date, status, file_path
    FROM vaccines
    WHERE pet_id = ?
    ORDER BY date_administered DESC
");

$stmt->bind_param("i", $pet_id);
if (!$stmt->execute()) {
    echo json_encode(["status" => "error", "message" => "Query failed: " . $stmt->error]);
    exit;
}

$result = $stmt->get_result();

$records = [];
while ($row = $result->fetch_assoc()) {
    // Android model expects 'vet_name' but table doesn't have it.
    // We can just add it as null or ignore if nullable.
    $row['vet_name'] = null; 
    $records[] = $row;
}

echo json_encode([
    "status" => "success",
    "vaccines" => $records
]);

$stmt->close();
$conn->close();
?>
