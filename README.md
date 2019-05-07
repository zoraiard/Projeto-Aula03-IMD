# Módulo Avançado Curso: Técnico IMD

## Aula 03 - Validação de Campos no JSF

As validações são importantes, pois refletem as regras de negócio das nossas aplicações. Praticaremos desenvolvendo uma aplicação JSF com validações de campos.

* Entender a motivação para realizar validações em sistemas de informações,

* Aprender as diferentes formas de realizar validações com o framework JSF.

## Validações

As validações das entradas de dados nos sistemas de informações são muito comuns, e têm o objetivo de verificar se todas as informações necessárias estão presentes para que o usuário possa continuar com a execução de uma determinada funcionalidade. Por exemplo, é comum os sites que tenham funcionalidade de cadastro de usuários validarem se o usuário informou seu login, seu e-mail, etc. Isso porque essas informações são consideradas necessárias para a realização do cadastro de usuário.

Então, o JSF possui duas formas de validação, as quais veremos mais detalhadamente a seguir:

* Declarativa: utilizando os validadores padrões do JSF;
* Imperativa: método de validação no managed bean e classes validadoras que implementam a interface javax.faces.validator.Validator.

## Validação de campo obrigatório

A validação de campo obrigatório verifica se um determinado campo não está vazio durante a submissão do formulário.

```
<h:inputText id="nome" value="#{pessoaMBean.pessoa.nome}" title="Nome" required="true" requiredMessage="Nome:campo obrigatório!"/>

```

## Validação de intervalo de campos numéricos
A validação de campo obrigatório verifica se um determinado campo não está vazio durante a submissão do formulário.

```
<h:inputText value="#{alunoMBean.aluno.nota}" size="3" required="true" 
	requiredMessage="Nota: Campo obrigatório." 
	validatorMessage="Nota entre 0.0 e 10.0."> 
	<f:validateDoubleRange minimum="0.0" maximum="10.0"/> 
</h:inputText>

```

## Validação de intervalo de campos numéricos
A validação de campo obrigatório verifica se um determinado campo não está vazio durante a submissão do formulário.

```
<h:inputText id="idade" value="#{pessoaMBean.idade}" 
    validatorMessage="A idade deve ser entre 26 e 45 anos."> 
    <f:validateLongRange minimum="26" maximum="45" /> 
</h:inputText>
```

Observe que em ambas as Tags utilizamos os atributos minimum e maximum para determinar o intervalo no qual se deseja a validação. Para definição da mensagem de validação, utilizamos o atributo validatorMessage.

     
## Validação de tamanho de campos de texto

```
<h:inputText id="login" value="#{usuario.login}" 
    validatorMessage="Login deve ter entre 5 e 10 caracteres.">
    <f:validateLength minimum="5" maximum="10" /> 
</h:inputText>
```
## Validação Imperativa

As validações declarativas são simples de serem utilizadas, mas geralmente não são suficientes para as diferentes regras de negócio em um sistema de informação. Muitas vezes, não queremos validar apenas a não existência de uma informação em um determinado campo. Existem casos que precisamos ir além disso, por exemplo: verificar se uma determinada matrícula já existe em um banco de dados durante um cadastro de aluno.


Esses casos estão associados às regras de negócio da aplicação e para eles, onde a validação declarativa não é suficiente, recorremos à validação imperativa. Para esse tipo de validação podemos criar métodos nos controllers JSF (ManagedBeans) ou criar validadores customizados.

```
public String cadastrar() { 
	//validando se a pessoa já existe no banco de dados
	Pessoa pessoaBanco = dao.findPessoa(pessoa.getCpf());
	if(pessoaBanco != null) {
   		FacesMessage msg = new FacesMessage("Essa pessoa já existe no banco de dados.");
    	msg.setSeverity(FacesMessage.SEVERITY_ERROR);
    	FacesContext.getCurrentInstance().addMessage("", msg);
    	return "/cadastro.jsf";
    }
	return "/continue.jsf";
}
```

## Assinatura dos métodos para validação associada a componentes.

```
public void validarPessoa(FacesContext context, UIComponent component, Object value) throws ValidatorException {
    String cpf = (String) value;
    Pessoa pessoaBanco = dao.findPessoa(pessoa.getCpf());
    if(pessoaBanco != null) {
        String msg = "Essa pessoa já existe no banco de dados.";
        throw new ValidatorException
       (new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
    }
}



<h:inputText value="#{cadastroMBean.pessoa.cpf}" size="11" validator="#{cadastroMBean.validarPessoa}" />
```

## Validação usando componentes customizados
A criação de validadores customizados é útil quando temos uma determinada validação para um campo que se repetirá em mais de um formulário. Por exemplo, suponha que você tenha um campo para e-mail na sua aplicação o qual é solicitado ao usuário em diversas páginas diferentes. Nesse cenário, criar um validador customizado permitirá a centralização das verificações dos e-mails válidos. Para criar um validador customizado, precisamos:

* 1. Criar uma classe Java e anotá-la com @FacesValidator. A inclusão dessa anotação registra a classe como um validador JSF;

* 2. Além de anotar a classe, é necessário que ela implemente a interface javax.faces.validator.Validator. Ao implementar essa interface, precisamos definir o método validate, que possui a assinatura: public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException;

* 3. Na página JSF (xhtml), no componente a ser validado, precisamos incluir a tag validator, especificando o identificador do validador (validatorId).



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
            FacesMessage msg = new FacesMessage(" E-mail inválido.", 
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


