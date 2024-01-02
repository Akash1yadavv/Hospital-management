/**
 * 
 */
package com.hospital.media;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * Ankit Patel
 */


@ConfigurationProperties(prefix = "file")
@Data
public class FileStorageProperties {
    private String uploadDir;
    private String profileImageDir;

}
