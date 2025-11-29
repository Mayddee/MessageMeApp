package org.example.messageme.security;


import org.example.messageme.entity.User;

public class JwtEntityFactory {

    public static JwtEntity generateUserDetails(User user) {
        return new JwtEntity(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getPassword(),
                user.getRoles()
        );

    }
}
