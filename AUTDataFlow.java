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
	public java.util.HashMap<String,java.util.HashMap<Integer,java.util.HashMap<String, Object>>> AUT_GLOBAL_PARAMETERS = null;
	
	
	public enum AUT_TABLE_PARAMETERS_NAMES{
		AUT_VA_LOGIN,
		AUT_VA_CADASTRO_PF;
		
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			switch(this) {
			case AUT_VA_CADASTRO_PF:{
				return "AUTVACADASTRO001";
			}
			case AUT_VA_LOGIN:{
				return "AUTVALOGIN001";
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
	public java.util.HashMap<String,java.util.HashMap<Integer,java.util.HashMap<String,Object>>> autInitDataFlow() {
		try {
			
			AUT_GLOBAL_PARAMETERS = new java.util.HashMap<String,java.util.HashMap<Integer,java.util.HashMap<String, Object>>>();
			
			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataLogin = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();
			
			/**
			 * 
			 * Configuração de parametros para o login
			 * 
			 */
			vaDataLogin.put(1, new java.util.HashMap<String,Object>());
			vaDataLogin.get(1).put("AUT_USER", "51021157");
			vaDataLogin.get(1).put("AUT_PASSWORD", "1234");
			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.AUT_VA_LOGIN.toString(),vaDataLogin);

			
			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataCadastroPF = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();
			
			vaDataCadastroPF.put(1, new java.util.HashMap<String,Object>());
			String codEstrangeiro = AUTProjectsFunctions.gerarEstrangeiro();
			vaDataCadastroPF.get(1).put("AUT_TIPO_CADASTRO", AUT_VA_CADASTROS.FISICA);
			vaDataCadastroPF.get(1).put("AUT_PASSAPORTE", codEstrangeiro);
			vaDataCadastroPF.get(1).put("AUT_CNPJ", AUTProjectsFunctions.gerarCNPJ());
			vaDataCadastroPF.get(1).put("AUT_CPF", AUTProjectsFunctions.gerarCPF());
			vaDataCadastroPF.get(1).put("AUT_NOME_ESTRANGEIRO", "AUT NOME ESTRANG: ".concat(codEstrangeiro));	
			vaDataCadastroPF.get(1).put("AUT_NOME", "AUT NOME PF: ".concat(AUTProjectsFunctions.gerarCPF()));	
			vaDataCadastroPF.get(1).put("AUT_EMAIL", "aut.qaemail@automation.com");	
			vaDataCadastroPF.get(1).put("AUT_INCRICAO_ESTADUAL", AUTProjectsFunctions.gerarEstrangeiro());	
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
			
			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.AUT_VA_CADASTRO_PF.toString(), vaDataCadastroPF);
								
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

	/**
	 * 
	 * Construtor padrão da classe
	 * 
	 */
	public AUTDataFlow() {
		AUT_LOG_MANAGER = new AUTLogMensagem();
	}
}
