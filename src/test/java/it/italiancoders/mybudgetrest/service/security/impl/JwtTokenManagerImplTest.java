package it.italiancoders.mybudgetrest.service.security.impl;

import io.jsonwebtoken.ExpiredJwtException;
import it.italiancoders.mybudgetrest.dao.BlackListRefreshTokenDao;
import it.italiancoders.mybudgetrest.exception.security.RestException;
import it.italiancoders.mybudgetrest.model.dto.UserRole;
import it.italiancoders.mybudgetrest.security.JwtUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@RunWith(SpringRunner.class)
@SpringBootTest
//@TestPropertySource(locations="classpath:application.properties")
public class JwtTokenManagerImplTest {

    @InjectMocks
    JwtTokenManagerImpl jwtTokenManager;

    @Mock
    private BlackListRefreshTokenDao blackListRefreshTokenDao;

    @Mock
    private UserDetailsService userDetailsService;

    private JwtUser userA;
    private String userAccessTokenA;
    private JwtUser userB;
    private String userAccessTokenB;
    private String expiredAccessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsIm1haWwiOiJhZG1pbkBnbWFpbC5jb20iLCJyb2xlcyI6WyJWSUVXX0JVREdFVCIsIkRFTEVURV9FWFBFTlNFIiwiQUREX0VYUEVOU0UiXSwiaXNFbmFibGVkIjp0cnVlLCJleHAiOjE1NjUxOTI3MTYsImlhdCI6MTU2NTE4NTUxNjEwMn0.aUcCiDzPpj8UZKCp_b-OAuXo49mP0URFJYiY-YsDVEg";
    private String expiredRefreshToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTU5NjgxNjEwMywiaWF0IjoxNTY1MjgwMTAzNDM5fQ.C8KMAzyVMLXlRw5Cb2iNVjeHhDhBRculKZqH3eUVQeg";
    private String invalidAccessToken = "eyJhbGciOiJIUzI1NiJ9.x.aUcCiDzPpj8UZKCp_b-OAuXo49mP0URFJYiY-YsDVEg";
    private String userRefreshTokenA;
    private String userRefreshTokenB;

    @Value("${jwt.accessToken.expiration}")
    private Long accessTokenExpiration;

    @Value("${jwt.refreshToken.expiration}")
    private Long refreshTokenExpiration;

    @Value("${jwt.secret}")
    private String secret;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(userDetailsService.loadUserByUsername("admin")).thenReturn(this.userA);
        Mockito.when(userDetailsService.loadUserByUsername("PIPPO")).thenReturn(this.userB);
        doReturn(null).when(blackListRefreshTokenDao).findByRefreshToken(any());

        ReflectionTestUtils.setField(jwtTokenManager, "accessTokenExpiration", accessTokenExpiration);
        ReflectionTestUtils.setField(jwtTokenManager, "refreshTokenExpiration", refreshTokenExpiration);
        ReflectionTestUtils.setField(jwtTokenManager, "secret", secret);

        this.userA = JwtUser.newBuilderExt()
                .username("admin")
                .email("admin@gmail.com")
                .password("admin")
                .roles(Collections.singletonList(UserRole.VIEW_BUDGET))
                .build();

        this.userAccessTokenA = jwtTokenManager.generateAccessToken(this.userA);
        this.userRefreshTokenA = jwtTokenManager.generateRefreshToken(this.userA);

        this.userB = JwtUser.newBuilderExt()
                .username("PIPPO")
                .email("PIPPO@gmail.com")
                .password("PIPPO")
                .roles(Collections.singletonList(UserRole.VIEW_BUDGET))
                .build();


        this.userAccessTokenB = jwtTokenManager.generateAccessToken(this.userB);
        this.userRefreshTokenB = jwtTokenManager.generateRefreshToken(this.userB);


    }

    @Test
    public void testValidateAccessTokenWithValidAccessToken() {
        try {
            jwtTokenManager.validateAccessToken(userAccessTokenA, userA);
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testValidateAccessTokenWithInvalidAccessToken() {
        try {
            jwtTokenManager.validateAccessToken(invalidAccessToken, userA);
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testValidateAccessTokenWithTokenOfOtherUser() {
        try {
            jwtTokenManager.validateAccessToken(userAccessTokenB, userA);
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }
    }


    @Test
    public void testValidateAccessTokenWithExpiredAccessToken() {
        try {
            jwtTokenManager.validateAccessToken(expiredAccessToken, userA);
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }
    }
    @Test
    public void testGetCreatedDateFromTokenIfValid() {
        try {
            Date date = new Date();
            Date tokenDate = jwtTokenManager.getCreatedDateFromToken(userAccessTokenA);
            boolean isSomeDate = date.getYear() == tokenDate.getYear()
                    && date.getYear() == tokenDate.getYear()
                    && date.getMonth() == tokenDate.getMonth()
                    && date.getDay() == tokenDate.getDay()
                    && date.getHours() == tokenDate.getHours()
                    && date.getMinutes() == tokenDate.getMinutes();
            assertTrue(isSomeDate);
        } catch (Exception e) {
            fail();
        }
    }
    @Test
    public void testGetCreatedDateFromTokenIfTokenInvalid() {
        assertNull(jwtTokenManager.getCreatedDateFromToken(invalidAccessToken));
    }

    @Test
    public void testGetExpirationDateFromTokenIfTokenInvalid() {
        assertNull(jwtTokenManager.getExpirationDateFromToken(invalidAccessToken));
    }

    @Test
    public void testGetExpirationDateFromTokenIfExpired() {
        assertNull(jwtTokenManager.getExpirationDateFromToken(expiredAccessToken));
    }


    @Test
    public void testGetExpirationDateFromTokenIfValid() {

        Date expirationDate = jwtTokenManager.getExpirationDateFromToken(userAccessTokenA);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR_OF_DAY, (int) (accessTokenExpiration /3600));
        long diff = cal.getTime().getTime() - expirationDate.getTime();
        assertTrue(diff / 1000 % 60 < 1);
    }

    @Test
    public void testGenerateAccessTokenFromValidRefreshToken() {
        try {
            Mockito.when(userDetailsService.loadUserByUsername("admin")).thenReturn(this.userA);
            String accessToken = jwtTokenManager.generateAccessTokenByRefreshToken(this.userRefreshTokenA);
            assertNotNull(accessToken);
        } catch (Exception ex) {
            fail();
        }

    }

    @Test
    public void testGenerateRefreshTokenWithValidUserDetails() {
        String refreshToken = jwtTokenManager.generateRefreshToken(userA);
        assertNotNull(refreshToken);

    }
    @Test
    public void testGetUserDetailsWithValidToken() {
        try {
            JwtUser user = (JwtUser) jwtTokenManager.getUserDetails(userAccessTokenA).orElse(null);
            assertEquals(user, this.userA);
        } catch (ExpiredJwtException e) {
            fail();
        }

    }

    @Test
    public void testGetUserDetailsWithExpireddToken() {
        try {
            JwtUser user = (JwtUser) jwtTokenManager.getUserDetails(expiredAccessToken).orElse(null);
            fail();
        } catch (ExpiredJwtException e) {
            assertTrue(true);
        }

    }

}
