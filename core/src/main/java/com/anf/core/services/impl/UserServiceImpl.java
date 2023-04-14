package com.anf.core.services.impl;

import com.example.aem.demo.core.constants.Constants;
import com.example.aem.demo.core.service.AuthorService;
import com.example.aem.demo.core.service.AuthorServiceConfig;
import com.example.aem.demo.core.utils.ResolverUtil;
import com.example.aem.demo.core.utils.ServiceUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.Session;
import javax.jcr.ValueFactory;
import java.io.InputStream;
import java.util.*;

@Component(
        service = UserService.class,
        name = "UserService",
        immediate = true
)
public class UserServiceImpl implements UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    @Reference
    AuthorServiceConfig userServiceConfig;

    @Reference
    ResourceResolverFactory resourceResolverFactory;

    @Override
    public String createUserNode(String country, SlingHttpServletRequest request) {
       String nodeCreated= StringUtils.EMPTY;
       try {
           UserServiceConfig config = userServiceConfig.getCountryConfig(country);
           String nodeLocation = config.getNodePath() + "/" + config.getNodeName();
           ResourceResolver resourceResolver = ResolverUtil.newResolver(resourceResolverFactory);
           Session session=resourceResolver.adaptTo(Session.class);
           if(session.nodeExists(nodeLocation)){
               nodeCreated =addUser(session,request,nodeLocation);
           }else{
               addParentNode(session,config);
               nodeCreated=addUser(session,request,nodeLocation);
           }
       }catch (Exception e){
           LOG.error("\n Error while creating node - {} ",e.getMessage());
       }
        return nodeCreated;
    }

    @Override
    public List<Map<String, String>> getUsers(final String country) {
        final List<Map<String, String>> userList = new ArrayList<Map<String, String>>();
        userServiceConfig config = userServiceConfig.getCountryConfig(country);
        String nodeLocation = config.getNodePath() + "/" + config.getNodeName();
        try {
            ResourceResolver resolverResolver = ResolverUtil.newResolver(resourceResolverFactory);
            Iterator<Resource> users=resolverResolver.getResource(nodeLocation).listChildren();
            while (users.hasNext()){
                Resource resource=users.next();
                Map<String,String> user=new HashMap<>();
                ValueMap prop=resource.getValueMap();
                user.put("fname",ServiceUtil.getProprty(prop,"fname"));
                user.put("lname",ServiceUtil.getProprty(prop,"lname"));
                user.put("age",ServiceUtil.getProprty(prop,"age"));
                userList.add(user);
            }
        } catch (Exception e) {
            LOG.error("Occurred exception - {}", e.getMessage());
        }

        return userList;
    }

    @Override
    public Resource getUserDetails(final String country,final String user) {
        UserServiceConfig config = userServiceConfig.getCountryConfig(country);
        String nodeLocation = config.getNodePath() + "/" + config.getNodeName();
        try {
            ResourceResolver resolverResolver = ResolverUtil.newResolver(resourceResolverFactory);
            Resource userDetails=resolverResolver.getResource(nodeLocation+"/"+user);
            return userDetails;

        } catch (Exception e) {
            LOG.error("Occurred exception - {}", e.getMessage());
        }

        return null;
    }

    private String addUser(Session session,SlingHttpServletRequest request,String nodeLocation){
      try {
          Node parentNode = session.getNode(nodeLocation);
          if (!parentNode.hasNode(getNodeName(request))) {
              Node userNode = parentNode.addNode(getNodeName(request), Constants.AUTHORNODE_TYPE);
              userNode.setProperty("fname", ServiceUtil.getRequestParamter(request, "fname"));
              userNode.setProperty("lname", ServiceUtil.getRequestParamter(request, "lname"));
              userNode.setProperty("age", ServiceUtil.getRequestParamter(request, "age"));
              addThumbnail(userNode, request);
              session.save();
              return userNode.getName() + " added.";
          } else {
              return "This user already exists.";
          }
      }catch (Exception e){
          LOG.error("\n Error while creating User node ");
      }
      return null;
    }
    private String addParentNode(Session session,UserServiceConfig config){
        try {
            if(session.nodeExists(config.getNodePath())){
                Node gParentNode=session.getNode(config.getNodePath());
                Node parentNode=gParentNode.addNode(config.getNodeName(),Constants.AUTHORNODE_TYPE);
                session.save();
                return parentNode.getName();
            }
        }catch (Exception e){
            LOG.error("\n Error while creating Parent node ");
        }
        return null;
    }
    private String getNodeName(SlingHttpServletRequest request){
        String fName=request.getParameter("fname");
        String lName=request.getParameter("lname");
        String age=Integer.parseInt(request.getParameter("age"));
        String[] books=request.getParameter("books").split(",");
        String userNodeName=fName+"-"+lName+"-"+age;
        return userNodeName;
    }

    private boolean addThumbnail(Node node,SlingHttpServletRequest request){
        try {
            ResourceResolver resourceResolver = ResolverUtil.newResolver(resourceResolverFactory);
            RequestParameter rp = request.getRequestParameter("file");
            InputStream is = rp.getInputStream();
            Session session=resourceResolver.adaptTo(Session.class);
            ValueFactory valueFactory=session.getValueFactory();
            Binary imageBinary=valueFactory.createBinary(is);
            Node photo=node.addNode("photo","sling:Folder");
            Node file=photo.addNode("image","nt:file");
            Node content = file.addNode("jcr:content", "nt:resource");
            content.setProperty("jcr:mimeType", rp.getContentType());
            content.setProperty("jcr:data", imageBinary);
            return true;

        }catch (Exception e){
            LOG.info("\n ERROR while add Thumbnail - {} ",e.getMessage());
        }
        return false;
    }
}
