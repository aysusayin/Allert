package aysusayin.com.allert;

import java.util.ArrayList;

/**
 * Created by Aysu on 6.09.2017.
 */

public class User {
    private String username;
    private String email;
    private String allergens;
    public ArrayList<Product> blackList = new ArrayList<>();
    public ArrayList<Product> favorites = new ArrayList<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User() {
    }

    public User(String username, String email, String allergens) {
        this.username = username;
        this.email = email;

        this.allergens = allergens;
    }

    public String getAllergens() {
        return allergens;
    }

    public void setAllergens(String allergens) {
        this.allergens = allergens;
    }


}
