package umc6.tom.comment.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CommentComplaintPicture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String pic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commentComplaint_id")
    private CommentComplaint commentComplaint;
}
