package com.nivtech.petitecaisse.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private final Auth auth = new Auth();
    private final Config config = new Config();

    public static class Config {
        private String fronendOrigin;

        public String getFronendOrigin() {
            return fronendOrigin;
        }

        public void setFronendOrigin(String fronendOrigin) {
            this.fronendOrigin = fronendOrigin;
        }

    }

    public static class Auth {
        private String tokenSecret;
        private long tokenExpirationMsec;
        private List<String> authorizedApiToken = new ArrayList<>();

        public String getTokenSecret() {
            return tokenSecret;
        }

        public void setTokenSecret(String tokenSecret) {
            this.tokenSecret = tokenSecret;
        }

        public long getTokenExpirationMsec() {
            return tokenExpirationMsec;
        }

        public void setTokenExpirationMsec(long tokenExpirationMsec) {
            this.tokenExpirationMsec = tokenExpirationMsec;
        }

        public List<String> getAuthorizedApiToken() {
            return authorizedApiToken;
        }

        public void setAuthorizedApiToken(List<String> authorizedApiToken) {
            this.authorizedApiToken = authorizedApiToken;
        }
    }

    public Auth getAuth() {
        return auth;
    }

    public Config getConfig() {
        return config;
    }

}
