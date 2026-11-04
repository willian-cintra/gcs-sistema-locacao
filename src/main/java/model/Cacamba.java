package model;

/**
 * Cacamba fisica, identificada pelo numero de patrimonio.
 * A situacao 'disponivel' indica que ela pode ser locada.
 */
public class Cacamba {

    private String patrimonio;
    private double capacidade;
    private String cor;
    private String situacao;

    private TipoCacamba tipoCacamba;

    public Cacamba() {
    }

    public String getPatrimonio() {
        return patrimonio;
    }

    public void setPatrimonio(String patrimonio) {
        this.patrimonio = patrimonio;
    }

    public double getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(double capacidade) {
        this.capacidade = capacidade;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public TipoCacamba getTipoCacamba() {
        return tipoCacamba;
    }

    public void setTipoCacamba(TipoCacamba tipoCacamba) {
        this.tipoCacamba = tipoCacamba;
    }
}
