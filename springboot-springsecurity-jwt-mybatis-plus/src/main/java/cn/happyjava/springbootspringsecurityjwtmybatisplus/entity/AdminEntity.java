package cn.happyjava.springbootspringsecurityjwtmybatisplus.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author happy
 */
@Data
@TableName(value = "admin")
public class AdminEntity {

    private Integer id;

    private String username;

    private String password;

}
