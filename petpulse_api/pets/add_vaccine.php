<?php
header('Content-Type: application/json');
require '../utils/db.php';

// Check if request method is POST
if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    echo json_encode(["status" => "error", "message" => "Invalid request method"]);
    exit;
}

// Get parameters
$pet_id = $_POST['pet_id'] ?? null;
$title = $_POST['title'] ?? null;
$record_date = $_POST['record_date'] ?? null;
$status = $_POST['status'] ?? null;
$next_due = $_POST['next_due'] ?? null;

// Validate required fields
if (!$pet_id || !$title || !$record_date) {
    echo json_encode([
        "status" => "error", 
        "message" => "Missing required fields (pet_id, title, record_date)"
    ]);
    exit;
}

$file_path = null;
if (!empty($_FILES['file']['name'])) {
    $dir = "../uploads/vaccines/";
    if (!is_dir($dir)) mkdir($dir, 0777, true);
    $filename = time() . "_" . basename($_FILES['file']['name']);
    if (move_uploaded_file($_FILES['file']['tmp_name'], $dir . $filename)) {
        $file_path = "uploads/vaccines/" . $filename;
    }
}

// Prepare statement - Using 'vaccines' table
$stmt = $conn->prepare("INSERT INTO vaccines (pet_id, vaccine_name, date_administered, status, next_due_date, file_path) VALUES (?, ?, ?, ?, ?, ?)");
if (!$stmt) {
    echo json_encode(["status" => "error", "message" => "Prepare failed: " . $conn->error]);
    exit;
}

$stmt->bind_param("isssss", $pet_id, $title, $record_date, $status, $next_due, $file_path);

if ($stmt->execute()) {
    echo json_encode(["status" => "success", "message" => "Vaccine added successfully"]);
} else {
    echo json_encode(["status" => "error", "message" => "Database error: " . $stmt->error]);
}

$stmt->close();
$conn->close();
?>
