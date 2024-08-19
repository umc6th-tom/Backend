package umc6.tom.gpt.dto;

import lombok.*;
import umc6.tom.gpt.model.Answer;
import umc6.tom.gpt.model.Example;
import umc6.tom.gpt.model.ExampleFavorite;

import java.util.List;

public class MajorRes {

    @Getter
    @ToString
    @NoArgsConstructor
    public static class SearchDto {
        private Long id;
        private String question;
        private String questionTag;
        private String content;
        private String timer;

    }

    @Getter
    @Setter
    @Builder
    public static class getHome{
        private Long userId;
        private Long answerId;
        private Long majorId;
        private String question;
        private String content;
        private String nickname;
        private String major;
        private String createdAt;

    }

    @Getter
    @Setter
    @Builder
    public static class ExampleResDto {

        private Long exampleId;
        private Long answerId;
        private Long favoriteId;
        private String problem;
        private String answer;
        private String tag;

    }


    @Getter
    @Setter
    public static class ExampleList {

        private List<Example> exampleList;
        private ExampleFavorite exampleFavorite;

        public ExampleList(List<Example> allById, ExampleFavorite favorite) {
            this.exampleList = allById;
            this.exampleFavorite = favorite;
        }
    }

    @Getter
    @Setter
    @Builder
    public static class ExampleDto {

        private Long id;
        private Answer answerEntity;
        private String problem;
        private String answer;
        private String tag;

    }
}
