package com.example.demo;

import com.example.demo.tools.Notification;
import com.example.demo.tools.Outils;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    private IUser userDao= new UserImple();
    @FXML
    private TextField emailtfd;

    @FXML
    private Button saveBtn;

    @FXML
    private PasswordField passwordtfd;

    @FXML
    void login(ActionEvent event) {
        String email = emailtfd.getText();
        String password = passwordtfd.getText();

        if(email.isEmpty() || password.isEmpty())
            Notification.NotifError("Erreur", "Veuillez remplir tous les champs !");

        else{
            User u=userDao.SeConnecter(email,password);

            if(u!=null){

                try {
                    Notification.NotifSuccess("Sucess", "Connexion reussie !");
                    Outils.load(event, "Panel Administrateur 🔨🔨🔨", "/Fxml/Administrateur.fxml");

                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                Notification.NotifError("Erreur", "Email et/ou mot de passe incorrects");

            }
        }
    }

    }

