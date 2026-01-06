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

$stmt = $conn->prepare("SELECT id, verification_code FROM users WHERE email = ?");
$stmt->bind_param("s", $email);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows > 0) {
    $user = $result->fetch_assoc();
    
    if ($user['verification_code'] === $otp) {
        // Verify user
        $updateStmt = $conn->prepare("UPDATE users SET is_verified = 1, verification_code = NULL WHERE email = ?");
        $updateStmt->bind_param("s", $email);
        
        if ($updateStmt->execute()) {
            echo json_encode(['status' => 'success', 'message' => 'Email verified successfully']);
        } else {
            echo json_encode(['status' => 'error', 'message' => 'Update failed']);
        }
    } else {
        echo json_encode(['status' => 'error', 'message' => 'Invalid OTP']);
    }
} else {
    echo json_encode(['status' => 'error', 'message' => 'User not found']);
}
?>
