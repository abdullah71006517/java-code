package com.example.dresscollection;

import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class NextSceneController implements Initializable {

    @FXML
    private Label Name, Price, Quality, Size, Type, bossting, color, date, details, discount,Customer;
    @FXML
    private TextField serachfiled;
    @FXML
    private TableView<Dress> tableview;
    @FXML
    private TableColumn<Dress,String> colorcolumn;
    @FXML
    private TableColumn<Dress, String> namecolumn;
    @FXML
    private TableColumn<Dress, String> typecolumn;
    @FXML
    private TableColumn<Dress, Double> pricecolumn;
    @FXML
    private TableColumn<Dress, Integer> quantitycolumn;
    @FXML
    private TableColumn<Dress, String> purchasecolumn;
    @FXML
    private TableColumn<Dress, String> boostedcolum;
    @FXML
    private ImageView imageload;

    private String currentDiscountCode;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadLastRecord();
        loadTableData();
        serachfiled.textProperty().addListener((observable, oldValue, newValue) -> filterTable(newValue));
    }

    @FXML
    void backScene(ActionEvent event) {
        HelloApplication.changeScene("hello-view.fxml");
    }

    @FXML
    void showDiscount() {
        discount.setText(currentDiscountCode);
    }
@FXML
void Editaction(){
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Coming Soon");
    alert.setHeaderText(null);

    alert.setContentText("This feature is coming soon.");

    alert.showAndWait();
}
    private void loadLastRecord() {
        try (RandomAccessFile file = new RandomAccessFile("dresses.txt", "r")) {
            String lastLine = null, line;
            while ((line = file.readLine()) != null) {
                lastLine = line;
            }

            if (lastLine != null) {
                String[] parts = lastLine.split(",", -1);
                if (parts.length < 12) return;

                updateUIWithDressData(parts);
            } else {
                Name.setText("No record found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateUIWithDressData(String[] parts) {
        Name.setText(parts[0]);
        Type.setText(parts[1]);
        Size.setText(parts[2]);
        color.setText(getColorName(parts[3].trim()));

        try {
            double priceValue = Double.parseDouble(parts[4].trim());
            DecimalFormat df = new DecimalFormat("0.00");
            Price.setText(df.format(priceValue));
        } catch (NumberFormatException e) {
            Price.setText("Invalid");
        }

        details.setText(parts[5]);
        Customer.setText(parts[6]);
        currentDiscountCode = parts[9];

        bossting.setText(parts[10]);

        try {
            int quantity = Integer.parseInt(parts[7].trim());
            Quality.setText(String.valueOf(quantity));
            Quality.setStyle(quantity < 10 ? "-fx-text-fill: red; -fx-font-weight: bold;" : "-fx-text-fill: black;");
        } catch (NumberFormatException e) {
            Quality.setText("Invalid");
        }

        date.setText(parts[8]);
        loadImage(parts[11].trim());
    }

    private void loadImage(String imagePath) {
        if (imagePath.equals("N/A")) {
            imageload.setImage(null);
            return;
        }



        File imageFile = new File(imagePath);

        // If not found, try relative path from resources
        if (!imageFile.exists()) {
            imageFile = new File("src/main/resources/" + imagePath);
        }

        if (imageFile.exists()) {
            imageload.setImage(new Image(imageFile.toURI().toString()));
        } else {
            System.out.println("Image file not found: " + imagePath);
            imageload.setImage(null);
        }
    }

    private void loadTableData() {
        ObservableList<Dress> data = FXCollections.observableArrayList();

        try (RandomAccessFile file = new RandomAccessFile("dresses.txt", "r")) {
            String line;
            while ((line = file.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length >= 12) {
                    data.add(new Dress(
                            parts[0], parts[1], parts[2], getColorName(parts[3].trim()),
                            Double.parseDouble(parts[4]), parts[5], parts[6],
                            Integer.parseInt(parts[7]), parts[8], parts[9], parts[10], parts[11])
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        namecolumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        typecolumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        pricecolumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantitycolumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        purchasecolumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        boostedcolum.setCellValueFactory(new PropertyValueFactory<>("boosting"));
        colorcolumn.setCellValueFactory(new PropertyValueFactory<>("color"));

        tableview.setItems(data);
    }

    private String getColorName(String codeOrName) {
        if (codeOrName.startsWith("0x") || codeOrName.startsWith("#")) {
            switch (codeOrName.toLowerCase()) {
                case "0xff0000ff": case "#ff0000": return "Red";
                case "0x00ff00ff": case "#00ff00": return "Green";
                case "0x0000ffff": case "#0000ff": return "Blue";
                case "0xffff00ff": case "#ffff00": return "Yellow";
                case "0x000000ff": case "#000000": return "Black";
                case "0xffffffff": case "#ffffff": return "White";
                case "0x800080ff": case "#800080": return "Purple";
                case "0xffa500ff": case "#ffa500": return "Orange";
                case "0x008080ff": return "Teal";
                case "0xff00ffff": return "Magenta";
                default: return codeOrName;
            }
        }
        return codeOrName;
    }


    private void filterTable(String searchText) {
        ObservableList<Dress> filteredList = FXCollections.observableArrayList();

        try (RandomAccessFile file = new RandomAccessFile("dresses.txt", "r")) {
            String line;
            while ((line = file.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length >= 12 && parts[0].toLowerCase().contains(searchText.toLowerCase())) {
                    filteredList.add(new Dress(
                            parts[0], parts[1], parts[2], getColorName(parts[3].trim()),
                            Double.parseDouble(parts[4]), parts[5], parts[6],
                            Integer.parseInt(parts[7]), parts[8], parts[9], parts[10], parts[11])
                    );
                }
            }

            tableview.setItems(filteredList);

            if (!filteredList.isEmpty()) {
                updateUIWithDressData(filteredList.get(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void updateUIWithDressData(Dress dress) {
        Name.setText(dress.getName());
        Type.setText(dress.getType());
        Size.setText(dress.getSize());
        color.setText(dress.getColor());

        DecimalFormat df = new DecimalFormat("0.00");
        Price.setText(df.format(dress.getPrice()));

        details.setText(dress.getDetails());
        currentDiscountCode = dress.getDiscountCode();
        Customer.setText(dress.getGender());
        bossting.setText(dress.getBoosting());

        Quality.setText(String.valueOf(dress.getQuantity()));
        Quality.setStyle(dress.getQuantity() < 10 ? "-fx-text-fill: red; -fx-font-weight: bold;" : "-fx-text-fill: black;");

        date.setText(dress.getDate());
        loadImage(dress.getImagePath());
    }

    
}