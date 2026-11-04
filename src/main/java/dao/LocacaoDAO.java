package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.Locacao;

/**
 * As datas sao gravadas como texto no formato aaaa-mm-dd.
 */
public class LocacaoDAO {

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final TipoCacambaDAO tipoCacambaDAO = new TipoCacambaDAO();

    /** Quando a locacao nao tem codigo, gera o proximo da sequencia e grava no objeto. */
    public void inserir(Locacao loc) {
        String sql = "INSERT INTO Locacao (codigo, enderecoEntrega, dataSolicitacao, dataPrevistaEntrega, "
                + "periodoPrevistoDias, qtdeSolicitada, observacoes, valorDiariaContratado, valorTotal, "
                + "formaPagamento, dataEncerramento, situacao, cpfCnpjCliente, codigoTipoCacamba) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexao.getConexao()) {
            if (loc.getCodigo() == null) {
                loc.setCodigo(gerarCodigo(con));
            }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, loc.getCodigo());
                ps.setString(2, loc.getEnderecoEntrega());
                ps.setString(3, data(loc.getDataSolicitacao()));
                ps.setString(4, data(loc.getDataPrevistaEntrega()));
                ps.setInt(5, loc.getPeriodoPrevistoDias());
                ps.setInt(6, loc.getQtdeSolicitada());
                ps.setString(7, loc.getObservacoes());
                ps.setDouble(8, loc.getValorDiariaContratado());
                ps.setDouble(9, loc.getValorTotal());
                ps.setString(10, loc.getFormaPagamento());
                ps.setString(11, data(loc.getDataEncerramento()));
                ps.setString(12, loc.getSituacao());
                ps.setString(13, loc.getCliente().getCpfCnpj());
                ps.setString(14, loc.getTipoCacamba().getCodigo());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir locação: " + e.getMessage(), e);
        }
    }

    public void alterar(Locacao loc) {
        String sql = "UPDATE Locacao SET enderecoEntrega = ?, dataSolicitacao = ?, dataPrevistaEntrega = ?, "
                + "periodoPrevistoDias = ?, qtdeSolicitada = ?, observacoes = ?, valorDiariaContratado = ?, "
                + "valorTotal = ?, formaPagamento = ?, dataEncerramento = ?, situacao = ?, "
                + "cpfCnpjCliente = ?, codigoTipoCacamba = ? WHERE codigo = ?";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, loc.getEnderecoEntrega());
            ps.setString(2, data(loc.getDataSolicitacao()));
            ps.setString(3, data(loc.getDataPrevistaEntrega()));
            ps.setInt(4, loc.getPeriodoPrevistoDias());
            ps.setInt(5, loc.getQtdeSolicitada());
            ps.setString(6, loc.getObservacoes());
            ps.setDouble(7, loc.getValorDiariaContratado());
            ps.setDouble(8, loc.getValorTotal());
            ps.setString(9, loc.getFormaPagamento());
            ps.setString(10, data(loc.getDataEncerramento()));
            ps.setString(11, loc.getSituacao());
            ps.setString(12, loc.getCliente().getCpfCnpj());
            ps.setString(13, loc.getTipoCacamba().getCodigo());
            ps.setString(14, loc.getCodigo());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao alterar locação: " + e.getMessage(), e);
        }
    }

    /** Devolve a locacao ja com cliente e tipo de cacamba, ou null se nao existir. */
    public Locacao buscar(String codigo) {
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM Locacao WHERE codigo = ?")) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? montar(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar locação: " + e.getMessage(), e);
        }
    }

    public List<Locacao> listar() {
        List<Locacao> locacoes = new ArrayList<>();
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM Locacao ORDER BY CAST(codigo AS INTEGER)");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                locacoes.add(montar(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar locações: " + e.getMessage(), e);
        }
        return locacoes;
    }

    public boolean existePorCliente(String cpfCnpj) {
        return existe("SELECT 1 FROM Locacao WHERE cpfCnpjCliente = ?", cpfCnpj);
    }

    public boolean existePorTipo(String codigoTipo) {
        return existe("SELECT 1 FROM Locacao WHERE codigoTipoCacamba = ?", codigoTipo);
    }

    private boolean existe(String sql, String parametro) {
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, parametro);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consultar locações: " + e.getMessage(), e);
        }
    }

    private Locacao montar(ResultSet rs) throws SQLException {
        Locacao loc = new Locacao();
        loc.setCodigo(rs.getString("codigo"));
        loc.setEnderecoEntrega(rs.getString("enderecoEntrega"));
        loc.setDataSolicitacao(data(rs.getString("dataSolicitacao")));
        loc.setDataEntrega(data(rs.getString("dataPrevistaEntrega")));
        loc.setPeriodoDias(rs.getInt("periodoPrevistoDias"));
        loc.setQtdeSolicitada(rs.getInt("qtdeSolicitada"));
        loc.setObservacoes(rs.getString("observacoes"));
        loc.setValorDiariaContratado(rs.getDouble("valorDiariaContratado"));
        loc.setValorTotal(rs.getDouble("valorTotal"));
        loc.setFormaPagamento(rs.getString("formaPagamento"));
        loc.setDataEncerramento(data(rs.getString("dataEncerramento")));
        loc.setSituacao(rs.getString("situacao"));
        loc.setCliente(clienteDAO.buscar(rs.getString("cpfCnpjCliente")));
        loc.setTipoCacamba(tipoCacambaDAO.buscar(rs.getString("codigoTipoCacamba")));
        return loc;
    }

    /** Retorna o maior codigo + 1. O codigo e texto, por isso e convertido para inteiro. */
    private String gerarCodigo(Connection con) throws SQLException {
        String sql = "SELECT COALESCE(MAX(CAST(codigo AS INTEGER)), 0) + 1 FROM Locacao";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return String.valueOf(rs.getInt(1));
        }
    }

    private static String data(LocalDate d) {
        return d != null ? d.toString() : null;
    }

    private static LocalDate data(String s) {
        return s != null ? LocalDate.parse(s) : null;
    }
}
