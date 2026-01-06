<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);

$servername = "localhost";
$username = "root";
$password = "";
$dbname = "petpulse";

// 1. Check Server Connection
$conn = new mysqli($servername, $username, $password);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
echo "Connected successfully to MySQL server.\n";

// 2. Check Database Existence
if (!$conn->select_db($dbname)) {
    echo "Database '$dbname' does not exist.\n";
    exit(1);
}
echo "Database '$dbname' exists.\n";

// 3. Check Tables
$tables = ['users', 'pets', 'pet_vaccines', 'pet_medical_history'];
foreach ($tables as $table) {
    $result = $conn->query("SHOW TABLES LIKE '$table'");
    if ($result->num_rows > 0) {
        echo "Table '$table' exists.\n";
    } else {
        echo "Table '$table' MISSING.\n";
    }
}

// 4. Debug Users Table Schema
echo "Columns in 'users' table:\n";
$result = $conn->query("SHOW COLUMNS FROM users");
while ($row = $result->fetch_assoc()) echo $row['Field'] . "\n";

echo "\nColumns in 'pets' table:\n";
$result = $conn->query("SHOW COLUMNS FROM pets");
while ($row = $result->fetch_assoc()) echo $row['Field'] . " (" . $row['Type'] . ")\n";

$conn->close();
?>
