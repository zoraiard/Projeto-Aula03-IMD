# M�dulo Avan�ado Curso: T�cnico IMD

## Aula 03 - Valida��o de Campos no JSF

As valida��es s�o importantes, pois refletem as regras de neg�cio das nossas aplica��es. Praticaremos desenvolvendo uma aplica��o JSF com valida��es de campos.

* Entender a motiva��o para realizar valida��es em sistemas de informa��es,

* Aprender as diferentes formas de realizar valida��es com o framework JSF.

## Valida��es

As valida��es das entradas de dados nos sistemas de informa��es s�o muito comuns, e t�m o objetivo de verificar se todas as informa��es necess�rias est�o presentes para que o usu�rio possa continuar com a execu��o de uma determinada funcionalidade. Por exemplo, � comum os sites que tenham funcionalidade de cadastro de usu�rios validarem se o usu�rio informou seu login, seu e-mail, etc. Isso porque essas informa��es s�o consideradas necess�rias para a realiza��o do cadastro de usu�rio.

Ent�o, o JSF possui duas formas de valida��o, as quais veremos mais detalhadamente a seguir:

* Declarativa: utilizando os validadores padr�es do JSF;
* Imperativa: m�todo de valida��o no managed bean e classes validadoras que implementam a interface javax.faces.validator.Validator.

## Valida��o de campo obrigat�rio

A valida��o de campo obrigat�rio verifica se um determinado campo n�o est� vazio durante a submiss�o do formul�rio.

```
<h:inputText id="nome" value="#{pessoaMBean.pessoa.nome}" title="Nome" required="true" requiredMessage="Nome:campo obrigat�rio!"/>

```

## Valida��o de intervalo de campos num�ricos
A valida��o de campo obrigat�rio verifica se um determinado campo n�o est� vazio durante a submiss�o do formul�rio.

```
<h:inputText value="#{alunoMBean.aluno.nota}" size="3" required="true" 
	requiredMessage="Nota: Campo obrigat�rio." 
	validatorMessage="Nota entre 0.0 e 10.0."> 
	<f:validateDoubleRange minimum="0.0" maximum="10.0"/> 
</h:inputText>

```

## Valida��o de intervalo de campos num�ricos
A valida��o de campo obrigat�rio verifica se um determinado campo n�o est� vazio durante a submiss�o do formul�rio.

```
<h:inputText id="idade" value="#{pessoaMBean.idade}" 
    validatorMessage="A idade deve ser entre 26 e 45 anos."> 
    <f:validateLongRange minimum="26" maximum="45" /> 
</h:inputText>
```

Observe que em ambas as Tags utilizamos os atributos minimum e maximum para determinar o intervalo no qual se deseja a valida��o. Para defini��o da mensagem de valida��o, utilizamos o atributo validatorMessage.

     
## Valida��o de tamanho de campos de texto

```
<h:inputText id="login" value="#{usuario.login}" 
    validatorMessage="Login deve ter entre 5 e 10 caracteres.">
    <f:validateLength minimum="5" maximum="10" /> 
</h:inputText>
```
## Valida��o Imperativa

As valida��es declarativas s�o simples de serem utilizadas, mas geralmente n�o s�o suficientes para as diferentes regras de neg�cio em um sistema de informa��o. Muitas vezes, n�o queremos validar apenas a n�o exist�ncia de uma informa��o em um determinado campo. Existem casos que precisamos ir al�m disso, por exemplo: verificar se uma determinada matr�cula j� existe em um banco de dados durante um cadastro de aluno.


Esses casos est�o associados �s regras de neg�cio da aplica��o e para eles, onde a valida��o declarativa n�o � suficiente, recorremos � valida��o imperativa. Para esse tipo de valida��o podemos criar m�todos nos controllers JSF (ManagedBeans) ou criar validadores customizados.

```
public String cadastrar() { 
	//validando se a pessoa j� existe no banco de dados
	Pessoa pessoaBanco = dao.findPessoa(pessoa.getCpf());
	if(pessoaBanco != null) {
   		FacesMessage msg = new FacesMessage("Essa pessoa j� existe no banco de dados.");
    	msg.setSeverity(FacesMessage.SEVERITY_ERROR);
    	FacesContext.getCurrentInstance().addMessage("", msg);
    	return "/cadastro.jsf";
    }
	return "/continue.jsf";
}
```

## Assinatura dos m�todos para valida��o associada a componentes.

```
public void validarPessoa(FacesContext context, UIComponent component, Object value) throws ValidatorException {
    String cpf = (String) value;
    Pessoa pessoaBanco = dao.findPessoa(pessoa.getCpf());
    if(pessoaBanco != null) {
        String msg = "Essa pessoa j� existe no banco de dados.";
        throw new ValidatorException
       (new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
    }
}



<h:inputText value="#{cadastroMBean.pessoa.cpf}" size="11" validator="#{cadastroMBean.validarPessoa}" />
```

## Valida��o usando componentes customizados
A cria��o de validadores customizados � �til quando temos uma determinada valida��o para um campo que se repetir� em mais de um formul�rio. Por exemplo, suponha que voc� tenha um campo para e-mail na sua aplica��o o qual � solicitado ao usu�rio em diversas p�ginas diferentes. Nesse cen�rio, criar um validador customizado permitir� a centraliza��o das verifica��es dos e-mails v�lidos. Para criar um validador customizado, precisamos:

* 1. Criar uma classe Java e anot�-la com @FacesValidator. A inclus�o dessa anota��o registra a classe como um validador JSF;

* 2. Al�m de anotar a classe, � necess�rio que ela implemente a interface javax.faces.validator.Validator. Ao implementar essa interface, precisamos definir o m�todo validate, que possui a assinatura: public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException;

* 3. Na p�gina JSF (xhtml), no componente a ser validado, precisamos incluir a tag validator, especificando o identificador do validador (validatorId).



```
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("emailValidator")
public class EmailValidator implements Validator{

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String email = value.toString();
        if(!email.contains("@")) {
            FacesMessage msg = new FacesMessage(" E-mail inv�lido.", 
            "Formato: abcd@abc.com");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }

}

```

```
<h:inputText value="#{cadastroMBean.pessoa.email}" size="20">
    <f:validator validatorId="emailValidator" />
</h:inputText>```


