package controle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import dao.BancoDeTeste;
import dao.CaminhaoDAO;
import model.Caminhao;

class ControladoraCaminhaoTest extends BancoDeTeste {

    private final ControladoraCaminhao controladora = new ControladoraCaminhao();

    private Caminhao caminhao(String placa) {
        Caminhao c = new Caminhao();
        c.setPlaca(placa);
        c.setModelo("Volkswagen Delivery 11.180");
        c.setAnoFabricacao(2021);
        c.setCapacidadeCarga(7.5);
        c.setSituacao("disponivel");
        return c;
    }

    @Test
    void inserirAceitaPlacaMercosulEAntiga() {
        controladora.inserir(caminhao("XYZ9A87"));
        controladora.inserir(caminhao("XYZ1234"));

        assertEquals(3, controladora.listar().size());
    }

    @Test
    void inserirRejeitaPlacaDuplicada() {
        assertThrows(IllegalArgumentException.class, () -> controladora.inserir(caminhao("ABC1D23")));
    }

    @Test
    void inserirRejeitaPlacaInvalida() {
        assertThrows(IllegalArgumentException.class, () -> controladora.inserir(caminhao("ABC-1234")));
        assertThrows(IllegalArgumentException.class, () -> controladora.inserir(caminhao("abc1d23")));
    }

    @Test
    void inserirRejeitaAnoForaDoIntervalo() {
        Caminhao c = caminhao("XYZ9A87");
        c.setAnoFabricacao(1900);
        assertThrows(IllegalArgumentException.class, () -> controladora.inserir(c));
    }

    @Test
    void inserirRejeitaCapacidadeZero() {
        Caminhao c = caminhao("XYZ9A87");
        c.setCapacidadeCarga(0);
        assertThrows(IllegalArgumentException.class, () -> controladora.inserir(c));
    }

    @Test
    void alterarAtualizaSituacao() {
        Caminhao c = new CaminhaoDAO().buscar("ABC1D23");
        c.setSituacao("manutencao");

        controladora.alterar(c);

        assertEquals("manutencao", new CaminhaoDAO().buscar("ABC1D23").getSituacao());
    }

    @Test
    void removerApagaCaminhao() {
        controladora.remover("ABC1D23");

        assertNull(new CaminhaoDAO().buscar("ABC1D23"));
    }

    @Test
    void removerRejeitaCaminhaoInexistente() {
        assertThrows(IllegalArgumentException.class, () -> controladora.remover("ZZZ0000"));
    }
}
