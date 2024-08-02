package umc6.tom.inquiry.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import umc6.tom.board.model.BoardPicture;
import umc6.tom.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column
    public Long adminUserId;

    @Column(nullable = false, length = 50)
    public String title;

    @Column(nullable = false, length = 300)
    public String content;

    @Column(length = 300)
    public String answer;

    @CreatedDate
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime answeredAt;

    @OneToMany(mappedBy = "inquiry", cascade = CascadeType.ALL)
    private List<InquiryPicture> inquiryPictureList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
