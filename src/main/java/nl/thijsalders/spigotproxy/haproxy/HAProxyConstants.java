package nl.thijsalders.spigotproxy.haproxy;

final class HAProxyConstants {

    /**
     * Command byte constants
     */
    static final byte COMMAND_LOCAL_BYTE = 0x00;
    static final byte COMMAND_PROXY_BYTE = 0x01;

    /**
     * Version byte constants
     */
    static final byte VERSION_ONE_BYTE = 0x10;
    static final byte VERSION_TWO_BYTE = 0x20;

    /**
     * Transport protocol byte constants
     */
    static final byte TRANSPORT_UNSPEC_BYTE = 0x00;
    static final byte TRANSPORT_STREAM_BYTE = 0x01;
    static final byte TRANSPORT_DGRAM_BYTE = 0x02;

    /**
     * Address family byte constants
     */
    static final byte AF_UNSPEC_BYTE = 0x00;
    static final byte AF_IPV4_BYTE = 0x10;
    static final byte AF_IPV6_BYTE = 0x20;
    static final byte AF_UNIX_BYTE = 0x30;

    /**
     * Transport protocol and address family byte constants
     */
    static final byte TPAF_UNKNOWN_BYTE = 0x00;
    static final byte TPAF_TCP4_BYTE = 0x11;
    static final byte TPAF_TCP6_BYTE = 0x21;
    static final byte TPAF_UDP4_BYTE = 0x12;
    static final byte TPAF_UDP6_BYTE = 0x22;
    static final byte TPAF_UNIX_STREAM_BYTE = 0x31;
    static final byte TPAF_UNIX_DGRAM_BYTE = 0x32;

    private HAProxyConstants() { }
}
