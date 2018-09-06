/**
 * 
 */
package br.lry.dataflow;

import java.util.HashMap;

import br.lry.components.va.AUTVACadastros.AUT_VA_CADASTROS;
import br.lry.components.va.AUTVACadastros.AUT_VA_ESTADOS;
import br.lry.components.va.AUTVACadastros.AUT_VA_TIPO_CONTATO;
import br.lry.components.va.AUTVACadastros.AUT_VA_TIPO_ENDERECO;
import br.lry.components.va.AUTVACadastros.AUT_VA_TIPO_RESIDENCIA;
import br.lry.functions.AUTProjectsFunctions;
import br.lry.functions.AUTProjectsFunctions.AUTLogMensagem;
import br.lry.functions.AUTProjectsFunctions.AUTNumerosRandomicos;
import br.lry.functions.AUTProjectsFunctions;


/**
 * 
 * Gerenciamento do fluxo de dados na automação
 * 
 * 
 * @author Softtek - QA
 *
 */
public class AUTDataFlow {
	AUTLogMensagem AUT_LOG_MANAGER = null;
	AUTNumerosRandomicos AUT_CURRENT_RANDOM_MANAGER = null;
	
	public java.util.HashMap<String,java.util.HashMap<Integer,java.util.HashMap<String, Object>>> AUT_GLOBAL_PARAMETERS = null;
	
	/**
	 * 
	 * Define os tipos de ambientes disponiveis na fábrica de testes
	 * 
	 * @author Softtek-QA
	 *
	 */
	public enum AUT_TIPO_AMBIENTE{
		HOMOLOG,
		HOMOLOG2,
		DESENVOLVIMENTO
	}
	
	
	public enum AUT_URL_APLICACOES{
		VA_HOMOLOG,
		VA_HOMOLOG2,
		HMC_HOMOLOG,
		HMC_HOMOLOG2
		
		
	}
	
	public enum AUT_TABLE_PARAMETERS_NAMES{
		AUT_VA_LOGIN,
		AUT_VA_CADASTROS,
		AUT_HMC_LOGIN,
		AUT_VA_GERACAO_PEDIDOS;
		
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			switch(this) {
			case AUT_VA_CADASTROS:{
				return "AUTVACADASTRO001";
			}
			case AUT_VA_LOGIN:{
				return "AUTVALOGIN001";
			}
			case AUT_HMC_LOGIN:{
				return "AUTHMC001";
			}
			case AUT_VA_GERACAO_PEDIDOS: {
				return "AUTVAPEDIDOS001";
			}
			default:{
				return super.toString();
			}
			}			
		}
	}
	
	/**
	 * 
	 * Classe padrão de definição da tabela de dados
	 * 
	 * @author Softtek-QA
	 *
	 */
	public static class AUTDataTableStruct{
		
		/**
		 * 
		 * Construtor padrão da classe de objetos
		 * 
		 */
		public AUTDataTableStruct() {
			super();
		}
	}
	
	
	/**
	 * 
	 * Carrega os parametros de inicialização padrão para todo sistema
	 * 
	 * @return dicionarioDeDados - por tabela
	 * 
	 */
	public java.util.HashMap<String,
	java.util.HashMap<Integer,java.util.HashMap<String,Object>>> autInitDataFlow() {
		try {
			AUT_CURRENT_RANDOM_MANAGER = new AUTNumerosRandomicos();
						
			AUT_GLOBAL_PARAMETERS = new java.util.HashMap<String,java.util.HashMap<Integer,java.util.HashMap<String, Object>>>();
			
			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataLogin = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();
			
			/**
			 * 
			 * Configuração de parametros para o login
			 * 
			 */
			vaDataLogin.put(1, new java.util.HashMap<String,Object>());
			vaDataLogin.get(1).put("AUT_USER", "55000001");
			vaDataLogin.get(1).put("AUT_PASSWORD", "1234");

			
			
			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.AUT_VA_LOGIN.toString(),vaDataLogin);

			//Inclusao de tabela - Pedidos
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuração de componente de login sistema HMC
			 * 
			 * 
			 */
			
			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataCadastroPF = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();
			
			vaDataCadastroPF.put(1, new java.util.HashMap<String,Object>());
			String codEstrangeiro = AUTProjectsFunctions.gerarEstrangeiro();
			vaDataCadastroPF.get(1).put("AUT_TIPO_CADASTRO", AUT_VA_CADASTROS.JURIDICA);
			vaDataCadastroPF.get(1).put("AUT_PASSAPORTE", codEstrangeiro);
			vaDataCadastroPF.get(1).put("AUT_CNPJ", AUTProjectsFunctions.gerarCNPJ());
			vaDataCadastroPF.get(1).put("AUT_CPF", AUTProjectsFunctions.gerarCPF());
			vaDataCadastroPF.get(1).put("AUT_NOME_ESTRANGEIRO", "AUT NOME ESTRANG: ".concat(codEstrangeiro));	
			vaDataCadastroPF.get(1).put("AUT_NOME", "AUT NOME PF: ".concat(AUTProjectsFunctions.gerarCPF()));	
			vaDataCadastroPF.get(1).put("AUT_NOME_PJ", "AUT NOME PJ: ".concat(AUTProjectsFunctions.gerarEstrangeiro()));	
			vaDataCadastroPF.get(1).put("AUT_NOME_PJ_FANTASIA", "AUT NOME PFFT: FT.".concat(AUTProjectsFunctions.gerarEstrangeiro()));	
			vaDataCadastroPF.get(1).put("AUT_EMAIL", "aut.qaemail@automation.com");	
			vaDataCadastroPF.get(1).put("AUT_INCRICAO_ESTADUAL", "474.018.412.567");
			vaDataCadastroPF.get(1).put("AUT_INCRICAO_ESTADUAL_PF", AUTProjectsFunctions.gerarEstrangeiro());	
			vaDataCadastroPF.get(1).put("AUT_INCRICAO_MUNICIPAL", AUTProjectsFunctions.gerarEstrangeiro());	
			vaDataCadastroPF.get(1).put("AUT_NOME_PJ_CONTATO", "NOME CONTATO PJ".concat(AUTProjectsFunctions.gerarCPF()));	
			vaDataCadastroPF.get(1).put("AUT_PJ_EMAIL_CONTATO", "EMAIL.QA@TESTE.COM.BR".concat(AUTProjectsFunctions.gerarCPF()));	
			vaDataCadastroPF.get(1).put("AUT_PJ_DEPARTAMENTO_CONTATO", "QUALIDADE".concat(AUTProjectsFunctions.gerarCPF()));	
			vaDataCadastroPF.get(1).put("AUT_TIPO_TELEFONE", AUT_VA_TIPO_CONTATO.CELULAR);	
			vaDataCadastroPF.get(1).put("AUT_NUMERO_TELEFONE", "11966447035");	
			vaDataCadastroPF.get(1).put("AUT_UF_PESQUISA", AUT_VA_ESTADOS.SP);	
			vaDataCadastroPF.get(1).put("AUT_CIDADE_PESQUISA", "CAJAMAR");
			vaDataCadastroPF.get(1).put("AUT_ENDERECO_PESQUISA", "RUA PREFEITO ANTONIO GARRIDO");
			vaDataCadastroPF.get(1).put("AUT_BAIRRO_PESQUISA", "JORDANÉSIA");	
			vaDataCadastroPF.get(1).put("AUT_TIPO_ENDERECO", AUT_VA_TIPO_ENDERECO.OBRA);
			vaDataCadastroPF.get(1).put("AUT_CEP", "07776-000");	
			vaDataCadastroPF.get(1).put("AUT_RUA_ENDERECO", "Rua Explanada");	
			vaDataCadastroPF.get(1).put("AUT_NUMERO_ENDERECO", "256");	
			vaDataCadastroPF.get(1).put("AUT_BAIRRO_ENDERECO", "EXPLANADA");	
			vaDataCadastroPF.get(1).put("AUT_COMPLEMENTO_ENDERECO", "CASA 123");	
			vaDataCadastroPF.get(1).put("AUT_CIDADE_ENDERECO", "BOCAIÚVA");	
			vaDataCadastroPF.get(1).put("AUT_ESTADO_ENDERECO", AUT_VA_ESTADOS.MG);	
			vaDataCadastroPF.get(1).put("AUT_REFERENCIA_ENDERECO", "ACOUGUE DA ESQUINA");	
			vaDataCadastroPF.get(1).put("AUT_TIPO_IMOVEL_RESIDENCIA", AUT_VA_TIPO_RESIDENCIA.RURAL_CHACARA_FAZENDA_OU_SITIO);	
			
			
			
			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.AUT_VA_CADASTROS.toString(), vaDataCadastroPF);
		
			
			/*
			 *MASSA DE DADOS PARA GERACAO DE PEDIDOS 
			 */
			
			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataGeracaoPedidos001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();
			
			vaDataGeracaoPedidos001.put(1, new java.util.HashMap<String, Object>());
			vaDataGeracaoPedidos001.get(1).put("AUT_USUARIO_LOJA", "55000002");
			vaDataGeracaoPedidos001.get(1).put("AUT_SENHA", "1234");
			vaDataGeracaoPedidos001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataGeracaoPedidos001.get(1).put("AUT_CODIGO_ITEM", "89423866");	// "89455163");
			vaDataGeracaoPedidos001.get(1).put("AUT_CPF_CLIENTE_NOVO", AUTProjectsFunctions.gerarCPF());
			vaDataGeracaoPedidos001.get(1).put("AUT_CPF_CLIENTE_CADASTRADO", "78651738811");
			vaDataGeracaoPedidos001.get(1).put("AUT_PASSAPORTE", "78651738811");
			vaDataGeracaoPedidos001.get(1).put("AUT_CNPJ", "78651738811");
			vaDataGeracaoPedidos001.get(1).put("AUT_CPF", "78651738811");

			
			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.AUT_VA_GERACAO_PEDIDOS.toString(), vaDataGeracaoPedidos001);

			

			/*
			 *  MASSA DE DADOS PARA CONFIGURAÇÕES E PARAMETRIZAÇÕES DO HMC - HYBRIS
			 * 
			 */
			
			java.util.HashMap<Integer, java.util.HashMap<String,Object>> hmcLogin = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();
			
			hmcLogin.put(1, new java.util.HashMap<String,Object>());
			
			hmcLogin.get(1).put("AUT_URL", "https://vahomolog-admin.leroymerlin.com.br/hmc/hybris");
			hmcLogin.get(1).put("AUT_USER", "fernanda.aguiar");
			hmcLogin.get(1).put("AUT_PASSWORD", "1234");
			hmcLogin.get(1).put("AUT_USER_ID", "5500".concat(AUTProjectsFunctions.gerarEstrangeiro()));	//criar numero randomico
			hmcLogin.get(1).put("AUT_USER_NAME", "Automation Test");	//Criar nome padronizado
			hmcLogin.get(1).put("AUT_USER_EMAIL", "automationTest@hotmail.com");
			hmcLogin.get(1).put("AUT_NOVA_SENHA", "1234");
			hmcLogin.get(1).put("AUT_CANAL", "channel_store");
			hmcLogin.get(1).put("AUT_TIPO", "B2BCustomer - Cliente B2B");
			hmcLogin.get(1).put("AUT_UNIDADE_B2B_PADRAO", "0035_LMStore");
			hmcLogin.get(1).put("AUT_DEPARTAMENTO", "50000425-PROJETO 3D VENDA ASSISTIDA");
			hmcLogin.get(1).put("AUT_CODIGO_CATEGORIA", "999");
			hmcLogin.get(1).put("AUT_CODIGO_DEPARTAMENTO", "50000425");
			hmcLogin.get(1).put("AUT_LOJA", "0035_LMStore");
			
			
			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.AUT_HMC_LOGIN.toString(), hmcLogin);
			

			return AUT_GLOBAL_PARAMETERS;
		}
		catch(java.lang.Exception e) {
			
			AUT_LOG_MANAGER.logMensagem("AUT ERROR: INICIALIZATION OF PARAMETERS DATAFLOW : ".concat(e.getMessage()));
			
			e.printStackTrace();
			
			return null;
		}
	}
	
	
	/**
	 * 
	 * Recupera um conjunto de parametros padrão para todos os sistemas
	 * 
	 * @return java.util.HashMap(String,String) - Matriz de dados do projeto
	 * 
	 * 
	 */
	public java.util.HashMap<String, Object> autGetParameter() {
		autInitDataFlow();
		/**
		java.util.HashMap<String, String> parametersOut = new java.util.HashMap<String, String>();
		parametersOut.put("AUT_USER", "51028487");
		parametersOut.put("AUT_PASSWORD", "1234");
**/
		return AUT_GLOBAL_PARAMETERS.get(AUT_TABLE_PARAMETERS_NAMES.AUT_VA_LOGIN.toString()).get(1);
	}

	//teste de configuração 0000000001
	/**
	 * 
	 * Construtor padrão da classe
	 * 
	 */
	public AUTDataFlow() {
		AUT_LOG_MANAGER = new AUTLogMensagem();
	}
}
