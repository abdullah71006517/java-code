package com.example.dresscollection;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private TextField dressName;
    @FXML
    private Label dressNameError;

    @FXML
    private ChoiceBox<String> dressTypeChoiceBox;
    @FXML
    private Label dressTypeError;

    @FXML
    private ComboBox<String> sizeComboBox;

    @FXML
    private ColorPicker dressColor;
    @FXML
    private Label dressColorError;

    @FXML
    private Slider priceRange;
    @FXML
    private Label priceError;

    @FXML
    private TextArea dressDetails;
    @FXML
    private Label dressDetailsError;

    @FXML
    private DatePicker purchaseDate;
    @FXML
    private Label purchaseDateError;

    @FXML
    private Spinner<Integer> availableQuantity;
    @FXML
    private Label availableQuantityError;

    @FXML
    private PasswordField discountCode;

    @FXML
    private RadioButton maleRadio, femaleRadio;
    @FXML
    private ToggleGroup gender;

    @FXML
    private CheckBox facebookBoost;

    @FXML
    private ImageView dressImage;
    @FXML
    private File selectedImageFile;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dressTypeChoiceBox.setItems(FXCollections.observableArrayList("Hoodie", "Sweater", "Jacket"));
        sizeComboBox.setItems(FXCollections.observableArrayList("Small", "Medium", "Large", "XL"));

        SpinnerValueFactory<Integer> quantityFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 1);
        availableQuantity.setValueFactory(quantityFactory);
    }

    @FXML
    private void handleUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Dress Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(new Stage());

        if (file != null) {
            try {
                File destDir = new File("src/main/resources/images");
                if (!destDir.exists()) destDir.mkdirs();

                File destFile = new File(destDir, file.getName());
                Files.copy(file.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                selectedImageFile = new File("images/" + file.getName());

                dressImage.setImage(new Image(destFile.toURI().toString()));
                dressColorError.setVisible(false);
            } catch (IOException e) {
                e.printStackTrace();
                dressColorError.setText("Failed to upload image");
                dressColorError.setVisible(true);
            }
        } else {
            dressColorError.setText("Image is required");
            dressColorError.setVisible(true);
        }
    }

    @FXML
    private void nextpage() {
        HelloApplication.changeScene("2nd.fxml");
    }

    @FXML
    private void handleSave() {
        boolean valid = true;

        if (dressName.getText().isEmpty()) {
            dressNameError.setVisible(true);
            valid = false;
        } else {
            dressNameError.setVisible(false);
        }

        if (dressTypeChoiceBox.getValue() == null) {
            dressTypeError.setVisible(true);
            valid = false;
        } else {
            dressTypeError.setVisible(false);
        }

        if (dressColor.getValue() == null) {
            dressColorError.setVisible(true);
            valid = false;
        } else {
            dressColorError.setVisible(false);
        }

        if (priceRange.getValue() < 500) {
            priceError.setVisible(true);
            valid = false;
        } else {
            priceError.setVisible(false);
        }

        if (dressDetails.getText().length() > 50) {
            dressDetailsError.setVisible(true);
            valid = false;
        } else {
            dressDetailsError.setVisible(false);
        }

        if (purchaseDate.getValue() != null && purchaseDate.getValue().isAfter(LocalDate.now())) {
            purchaseDateError.setVisible(true);
            valid = false;
        } else {
            purchaseDateError.setVisible(false);
        }

        if (availableQuantity.getValue() < 0) {
            availableQuantityError.setVisible(true);
            valid = false;
        } else {
            availableQuantityError.setVisible(false);
        }

        if (valid) {
            String name = dressName.getText();
            String type = dressTypeChoiceBox.getValue();
            String size = sizeComboBox.getValue() != null ? sizeComboBox.getValue() : "N/A";
            String color = dressColor.getValue() != null ? dressColor.getValue().toString() : "N/A";
            double price = priceRange.getValue();
            String details = dressDetails.getText();
            String genderText = gender.getSelectedToggle() != null ? ((RadioButton) gender.getSelectedToggle()).getText() : "N/A";
            int quantity = availableQuantity.getValue();
            String date = purchaseDate.getValue() != null ? purchaseDate.getValue().toString() : "N/A";
            String discount = discountCode.getText();

            String imagePath = selectedImageFile != null ? selectedImageFile.getPath() : "N/A";
            String facebookBoostStatus = facebookBoost.isSelected() ? "Enabled" : "Not Enabled";

            String line = name + "," + type + "," + size + "," + color + "," + price + "," + details + "," + genderText + "," + quantity + "," + date + "," + discount + "," + facebookBoostStatus + "," + imagePath + "\n";

            try (RandomAccessFile file = new RandomAccessFile("dresses.txt", "rw")) {
                file.seek(file.length());
                file.writeBytes(line);
                System.out.println("Dress saved successfully.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
