<?php
header('Content-Type: application/json');
require '../utils/db.php';

$input = json_decode(file_get_contents('php://input'), true);

$email = $input['email'] ?? '';
$password = $input['password'] ?? '';

if (empty($email) || empty($password)) {
    echo json_encode(['status' => 'error', 'message' => 'Email and password required']);
    exit;
}

$stmt = $conn->prepare("SELECT id, full_name, password, is_verified, is_premium FROM users WHERE email = ?");
$stmt->bind_param("s", $email);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows > 0) {
    $user = $result->fetch_assoc();
    
    // Verify password
    if (password_verify($password, $user['password'])) {
        
        if ($user['is_verified'] == 0) {
             echo json_encode(['status' => 'error', 'message' => 'Account exists but not verified']);
             exit;
        }

        // Remove password from response
        unset($user['password']);
        
        echo json_encode([
            'status' => 'success',
            'message' => 'Login successful',
            'data' => $user
        ]);
    } else {
        echo json_encode(['status' => 'error', 'message' => 'Invalid password']);
    }
} else {
    echo json_encode(['status' => 'error', 'message' => 'User not found']);
}
?>
