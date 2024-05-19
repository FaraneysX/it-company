package ru.denisov.itcompany.repository;

import java.util.List;

public interface BaseRepository<ID, E> {
    void insert(E entity);

    List<E> findAll();

    E findById(ID id);

    void update(E updatedEntity);

    void delete(ID id);
}
