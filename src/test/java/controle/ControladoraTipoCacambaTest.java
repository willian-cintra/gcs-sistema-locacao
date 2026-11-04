package controle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import dao.BancoDeTeste;
import dao.CacambaDAO;
import dao.TipoCacambaDAO;
import model.Cacamba;
import model.TipoCacamba;

class ControladoraTipoCacambaTest extends BancoDeTeste {

    private final ControladoraTipoCacamba controladora = new ControladoraTipoCacamba();

    private TipoCacamba tipo(String codigo, double preco) {
        TipoCacamba t = new TipoCacamba();
        t.setCodigo(codigo);
        t.setDescricao("Caçamba " + codigo);
        t.setCapacidadeVolumetrica(7.0);
        t.setPrecoDiario(preco);
        return t;
    }

    @Test
    void inserirGravaTipo() {
        controladora.inserir(tipo("TC-7M", 55.0));

        assertEquals(3, controladora.listar().size());
        assertEquals(55.0, new TipoCacambaDAO().buscar("TC-7M").getPrecoDiario(), 0.001);
    }

    @Test
    void inserirRejeitaCodigoDuplicado() {
        assertThrows(IllegalArgumentException.class, () -> controladora.inserir(tipo("TC-4M", 30.0)));
    }

    @Test
    void inserirRejeitaPrecoZero() {
        assertThrows(IllegalArgumentException.class, () -> controladora.inserir(tipo("TC-7M", 0)));
    }

    @Test
    void inserirRejeitaDescricaoVazia() {
        TipoCacamba t = tipo("TC-7M", 55.0);
        t.setDescricao("");
        assertThrows(IllegalArgumentException.class, () -> controladora.inserir(t));
    }

    @Test
    void alterarAtualizaPreco() {
        TipoCacamba t = new TipoCacambaDAO().buscar("TC-4M");
        t.setPrecoDiario(38.5);

        controladora.alterar(t);

        assertEquals(38.5, new TipoCacambaDAO().buscar("TC-4M").getPrecoDiario(), 0.001);
    }

    @Test
    void removerApagaTipoSemVinculos() {
        controladora.inserir(tipo("TC-7M", 55.0));

        controladora.remover("TC-7M");

        assertNull(new TipoCacambaDAO().buscar("TC-7M"));
    }

    @Test
    void removerRejeitaTipoComCacambas() {
        assertThrows(IllegalArgumentException.class, () -> controladora.remover("TC-4M"));
    }

    @Test
    void removerRejeitaTipoComLocacoes() {
        controladora.inserir(tipo("TC-7M", 55.0));
        Cacamba cac = new Cacamba();
        cac.setPatrimonio("PAT-070");
        cac.setCapacidade(7.0);
        cac.setSituacao("disponivel");
        cac.setTipoCacamba(new TipoCacambaDAO().buscar("TC-7M"));
        new CacambaDAO().inserir(cac);
        new ControladoraLocacao().cadastrarLocacao("12345678901", "TC-7M", 1, LocalDate.now(), "Rua A", "", 1, "PIX");
        new CacambaDAO().remover("PAT-070");

        assertThrows(IllegalArgumentException.class, () -> controladora.remover("TC-7M"));
    }
}
