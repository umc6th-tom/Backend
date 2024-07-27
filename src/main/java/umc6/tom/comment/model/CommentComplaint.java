package umc6.tom.comment.model;

import jakarta.persistence.*;
import lombok.*;
import umc6.tom.common.BaseEntity;
import umc6.tom.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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

    @Column(nullable = false, length = 200)
    public String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User commentUser;

    @Column(nullable = false, length = 200)
    public String commentComment;

    @OneToMany(mappedBy = "commentComplaint", cascade = CascadeType.ALL)
    public List<CommentComplaintPicture> commentComplaintPictureList = new ArrayList<>();
}
