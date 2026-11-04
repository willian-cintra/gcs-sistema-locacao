package controle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import dao.BancoDeTeste;
import dao.ClienteDAO;
import model.Cliente;

class ControladoraClienteTest extends BancoDeTeste {

    private final ControladoraCliente controladora = new ControladoraCliente();

    private Cliente cliente(String cpfCnpj, String nome) {
        Cliente c = new Cliente();
        c.setCpfCnpj(cpfCnpj);
        c.setNomeRazaoSocial(nome);
        c.setCidade("Dourados");
        c.setEstado("MS");
        c.setEmail("maria@exemplo.com");
        return c;
    }

    @Test
    void inserirGravaCliente() {
        controladora.inserir(cliente("11122233344", "Maria Souza"));

        assertEquals(2, controladora.listar().size());
        assertEquals("Maria Souza", new ClienteDAO().buscar("11122233344").getNomeRazaoSocial());
    }

    @Test
    void inserirAceitaCnpj() {
        controladora.inserir(cliente("12345678000199", "Obras Ltda"));

        assertEquals("Obras Ltda", new ClienteDAO().buscar("12345678000199").getNomeRazaoSocial());
    }

    @Test
    void inserirRejeitaCpfCnpjDuplicado() {
        assertThrows(IllegalArgumentException.class, () -> controladora.inserir(cliente("12345678901", "Outro")));
    }

    @Test
    void inserirRejeitaCpfComPontuacaoOuTamanhoErrado() {
        assertThrows(IllegalArgumentException.class, () -> controladora.inserir(cliente("111.222.333-44", "Ana")));
        assertThrows(IllegalArgumentException.class, () -> controladora.inserir(cliente("123", "Ana")));
    }

    @Test
    void inserirRejeitaNomeVazio() {
        assertThrows(IllegalArgumentException.class, () -> controladora.inserir(cliente("11122233344", " ")));
    }

    @Test
    void inserirRejeitaEstadoEEmailInvalidos() {
        Cliente estadoInvalido = cliente("11122233344", "Ana");
        estadoInvalido.setEstado("Mato Grosso do Sul");
        assertThrows(IllegalArgumentException.class, () -> controladora.inserir(estadoInvalido));

        Cliente emailInvalido = cliente("11122233344", "Ana");
        emailInvalido.setEmail("ana.exemplo.com");
        assertThrows(IllegalArgumentException.class, () -> controladora.inserir(emailInvalido));
    }

    @Test
    void alterarAtualizaDados() {
        Cliente c = new ClienteDAO().buscar("12345678901");
        c.setTelefone("67911112222");

        controladora.alterar(c);

        assertEquals("67911112222", new ClienteDAO().buscar("12345678901").getTelefone());
    }

    @Test
    void alterarRejeitaClienteInexistente() {
        assertThrows(IllegalArgumentException.class, () -> controladora.alterar(cliente("99988877766", "Ninguém")));
    }

    @Test
    void removerApagaCliente() {
        controladora.inserir(cliente("11122233344", "Maria Souza"));

        controladora.remover("11122233344");

        assertEquals(1, controladora.listar().size());
    }

    @Test
    void removerRejeitaClienteComLocacao() {
        new ControladoraLocacao().cadastrarLocacao("12345678901", "TC-4M", 1, LocalDate.now(), "Rua A", "", 1, "PIX");

        assertThrows(IllegalArgumentException.class, () -> controladora.remover("12345678901"));
    }
}
