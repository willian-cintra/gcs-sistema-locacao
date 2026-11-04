package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Cacamba;
import model.TipoCacamba;

public class CacambaDAO {

    // Traz junto os dados do tipo, para montar a Cacamba com o TipoCacamba completo
    private static final String SELECT_COM_TIPO = "SELECT c.*, t.descricao, t.capacidadeVolumetrica, t.precoDiario "
            + "FROM Cacamba c JOIN TipoCacamba t ON t.codigo = c.codigoTipoCacamba ";

    public void inserir(Cacamba cac) {
        String sql = "INSERT INTO Cacamba (patrimonio, capacidade, cor, situacao, codigoTipoCacamba) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cac.getPatrimonio());
            ps.setDouble(2, cac.getCapacidade());
            ps.setString(3, cac.getCor());
            ps.setString(4, cac.getSituacao());
            ps.setString(5, cac.getTipoCacamba().getCodigo());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir caçamba: " + e.getMessage(), e);
        }
    }

    public void alterar(Cacamba cac) {
        String sql = "UPDATE Cacamba SET capacidade = ?, cor = ?, situacao = ?, codigoTipoCacamba = ? "
                + "WHERE patrimonio = ?";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, cac.getCapacidade());
            ps.setString(2, cac.getCor());
            ps.setString(3, cac.getSituacao());
            ps.setString(4, cac.getTipoCacamba().getCodigo());
            ps.setString(5, cac.getPatrimonio());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao alterar caçamba: " + e.getMessage(), e);
        }
    }

    public void remover(String patrimonio) {
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement("DELETE FROM Cacamba WHERE patrimonio = ?")) {
            ps.setString(1, patrimonio);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover caçamba: " + e.getMessage(), e);
        }
    }

    /** Devolve null se a cacamba nao existir. */
    public Cacamba buscar(String patrimonio) {
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(SELECT_COM_TIPO + "WHERE c.patrimonio = ?")) {
            ps.setString(1, patrimonio);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? montar(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar caçamba: " + e.getMessage(), e);
        }
    }

    public List<Cacamba> listar() {
        List<Cacamba> cacambas = new ArrayList<>();
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(SELECT_COM_TIPO + "ORDER BY c.patrimonio");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                cacambas.add(montar(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar caçambas: " + e.getMessage(), e);
        }
        return cacambas;
    }

    public int contarDisponiveisPorTipo(String codigoTipo) {
        String sql = "SELECT COUNT(*) FROM Cacamba WHERE codigoTipoCacamba = ? AND situacao = 'disponivel'";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, codigoTipo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar caçambas disponíveis: " + e.getMessage(), e);
        }
    }

    public boolean existePorTipo(String codigoTipo) {
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement("SELECT 1 FROM Cacamba WHERE codigoTipoCacamba = ?")) {
            ps.setString(1, codigoTipo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar caçambas do tipo: " + e.getMessage(), e);
        }
    }

    private Cacamba montar(ResultSet rs) throws SQLException {
        TipoCacamba tipo = new TipoCacamba();
        tipo.setCodigo(rs.getString("codigoTipoCacamba"));
        tipo.setDescricao(rs.getString("descricao"));
        tipo.setCapacidadeVolumetrica(rs.getDouble("capacidadeVolumetrica"));
        tipo.setPrecoDiario(rs.getDouble("precoDiario"));

        Cacamba cac = new Cacamba();
        cac.setPatrimonio(rs.getString("patrimonio"));
        cac.setCapacidade(rs.getDouble("capacidade"));
        cac.setCor(rs.getString("cor"));
        cac.setSituacao(rs.getString("situacao"));
        cac.setTipoCacamba(tipo);
        return cac;
    }
}
