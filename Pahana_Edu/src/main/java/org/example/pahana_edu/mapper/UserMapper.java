package org.example.pahana_edu.mapper;

import org.example.pahana_edu.dto.UserRegistrationDTO;
import org.example.pahana_edu.dto.UserResponseDTO;
import org.example.pahana_edu.model.UserModel;

public class UserMapper {

    public static UserModel toEntity(UserRegistrationDTO dto) {
        if (dto == null) {
            return null;
        }

        return new UserModel(
                dto.getUsername(),
                dto.getEmail(),
                dto.getPassword(), // Note: Password should be hashed before this point
                dto.getFirstName(),
                dto.getLastName()
        );
    }

    public static UserResponseDTO toResponseDTO(UserModel user) {
        if (user == null) {
            return null;
        }

        return new UserResponseDTO(
                Math.toIntExact(user.getId()),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName()
        );
    }

    public static UserModel updateEntityFromDTO(UserModel existingUser, UserRegistrationDTO dto) {
        if (existingUser == null || dto == null) {
            return existingUser;
        }

        existingUser.setUsername(dto.getUsername());
        existingUser.setEmail(dto.getEmail());
        existingUser.setFirstName(dto.getFirstName());
        existingUser.setLastName(dto.getLastName());

        return existingUser;
    }
}