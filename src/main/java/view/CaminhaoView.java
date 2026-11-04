package view;

import java.awt.Window;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

import controle.ControladoraCaminhao;
import model.Caminhao;

public class CaminhaoView extends CadastroView<Caminhao> {

    private final ControladoraCaminhao controladora = new ControladoraCaminhao();

    private final JTextField txtPlaca = new JTextField(8);
    private final JTextField txtModelo = new JTextField(25);
    private final JTextField txtAno = new JTextField(5);
    private final JTextField txtCapacidade = new JTextField(8);
    private final JComboBox<String> cbSituacao = comboSituacao(ControladoraCaminhao.SITUACOES);

    public CaminhaoView(Window dono) {
        super(dono, "Caminhões");
        txtPlaca.putClientProperty("JTextField.placeholderText", "ex.: ABC1D23");
        montarTela();
    }

    @Override
    protected void montarFormulario() {
        adicionarCampo("Placa:", txtPlaca);
        adicionarCampo("Modelo:", txtModelo);
        adicionarCampo("Ano de fabricação:", txtAno);
        adicionarCampo("Capacidade de carga (t):", txtCapacidade);
        adicionarCampo("Situação:", cbSituacao);
    }

    @Override
    protected JComponent campoChave() {
        return txtPlaca;
    }

    @Override
    protected String[] colunas() {
        return new String[] {"Placa", "Modelo", "Ano", "Carga (t)", "Situação"};
    }

    @Override
    protected Object[] linha(Caminhao c) {
        return new Object[] {c.getPlaca(), c.getModelo(), c.getAnoFabricacao(), decimal(c.getCapacidadeCarga()),
                rotuloSituacao(c.getSituacao())};
    }

    @Override
    protected List<Caminhao> listar() {
        return controladora.listar();
    }

    @Override
    protected Caminhao lerFormulario() {
        Caminhao c = new Caminhao();
        c.setPlaca(texto(txtPlaca).toUpperCase().replace("-", ""));
        c.setModelo(texto(txtModelo));
        c.setAnoFabricacao(lerInteiro(txtAno, "Ano de fabricação"));
        c.setCapacidadeCarga(lerDecimal(txtCapacidade, "Capacidade de carga"));
        c.setSituacao((String) cbSituacao.getSelectedItem());
        return c;
    }

    @Override
    protected void preencher(Caminhao c) {
        txtPlaca.setText(c.getPlaca());
        txtModelo.setText(c.getModelo());
        txtAno.setText(String.valueOf(c.getAnoFabricacao()));
        txtCapacidade.setText(decimal(c.getCapacidadeCarga()));
        cbSituacao.setSelectedItem(c.getSituacao());
    }

    @Override
    protected void inserir(Caminhao c) {
        controladora.inserir(c);
    }

    @Override
    protected void alterar(Caminhao c) {
        controladora.alterar(c);
    }

    @Override
    protected void remover(Caminhao c) {
        controladora.remover(c.getPlaca());
    }

    @Override
    protected String descricao(Caminhao c) {
        return "o caminhão " + c.getPlaca();
    }
}
