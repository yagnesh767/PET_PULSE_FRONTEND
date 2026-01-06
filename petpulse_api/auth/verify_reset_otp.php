<?php
header('Content-Type: application/json');
require '../utils/db.php';

$input = json_decode(file_get_contents('php://input'), true);
$email = $input['email'] ?? '';
$otp = $input['otp'] ?? '';

if (empty($email) || empty($otp)) {
    echo json_encode(['status' => 'error', 'message' => 'Email and OTP required']);
    exit;
}

$stmt = $conn->prepare("SELECT user_id FROM users WHERE email = ? AND verification_code = ?");
$stmt->bind_param("ss", $email, $otp);
$stmt->execute();

if ($stmt->get_result()->num_rows > 0) {
    echo json_encode(['status' => 'success', 'message' => 'OTP verified']);
} else {
    echo json_encode(['status' => 'error', 'message' => 'Invalid OTP']);
}
?>
