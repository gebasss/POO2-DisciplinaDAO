package dao;

import java.util.List;
import entidade.Disciplina;

public interface DisciplinaDAO {

    void insert(Disciplina obj);
    void update(Disciplina obj);
    void deleteById(Integer id);
    Disciplina findById(Integer id);
    List<Disciplina> findAll();

}
