package view;

import java.awt.Window;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

import controle.ControladoraMotorista;
import model.Motorista;

public class MotoristaView extends CadastroView<Motorista> {

    private final ControladoraMotorista controladora = new ControladoraMotorista();

    private final JTextField txtMatricula = new JTextField(10);
    private final JTextField txtNome = new JTextField(30);
    private final JTextField txtCpf = new JTextField(11);
    private final JTextField txtCnh = new JTextField(11);
    private final JComboBox<String> cbCategoria =
            new JComboBox<>(ControladoraMotorista.CATEGORIAS_CNH.toArray(new String[0]));
    private final JTextField txtTelefone = new JTextField(15);

    public MotoristaView(Window dono) {
        super(dono, "Motoristas");
        txtCpf.putClientProperty("JTextField.placeholderText", "somente números");
        txtCnh.putClientProperty("JTextField.placeholderText", "somente números");
        montarTela();
    }

    @Override
    protected void montarFormulario() {
        adicionarCampo("Matrícula:", txtMatricula);
        adicionarCampo("Nome:", txtNome);
        adicionarCampo("CPF:", txtCpf);
        adicionarCampo("CNH:", txtCnh);
        adicionarCampo("Categoria da CNH:", cbCategoria);
        adicionarCampo("Telefone:", txtTelefone);
    }

    @Override
    protected JComponent campoChave() {
        return txtMatricula;
    }

    @Override
    protected String[] colunas() {
        return new String[] {"Matrícula", "Nome", "CPF", "CNH", "Categoria", "Telefone"};
    }

    @Override
    protected Object[] linha(Motorista m) {
        return new Object[] {m.getMatricula(), m.getNome(), m.getCpf(), m.getCnh(), m.getCategoriaCnh(),
                m.getTelefone()};
    }

    @Override
    protected List<Motorista> listar() {
        return controladora.listar();
    }

    @Override
    protected Motorista lerFormulario() {
        Motorista m = new Motorista();
        m.setMatricula(texto(txtMatricula));
        m.setNome(texto(txtNome));
        m.setCpf(texto(txtCpf));
        m.setCnh(texto(txtCnh));
        m.setCategoriaCnh((String) cbCategoria.getSelectedItem());
        m.setTelefone(opcional(txtTelefone));
        return m;
    }

    @Override
    protected void preencher(Motorista m) {
        txtMatricula.setText(m.getMatricula());
        txtNome.setText(m.getNome());
        txtCpf.setText(m.getCpf());
        txtCnh.setText(m.getCnh());
        cbCategoria.setSelectedItem(m.getCategoriaCnh());
        txtTelefone.setText(m.getTelefone());
    }

    @Override
    protected void inserir(Motorista m) {
        controladora.inserir(m);
    }

    @Override
    protected void alterar(Motorista m) {
        controladora.alterar(m);
    }

    @Override
    protected void remover(Motorista m) {
        controladora.remover(m.getMatricula());
    }

    @Override
    protected String descricao(Motorista m) {
        return "o motorista " + m.getNome();
    }
}
