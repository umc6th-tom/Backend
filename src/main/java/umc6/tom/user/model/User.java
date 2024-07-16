package umc6.tom.user.model;

import jakarta.persistence.*;
import lombok.*;
import umc6.tom.common.BaseEntity;
import umc6.tom.user.model.enums.Gender;
import umc6.tom.user.model.enums.Major;
import umc6.tom.user.model.enums.SocialType;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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
/*

    @Column(name = "pic")
    private String pic;

    @Column(name = "point")
    private Integer point;

    @Enumerated(EnumType.STRING)
    @Column(name = "socialType") // nullable = false 추가
    private SocialType socialType;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "major")
    private Major major;
*/

}
