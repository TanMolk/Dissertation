package uk.ac.ncl.c8099.wei.backend.controller.context;

/**
 * @author wei tan
 */
public class AddressContext {
    private static final ThreadLocal<String> tl = new ThreadLocal<>();

    public static String get() {
        if (tl.get() == null) {
            throw new RuntimeException("address context is null");
        }
        return tl.get();
    }

    public static void set(String address) {
        if (tl.get() != null) {
            tl.remove();
        }
        tl.set(address);
    }

    public static void clean() {
        try {
            if (tl.get() != null) {
                tl.remove();
            }
        } catch (Exception ignored) {
        }
    }


}
