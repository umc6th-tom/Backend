package umc6.tom.user.model;

import jakarta.persistence.*;
import lombok.*;
import umc6.tom.user.model.mapping.ProhibitBoard;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Prohibit {

    @Id
    private Long userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String division;

    @OneToMany(mappedBy = "prohibit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProhibitBoard> prohibitBoards = new ArrayList<>();

    @Column
    private LocalDateTime suspensionDue;
}