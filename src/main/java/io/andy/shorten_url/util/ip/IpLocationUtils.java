package io.andy.shorten_url.util.ip;

import io.andy.shorten_url.exception.server.InternalServerException;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.reactive.function.client.WebClient;

public class IpLocationUtils {
    private final WebClient webClient;

    public IpLocationUtils(String hostUrl) {
        webClient = WebClient.create(hostUrl);
    }

    public IpApiResponse getLocationByIp(String ip) throws InternalServerException {
        try {
            String response = webClient.get()
                .uri("/"+ip)
                .retrieve()
                .bodyToMono(String.class).block();
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response, IpApiResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error: "+e.getMessage());
        }
        // logback
        throw new InternalServerException("외부 api 호출 에러");
    }
}
