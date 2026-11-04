package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Motorista;

public class MotoristaDAO {

    public void inserir(Motorista mot) {
        String sql = "INSERT INTO Motorista (matricula, nome, cpf, cnh, categoriaCnh, telefone) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, mot.getMatricula());
            ps.setString(2, mot.getNome());
            ps.setString(3, mot.getCpf());
            ps.setString(4, mot.getCnh());
            ps.setString(5, mot.getCategoriaCnh());
            ps.setString(6, mot.getTelefone());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir motorista: " + e.getMessage(), e);
        }
    }

    public void alterar(Motorista mot) {
        String sql = "UPDATE Motorista SET nome = ?, cpf = ?, cnh = ?, categoriaCnh = ?, telefone = ? "
                + "WHERE matricula = ?";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, mot.getNome());
            ps.setString(2, mot.getCpf());
            ps.setString(3, mot.getCnh());
            ps.setString(4, mot.getCategoriaCnh());
            ps.setString(5, mot.getTelefone());
            ps.setString(6, mot.getMatricula());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao alterar motorista: " + e.getMessage(), e);
        }
    }

    public void remover(String matricula) {
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement("DELETE FROM Motorista WHERE matricula = ?")) {
            ps.setString(1, matricula);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover motorista: " + e.getMessage(), e);
        }
    }

    /** Devolve null se o motorista nao existir. */
    public Motorista buscar(String matricula) {
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM Motorista WHERE matricula = ?")) {
            ps.setString(1, matricula);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? montar(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar motorista: " + e.getMessage(), e);
        }
    }

    public List<Motorista> listar() {
        List<Motorista> motoristas = new ArrayList<>();
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM Motorista ORDER BY nome");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                motoristas.add(montar(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar motoristas: " + e.getMessage(), e);
        }
        return motoristas;
    }

    private Motorista montar(ResultSet rs) throws SQLException {
        Motorista mot = new Motorista();
        mot.setMatricula(rs.getString("matricula"));
        mot.setNome(rs.getString("nome"));
        mot.setCpf(rs.getString("cpf"));
        mot.setCnh(rs.getString("cnh"));
        mot.setCategoriaCnh(rs.getString("categoriaCnh"));
        mot.setTelefone(rs.getString("telefone"));
        return mot;
    }
}
