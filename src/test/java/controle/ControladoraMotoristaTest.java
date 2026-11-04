package controle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import dao.BancoDeTeste;
import dao.MotoristaDAO;
import model.Motorista;

class ControladoraMotoristaTest extends BancoDeTeste {

    private final ControladoraMotorista controladora = new ControladoraMotorista();

    private Motorista motorista(String matricula) {
        Motorista m = new Motorista();
        m.setMatricula(matricula);
        m.setNome("Carlos Lima");
        m.setCpf("11122233344");
        m.setCnh("98765432109");
        m.setCategoriaCnh("E");
        return m;
    }

    @Test
    void inserirGravaMotorista() {
        controladora.inserir(motorista("M002"));

        assertEquals(2, controladora.listar().size());
        assertEquals("E", new MotoristaDAO().buscar("M002").getCategoriaCnh());
    }

    @Test
    void inserirRejeitaMatriculaDuplicada() {
        assertThrows(IllegalArgumentException.class, () -> controladora.inserir(motorista("M001")));
    }

    @Test
    void inserirRejeitaCpfECnhInvalidos() {
        Motorista cpfInvalido = motorista("M002");
        cpfInvalido.setCpf("111.222.333-44");
        assertThrows(IllegalArgumentException.class, () -> controladora.inserir(cpfInvalido));

        Motorista cnhInvalida = motorista("M002");
        cnhInvalida.setCnh("123");
        assertThrows(IllegalArgumentException.class, () -> controladora.inserir(cnhInvalida));
    }

    @Test
    void inserirRejeitaCategoriaQueNaoDirigeCaminhao() {
        Motorista m = motorista("M002");
        m.setCategoriaCnh("B");
        assertThrows(IllegalArgumentException.class, () -> controladora.inserir(m));
    }

    @Test
    void alterarAtualizaCategoria() {
        Motorista m = new MotoristaDAO().buscar("M001");
        m.setCategoriaCnh("E");

        controladora.alterar(m);

        assertEquals("E", new MotoristaDAO().buscar("M001").getCategoriaCnh());
    }

    @Test
    void removerApagaMotorista() {
        controladora.remover("M001");

        assertNull(new MotoristaDAO().buscar("M001"));
    }

    @Test
    void removerRejeitaMotoristaInexistente() {
        assertThrows(IllegalArgumentException.class, () -> controladora.remover("M999"));
    }
}
