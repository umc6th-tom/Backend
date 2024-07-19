package umc6.tom.common.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class College {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = false, length = 30)
    private String college;

    @OneToMany(mappedBy = "college", cascade = CascadeType.ALL)
    private List<Majors> majorsList = new ArrayList<>();
}
