package model;

import java.time.LocalDate;

/**
 * Locacao de cacambas solicitada por um cliente.
 */
public class Locacao {

    private String codigo;
    private String enderecoEntrega;
    private LocalDate dataSolicitacao;
    private LocalDate dataPrevistaEntrega;
    private int periodoPrevistoDias;
    private int qtdeSolicitada;
    private String observacoes;
    private double valorDiariaContratado;
    private double valorTotal;
    private String formaPagamento;
    private LocalDate dataEncerramento;
    private String situacao;

    private Cliente cliente;
    private TipoCacamba tipoCacamba;

    public Locacao() {
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public void setEnderecoEntrega(String enderecoEntrega) {
        this.enderecoEntrega = enderecoEntrega;
    }

    public LocalDate getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setDataSolicitacao(LocalDate dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public LocalDate getDataPrevistaEntrega() {
        return dataPrevistaEntrega;
    }

    /** Define a data prevista de entrega. */
    public void setDataEntrega(LocalDate dataPrevistaEntrega) {
        this.dataPrevistaEntrega = dataPrevistaEntrega;
    }

    public int getPeriodoPrevistoDias() {
        return periodoPrevistoDias;
    }

    /** Define o periodo previsto da locacao, em dias. */
    public void setPeriodoDias(int periodoPrevistoDias) {
        this.periodoPrevistoDias = periodoPrevistoDias;
    }

    public int getQtdeSolicitada() {
        return qtdeSolicitada;
    }

    public void setQtdeSolicitada(int qtdeSolicitada) {
        this.qtdeSolicitada = qtdeSolicitada;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public double getValorDiariaContratado() {
        return valorDiariaContratado;
    }

    public void setValorDiariaContratado(double valorDiariaContratado) {
        this.valorDiariaContratado = valorDiariaContratado;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public LocalDate getDataEncerramento() {
        return dataEncerramento;
    }

    public void setDataEncerramento(LocalDate dataEncerramento) {
        this.dataEncerramento = dataEncerramento;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public TipoCacamba getTipoCacamba() {
        return tipoCacamba;
    }

    public void setTipoCacamba(TipoCacamba tipoCacamba) {
        this.tipoCacamba = tipoCacamba;
    }
}
