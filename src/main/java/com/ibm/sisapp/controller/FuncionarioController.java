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
