<?php
header('Content-Type: application/json');
require '../utils/db.php';

$input = json_decode(file_get_contents('php://input'), true);
$email = $input['email'] ?? '';

if (empty($email)) {
    echo json_encode(['status' => 'error', 'message' => 'Email required']);
    exit;
}

$otp = (string) rand(100000, 999999);
$stmt = $conn->prepare("UPDATE users SET verification_code = ? WHERE email = ?");
$stmt->bind_param("ss", $otp, $email);

if ($stmt->execute()) {
    require_once '../utils/send_email.php';
    $mailResult = sendOTP($email, $otp);

    echo json_encode([
        'status' => 'success', 
        'message' => 'OTP resent', 
        'debug_otp' => $otp,
        'mail_status' => $mailResult['message']
    ]);
} else {
    echo json_encode(['status' => 'error', 'message' => 'Failed to resend']);
}
?>
