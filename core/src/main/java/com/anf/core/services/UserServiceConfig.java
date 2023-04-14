package com.anf.core.services;

import java.util.List;

public interface UserServiceConfig {
    public String getCountryCode();
    public String getJsonName();
    public String getJsonPath();
    public String getNodeName();
    public String getNodePath();
    public UserServiceConfig getCountryConfig(String countryCode);
    public List<UserServiceConfig> getAllConfigs();
}
