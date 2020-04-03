package com.gramby.client.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    public User(User user) {
        this.setId(user.getId());
        this.setEmail(user.getEmail());
        this.setAge(user.getAge());
        this.setPermissions(user.getPermissions());
        this.setPassword(user.getPassword());
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    private Long id;
    @NotNull
    private String email;
    @NotNull
    private String password;
    private int age;
    private List<Permission> permissions = new ArrayList<>();

}
