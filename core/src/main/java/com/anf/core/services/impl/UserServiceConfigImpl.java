package com.anf.core.services.impl;

import com.anf.core.config.CountriesConfig;
import com.anf.core.service.AuthorServiceConfig;
import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.*;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;


@Component (service = UserServiceConfig.class,configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate (ocd = CountriesConfig.class, factory = true)
public class UserServiceConfigImpl implements UserServiceConfig {
    private static final Logger LOG = LoggerFactory.getLogger(UserServiceConfigImpl.class);


    private String countryCode;
    private String jsonName;
    private String jsonPath;
    private String usersNodeName;
    private String usersNodePath;
    private List<UserServiceConfig> configs;

    @Activate
    @Modified
    protected void activate(final CountriesConfig config) {
        countryCode = config.countryCode();
        jsonName = config.jsonName();
        jsonPath = config.jsonPath();
        usersNodeName = config.usersNode();
        usersNodePath = config.usersPath();
    }

    @Reference(service = UserServiceConfig.class, cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    public void bindUserServiceConfig(final UserServiceConfig config) {
        if (configs == null) {
            configs = new ArrayList<>();
        }
        configs.add(config);

    }

    public void unbindUserServiceConfig(final UserServiceConfig config) {
        configs.remove(config);
    }

    @Override
    public String getCountryCode() {
        return countryCode;
    }

    @Override
    public String getJsonName() {
        return jsonName;
    }

    @Override
    public String getJsonPath() {
        return jsonPath;
    }

    @Override
    public String getNodeName() {
        return usersNodeName;
    }

    @Override
    public String getNodePath() {
        return usersNodePath;
    }

    @Override
    public List<UserServiceConfig> getAllConfigs() {
        return configs;
    }

    @Override
    public UserServiceConfig getCountryConfig(String countryCode) {
        for (UserServiceConfig confFact : configs) {
            if (StringUtils.equalsIgnoreCase(countryCode, confFact.getCountryCode())) {
                LOG.info("\n CONFIG SERVICE - {} ",confFact.getCountryCode());
                return confFact;
            }
        }
        return null;
    }
}
