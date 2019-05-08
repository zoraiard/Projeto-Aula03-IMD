package br.ufrn.imd.controllers;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import br.ufrn.imd.dominio.Cliente;

/**
* MBean para cadastro de cliente.
* @author zoraia.rodrigues
*/

@ManagedBean
@SessionScoped
public class ClienteMBean{

	private Cliente cliente;
	
	public ClienteMBean(){		
	   cliente = new Cliente();		
	}
	
	public String cadastrar() {
		
		if ( cliente.getTelefone().equals("") || cliente.getCelular().equals("") ){
	        FacesMessage msg = new FacesMessage("Para efetivar o cadastro, é necessário informar um número de telefone ou celular.");
	        msg.setSeverity(FacesMessage.SEVERITY_ERROR);
	        FacesContext.getCurrentInstance().addMessage("", msg);
	        return "/form.jsf";
	    }
	    
	    return "/sucesso.jsf";
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
}
