package dao;

import java.util.List;

public interface DAO<T> {
    boolean save(T t);
    boolean update(T t);
    boolean delete(int id);
    T findById(int id);
    List<T> findAll();
}