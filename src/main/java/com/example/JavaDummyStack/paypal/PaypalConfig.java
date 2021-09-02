package com.example.JavaDummyStack.paypal;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;

@Configuration
public class PaypalConfig {

	@Value("${paypal.client.id}")
	private String clientId;
	@Value("${paypal.client.secret}")
	private String clientSecret;
	@Value("${paypal.mode}")
	private String mode;

	@Bean
	public Map<String, String> paypalSdkConfig() {
		Map<String, String> configMap = new HashMap<>();
		configMap.put("mode", mode);
		return configMap;
	}

	@Bean
	public OAuthTokenCredential oAuthTokenCredential() {
		return new OAuthTokenCredential(clientId, clientSecret, paypalSdkConfig());
	}

	@Bean
	public APIContext apiContext() throws PayPalRESTException {
		System.out.println("Client ID " + clientId);
		System.out.println("Client Secreat " + clientSecret);
		String accessToken = oAuthTokenCredential().getAccessToken();
		System.out.println(accessToken);
		// APIContext apiContext = new APIContext(accessToken);
		return new APIContext(clientId, clientSecret, mode);
		// apiContext.setConfigurationMap(paypalSdkConfig());
		// return apiContext;
	}
}
