<?php
header('Content-Type: application/json');
require '../utils/db.php';

$input = json_decode(file_get_contents('php://input'), true);
$email = $input['email'] ?? '';
$otp = $input['otp'] ?? '';
$new_password = $input['new_password'] ?? '';

if (empty($email) || empty($otp) || empty($new_password)) {
    echo json_encode(['status' => 'error', 'message' => 'All fields required']);
    exit;
}

// Verify OTP again just in case
$stmt = $conn->prepare("SELECT user_id FROM users WHERE email = ? AND otp = ?");
$stmt->bind_param("ss", $email, $otp);
$stmt->execute();

if ($stmt->get_result()->num_rows > 0) {
    $hashed = password_hash($new_password, PASSWORD_DEFAULT);
    $update = $conn->prepare("UPDATE users SET password = ?, otp = NULL WHERE email = ?");
    $update->bind_param("ss", $hashed, $email);
    
    if ($update->execute()) {
        echo json_encode(['status' => 'success', 'message' => 'Password reset successfully']);
    } else {
        echo json_encode(['status' => 'error', 'message' => 'Update failed']);
    }
} else {
    echo json_encode(['status' => 'error', 'message' => 'Invalid OTP or Email']);
}
?>
