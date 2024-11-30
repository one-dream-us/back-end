package com.onedreamus.project.bank.model.dto;

public interface OAuth2Response {

    String getProvider();
    String getProviderId();
    String getEmail();
    String getName();
    Long getSocialId();
}
