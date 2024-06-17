package com.cerc.tio.libcdpcommon.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UUIDv5Util {

    /**
     * Generate a UUIDv5 from a name and a namespace
     * @param namespace UUID string to use as namespace
     * @param name String to use as name to be hashed
     * @return UUIDv5 generated from the namespace and name
     * @throws NoSuchAlgorithmException if the SHA-1 algorithm is not available
     * @throws IllegalArgumentException if the namespace is not a valid UUID string
     */
    public static UUID generateUUIDv5(String namespace, String name) throws NoSuchAlgorithmException, IllegalArgumentException {
        if (!checkIfNamespaceIsUuidString(namespace)) {
            throw new IllegalArgumentException("Namespace must be a valid UUID string");
        }

        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        sha1.update(namespace.getBytes(StandardCharsets.UTF_8));
        sha1.update(name.getBytes(StandardCharsets.UTF_8));

        byte[] data = sha1.digest();
        data[6] &= (byte) 0x0f;
        data[6] |= (byte) 0x50;
        data[8] &= (byte) 0x3f;
        data[8] |= (byte) 0x80;

        long mostSigBits = 0L;
        long leastSigBits = 0L;

        for (int i = 0; i < 8; i++) {
            mostSigBits = (mostSigBits << 8) | (data[i] & 0xff);
        }

        for (int i = 8; i < 16; i++) {
            leastSigBits = (leastSigBits << 8) | (data[i] & 0xff);
        }

        return new UUID(mostSigBits, leastSigBits);
    }

    private static boolean checkIfNamespaceIsUuidString(String namespace) {
        try {
            UUID.fromString(namespace);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
