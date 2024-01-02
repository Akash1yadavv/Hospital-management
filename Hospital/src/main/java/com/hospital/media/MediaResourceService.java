/**
 * 
 */
package com.hospital.media;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.transaction.annotation.Transactional;

import com.hospital.media.MediaResource;

/**
 * 
 */
@Transactional
public interface MediaResourceService {
	MediaResource createMediaResource(MediaResource mediaResource);

	MediaResource updateMediaResource(MediaResource mediaResource);

	Void deleteMediaResource(String id);
	
	MediaResource getMediaResource(String id);

}
