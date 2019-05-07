package br.ufrn.imd.controllers;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.ufrn.imd.dominio.Cliente;

/**
* MBean para cadastro de cliente.
* @author zoraia.rodrigues
*/

@ManagedBean
@SessionScoped
public class ClienteMBean {

	private Cliente cliente;
	
	public ClienteMBean() {		
		cliente = new Cliente();		
	}
	
	public String Cadastrar() {
		 //validando se foi informado um telefone/celular para contato.
		return " ";
	}
}
