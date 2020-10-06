package SchedulingApp.View_Controller;

import SchedulingApp.DAO.DBAppointment;
import SchedulingApp.DAO.DBCustomer;
import SchedulingApp.DAO.DBUser;
import SchedulingApp.Model.Appointment;
import SchedulingApp.Model.Customer;
import SchedulingApp.Model.User;
import java.io.IOException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class ReportController implements Initializable {
    
    @FXML
    private ToggleGroup tgReports;
    
    @FXML
    private RadioButton tbApptTypes;

    @FXML
    private RadioButton tbSchedule;

    @FXML
    private RadioButton tbCustomer;
    
    @FXML
    private TextArea txtReportField;
    
    @FXML
    private Button btnExit;
    
    @FXML
    private final DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    
    @FXML
    private final DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("hh:mm a z");

    @FXML
    void getApptTypes(ActionEvent event) {
        if (tbApptTypes.isSelected()) {
            try {
                txtReportField.clear();
                ObservableList<Appointment> apptTypes = DBAppointment.getApptsByMonth();
                Integer value = 1;
                Map<String, Integer> map = new HashMap<>();
                for (Appointment a : apptTypes) {
                    if (map.containsKey(a.getType())) {
                        map.put(a.getType(), map.get(a.getType())+1);
                    }
                    else{
                        map.put(a.getType(), value);
                    }
                }
                for (String s : map.keySet()) {
                    txtReportField.appendText("There are: " + map.get(s) + " appointment type(s) of: " + s + " this month.\n");
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void getCustomerList(ActionEvent event) {
        if (tbCustomer.isSelected()) {
            try {
                txtReportField.clear();
                ObservableList<Customer> allCustomers = DBCustomer.getAllCustomers();
                String customerName = "";
                Boolean active;
                String newline = "\n";
                for (Customer c : allCustomers) {
                    customerName = c.getCustomerName();
                    active = c.activeProperty().get();
                    txtReportField.appendText("Customer: " + customerName + " active status equals: " + active + " in the database." + newline);
                }
                
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void getSchedule(ActionEvent event) {
        if (tbSchedule.isSelected()) {
            try {
                txtReportField.clear();
                ObservableList<Appointment> userSchedule = DBAppointment.getApptsByUser();
                String userName = "";
                String custName = "";
                ZonedDateTime startZDT;
                
                for (Appointment a : userSchedule) {
                    int u = a.getUserId();
                    int c = a.getCustomerId();
                    startZDT = a.getStart();
                    User userById = DBUser.getUserById(u);
                    userName = userById.getUserName();
                    Customer activeCustomerById = DBCustomer.getActiveCustomerById(c);
                    custName = activeCustomerById.getCustomerName();
                    txtReportField.appendText("User: " + userName 
                            + " has an appointment with " + custName 
                            + " on " + startZDT.format(formatDate) 
                            + " at " + startZDT.format(formatTime) + ".\n");
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void handleExitAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Customer Additon");
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }    
    
}
