package com.indracompany.treinamento.controller.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.indracompany.treinamento.model.dto.DepositoDTO;
import com.indracompany.treinamento.model.dto.SaqueDTO;
import com.indracompany.treinamento.model.dto.TransferenciaBancariaDTO;
import com.indracompany.treinamento.model.entity.ContaBancaria;
import com.indracompany.treinamento.model.service.ContaBancariaService;

@RestController
@RequestMapping("rest/contas")
public class ContaBancariaRest extends GenericCrudRest<ContaBancaria, Long, ContaBancariaService>{
	
	@Autowired
	private ContaBancariaService contaBancariaService;
	
	@RequestMapping(value = "/consultar-saldo/{agencia}/{conta}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Double> consultarSaldo(@PathVariable String agencia, 
			@PathVariable String conta){
		double saldo = contaBancariaService.consultarSaldo(agencia, conta);
		return new ResponseEntity<>(saldo, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/consultar-contas-cliente/{cpf}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<List<ContaBancaria>> consultarContasPorCliente(@PathVariable String cpf){
		List<ContaBancaria> contasCliente = contaBancariaService.obterContas(cpf);
		return new ResponseEntity<>(contasCliente, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/deposito", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<Void> depositar(@RequestBody DepositoDTO dto) {
		contaBancariaService.depositar(dto.getAgencia(), dto.getNumeroConta(), dto.getValor());
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/saque", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<Void> sacar(@RequestBody SaqueDTO dto) {
		contaBancariaService.sacar(dto.getAgencia(), dto.getNumeroConta(), dto.getValor());
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/transferencia", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<Void> transferir(@RequestBody TransferenciaBancariaDTO dto) {
		contaBancariaService.transferir(dto);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
