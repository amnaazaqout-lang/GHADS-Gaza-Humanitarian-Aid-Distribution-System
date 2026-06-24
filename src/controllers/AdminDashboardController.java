package controllers;

// استدعاء ملفات الـ DAO بشكل صريح
import dao.UserDAO;
import dao.FamilyDAO;
import dao.OrganizationDAO;
import dao.AidDistributionDAO;

// استدعاء ملفات الـ Model بالمفرد لتطابق مشروعك تماماً واختفاء الأخطاء
import model.User;
import model.Family;
import model.Organization;
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

public class AdminDashboardController {
    
    private User currentUser;
    
    // Dashboard labels
    @FXML private Label totalOrgsLabel;
    @FXML private Label totalUsersLabel;
    @FXML private Label totalFamiliesLabel;
    @FXML private Label familiesServedLabel;
    @FXML private Label familiesNotServedLabel;
    
    // Tables
    @FXML private TableView<Organization> orgTable;
    @FXML private TableView<User> userTable;
    @FXML private TableView<Family> familyTable;
    @FXML private TableView<AidDistribution> distTable;
    
    // Organization form fields
    @FXML private TextField orgNameField, orgTypeField, orgContactField;
    @FXML private TextField orgIdField;
    
    // User form fields
    @FXML private TextField userFullNameField, userUsernameField, userPasswordField, userEmailField;
    @FXML private ComboBox<String> userRoleCombo;
    @FXML private ComboBox<String> userOrgCombo;
    @FXML private TextField userIdField;
    
    // Family form fields
    @FXML private TextField familyNationalIdField, familyHeadNameField, familyPhoneField, familyAddressField;
    @FXML private ComboBox<String> familyVulnerabilityCombo;
    @FXML private DatePicker familyDatePicker;
    @FXML private TextField familyIdField;
    
    private OrganizationDAO orgDAO = new OrganizationDAO();
    private UserDAO userDAO = new UserDAO();
    private FamilyDAO familyDAO = new FamilyDAO();
    private AidDistributionDAO distDAO = new AidDistributionDAO();
    
    // ========== MENU HANDLERS ==========
    
    @FXML
    public void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/views/Login.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) totalOrgsLabel.getScene().getWindow();
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
    
    // ========== FORMAT MENU METHODS ==========
    
    @FXML
    public void setSmallFont() {
        Scene scene = totalOrgsLabel.getScene();
        scene.getRoot().setStyle("-fx-font-size: 10px;");
    }
    
    @FXML
    public void setMediumFont() {
        Scene scene = totalOrgsLabel.getScene();
        scene.getRoot().setStyle("-fx-font-size: 14px;");
    }
    
    @FXML
    public void setLargeFont() {
        Scene scene = totalOrgsLabel.getScene();
        scene.getRoot().setStyle("-fx-font-size: 18px;");
    }
    
    @FXML
    public void setFontArial() {
        Scene scene = totalOrgsLabel.getScene();
        scene.getRoot().setStyle("-fx-font-family: 'Arial';");
    }
    
    @FXML
    public void setFontTimes() {
        Scene scene = totalOrgsLabel.getScene();
        scene.getRoot().setStyle("-fx-font-family: 'Times New Roman';");
    }
    
    @FXML
    public void setFontCourier() {
        Scene scene = totalOrgsLabel.getScene();
        scene.getRoot().setStyle("-fx-font-family: 'Courier New';");
    }
    
    @FXML
    public void setLightTheme() {
        Scene scene = totalOrgsLabel.getScene();
        scene.getRoot().setStyle("-fx-base: #f5f5f5; -fx-background: #ffffff;");
    }
    
    @FXML
    public void setDarkTheme() {
        Scene scene = totalOrgsLabel.getScene();
        scene.getRoot().setStyle("-fx-base: #2b2b2b; -fx-background: #1e1e1e;");
    }
    
    // ========== INITIALIZATION ==========
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
        loadDashboardData();
        loadOrganizationTable();
        loadUserTable();
        loadFamilyTable();
        loadDistributionTable();
        setupCombos();
    }
    
    private void setupCombos() {
        userRoleCombo.getItems().clear();
        userRoleCombo.getItems().addAll("ADMIN", "COORDINATOR");
        familyVulnerabilityCombo.getItems().clear();
        familyVulnerabilityCombo.getItems().addAll("HIGH", "MEDIUM", "LOW");
        loadOrganizationCombo();
    }
    
    private void loadOrganizationCombo() {
        List<Organization> orgs = orgDAO.getAllOrganizations();
        userOrgCombo.getItems().clear();
        userOrgCombo.getItems().add("None");
        for (Organization org : orgs) {
            userOrgCombo.getItems().add(org.getOrgId() + " - " + org.getOrgName());
        }
    }
    
    private void loadDashboardData() {
        int totalOrgs = orgDAO.getAllOrganizations().size();
        int totalUsers = userDAO.getAllUsers().size();
        
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
        
        totalOrgsLabel.setText(String.valueOf(totalOrgs));
        totalUsersLabel.setText(String.valueOf(totalUsers));
        totalFamiliesLabel.setText(String.valueOf(totalFamilies));
        familiesServedLabel.setText(String.valueOf(served));
        familiesNotServedLabel.setText(String.valueOf(notServed));
    }
    
    private void loadOrganizationTable() {
        ObservableList<Organization> orgs = FXCollections.observableArrayList(orgDAO.getAllOrganizations());
        orgTable.setItems(orgs);
        
        orgTable.getColumns().clear();
        TableColumn<Organization, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("orgId"));
        TableColumn<Organization, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("orgName"));
        TableColumn<Organization, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("orgType"));
        TableColumn<Organization, String> contactCol = new TableColumn<>("Contact");
        contactCol.setCellValueFactory(new PropertyValueFactory<>("orgContact"));
        
        orgTable.getColumns().addAll(idCol, nameCol, typeCol, contactCol);
    }
    
    private void loadUserTable() {
        ObservableList<User> users = FXCollections.observableArrayList(userDAO.getAllUsers());
        userTable.setItems(users);
        
        userTable.getColumns().clear();
        TableColumn<User, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        TableColumn<User, String> nameCol = new TableColumn<>("Full Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        TableColumn<User, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        TableColumn<User, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        
        userTable.getColumns().addAll(idCol, nameCol, usernameCol, emailCol, roleCol);
    }
    
    private void loadFamilyTable() {
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
    
    // ========== ORGANIZATION CRUD ==========
    
    @FXML
    public void addOrganization() {
        Organization org = new Organization();
        org.setOrgName(orgNameField.getText());
        org.setOrgType(orgTypeField.getText());
        org.setOrgContact(orgContactField.getText());
        
        if (orgDAO.addOrganization(org)) {
            showAlert("Success", "Organization added");
            loadOrganizationTable();
            loadOrganizationCombo();
            loadDashboardData();
            clearOrganizationForm();
        } else {
            showAlert("Error", "Failed to add organization");
        }
    }
    
    @FXML
    public void updateOrganization() {
        if (orgIdField.getText().isEmpty()) {
            showAlert("Error", "Select an organization first");
            return;
        }
        
        Organization org = new Organization();
        org.setOrgId(Integer.parseInt(orgIdField.getText()));
        org.setOrgName(orgNameField.getText());
        org.setOrgType(orgTypeField.getText());
        org.setOrgContact(orgContactField.getText());
        
        if (orgDAO.updateOrganization(org)) {
            showAlert("Success", "Organization updated");
            loadOrganizationTable();
            loadOrganizationCombo();
            clearOrganizationForm();
        } else {
            showAlert("Error", "Failed to update organization");
        }
    }
    
    @FXML
    public void deleteOrganization() {
        if (orgIdField.getText().isEmpty()) {
            showAlert("Error", "Select an organization first");
            return;
        }
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setContentText("Are you sure?");
        
        if (confirm.showAndWait().get() == ButtonType.OK) {
            int id = Integer.parseInt(orgIdField.getText());
            if (orgDAO.deleteOrganization(id)) {
                showAlert("Success", "Organization deleted");
                loadOrganizationTable();
                loadOrganizationCombo();
                loadDashboardData();
                clearOrganizationForm();
            } else {
                showAlert("Error", "Failed to delete organization");
            }
        }
    }
    
    @FXML
    public void onOrganizationSelected() {
        Organization org = orgTable.getSelectionModel().getSelectedItem();
        if (org != null) {
            orgIdField.setText(String.valueOf(org.getOrgId()));
            orgNameField.setText(org.getOrgName());
            orgTypeField.setText(org.getOrgType());
            orgContactField.setText(org.getOrgContact());
        }
    }
    
    @FXML
    public void clearOrganizationForm() {
        orgIdField.clear();
        orgNameField.clear();
        orgTypeField.clear();
        orgContactField.clear();
    }
    
    // ========== USER CRUD ==========
    
    @FXML
    public void addUser() {
        User user = new User();
        user.setFullName(userFullNameField.getText());
        user.setUsername(userUsernameField.getText());
        user.setPassword(userPasswordField.getText());
        user.setEmail(userEmailField.getText());
        user.setRole(userRoleCombo.getValue());
        
        if (userDAO.addUser(user)) {
            showAlert("Success", "User added");
            loadUserTable();
            loadDashboardData();
            clearUserForm();
        } else {
            showAlert("Error", "Username or email may already exist");
        }
    }
    
    @FXML
    public void updateUser() {
        if (userIdField.getText().isEmpty()) {
            showAlert("Error", "Select a user first");
            return;
        }
        
        User user = new User();
        user.setUserId(Integer.parseInt(userIdField.getText()));
        user.setFullName(userFullNameField.getText());
        user.setUsername(userUsernameField.getText());
        user.setEmail(userEmailField.getText());
        user.setRole(userRoleCombo.getValue());
        
        if (userDAO.updateUser(user)) {
            showAlert("Success", "User updated");
            loadUserTable();
            clearUserForm();
        } else {
            showAlert("Error", "Failed to update user");
        }
    }
    
    @FXML
    public void deleteUser() {
        if (userIdField.getText().isEmpty()) {
            showAlert("Error", "Select a user first");
            return;
        }
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setContentText("Are you sure?");
        
        if (confirm.showAndWait().get() == ButtonType.OK) {
            int id = Integer.parseInt(userIdField.getText());
            if (userDAO.deleteUser(id)) {
                showAlert("Success", "User deleted");
                loadUserTable();
                loadDashboardData();
                clearUserForm();
            } else {
                showAlert("Error", "Failed to delete user");
            }
        }
    }
    
    @FXML
    public void onUserSelected() {
        User user = userTable.getSelectionModel().getSelectedItem();
        if (user != null) {
            userIdField.setText(String.valueOf(user.getUserId()));
            userFullNameField.setText(user.getFullName());
            userUsernameField.setText(user.getUsername());
            userEmailField.setText(user.getEmail());
            userRoleCombo.setValue(user.getRole());
        }
    }
    
    @FXML
    public void clearUserForm() {
        userIdField.clear();
        userFullNameField.clear();
        userUsernameField.clear();
        userPasswordField.clear();
        userEmailField.clear();
        userRoleCombo.setValue(null);
        userOrgCombo.setValue(null);
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
            showAlert("Success", "Family added");
            loadFamilyTable();
            loadDashboardData();
            clearFamilyForm();
        } else {
            showAlert("Error", "Failed to add family");
        }
    }
    
    @FXML
    public void updateFamily() {
        if (familyIdField.getText().isEmpty()) {
            showAlert("Error", "Select a family first");
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
        family.setReceivedAid(false);
        
        if (familyDAO.updateFamily(family)) {
            showAlert("Success", "Family updated");
            loadFamilyTable();
            clearFamilyForm();
        } else {
            showAlert("Error", "Failed to update family");
        }
    }
    
    @FXML
    public void deleteFamily() {
        if (familyIdField.getText().isEmpty()) {
            showAlert("Error", "Select a family first");
            return;
        }
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setContentText("Are you sure?");
        
        if (confirm.showAndWait().get() == ButtonType.OK) {
            int id = Integer.parseInt(familyIdField.getText());
            if (familyDAO.deleteFamily(id)) {
                showAlert("Success", "Family deleted");
                loadFamilyTable();
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
        familyPhoneField.clear();
        familyAddressField.clear();
        if (familyVulnerabilityCombo != null) familyVulnerabilityCombo.setValue(null);
        if (familyDatePicker != null) familyDatePicker.setValue(null);
    }
    
    // ========== UTILITIES ==========
    
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}