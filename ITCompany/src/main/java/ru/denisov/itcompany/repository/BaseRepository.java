package ru.denisov.itcompany.repository;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<ID, E> {
    void insert(E entity);

    List<E> findAll();

    Optional<E> findById(ID id);

    void update(ID id, E updatedEntity);

    void delete(ID id);
}
