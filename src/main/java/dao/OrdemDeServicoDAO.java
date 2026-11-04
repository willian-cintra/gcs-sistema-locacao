package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import model.OrdemDeServico;

/**
 * As datas sao gravadas como texto no formato aaaa-mm-dd (ou aaaa-mm-ddThh:mm com hora).
 */
public class OrdemDeServicoDAO {

    private final LocacaoDAO locacaoDAO = new LocacaoDAO();

    /** Quando a OS nao tem codigo, gera o proximo da sequencia e grava no objeto. */
    public void inserir(OrdemDeServico os) {
        String sql = "INSERT INTO OrdemDeServico (codigo, tipoOS, dataPrevista, dataHoraAgendada, "
                + "dataHoraExecucao, observacoes, situacao, codigoLocacao) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexao.getConexao()) {
            if (os.getCodigo() == null) {
                os.setCodigo(gerarCodigo(con));
            }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, os.getCodigo());
                ps.setString(2, os.getTipoOS());
                ps.setString(3, texto(os.getDataPrevista()));
                ps.setString(4, texto(os.getDataHoraAgendada()));
                ps.setString(5, texto(os.getDataHoraExecucao()));
                ps.setString(6, os.getObservacoes());
                ps.setString(7, os.getSituacao());
                ps.setString(8, os.getLocacao().getCodigo());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir ordem de serviço: " + e.getMessage(), e);
        }
    }

    /** Devolve a OS ja com a locacao, ou null se nao existir. */
    public OrdemDeServico buscar(String codigo) {
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM OrdemDeServico WHERE codigo = ?")) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                OrdemDeServico os = new OrdemDeServico();
                os.setCodigo(rs.getString("codigo"));
                os.setTipoOS(rs.getString("tipoOS"));
                os.setDataPrevista(data(rs.getString("dataPrevista")));
                os.setDataHoraAgendada(dataHora(rs.getString("dataHoraAgendada")));
                os.setDataHoraExecucao(dataHora(rs.getString("dataHoraExecucao")));
                os.setObservacoes(rs.getString("observacoes"));
                os.setSituacao(rs.getString("situacao"));
                os.setLocacao(locacaoDAO.buscar(rs.getString("codigoLocacao")));
                return os;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar ordem de serviço: " + e.getMessage(), e);
        }
    }

    /** Retorna o maior codigo + 1. O codigo e texto, por isso e convertido para inteiro. */
    private String gerarCodigo(Connection con) throws SQLException {
        String sql = "SELECT COALESCE(MAX(CAST(codigo AS INTEGER)), 0) + 1 FROM OrdemDeServico";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return String.valueOf(rs.getInt(1));
        }
    }

    private static String texto(Object dataOuDataHora) {
        return dataOuDataHora != null ? dataOuDataHora.toString() : null;
    }

    private static LocalDate data(String s) {
        return s != null ? LocalDate.parse(s) : null;
    }

    private static LocalDateTime dataHora(String s) {
        return s != null ? LocalDateTime.parse(s) : null;
    }
}
