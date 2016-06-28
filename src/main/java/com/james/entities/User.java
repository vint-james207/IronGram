package com.james.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by jamesyburr on 6/28/16.
 */

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    int id;

    @NotNull
    String name;

    @NotNull
    String password;

    public User() {
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String username) {
        this.name = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
