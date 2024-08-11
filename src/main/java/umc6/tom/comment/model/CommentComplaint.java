package umc6.tom.comment.model;

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
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
public class CommentComplaint extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    public Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(10) DEFAULT 'WAITING'")
    private BoardComplaintStatus status;

    @Column(nullable = false, length = 200)
    public String content;

    @Column(nullable = false)
    private Long commentUserId;

    @Column(nullable = false, length = 200)
    public String commentComment;

    @OneToMany(mappedBy = "commentComplaint", cascade = CascadeType.ALL)
    public List<CommentComplaintPicture> commentComplaintPictureList = new ArrayList<>();
}
