package SchedulingApp.View_Controller;

import SchedulingApp.DAO.DBAddress;
import SchedulingApp.DAO.DBCity;
import SchedulingApp.DAO.DBCountry;
import SchedulingApp.DAO.DBCustomer;
import SchedulingApp.Exceptions.CustomerException;
import SchedulingApp.Model.Address;
import SchedulingApp.Model.City;
import SchedulingApp.Model.Country;
import SchedulingApp.Model.Customer;
import static SchedulingApp.View_Controller.CustomerSelectionController.selectedCust;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class ModifyCustomerController implements Initializable {

    @FXML
    private Label lblCustName;

    @FXML
    private Label lblCustAddress;

    @FXML
    private Label lblCustAddress2;

    @FXML
    private Label lblCity;

    @FXML
    private Label lblCustPostalCode;

    @FXML
    private Label lblCustPhone;

    @FXML
    private TextField txtCustAddress;
    
    @FXML
    private TextField txtCustAddress2;

    @FXML
    private TextField txtCustCity;

    @FXML
    private TextField txtCustPostalCode;

    @FXML
    private TextField txtCustPhone;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnExit;

    @FXML
    private TextField txtCustName;
    
    @FXML
    private Label lblCountry;

    @FXML
    private ComboBox<Country> cbCountry;
    
    @FXML
    private Address custAddress = DBAddress.getAddressById(selectedCust.getAddressId());
    private City custCity = DBCity.getCityById(custAddress.getCityId());
    private Country custCountry = DBCountry.getCountryById(custCity.getCountryId());

    @FXML
    void getExitAction(ActionEvent eExitButton) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Customer Modification");
        alert.setHeaderText("Are you sure you want to exit?");
        alert.setContentText("Press OK to exit the program. \nPress Cancel to stay on this screen.");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            try {
                FXMLLoader apptCalLoader = new FXMLLoader(AppointmentCalendarController.class.getResource("AppointmentCalendar.fxml"));
                Parent apptCalScreen = apptCalLoader.load();
                Scene apptCalScene = new Scene(apptCalScreen);
                Stage apptCalStage = new Stage();
                apptCalStage.setTitle("Appointment Calendar");
                apptCalStage.setScene(apptCalScene);
                apptCalStage.show();
                Stage modCustStage = (Stage) btnExit.getScene().getWindow();
                modCustStage.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            alert.close();
        }
    }

    @FXML
    void getSaveAction(ActionEvent event) {
        Alert saveAlert = new Alert(Alert.AlertType.CONFIRMATION);
        saveAlert.setTitle("Save Customer Modifications");
        saveAlert.setHeaderText("Are you sure you want to save?");
        saveAlert.setContentText("Press OK to save the modifications. \nPress Cancel to stay on this screen.");
        saveAlert.showAndWait();
        if (saveAlert.getResult() == ButtonType.OK) {
            try {
                updateCustInfo();
                if (Customer.isValidInput(selectedCust, custAddress, custCity, custCountry)) {
                    try {
                        DBCustomer.updateCustomer(selectedCust);
                        DBAddress.updateAddress(custAddress);
                        DBCity.updateCity(custCity);
                        FXMLLoader apptCalLoader = new FXMLLoader(AppointmentCalendarController.class.getResource("AppointmentCalendar.fxml"));
                        Parent apptCalScreen = apptCalLoader.load();
                        Scene apptCalScene = new Scene(apptCalScreen);
                        Stage apptCalStage = new Stage();
                        apptCalStage.setTitle("Appointment Calendar");
                        apptCalStage.setScene(apptCalScene);
                        apptCalStage.show();
                        Stage modCustStage = (Stage) btnSave.getScene().getWindow();
                        modCustStage.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            catch (CustomerException e) {
                Alert exAlert = new Alert(Alert.AlertType.ERROR);
                exAlert.setTitle("Exception");
                exAlert.setHeaderText("There was an exception!");
                exAlert.setContentText(e.getMessage());
                exAlert.showAndWait().filter(response -> response == ButtonType.OK);
            }
        }
        else {
            saveAlert.close();
        }
    }
    
    public void updateCustInfo() {
        try {
            selectedCust.setCustomerName(txtCustName.getText());
            custAddress.setAddress(txtCustAddress.getText());
            custAddress.setAddress2(txtCustAddress2.getText());
            custAddress.setPostalCode(txtCustPostalCode.getText());
            custAddress.setPhone(txtCustPhone.getText());
            custCity.setCity(txtCustCity.getText());
            custCity.setCountryId(cbCountry.getSelectionModel().getSelectedItem().getCountryId());
        }
        catch (NullPointerException e) {
            Alert nullAlert = new Alert(Alert.AlertType.ERROR);
            nullAlert.setTitle("Customer Addition Error");
            nullAlert.setHeaderText("The customer is not able to be added!");
            nullAlert.setContentText("One or more fields are empty!" + "\n" + e.getCause().toString());
            nullAlert.showAndWait();
        }
    }
    
    public void setCountries() {
        cbCountry.setItems(DBCountry.getAllCountries());
    }
    
    public void setSelectedCustomerInfo() {
        txtCustName.setText(selectedCust.getCustomerName());
        txtCustAddress.setText(custAddress.getAddress());
        txtCustAddress2.setText(custAddress.getAddress2());
        txtCustPostalCode.setText(custAddress.getPostalCode());
        txtCustPhone.setText(custAddress.getPhone());
        txtCustCity.setText(custCity.getCity());
        cbCountry.setValue(custCountry);
    }
    
    public void convertCountryString() {
        cbCountry.setConverter(new StringConverter<Country>() {
            @Override
            public String toString(Country country) {
                return country.getCountry();
            }

            @Override
            public Country fromString(String string) {
                return cbCountry.getValue();
            }
        });
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setCountries();
        convertCountryString();
        setSelectedCustomerInfo();
    }    
    
}
