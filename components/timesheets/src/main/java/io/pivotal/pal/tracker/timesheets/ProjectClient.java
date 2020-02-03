package io.pivotal.pal.tracker.timesheets;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.LoggerFactory;
import org.springframework.util.SocketUtils;
import org.springframework.web.client.RestOperations;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class ProjectClient {

    private final RestOperations restOperations;
    private final String endpoint;
    //private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Map<Long, ProjectInfo> projectsCache = new ConcurrentHashMap<>();

    public ProjectClient(RestOperations restOperations, String registrationServerEndpoint) {
        this.restOperations = restOperations;
        this.endpoint = registrationServerEndpoint;
    }

    @HystrixCommand(fallbackMethod = "getProjectFromCache")
    public ProjectInfo getProject(long projectId) {
        return restOperations.getForObject(endpoint + "/projects/" + projectId, ProjectInfo.class);
    }

    public ProjectInfo getProjectFromCache(long projectId) {
        //logger.info("Getting project with id {} from cache", projectId);
        System.out.println("Getting project with id {} from cache"+ projectId);
        return projectsCache.get(projectId);
    }
}
