package com.hospital.dto.user;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.hospital.enums.AuthProvider;
import com.hospital.media.MediaResource;
import com.hospital.model.Authority;
import com.hospital.model.Department;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CurrentUserResDto {
    private String id;
    private String name;
    private String email;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private boolean emailVerified;
    private AuthProvider authProvider;
    private String authProviderId;
    private Instant createdOn;	
    private Instant lastUpdatedOn;
    private MediaResource image;
    private boolean blocked;
    private List<Authority> authorities = new ArrayList<>();
    private List<Department> departments = new ArrayList<Department>();
    
    
	public CurrentUserResDto(String id, String name, String email, boolean accountNonExpired,
			boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled, boolean emailVerified,
			AuthProvider authProvider, String authProviderId, Instant createdOn, Instant lastUpdatedOn, MediaResource image,
			boolean blocked) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.accountNonExpired = accountNonExpired;
		this.accountNonLocked = accountNonLocked;
		this.credentialsNonExpired = credentialsNonExpired;
		this.enabled = enabled;
		this.emailVerified = emailVerified;
		this.authProvider = authProvider;
		this.authProviderId = authProviderId;
		this.createdOn = createdOn;
		this.lastUpdatedOn = lastUpdatedOn;
		this.image = image;
		this.blocked = blocked;
	}

}
