package nl.thijsalders.spigotproxy.haproxy;


public enum HAProxyCommand {
   /**
    * The LOCAL command represents a connection that was established on purpose by the proxy
    * without being relayed.
    */
   LOCAL(HAProxyConstants.COMMAND_LOCAL_BYTE),
   /**
    * The PROXY command represents a connection that was established on behalf of another node,
    * and reflects the original connection endpoints.
    */
   PROXY(HAProxyConstants.COMMAND_PROXY_BYTE);

   /**
    * The command is specified in the lowest 4 bits of the protocol version and command byte
    */
   private static final byte COMMAND_MASK = 0x0f;

   private final byte byteValue;

   /**
    * Creates a new instance
    */
   HAProxyCommand(byte byteValue) {
       this.byteValue = byteValue;
   }

   /**
    * Returns the {@link HAProxyCommand} represented by the lowest 4 bits of the specified byte.
    *
    * @param verCmdByte protocol version and command byte
    */
   public static HAProxyCommand valueOf(byte verCmdByte) {
       int cmd = verCmdByte & COMMAND_MASK;
       switch ((byte) cmd) {
           case HAProxyConstants.COMMAND_PROXY_BYTE:
               return PROXY;
           case HAProxyConstants.COMMAND_LOCAL_BYTE:
               return LOCAL;
           default:
               throw new IllegalArgumentException("unknown command: " + cmd);
       }
   }

   /**
    * Returns the byte value of this command.
    */
   public byte byteValue() {
       return byteValue;
   }
}