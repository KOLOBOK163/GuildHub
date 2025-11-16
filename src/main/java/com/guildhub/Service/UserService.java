package com.guildhub.Service;

import com.guildhub.Dto.UserDto;
import com.guildhub.Entity.User;


import java.util.List;

public interface UserService {
    UserDto create(UserDto userCreateDto);

    void delete(Long userId);

    UserDto update(Long userId, UserDto userUpdateDto);

    List<UserDto> getAllUsers();

    User getUserById(Long userId);

    void updateAvatar(String newAvatarPath);

    String getAvatar(Long userId);
}
