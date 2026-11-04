package controle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import dao.BancoDeTeste;
import dao.Conexao;
import dao.LocacaoDAO;
import dao.OrdemDeServicoDAO;
import model.Locacao;
import model.OrdemDeServico;

class ControladoraLocacaoTest extends BancoDeTeste {

    private static final String CLIENTE = "12345678901";

    private final ControladoraLocacao controladora = new ControladoraLocacao();
    private final LocalDate amanha = LocalDate.now().plusDays(1);

    @Test
    void existeClienteCadastrado() {
        assertTrue(controladora.existeCliente(CLIENTE));
    }

    @Test
    void naoExisteClienteNaoCadastrado() {
        assertFalse(controladora.existeCliente("00000000000"));
    }

    @Test
    void listaTiposDeCacambaParaSelecao() {
        assertEquals(2, controladora.listarTiposCacamba().size());
    }

    @Test
    void consultaDisponibilidadeContaCacambasDisponiveisDoTipo() {
        assertEquals(2, controladora.consultaDisponibilidade("TC-4M", 1));
        assertEquals(1, controladora.consultaDisponibilidade("TC-5M", 1));
    }

    @Test
    void consultaDisponibilidadeRejeitaTipoInexistente() {
        assertThrows(IllegalArgumentException.class, () -> controladora.consultaDisponibilidade("XX", 1));
    }

    @Test
    void consultaDisponibilidadeRejeitaTipoNaoSelecionado() {
        assertThrows(IllegalArgumentException.class, () -> controladora.consultaDisponibilidade(null, 1));
    }

    @Test
    void consultaDisponibilidadeRejeitaQuantidadeZero() {
        assertThrows(IllegalArgumentException.class, () -> controladora.consultaDisponibilidade("TC-4M", 0));
    }

    @Test
    void calcularValorMultiplicaPrecoDiarioPorQtdeEPeriodo() {
        assertEquals(350.0, controladora.calcularValorLocacao("TC-4M", 2, 5), 0.001);
        assertEquals(126.0, controladora.calcularValorLocacao("TC-5M", 1, 3), 0.001);
    }

    @Test
    void calcularValorRejeitaPeriodoZero() {
        assertThrows(IllegalArgumentException.class, () -> controladora.calcularValorLocacao("TC-4M", 1, 0));
    }

    @Test
    void cadastrarLocacaoGravaLocacaoComDadosInformados() {
        controladora.cadastrarLocacao(CLIENTE, "TC-4M", 2, amanha, "Rua A, 10", "portao azul", 5, "PIX");

        Locacao loc = new LocacaoDAO().buscar("1");
        assertEquals(CLIENTE, loc.getCliente().getCpfCnpj());
        assertEquals("TC-4M", loc.getTipoCacamba().getCodigo());
        assertEquals(2, loc.getQtdeSolicitada());
        assertEquals(amanha, loc.getDataPrevistaEntrega());
        assertEquals(LocalDate.now(), loc.getDataSolicitacao());
        assertEquals("Rua A, 10", loc.getEnderecoEntrega());
        assertEquals("portao azul", loc.getObservacoes());
        assertEquals(5, loc.getPeriodoPrevistoDias());
        assertEquals("PIX", loc.getFormaPagamento());
        assertEquals(35.0, loc.getValorDiariaContratado(), 0.001);
        assertEquals(350.0, loc.getValorTotal(), 0.001);
        assertEquals("ativa", loc.getSituacao());
    }

    @Test
    void cadastrarLocacaoGeraOrdemDeServicoDeEntrega() {
        controladora.cadastrarLocacao(CLIENTE, "TC-4M", 1, amanha, "Rua A, 10", "", 3, "Boleto");

        OrdemDeServico os = new OrdemDeServicoDAO().buscar("1");
        assertEquals("Entrega", os.getTipoOS());
        assertEquals(amanha, os.getDataPrevista());
        assertEquals("agendada", os.getSituacao());
        assertEquals("1", os.getLocacao().getCodigo());
    }

    @Test
    void cadastrarLocacaoGeraCodigosSequenciais() {
        controladora.cadastrarLocacao(CLIENTE, "TC-4M", 1, amanha, "Rua A, 10", "", 1, "PIX");
        controladora.cadastrarLocacao(CLIENTE, "TC-5M", 1, amanha, "Rua B, 20", "", 1, "PIX");

        assertEquals("TC-4M", new LocacaoDAO().buscar("1").getTipoCacamba().getCodigo());
        assertEquals("TC-5M", new LocacaoDAO().buscar("2").getTipoCacamba().getCodigo());
        assertEquals(2, new LocacaoDAO().listar().size());
    }

    @Test
    void cadastrarLocacaoRejeitaClienteInexistenteSemGravarNada() throws SQLException {
        assertThrows(IllegalArgumentException.class,
                () -> controladora.cadastrarLocacao("00000000000", "TC-4M", 1, amanha, "Rua A", "", 1, "PIX"));
        assertEquals(0, contar("Locacao"));
        assertEquals(0, contar("OrdemDeServico"));
    }

    @Test
    void cadastrarLocacaoRejeitaQuantidadeAcimaDaDisponivel() {
        assertThrows(IllegalArgumentException.class,
                () -> controladora.cadastrarLocacao(CLIENTE, "TC-5M", 2, amanha, "Rua A", "", 1, "PIX"));
    }

    @Test
    void cadastrarLocacaoRejeitaDataDeEntregaNoPassado() {
        LocalDate ontem = LocalDate.now().minusDays(1);
        assertThrows(IllegalArgumentException.class,
                () -> controladora.cadastrarLocacao(CLIENTE, "TC-4M", 1, ontem, "Rua A", "", 1, "PIX"));
    }

    @Test
    void cadastrarLocacaoRejeitaEnderecoVazio() {
        assertThrows(IllegalArgumentException.class,
                () -> controladora.cadastrarLocacao(CLIENTE, "TC-4M", 1, amanha, "  ", "", 1, "PIX"));
    }

    private int contar(String tabela) throws SQLException {
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM " + tabela);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }
}
