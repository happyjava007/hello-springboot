package cn.happyjava.springbootsecurity.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserEntity {

    private Integer id;

    private String username;

    private String password;


}
