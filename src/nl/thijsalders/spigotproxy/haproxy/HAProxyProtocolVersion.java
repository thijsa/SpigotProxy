package nl.thijsalders.spigotproxy.haproxy;

import static nl.thijsalders.spigotproxy.haproxy.HAProxyConstants.*;

public enum HAProxyProtocolVersion {
    /**
     * The ONE proxy protocol version represents a version 1 (human-readable) header.
     */
    V1(VERSION_ONE_BYTE),
    /**
     * The TWO proxy protocol version represents a version 2 (binary) header.
     */
    V2(VERSION_TWO_BYTE);

    /**
     * The highest 4 bits of the protocol version and command byte contain the version
     */
    private static final byte VERSION_MASK = (byte) 0xf0;

    private final byte byteValue;

    /**
     * Creates a new instance
     */
    HAProxyProtocolVersion(byte byteValue) {
        this.byteValue = byteValue;
    }

    /**
     * Returns the {@link HAProxyProtocolVersion} represented by the higest 4 bits of the specified byte.
     *
     * @param verCmdByte protocol version and command byte
     */
    public static HAProxyProtocolVersion valueOf(byte verCmdByte) {
        int version = verCmdByte & VERSION_MASK;
        switch ((byte) version) {
            case VERSION_TWO_BYTE:
                return V2;
            case VERSION_ONE_BYTE:
                return V1;
            default:
                throw new IllegalArgumentException("unknown version: " + version);
        }
    }

    /**
     * Returns the byte value of this version.
     */
    public byte byteValue() {
        return byteValue;
    }
}