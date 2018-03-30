package com.gameapi.rha.models;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.apache.commons.codec.binary.Base64;


class Password {
  /**  The higher the number of iterations the more
   expensive computing the hash is for us and
   also for an attacker.
   */
  private static final int ITERATIONS = 20 * 1000;
  private static final int SALT_LEN = 32;
  private static final int DESIRED_KEY_LEN = 256;

  /** Computes a salted PBKDF2 hash of given plaintext password
   suitable for storing in a database.
   Empty passwords are not supported. */
  public static String getSaltedHash(String password) {
    try {
      final byte[] salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(SALT_LEN);
      return Base64.encodeBase64String(salt) + '$' + hash(password, salt);
    } catch (NoSuchAlgorithmException ex) {
      return null;
    }
  }


  /** Checks whether given plaintext password corresponds
  to a stored salted hash of the password. */
  public static boolean check(String password, String stored) {
    final String[] saltAndPass = stored.split("\\$");
    if (saltAndPass.length != 2) {
      throw new IllegalStateException(
      "The stored password have the form 'salt$hash'");
    }

    final String hashOfInput = hash(password, Base64.decodeBase64(saltAndPass[0]));
    return hashOfInput != null && hashOfInput.equals(saltAndPass[1]);
  }

  private static String hash(String password, byte[] salt) {
    if (password == null || password.isEmpty()) {
      throw new IllegalArgumentException("Empty passwords are not supported.");
    }
    try {
      final SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
      final SecretKey key = f.generateSecret(new PBEKeySpec(
                 password.toCharArray(), salt, ITERATIONS, DESIRED_KEY_LEN)
      );
      return Base64.encodeBase64String(key.getEncoded());
    } catch (IllegalArgumentException | NoSuchAlgorithmException | InvalidKeySpecException ex) {
      return null;
    }
  }
}
