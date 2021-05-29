package com.indracompany.treinamento.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.indracompany.treinamento.exception.AplicacaoException;
import com.indracompany.treinamento.exception.ExceptionValidacoes;
import com.indracompany.treinamento.model.dto.ClienteDTO;
import com.indracompany.treinamento.model.dto.TransferenciaBancariaDTO;
import com.indracompany.treinamento.model.entity.ContaBancaria;
import com.indracompany.treinamento.model.repository.ContaBancariaRepository;

@Service
public class ContaBancariaService  extends GenericCrudService<ContaBancaria, Long, ContaBancariaRepository>{

	@Autowired
	private ContaBancariaRepository contaBancariaRepository;
	
	
	@Autowired
	private ClienteService clienteService;
	
	
	public double consultarSaldo(String agencia, String conta) {
		ContaBancaria c = this.consultarConta(agencia, conta);
		return c.getSaldo();
	}

	public ContaBancaria consultarConta(String agencia, String numeroConta) {
		ContaBancaria c = contaBancariaRepository.findByAgenciaAndNumero(agencia, numeroConta);
		if (c == null) {
			throw new AplicacaoException(ExceptionValidacoes.ERRO_CONTA_INVALIDA);
		}
		return c;
	}

	public List<ContaBancaria> obterContas(String cpf) {
		ClienteDTO cli = clienteService.buscarClientePorCpf(cpf);
		List<ContaBancaria> contasDoCliente = contaBancariaRepository.buscarContasPorClienteSql(cli.getId());
		return contasDoCliente;
	}

	public void sacar(String agencia, String numeroConta, double valor) {
		ContaBancaria conta = this.consultarConta(agencia, numeroConta);
		
		
		if (conta.getSaldo() < valor) {
			throw new AplicacaoException(ExceptionValidacoes.ERRO_SALDO_INEXISTENTE);
		}
		conta.setSaldo(conta.getSaldo() - valor);
		super.salvar(conta);
		
	}

	public void depositar(String agencia, String numeroConta, double valor) {
		ContaBancaria conta = this.consultarConta(agencia, numeroConta);
		conta.setSaldo(conta.getSaldo() + valor);
		super.salvar(conta);
	}

	@Transactional(rollbackFor = Exception.class)
	public void transferir(TransferenciaBancariaDTO dto) {
		if (dto.getAgenciaDestino().equals(dto.getAgenciaOrigem())
				&& dto.getNumeroContaDestino().equals(dto.getNumeroContaOrigem())) {
			throw new AplicacaoException(ExceptionValidacoes.ERRO_CONTA_INVALIDA);
		}
		
		this.sacar(dto.getAgenciaOrigem(), dto.getNumeroContaOrigem(), dto.getValor());
		this.depositar(dto.getAgenciaDestino(), dto.getNumeroContaDestino(), dto.getValor());
	}
	

	
}
