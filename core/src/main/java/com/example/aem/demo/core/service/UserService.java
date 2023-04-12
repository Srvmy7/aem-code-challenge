package com.example.aem.demo.core.service;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;

import java.util.List;
import java.util.Map;

public interface UserService {
    public String createUserNode(String country, SlingHttpServletRequest request);
    public List<Map<String, String>> getUsers(final String country);
    public Resource getUserDetails(final String country, final String user);
}
