package pl.selfcloud.chat.domain.model;

import jakarta.annotation.PostConstruct;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UUIDGenerator {

  @Value("${selfcloud-chat.uuid.salt}")
  private String salt;

  private static String staticSalt;

  @PostConstruct
  public void init() {
    staticSalt = salt;
  }
  public static UUID generateUUID(String str1, String str2) throws NoSuchAlgorithmException {
    // Sortuj stringi, aby zapewnić deterministyczny wynik
    String[] strings = {str1, str2};
    Arrays.sort(strings);

    // Połącz stringi
    String combined = strings[0] + strings[1] + staticSalt;

    // Generuj hash z połączonego stringa
    MessageDigest md = MessageDigest.getInstance("SHA-1");
    byte[] hashBytes = md.digest(combined.getBytes());

    // Użyj pierwszych 16 bajtów hasha do utworzenia UUID
    long mostSigBits = 0;
    long leastSigBits = 0;
    for (int i = 0; i < 8; i++) {
      mostSigBits = (mostSigBits << 8) | (hashBytes[i] & 0xFF);
    }
    for (int i = 8; i < 16; i++) {
      leastSigBits = (leastSigBits << 8) | (hashBytes[i] & 0xFF);
    }

    // Wygeneruj UUID z most significant bits i least significant bits
    return new UUID(mostSigBits, leastSigBits);
  }

}