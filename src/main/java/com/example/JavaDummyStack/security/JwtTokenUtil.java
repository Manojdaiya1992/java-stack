package com.example.JavaDummyStack.security;

import java.security.Key;
import java.security.SignatureException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenUtil {

	// @Value("${jwt.secret}")
	private String secret = "$2y$12$eUovJt3tV2DxfQEspaPxcucmYcPYPBGGojGoiI1vt4Oic3hfm62w6";

	public Jws<Claims> getAllClaimsFromToken(String token) throws SignatureException, MalformedJwtException,
			ExpiredJwtException, UnsupportedJwtException, IllegalArgumentException {
		Key key = Keys.hmacShaKeyFor(secret.getBytes());
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
	}

	public String generateToken(UserDetails userDetails) {
		return doGenerateToken(userDetails);
	}

	public String generateToken(String username, Collection<GrantedAuthority> authorities) {
		return doGenerateToken(username, authorities);
	}

	private String doGenerateToken(UserDetails userDetails) {
		Key key = Keys.hmacShaKeyFor(secret.getBytes());
		long currentTimeInMillSeconds = System.currentTimeMillis();
		long expireTimeInMillSeconds = currentTimeInMillSeconds + (1000 * 60 * 60 * 24);
		Date date = new Date(currentTimeInMillSeconds);

		Map<String, Object> claimsMap = new HashMap<String, Object>();
		claimsMap.put("username", userDetails.getUsername());
		Claims claims = Jwts.claims(claimsMap);
		String jws = Jwts.builder().setClaims(claims).setIssuedAt(date).setExpiration(new Date(expireTimeInMillSeconds))
				.signWith(key).compact();
		return jws;
	}

	private String doGenerateToken(String username, Collection<GrantedAuthority> authorities) {
		Key key = Keys.hmacShaKeyFor(secret.getBytes());
		long currentTimeInMillSeconds = System.currentTimeMillis();
		long expireTimeInMillSeconds = currentTimeInMillSeconds + (1000 * 60);
		Date date = new Date(currentTimeInMillSeconds);

		Map<String, Object> claimsMap = new HashMap<String, Object>();
		claimsMap.put("username", username);
		claimsMap.put("role", authorities);
		Claims claims = Jwts.claims(claimsMap);
		String jws = Jwts.builder().setClaims(claims).setIssuedAt(date).setExpiration(new Date(expireTimeInMillSeconds))
				.signWith(key).compact();
		return jws;
	}

}
