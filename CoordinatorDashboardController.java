package controllers;


import dao.FamilyDAO;
import dao.AidDistributionDAO;
import dao.OrganizationDAO;


import model.User;
import model.Family;
import model.AidDistribution;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.List;

public class CoordinatorDashboardController {

    private User currentUser;

    @FXML private Label totalFamiliesLabel;
    @FXML private Label familiesServedLabel;
    @FXML private Label familiesNotServedLabel;

    @FXML private TableView<Family> familyTable;
    @FXML private TableView<AidDistribution> distTable;

    // Family form fields
    @FXML private TextField familyNationalIdField;
    @FXML private TextField familyHeadNameField;
    @FXML private TextField familyPhoneField;
    @FXML private TextField familyAddressField;
    @FXML private ComboBox<String> familyVulnerabilityCombo;
    @FXML private DatePicker familyDatePicker;
    @FXML private TextField familyIdField;

    // Aid Distribution form fields
    @FXML private ComboBox<String> familyCombo;
    @FXML private ComboBox<String> aidTypeCombo;
    @FXML private DatePicker aidDatePicker;

    @FXML private Label duplicateAlertLabel;

    private FamilyDAO familyDAO = new FamilyDAO();
    private AidDistributionDAO distDAO = new AidDistributionDAO();
    private OrganizationDAO orgDAO = new OrganizationDAO();

    // ========== MENU HANDLERS ==========

    @FXML
    public void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/views/Login.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) totalFamiliesLabel.getScene().getWindow();
            Scene scene = new Scene(root);
            String cssPath = getClass().getResource("/resources/styles/style.css").toExternalForm();
            if (cssPath != null) {
                scene.getStylesheets().add(cssPath);
            }
            stage.setScene(scene);
            stage.setTitle("GHADS - Login");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not logout: " + e.getMessage());
        }
    }

    @FXML
    public void handleExit() {
        System.exit(0);
    }

    @FXML
    public void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About GHADS");
        alert.setHeaderText("Gaza Humanitarian Aid Distribution System");
        alert.setContentText("Version 1.0\nCreated by: Shahed Samir Mohammed\nID: 220231639");
        alert.showAndWait();
    }

    // ========== INITIALIZATION ==========

    public void setCurrentUser(User user) {
        this.currentUser = user;
        loadDashboardData();
        loadFamilyTable();
        loadDistributionTable();
        setupCombos();
    }

    private void setupCombos() {
        if (familyVulnerabilityCombo != null) {
            familyVulnerabilityCombo.getItems().clear();
            familyVulnerabilityCombo.getItems().addAll("HIGH", "MEDIUM", "LOW");
        }
        
        if (aidTypeCombo != null) {
            aidTypeCombo.getItems().clear();
            aidTypeCombo.getItems().addAll("FOOD", "CASH", "MEDICAL", "SHELTER");
        }
        
        loadFamilyCombo();
    }

    private void loadFamilyCombo() {
        if (familyCombo == null) return;
        List<Family> families = familyDAO.getAllFamilies();
        familyCombo.getItems().clear();
        for (Family f : families) {
            familyCombo.getItems().add(f.getFamilyId() + " - " + f.getHeadOfFamily());
        }
    }

    private void loadDashboardData() {
        List<Family> families = familyDAO.getAllFamilies();
        int totalFamilies = families.size();
        
        int served = 0;
        int notServed = 0;
        for (Family f : families) {
            if (f.isReceivedAid()) {
                served++;
            } else {
                notServed++;
            }
        }
        
        if (totalFamiliesLabel != null) totalFamiliesLabel.setText(String.valueOf(totalFamilies));
        if (familiesServedLabel != null) familiesServedLabel.setText(String.valueOf(served));
        if (familiesNotServedLabel != null) familiesNotServedLabel.setText(String.valueOf(notServed));
    }

    private void loadFamilyTable() {
        if (familyTable == null) return;
        ObservableList<Family> families = FXCollections.observableArrayList(familyDAO.getAllFamilies());
        familyTable.setItems(families);
        
        familyTable.getColumns().clear();
        TableColumn<Family, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("familyId"));
        TableColumn<Family, String> nameCol = new TableColumn<>("Head Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("headOfFamily"));
        TableColumn<Family, Integer> countCol = new TableColumn<>("Member Count");
        countCol.setCellValueFactory(new PropertyValueFactory<>("memberCount"));
        TableColumn<Family, String> regionCol = new TableColumn<>("Region");
        regionCol.setCellValueFactory(new PropertyValueFactory<>("region"));
        TableColumn<Family, Boolean> aidCol = new TableColumn<>("Received Aid");
        aidCol.setCellValueFactory(new PropertyValueFactory<>("receivedAid"));
        
        familyTable.getColumns().addAll(idCol, nameCol, countCol, regionCol, aidCol);
    }

    private void loadDistributionTable() {
        if (distTable == null) return;
        ObservableList<AidDistribution> dists = FXCollections.observableArrayList(distDAO.getAllDistributions());
        distTable.setItems(dists);
        
        distTable.getColumns().clear();
        TableColumn<AidDistribution, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("distId"));
        TableColumn<AidDistribution, String> familyCol = new TableColumn<>("Family");
        familyCol.setCellValueFactory(new PropertyValueFactory<>("familyName"));
        TableColumn<AidDistribution, String> orgCol = new TableColumn<>("Organization");
        orgCol.setCellValueFactory(new PropertyValueFactory<>("orgName"));
        TableColumn<AidDistribution, String> typeCol = new TableColumn<>("Aid Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("aidType"));
        TableColumn<AidDistribution, LocalDate> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("distributionDate"));
        
        distTable.getColumns().addAll(idCol, familyCol, orgCol, typeCol, dateCol);
    }

    // ========== FAMILY CRUD ==========

    @FXML
    public void addFamily() {
        Family family = new Family();
        family.setHeadOfFamily(familyHeadNameField.getText());
        
        try {
            family.setMemberCount(Integer.parseInt(familyNationalIdField.getText()));
        } catch (NumberFormatException e) {
            family.setMemberCount(1);
        }
        
        family.setRegion(familyAddressField.getText());
        family.setReceivedAid(false);
        
        if (familyDAO.addFamily(family)) {
            showAlert("Success", "Family added successfully");
            loadFamilyTable();
            loadFamilyCombo();
            loadDashboardData();
            clearFamilyForm();
        } else {
            showAlert("Error", "Failed to add family");
        }
    }

    @FXML
    public void updateFamily() {
        if (familyIdField.getText().isEmpty()) {
            showAlert("Error", "Select a family from the table first");
            return;
        }
        
        Family family = new Family();
        family.setFamilyId(Integer.parseInt(familyIdField.getText()));
        family.setHeadOfFamily(familyHeadNameField.getText());
        
        try {
            family.setMemberCount(Integer.parseInt(familyNationalIdField.getText()));
        } catch (NumberFormatException e) {
            family.setMemberCount(1);
        }
        
        family.setRegion(familyAddressField.getText());
        
        if (familyDAO.updateFamily(family)) {
            showAlert("Success", "Family updated successfully");
            loadFamilyTable();
            loadFamilyCombo();
            clearFamilyForm();
        } else {
            showAlert("Error", "Failed to update family");
        }
    }

    @FXML
    public void deleteFamily() {
        if (familyIdField.getText().isEmpty()) {
            showAlert("Error", "Select a family from the table first");
            return;
        }
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setContentText("Are you sure you want to delete this family?");
        
        if (confirm.showAndWait().get() == ButtonType.OK) {
            int id = Integer.parseInt(familyIdField.getText());
            if (familyDAO.deleteFamily(id)) {
                showAlert("Success", "Family deleted successfully");
                loadFamilyTable();
                loadFamilyCombo();
                loadDashboardData();
                clearFamilyForm();
            } else {
                showAlert("Error", "Failed to delete family");
            }
        }
    }

    @FXML
    public void onFamilySelected() {
        Family family = familyTable.getSelectionModel().getSelectedItem();
        if (family != null) {
            familyIdField.setText(String.valueOf(family.getFamilyId()));
            familyHeadNameField.setText(family.getHeadOfFamily());
            familyNationalIdField.setText(String.valueOf(family.getMemberCount()));
            familyAddressField.setText(family.getRegion());
        }
    }

    @FXML
    public void clearFamilyForm() {
        familyIdField.clear();
        familyNationalIdField.clear();
        familyHeadNameField.clear();
        if (familyPhoneField != null) familyPhoneField.clear();
        familyAddressField.clear();
        if (familyVulnerabilityCombo != null) familyVulnerabilityCombo.setValue(null);
        if (familyDatePicker != null) familyDatePicker.setValue(null);
    }

    // ========== AID DISTRIBUTION ==========

    @FXML
    public void distributeAid() {
        if (familyCombo.getValue() == null || aidTypeCombo.getValue() == null || aidDatePicker.getValue() == null) {
            showAlert("Error", "Please fill all distribution fields");
            return;
        }
        
        String selectedFamily = familyCombo.getValue();
        int familyId = Integer.parseInt(selectedFamily.split(" - ")[0]);
        
        AidDistribution dist = new AidDistribution();
        dist.setFamilyId(familyId);
        dist.setAidType(aidTypeCombo.getValue());
        dist.setDistributionDate(aidDatePicker.getValue());
        
        if (currentUser != null && currentUser.getOrgId() != 0) {
            dist.setOrgId(currentUser.getOrgId());
        } else {
            dist.setOrgId(1);
        }
        
        if (distDAO.addDistribution(dist)) {
            familyDAO.updateAidStatus(familyId, true);
            
            showAlert("Success", "Aid distributed successfully");
            loadDistributionTable();
            loadFamilyTable();
            loadDashboardData();
            
            familyCombo.setValue(null);
            aidTypeCombo.setValue(null);
            aidDatePicker.setValue(null);
        } else {
            showAlert("Error", "Failed to record distribution");
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}