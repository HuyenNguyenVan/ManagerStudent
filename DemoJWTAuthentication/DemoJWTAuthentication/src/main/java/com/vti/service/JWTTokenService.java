package com.vti.service;

import javax.servlet.http.HttpServletResponse;

import com.vti.entity.Role;
import com.vti.entity.User;
import com.vti.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import java.util.*;

@Component
public class JWTTokenService {

    private static final long EXPIRATION_TIME = 864000000; // 10 days
        private static final String SECRET = "123456";
    private static final String PREFIX_TOKEN = "Bearer";
    private static final String AUTHORIZATION = "Authorization";

//    @Value("${bezkoder.app.jwtSecret}")
//    private String SECRET;

    private static UserRepository repository = null;

    public JWTTokenService(UserRepository repository) {
        this.repository = repository;
    }

    public static void addJWTTokenToHeader(HttpServletResponse response, String username) {
        String JWT = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        response.addHeader(AUTHORIZATION, PREFIX_TOKEN + " " + JWT);
    }


    public static Authentication parseTokenToUserInformation(HttpServletRequest request) {
//        String token = request.getHeader(AUTHORIZATION);
        String tokenCurrent = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");
        // Check user exists by username
        // parse the token

        System.out.println();
        String username = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(tokenCurrent.replace(PREFIX_TOKEN, ""))
                .getBody()
                .getSubject();

        //       Collection<? extends GrantedAuthority> nameRoles = new Collection();

        User userCurrent = repository.findByUserName(username);

        Set<Role> roles = new HashSet<Role>();

        if (userCurrent != null) {
            roles = userCurrent.getRoles().size() > 0 ? userCurrent.getRoles() : null;
        }
        List<GrantedAuthority> list = null;
        if (roles.size() > 0) {
            list = new ArrayList<>();

            for (Role role : roles) {
                list.add(new SimpleGrantedAuthority(role.getName()));
            }
        }


        if (tokenCurrent == null) {
            return null;
        }
        return username != null ?
                new UsernamePasswordAuthenticationToken(username, null, list) :
                null;
    }
}