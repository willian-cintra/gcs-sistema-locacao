package view;

import java.awt.Window;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTextField;

import controle.ControladoraTipoCacamba;
import model.TipoCacamba;

public class TipoCacambaView extends CadastroView<TipoCacamba> {

    private final ControladoraTipoCacamba controladora = new ControladoraTipoCacamba();

    private final JTextField txtCodigo = new JTextField(10);
    private final JTextField txtDescricao = new JTextField(30);
    private final JTextField txtCapacidade = new JTextField(8);
    private final JTextField txtPreco = new JTextField(8);

    public TipoCacambaView(Window dono) {
        super(dono, "Tipos de caçamba");
        txtCodigo.putClientProperty("JTextField.placeholderText", "ex.: TC-4M");
        montarTela();
    }

    @Override
    protected void montarFormulario() {
        adicionarCampo("Código:", txtCodigo);
        adicionarCampo("Descrição:", txtDescricao);
        adicionarCampo("Capacidade (m³):", txtCapacidade);
        adicionarCampo("Preço diário (R$):", txtPreco);
    }

    @Override
    protected JComponent campoChave() {
        return txtCodigo;
    }

    @Override
    protected String[] colunas() {
        return new String[] {"Código", "Descrição", "Capacidade (m³)", "Preço diário (R$)"};
    }

    @Override
    protected Object[] linha(TipoCacamba t) {
        return new Object[] {t.getCodigo(), t.getDescricao(), decimal(t.getCapacidadeVolumetrica()),
                decimal(t.getPrecoDiario())};
    }

    @Override
    protected List<TipoCacamba> listar() {
        return controladora.listar();
    }

    @Override
    protected TipoCacamba lerFormulario() {
        TipoCacamba t = new TipoCacamba();
        t.setCodigo(texto(txtCodigo));
        t.setDescricao(texto(txtDescricao));
        t.setCapacidadeVolumetrica(lerDecimal(txtCapacidade, "Capacidade"));
        t.setPrecoDiario(lerDecimal(txtPreco, "Preço diário"));
        return t;
    }

    @Override
    protected void preencher(TipoCacamba t) {
        txtCodigo.setText(t.getCodigo());
        txtDescricao.setText(t.getDescricao());
        txtCapacidade.setText(decimal(t.getCapacidadeVolumetrica()));
        txtPreco.setText(decimal(t.getPrecoDiario()));
    }

    @Override
    protected void inserir(TipoCacamba t) {
        controladora.inserir(t);
    }

    @Override
    protected void alterar(TipoCacamba t) {
        controladora.alterar(t);
    }

    @Override
    protected void remover(TipoCacamba t) {
        controladora.remover(t.getCodigo());
    }

    @Override
    protected String descricao(TipoCacamba t) {
        return "o tipo " + t.getCodigo();
    }
}
