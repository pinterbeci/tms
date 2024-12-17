package hu.pinterbeci.tms.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.UUID;

public class TMSAnnotationProcessor {
    public static void initiateTMSIdField(final Object object) {
        Class<?> clazz = object.getClass();
        while (Objects.nonNull(clazz)) {
            for (final Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(TMSId.class)) {
                    try {
                        field.setAccessible(true);
                        if (Objects.isNull(field.get(object))) {
                            field.set(object, UUID.randomUUID().toString());
                        }
                    } catch (final IllegalAccessException exception) {
                        exception.printStackTrace();
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
    }

    public static void constructNewInstance(final Object object) {
        Class<?> clazz = object.getClass();
        for (Annotation declaredAnnotation : clazz.getDeclaredAnnotations()) {
            if (Objects.equals(declaredAnnotation.getClass(), ConstructNewTMSInstance.class)) {
                try {
                    clazz.getDeclaredConstructors()[0].newInstance();
                    return;
                } catch (final java.lang.InstantiationException |
                               java.lang.IllegalAccessException |
                               java.lang.reflect.InvocationTargetException exception
                ) {
                    exception.printStackTrace();
                    return;
                }
            }
        }
    }
}