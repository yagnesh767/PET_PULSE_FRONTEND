<?php
header('Content-Type: application/json');
require '../utils/db.php';

$input = json_decode(file_get_contents('php://input'), true);
$email = $input['email'] ?? '';

if (empty($email)) {
    echo json_encode(['status' => 'error', 'message' => 'Email required']);
    exit;
}

$stmt = $conn->prepare("SELECT user_id FROM users WHERE email = ?");
$stmt->bind_param("s", $email);
$stmt->execute();
if ($stmt->get_result()->num_rows > 0) {
    $otp = (string) rand(100000, 999999); // Generate random OTP
    $update = $conn->prepare("UPDATE users SET verification_code = ? WHERE email = ?");
    $update->bind_param("ss", $otp, $email);
    $update->execute();
    
    require_once '../utils/send_email.php';
    $mailResult = sendOTP($email, $otp);
    
    echo json_encode([
        'status' => 'success', 
        'message' => 'OTP sent to email',
        'debug_otp' => $otp,
        'mail_status' => $mailResult['message']
    ]);
} else {
    echo json_encode(['status' => 'error', 'message' => 'Email not found']);
}
?>
