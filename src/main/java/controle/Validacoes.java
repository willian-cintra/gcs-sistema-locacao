package controle;

import java.util.List;

/**
 * Validacoes de campos usadas pelas controladoras. Cada falha gera
 * IllegalArgumentException com a mensagem que sera mostrada ao usuario.
 */
final class Validacoes {

    private Validacoes() {
    }

    static void obrigatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " deve ser informado.");
        }
    }

    static void positivo(double valor, String campo) {
        if (valor <= 0) {
            throw new IllegalArgumentException(campo + " deve ser maior que zero.");
        }
    }

    /** Aceita o valor se tiver apenas digitos e uma das quantidades informadas. */
    static void digitos(String valor, String mensagem, int... quantidades) {
        if (valor != null && valor.matches("\\d+")) {
            for (int qtd : quantidades) {
                if (valor.length() == qtd) {
                    return;
                }
            }
        }
        throw new IllegalArgumentException(mensagem);
    }

    static void opcao(String valor, List<String> opcoes, String mensagem) {
        if (!opcoes.contains(valor)) {
            throw new IllegalArgumentException(mensagem);
        }
    }
}
