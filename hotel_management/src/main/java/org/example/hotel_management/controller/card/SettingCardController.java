package org.example.hotel_management.controller.card;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.shape.Circle;
import org.example.hotel_management.dto.request.ChangePasswordRequestDTO;
import org.example.hotel_management.dto.request.UpdateProfileRequestDTO;
import org.example.hotel_management.dto.response.UserResponseDTO;
import org.example.hotel_management.entity.UserSessionUtil;
import org.example.hotel_management.service.IUserService;
import org.example.hotel_management.service.impl.IUserServiceImpl;
import org.example.hotel_management.util.AlertUtil;
import org.example.hotel_management.util.TaskUtil;
import org.example.hotel_management.util.ValidatorUtil;

public class SettingCardController {

    @FXML private Circle circleAvatar;
    @FXML private Label lblDisplayName;
    @FXML private Label lblDisplayRole;
    @FXML private Label lblDisplayEmail;

    @FXML private TextField txtFullName;
    @FXML private TextField txtPhone;
    @FXML private TextField txtEmail;
    @FXML private Button btnSaveInfo;

    @FXML private PasswordField txtCurrentPass;
    @FXML private PasswordField txtNewPass;
    @FXML private PasswordField txtConfirmPass;
    @FXML private Button btnChangePass;

    private final IUserService userService = IUserServiceImpl.getInstance();
    private UserResponseDTO currentUser;

    public void initialize() {
        this.currentUser = UserSessionUtil.getInstance().getCurrentUser();

        if (currentUser != null) {
            fillData();
        }
    }

    private void fillData() {
        lblDisplayName.setText(currentUser.getFullName());
        lblDisplayRole.setText(currentUser.getRole().name());
        lblDisplayEmail.setText(currentUser.getEmail());

        // Fill vào form
        txtFullName.setText(currentUser.getFullName());
        txtPhone.setText(currentUser.getPhoneNumber());
        txtEmail.setText(currentUser.getEmail());

//        try {
//            // String imgUrl = getClass().getResource("/images/default_avatar.png").toExternalForm();
//            // circleAvatar.setFill(new ImagePattern(new Image(imgUrl)));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @FXML
    private void handleUpdateInfo() {
        boolean confirm = AlertUtil.showConfirmation("Confirm Update Profile", "Are you sure you want to update your profile?", null);
        if (!confirm) return;
        String newName = txtFullName.getText().trim();
        String newPhone = txtPhone.getText().trim();

        if (newName.isEmpty() || newPhone.isEmpty()) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Validation", "Name and Phone cannot be empty.", null);
            return;
        }

        UpdateProfileRequestDTO request = new UpdateProfileRequestDTO(currentUser.getId(), newName, newPhone);
        String errors = ValidatorUtil.validate(request);
        if (!errors.isEmpty()) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Validation", errors, null);
            return;
        }

        TaskUtil.run(
                btnSaveInfo,
                () -> userService.updateProfile(request),
                (updatedUser) -> {
                    if (updatedUser != null) {
                        // Cập nhật thành công -> Update Session & UI
                        UserSessionUtil.getInstance().setCurrentUser(updatedUser);
                        this.currentUser = updatedUser;
                        fillData(); // Refresh lại giao diện
                        AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Success", "Profile updated successfully!", null);
                    } else {
                        AlertUtil.showAlert(Alert.AlertType.ERROR,"Error", "Failed to update profile.", null);
                    }
                },
                (error) -> AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", error.getMessage(), null)
        );
    }

    @FXML
    private void handleChangePassword() {
        boolean confirm = AlertUtil.showConfirmation("Confirm Change Password", "Are you sure you want to change your password?", null);
        if (!confirm) return;
        String oldPass = txtCurrentPass.getText();
        String newPass = txtNewPass.getText();
        String confirmPass = txtConfirmPass.getText();

        if (oldPass.isEmpty() || newPass.isEmpty()) {
            AlertUtil.showAlert(Alert.AlertType.ERROR,"Validation", "Please fill all password fields.", null);
            return;
        }

        if (!newPass.equals(confirmPass)) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Validation", "New password and confirm password do not match.", null);
            return;
        }


        ChangePasswordRequestDTO request = new ChangePasswordRequestDTO(currentUser.getId(), oldPass, newPass);

        String errors = ValidatorUtil.validate(request);
        if (!errors.isEmpty()) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Validation", errors, null);
            return;
        }

        TaskUtil.run(
                btnChangePass,
                () -> userService.changePassword(request),
                (success) -> {
                    if (success) {
                        AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Success", "Password changed successfully!", null);
                        // Clear form
                        txtCurrentPass.clear();
                        txtNewPass.clear();
                        txtConfirmPass.clear();
                    } else {
                        AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", "Incorrect current password.", null);
                    }
                },
                (error) -> AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", error.getMessage(), null)
        );
    }
}