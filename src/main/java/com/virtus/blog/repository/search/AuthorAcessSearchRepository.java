package com.virtus.blog.repository.search;

import com.virtus.blog.domain.AuthorAcess;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the AuthorAcess entity.
 */
public interface AuthorAcessSearchRepository extends ElasticsearchRepository<AuthorAcess, Long> {
}
