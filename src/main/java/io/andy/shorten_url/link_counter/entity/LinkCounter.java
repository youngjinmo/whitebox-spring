package io.andy.shorten_url.link_counter.entity;

import jakarta.persistence.*;

import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class LinkCounter {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime accessDate;
    private Long linkId;
    private String ipAddress;
    private String userAgent;
    private String location;
    private String referer;

    public LinkCounter() {}

    public LinkCounter(
            Long linkId,
            LocalDateTime accessDate,
            String ipAddress,
            String userAgent,
            String location,
            String referer
    ) {
        this.linkId = linkId;
        this.accessDate = accessDate;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.location = location;
        this.referer = referer;
    }
}
