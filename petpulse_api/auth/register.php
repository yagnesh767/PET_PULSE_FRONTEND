<?php
header('Content-Type: application/json');
require '../utils/db.php';

$input = json_decode(file_get_contents('php://input'), true);

$full_name = $input['full_name'] ?? '';
$email = $input['email'] ?? '';
$password = $input['password'] ?? '';

if (empty($full_name) || empty($email) || empty($password)) {
    echo json_encode(['status' => 'error', 'message' => 'All fields are required']);
    exit;
}

// Check if email already exists
$checkStmt = $conn->prepare("SELECT id, is_verified FROM users WHERE email = ?");
$checkStmt->bind_param("s", $email);
$checkStmt->execute();
$checkResult = $checkStmt->get_result();

if ($checkResult->num_rows > 0) {
    $existingUser = $checkResult->fetch_assoc();
    if ($existingUser['is_verified'] == 1) {
        echo json_encode(['status' => 'error', 'message' => 'Email already registered']);
        exit;
    } else {
        // User exists but NOT verified. 
        // Update password and generate new OTP to allow them to complete verification.
        $hashed_password = password_hash($password, PASSWORD_DEFAULT);
        $otp = (string) rand(100000, 999999);
        
        $updateStmt = $conn->prepare("UPDATE users SET full_name = ?, password = ?, verification_code = ? WHERE email = ?");
        $updateStmt->bind_param("ssss", $full_name, $hashed_password, $otp, $email);
        
        if ($updateStmt->execute()) {
             // Try to send email
             require_once '../utils/send_email.php';
             $mailResult = sendOTP($email, $otp);
             
             echo json_encode([
                'status' => 'success', 
                'message' => 'OTP sent to email. Please verify to continue.',
                'debug_otp' => $otp,
                'mail_status' => $mailResult['message']
            ]);
            exit;
        } else {
             echo json_encode(['status' => 'error', 'message' => 'Update failed: ' . $updateStmt->error]);
             exit;
        }
    }
}

// Hash password
$hashed_password = password_hash($password, PASSWORD_DEFAULT);
$otp = (string) rand(100000, 999999); // Generate random 6-digit OTP

// Insert new user
// Note: Using 'verification_code' as per user database schema
$stmt = $conn->prepare("INSERT INTO users (full_name, email, password, verification_code, is_verified) VALUES (?, ?, ?, ?, 0)");
$stmt->bind_param("ssss", $full_name, $email, $hashed_password, $otp);

if ($stmt->execute()) {
    // Try to send email
    require_once '../utils/send_email.php';
    $mailResult = sendOTP($email, $otp);

    echo json_encode([
        'status' => 'success', 
        'message' => 'OTP sent to email. Please verify to continue.',
        'debug_otp' => $otp, // Expose OTP for testing purposes
        'mail_status' => $mailResult['message']
    ]);
} else {
    echo json_encode(['status' => 'error', 'message' => 'Registration failed: ' . $stmt->error]);
}
?>
