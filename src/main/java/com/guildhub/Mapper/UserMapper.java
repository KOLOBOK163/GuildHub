package com.guildhub.Mapper;

import com.guildhub.Dto.UserDto;
import com.guildhub.Entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    public User UserDtoToUserPojo(UserDto userCreateDto);

    @Mapping(target = "password", ignore = true)
    public UserDto UserPojoToUserDto(User user);

}
