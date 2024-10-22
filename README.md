
![Projeto](projeto.gif)

# CRUD de Funcionários com Spring Boot e MySQL

Este projeto é um CRUD (Create, Read, Update, Delete) para cadastro de funcionários utilizando Spring Boot, Thymeleaf para renderização de páginas e MySQL para persistência dos dados.

## Pré-requisitos

Antes de começar, certifique-se de que você tem o seguinte instalado no seu ambiente de desenvolvimento:

- Java Development Kit (JDK) 11 ou superior
- Maven
- MySQL
- IDE de sua preferência (Eclipse, IntelliJ, VSCode)
- XAMPP (opcional, para gerenciar o MySQL)
- Navegador da Web

## 1. Criando o Projeto Spring Boot

### 1.1. Spring Initializr
1. Acesse o [Spring Initializr](https://start.spring.io/).
2. Configure o projeto com as seguintes opções:
   - **Project**: Maven Project
   - **Language**: Java
   - **Spring Boot**: 2.5.4 (ou a mais recente)
   - **Group**: `br.com.empresa`
   - **Artifact**: `sisapp`
   - **Name**: `sisapp`
   - **Packaging**: Jar
   - **Java**: 11
3. Adicione as seguintes dependências:
   - Spring Web
   - Spring Data JPA
   - Thymeleaf
   - MySQL Driver
4. Clique em **Generate** para baixar o projeto.

### 1.2. Importar para a IDE
1. Descompacte o projeto gerado e abra sua IDE.
2. Importe o projeto como um projeto Maven.
3. Aguarde até que o Maven baixe todas as dependências.

### 1.3. Estrutura do Projeto
A estrutura do projeto será organizada da seguinte forma:
- **src/main/java**: Contém o código Java principal do projeto.
- **src/main/resources/templates**: Contém os templates Thymeleaf para as views (formulários e listagens).
- **pom.xml**: Arquivo de configuração do Maven com as dependências do projeto.

## 2. Configurando o Banco de Dados MySQL

### 2.1. Criando o Banco de Dados
1. Abra o MySQL através do XAMPP ou qualquer outro gerenciador.
2. Crie um banco de dados chamado `bdibm` executando o seguinte comando no MySQL:

```sql
CREATE DATABASE bdibm;
```

### 2.2. Configuração do `application.properties`
Abra o arquivo `src/main/resources/application.properties` e adicione as configurações de conexão com o banco de dados MySQL:

```properties
spring.application.name=sisapp

spring.datasource.url=jdbc:mysql://localhost/bdibm?createDatabaseIfNotExist=true
spring.datasource.username=seu usuario
spring.datasource.password= sua senha

spring.mvc.hiddenmethod.filter.enabled=true

 
```

- **Nota**: Substitua `seu usuario` e a senha conforme necessário.

## 3. Criando as Classes do Projeto

### 3.1. Modelo `Funcionario.java`

Crie a classe `Funcionario.java` no pacote `com.ibm.sisapp.model`:

```java
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

```

### 3.2. DTO `DadosCadastroFuncionario.java`
Crie o `DadosCadastroFuncionario.java` para passar os dados do formulário:

```java
package com.ibm.sisapp.model;

public record DadosCadastroFuncionario(Long id, String nome, String funcao, String dtadm, double salario) {
    // O id agora é um campo do registro, permitindo que você o use para atualização e identificação do funcionário.
}

```

### 3.3. Repositório `FuncionarioRepository.java`
Crie a interface de repositório para persistência no banco de dados:

```java
package com.ibm.sisapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ibm.sisapp.model.Funcionario;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
}

```

### 3.4. Controlador `FuncionarioController.java`
Crie a classe controladora para gerenciar as requisições:

```java
package com.ibm.sisapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

import com.ibm.sisapp.model.DadosCadastroFuncionario;
import com.ibm.sisapp.model.Funcionario;
import com.ibm.sisapp.repository.FuncionarioRepository;

import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/funcionario")
public class FuncionarioController {

    @Autowired
    private FuncionarioRepository repository;

    // Método para abrir o formulário de cadastro
    @GetMapping
    public String abreFormFuncionario(Model model) {
        model.addAttribute("funcionario", new Funcionario()); // Para novo funcionário
        return "funcionario/formFuncionario"; // A página do formulário
    }

    // Método para listar os funcionários
    @GetMapping("/listagem")
    public String carregaPaginaListagem(Model model) {
        model.addAttribute("lista", repository.findAll()); // Passa a lista para view
        return "funcionario/listagem"; // Retorna a view de listagem
    }

    // Método para carregar o formulário de edição
    @GetMapping("/formFuncionario")
    public String carregaFormularioEdicao(@RequestParam Long id, Model model) {
        Funcionario funcionario = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Funcionário inválido: " + id));
        model.addAttribute("funcionario", funcionario); // Passa o funcionário para a view
        return "funcionario/formFuncionario"; // Retorna a view do formulário
    }

    // Método para cadastrar ou atualizar o funcionário
    @PostMapping
    @Transactional
    public String cadastraFuncionario(DadosCadastroFuncionario dados) {
        Funcionario funcionario;

        if (dados.id() != null) { // Verifica se é uma atualização
            funcionario = repository.findById(dados.id())
                    .orElseThrow(() -> new IllegalArgumentException("Funcionário inválido: " + dados.id()));
            funcionario.atualizaDados(dados); // Atualiza os dados do funcionário existente
        } else { // Se não houver ID, é uma nova inserção
            funcionario = new Funcionario(dados);
        }

        repository.save(funcionario); // Salva o funcionário (criação ou atualização)
        return "redirect:/funcionario/listagem"; // Redireciona para a listagem
    }

    // Método para remover o funcionário
    @DeleteMapping
    @Transactional
    public String removeFuncionario(@RequestParam Long id) {
        repository.deleteById(id);
        System.out.println("Funcionário EXCLUÍDO!");
        return "redirect:/funcionario/listagem"; // Redireciona para a listagem
    }
}
```

## 4. Criando as Views com Thymeleaf

### 4.1. Formulário de Cadastro (`formFuncionario.html`)
Este formulário permitirá cadastrar e editar funcionários:

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0"> <!-- Adicionando responsividade -->
    <title>Cadastro de Funcionário</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            display: flex;
            flex-direction: column;
            min-height: 100vh; /* Altura mínima da tela */
        }
        .container {
            flex: 1; /* Faz o container ocupar o espaço disponível */
        }
        footer {
            background-color: #f8f9fa; /* Cor de fundo leve */
            text-align: center; /* Centraliza o texto */
            padding: 10px 0; /* Espaçamento acima e abaixo do texto */
        }
    </style>
</head>
<body>
<div class="container mt-5">
    <h1 class="mb-4">Cadastro de Funcionário</h1>
    <form th:action="@{/funcionario}" method="post">
        <input type="hidden" th:if="${funcionario.id}" name="id" th:value="${funcionario.id}"/>
        
        <div class="mb-3">
            <label for="nome" class="form-label">Nome:</label>
            <input type="text" id="nome" name="nome" th:value="${funcionario.nome}" class="form-control" required />
        </div>
        
        <div class="mb-3">
            <label for="funcao" class="form-label">Função:</label>
            <input type="text" id="funcao" name="funcao" th:value="${funcionario.funcao}" class="form-control" required />
        </div>
        
        <div class="mb-3">
            <label for="dtadm" class="form-label">Data Admissão:</label>
            <input type="date" id="dtadm" name="dtadm" th:value="${funcionario.dtadm}" class="form-control" required />
        </div>
        
        <div class="mb-3">
            <label for="salario" class="form-label">Salário:</label>
            <input type="number" id="salario" name="salario" th:value="${funcionario.salario}" class="form-control" required />
        </div>
        
        <div class="d-flex justify-content-between mt-4">
            <button type="submit" class="btn btn-primary">Salvar</button>
            <a th:href="@{/}" class="btn btn-secondary">Voltar ao Início</a>
        </div>
    </form>
</div>

<footer>
    <p>&copy; Giovane Ines 2024</p>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script> <!-- Adicionando script do Bootstrap -->
</body>
</html>

```

### 4.2. Listagem de Funcionários (`listagem.html`)
A página para listar os funcionários cadastrados:

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0"> <!-- Adicionando responsividade -->
    <title>Lista de Funcionários</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            display: flex;
            flex-direction: column;
            min-height: 100vh; /* Altura mínima da tela */
        }
        .container {
            flex: 1; /* Faz o container ocupar o espaço disponível */
        }
        footer {
            background-color: #f8f9fa; /* Cor de fundo leve */
            text-align: center; /* Centraliza o texto */
            padding: 10px 0; /* Espaçamento acima e abaixo do texto */
        }
    </style>
</head>
<body>
<div class="container mt-5">
    <h1 class="mb-4">Lista de Funcionários</h1>

    <!-- Alerta de mensagem -->
    <div th:if="${mensagem}" class="alert alert-success" role="alert">
        <span th:text="${mensagem}"></span>
    </div>

    <table class="table table-striped table-bordered">
        <thead class="table-light">
            <tr>
                <th>Nome</th>
                <th>Função</th>
                <th>Data Admissão</th>
                <th>Salário</th>
                <th>Ações</th>
            </tr>
        </thead>
        <tbody>
            <tr th:each="funcionario : ${lista}">
                <td th:text="${funcionario.nome}"></td>
                <td th:text="${funcionario.funcao}"></td>
                <td th:text="${funcionario.dtadm}"></td>
                <td th:text="${funcionario.salario}"></td>
                <td>
                    <form th:action="@{/funcionario(id=${funcionario.id})}" method="post" class="d-inline">
                        <input type="hidden" name="_method" value="delete">
                        <input type="submit" value="Excluir" class="btn btn-danger" onclick="return confirm('Tem certeza que deseja excluir este funcionário?');">
                    </form>
                    <a th:href="@{/funcionario/formFuncionario(id=${funcionario.id})}" class="btn btn-primary">Editar</a>
                </td>
            </tr>
        </tbody>
    </table>

    <div class="d-flex justify-content-between mt-3">
        <a href="/funcionario" class="btn btn-success">Novo Funcionário</a>
        <a th:href="@{/}" class="btn btn-secondary">Voltar ao Início</a>
    </div>
</div>

<footer>
    <p>&copy; Giovane Ines 2024</p>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script> <!-- Adicionando script do Bootstrap -->
</body>
</html>

```

## 5. Executando o Projeto

Para executar o projeto, siga os passos abaixo:

1. **Na sua IDE**, localize o arquivo `SpringApplication` no projeto.

2. Clique com o botão direito no arquivo `SpringApplication`.

3. Selecione a opção `Run As` e depois escolha `Java Application`.

4. **Acesse a aplicação** no navegador:
   - Para cadastrar index, acesse `http://localhost:8080`.
   - Para cadastrar funcionários, acesse `http://localhost:8080/funcionario`.
   - Para listar os funcionários cadastrados, acesse `http://localhost:8080/funcionario/listagem`.


### 5.2. Acessar a Aplicação
Abra seu navegador e acesse `http://localhost:8080`.


