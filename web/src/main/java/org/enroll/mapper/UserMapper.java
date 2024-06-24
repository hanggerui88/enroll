package org.enroll.mapper;

import org.apache.ibatis.annotations.Param;
import org.enroll.pojo.User;
import org.springframework.stereotype.Repository;


@Repository
public interface UserMapper {
    void insertUser(User user);

    boolean checkUsernameExists(@Param("username") String username);

    boolean checkEmailExists(@Param("email") String email);


    void updatePasswordByEmail(@Param("email") String email);

    String getPasswordByUsername(String username);

}

