package com.ibm.sisapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ibm.sisapp.model.Funcionario;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
}
