package com.hospital.service.interfaces;


import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.transaction.annotation.Transactional;

import com.hospital.model.Authority;

@Transactional
public interface AuthorityService {
    Optional<Authority> getAuthorityByAuthority(String authority);
    Optional<Authority> getAuthorityById(Long id);
	boolean hasAuthority(String authority);
	Set<String> getReachableGrantedAuthorities(Collection<? extends GrantedAuthority> grantedAuthorities);
	Set<String> getReachableGrantedAuthorities(String userId);
	Set<String> assignAuthorityToUser(String userId, String authority);
	Authority getAuthorityByAuthorityOrThrow(String authority);
	Set<String> removeAuthorityFromUser(String userId, String authority);
}
