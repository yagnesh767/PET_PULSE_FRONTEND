<?php
$baseUrl = 'http://localhost/petpulse_api/auth/';

function callApi($endpoint, $data) {
    global $baseUrl;
    $url = $baseUrl . $endpoint;
    $ch = curl_init($url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_POST, true);
    curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($data));
    curl_setopt($ch, CURLOPT_HTTPHEADER, ['Content-Type: application/json']);
    $response = curl_exec($ch);
    
    if (curl_errno($ch)) {
        echo 'Curl error: ' . curl_error($ch) . "\n";
    }
    curl_close($ch);
    
    echo "Raw Response for $endpoint: " . $response . "\n";
    return json_decode($response, true);
}

// 1. Register
echo "1. Testing Registration...\n";
$email = "test_" . time() . "@example.com";
$password = "password123";
$res = callApi('register.php', [
    'full_name' => 'Test User',
    'email' => $email,
    'password' => $password
]);
print_r($res);

if (($res['status'] ?? '') !== 'success') {
    die("Registration failed.\n");
}

// 2. Verify OTP
echo "\n2. Testing OTP Verification...\n";
$res = callApi('verify_otp.php', [
    'email' => $email,
    'otp' => '123456'
]);
print_r($res);

if (($res['status'] ?? '') !== 'success') {
    die("OTP Verification failed.\n");
}

// 3. Login
echo "\n3. Testing Login...\n";
$res = callApi('login.php', [
    'email' => $email,
    'password' => $password
]);
print_r($res);

if (($res['status'] ?? '') === 'success') {
    echo "\nSUCCESS: Full Auth Flow working for $email\n";
} else {
    echo "\nFAILURE: Login failed.\n";
}
?>
