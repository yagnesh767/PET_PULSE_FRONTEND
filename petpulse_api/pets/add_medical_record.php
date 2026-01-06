<?php
header('Content-Type: application/json');
require '../utils/db.php'; // Kept 'utils/db.php' as per previous file structure, user content says '../db.php' but we must respect local structure

$pet_id = $_POST['pet_id'] ?? '';
$record_type = $_POST['record_type'] ?? '';
$title = $_POST['title'] ?? '';
$record_date = $_POST['record_date'] ?? '';
$veterinarian = $_POST['veterinarian'] ?? '';

if (!$pet_id || !$record_type || !$title || !$record_date) {
    echo json_encode([
        "status" => "error",
        "message" => "Missing required fields",
        "received" => $_POST
    ]);
    exit;
}

$file_path = null;
if (!empty($_FILES['file']['name'])) {
    $dir = "../uploads/medical/";
    if (!is_dir($dir)) mkdir($dir, 0777, true);
    $filename = time() . "_" . basename($_FILES['file']['name']);
    move_uploaded_file($_FILES['file']['tmp_name'], $dir . $filename);
    $file_path = "uploads/medical/" . $filename;
}

// Using $conn because previous files used $conn from db.php, not $pdo
$stmt = $conn->prepare("
    INSERT INTO pet_medical_history
    (pet_id, record_type, title, record_date, veterinarian, file_path)
    VALUES (?, ?, ?, ?, ?, ?)
");

$stmt->bind_param("isssss", $pet_id, $record_type, $title, $record_date, $veterinarian, $file_path);

if ($stmt->execute()) {
    echo json_encode([
        "status" => "success",
        "message" => "Record added successfully"
    ]);
} else {
    echo json_encode([
        "status" => "error",
        "message" => $stmt->error
    ]);
}
