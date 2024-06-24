package org.enroll.pojo;

import lombok.*;


@ToString
@NoArgsConstructor
@Data
@Getter
@Setter
public class User {

    private String username;

    private String password;

    private String email;

}