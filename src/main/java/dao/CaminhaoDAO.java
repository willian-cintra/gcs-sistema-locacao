package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Caminhao;

public class CaminhaoDAO {

    public void inserir(Caminhao cam) {
        String sql = "INSERT INTO Caminhao (placa, modelo, anoFabricacao, capacidadeCarga, situacao) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cam.getPlaca());
            ps.setString(2, cam.getModelo());
            ps.setInt(3, cam.getAnoFabricacao());
            ps.setDouble(4, cam.getCapacidadeCarga());
            ps.setString(5, cam.getSituacao());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir caminhão: " + e.getMessage(), e);
        }
    }

    public void alterar(Caminhao cam) {
        String sql = "UPDATE Caminhao SET modelo = ?, anoFabricacao = ?, capacidadeCarga = ?, situacao = ? "
                + "WHERE placa = ?";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cam.getModelo());
            ps.setInt(2, cam.getAnoFabricacao());
            ps.setDouble(3, cam.getCapacidadeCarga());
            ps.setString(4, cam.getSituacao());
            ps.setString(5, cam.getPlaca());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao alterar caminhão: " + e.getMessage(), e);
        }
    }

    public void remover(String placa) {
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement("DELETE FROM Caminhao WHERE placa = ?")) {
            ps.setString(1, placa);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover caminhão: " + e.getMessage(), e);
        }
    }

    /** Devolve null se o caminhao nao existir. */
    public Caminhao buscar(String placa) {
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM Caminhao WHERE placa = ?")) {
            ps.setString(1, placa);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? montar(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar caminhão: " + e.getMessage(), e);
        }
    }

    public List<Caminhao> listar() {
        List<Caminhao> caminhoes = new ArrayList<>();
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM Caminhao ORDER BY placa");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                caminhoes.add(montar(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar caminhões: " + e.getMessage(), e);
        }
        return caminhoes;
    }

    private Caminhao montar(ResultSet rs) throws SQLException {
        Caminhao cam = new Caminhao();
        cam.setPlaca(rs.getString("placa"));
        cam.setModelo(rs.getString("modelo"));
        cam.setAnoFabricacao(rs.getInt("anoFabricacao"));
        cam.setCapacidadeCarga(rs.getDouble("capacidadeCarga"));
        cam.setSituacao(rs.getString("situacao"));
        return cam;
    }
}
