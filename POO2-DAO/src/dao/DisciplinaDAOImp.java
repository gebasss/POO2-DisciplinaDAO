package dao;

import entidade.Disciplina;
import jdbc.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DisciplinaDAOImp implements DisciplinaDAO{

    private final Connection conexao;

    public DisciplinaDAOImp(Connection conexao) {
        this.conexao = conexao;
    }

    @Override
    public void insert(Disciplina obj) {

        if (obj == null)
            throw new IllegalArgumentException("Disciplina não pode ser nulo");

        if (obj.getNomeDisciplina() == null || obj.getNomeDisciplina().trim().isEmpty())
            throw new IllegalArgumentException("Nome da disciplina não pode ser vazio");

        String sql = "INSERT INTO disciplina (nome_disciplina, carga_horaria) VALUES (?, ?)";
        try (PreparedStatement pst = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, obj.getNomeDisciplina());
            pst.setInt(2, obj.getCargaHoraria());
            int linhas = pst.executeUpdate();

            if (linhas > 0) {
                try (ResultSet rs = pst.getGeneratedKeys()) {
                    if (rs.next()) {
                        obj.setIdDisciplina(rs.getInt(1));
                    }
                }
                System.out.println("Disciplina inserida: " + obj.getIdDisciplina() + " | " + obj.getNomeDisciplina());
            } else {
                throw new RuntimeException("Nenhuma linha inserida para a disciplina: " + obj.getNomeDisciplina());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir disciplina", e);
        }
    }

    @Override
    public void update(Disciplina obj) {

        if (obj == null || obj.getIdDisciplina() == null)
            throw new IllegalArgumentException("Disciplina e ID não podem ser nulos");

        String sql = "UPDATE disciplina SET nome_disciplina = ?, carga_horaria = ? WHERE id_disciplina = ?";

        try (PreparedStatement pst = conexao.prepareStatement(sql)) {

            pst.setString(1, obj.getNomeDisciplina());
            pst.setInt(2, obj.getCargaHoraria());
            pst.setInt(3, obj.getIdDisciplina());
            int linhas = pst.executeUpdate();

            if (linhas == 0) {
                throw new RuntimeException("Disciplina com ID " + obj.getIdDisciplina() + " não encontrada para atualização");
            }

            System.out.println("Disciplina atualizada: " + obj.getIdDisciplina() + " | " + obj.getNomeDisciplina());

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar disciplina", e);
        }
    }

    @Override
    public void deleteById(Integer id) {

        if (id == null) throw new IllegalArgumentException("ID não pode ser nulo");

        String sql = "DELETE FROM disciplina WHERE id_disciplina = ?";

        try (PreparedStatement pst = conexao.prepareStatement(sql)) {

            pst.setInt(1, id);
            int linhas = pst.executeUpdate();

            if (linhas == 0) {
                throw new RuntimeException("Disciplina com ID " + id + " não existe");
            }

            System.out.println("Disciplina removida: ID " + id);

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar Disciplina", e);
        }
    }

    @Override
    public Disciplina findById(Integer id) {

        if (id == null) return null;

        String sql = "SELECT id_disciplina, nome_disciplina, carga_horaria FROM disciplina WHERE id_disciplina = ?";
        try (PreparedStatement pst = conexao.prepareStatement(sql)) {
            pst.setInt(1, id);

        try (ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                return new Disciplina(rs.getInt("id_disciplina"), rs.getString("nome_disciplina"), rs.getInt("carga_horaria"));
            }
            return null;
        }

    } catch (SQLException e) {
        throw new RuntimeException("Erro ao buscar disciplina por ID", e) ;
}

    @Override
    public List<Disciplina> findAll() {

        List<Disciplina> disciplinas = new ArrayList<>();
        String sql = "SELECT id_disciplina, nome_disciplina, carga_horaria FROM disciplina ORDER BY nome_disciplina";

        try (PreparedStatement pst = conexao.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()
        ) {

            while (rs.next()) {
                disciplinas.add(new Disciplina(rs.getInt("id_disciplina"), rs.getString("nome_disciplina"), rs.getInt("carga_horaria"));
            }

            return disciplinas;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar todas as disciplinas", e);
        }
    }

}
