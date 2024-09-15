package io.andy.shorten_url.common;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CommonRepository<Entity> {
    Optional<List<Entity>> findByOrderByCreatedAtAsc(Pageable pageable);
    Optional<List<Entity>> findByOrderByCreatedAtDesc(Pageable pageable);
}
