package hu.pinterbeci.tms.service;

import hu.pinterbeci.tms.annotations.TMSAllowedRoles;
import hu.pinterbeci.tms.enums.Role;
import hu.pinterbeci.tms.errors.TMSException;
import hu.pinterbeci.tms.model.BaseModel;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class TMSReflectionCallService<E extends BaseModel, S extends BaseService<E>> {
    private final S serviceInstance;

    public TMSReflectionCallService(final S service) {
        this.serviceInstance = service;
    }

    public Object invoke(final String methodName, final Object[] params, final Role callerRole) {
        return invokeServiceMethod(methodName, params, callerRole);
    }

    private Object invokeServiceMethod(final String methodName, final Object[] params, final Role callerRole) {
        Class<?> clazz = serviceInstance.getClass();
        try {
            while (Objects.nonNull(clazz)) {
                final Optional<Method> methodOpt = Arrays.stream(clazz.getDeclaredMethods())
                        .filter(method -> Objects.equals(method.getName(), methodName) &&
                                Objects.equals(method.getParameterCount(), params.length))
                        .findFirst();

                if (methodOpt.isPresent()) {
                    methodOpt.get().setAccessible(true);
                    return methodInvokeWithTSMRole(methodOpt.get(), params, callerRole);
                }
                clazz = clazz.getSuperclass();
            }
            return null;
        } catch (final Exception exception) {
            throw new TMSException("Exception occurred during method invoke!", "METHOD_INVOKE_ERROR", exception);
        }
    }

    private Object methodInvokeWithTSMRole(final Method method, final Object[] params, final Role callerRole) {
        try {
            final TMSAllowedRoles allowedRoles = method.getAnnotation(TMSAllowedRoles.class);
            if (Objects.isNull(allowedRoles)) {
                throw new TMSException("Method " + method.getName() + " is not annotated with @TMSAllowedRoles");
            }

            final boolean hasRoleToCallMethod = Arrays.stream(allowedRoles.value()).anyMatch(role -> Objects.equals(role.name(), callerRole.name()));

            if (!hasRoleToCallMethod) {
                throw new TMSException("The User has not role to call the method!");
            }
            return method.invoke(serviceInstance, params);
        } catch (final Exception exception) {
            throw new TMSException("Exception occurred during method invoke!", "METHOD_INVOKE_ERROR", exception);
        }
    }
}
