package by.project.turamyzba.services.impl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class GoogleAuthService {

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.client.token-uri}")
    private String tokenUri;

    @Value("${google.client.userinfo-uri}")
    private String userInfoUri;

    @Value("${google.client.redirect-uri}")
    private String redirectUri;

    private final RestTemplate restTemplate = new RestTemplate();

    public String exchangeCodeForToken(String code) {
        Map<String, Object> request = Map.of(
                "code", code,
                "client_id", clientId,
                "client_secret", clientSecret,
                "redirect_uri", redirectUri,
                "grant_type", "authorization_code"
        );

        Map response = restTemplate.postForObject(tokenUri, request, Map.class);
        if (response == null || !response.containsKey("access_token")) {
            throw new RuntimeException("Failed to get access_token from Google");
        }

        return (String) response.get("access_token");
    }

    public GoogleUserInfo getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> resp = restTemplate.exchange(userInfoUri, HttpMethod.GET, entity, Map.class);
        Map userInfo = resp.getBody();

        if (userInfo == null || !userInfo.containsKey("email")) {
            throw new RuntimeException("Failed to get user info from Google");
        }

        String email = (String) userInfo.get("email");
        String name = (String) userInfo.get("name");
        return new GoogleUserInfo(email, name);
    }

    public static class GoogleUserInfo {
        private final String email;
        private final String name;

        public GoogleUserInfo(String email, String name) {
            this.email = email;
            this.name = name;
        }

        public String getEmail() {return email;}
        public String getName() {return name;}
    }
}

