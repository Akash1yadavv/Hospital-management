package com.hospital.config.oauth.oauth2;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.hospital.config.oauth.UserPrincipal;
import com.hospital.config.oauth.oauth2.user.OAuth2UserInfo;
import com.hospital.config.oauth.oauth2.user.OAuth2UserInfoFactory;
import com.hospital.enums.AuthProvider;
import com.hospital.exception.OAuth2AuthenticationProcessingException;
import com.hospital.model.User;
import com.hospital.service.interfaces.AuthService;
import com.hospital.service.interfaces.AuthorityService;
import com.hospital.service.interfaces.EmailService;
import com.hospital.service.interfaces.UserService;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired private UserService userService;
    @Autowired private EmailService emailService;
    @Autowired private AuthorityService authoritySetvice;
    @Autowired private AuthService authService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        
        if(oAuth2UserInfo.getEmail() == null) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }
        
        Optional<User> userOptioal = userService.getUserByEmail(oAuth2UserInfo.getEmail());
                 
        User user;
        if(userOptioal.isPresent()) {
            user = userOptioal.get();
            if(!user.getAuthProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        user.getAuthProviderId() + " account. Please use your " + user.getAuthProvider() +
                        " account to login.");
            }
            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

   
    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
    	 if(!oAuth2UserInfo.emailVerified()) {
    		 throw new OAuth2AuthenticationProcessingException("Please, verify your email with "+oAuth2UserRequest.getClientRegistration().getRegistrationId()+". And try again.");
    	 }
        User user = new User();
        user.setAuthProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        user.setAuthProviderId(oAuth2UserInfo.getId());
        user.setName(oAuth2UserInfo.getName());
        user.setEmailVerified(oAuth2UserInfo.emailVerified());
        user.setEmail(oAuth2UserInfo.getEmail());
        
        return authService.signup(user);
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
   	 if(!oAuth2UserInfo.emailVerified()) {
		 throw new OAuth2AuthenticationProcessingException("Please, verify your email with "+existingUser.getAuthProvider()+". And try again.");
	 }
        existingUser.setName(oAuth2UserInfo.getName());
        existingUser.setEmailVerified(oAuth2UserInfo.emailVerified());
        return userService.updateUser(existingUser);
    }

}
