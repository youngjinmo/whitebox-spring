package io.andy.shorten_url.link_counter.service;

import io.andy.shorten_url.link_counter.repository.LinkCounterRepository;
import io.andy.shorten_url.link_counter.entity.LinkCounter;
import io.andy.shorten_url.util.ClientMapper;
import io.andy.shorten_url.util.ip.IpApiResponse;
import io.andy.shorten_url.util.ip.IpLocationUtils;

import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class LinkCounterServiceImpl implements LinkCounterService {

    @Autowired private final LinkCounterRepository repository;
    @Autowired private final IpLocationUtils ipLocationUtils;

    public LinkCounterServiceImpl(LinkCounterRepository repository, IpLocationUtils ipLocationUtils) {
        this.repository = repository;
        this.ipLocationUtils = ipLocationUtils;
    }

    @Override
    public void putAccessCount(HttpServletRequest request, Long linkId) {
        String ipAddress = ClientMapper.parseClientIp(request);
        String userAgent = ClientMapper.parseUserAgent(request);
        String referer = request.getHeader("Referer");
        try {
            IpApiResponse externalApiResponse = ipLocationUtils.getLocationByIp(ipAddress);
            String location = externalApiResponse.country();
            if (externalApiResponse.country() == null) {
                location = request.getLocale().getCountry();
            }
            repository.save(new LinkCounter(
                    linkId,
                    LocalDateTime.now(),
                    ipAddress,
                    userAgent,
                    location,
                    referer
            ));
        } catch (Exception e) {
            log.error("failed to get location by ip, message={}", e.getMessage());
        }
    }

    @Override
    public List<LinkCounter> findAllAccessCounts() {
        return repository.findAll();
    }

    @Override
    public List<LinkCounter> findAccessCountsByLinkId(Long linkId) {
        return repository.findLinkCounterByLinkId(linkId);
    }
}
