package com.ibm.sisapp.model;

public record DadosCadastroFuncionario(Long id, String nome, String funcao, String dtadm, double salario) {
    // O id agora é um campo do registro, permitindo que você o use para atualização e identificação do funcionário.
}
