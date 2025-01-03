package hu.pinterbeci.tms.annotations.processor;

import hu.pinterbeci.tms.annotations.ConstructNewTMSInstance;
import hu.pinterbeci.tms.annotations.TMSId;
import hu.pinterbeci.tms.errors.TMSException;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class TMSModelAnnotationProcessor {

    public static void initiateTMSIdField(final Object object) {
        Class<?> clazz = object.getClass();
        while (Objects.nonNull(clazz)) {

            final Optional<Field> fieldOpt = Arrays.stream(clazz.getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(TMSId.class))
                    .findFirst();

            fieldOpt.ifPresent(field -> {
                field.setAccessible(true);
                try {
                    if (Objects.isNull(field.get(object))) {
                        field.set(object, UUID.randomUUID().toString());
                    }
                } catch (final Exception exception) {
                    throw new TMSException(field.getName() + " field invoke during reflection, thrown exception", "INVALID_FIELD_INVOKE", exception);
                }
            });
            clazz = clazz.getSuperclass();
        }
    }

    public static void constructNewInstance(final Object object) {
        Class<?> clazz = object.getClass();
        Arrays.stream(clazz.getDeclaredAnnotations())
                .filter(annotation -> Objects.equals(annotation.getClass(), ConstructNewTMSInstance.class))
                .findFirst()
                .ifPresent(annotation -> {
                    try {
                        clazz.getDeclaredConstructors()[0].newInstance();
                    } catch (final Exception exception) {
                        throw new TMSException(clazz + " instance initialization during reflection, thrown an exception", "INVALID_CONSTRUCTOR_INVOKE", exception);
                    }
                });
    }
}