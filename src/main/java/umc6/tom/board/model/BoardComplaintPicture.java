package umc6.tom.board.model;

import jakarta.persistence.*;
import lombok.*;
import umc6.tom.common.BaseEntity;
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BoardComplaintPicture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String pic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boardComplaint_id")
    private BoardComplaint boardComplaint;
}
