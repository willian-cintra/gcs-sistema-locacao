package model;

/**
 * Tipo de cacamba, que define a capacidade e o preco diario cobrado na locacao.
 */
public class TipoCacamba {

    private String codigo;
    private String descricao;
    private double capacidadeVolumetrica;
    private double precoDiario;

    public TipoCacamba() {
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getCapacidadeVolumetrica() {
        return capacidadeVolumetrica;
    }

    public void setCapacidadeVolumetrica(double capacidadeVolumetrica) {
        this.capacidadeVolumetrica = capacidadeVolumetrica;
    }

    public double getPrecoDiario() {
        return precoDiario;
    }

    public void setPrecoDiario(double precoDiario) {
        this.precoDiario = precoDiario;
    }

    @Override
    public String toString() {
        return descricao + " (" + codigo + ")";
    }
}
