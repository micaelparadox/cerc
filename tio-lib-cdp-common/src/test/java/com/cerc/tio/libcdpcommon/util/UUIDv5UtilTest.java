package com.cerc.tio.libcdpcommon.util;

import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UUIDv5UtilTest {

    @Test
    void generateUUIDv5_shouldGenerateType5UUID() throws NoSuchAlgorithmException {
        String name = "namespace";
        String namespace = UUID.randomUUID().toString();

        UUID uuid = UUIDv5Util.generateUUIDv5(namespace, name);

        assertEquals(5, uuid.version());
    }

    @Test
    void generateUUIDv5_shouldGenerateSameUUIDForSameNameAndNamespace() throws NoSuchAlgorithmException {
        String name = "namespace";
        String namespace = UUID.randomUUID().toString();

        UUID uuid1 = UUIDv5Util.generateUUIDv5(namespace, name);
        UUID uuid2 = UUIDv5Util.generateUUIDv5(namespace, name);

        assertEquals(uuid1, uuid2);
    }

    @Test
    void generateUUIDv5_shouldBeJDKUUIDCompliant() throws NoSuchAlgorithmException {
        String name = "namespace";
        String namespace = UUID.randomUUID().toString();

        UUID uuid = UUIDv5Util.generateUUIDv5(namespace, name);

        assertDoesNotThrow(() -> UUID.fromString(uuid.toString()));
    }

    @Test
    void generateUUIDv5_shouldThrowIllegalArgumentException_whenNamespaceIsNotValidUUID() {
        String name = "namespace";
        String namespace = "not-a-uuid";

        assertThrows(IllegalArgumentException.class, () -> UUIDv5Util.generateUUIDv5(namespace, name));
    }

}
