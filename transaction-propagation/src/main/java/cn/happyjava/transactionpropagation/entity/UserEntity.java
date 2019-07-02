package cn.happyjava.transactionpropagation.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author happy
 */
@Data
@Entity
@Table(name = "t_user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private String username;

    private String password;

}
