package io.andy.shorten_url.link.service;

import io.andy.shorten_url.link.constant.LinkState;
import io.andy.shorten_url.link.entity.Link;
import io.andy.shorten_url.link.dto.CreateLinkDto;

import java.util.List;

public interface LinkService {
    Link createLink(CreateLinkDto dto);
    Link findLinkById(Long id);
    Link findLinkByUrlPath(String urlPath);
    List<Link> findLinksByUserId(Long userId);
    List<Link> findAllLinks();
    boolean isUniqueUrlPath(String urlPath);
    Link updateLinkState(Long id, LinkState state);
    Link updateRedirectionUrl(Long id, String redirectionUrl);
    void deleteLinkById(Long id);
    long increaseLinkCount(Long id);
}
