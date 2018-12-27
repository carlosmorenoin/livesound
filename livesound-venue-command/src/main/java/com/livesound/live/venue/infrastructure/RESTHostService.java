package com.livesound.live.venue.infrastructure;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.livesound.live.core.User;
import com.livesound.live.venue.core.HostService;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;

@Service
public class RESTHostService implements HostService {

    private static final String URL_PATTERN = "http://%s:%s/users/%s";

    private EurekaClient eurekaClient;

    private RestTemplate restTemplate;

    public RESTHostService(final EurekaClient eurekaClient, final RestTemplate restTemplate) {
        this.eurekaClient = eurekaClient;
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean isValidHostId(final String hostId, final String token) {
        if (StringUtils.isEmpty(hostId)) {
            return false;
        }
        com.netflix.discovery.shared.Application application = eurekaClient.getApplication("live-users");
        InstanceInfo instanceInfo = application.getInstances().get(0);
        final String url = String.format(URL_PATTERN, instanceInfo.getIPAddr(), instanceInfo.getPort(), hostId);
        try {
            ResponseEntity<User> responseEntity = restTemplate.exchange(url, HttpMethod.GET, new AuthHttpEntity(token).getEntity(), User.class);
            return responseEntity.getBody() != null;
        } catch (HttpClientErrorException e) {
            return false;
        }
    }

}
