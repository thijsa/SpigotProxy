package nl.thijsalders.spigotproxy;

import java.lang.reflect.Field;

public class ReflectionUtils {

    public static void setPrivateField(Class<?> objectClass, Object object, String fieldName, Object value) throws Exception {
        Field field = objectClass.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getPrivateField(Class<?> objectClass, Object object, Class<T> fieldClass, String fieldName) throws Exception {
        Field field = objectClass.getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(object);
    }

}
