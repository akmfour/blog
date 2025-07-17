package com.suzumiya.blog.mapper;

import com.suzumiya.blog.dto.UserDto;
import com.suzumiya.blog.entity.UserEntity;

public class UserMapper {

    /**
     * 將 UserEntity 轉換為 UserDto
     * @param userEntity 資料庫實體物件
     * @return DTO 物件
     */
    public static UserDto toDto(UserEntity userEntity) {

        UserDto userDto = new UserDto();
        userDto.setId(userEntity.getUserId());
        userDto.setName(userEntity.getUsername());
        userDto.setEmail(userEntity.getEmail());

        return userDto;
    }
}
