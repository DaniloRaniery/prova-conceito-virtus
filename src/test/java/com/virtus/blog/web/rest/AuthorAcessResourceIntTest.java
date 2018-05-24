package com.virtus.blog.web.rest;

import com.virtus.blog.JHipsterBlogApp;

import com.virtus.blog.domain.AuthorAcess;
import com.virtus.blog.repository.AuthorAcessRepository;
import com.virtus.blog.service.AuthorAcessService;
import com.virtus.blog.repository.search.AuthorAcessSearchRepository;
import com.virtus.blog.service.dto.AuthorAcessDTO;
import com.virtus.blog.service.mapper.AuthorAcessMapper;
import com.virtus.blog.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.virtus.blog.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AuthorAcessResource REST controller.
 *
 * @see AuthorAcessResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JHipsterBlogApp.class)
public class AuthorAcessResourceIntTest {

    private static final String DEFAULT_REQUEST_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_REQUEST_LOGIN = "BBBBBBBBBB";

    @Autowired
    private AuthorAcessRepository authorAcessRepository;

    @Autowired
    private AuthorAcessMapper authorAcessMapper;

    @Autowired
    private AuthorAcessService authorAcessService;

    @Autowired
    private AuthorAcessSearchRepository authorAcessSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAuthorAcessMockMvc;

    private AuthorAcess authorAcess;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AuthorAcessResource authorAcessResource = new AuthorAcessResource(authorAcessService);
        this.restAuthorAcessMockMvc = MockMvcBuilders.standaloneSetup(authorAcessResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AuthorAcess createEntity(EntityManager em) {
        AuthorAcess authorAcess = new AuthorAcess()
            .requestLogin(DEFAULT_REQUEST_LOGIN);
        return authorAcess;
    }

    @Before
    public void initTest() {
        authorAcessSearchRepository.deleteAll();
        authorAcess = createEntity(em);
    }

    @Test
    @Transactional
    public void createAuthorAcess() throws Exception {
        int databaseSizeBeforeCreate = authorAcessRepository.findAll().size();

        // Create the AuthorAcess
        AuthorAcessDTO authorAcessDTO = authorAcessMapper.toDto(authorAcess);
        restAuthorAcessMockMvc.perform(post("/api/author-acesses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(authorAcessDTO)))
            .andExpect(status().isCreated());

        // Validate the AuthorAcess in the database
        List<AuthorAcess> authorAcessList = authorAcessRepository.findAll();
        assertThat(authorAcessList).hasSize(databaseSizeBeforeCreate + 1);
        AuthorAcess testAuthorAcess = authorAcessList.get(authorAcessList.size() - 1);
        assertThat(testAuthorAcess.getRequestLogin()).isEqualTo(DEFAULT_REQUEST_LOGIN);

        // Validate the AuthorAcess in Elasticsearch
        AuthorAcess authorAcessEs = authorAcessSearchRepository.findOne(testAuthorAcess.getId());
        assertThat(authorAcessEs).isEqualToIgnoringGivenFields(testAuthorAcess);
    }

    @Test
    @Transactional
    public void createAuthorAcessWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = authorAcessRepository.findAll().size();

        // Create the AuthorAcess with an existing ID
        authorAcess.setId(1L);
        AuthorAcessDTO authorAcessDTO = authorAcessMapper.toDto(authorAcess);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAuthorAcessMockMvc.perform(post("/api/author-acesses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(authorAcessDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AuthorAcess in the database
        List<AuthorAcess> authorAcessList = authorAcessRepository.findAll();
        assertThat(authorAcessList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkRequestLoginIsRequired() throws Exception {
        int databaseSizeBeforeTest = authorAcessRepository.findAll().size();
        // set the field null
        authorAcess.setRequestLogin(null);

        // Create the AuthorAcess, which fails.
        AuthorAcessDTO authorAcessDTO = authorAcessMapper.toDto(authorAcess);

        restAuthorAcessMockMvc.perform(post("/api/author-acesses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(authorAcessDTO)))
            .andExpect(status().isBadRequest());

        List<AuthorAcess> authorAcessList = authorAcessRepository.findAll();
        assertThat(authorAcessList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAuthorAcesses() throws Exception {
        // Initialize the database
        authorAcessRepository.saveAndFlush(authorAcess);

        // Get all the authorAcessList
        restAuthorAcessMockMvc.perform(get("/api/author-acesses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(authorAcess.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestLogin").value(hasItem(DEFAULT_REQUEST_LOGIN.toString())));
    }

    @Test
    @Transactional
    public void getAuthorAcess() throws Exception {
        // Initialize the database
        authorAcessRepository.saveAndFlush(authorAcess);

        // Get the authorAcess
        restAuthorAcessMockMvc.perform(get("/api/author-acesses/{id}", authorAcess.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(authorAcess.getId().intValue()))
            .andExpect(jsonPath("$.requestLogin").value(DEFAULT_REQUEST_LOGIN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAuthorAcess() throws Exception {
        // Get the authorAcess
        restAuthorAcessMockMvc.perform(get("/api/author-acesses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAuthorAcess() throws Exception {
        // Initialize the database
        authorAcessRepository.saveAndFlush(authorAcess);
        authorAcessSearchRepository.save(authorAcess);
        int databaseSizeBeforeUpdate = authorAcessRepository.findAll().size();

        // Update the authorAcess
        AuthorAcess updatedAuthorAcess = authorAcessRepository.findOne(authorAcess.getId());
        // Disconnect from session so that the updates on updatedAuthorAcess are not directly saved in db
        em.detach(updatedAuthorAcess);
        updatedAuthorAcess
            .requestLogin(UPDATED_REQUEST_LOGIN);
        AuthorAcessDTO authorAcessDTO = authorAcessMapper.toDto(updatedAuthorAcess);

        restAuthorAcessMockMvc.perform(put("/api/author-acesses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(authorAcessDTO)))
            .andExpect(status().isOk());

        // Validate the AuthorAcess in the database
        List<AuthorAcess> authorAcessList = authorAcessRepository.findAll();
        assertThat(authorAcessList).hasSize(databaseSizeBeforeUpdate);
        AuthorAcess testAuthorAcess = authorAcessList.get(authorAcessList.size() - 1);
        assertThat(testAuthorAcess.getRequestLogin()).isEqualTo(UPDATED_REQUEST_LOGIN);

        // Validate the AuthorAcess in Elasticsearch
        AuthorAcess authorAcessEs = authorAcessSearchRepository.findOne(testAuthorAcess.getId());
        assertThat(authorAcessEs).isEqualToIgnoringGivenFields(testAuthorAcess);
    }

    @Test
    @Transactional
    public void updateNonExistingAuthorAcess() throws Exception {
        int databaseSizeBeforeUpdate = authorAcessRepository.findAll().size();

        // Create the AuthorAcess
        AuthorAcessDTO authorAcessDTO = authorAcessMapper.toDto(authorAcess);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAuthorAcessMockMvc.perform(put("/api/author-acesses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(authorAcessDTO)))
            .andExpect(status().isCreated());

        // Validate the AuthorAcess in the database
        List<AuthorAcess> authorAcessList = authorAcessRepository.findAll();
        assertThat(authorAcessList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAuthorAcess() throws Exception {
        // Initialize the database
        authorAcessRepository.saveAndFlush(authorAcess);
        authorAcessSearchRepository.save(authorAcess);
        int databaseSizeBeforeDelete = authorAcessRepository.findAll().size();

        // Get the authorAcess
        restAuthorAcessMockMvc.perform(delete("/api/author-acesses/{id}", authorAcess.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean authorAcessExistsInEs = authorAcessSearchRepository.exists(authorAcess.getId());
        assertThat(authorAcessExistsInEs).isFalse();

        // Validate the database is empty
        List<AuthorAcess> authorAcessList = authorAcessRepository.findAll();
        assertThat(authorAcessList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAuthorAcess() throws Exception {
        // Initialize the database
        authorAcessRepository.saveAndFlush(authorAcess);
        authorAcessSearchRepository.save(authorAcess);

        // Search the authorAcess
        restAuthorAcessMockMvc.perform(get("/api/_search/author-acesses?query=id:" + authorAcess.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(authorAcess.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestLogin").value(hasItem(DEFAULT_REQUEST_LOGIN.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AuthorAcess.class);
        AuthorAcess authorAcess1 = new AuthorAcess();
        authorAcess1.setId(1L);
        AuthorAcess authorAcess2 = new AuthorAcess();
        authorAcess2.setId(authorAcess1.getId());
        assertThat(authorAcess1).isEqualTo(authorAcess2);
        authorAcess2.setId(2L);
        assertThat(authorAcess1).isNotEqualTo(authorAcess2);
        authorAcess1.setId(null);
        assertThat(authorAcess1).isNotEqualTo(authorAcess2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AuthorAcessDTO.class);
        AuthorAcessDTO authorAcessDTO1 = new AuthorAcessDTO();
        authorAcessDTO1.setId(1L);
        AuthorAcessDTO authorAcessDTO2 = new AuthorAcessDTO();
        assertThat(authorAcessDTO1).isNotEqualTo(authorAcessDTO2);
        authorAcessDTO2.setId(authorAcessDTO1.getId());
        assertThat(authorAcessDTO1).isEqualTo(authorAcessDTO2);
        authorAcessDTO2.setId(2L);
        assertThat(authorAcessDTO1).isNotEqualTo(authorAcessDTO2);
        authorAcessDTO1.setId(null);
        assertThat(authorAcessDTO1).isNotEqualTo(authorAcessDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(authorAcessMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(authorAcessMapper.fromId(null)).isNull();
    }
}
