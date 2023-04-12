package com.example.aem.demo.core.models.Impl;

import com.day.cq.wcm.api.Page;
import com.example.aem.demo.core.models.AuthorProfile;
import com.example.aem.demo.core.models.AuthorsInfo;
import com.example.aem.demo.core.service.AuthorService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Model(
        adaptables = SlingHttpServletRequest.class,
        adapters = UserProfile.class,
        resourceType = UserProfileImpl.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class UserProfileImpl implements UserProfile {
    private static final Logger LOG = LoggerFactory.getLogger(AuthorProfileImpl.class);
    final protected static String RESOURCE_TYPE="mydemo/components/custom/authorsinfo";

    @SlingObject
    ResourceResolver resourceResolver;

    @ScriptVariable
    Page currentPage;

    @OSGiService
    AuthorService authorService;

    @SlingObject
    SlingHttpServletRequest request;

    String firstName;
    String lastName;
    Integer age;
    String thumbnail;


    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public Integer getAge() {
        return age;
    }

    @Override
    public String getThumbnail() {
        return thumbnail;
    }

    @PostConstruct
    protected void init(){
        String country=currentPage.getPath().split("/")[3];
        String userPath=request.getRequestParameter("user").getString();
        Resource user=userService.getUserDetails(country,userPath);
        ValueMap userMap=author.getValueMap();
        firstName=userMap.get("fname",String.class);
        lastName=userMap.get("lname",String.class);
        age=userMap.get("age",Integer.class);
        thumbnail=author.getPath()+"/photo/image";
        LOG.info("\n Resource Path - {} : {} ",author.getPath(),books.size());
    }
}
