package sample;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    Connection con;
    @FXML
    JFXListView<Patient> listView;

    @FXML
    JFXComboBox<String> cmbBloodGroup;

    @FXML
    JFXComboBox<String> cmbGender;

    @FXML
    JFXTextField txtName;

    @FXML
    public void createTable() {
        try {
            Statement statement = con.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS `patients_data`.`patients` ( `uuid` INT NOT NULL AUTO_INCREMENT , "
                    + "`patient_name` VARCHAR(50) NOT NULL , `patient_age` INT NOT NULL , `patient_gender` VARCHAR(50) NOT NULL ,"
                    + " `patient_blood_type` VARCHAR(50) NOT NULL , `patient_weight` DOUBLE NOT NULL , `patient_height` VARCHAR(50) NOT NULL ,"
                    + " PRIMARY KEY (`uuid`))";

            if(statement.executeUpdate(sql) == 1) {
                System.out.println("Table created");
                fillTable();
            } else {
                System.out.println("Table already created");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void fillTable() {
        try {
            Statement statement = con.createStatement();
            String sql = "INSERT INTO `patients_data`.`patients` ( `patient_name`, `patient_age`, `patient_gender`, `patient_blood_type`, `patient_weight`, `patient_height`) VALUES ( 'Victor Valdes', '32', 'Male', 'O+', '34', '5 feet 5 inches')";
            statement.executeUpdate(sql);

            sql = "INSERT INTO `patients_data`.`patients` (  `patient_name`, `patient_age`, `patient_gender`, `patient_blood_type`, `patient_weight`, `patient_height`) VALUES ( 'Lionel Messi', '24', 'Male', 'A+', '32', '5 feet')";
            statement.executeUpdate(sql);

            sql = "INSERT INTO `patients_data`.`patients` (  `patient_name`, `patient_age`, `patient_gender`, `patient_blood_type`, `patient_weight`, `patient_height`) VALUES ( 'Christiano Ronaldo', '51', 'Male', 'A+', '28', '5 feet 3 inches')";
            statement.executeUpdate(sql);

            sql = "INSERT INTO `patients_data`.`patients` (  `patient_name`, `patient_age`, `patient_gender`, `patient_blood_type`, `patient_weight`, `patient_height`) VALUES ( 'Luis Suarez', '32', 'Male', 'B+', '23', '6 feet')";
            statement.executeUpdate(sql);

            sql = "INSERT INTO `patients_data`.`patients` (  `patient_name`, `patient_age`, `patient_gender`, `patient_blood_type`, `patient_weight`, `patient_height`) VALUES ( 'Roger Federer', '36', 'Male', 'AB-', '32', '5 feet 9 inches')";
            statement.executeUpdate(sql);

            sql = "INSERT INTO `patients_data`.`patients` (  `patient_name`, `patient_age`, `patient_gender`, `patient_blood_type`, `patient_weight`, `patient_height`) VALUES ( 'Sarah Paul', '18', 'Female', 'AB+', '40', '5 feet 8 inches')";
            statement.executeUpdate(sql);

            sql = "INSERT INTO `patients_data`.`patients` (  `patient_name`, `patient_age`, `patient_gender`, `patient_blood_type`, `patient_weight`, `patient_height`) VALUES ( 'Serena Williams', '20', 'Female', 'O+', '38', '5 feet 7 inches')";
            statement.executeUpdate(sql);

            sql = "INSERT INTO `patients_data`.`patients` (  `patient_name`, `patient_age`, `patient_gender`, `patient_blood_type`, `patient_weight`, `patient_height`) VALUES ( 'Jennifer Anniston', '28', 'Female', 'O-', '60', '5 feet 6 inches')";
            statement.executeUpdate(sql);

            sql = "INSERT INTO `patients_data`.`patients` (  `patient_name`, `patient_age`, `patient_gender`, `patient_blood_type`, `patient_weight`, `patient_height`) VALUES ( 'Amber Heard', '32', 'Female', 'A-', '10', '5 feet 3 inches')";
            statement.executeUpdate(sql);

            sql = "INSERT INTO `patients_data`.`patients` (  `patient_name`, `patient_age`, `patient_gender`, `patient_blood_type`, `patient_weight`, `patient_height`) VALUES ( 'Anne Hathaway', '36', 'Female', 'O-', '9', '5 feet 4 inches')";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void createDatabase() {
        try {

            System.out.println("DB connection successful");
            Statement statement = con.createStatement();
            String sql = "CREATE DATABASE IF NOT EXISTS patients_data";
            statement.executeUpdate(sql);
        } catch ( SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "houston1995");
            cmbGender.setPromptText("Select Gender");
            cmbGender.getItems().add("");
            cmbGender.getItems().add("Male");
            cmbGender.getItems().add("Female");

            cmbBloodGroup.setPromptText("Select Blood Group");
            cmbBloodGroup.getItems().add("");
            cmbBloodGroup.getItems().add("A+");
            cmbBloodGroup.getItems().add("A-");
            cmbBloodGroup.getItems().add("B+");
            cmbBloodGroup.getItems().add("B-");
            cmbBloodGroup.getItems().add("O+");
            cmbBloodGroup.getItems().add("O-");
            cmbBloodGroup.getItems().add("AB+");
            cmbBloodGroup.getItems().add("AB-");

            txtName.setPromptText("Enter Name");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        populateListView();
    }

    ArrayList<Patient> patientsList;

    @FXML
    public void populateListView() {
        patientsList = getDataFromDB();
        listView.getItems().clear();
        if (patientsList != null) {
           for (int i = 0; i < patientsList.size(); i++) {
               listView.getItems().add(patientsList.get(i));
           }
        }
    }

    private ArrayList<Patient> getDataFromDB() {

        patientsList = new ArrayList<>();

        try {

            Statement statement = con.createStatement();
            String whereClause = getWhereClause();
            String sql = "SELECT * FROM `patients_data`.`patients` WHERE 1 " + whereClause;
            System.out.println("sql = " + sql);
            ResultSet rs = statement.executeQuery(sql);

            while(rs.next()) {
                Patient patient = new Patient();
                patient.setAge(rs.getInt("patient_age"));
                patient.setBloodType(rs.getString("patient_blood_type"));
                patient.setGender(rs.getString("patient_gender"));
                patient.setHeight(rs.getString("patient_height"));
                patient.setId(rs.getInt("uuid"));
                patient.setName(rs.getString("patient_name"));
                patient.setWeight(rs.getDouble("patient_weight"));
                patientsList.add(patient);
            }

            return patientsList;
        } catch (SQLException ex) {
            return null;
        }
    }

    private String getWhereClause() {
        String whereClause = "";
        if (txtName.getText() != null && !txtName.getText().equals("")) {
            whereClause += " and `patients`.patient_name LIKE '%" + txtName.getText() + "%' ";
        }

        if (cmbGender.getSelectionModel().getSelectedItem() != null && !cmbGender.getSelectionModel().getSelectedItem().equals("")) {
            whereClause += " and `patients`.patient_gender = '" +cmbGender.getSelectionModel().getSelectedItem() + "' ";
        }

        if (cmbBloodGroup.getSelectionModel().getSelectedItem() != null && !cmbBloodGroup.getSelectionModel().getSelectedItem().equals("")) {
            whereClause += " and `patients`.patient_blood_type = '" + cmbBloodGroup.getSelectionModel().getSelectedItem() + "' ";
        }


        return whereClause;
    }
}
