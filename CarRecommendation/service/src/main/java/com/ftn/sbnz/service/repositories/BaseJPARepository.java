package com.ftn.sbnz.service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseJPARepository<T> extends JpaRepository<T, Long> {
    Optional<T> findById(Long id);
    List<T> findAllById(Iterable<Long> ids);

    void deleteById(Long id);
    void delete(T entity);
    void deleteAllById(Iterable<? extends Long> ids);
    void deleteAll(Iterable<? extends T> entities);
}
