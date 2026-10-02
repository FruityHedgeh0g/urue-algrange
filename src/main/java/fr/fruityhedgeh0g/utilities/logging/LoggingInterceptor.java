package fr.fruityhedgeh0g.utilities.logging;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import org.jboss.logging.Logger;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

/**
 * Remplace les anciens décorateurs *LogProxy : une seule politique de journalisation
 * pour tous les services, sans implémentation à maintenir par méthode.
 * Le logger porte le nom de la classe du service pour conserver les catégories de log.
 */
@Logged
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
public class LoggingInterceptor {

    @AroundInvoke
    Object log(InvocationContext context) throws Exception {
        Class<?> service = context.getMethod().getDeclaringClass();
        Logger log = Logger.getLogger(service);
        String operation = service.getSimpleName() + "." + context.getMethod().getName();

        log.debugf("Calling %s%s.", operation, Arrays.toString(context.getParameters()));
        try {
            Object result = context.proceed();
            log.debugf("%s succeeded%s.", operation, describe(result));
            return result;
        } catch (Exception e) {
            log.errorf(e, "%s failed: %s", operation, e.getMessage());
            throw e;
        }
    }

    static String describe(Object result) {
        return switch (result) {
            case null -> "";
            case Collection<?> c -> " with " + c.size() + " element(s)";
            case Optional<?> o -> o.isPresent() ? " with a result" : " with no result";
            default -> "";
        };
    }
}
