package fr.fruityhedgeh0g.interceptors;

import fr.fruityhedgeh0g.interceptors.bindings.EnforceAuthentification;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@Interceptor
@EnforceAuthentification
public class AuthEnforcerInterceptor {


    @AroundInvoke
    public void intercept(InvocationContext context) {

    }

}
