package hexlet.code.config.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import hexlet.code.config.security.interfaces.ExtendUserDetails;
import hexlet.code.entity.User;
import hexlet.code.service.interfaces.AuthService;

@Component
public class TokenAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    private static final String ROLE = "USER";

    @Autowired
    private AuthService authService;

    @Override
    protected void additionalAuthenticationChecks(
            UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) {
    }

    @Override
    protected ExtendUserDetails retrieveUser(
            String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        String token = authentication.getCredentials().toString();
        return this.authService.findByToken(token)
                .map(this::buildUserDetails)
                .orElse(null);
    }

    private ExtendUserDetails buildUserDetails(User user) {
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(ROLE));
        return new UserDetailsImpl(authorities, user);
    }
}
