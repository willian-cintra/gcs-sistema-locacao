package view;

import java.awt.Window;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

import controle.ControladoraCacamba;
import model.Cacamba;
import model.TipoCacamba;

public class CacambaView extends CadastroView<Cacamba> {

    private final ControladoraCacamba controladora = new ControladoraCacamba();

    private final JTextField txtPatrimonio = new JTextField(10);
    private final JComboBox<TipoCacamba> cbTipo = new JComboBox<>();
    private final JTextField txtCapacidade = new JTextField(8);
    private final JTextField txtCor = new JTextField(15);
    private final JComboBox<String> cbSituacao = comboSituacao(ControladoraCacamba.SITUACOES);

    public CacambaView(Window dono) {
        super(dono, "Caçambas");
        for (TipoCacamba tipo : controladora.listarTipos()) {
            cbTipo.addItem(tipo);
        }
        montarTela();
    }

    @Override
    protected void montarFormulario() {
        adicionarCampo("Patrimônio:", txtPatrimonio);
        adicionarCampo("Tipo:", cbTipo);
        adicionarCampo("Capacidade (m³):", txtCapacidade);
        adicionarCampo("Cor:", txtCor);
        adicionarCampo("Situação:", cbSituacao);
    }

    @Override
    protected JComponent campoChave() {
        return txtPatrimonio;
    }

    @Override
    protected String[] colunas() {
        return new String[] {"Patrimônio", "Tipo", "Capacidade (m³)", "Cor", "Situação"};
    }

    @Override
    protected Object[] linha(Cacamba c) {
        return new Object[] {c.getPatrimonio(), c.getTipoCacamba().getDescricao(), decimal(c.getCapacidade()),
                c.getCor(), rotuloSituacao(c.getSituacao())};
    }

    @Override
    protected List<Cacamba> listar() {
        return controladora.listar();
    }

    @Override
    protected Cacamba lerFormulario() {
        Cacamba c = new Cacamba();
        c.setPatrimonio(texto(txtPatrimonio));
        c.setTipoCacamba((TipoCacamba) cbTipo.getSelectedItem());
        c.setCapacidade(lerDecimal(txtCapacidade, "Capacidade"));
        c.setCor(opcional(txtCor));
        c.setSituacao((String) cbSituacao.getSelectedItem());
        return c;
    }

    @Override
    protected void preencher(Cacamba c) {
        txtPatrimonio.setText(c.getPatrimonio());
        // O tipo da caçamba e outro objeto, entao a selecao e feita pelo codigo
        for (int i = 0; i < cbTipo.getItemCount(); i++) {
            if (cbTipo.getItemAt(i).getCodigo().equals(c.getTipoCacamba().getCodigo())) {
                cbTipo.setSelectedIndex(i);
            }
        }
        txtCapacidade.setText(decimal(c.getCapacidade()));
        txtCor.setText(c.getCor());
        cbSituacao.setSelectedItem(c.getSituacao());
    }

    @Override
    protected void inserir(Cacamba c) {
        controladora.inserir(c);
    }

    @Override
    protected void alterar(Cacamba c) {
        controladora.alterar(c);
    }

    @Override
    protected void remover(Cacamba c) {
        controladora.remover(c.getPatrimonio());
    }

    @Override
    protected String descricao(Cacamba c) {
        return "a caçamba " + c.getPatrimonio();
    }
}
