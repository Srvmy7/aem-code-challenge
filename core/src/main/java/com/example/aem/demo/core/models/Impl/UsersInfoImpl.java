package com.example.aem.demo.core.models.Impl;

import com.day.cq.wcm.api.Page;
import com.example.aem.demo.core.models.AddAuthor;
import com.example.aem.demo.core.models.AuthorsInfo;
import com.example.aem.demo.core.service.AuthorService;
import com.example.aem.demo.core.service.UtilService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Model(
        adaptables = SlingHttpServletRequest.class,
        adapters = UsersInfo.class,
        resourceType = UsersInfoImpl.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class UsersInfoImpl implements UsersInfo {
    private static final Logger LOG = LoggerFactory.getLogger(UsersInfoImpl.class);
    final protected static String RESOURCE_TYPE="mydemo/components/custom/usersinfo";

    @SlingObject
    Resource resource;

    @ScriptVariable
    Page currentPage;

    @OSGiService
    AuthorService userService;


    private String country;

    @Override
    public List<Map<String, String>> getUsersList() {
        List<Map<String, String>> authos=userService.getUsers(country);
        return users;
    }

    @PostConstruct
    protected void init() throws LoginException {
       country=currentPage.getPath().split("/")[3];
    }

}
