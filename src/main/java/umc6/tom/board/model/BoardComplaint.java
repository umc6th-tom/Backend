package umc6.tom.board.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import umc6.tom.board.model.enums.BoardComplaintStatus;
import umc6.tom.common.BaseEntity;
import umc6.tom.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BoardComplaint extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(10) DEFAULT 'WAITING'")
    private BoardComplaintStatus status;

    @Column(nullable = false, length = 300)
    private String complaintContent;

    @Column(nullable = false, length = 50)
    private String boardTitle;

    @Column(nullable = false, length = 500)
    private String boardContent;

    @Column(nullable = false)
    private Long boardUserId;

    @OneToMany(mappedBy = "boardComplaint", cascade = CascadeType.ALL)
    private List<BoardComplaintPicture> boardComplaintPictureList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
