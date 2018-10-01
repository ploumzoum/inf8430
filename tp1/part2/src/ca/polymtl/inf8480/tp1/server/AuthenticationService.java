package ca.polymtl.inf8480.tp1.server;

import ca.polymtl.inf8480.tp1.shared.UserModel;

import java.io.*;
import java.util.List;

public class AuthenticationService {
    private List<UserModel> userList;

    public AuthenticationService() {
        try {
            FileInputStream fileIn = new FileInputStream("./Save/userList.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            try
            {
                userList = (List<UserModel>) in.readObject();
            }
            catch (ClassNotFoundException e)
            {
                System.err.println("Erreur: " + e.getMessage());
            }
            in.close();
            fileIn.close();

        } catch (IOException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
    }

    public void register(String username, String password) {
        if (isAvailable(username)) {
            UserModel newUser = new UserModel(username, password);
            userList.add(newUser);

            FileOutputStream fileOut = null;
            try {
                fileOut = new FileOutputStream("./Save/userList.ser");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            ObjectOutputStream out = null;
            try {
                out = new ObjectOutputStream(fileOut);
                out.writeObject(userList);
                out.close();
                fileOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Utilisateur: " + username + " enregistré.");
        } else {
            System.err.println("Erreur: Le nom d'utilisateur existe déjà.");
        }
    }

    public boolean verify(String username, String password) {
        for (UserModel user : userList
        ) {
            if (user._userName.equalsIgnoreCase(username) && user._password.equals(password)) {
                return true;
            }
        }

        return false;
    }

    private boolean isAvailable(String username) {
        for (UserModel user : userList
             ) {
            if (user._userName.equalsIgnoreCase(username)) {
                return false;
            }
        }

        return true;
    }
}
