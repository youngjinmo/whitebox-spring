package io.andy.shorten_url.link_counter.service;

import io.andy.shorten_url.link_counter.entity.LinkCounter;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface LinkCounterService {
    void putAccessCount(HttpServletRequest request, Long linkId);
    List<LinkCounter> findAllAccessCounts();
    List<LinkCounter> findAccessCountsByLinkId(Long linkId);
}
