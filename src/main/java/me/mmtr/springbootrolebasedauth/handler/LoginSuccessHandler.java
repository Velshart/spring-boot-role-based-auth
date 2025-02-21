package me.mmtr.springbootrolebasedauth.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        Map<String, String> roleRedirects = Map.of(
                "ROLE_ADMIN", "/admin/home",
                "ROLE_USER", "/user/home"
        );

        for (Map.Entry<String, String> entry : roleRedirects.entrySet()) {
            if (roles.contains(entry.getKey())) {
                response.sendRedirect(entry.getValue());
                break;
            }
        }
    }
}
