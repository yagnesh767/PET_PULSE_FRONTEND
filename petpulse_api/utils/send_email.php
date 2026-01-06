<?php
use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

require_once 'mail_config.php';

function sendOTP($toEmail, $otp) {
    // Check if PHPMailer exists
    $phpMailerPath = __DIR__ . '/../libs/PHPMailer/src/PHPMailer.php';
    $smtpPath = __DIR__ . '/../libs/PHPMailer/src/SMTP.php';
    $exceptionPath = __DIR__ . '/../libs/PHPMailer/src/Exception.php';

    if (file_exists($phpMailerPath) && file_exists($smtpPath) && file_exists($exceptionPath)) {
        require_once $exceptionPath;
        require_once $phpMailerPath;
        require_once $smtpPath;

        $mail = new PHPMailer(true);

        try {
            // Server settings
            $mail->isSMTP();
            $mail->Host       = SMTP_HOST;
            $mail->SMTPAuth   = true;
            $mail->Username   = SMTP_USER;
            $mail->Password   = SMTP_PASS;
            $mail->SMTPSecure = PHPMailer::ENCRYPTION_STARTTLS;
            $mail->Port       = SMTP_PORT;

            // Recipients
            $mail->setFrom(SMTP_FROM_EMAIL, SMTP_FROM_NAME);
            $mail->addAddress($toEmail);

            // Content
            $mail->isHTML(true);
            $mail->Subject = 'Your PetPulse Verification Code';
            $mail->Body    = "
                <html>
                <head>
                  <title>Verification Code</title>
                </head>
                <body>
                  <h2>Welcome to PetPulse!</h2>
                  <p>Your verification code is: <strong>$otp</strong></p>
                  <p>This code will expire in 10 minutes.</p>
                </body>
                </html>
            ";
            $mail->AltBody = "Your verification code is: $otp";

            $mail->send();
            return ['status' => 'success', 'message' => 'OTP sent to email.'];
        } catch (Exception $e) {
            return ['status' => 'error', 'message' => "Message could not be sent. Mailer Error: {$mail->ErrorInfo}"];
        }
    } else {
        // PHPMailer not installed. Return debug info.
        return [
            'status' => 'success', // Treat as success for flow continuity if local
            'message' => 'OTP generated (PHPMailer not found). Please install PHPMailer or check debug_otp.',
            'debug_note' => 'PHPMailer lib missing in libs/PHPMailer'
        ];
    }
}
?>
