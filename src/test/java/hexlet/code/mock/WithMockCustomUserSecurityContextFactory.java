package hexlet.code.mock;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import hexlet.code.config.security.UserDetailsImpl;
import hexlet.code.entity.User;

public class WithMockCustomUserSecurityContextFactory
        implements WithSecurityContextFactory<WithMockCustomUser> {
    private static final String ROLE = "USER";
    private static final String EXIST_USER_EMAIL = "test@Email.com";

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        var context = SecurityContextHolder.createEmptyContext();
        var authorities = List.of(new SimpleGrantedAuthority(ROLE));
        var user = new User();
        user.setId(1);
        user.setEmail(EXIST_USER_EMAIL);
        var principal = new UserDetailsImpl(authorities, user);
        var auth = new UsernamePasswordAuthenticationToken(principal, "password", authorities);
        context.setAuthentication(auth);
        System.out.println("====================WithMockCustomUserSecurityContextFactory====================");
        return context;
    }
}
