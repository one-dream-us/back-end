package com.onedreamus.project.thisismoney.model.dto;

public interface OAuth2Response {

    String getProvider();
    String getProviderId();
    String getEmail();
    String getName();
    Long getSocialId();
}
