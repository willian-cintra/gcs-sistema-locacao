package view;

import java.awt.Window;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTextField;

import controle.ControladoraCliente;
import model.Cliente;

public class ClienteView extends CadastroView<Cliente> {

    private final ControladoraCliente controladora = new ControladoraCliente();

    private final JTextField txtCpfCnpj = new JTextField(14);
    private final JTextField txtNome = new JTextField(30);
    private final JTextField txtEndereco = new JTextField(30);
    private final JTextField txtCidade = new JTextField(20);
    private final JTextField txtEstado = new JTextField(2);
    private final JTextField txtTelefone = new JTextField(15);
    private final JTextField txtEmail = new JTextField(30);

    public ClienteView(Window dono) {
        super(dono, "Clientes");
        txtCpfCnpj.putClientProperty("JTextField.placeholderText", "somente números");
        txtEstado.putClientProperty("JTextField.placeholderText", "ex.: MS");
        montarTela();
    }

    @Override
    protected void montarFormulario() {
        adicionarCampo("CPF/CNPJ:", txtCpfCnpj);
        adicionarCampo("Nome/razão social:", txtNome);
        adicionarCampo("Endereço:", txtEndereco);
        adicionarCampo("Cidade:", txtCidade);
        adicionarCampo("Estado:", txtEstado);
        adicionarCampo("Telefone:", txtTelefone);
        adicionarCampo("E-mail:", txtEmail);
    }

    @Override
    protected JComponent campoChave() {
        return txtCpfCnpj;
    }

    @Override
    protected String[] colunas() {
        return new String[] {"CPF/CNPJ", "Nome/razão social", "Cidade", "UF", "Telefone"};
    }

    @Override
    protected Object[] linha(Cliente c) {
        return new Object[] {c.getCpfCnpj(), c.getNomeRazaoSocial(), c.getCidade(), c.getEstado(), c.getTelefone()};
    }

    @Override
    protected List<Cliente> listar() {
        return controladora.listar();
    }

    @Override
    protected Cliente lerFormulario() {
        Cliente c = new Cliente();
        c.setCpfCnpj(texto(txtCpfCnpj));
        c.setNomeRazaoSocial(texto(txtNome));
        c.setEndereco(opcional(txtEndereco));
        c.setCidade(opcional(txtCidade));
        c.setEstado(opcional(txtEstado) == null ? null : texto(txtEstado).toUpperCase());
        c.setTelefone(opcional(txtTelefone));
        c.setEmail(opcional(txtEmail));
        return c;
    }

    @Override
    protected void preencher(Cliente c) {
        txtCpfCnpj.setText(c.getCpfCnpj());
        txtNome.setText(c.getNomeRazaoSocial());
        txtEndereco.setText(c.getEndereco());
        txtCidade.setText(c.getCidade());
        txtEstado.setText(c.getEstado());
        txtTelefone.setText(c.getTelefone());
        txtEmail.setText(c.getEmail());
    }

    @Override
    protected void inserir(Cliente c) {
        controladora.inserir(c);
    }

    @Override
    protected void alterar(Cliente c) {
        controladora.alterar(c);
    }

    @Override
    protected void remover(Cliente c) {
        controladora.remover(c.getCpfCnpj());
    }

    @Override
    protected String descricao(Cliente c) {
        return "o cliente " + c.getNomeRazaoSocial();
    }
}
