package com.baison.e3plus.basebiz.order.service.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "order")
public class OrderProperties {
	private final Log log = new Log();
	private final ShopReject shopReject = new ShopReject();

	public Log getLog() {
		return log;
	}

	public ShopReject getShopReject() {
		return shopReject;
	}

	public static class Log {
		private Boolean enable = false;

		public Boolean getEnable() {
			return enable;
		}

		public void setEnable(Boolean enable) {
			this.enable = enable;
		}
	}

	public static class ShopReject {
		private Map<String, Integer> delayHour = new HashMap<>();

		public Map<String, Integer> getDelayHour() {
			return delayHour;
		}

		public void setDelayHour(Map<String, Integer> delayHour) {
			this.delayHour = delayHour;
		}
	}
}
