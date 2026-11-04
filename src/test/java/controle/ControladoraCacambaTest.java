package controle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import dao.BancoDeTeste;
import dao.CacambaDAO;
import dao.TipoCacambaDAO;
import model.Cacamba;
import model.TipoCacamba;

class ControladoraCacambaTest extends BancoDeTeste {

    private final ControladoraCacamba controladora = new ControladoraCacamba();

    private Cacamba cacamba(String patrimonio, String codigoTipo) {
        Cacamba c = new Cacamba();
        c.setPatrimonio(patrimonio);
        c.setCapacidade(5.0);
        c.setCor("azul");
        c.setSituacao("disponivel");
        c.setTipoCacamba(new TipoCacambaDAO().buscar(codigoTipo));
        return c;
    }

    @Test
    void inserirGravaCacambaComTipo() {
        controladora.inserir(cacamba("PAT-010", "TC-5M"));

        Cacamba gravada = new CacambaDAO().buscar("PAT-010");
        assertEquals("TC-5M", gravada.getTipoCacamba().getCodigo());
        assertEquals(42.0, gravada.getTipoCacamba().getPrecoDiario(), 0.001);
        assertEquals(4, controladora.listar().size());
    }

    @Test
    void inserirRejeitaPatrimonioDuplicado() {
        assertThrows(IllegalArgumentException.class, () -> controladora.inserir(cacamba("PAT-001", "TC-4M")));
    }

    @Test
    void inserirRejeitaTipoNaoSelecionado() {
        Cacamba c = cacamba("PAT-010", "TC-5M");
        c.setTipoCacamba(null);
        assertThrows(IllegalArgumentException.class, () -> controladora.inserir(c));
    }

    @Test
    void inserirRejeitaTipoInexistente() {
        TipoCacamba inexistente = new TipoCacamba();
        inexistente.setCodigo("NAO-EXISTE");
        Cacamba c = cacamba("PAT-010", "TC-5M");
        c.setTipoCacamba(inexistente);
        assertThrows(IllegalArgumentException.class, () -> controladora.inserir(c));
    }

    @Test
    void inserirRejeitaSituacaoInvalida() {
        Cacamba c = cacamba("PAT-010", "TC-5M");
        c.setSituacao("quebrada");
        assertThrows(IllegalArgumentException.class, () -> controladora.inserir(c));
    }

    @Test
    void alterarSituacaoTiraCacambaDaDisponibilidade() {
        Cacamba c = new CacambaDAO().buscar("PAT-001");
        c.setSituacao("manutencao");

        controladora.alterar(c);

        assertEquals(1, new CacambaDAO().contarDisponiveisPorTipo("TC-4M"));
    }

    @Test
    void removerApagaCacamba() {
        controladora.remover("PAT-003");

        assertNull(new CacambaDAO().buscar("PAT-003"));
    }

    @Test
    void removerRejeitaCacambaLocada() {
        Cacamba c = new CacambaDAO().buscar("PAT-001");
        c.setSituacao("locada");
        controladora.alterar(c);

        assertThrows(IllegalArgumentException.class, () -> controladora.remover("PAT-001"));
    }
}
