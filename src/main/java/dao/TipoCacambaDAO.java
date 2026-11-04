package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.TipoCacamba;

public class TipoCacambaDAO {

    public void inserir(TipoCacamba tipo) {
        String sql = "INSERT INTO TipoCacamba (codigo, descricao, capacidadeVolumetrica, precoDiario) "
                + "VALUES (?, ?, ?, ?)";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tipo.getCodigo());
            ps.setString(2, tipo.getDescricao());
            ps.setDouble(3, tipo.getCapacidadeVolumetrica());
            ps.setDouble(4, tipo.getPrecoDiario());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir tipo de caçamba: " + e.getMessage(), e);
        }
    }

    public void alterar(TipoCacamba tipo) {
        String sql = "UPDATE TipoCacamba SET descricao = ?, capacidadeVolumetrica = ?, precoDiario = ? "
                + "WHERE codigo = ?";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tipo.getDescricao());
            ps.setDouble(2, tipo.getCapacidadeVolumetrica());
            ps.setDouble(3, tipo.getPrecoDiario());
            ps.setString(4, tipo.getCodigo());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao alterar tipo de caçamba: " + e.getMessage(), e);
        }
    }

    public void remover(String codigo) {
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement("DELETE FROM TipoCacamba WHERE codigo = ?")) {
            ps.setString(1, codigo);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover tipo de caçamba: " + e.getMessage(), e);
        }
    }

    /** Devolve null se o tipo nao existir. */
    public TipoCacamba buscar(String codigo) {
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM TipoCacamba WHERE codigo = ?")) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? montar(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar tipo de caçamba: " + e.getMessage(), e);
        }
    }

    public List<TipoCacamba> listar() {
        List<TipoCacamba> tipos = new ArrayList<>();
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM TipoCacamba ORDER BY codigo");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                tipos.add(montar(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar tipos de caçamba: " + e.getMessage(), e);
        }
        return tipos;
    }

    private TipoCacamba montar(ResultSet rs) throws SQLException {
        TipoCacamba tipo = new TipoCacamba();
        tipo.setCodigo(rs.getString("codigo"));
        tipo.setDescricao(rs.getString("descricao"));
        tipo.setCapacidadeVolumetrica(rs.getDouble("capacidadeVolumetrica"));
        tipo.setPrecoDiario(rs.getDouble("precoDiario"));
        return tipo;
    }
}
