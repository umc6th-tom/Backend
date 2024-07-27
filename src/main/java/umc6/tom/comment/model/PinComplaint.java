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
public class PinComplaint extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pin_id")
    public Pin pin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User user;

    @Column(nullable = false, length = 200)
    public String content;

    @Column(nullable = false)
    private Long pinUserId;

    @Column(nullable = false, length = 200)
    public String pinComment;

    @OneToMany(mappedBy = "pinComplaint", cascade = CascadeType.ALL)
    public List<PinComplaintPicture> pinComplaintPictureList = new ArrayList<>();
}
