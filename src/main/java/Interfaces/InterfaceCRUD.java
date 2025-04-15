package Interfaces;

import java.util.List;

public interface InterfaceCRUD<T> {
    void add(T t);
    void update(T t);
    void delete(T t);
    List<T> find();
}
