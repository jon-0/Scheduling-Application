package SchedulingApp.View_Controller;

import SchedulingApp.DAO.DBAppointment;
import static SchedulingApp.DAO.DBAppointment.getApptsByMonth;
import static SchedulingApp.DAO.DBAppointment.getApptsByWeek;
import SchedulingApp.DAO.DBCustomer;
import SchedulingApp.Model.Appointment;
import SchedulingApp.Model.Customer;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;


public class AppointmentCalendarController implements Initializable {
    
    @FXML
    private Button btnNewAppt;

    @FXML
    private Button btnModifyAppt;

    @FXML
    private Button btnDeleteAppt;

    @FXML
    private Button btnNewCust;

    @FXML
    private Button btnModifyCust;

    @FXML
    private Button btnDeleteCust;

    @FXML
    private Button btnReports;

    @FXML
    private Button btnUserLogs;

    @FXML
    private Button btnExit;

    @FXML
    private Tab tpWeeklyAppts;

    @FXML
    private TableView<Appointment> tvWeeklyAppts;

    @FXML
    private TableColumn<Appointment, String> tcWeeklyCustName;

    @FXML
    private TableColumn<Appointment, String> tcWeeklyApptTitle;

    @FXML
    private TableColumn<Appointment, String> tcWeeklyApptDescription;

    @FXML
    private TableColumn<Appointment, String> tcWeeklyApptLocation;

    @FXML
    private TableColumn<Appointment, String> tcWeeklyApptContact;

    @FXML
    private TableColumn<Appointment, String> tcWeeklyApptType;

    @FXML
    private TableColumn<Appointment, String> tcWeeklyApptURL;

    @FXML
    private TableColumn<Appointment, String> tcWeeklyApptStart;

    @FXML
    private TableColumn<Appointment, String> tcWeeklyApptEnd;

    @FXML
    private Tab tpMonthlyAppts;

    @FXML
    private TableView<Appointment> tvMonthlyAppts;

    @FXML
    private TableColumn<Appointment, String> tcMonthlyCustName;

    @FXML
    private TableColumn<Appointment, String> tcMonthlyApptTitle;

    @FXML
    private TableColumn<Appointment, String> tcMonthlyApptDescription;

    @FXML
    private TableColumn<Appointment, String> tcMonthlyApptLocation;

    @FXML
    private TableColumn<Appointment, String> tcMonthlyApptContact;

    @FXML
    private TableColumn<Appointment, String> tcMonthlyApptType;

    @FXML
    private TableColumn<Appointment, String> tcMonthlyApptURL;

    @FXML
    private TableColumn<Appointment, String> tcMonthlyApptStart;

    @FXML
    private TableColumn<Appointment, String> tcMonthlyApptEnd;
    
    @FXML
    private final DateTimeFormatter formatDT = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a z");
    
    @FXML
    public static Appointment selectedAppt;
    
    @FXML
    public static boolean isModification;
    
    @FXML
    void getNewAppt(ActionEvent event) {
        Alert addAlert = new Alert(AlertType.CONFIRMATION);
        addAlert.setTitle("Add Appointment");
        addAlert.setHeaderText("Are you sure you want to add a new appointment?");
        addAlert.setContentText("Press OK to add the appointment. \nPress Cancel to cancel the addition.");
        addAlert.showAndWait();
        if (addAlert.getResult() == ButtonType.OK) {
            try {
                FXMLLoader addApptLoader = new FXMLLoader(AddAppointmentController.class.getResource("AddAppointment.fxml"));
                Parent addApptScreen = addApptLoader.load();
                Scene addApptScene = new Scene(addApptScreen);
                Stage addApptStage = new Stage();
                addApptStage.setTitle("Add Appointment");
                addApptStage.setScene(addApptScene);
                addApptStage.setResizable(false);
                addApptStage.show();
                Stage apptCalStage = (Stage) btnNewAppt.getScene().getWindow();
                apptCalStage.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    @FXML
    void getModifyAppt(ActionEvent event) {
        if (tpWeeklyAppts.isSelected()) {
            selectedAppt = tvWeeklyAppts.getSelectionModel().getSelectedItem();
            if (selectedAppt == null) {
                Alert nullAlert = new Alert(AlertType.ERROR);
                nullAlert.setTitle("Appointment Modification Error");
                nullAlert.setHeaderText("The appointment is not able to be modified!");
                nullAlert.setContentText("There was no appointment selected!");
                nullAlert.showAndWait();
            }
            else {
                    Alert modAlert = new Alert(AlertType.CONFIRMATION);
                    modAlert.setTitle("Modify Appointment");
                    modAlert.setHeaderText("Are you sure you want to modify this appointment?");
                    modAlert.setContentText("Press OK to modify the appointment. \nPress Cancel to cancel the modification.");
                    modAlert.showAndWait();
                    if (modAlert.getResult() == ButtonType.OK) {
                        try {
                            FXMLLoader modApptLoader = new FXMLLoader(ModifyAppointmentController.class.getResource("ModifyAppointment.fxml"));
                            Parent modApptScreen = modApptLoader.load();
                            Scene modApptScene = new Scene(modApptScreen);
                            Stage modApptStage = new Stage();
                            modApptStage.setTitle("Modify Appointment");
                            modApptStage.setScene(modApptScene);
                            modApptStage.setResizable(false);
                            modApptStage.show();
                            Stage apptCalStage = (Stage) btnModifyAppt.getScene().getWindow();
                            apptCalStage.close();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        else if (tpMonthlyAppts.isSelected()) {
            selectedAppt = tvMonthlyAppts.getSelectionModel().getSelectedItem();
            if (selectedAppt == null) {
                Alert nullAlert = new Alert(AlertType.ERROR);
                nullAlert.setTitle("Appointment Modification Error");
                nullAlert.setHeaderText("The appointment is not able to be modified!");
                nullAlert.setContentText("There was no appointment selected!");
                nullAlert.showAndWait();
            }
            else {
                    Alert modAlert = new Alert(AlertType.CONFIRMATION);
                    modAlert.setTitle("Modify Appointment");
                    modAlert.setHeaderText("Are you sure you want to modify this appointment?");
                    modAlert.setContentText("Press OK to modify the appointment. \nPress Cancel to cancel the modification.");
                    modAlert.showAndWait();
                    if (modAlert.getResult() == ButtonType.OK) {
                        try {
                            FXMLLoader modApptLoader = new FXMLLoader(ModifyAppointmentController.class.getResource("ModifyAppointment.fxml"));
                            Parent modApptScreen = modApptLoader.load();
                            Scene modApptScene = new Scene(modApptScreen);
                            Stage modApptStage = new Stage();
                            modApptStage.setTitle("Modify Appointment");
                            modApptStage.setScene(modApptScene);
                            modApptStage.setResizable(false);
                            modApptStage.show();
                            Stage apptCalStage = (Stage) btnModifyAppt.getScene().getWindow();
                            apptCalStage.close();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        }
    }
    
    @FXML
    void getDeleteAppt(ActionEvent event) {
        if (tpWeeklyAppts.isSelected()) {
            Alert delAlert = new Alert(AlertType.CONFIRMATION);
            delAlert.setTitle("Delete Appointment");
            delAlert.setHeaderText("Are you sure you want to delete this appointment?");
            delAlert.setContentText("Press OK to delete the appointment. \nPress Cancel to cancel the deletion.");
            delAlert.showAndWait();
            if (delAlert.getResult() == ButtonType.OK) {
                try {
                    Appointment appt = tvWeeklyAppts.getSelectionModel().getSelectedItem();
                    DBAppointment.deleteAppointment(appt);
                    getAppointments();
                }
                catch (NullPointerException e) {
                    Alert nullAlert = new Alert(AlertType.ERROR);
                    nullAlert.setTitle("Appointment Modification Error");
                    nullAlert.setHeaderText("The appointment is not able to be deleted!");
                    nullAlert.setContentText("There was no appointment selected!");
                    nullAlert.showAndWait();
                }
            }
            else {
                delAlert.close();
            }
        }
        else if (tpMonthlyAppts.isSelected()) {
            Alert delAlert = new Alert(AlertType.CONFIRMATION);
            delAlert.setTitle("Delete Appointment");
            delAlert.setHeaderText("Are you sure you want to delete this appointment?");
            delAlert.setContentText("Press OK to delete the appointment. \nPress Cancel to cancel the deletion.");
            delAlert.showAndWait();
            if (delAlert.getResult() == ButtonType.OK) {
                try {
                    Appointment appt = tvMonthlyAppts.getSelectionModel().getSelectedItem();
                    DBAppointment.deleteAppointment(appt);
                    getAppointments();
                }
                catch (NullPointerException e) {
                    Alert nullAlert = new Alert(AlertType.ERROR);
                    nullAlert.setTitle("Appointment Modification Error");
                    nullAlert.setHeaderText("The appointment is not able to be deleted!");
                    nullAlert.setContentText("There was no appointment selected!");
                    nullAlert.showAndWait();
                }
            }
            else {
                delAlert.close();
            }
        }
    }

    @FXML
    void getNewCust(ActionEvent event) {
        Alert addAlert = new Alert(AlertType.CONFIRMATION);
        addAlert.setTitle("Add Customer");
        addAlert.setHeaderText("Are you sure you want to add a new customer?");
        addAlert.setContentText("Press OK to add the customer. \nPress Cancel to cancel the addition.");
        addAlert.showAndWait();
        if (addAlert.getResult() == ButtonType.OK) {
            try {
                FXMLLoader addCustLoader = new FXMLLoader(AddCustomerController.class.getResource("AddCustomer.fxml"));
                Parent addCustScreen = addCustLoader.load();
                Scene addCustScene = new Scene(addCustScreen);
                Stage addCustStage = new Stage();
                addCustStage.setTitle("Add Customer");
                addCustStage.setScene(addCustScene);
                addCustStage.setResizable(false);
                addCustStage.show();
                Stage apptCalStage = (Stage) btnNewCust.getScene().getWindow();
                apptCalStage.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    @FXML
    void getModifyCust(ActionEvent event) {
        Alert modAlert = new Alert(AlertType.CONFIRMATION);
        modAlert.setTitle("Modify Customer");
        modAlert.setHeaderText("Are you sure you want to modify a customer?");
        modAlert.setContentText("Press OK to modify a customer. \nPress Cancel to cancel the modification.");
        modAlert.showAndWait();
        if (modAlert.getResult() == ButtonType.OK) {
            try {
                isModification=true;
                FXMLLoader selCustLoader = new FXMLLoader(CustomerSelectionController.class.getResource("CustomerSelection.fxml"));
                Parent selCustScreen = selCustLoader.load();
                Scene selCustScene = new Scene(selCustScreen);
                Stage selCustStage = new Stage();
                selCustStage.setTitle("Customer Selection");
                selCustStage.setScene(selCustScene);
                selCustStage.setResizable(false);
                selCustStage.show();
                Stage apptCalStage = (Stage) btnModifyCust.getScene().getWindow();
                apptCalStage.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            modAlert.close();
        }
    }     
    
    @FXML
    void getDeleteCust(ActionEvent event) {
        Alert delAlert = new Alert(AlertType.CONFIRMATION);
        delAlert.setTitle("Delete Customer");
        delAlert.setHeaderText("Are you sure you want to delete a customer?");
        delAlert.setContentText("Press OK to delete a customer. \nPress Cancel to cancel the deletion.");
        delAlert.showAndWait();
        if (delAlert.getResult() == ButtonType.OK) {
            try {
                isModification=false;
                FXMLLoader selCustLoader = new FXMLLoader(CustomerSelectionController.class.getResource("CustomerSelection.fxml"));
                Parent selCustScreen = selCustLoader.load();
                Scene selCustScene = new Scene(selCustScreen);
                Stage selCustStage = new Stage();
                selCustStage.setTitle("Customer Selection");
                selCustStage.setScene(selCustScene);
                selCustStage.setResizable(false);
                selCustStage.show();
                Stage apptCalStage = (Stage) btnDeleteCust.getScene().getWindow();
                apptCalStage.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            delAlert.close();
        }
    }

    @FXML
    void getReports(ActionEvent event) {
        try {
            FXMLLoader runReportLoader = new FXMLLoader(ReportController.class.getResource("Report.fxml"));
            Parent runReportScreen = runReportLoader.load();
            Scene runReportScene = new Scene(runReportScreen);
            Stage runReportStage = new Stage();
            runReportStage.setTitle("Reports");
            runReportStage.setScene(runReportScene);
            runReportStage.setResizable(false);
            runReportStage.show();
            Stage apptCalStage = (Stage) btnModifyCust.getScene().getWindow();
            apptCalStage.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void getUserLogs(ActionEvent event) {
        try {
            ProcessBuilder pb = new ProcessBuilder("Notepad.exe", "userlog.txt");
            pb.start();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    void getExitAction(ActionEvent eExitButton) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Scheduling App");
        alert.setHeaderText("Are you sure you want to exit?");
        alert.setContentText("Press OK to exit the program. \nPress Cancel to stay on this screen.");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            Stage winMainScreen = (Stage)((Node)eExitButton.getSource()).getScene().getWindow();
            winMainScreen.close();
        }
        else {
            alert.close();
        }
    }
    
    public void getAppointments() {
        //Multiple lambdas to effciently set the values of the weekly and monthly table view columns.
        tcWeeklyCustName.setCellValueFactory(cellData -> { return cellData.getValue().getCustomer().customerNameProperty(); });
        tcWeeklyApptTitle.setCellValueFactory(cellData -> { return cellData.getValue().titleProperty(); });
        tcWeeklyApptDescription.setCellValueFactory(cellData -> { return cellData.getValue().descriptionProperty(); });
        tcWeeklyApptLocation.setCellValueFactory(cellData -> { return cellData.getValue().locationProperty(); });
        tcWeeklyApptContact.setCellValueFactory(cellData -> { return cellData.getValue().contactProperty(); });
        tcWeeklyApptType.setCellValueFactory(cellData -> { return cellData.getValue().typeProperty(); });
        tcWeeklyApptURL.setCellValueFactory(cellData -> { return cellData.getValue().urlProperty(); });
        tcWeeklyApptStart.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStart().format(formatDT)));
        tcWeeklyApptEnd.setCellValueFactory(cellData ->  new SimpleStringProperty(cellData.getValue().getEnd().format(formatDT)));
        tvWeeklyAppts.setItems(getApptsByWeek());
        tcMonthlyCustName.setCellValueFactory(cellData -> { return cellData.getValue().getCustomer().customerNameProperty(); });
        tcMonthlyApptTitle.setCellValueFactory(cellData -> { return cellData.getValue().titleProperty(); });
        tcMonthlyApptDescription.setCellValueFactory(cellData -> { return cellData.getValue().descriptionProperty(); });
        tcMonthlyApptLocation.setCellValueFactory(cellData -> { return cellData.getValue().locationProperty(); });
        tcMonthlyApptContact.setCellValueFactory(cellData -> { return cellData.getValue().contactProperty(); });
        tcMonthlyApptType.setCellValueFactory(cellData -> { return cellData.getValue().typeProperty(); });
        tcMonthlyApptURL.setCellValueFactory(cellData -> { return cellData.getValue().urlProperty(); });
        tcMonthlyApptStart.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStart().format(formatDT)));
        tcMonthlyApptEnd.setCellValueFactory(cellData ->  new SimpleStringProperty(cellData.getValue().getEnd().format(formatDT)));
        tvMonthlyAppts.setItems(getApptsByMonth());
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getAppointments();
    }
    
}
