package com.mastercom.ps.connector.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.mastercard.api.core.ApiConfig;
import com.mastercard.api.core.model.Environment;
import com.mastercard.api.core.security.oauth.OAuthAuthentication;
import com.peoplesoft.pt.integrationgateway.framework.ConnectorInfo;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CustomHttpClientBuilder;

/**
 * Classe di configurazione properties e sicurezza.
 * 
 * 
 * @author SabatiniJa
 *
 */
public class ServiceConfiguration {

	private final Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
	private Properties properties;
	private String P12;
	private String consumerKey;
	private String keyAlias;
	private String keyPassword;
	private String setDebug;
	// da impostare per gli ambienti di sviluppo in 'local' o 'dev' mentre in
	// produzione: 'prod'
	private String env;
	private String host;
	private int port;
	private String username;
	private String password;
	private int socketTimeout;
	private int connectTimeout;
	private int connectionRequestTimeout;

	/**
	 * Instanziare per connessione locale
	 */
	public ServiceConfiguration() {
		setLocalConfig();
	}

	/**
	 * Instanziare per ambienti SVILUPPO/STAGE/PRODUZIONE
	 * 
	 * @param connInfo
	 */
	public ServiceConfiguration(ConnectorInfo connInfo) {
		setConfig(connInfo);
	}

	/**
	 * @see <a href="https://developer.mastercard.com/page/java-sdk-guide#http-proxy">https://developer.mastercard.com/page/java-sdk-guide#http-proxy</a>
	 * 
	 * @author SabatiniJa
	 *
	 */
	public class HttpProxyConfiguration {

		private String host;
		private int port;
		private String username;
		private String password;

		public HttpProxyConfiguration(String host, int port, String username, String password) {
			super();
			this.setHost(host);
			this.setPort(port);
			this.setUsername(username);
			this.setPassword(password);
			this.setProxy(this.getHost(), this.getPort(), this.getUsername(), this.getPassword());
		}

		private void setProxy(String host, int port, String username, String password) {
			CustomHttpClientBuilder builder = ApiConfig.getHttpClientBuilder();
			HttpHost proxy = new HttpHost(host, port);
			builder.setProxy(proxy);

			// For other authentication mechanisms look at Apache HttpClient documentation
			// https://hc.apache.org/httpcomponents-client-ga/httpclient/apidocs/org/apache/http/client/CredentialsProvider.html
			CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			credentialsProvider.setCredentials(new AuthScope(host, port),
					new UsernamePasswordCredentials(username, password));
			builder.setDefaultCredentialsProvider(credentialsProvider);
		}

		private String getHost() {
			return host;
		}

		private void setHost(String host) {
			this.host = host;
		}

		private int getPort() {
			return port;
		}

		private void setPort(int port) {
			this.port = port;
		}

		private String getUsername() {
			return username;
		}

		private void setUsername(String username) {
			this.username = username;
		}

		private String getPassword() {
			return password;
		}

		private void setPassword(String password) {
			this.password = password;
		}

		@Override
		public String toString() {
			return "HttpProxyConfiguration [host=" + host + ", port=" + port + ", username=" + username + ", password="
					+ password + "]";
		}

	}
	
	public class ConnectionTimeout {
		
		private int socketTimeout;
		private int connectTimeout;
		private int connectionRequestTimeout;
		
		public ConnectionTimeout(int socketTimeout, int conntectTimeout, int connectionRequestTimeout) {
			super();
			this.setSocketTimeout(socketTimeout);
			this.setConnectTimeout(conntectTimeout);
			this.setConnectionRequestTimeout(connectionRequestTimeout);
			this.setConnectionTimeout(this.getSocketTimeout(), this.getConnectTimeout(), this.getConnectionRequestTimeout());
		}
		
		private void setConnectionTimeout(int socketTimeout, int connectTimeout, int connectionRequestTimeout){
			RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
			// Limit the amount of time waiting for data
			requestConfigBuilder.setSocketTimeout(1000);
			// Limit the amount of time waiting for a connection to the server to be
			// established
			requestConfigBuilder.setConnectTimeout(1000);
			// Limit the amount of time waiting for a connection from the connection pool
			requestConfigBuilder.setConnectionRequestTimeout(1000);

			ApiConfig.getHttpClientBuilder().setDefaultRequestConfig(requestConfigBuilder.build());
		}

		private int getSocketTimeout() {
			return socketTimeout;
		}

		private void setSocketTimeout(int socketTimeout) {
			this.socketTimeout = socketTimeout;
		}

		private int getConnectTimeout() {
			return connectTimeout;
		}

		private void setConnectTimeout(int conntectTimeout) {
			this.connectTimeout = conntectTimeout;
		}

		private int getConnectionRequestTimeout() {
			return connectionRequestTimeout;
		}

		private void setConnectionRequestTimeout(int connectionRequestTimeout) {
			this.connectionRequestTimeout = connectionRequestTimeout;
		}

		@Override
		public String toString() {
			return "ConnectionTimeout [socketTimeout=" + socketTimeout + ", connectTimeout=" + connectTimeout
					+ ", connectionRequestTimeout=" + connectionRequestTimeout + "]";
		}
	}
	/**
	 * 
	 */
	private void setProperties() {
		InputStream is = null;
		try {
			File file = new File("local.properties");
			is = new FileInputStream(file);
			properties = new Properties();
			properties.load(is);

		} catch (IOException exc) {
			log.error("Properties exception: " + exc.getMessage());
		}

	}

	private void setLocalConfig() {
		try {
			setProperties();
			log.debug("--------------init Local Properties--------------");
			this.env = properties.getProperty("env");
			log.debug("env: " + env);
			this.P12 = properties.getProperty("P12");
			log.debug("P12: " + P12);
			this.consumerKey = properties.getProperty("consumerKey");
			log.debug("consumerKey: " + consumerKey);
			this.keyAlias = properties.getProperty("keyAlias");
			log.debug("keyAlias: " + keyAlias);
			this.keyPassword = properties.getProperty("keyPassword");
			log.debug("keyPassword: " + keyPassword);
			this.setDebug = properties.getProperty("setDebug");
			log.debug("setDebug: " + setDebug);
			InputStream is = new FileInputStream(P12);
			ApiConfig.setAuthentication(new OAuthAuthentication(this.consumerKey, is, this.keyAlias, this.keyPassword));
			log.debug("ApiConfig.setAuthentication done!");
			// Enable http wire logging
			ApiConfig.setDebug(Boolean.parseBoolean(this.setDebug));
			log.debug("ApiConfig.setDebug done!");
			// This is needed to change the environment to run the sample code. For
			// production: use ApiConfig.setSandbox(false);
			if ("local".equalsIgnoreCase(this.env) || "dev".equalsIgnoreCase(this.env)) {
				ApiConfig.setEnvironment(Environment.parse("sandbox"));
				log.debug("Set Environment: sandbox");
			} else if ("prod".equalsIgnoreCase(this.env)) {
				ApiConfig.setSandbox(false);
				log.debug("Set Environment: prod");
			}
			this.host = properties.getProperty("host");
			log.debug("host: " + host);
			this.port = Integer.valueOf(properties.getProperty("port"));
			log.debug("port: " + port);
			this.username = properties.getProperty("username");
			log.debug("username: " + username);
			this.password = properties.getProperty("password");
			log.debug("password: " + password);
			if (!"local".equalsIgnoreCase(this.env)) {
				@SuppressWarnings("unused")
				HttpProxyConfiguration proxyConfiguration = new HttpProxyConfiguration(this.host, this.port,
						this.username, this.password);
				log.debug("Set Proxy");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		log.info("--------------Local Properties impostato--------------");
	}

	private void setConfig(ConnectorInfo connInfo) {
		log.debug("--------------init Properties--------------");
		env = connInfo.getFieldValue("env");
		log.debug("env: " + env);
		P12 = connInfo.getFieldValue("P12");
		log.debug("P12: " + P12);
		consumerKey = connInfo.getFieldValue("consumerKey");
		log.debug("consumerKey: " + consumerKey);
		keyAlias = connInfo.getFieldValue("keyAlias");
		log.debug("keyAlias: " + keyAlias);
		keyPassword = connInfo.getFieldValue("keyPassword");
		log.debug("keyPassword: " + keyPassword);
		setDebug = connInfo.getFieldValue("setDebug");
		log.debug("setDebug: " + setDebug);
		InputStream is = null;
		try {
			is = new FileInputStream(P12);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		ApiConfig.setAuthentication(new OAuthAuthentication(consumerKey, is, keyAlias, keyPassword));
		log.debug("Authenticazione impostato!");
		// Enable http wire logging
		ApiConfig.setDebug(Boolean.parseBoolean(setDebug));
		log.debug("Debug http wire impostato a " + setDebug);
		// This is needed to change the environment to run the sample code. For
		// production: use ApiConfig.setSandbox(false);
		if ("local".equalsIgnoreCase(env) || "dev".equalsIgnoreCase(env)) {
			ApiConfig.setEnvironment(Environment.parse("sandbox"));
			log.debug("Environment impostato a sandbox");
		} else if ("prod".equalsIgnoreCase(env)) {
			ApiConfig.setSandbox(false);
			log.debug("Environment impostato a Produzione");
		}
		this.host = connInfo.getFieldValue("host");
		log.debug("host: " + host);
		this.port = Integer.valueOf(connInfo.getFieldValue("port"));
		log.debug("port: " + port);
		this.username = connInfo.getFieldValue("username");
		log.debug("username: " + username);
		this.password = connInfo.getFieldValue("password");
		log.debug("password: " + password);
		this.connectTimeout = Integer.valueOf(connInfo.getFieldValue("connectTimeout"));
		log.debug("password: " + password);
		@SuppressWarnings("unused")
		HttpProxyConfiguration proxyConfiguration = new HttpProxyConfiguration(this.host, this.port, this.username,
				this.password);
		log.debug("Proxy impostato");
		this.connectTimeout = Integer.valueOf(connInfo.getFieldValue("connectTimeout"));
		log.debug("connectTimeout: " + connectTimeout);
		this.socketTimeout = Integer.valueOf(connInfo.getFieldValue("socketTimeout"));
		log.debug("socketTimeout: " + socketTimeout);
		this.connectionRequestTimeout = Integer.valueOf(connInfo.getFieldValue("connectionRequestTimeout"));
		log.debug("connectionRequestTimeout: " + connectionRequestTimeout);
		@SuppressWarnings("unused")
		ConnectionTimeout connectionTimeout = new ConnectionTimeout(this.socketTimeout, this.connectTimeout,
				this.connectionRequestTimeout);
		log.debug("Connection Timeout impostato");
		log.info("--------------Properties impostate--------------");
	}

}
