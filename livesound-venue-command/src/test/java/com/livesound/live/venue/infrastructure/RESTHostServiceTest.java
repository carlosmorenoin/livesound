package com.livesound.live.venue.infrastructure;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.livesound.live.core.User;
import com.livesound.live.venue.core.MockitoExtension;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;

@ExtendWith(MockitoExtension.class)
class RESTHostServiceTest {

	private static final String URL_PATTERN = "http://%s:%s/users/%s";
	private static final String IP = "127.0.0.1";
	private static final int PORT = 8080;

	@Mock
	private EurekaClient eurekaClient;

	@Mock
	private RestTemplate restTemplate;

	@Mock
	private Application application;

	@InjectMocks
	private RESTHostService restHostService;


	@BeforeEach void setUp() {
		InstanceInfo instanceInfo = Mockito.mock(InstanceInfo.class);
		List<InstanceInfo> list = new ArrayList<>();
		list.add(instanceInfo);
		when(instanceInfo.getIPAddr()).thenReturn(IP);
		when(instanceInfo.getPort()).thenReturn(PORT);
		when(eurekaClient.getApplication("live-users")).thenReturn(application);
		when(application.getInstances()).thenReturn(list);
	}

	@Test void emptyHostIdIsNotValid() {
		assertFalse(restHostService.isValidHostId(StringUtils.EMPTY, "token"));
	}

	@Test void isValidHostId() {
		String url = String.format(URL_PATTERN, IP, PORT, "1");
		when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(), eq(User.class))).thenReturn(ResponseEntity.ok().body(new User()));
		assertTrue(restHostService.isValidHostId("1", "token"));
		verify(restTemplate, timeout(1)).exchange(eq(url), eq(HttpMethod.GET), any(), eq(User.class));
	}
}
