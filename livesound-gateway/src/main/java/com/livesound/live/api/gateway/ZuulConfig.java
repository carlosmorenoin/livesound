package com.livesound.live.api.gateway;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("zuul")
public class ZuulConfig {

	private Map<String, Route> routes = new HashMap<>();

	public Map<String, Route> getRoutes() {
		return routes;
	}

	public void setRoutes(final Map<String, Route> routes) {
		this.routes = routes;
	}

	public static class Route {
		private String       path;
		private String       serviceId;
		private String       stripPrefix;
		private boolean		 cqrs;
		private List<Service> services = new ArrayList<>();

		public String getPath() {
			return path;
		}

		public void setPath(final String path) {
			this.path = path;
		}

		public String getServiceId() {
			return serviceId;
		}

		public void setServiceId(final String serviceId) {
			this.serviceId = serviceId;
		}

		public String getStripPrefix() {
			return stripPrefix;
		}

		public void setStripPrefix(final String stripPrefix) {
			this.stripPrefix = stripPrefix;
		}

		public boolean isCqrs() {
			return cqrs;
		}

		public void setCqrs(boolean cqrs) {
			this.cqrs = cqrs;
		}

		public List<Service> getServices() {
			return services;
		}

		public void setServices(List<Service> services) {
			this.services = services;
		}
	}

	static class Service {
		private String serviceId;
		private List<String> methods;

		public String getServiceId() {
			return serviceId;
		}

		public void setServiceId(String serviceId) {
			this.serviceId = serviceId;
		}

		public List<String> getMethods() {
			return methods;
		}

		public void setMethods(List<String> methods) {
			this.methods = methods;
		}
	}

}
