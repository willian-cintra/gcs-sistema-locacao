package controle;

import java.util.List;

import dao.ClienteDAO;
import dao.LocacaoDAO;
import model.Cliente;

public class ControladoraCliente {

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final LocacaoDAO locacaoDAO = new LocacaoDAO();

    public void inserir(Cliente cli) {
        validar(cli);
        if (clienteDAO.existe(cli.getCpfCnpj())) {
            throw new IllegalArgumentException("Já existe cliente com o CPF/CNPJ " + cli.getCpfCnpj() + ".");
        }
        clienteDAO.inserir(cli);
    }

    public void alterar(Cliente cli) {
        validar(cli);
        exigirExistente(cli.getCpfCnpj());
        clienteDAO.alterar(cli);
    }

    public void remover(String cpfCnpj) {
        exigirExistente(cpfCnpj);
        if (locacaoDAO.existePorCliente(cpfCnpj)) {
            throw new IllegalArgumentException("O cliente possui locações e não pode ser removido.");
        }
        clienteDAO.remover(cpfCnpj);
    }

    public List<Cliente> listar() {
        return clienteDAO.listar();
    }

    private void validar(Cliente cli) {
        Validacoes.digitos(cli.getCpfCnpj(), "CPF/CNPJ deve ter 11 (CPF) ou 14 (CNPJ) dígitos, sem pontuação.", 11, 14);
        Validacoes.obrigatorio(cli.getNomeRazaoSocial(), "Nome/razão social");
        String uf = cli.getEstado();
        if (uf != null && !uf.isBlank() && !uf.matches("[A-Z]{2}")) {
            throw new IllegalArgumentException("Estado deve ser a sigla com 2 letras maiúsculas (ex.: MS).");
        }
        String email = cli.getEmail();
        if (email != null && !email.isBlank() && !email.matches("[^@\\s]+@[^@\\s]+\\.[^@\\s]+")) {
            throw new IllegalArgumentException("E-mail inválido.");
        }
    }

    private void exigirExistente(String cpfCnpj) {
        if (!clienteDAO.existe(cpfCnpj)) {
            throw new IllegalArgumentException("Cliente " + cpfCnpj + " não encontrado.");
        }
    }
}
