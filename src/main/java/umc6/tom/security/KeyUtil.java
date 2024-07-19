package umc6.tom.security;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;

public class KeyUtil {
    public static Key generateKey(String keyStr) {
        return new SecretKeySpec(keyStr.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    }
}
