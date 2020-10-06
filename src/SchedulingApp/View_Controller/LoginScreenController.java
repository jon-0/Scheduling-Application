package SchedulingApp.View_Controller;

import SchedulingApp.DAO.DBAppointment;
import SchedulingApp.DAO.DBUser;
import SchedulingApp.Model.Appointment;
import SchedulingApp.Model.User;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class LoginScreenController implements Initializable {
    ResourceBundle rb;
    Locale userLocale;
    Logger userLog = Logger.getLogger("userlog.txt");
    
    @FXML
    private Label lblAlert;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUsername;

    @FXML
    private Label lblUsername;

    @FXML
    private Label lblPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnExit;

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

    @FXML
    void getLoginAction(ActionEvent eLogin) throws IOException, Exception {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        loggedUser.setUserName(username);
        loggedUser.setPassword(password);
        
        FileHandler userLogFH = new FileHandler("userlog.txt", true);
        SimpleFormatter sf = new SimpleFormatter();
        userLogFH.setFormatter(sf);
        userLog.addHandler(userLogFH);
        userLog.setLevel(Level.INFO);
        
        try {
            ObservableList<User> userLoginInfo = DBUser.getActiveUsers();
            // Lambda - efficiently iterates through the list of active users
            userLoginInfo.forEach((u) -> {
                try {
                    assert loggedUser.getUserName().equals(u.getUserName()) && loggedUser.getPassword().equals(u.getPassword()) : "Incorrect login info!";
                    loggedUser.setUserId(u.getUserId());
                    try {
                        Appointment upcomingAppt = DBAppointment.getUpcomingAppt();
                        if (!(upcomingAppt.getAppointmentId() == 0)) {
                            Alert apptAlert = new Alert(Alert.AlertType.INFORMATION);
                            apptAlert.setTitle("Upcoming Appointment Reminder");
                            apptAlert.setHeaderText("You have an upcoming appointment!");
                            apptAlert.setContentText("You have an appointment scheduled" 
                                    + "\non " + upcomingAppt.getStart().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))
                                    + "\nat " + upcomingAppt.getStart().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.FULL))
                                    + " with client " + upcomingAppt.getCustomer().getCustomerName() + ".");
                            apptAlert.showAndWait();
                            if (apptAlert.getResult() == ButtonType.OK) {
                                userLog.log(Level.INFO, "User: {0} logged in.", loggedUser.getUserName());
                                Stage loginStage = (Stage) btnLogin.getScene().getWindow();
                                loginStage.close();
                                FXMLLoader apptCalLoader = new FXMLLoader(AppointmentCalendarController.class.getResource("AppointmentCalendar.fxml"));
                                Parent apptCalScreen = apptCalLoader.load();
                                Scene apptCalScene = new Scene(apptCalScreen);
                                Stage apptCalStage = new Stage();
                                apptCalStage.setTitle("Appointment Calendar");
                                apptCalStage.setScene(apptCalScene);
                                apptCalStage.show();
                            }
                            else {
                                apptAlert.close();
                            }
                        }
                        else {
                            userLog.log(Level.INFO, "User: {0} logged in.", loggedUser.getUserName());
                            FXMLLoader apptCalLoader = new FXMLLoader(AppointmentCalendarController.class.getResource("AppointmentCalendar.fxml"));
                            Parent apptCalScreen = apptCalLoader.load();
                            Scene apptCalScene = new Scene(apptCalScreen);
                            Stage apptCalStage = new Stage();
                            apptCalStage.setTitle("Appointment Calendar");
                            apptCalStage.setScene(apptCalScene);
                            apptCalStage.show();
                            Stage loginStage = (Stage) btnLogin.getScene().getWindow();
                            loginStage.close();
                        }
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                catch (AssertionError e) {
                    System.out.println(e.getMessage());
                    this.lblAlert.setText(this.rb.getString("lblErrorAlert") + ".");
                    this.lblAlert.setTextFill(Paint.valueOf("RED"));
                    userLog.log(Level.WARNING, "Invalid credentials entered! User: {0}", loggedUser.getUserName());
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static User loggedUser = new User();
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.userLocale = Locale.getDefault();
        this.rb = ResourceBundle.getBundle("LocaleLanguageFiles/rb", this.userLocale);
        this.lblUsername.setText(this.rb.getString("username") + ":");
        this.lblPassword.setText(this.rb.getString("password") + ":");
        this.txtUsername.setPromptText(this.rb.getString("usernamePrompt"));
        this.txtPassword.setPromptText(this.rb.getString("passwordPrompt"));
        this.btnLogin.setText(this.rb.getString("btnLoginText"));
        this.btnExit.setText(this.rb.getString("btnExitText"));
    }
}
