package org.example.messageme.mapper;

import org.example.messageme.dto.UserDTO;
import org.example.messageme.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDto(User user);

    @Mapping(target = "roles", ignore = true)
    User toEntity(UserDTO userDTO);

}