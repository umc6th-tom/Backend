package umc6.tom.user.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import umc6.tom.alarm.model.AlarmSet;
import umc6.tom.common.BaseEntity;
import umc6.tom.common.model.Majors;
import umc6.tom.user.model.enums.*;

import java.util.Collection;
import java.util.Collections;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20) // nullable = false 추가
    private String name;

    @Column(length = 20, nullable = false)
    private String nickName;

    @Column(length = 20, nullable = false) // nullable = false 추가
    private String account;

    @Column(nullable = false) // nullable = false 추가
    private String password;

    @Column(length = 11, nullable = false)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10) DEFAULT 'AGREE'")
    private Agreement agreement;

    @Column(length = 50)
    private String pic;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10) DEFAULT 'NON'")
    private SocialType socialType;

    private String description; // 디폴트 길이 255

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(15) DEFAULT 'USER'", length = 15)
    private Role role;

    @Column(length = 5)
    private Integer report;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, columnDefinition = "VARCHAR(10) DEFAULT 'INACTIVE'")
    private UserStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "majorId", nullable = false)
    private Majors majors;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Resign resign;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private AlarmSet alarmSet;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
    }

    @Override
    public String getUsername() {
        return this.name;
    }
}
