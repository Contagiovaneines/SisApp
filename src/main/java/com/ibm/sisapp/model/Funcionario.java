package com.ibm.sisapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "funcionarios")
public class Funcionario {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	String nome;
	String funcao;
	String dtadm;
	double salario;
	
	
	 public Funcionario(DadosCadastroFuncionario dados) {
	 	this.nome = dados.nome();
		this.funcao = dados.funcao();
		this.dtadm = dados.dtadm();
		this.salario = dados.salario();
	}
	 
	 //construtor para uso do JPA
	 public Funcionario() {}
	
	 @Override
		public String toString() {
			return "Funcionario ["
					+ "nome=" + nome + ", "
					+ "funcao=" + funcao + ", "
					+ "dtadm=" + dtadm + ", "
					+ "salario=" + salario + ""
					+ "]";
		}

	 public Long getId() {
	        return id;
	    }
	 
	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public String getFuncao() {
		return funcao;
	}


	public void setFuncao(String funcao) {
		this.funcao = funcao;
	}


	public String getDtadm() {
		return dtadm;
	}


	public void setDtadm(String dtadm) {
		this.dtadm = dtadm;
	}


	public double getSalario() {
		return salario;
	}


	public void setSalario(double salario) {
		this.salario = salario;
	}

	

	  public void atualizaDados(DadosCadastroFuncionario dados) {
			this.nome = dados.nome();
			this.funcao = dados.funcao();
			this.dtadm = dados.dtadm();
			this.salario = dados.salario();
			
		}
	
	
	
	
}


