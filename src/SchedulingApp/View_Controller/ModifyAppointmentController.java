package SchedulingApp.View_Controller;

import SchedulingApp.DAO.DBAppointment;
import SchedulingApp.DAO.DBCustomer;
import SchedulingApp.Exceptions.AppointmentException;
import SchedulingApp.Model.Appointment;
import SchedulingApp.Model.Customer;
import static SchedulingApp.View_Controller.AppointmentCalendarController.selectedAppt;
import static SchedulingApp.View_Controller.LoginScreenController.loggedUser;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.LocalTimeStringConverter;


public class ModifyAppointmentController implements Initializable {
    
    @FXML
    private Label lblCustomer;

    @FXML
    private Label lblTitle;

    @FXML
    private Label lblDescription;

    @FXML
    private Label lblLocation;

    @FXML
    private Label lblContact;

    @FXML
    private Label lblType;

    @FXML
    private Label lblUrl;

    @FXML
    private Label lblStart;

    @FXML
    private Label lblEnd;

    @FXML
    private ComboBox<Customer> cbCustomer;

    @FXML
    private TextField txtTitle;

    @FXML
    private TextField txtDescription;

    @FXML
    private TextField txtLocation;

    @FXML
    private TextField txtContact;

    @FXML
    private TextField txtType;

    @FXML
    private TextField txtUrl;

    @FXML
    private TextField txtStart;

    @FXML
    private TextField txtEnd;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnExit;
    
    @FXML
    private DatePicker dpStartDate;

    @FXML
    private Spinner<LocalTime> spStartTime;
    
    @FXML
    private DatePicker dpEndDate;

    @FXML
    private Spinner<LocalTime> spEndTime;
    
    @FXML
    private String errEmptyInput = new String();
    
    @FXML
    private String errValidation = new String();
    
    @FXML
    public static Appointment appt = new Appointment();
    
    @FXML
    private final DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    @FXML
    private final DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("HH:mm");
    
    @FXML
    private static ZoneId zId = ZoneId.systemDefault();
    
    @FXML
    void getExitAction(ActionEvent eExitAction) {
        Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION);
        exitAlert.setTitle("Exit Scheduling App");
        exitAlert.setHeaderText("Are you sure you want to exit?");
        exitAlert.setContentText("Press OK to exit the program. \nPress Cancel to stay on this screen.");
        exitAlert.showAndWait();
        if (exitAlert.getResult() == ButtonType.OK) {
            try {
                FXMLLoader apptCalLoader = new FXMLLoader(AppointmentCalendarController.class.getResource("AppointmentCalendar.fxml"));
                Parent apptCalScreen = apptCalLoader.load();
                Scene apptCalScene = new Scene(apptCalScreen);
                Stage apptCalStage = new Stage();
                apptCalStage.setTitle("Appointment Calendar");
                apptCalStage.setScene(apptCalScene);
                apptCalStage.show();
                Stage modApptStage = (Stage) btnExit.getScene().getWindow();
                modApptStage.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            exitAlert.close();
        }
    }

    @FXML
    void getSaveAction(ActionEvent eSaveAction) throws Exception {
        Alert saveAlert = new Alert(Alert.AlertType.CONFIRMATION);
        saveAlert.setTitle("Save Appointment Modifications");
        saveAlert.setHeaderText("Are you sure you want to save?");
        saveAlert.setContentText("Press OK to save the modifications. \nPress Cancel to stay on this screen.");
        saveAlert.showAndWait();
        if (saveAlert.getResult() == ButtonType.OK) {
            try {
                updateApptInfo();
                appt.isValidInput();
                appt.isNotOverlapping();
                if (appt.isValidInput() && appt.isNotOverlapping()) {
                    DBAppointment.updateAppointment(appt);
                    FXMLLoader apptCalLoader = new FXMLLoader(AppointmentCalendarController.class.getResource("AppointmentCalendar.fxml"));
                    Parent apptCalScreen = apptCalLoader.load();
                    Scene apptCalScene = new Scene(apptCalScreen);
                    Stage apptCalStage = new Stage();
                    apptCalStage.setTitle("Appointment Calendar");
                    apptCalStage.setScene(apptCalScene);
                    apptCalStage.show();
                    Stage modApptStage = (Stage) btnSave.getScene().getWindow();
                    modApptStage.close();
                }
            }
            catch (AppointmentException e) {
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
    
    private ObservableList<Customer> activeCusts = DBCustomer.getActiveCustomers();
    
    public void getActiveCustomers() {
        cbCustomer.setItems(activeCusts);
    }
    
    public void convertCustomerString() {
        cbCustomer.setConverter(new StringConverter<Customer>() {
            @Override
            public String toString(Customer cust) {
                return cust.getCustomerName();
            }

            @Override
            public Customer fromString(String string) {
                return cbCustomer.getValue();
            }
        });
    }
    
    public void setSelectedApptInfo() {
        cbCustomer.setValue(selectedAppt.getCustomer());
        txtTitle.setText(selectedAppt.getTitle());
        txtDescription.setText(selectedAppt.getDescription());
        txtLocation.setText(selectedAppt.getLocation());
        txtContact.setText(selectedAppt.getContact());
        txtType.setText(selectedAppt.getType());
        txtUrl.setText(selectedAppt.getUrl());
        dpStartDate.setValue(selectedAppt.getStart().toLocalDate());
        spStartTime.setValueFactory(svfStart);
        svfStart.setValue(selectedAppt.getStart().toLocalTime());
        dpEndDate.setValue(selectedAppt.getEnd().toLocalDate());
        spEndTime.setValueFactory(svfEnd);
        svfEnd.setValue(selectedAppt.getEnd().toLocalTime());
    }
    
    public void updateApptInfo() {
        appt.setCustomerId(cbCustomer.getValue().getCustomerId());
        appt.setUserId(loggedUser.getUserId());
        appt.setTitle(txtTitle.getText());
        appt.setDescription(txtDescription.getText());
        appt.setLocation(txtLocation.getText());
        appt.setContact(txtContact.getText());
        appt.setType(txtType.getText());
        appt.setUrl(txtUrl.getText());
        appt.setStart(ZonedDateTime.of(LocalDate.parse(dpStartDate.getValue().toString(), formatDate), LocalTime.parse(spStartTime.getValue().toString(), formatTime), zId));
        appt.setEnd(ZonedDateTime.of(LocalDate.parse(dpEndDate.getValue().toString(), formatDate), LocalTime.parse(spEndTime.getValue().toString(), formatTime), zId));
        appt.setAppointmentId(selectedAppt.getAppointmentId());
    }

    SpinnerValueFactory svfStart = new SpinnerValueFactory<LocalTime>() {
        {
            setConverter(new LocalTimeStringConverter(formatTime,null));
        }
        @Override
        public void decrement(int steps) {
            LocalTime time = (LocalTime) getValue();
            setValue(time.minusHours(steps));
            setValue(time.minusMinutes(16 - steps));
        }
        @Override
        public void increment(int steps) {
            LocalTime time = (LocalTime) getValue();
            setValue(time.plusHours(steps));
            setValue(time.plusMinutes(steps + 14));            
        }
    };
    
    SpinnerValueFactory svfEnd = new SpinnerValueFactory<LocalTime>() {
        {
            setConverter(new LocalTimeStringConverter(formatTime,null));
        }
        @Override
        public void decrement(int steps) {
            LocalTime time = (LocalTime) getValue();
            setValue(time.minusHours(steps));
            setValue(time.minusMinutes(16 - steps));
        }
        @Override
        public void increment(int steps) {
            LocalTime time = (LocalTime) getValue();
            setValue(time.plusHours(steps));
            setValue(time.plusMinutes(steps + 14));
        }
    };
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getActiveCustomers();
        setSelectedApptInfo();
        convertCustomerString();
    }
    
}
