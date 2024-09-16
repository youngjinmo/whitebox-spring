package io.andy.shorten_url.link_counter.repository;

import io.andy.shorten_url.link_counter.entity.LinkCounter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LinkCounterRepository extends JpaRepository<LinkCounter, Long> {
    List<LinkCounter> findLinkCounterByLinkId(Long linkId);
}
