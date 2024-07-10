package umc6.tom.user.model;

import jakarta.persistence.*;
import umc6.tom.common.model.BaseEntity;
import umc6.tom.user.model.enums.Gender;
import umc6.tom.user.model.enums.SocialType;

@Entity
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name") // nullable = false 추가
    private String name;

    @Column(name = "email") // nullable = false 추가
    private String email;

    @Column(name = "password") // nullable = false 추가
    private String password;
//
//    @Column(name = "socialType") // nullable = false 추가
//    private SocialType socialType;
//
//    @Column(name = "pic")
//    private String pic;
//
//    @Column(name = "gender")
//    private Gender gender;
}
