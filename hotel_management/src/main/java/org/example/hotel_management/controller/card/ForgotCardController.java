package org.example.hotel_management.controller.card;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import org.example.hotel_management.dao.UserDAO;
import org.example.hotel_management.service.IMailSenderService;
import org.example.hotel_management.service.IOTPCodeService;
import org.example.hotel_management.service.IUserService;
import org.example.hotel_management.service.impl.IMailSenderServiceImpl;
import org.example.hotel_management.service.impl.IOTPCodeServiceImpl;
import org.example.hotel_management.service.impl.IUserServiceImpl;
import org.example.hotel_management.util.AlertUtil;
import org.example.hotel_management.util.TaskUtil;

import java.util.concurrent.CompletableFuture;

public class ForgotCardController {

    @FXML
    private Button btnReset;

    @FXML
    private Button btnSendOtp;

    @FXML
    private PasswordField txtConfirmPass;

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtNewPass;

    @FXML
    private TextField txtOtp;

    private IMailSenderService mailSenderService = IMailSenderServiceImpl.getInstance();
    private IOTPCodeService otpCodeService = IOTPCodeServiceImpl.getInstance();
    private IUserService userService = IUserServiceImpl.getInstance();
    private UserDAO userDAO = UserDAO.getInstance();
    private final String REGEX_OTP = "^[0-9]{6}$";
    private final String REGEX_PASSWORD = "^\\S{6,}$";
    private final int COOLDOWN_SECONDS = 300;
    private Runnable onSuccessCallback = null;

    public void initialize(){
        btnSendOtp.setOnAction(event -> handleSendOTP());
        btnReset.setOnAction(event -> handleResetPassword());

    }

    public void setOnSuccessCallback(Runnable callback) {
        this.onSuccessCallback = callback;
    }


    private void handleSendOTP() {
        String email = txtEmail.getText().trim();
        if (email.isEmpty()) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", "Please enter email first.", null);
            return;
        }

        if (userDAO.findByEmail(email).isEmpty()) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", "Email not found in system", null);
            return;
        }

        btnSendOtp.setDisable(true);
        btnSendOtp.setText("Sending...");

        mailSenderService.sendMail(email, "Hotel Management System OTP Code")
                .thenAccept(success -> {
                    javafx.application.Platform.runLater(() -> {
                        if (success) {
                            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Success", "OTP sent to " + email, null);
                            txtEmail.setDisable(true);
                            txtOtp.requestFocus();
                            startCountdown();
                        } else {
                            btnSendOtp.setDisable(false);
                            btnSendOtp.setText("Send OTP");
                        }
                    });
                })
                .exceptionally(ex -> {
                    javafx.application.Platform.runLater(() -> {
                        AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", "System error: " + ex.getMessage(), null);
                        btnSendOtp.setDisable(false);
                        btnSendOtp.setText("Send OTP");
                    });
                    return null;
                });
    }

    private void handleResetPassword() {
        String email = txtEmail.getText().trim();
        String otp = txtOtp.getText().trim();
        String newPassword = txtNewPass.getText().trim();
        String confirmPassword = txtConfirmPass.getText().trim();

        if (otp.isEmpty() || newPassword.isEmpty()) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", "Please fill all fields.", null);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", "Passwords do not match.", null);
            return;
        }

        if (otpCodeService.checkOTPCode(otp, email)) {
            if (!newPassword.matches(REGEX_PASSWORD) || !confirmPassword.matches(REGEX_PASSWORD)) {
                AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", "Password must be at least 6 characters long.", null);
                return;
            }
            if (!newPassword.equals(confirmPassword)) {
                AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", "Passwords do not match.", null);
                return;
            }
            System.out.println("Reset password successful: " + email + " - " + newPassword + " - " + confirmPassword + " -");
            if (userService.changePassword(email, newPassword, confirmPassword)) {
                AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Success", "Password reset successfully!", null);
                txtEmail.clear();
                txtConfirmPass.clear();
                txtNewPass.clear();
                txtOtp.clear();
            } else {
                AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", "Failed to reset password.", "Please try again later.");
            }
        }
    }

    private void startCountdown() {
        String originalText = "Send OTP";

        btnSendOtp.setDisable(true);

        final int[] timeSeconds = {COOLDOWN_SECONDS};

        Timeline timeline = new Timeline();
        timeline.setCycleCount(COOLDOWN_SECONDS + 1);

        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), event -> {
            timeSeconds[0]--;

            if (timeSeconds[0] > 0) {
                int minutes = timeSeconds[0] / 60;
                int seconds = timeSeconds[0] % 60;
                btnSendOtp.setText(String.format("Resend in %02d:%02d", minutes, seconds));
            } else {
                btnSendOtp.setText(originalText);
                btnSendOtp.setDisable(false);
                timeline.stop();
            }
        });

        timeline.getKeyFrames().add(keyFrame);
        timeline.playFromStart();
    }
}
