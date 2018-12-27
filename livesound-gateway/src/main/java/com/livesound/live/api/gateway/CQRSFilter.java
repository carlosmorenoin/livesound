package com.livesound.live.api.gateway;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PROXY_KEY;

import java.util.function.Predicate;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class CQRSFilter extends ZuulFilter {

	@Autowired
	private ZuulConfig zuulConfig;

	@Override public String filterType() {
		return PRE_TYPE;
	}

	@Override public int filterOrder() {
		return 5 + 1;
	}

	@Override public boolean shouldFilter() {
		final RequestContext ctx = RequestContext.getCurrentContext();
		final String proxy = (String) ctx.get(PROXY_KEY);

		return zuulConfig.getRoutes().containsKey(proxy) &&
				zuulConfig.getRoutes().get(proxy).isCqrs()
				&& !zuulConfig.getRoutes().get(proxy).getServices().isEmpty();
	}

	@Override public Object run() {
		final RequestContext ctx = RequestContext.getCurrentContext();
		final HttpServletRequest request = ctx.getRequest();

		final String proxy = (String) ctx.get(PROXY_KEY);
		final String method = request.getMethod();
		final String path = zuulConfig.getRoutes().get(proxy).getPath();

		final Predicate<ZuulConfig.Route> isTheCurrentRoute = route -> path.equals(route.getPath());
		final Predicate<ZuulConfig.Route> isCQRSRoute = route -> route.isCqrs() && !route.getServices().isEmpty();

		zuulConfig.getRoutes().values().stream()
				.filter(isTheCurrentRoute)
				.filter(isCQRSRoute)
				.flatMap(route -> route.getServices().stream())
				.filter(service -> service.getMethods().contains(method))
				.map(ZuulConfig.Service::getServiceId)
				.findAny()
				.ifPresent(serviceId -> ctx.set(FilterConstants.SERVICE_ID_KEY, serviceId));

		return null;
	}


}
