package com.example.demo;


import com.example.demo.tools.Notification;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;


import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    private DBConnection db = new DBConnection();
    private int id;
    private String password;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTable();
    }

    public ObservableList<Client> getClients() {
        ObservableList<Client> clients = FXCollections.observableArrayList();
        String sql = "select * from client order by nom ASC";
        try {
            db.initPrepar(sql);
            ResultSet rs = db.executeSelect();
            while (rs.next()) {
                Client c = new Client();
                c.setId(rs.getInt("id"));
                c.setPrenom(rs.getString("prenom"));
                c.setNom(rs.getString("nom"));
                c.setEmail(rs.getString("email"));
                c.setPassword(rs.getString("password"));
                clients.add(c);

            }
            db.closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return clients;
    }

    public void loadTable() {
        ObservableList<Client> liste = getClients();
        ClientTbl.setItems(liste);
        idCol.setCellValueFactory(new PropertyValueFactory<Client, Integer>("id"));
        prenomCol.setCellValueFactory(new PropertyValueFactory<Client, String>("prenom"));
        nomCol.setCellValueFactory(new PropertyValueFactory<Client, String>("nom"));
        emailCol.setCellValueFactory(new PropertyValueFactory<Client, String>("email"));
        passwordCol.setCellValueFactory(new PropertyValueFactory<Client, String>("password"));


    }

    @FXML
    private Button ajouterBtn;

    @FXML
    private Button rechercherbtn;

    @FXML
    private Button modifierbtn;

    @FXML
    private Button supprimerbtn;

    @FXML
    private TextField prenomtfd;

    @FXML
    private TextField nomtfd;

    @FXML
    private TextField emailtfd;

    @FXML
    private PasswordField passwordtfd;

    @FXML
    private Button effacerBtn;

    @FXML
    private TableView<Client> ClientTbl;

    @FXML
    private TableColumn<Client, Integer> idCol;

    @FXML
    private TableColumn<Client, String> prenomCol;

    @FXML
    private TableColumn<Client, String> nomCol;

    @FXML
    private TableColumn<Client, String> emailCol;
    @FXML
    private TableColumn<Client, String> passwordCol;

    public void ClierFields() {
        prenomtfd.setText("");
        nomtfd.setText("");
        emailtfd.setText("");
        passwordtfd.setText("");
    }


    public void ajouter(javafx.event.ActionEvent actionEvent) {
        String sql = "INSERT INTO client VALUES(NULL, ?, ?, ?, ?)";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, prenomtfd.getText());
            db.getPstm().setString(2, nomtfd.getText());
            db.getPstm().setString(3, emailtfd.getText());
            db.getPstm().setString(4, passwordtfd.getText());

            db.executeMaj();
            db.closeConnection();
            loadTable();
            ClierFields();
            Notification.NotifSuccess("Success", "Client Enregistre avec succes");
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    public void modifier(ActionEvent actionEvent) {
        String sql = "UPDATE client SET  prenom = ?, nom = ?, email = ?, password = ? WHERE id = ? ";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, prenomtfd.getText());
            db.getPstm().setString(2, nomtfd.getText());
            db.getPstm().setString(3, emailtfd.getText());
            db.getPstm().setString(4, passwordtfd.getText());
            db.getPstm().setInt(5, id);
            db.executeMaj();
            db.closeConnection();
            loadTable();
            ClierFields();
            ajouterBtn.setDisable(false);
            Notification.NotifSuccess("Success", "Client modifie avec succes");
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    public void supprimer(ActionEvent actionEvent) {
        String sql = "DELETE FROM client WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, id);
            db.executeMaj();
            db.closeConnection();
            loadTable();
            ClierFields();
            ajouterBtn.setDisable(false);
            Notification.NotifSuccess("Success", "Client supprimer avec succes");
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    @FXML
    void effacer(ActionEvent event) {
        ClierFields();
    }

    public void rechercher(javafx.event.ActionEvent actionEvent) {
    }


    @FXML
    void GetData(MouseEvent event) {
        Client c = ClientTbl.getSelectionModel().getSelectedItem();
        System.out.println("Client sélectionné : " + c.getPrenom() + " " + c.getNom());
        id = c.getId();

        prenomtfd.setText(c.getPrenom());
        nomtfd.setText(c.getNom());
        emailtfd.setText(c.getEmail());
        passwordtfd.setText(c.getPassword());
        ajouterBtn.setDisable(true);
    }

}
