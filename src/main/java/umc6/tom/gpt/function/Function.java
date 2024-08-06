package umc6.tom.gpt.function;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Function {

    //텍스트 블록 추출 메소드
    public static String extractContent(String content, String keyword) {
        Pattern pattern = Pattern.compile(keyword + "\\n(.*?)(?=(\\n\\n|\\z))", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "";
    }

}
