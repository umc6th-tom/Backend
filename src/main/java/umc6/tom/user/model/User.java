package umc6.tom.user.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import umc6.tom.common.BaseEntity;
import umc6.tom.user.model.enums.Role;

import java.util.Collection;
import java.util.Collections;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20) // nullable = false 추가
    private String name;

    @Column(length = 20) // nullable = false 추가
    private String account;

    @Column(length = 50) // nullable = false 추가
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(15) DEFAULT 'USER'")
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
    }

    @Override
    public String getUsername() {
        return this.name;
    }

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
