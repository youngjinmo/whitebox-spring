package io.andy.shorten_url.link.service;

import io.andy.shorten_url.exception.client.NotFoundException;
import io.andy.shorten_url.exception.server.InternalServerException;
import io.andy.shorten_url.link.dto.CreateLinkDto;
import io.andy.shorten_url.link.entity.Link;
import io.andy.shorten_url.link.repository.LinkRepository;
import io.andy.shorten_url.util.random.RandomObject;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class LinkServiceImpl implements LinkService {
    @Autowired private final LinkRepository linkRepository;
    @Autowired private final RandomObject<StringBuilder> randomString;

    public LinkServiceImpl(LinkRepository linkRepository, RandomObject<StringBuilder> randomString) {
        this.linkRepository = linkRepository;
        this.randomString = randomString;
    }

    @Override
    public Link createLink(CreateLinkDto dto) throws InternalServerException {
        return null;
    }

    @Override
    public Link findLinkById(Long id) throws NotFoundException {
        return null;
    }

    @Override
    public Link[] findAllLinksByUserId(Long userId) {
        return new Link[0];
    }

    @Override
    public boolean isUniqueUrlPath(String urlPath) {
        return false;
    }

    @Override
    public String getRedirectionUrl(String urlPath) throws NotFoundException {
        return "";
    }
}

