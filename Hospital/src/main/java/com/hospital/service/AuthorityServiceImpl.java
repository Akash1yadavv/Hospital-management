package com.hospital.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.hospital.exception.NotFoundException;
import com.hospital.model.Authority;
import com.hospital.model.User;
import com.hospital.repo.AuthorityRepo;
import com.hospital.service.interfaces.AuthorityService;
import com.hospital.service.interfaces.UserService;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthorityServiceImpl implements AuthorityService{
    /**
     * @param authority
     * @return
     */

    @Autowired
    private AuthorityRepo authorityRepo;
    @Autowired UserService userService;
    @Autowired RoleHierarchy roleHierarchy;
    
    @Override
    public Optional<Authority> getAuthorityByAuthority(String authority) {
        return authorityRepo.findByAuthority(authority);
    }
    
    @Override
    public Authority getAuthorityByAuthorityOrThrow(String authority) {
        return authorityRepo.findByAuthority(authority).orElseThrow(()-> new NotFoundException("Authority not found: "+ authority));
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Optional<Authority> getAuthorityById(Long id) {
        return authorityRepo.findById(id);
    }
    
    
	

	@Override
	public boolean hasAuthority(String authority) {
		Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        Set<String> reachableGrantedAuthorities = new HashSet<>(roleHierarchy.getReachableGrantedAuthorities(authorities).stream().map(authoritie -> authoritie.getAuthority()).collect(Collectors.toSet()));
        return reachableGrantedAuthorities.contains(authority);
	}
	
	@Override
	public Set<String> getReachableGrantedAuthorities(Collection<? extends GrantedAuthority> grantedAuthorities) {
        Set<String> reachableGrantedAuthorities = new HashSet<>(roleHierarchy.getReachableGrantedAuthorities(grantedAuthorities).stream().map(authoritie -> authoritie.getAuthority()).collect(Collectors.toSet()));
        return reachableGrantedAuthorities;
		
	}

	@Override
	public Set<String> getReachableGrantedAuthorities(String userId) {
		User user = userService.getUserById(userId);
		return this.getReachableGrantedAuthorities(user.getAuthorities());
	}

	@Override
	public Set<String> assignAuthorityToUser(String userId, String authority) {
		Authority founAuthority = this.getAuthorityByAuthorityOrThrow(authority);
		var reachableGrantedAuthorities =  this.getReachableGrantedAuthorities(Arrays.asList(founAuthority));
		User user = userService.getUserByIdForUpdate(userId);
		for(String rga :reachableGrantedAuthorities) {
			user.getAuthorities().remove(this.getAuthorityByAuthorityOrThrow(rga));
		}
		user.getAuthorities().add(founAuthority);
		User updatedUser = userService.updateUser(user);
		
		return this.getReachableGrantedAuthorities(updatedUser.getAuthorities());
	}
	
	@Override
	public Set<String> removeAuthorityFromUser(String userId, String authority) {
		Authority founAuthority = this.getAuthorityByAuthorityOrThrow(authority);
		User user = userService.getUserByIdForUpdate(userId);
		user.getAuthorities().remove(founAuthority);
		User updatedUser = userService.updateUser(user);
		return this.getReachableGrantedAuthorities(updatedUser.getAuthorities());
	}
}
