package hexlet.code.config.security.interfaces;

import org.springframework.security.core.userdetails.UserDetails;

public interface ExtendUserDetails extends UserDetails {
    long getUserId();
}
