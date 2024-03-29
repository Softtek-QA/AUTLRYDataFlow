/**
 * 
 */
package br.lry.dataflow;

import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;
import java.util.HashMap;

import org.junit.Test;

import br.lry.components.AUTBaseComponent.AUTStoreItem.AUT_SELECT_PRODUCT_OPTIONS_BY_STORE;
import br.lry.components.AUTVABaseComponent;
import br.lry.components.AUTVABaseComponent.AUTVAFluxosSaidaComponente.FILIAIS;
import br.lry.components.AUTVABaseComponent.AUTVAFormasPagamento.METODOS_PAGAMENTO;
import br.lry.components.AUTVABaseComponent.AUTVAFormasPagamento.PLANOS_PAGAMENTO;
import br.lry.components.AUTVABaseComponent.AUT_BOITATA_LOJAS;
import br.lry.components.AUTVABaseComponent.AUT_TIPO_ACESSO_LOGIN;
import br.lry.components.safe.AUTSafeBaseComponent.AUT_SAFE_LOJAS_ENUM;
import br.lry.components.safe.AUTSafeBaseComponent.AUT_SAFE_PROFISSOES;
import br.lry.components.safe.AUTSafeBaseComponent.AUT_SAFE_TIPO_CONVENIO;
import br.lry.components.safe.AUTSafeBaseComponent.AUT_SAFE_TYPE_PERSONS;
import br.lry.components.va.AUTVACadastros.AUT_VA_CADASTROS;
import br.lry.components.va.AUTVACadastros.AUT_VA_ESTADOS;
import br.lry.components.va.AUTVACadastros.AUT_VA_TIPO_CONTATO;
import br.lry.components.va.AUTVACadastros.AUT_VA_TIPO_ENDERECO;
import br.lry.components.va.AUTVACadastros.AUT_VA_TIPO_RESIDENCIA;
import br.lry.components.va.cat007.AUTFluxoSaida;
import br.lry.components.va.cat007.AUTFluxoSaida.AUT_VA_FLUXO_SAIDA;
import br.lry.components.va.cat009.AUTMeiosPagamento;
import br.lry.components.va.cat010.AUTDesconto;
import br.lry.components.va.cat010.AUTEdicao.AUT_MANTER_CONDICOES;
import br.lry.components.va.cat010.AUTEdicao.AUT_STATUS_PESQUISA;
import br.lry.components.va.cat018.AUTSelecaoLojaBoitata;
import br.lry.components.va.cat018.AUTSelecaoLojaVA;
import br.lry.dataflow.AUTDataFlow.AUT_EDICAO_PEDIDO;
import br.lry.dataflow.AUTDataFlow.AUT_MODO_CONSULTAS_VA_SELECAO_ITEM;
import br.lry.dataflow.AUTDataFlow.AUT_TABLE_PARAMETERS_NAMES;
import br.lry.dataflow.AUTDataFlow.AUT_VA_TIPO_DOCUMENTO_PESQUISA;
import br.lry.functions.AUTProjectsFunctions;
import br.lry.functions.AUTProjectsFunctions.AUTLogMensagem;
import br.lry.functions.AUTProjectsFunctions.AUTNumerosRandomicos;
import br.lry.functions.AUTProjectsFunctions.AUT_TIPO_FLUXO_SAIDA;
import br.lry.functions.AUTProjectsFunctions.AUT_TIPO_LOJA;
import br.stk.framework.db.management.AUTDBProcessDataFlow;
import br.stk.framework.db.management.AUTDBProcessDataFlow.AUT_SQL_OPERATIONS_PROCESS_PARAMETERS;
import br.stk.framework.db.management.AUTDBProcessDataFlow.AUT_SQL_PROPERTIES;
import br.stk.framework.db.management.AUTDBProcessDataFlow.AUT_TYPE_FIELD_DATAFLOW;
import br.stk.framework.db.transactions.AUTJDBCProcess.AUT_TYPE_SQL_OPERATIONS;
import br.stk.framework.tests.AUTFWKTestObjectBase;
import br.lry.functions.AUTProjectsFunctions;


/**
 * 
 * Gerenciamento do fluxo de dados na automação
 * 
 *  
 * @author Softtek - QA
 *
 */
public class AUTDataFlow extends AUTFWKTestObjectBase{
	public Boolean AUT_ENABLE_SYNCRONIZE_ALL_OBJECTS = false;			
	public static java.util.HashMap<String,java.util.HashMap<Integer,java.util.HashMap<String, Object>>> AUT_GLOBAL_PARAMETERS = null;
	AUTLogMensagem AUT_LOG_MANAGER = null;
	AUTNumerosRandomicos AUT_CURRENT_RANDOM_MANAGER = null;
	boolean enableSearchAllScenarios = false;
	
	public static enum AUT_MODO_CONSULTAS_VA_SELECAO_ITEM{
		IMPRESSAO,
		EXIBICAO_DOCUMENTOS_VINCULADOS,
		CONSULTA_STATUS_PEDIDO,
		EDICAO,
		EDICAO_DO_BOITATA,
		COPIA
	}
	/**
	 * 
	 * Define as opções para edição de pedidos
	 * 
	 * @author Softtek-QA
	 *
	 */
	public static enum AUT_EDICAO_PEDIDO{
		HABILITAR_ANTIFRAUDE,
		ALTERAR_FORMA_PAGAMENTO_CARTAO,
		SERVICO_ITEM_INDIVIDUAL,
		GARANTIA_ITEM_INDIVIDUAL,
		QUANTIDADE_ITEM_QUANT_ADICIONAR_POR_ATRIBUTO,
		QUANTIDADE_ITEM_QUANT_ADICIONAR_PADRAO,
		QUANTIDADE_ITEM_SUBTRAIR_PADRAO,
		LOJA_ITEM,
		DEPOSITO_ITEM,
		ALTERAR_CONFIGURACAO_FLUXO_SAIDA,
		DATA_AGENDAMENTO_FLUXO_ENTREGAS;
		
		@Override
		public String toString() {
			switch(this) {
			case QUANTIDADE_ITEM_QUANT_ADICIONAR_PADRAO:{
				return "10";
			}
			case QUANTIDADE_ITEM_SUBTRAIR_PADRAO:{
				return "2";
			}
			}
			return this.name();
		}
		
	}
	
	/**
	 * 
	 * Tipo de telefone para contato
	 * 
	 * @author Softtek-QA
	 *
	 */
	public static enum AUT_TIPO_TELEFONE_PARA_CONTATO{
		FIXO,
		CELULAR;
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			switch(this) {
			case CELULAR:{
				return "(?i:Celular)";
			}
			case FIXO:{
				return "(?i:Fixo)";
			}
			}
			return super.toString();
		}
	}
	
	public static enum AUT_CONFIRMACAO_USUARIO{
		SIM,
		NAO,
		CANCELAR,
		CONFIRMAR,
		EXCLUSAO_ITEM_DESABILITADA,
		EXCLUIR_ITEM_INDIVIDUAL,
		EXCLUIR_ITEM_POR_LM,
		EXCLUIR_ITEM_MULTIPLOS_ITENS,
		EXCLUSAO_GERAL_LIMPAR_CARRINHO,
		EXCLUIR_ITEM_POR_ATRIB_MATERIAL,
	}
	
	public void autEnableSearchOnDataFlowAllScenario() {
		enableSearchAllScenarios = true;
	}

	public void autDisableSearchOnDataFlowAllScenario() {
		enableSearchAllScenarios = true;
	}
	
	public boolean autIsSearchOnDataFlowAllScenario() {
		return enableSearchAllScenarios;
	}

	
	/**
	 * 
	 * Tipo de documento para pesquisa de documento
	 * 
	 * @author Softtek - QA
	 *
	 */
	public static enum AUT_VA_TIPO_DOCUMENTO_PESQUISA{
		CPF_OU_CNPJ,
		NOME,
		PASSAPORTE,
		RG_OU_RNE,
		RAZAO_SOCIAL,
		NOME_FANTASIA
	}
	
	
	public  <TOption extends java.lang.Enum> Object autGetParametersFromTable(TOption nomeTabela, String chave,Integer rowIndex){
		Object valor = autGetParametersFromTable(nomeTabela).get(chave);
		if(valor==null) {
			AUTRuntimeExecutionScenario scn = autGetCurrentScenarioRuntime();
			scn.AUT_DATAFLOW_SEARCH_KEY = nomeTabela;
			scn.AUT_ENABLE_SEARCH_DATAFLOW_FROM_ALL_SCENARIOS_TO_PROJECT = true;
			valor = autGetDataFlowDBIntegration().autGetDataFlowFromDataBaseDownload(scn.autGetIdNumber(), scn).get(rowIndex).get(chave);
			System.out.println("AUT INFO : O VALOR DO CAMPO É NULO ----> TESTES");
			
			return valor;
		}
		else {
			return valor;
		}		
	}

	public  <TOption extends java.lang.Enum> Object autGetParametersFromTable(TOption nomeTabela, String chave) {
		return autGetParametersFromTable(nomeTabela, chave, 1);
	}
	
	public  <TOption extends java.lang.Enum> java.util.HashMap<String,Object> autGetParametersFromTable(TOption nomeTabela,Integer numeroLinha) throws NumberFormatException, SQLException{
		AUTRuntimeExecutionScenario scn = autGetCurrentScenarioRuntime();
		java.util.HashMap<Integer,java.util.HashMap<String,Object>> paramsOut = null;	
		java.util.HashMap<Integer,java.util.HashMap<String,Object>> paramsOutDependences = null;
		java.util.HashMap<Integer,java.util.HashMap<String,Object>> paramsOutFull = null;

		if(scn.AUT_SCENARIO_FULL_NAME_RUNTIME!=null) {
			java.util.regex.Pattern regExp = java.util.regex.Pattern.compile("\\d+");
			java.util.regex.Matcher verif = regExp.matcher(scn.AUT_PROJECT_ID);
			scn.AUT_DATAFLOW_SEARCH_KEY = nomeTabela;
			if(verif.find()) {
				System.out.println("@@@@AUT INFO: CARREGANDO PARAMETROS DO BANCO DE DADOS : INICIO");
				Integer projId = Integer.parseInt(verif.group());
				paramsOut = autGetDataFlowDBIntegration().autGetDataFlowFromDataBaseDownload(projId.toString(), scn);			

				if(paramsOut==null || paramsOut.size()==0) {
					if(AUT_GLOBAL_PARAMETERS.containsKey(nomeTabela.toString())) {
						System.out.println("@@@@AUT INFO: NAO EXISTE PARAMETROS NO BANCO DE DADOS, INICIANDO UPLOAD DO DATAFLOW LOCAL : INICIO");						
						autGetDataFlowDBIntegration().autUploadDataFlow(AUT_GLOBAL_PARAMETERS.get(nomeTabela.toString()), scn);
						scn.AUT_ENABLE_SEARCH_DATAFLOW_FROM_ALL_SCENARIOS_TO_PROJECT = false;
						paramsOut = autGetDataFlowDBIntegration().autGetDataFlowFromDataBaseDownload(projId.toString(), scn);			
						System.out.println("@@@@AUT INFO: NAO EXISTE PARAMETROS NO BANCO DE DADOS, INICIANDO UPLOAD DO DATAFLOW LOCAL : FIM");
					}
					else {
						System.out.println(String.format("@@@@AUT INFO: NAO FOI CONFIGURADO UM FLUXO DE DADOS PARA O PROCESSO AUTOMATIZADO : DB: NAO : LOCAL: NAO : PROCESSO: %s",scn.AUT_SCENARIO_FULL_NAME));						
					}
				}
				else {
					//********************* TESTE DE UPLOAD DE PARAMETROS ************************
				}
				System.out.println("@@@@AUT INFO: CARREGANDO PARAMETROS DO BANCO DE DADOS : FIM");
				AUT_DB_CONNECTION_OFFLINE = false;
				return paramsOut.get(numeroLinha);
			}
			else {
				System.out.println("@@@@AUT INFO: CARREGANDO PARAMETROS DO DATAFLOW LOCAL");
				AUT_DB_CONNECTION_OFFLINE = true;
				return AUT_GLOBAL_PARAMETERS.get(nomeTabela.toString()).get(numeroLinha);
			}			
		}
		else {
			System.out.println("@@@@AUT INFO: CARREGANDO PARAMETROS DO DATAFLOW LOCAL");
			return AUT_GLOBAL_PARAMETERS.get(nomeTabela.toString()).get(numeroLinha);
		}						
	}

	public  <TOption extends java.lang.Enum> java.util.HashMap<Integer,java.util.HashMap<String,Object>> autGetAllParametersFromTable(TOption nomeTabela){
		return AUT_GLOBAL_PARAMETERS.get(nomeTabela.toString());		
	}

	public <TOption extends java.lang.Enum<AUT_TABLE_PARAMETERS_NAMES>> java.util.HashMap<String,Object> autGetParametersFromTable(TOption nomeTabela){
		try {
			return autGetParametersFromTable(nomeTabela,1);
		} catch (NumberFormatException | SQLException e) {
			System.out.println("AUT ERROR: GET DATAFLOW PARAMETERS");
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}				
	}


	/**
	 * Executa procedimentos para copi de dados de um hash map orirgem para o destino especificado
	 * 
	 * 
	 * @param dataFlowOrig - Dataflow de origem 
	 * @param dataFlowDest - Dataflow de destino dos dados
	 * 
	 * @return AUTDataFlow - Objeto resultante do merge de dados entre estruturas
	 * 
	 */
	public AUTDataFlow autCopyDataFlow(AUTDataFlow dataFlowOrig,AUTDataFlow dataFlowDest) {
		try {
			System.out.println("AUT INFO: DATAFLOW : COPY DATA : INIT");
			System.out.println("BEFORE : DATA FLOW ORIG:");
			System.out.println(dataFlowOrig);
			System.out.println("BEFORE : DATA FLOW DEST:");
			System.out.println(dataFlowDest);

			for(String keyTable : dataFlowOrig.AUT_GLOBAL_PARAMETERS.keySet()) {
				if(dataFlowDest.AUT_GLOBAL_PARAMETERS.containsKey(keyTable)) {
					dataFlowDest.AUT_GLOBAL_PARAMETERS.remove(keyTable);
					dataFlowDest.AUT_GLOBAL_PARAMETERS.put(keyTable, dataFlowOrig.AUT_GLOBAL_PARAMETERS.get(keyTable));
				}
			}

			System.out.println("AFTER : DATA FLOW ORIG:");
			System.out.println(dataFlowOrig);
			System.out.println("AFTER : DATA FLOW DEST:");
			System.out.println(dataFlowDest);

			System.out.println("AUT INFO: DATAFLOW : COPY DATA : END");
			return dataFlowDest;
		}
		catch(java.lang.Exception e) {
			System.out.println("AUT ERRO: DATAFLOW : COPY DATA");
			System.out.println(e.getMessage());
			e.printStackTrace();			

			return null;
		}
	}
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

	public enum AUT_HMC_PERFIL_ACESSO{
		USUARIO_LOJA,
		APROVADOR_COMERCIAL,
		PJ_CADASTRO_EXCECAO,
		USUARIO_TELEVENDAS

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
		AUT_VA_GERACAO_PEDIDOS,
		AUT_SAP_ABASTECIMENTO,
		AUT_SAP_FATURAMENTO_ZOSDGCP,
		AUT_PDV_LINX,
		AUT_SAFE_VALE_TROCA_LINX,
		AUT_SAFE_CADASTROS_CLIENTE_CONVENIADO_LINX,
		AUT_SAFE_GERADOR_VOUCHER_LINX,
		RSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00003,
		RSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00004,
		RSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00005,
		RSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00006,
		RSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00007,
		RSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00008,
		RSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00009,
		RSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00010,
		RSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00011,
		RSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00012,
		RSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00013,
		RSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00014,
		RSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00015,
		RSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00016,
		RSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00017,
		RSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00018,
		RSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00019,
		RSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00020,
		RSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00021,
		RSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00022,
		RSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00023,
		RSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00024,
		RSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00025,
		RSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00026,
		RSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00027,

		RSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTR00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00002_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00003_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00004_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTR00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTR00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00007_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTR00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00009_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00011_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00012_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00013_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00014_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00015_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00016_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00017_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00018_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00019_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00023_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00024_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00025_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00026_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00027_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00028_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00029_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00030_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00031_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00032_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00033_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00034_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00035_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00036_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00037_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00038_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00039_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00040_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00041_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00042_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00043_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00044_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00045_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00046_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00047_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00048_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00049_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00050_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00051_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00052_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00053_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00054_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00055_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00059_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00060_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00061_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00062_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00063_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00064_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00065_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00066_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00067_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00068_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00069_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00070_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00071_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00072_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00073_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00074_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00075_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00076_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00077_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00078_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00079_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTR00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00081_CTR00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00081_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTR00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTR00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00084_CTR00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00084_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTR00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00086_CTR00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00086_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTR00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00088_CTR00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00088_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00089_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00090_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00091_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00092_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00093_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00094_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00001_CN00095_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00002_CN00001_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00002_CN00002_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00002_CN00003_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00002_CN00004_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00002_CN00005_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00002_CN00006_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00002_CN00007_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00002_CN00008_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00002_CN00009_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00002_CN00010_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00002_CN00011_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00002_CN00012_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00002_CN00013_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00002_CN00014_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00002_CN00015_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00002_CN00016_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00002_CN00017_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00002_CN00018_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00002_CN00019_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00002_CN00020_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00002_CN00021_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00002_CN00022_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00002_CN00023_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00002_CN00024_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00002_CN00025_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00002_CN00026_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00002_CN00027_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00002_CN00028_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00002_CN00029_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00002_CN00030_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00002_CN00031_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00002_CN00032_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTR00001,
		RSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00003_CN00002_CTR00001,
		RSP_PJTTRC_FRT001_VA_MD00003_CN00002_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00003_CN00003_CTR00001,
		RSP_PJTTRC_FRT001_VA_MD00003_CN00003_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00003_CN00004_CTR00001,
		RSP_PJTTRC_FRT001_VA_MD00003_CN00004_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00003_CN00005_CTR00001,
		RSP_PJTTRC_FRT001_VA_MD00003_CN00005_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00004,
		RSP_PJTTRC_FRT001_VA_MD00004_CN00001_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00004_CN00002_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00004_CN00003_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00004_CN00004_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00004_CN00005_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTR00001,
		RSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001,
		RSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00004_CN00008_CTR00001,
		RSP_PJTTRC_FRT001_VA_MD00004_CN00008_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTR00001,
		RSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001,
		RSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTR00001,
		RSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTR00001,
		RSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTR00001,
		RSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTR00001,
		RSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTR00001,
		RSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTR00001,
		RSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTR00001,
		RSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00004_CN00018_CTR00001,
		RSP_PJTTRC_FRT001_VA_MD00004_CN00018_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00005_CN00001_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00005_CN00002_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00005_CN00003_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00005_CN00004_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00005_CN00005_CTR00001,
		RSP_PJTTRC_FRT001_VA_MD00005_CN00005_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00005_CN00006_CTR00001,
		RSP_PJTTRC_FRT001_VA_MD00005_CN00006_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00005_CN00007_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00005_CN00008_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00005_CN00009_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00005_CN00010_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00005_CN00011_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00005_CN00012_CTR00001,
		RSP_PJTTRC_FRT001_VA_MD00005_CN00012_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00005_CN00013_CTR00001,
		RSP_PJTTRC_FRT001_VA_MD00005_CN00013_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00006_CN00001_CTR00001,
		RSP_PJTTRC_FRT001_VA_MD00006_CN00001_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00006_CN00002_CTR00001,
		RSP_PJTTRC_FRT001_VA_MD00006_CN00002_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00006_CN00003_CTR00001,
		RSP_PJTTRC_FRT001_VA_MD00006_CN00003_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00007_CN00001_CTP00001,		
		RSP_PJTTRC_FRT001_VA_MD00007_CN00002_CTP00001,
		RSP_PJTTRC_FRT001_VA_CMP_CAT001_CMP00001,
		RSP_PJTTRC_FRT001_VA_CMP_CAT001_CMP00002,
		RSP_PJTTRC_FRT001_VA_CMP_CAT001_CMP00003,
		RSP_PJTTRC_FRT001_VA_CMP_CAT001_CMP00004,
		RSP_PJTTRC_FRT001_VA_CMP_CAT001_CMP00005,
		RSP_PJTTRC_FRT001_VA_CMP_CAT001_CMP00006,
		RSP_PJTTRC_FRT001_VA_CMP_CAT002_CMP00001,
		RSP_PJTTRC_FRT001_VA_CMP_CAT002_CMP00002,
		RSP_PJTTRC_FRT001_VA_CMP_CAT002_CMP00003,
		RSP_PJTTRC_FRT001_VA_CMP_CAT003_CMP00001,
		RSP_PJTTRC_FRT001_VA_CMP_CAT003_CMP00002,
		RSP_PJTTRC_FRT001_VA_CMP_CAT003_CMP00003,
		RSP_PJTTRC_FRT001_VA_CMP_CAT004_CMP00001,
		RSP_PJTTRC_FRT001_VA_CMP_CAT005_CMP00001,
		RSP_PJTTRC_FRT001_VA_CMP_CAT005_CMP00002,
		RSP_PJTTRC_FRT001_VA_CMP_CAT006_CMP00001,
		RSP_PJTTRC_FRT001_VA_CMP_CAT006_CMP00002,
		RSP_PJTTRC_FRT001_VA_CMP_CAT006_CMP00003,
		RSP_PJTTRC_FRT001_VA_CMP_CAT006_CMP00004,
		RSP_PJTTRC_FRT001_VA_CMP_CAT006_CMP00005,
		RSP_PJTTRC_FRT001_VA_CMP_CAT006_CMP00006,
		RSP_PJTTRC_FRT001_VA_CMP_CAT006_CMP00007,
		RSP_PJTTRC_FRT001_VA_CMP_CAT006_CMP00008,
		RSP_PJTTRC_FRT001_VA_CMP_CAT007_CMP00001,
		RSP_PJTTRC_FRT001_VA_CMP_CAT007_CMP00002,
		RSP_PJTTRC_FRT001_VA_CMP_CAT007_CMP00003,
		RSP_PJTTRC_FRT001_VA_CMP_CAT007_CMP00004,
		RSP_PJTTRC_FRT001_VA_CMP_CAT007_CMP00005,
		RSP_PJTTRC_FRT001_VA_CMP_CAT007_CMP00006,
		RSP_PJTTRC_FRT001_VA_CMP_CAT007_CMP00007,
		RSP_PJTTRC_FRT001_VA_CMP_CAT007_CMP00008,
		RSP_PJTTRC_FRT001_VA_CMP_CAT007_CMP00009,
		RSP_PJTTRC_FRT001_VA_CMP_CAT007_CMP00010,
		RSP_PJTTRC_FRT001_VA_CMP_CAT007_CMP00011,
		RSP_PJTTRC_FRT001_VA_CMP_CAT008_CMP00001,
		RSP_PJTTRC_FRT001_VA_CMP_CAT008_CMP00002,
		RSP_PJTTRC_FRT001_VA_CMP_CAT008_CMP00003,
		RSP_PJTTRC_FRT001_VA_CMP_CAT009_CMP00001,
		RSP_PJTTRC_FRT001_VA_CMP_CAT009_CMP00002,
		RSP_PJTTRC_FRT001_VA_CMP_CAT009_CMP00003,
		RSP_PJTTRC_FRT001_VA_CMP_CAT009_CMP00004,
		RSP_PJTTRC_FRT001_VA_CMP_CAT009_CMP00005,
		RSP_PJTTRC_FRT001_VA_CMP_CAT009_CMP00006,
		RSP_PJTTRC_FRT001_VA_CMP_CAT009_CMP00007,
		RSP_PJTTRC_FRT001_VA_CMP_CAT009_CMP00008,
		RSP_PJTTRC_FRT001_VA_CMP_CAT009_CMP00009,
		RSP_PJTTRC_FRT001_VA_CMP_CAT009_CMP00010,
		RSP_PJTTRC_FRT001_VA_CMP_CAT009_CMP00011,
		RSP_PJTTRC_FRT001_VA_CMP_CAT009_CMP00012,
		RSP_PJTTRC_FRT001_VA_CMP_CAT009_CMP00013,
		RSP_PJTTRC_FRT001_VA_CMP_CAT010_CMP00001,
		RSP_PJTTRC_FRT001_VA_CMP_CAT011_CMP00001,
		RSP_PJTTRC_FRT001_VA_CMP_CAT011_CMP00002,
		RSP_PJTTRC_FRT001_VA_CMP_CAT012_CMP00001,
		RSP_PJTTRC_FRT001_VA_CMP_CAT012_CMP00002,
		RSP_PJTTRC_FRT001_VA_CMP_CAT012_CMP00003,
		RSP_PJTTRC_FRT001_VA_CMP_CAT012_CMP00004,
		RSP_PJTTRC_FRT001_VA_CMP_CAT012_CMP00005,
		RSP_PJTTRC_FRT001_VA_CMP_CAT012_CMP00006,
		RSP_PJTTRC_FRT001_VA_CMP_CAT012_CMP00007,
		RSP_PJTTRC_FRT001_VA_CMP_CAT012_CMP00008,
		RSP_PJTTRC_FRT001_VA_CMP_CAT013_CMP00001,
		RSP_PJTTRC_FRT001_VA_CMP_CAT014_CMP00001,
		RSP_PJTTRC_FRT001_VA_CMP_CAT014_CMP00002,
		RSP_PJTTRC_FRT001_VA_CMP_CAT014_CMP00003,
		RSP_PJTTRC_FRT001_VA_CMP_CAT015_CMP00001,
		RSP_PJTTRC_FRT001_VA_CMP_CAT015_CMP00002,
		RSP_PJTTRC_FRT001_VA_CMP_CAT016_CMP00001,
		RSP_PJTTRC_FRT001_VA_CMP_CAT016_CMP00002,
		RSP_PJTTRC_FRT001_VA_CMP_CAT017_CMP00001,
		RSP_PJTTRC_FRT001_VA_CMP_CAT017_CMP00002,
		RSP_PJTTRC_FRT001_VA_CMP_CAT018_CMP00001,
		RSP_PJTTRC_FRT001_VA_CMP_CAT019_CMP00001,
		RSP_PJTTRC_FRT001_VA_CMP_CAT019_CMP00002,
		RSP_PJTTRC_FRT001_VA_CMP_CAT019_CMP00003,
		RSP_PJTTRC_FRT001_VA_CMP_CAT019_CMP00004;	

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			switch(this) {
			case RSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTR00001:{
				return "CN00004_CADASTRO_CLIENTE_PF_LOJA0035";
			}
			case AUT_SAFE_GERADOR_VOUCHER_LINX:{
				return "AUTSAFEGERADORVOUCHER001";
			}
			case AUT_SAFE_CADASTROS_CLIENTE_CONVENIADO_LINX:{
				return "AUTSAFECADASTROSCLIENTESCONVENLINX001";
			}
			case AUT_SAFE_VALE_TROCA_LINX:{
				return "AUTSAFELINX001";
			}
			case AUT_PDV_LINX:{
				return "AUTPDVLINX001";
			}
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
			case AUT_SAP_ABASTECIMENTO:{
				return "AUTSAPABASTESTOQUE001";
			}
			case AUT_SAP_FATURAMENTO_ZOSDGCP:{
				return "AUTSAPFATZOSDGCP001";
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

	public java.util.HashMap<String,
	java.util.HashMap<Integer,java.util.HashMap<String,Object>>> autInitDataFlow() {
		if(AUT_ENABLE_SYNCRONIZE_ALL_OBJECTS) {
			return autInitDataFlowConfiguration(AUT_ENABLE_SYNCRONIZE_ALL_OBJECTS);
		}
		else {
			return autInitDataFlowConfiguration(AUT_ENABLE_SYNCRONIZE_ALL_OBJECTS);
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
	java.util.HashMap<Integer,java.util.HashMap<String,Object>>> autInitDataFlowConfiguration(boolean syncronizeAllObject) {
		try {
			AUT_CURRENT_RANDOM_MANAGER = new AUTNumerosRandomicos();


			AUT_GLOBAL_PARAMETERS = 
					(
							syncronizeAllObject 
							? 
									new java.util.HashMap<String,java.util.HashMap<Integer,java.util.HashMap<String, Object>>>() 
									: 
										(
												AUT_GLOBAL_PARAMETERS==null ? 
														new java.util.HashMap<String,java.util.HashMap<Integer,java.util.HashMap<String, Object>>>() 
														: 
															AUT_GLOBAL_PARAMETERS
												)
							);


			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataLogin = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();

			/**
			 * 
			 * Configuração de parametros para o login
			 * 
			 */
			vaDataLogin.put(1, new java.util.HashMap<String,Object>());
			vaDataLogin.get(1).put("AUT_USER", "55000035");//"55000001");
			vaDataLogin.get(1).put("AUT_PASSWORD", "1234");
			vaDataLogin.get(1).put("AUT_URL_VA", "https://vahomolog.leroymerlin.com.br/va/lmbr/pt/BRL/login");			
			vaDataLogin.get(1).put("AUT_URL_BOITATA", "https://homolog.leroymerlin.com.br");


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

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataCadastro = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();

			vaDataCadastro.put(1, new java.util.HashMap<String,Object>());

			vaDataCadastro.put(1, new java.util.HashMap<String,Object>());
			String codEstrangeiro = AUTProjectsFunctions.gerarEstrangeiro();
			String cnpj = AUTProjectsFunctions.gerarCNPJ();
			String cpf = AUTProjectsFunctions.gerarCPF();
			String cpfEstrangeiro = AUTProjectsFunctions.gerarCPF();
			String inscricaoEstadual = AUTProjectsFunctions.autGetIncriptionRS();

			vaDataCadastro.get(1).put("AUT_TIPO_CADASTRO", AUT_VA_CADASTROS.JURIDICA);
			vaDataCadastro.get(1).put("AUT_PASSAPORTE", codEstrangeiro);
			vaDataCadastro.get(1).put("AUT_CNPJ", cnpj);
			vaDataCadastro.get(1).put("AUT_CPF", cpf);
			vaDataCadastro.get(1).put("AUT_CPF_ESTRANGEIRO", cpfEstrangeiro);
			vaDataCadastro.get(1).put("AUT_NOME_ESTRANGEIRO", String.format("AUT NOME ESTRANG: %s : CPF EST: %s",codEstrangeiro,cpfEstrangeiro));	
			vaDataCadastro.get(1).put("AUT_NOME", "AUT NOME PF: ".concat(cpf));	
			vaDataCadastro.get(1).put("AUT_NOME_COMPLETO", "AUT NOME COMPLETO: ".concat(cpf));	
			vaDataCadastro.get(1).put("AUT_NOME_PJ", "AUT NOME PJ: ".concat(cnpj));	
			vaDataCadastro.get(1).put("AUT_NOME_PJ_FANTASIA", "AUT NOME PJ:".concat(AUTProjectsFunctions.gerarEstrangeiro()));	
			vaDataCadastro.get(1).put("AUT_NOME_PJ_FANTASIA2", "AUT NOME PJ 2:".concat(AUTProjectsFunctions.gerarEstrangeiro()));	
			vaDataCadastro.get(1).put("AUT_EMAIL", "aut.qaemail@automation.com");	
			vaDataCadastro.get(1).put("AUT_INCRICAO_ESTADUAL", inscricaoEstadual);
			vaDataCadastro.get(1).put("AUT_INCRICAO_ESTADUAL_PF", inscricaoEstadual);	
			vaDataCadastro.get(1).put("AUT_INCRICAO_MUNICIPAL", inscricaoEstadual);	
			vaDataCadastro.get(1).put("AUT_NOME_PJ_CONTATO", "NOME CONTATO PJ");	
			vaDataCadastro.get(1).put("AUT_PJ_EMAIL_CONTATO", "EMAIL.QA@TESTE.COM.BR");	
			vaDataCadastro.get(1).put("AUT_PJ_DEPARTAMENTO_CONTATO", "QUALIDADE");	
			vaDataCadastro.get(1).put("AUT_TIPO_TELEFONE", AUT_VA_TIPO_CONTATO.CELULAR.name());	
			vaDataCadastro.get(1).put("AUT_NUMERO_TELEFONE", "11966447035");	
			vaDataCadastro.get(1).put("AUT_NUMERO_TELEFONE_2", "1196306-3067");	
			vaDataCadastro.get(1).put("AUT_UF_PESQUISA", AUT_VA_ESTADOS.RS);	
			vaDataCadastro.get(1).put("AUT_CIDADE_PESQUISA", "CANOAS");
			vaDataCadastro.get(1).put("AUT_ENDERECO_PESQUISA", "AUT RUA SERGIPE");
			vaDataCadastro.get(1).put("AUT_BAIRRO_PESQUISA", "NITEROI");	
			vaDataCadastro.get(1).put("AUT_TIPO_ENDERECO", AUT_VA_TIPO_ENDERECO.OBRA);

			vaDataCadastro.get(1).put("AUT_CEP", "92130-270");	
			vaDataCadastro.get(1).put("AUT_RUA_ENDERECO", "AUT RUA SERGIPE");	
			vaDataCadastro.get(1).put("AUT_NUMERO_ENDERECO", "256");	
			vaDataCadastro.get(1).put("AUT_BAIRRO_ENDERECO", "NITEROI");	
			vaDataCadastro.get(1).put("AUT_COMPLEMENTO_ENDERECO", "CASA 123");	
			vaDataCadastro.get(1).put("AUT_CIDADE_ENDERECO", "CANOAS");	
			vaDataCadastro.get(1).put("AUT_ESTADO_ENDERECO", AUT_VA_ESTADOS.RS);	
			vaDataCadastro.get(1).put("AUT_REFERENCIA_ENDERECO", "ACOUGUE DA ESQUINA");	
			vaDataCadastro.get(1).put("AUT_TIPO_IMOVEL_RESIDENCIA", AUT_VA_TIPO_RESIDENCIA.RURAL_CHACARA_FAZENDA_OU_SITIO);	

			vaDataCadastro.get(1).put("AUT_CEP_2", "92130-270");	
			vaDataCadastro.get(1).put("AUT_RUA_ENDERECO_2", "AUT RUA SERGIPE");	
			vaDataCadastro.get(1).put("AUT_NUMERO_ENDERECO_2", "256");	
			vaDataCadastro.get(1).put("AUT_BAIRRO_ENDERECO_2", "NITEROI");	
			vaDataCadastro.get(1).put("AUT_COMPLEMENTO_ENDERECO_2", "CASA 123");	
			vaDataCadastro.get(1).put("AUT_CIDADE_ENDERECO_2", "CANOAS");	
			vaDataCadastro.get(1).put("AUT_ESTADO_ENDERECO_2", AUT_VA_ESTADOS.RS);	
			vaDataCadastro.get(1).put("AUT_REFERENCIA_ENDERECO_2", "Loja Sabaroa");	
			vaDataCadastro.get(1).put("AUT_TIPO_IMOVEL_RESIDENCIA_2", AUT_VA_TIPO_RESIDENCIA.LOJA_OU_SOBRELOJA);	

			vaDataCadastro.get(1).put("AUT_CNPJ_INVALIDO", "37.764.388/0009-90");	
			vaDataCadastro.get(1).put("AUT_CPF_INVALIDO", "510.354.523-93");	

			vaDataCadastro.get(1).put("AUT_CEP_INVALIDO", "12345678");


			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.AUT_VA_CADASTROS.toString(), vaDataCadastro);


			/*
			 *MASSA DE DADOS PARA GERACAO DE PEDIDOS 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataGeracaoPedidos001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();

			vaDataGeracaoPedidos001.put(1, new java.util.HashMap<String, Object>());
			vaDataGeracaoPedidos001.get(1).put("AUT_USUARIO_LOJA", "5500368019793");
			vaDataGeracaoPedidos001.get(1).put("AUT_SENHA", "1234");
			vaDataGeracaoPedidos001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataGeracaoPedidos001.get(1).put("AUT_CODIGO_ITEM", "89296193");//89407066"89296193-ItemOficial	");	// "89296193"|"89368790");
			vaDataGeracaoPedidos001.get(1).put("AUT_CODIGO_ITEM2", "89296193");
			vaDataGeracaoPedidos001.get(1).put("AUT_CPF_CLIENTE_NOVO", AUTProjectsFunctions.gerarCPF());
			vaDataGeracaoPedidos001.get(1).put("AUT_CPF_CLIENTE_CADASTRADO", "78651738811");
			vaDataGeracaoPedidos001.get(1).put("AUT_PASSAPORTE", "78651738811");
			vaDataGeracaoPedidos001.get(1).put("AUT_CNPJ", "78651738811");
			vaDataGeracaoPedidos001.get(1).put("AUT_CPF", "78651738811");
			vaDataGeracaoPedidos001.get(1).put("AUT_NUMERO_CARTAO", "4000000000000044");
			vaDataGeracaoPedidos001.get(1).put("AUT_NOME_TITULAR", "Usuário Automação");
			vaDataGeracaoPedidos001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataGeracaoPedidos001.get(1).put("AUT_CODIGO_CARTAO", "451");


			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.AUT_VA_GERACAO_PEDIDOS.toString(), vaDataGeracaoPedidos001);



			/*
			 *  MASSA DE DADOS PARA CONFIGURAÇÕES E PARAMETRIZAÇÕES DO HMC - HYBRIS
			 * 
			 */
			java.util.HashMap<Integer, java.util.HashMap<String,Object>> hmcLogin = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();

			hmcLogin.put(1, new java.util.HashMap<String,Object>());

			hmcLogin.get(1).put("AUT_URL", "https://10.56.62.199:9002/hmc/hybris");
			hmcLogin.get(1).put("AUT_USER2", "marcos.oliveira");
			hmcLogin.get(1).put("AUT_PASSWORD", "1234");
			hmcLogin.get(1).put("AUT_USER_ID", "marcos.oliveira");//"55".concat(AUTProjectsFunctions.gerarItemChaveRandomico(6)));	//criar numero randomico
			hmcLogin.get(1).put("AUT_USER_NAME", "AUT HMCVA USER: ");	//Criar nome padronizado
			hmcLogin.get(1).put("AUT_USER_EMAIL", "automationTest@softtek.com");
			hmcLogin.get(1).put("AUT_NOVA_SENHA", "1234");
			hmcLogin.get(1).put("AUT_CANAL", "channel_store");
			hmcLogin.get(1).put("AUT_TIPO", "B2BCustomer - Cliente B2B");
			hmcLogin.get(1).put("AUT_UNIDADE_B2B_PADRAO", "0045_LMStore");
			hmcLogin.get(1).put("AUT_DEPARTAMENTO", "50000425-PROJETO 3D VENDA ASSISTIDA");
			hmcLogin.get(1).put("AUT_CODIGO_CATEGORIA", "999");
			hmcLogin.get(1).put("AUT_CODIGO_DEPARTAMENTO", "50000425");
			hmcLogin.get(1).put("AUT_GESTOR", "51017672");
			hmcLogin.get(1).put("AUT_LOJA", "0045_LMStore");
			hmcLogin.get(1).put("AUT_PERFIL_ACESSO", AUT_HMC_PERFIL_ACESSO.USUARIO_TELEVENDAS.name());
			hmcLogin.get(1).put("AUT_PAIS_USUARIO", "Português do Brasil");
			hmcLogin.get(1).put("AUT_MOEDA_PADRAO", "BRL");


			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.AUT_HMC_LOGIN.toString(), hmcLogin);


			java.util.HashMap<Integer, java.util.HashMap<String,Object>> sapAbastecimento = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();
			sapAbastecimento.put(1, new java.util.HashMap<String,Object>());
			sapAbastecimento.get(1).put("AUT_USER", "51028487");
			sapAbastecimento.get(1).put("AUT_PWD", "Auto5@2020");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.AUT_SAP_ABASTECIMENTO.toString(), sapAbastecimento);

			java.util.HashMap<Integer, java.util.HashMap<String,Object>> sapFatZOSDGCP = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();
			sapFatZOSDGCP.put(1, new java.util.HashMap<String,Object>());

			sapFatZOSDGCP.get(1).put("USER_SAP", "51028487");
			sapFatZOSDGCP.get(1).put("PWD_SAP", "Auto5@2020");
			sapFatZOSDGCP.get(1).put("INIT_TRANSACTION", true);
			sapFatZOSDGCP.get(1).put("AUT_PEDIDO", "939404");
			sapFatZOSDGCP.get(1).put("AUT_DOCUMENTO_SD", "30003866");
			sapFatZOSDGCP.get(1).put("AUT_TIPO_PEDIDO", "ZVAS");
			sapFatZOSDGCP.get(1).put("AUT_LOJA", "0035");
			sapFatZOSDGCP.get(1).put("AUT_DATA_INICIAL", "01.01.2019");
			sapFatZOSDGCP.get(1).put("AUT_DATA_FINAL", "01.02.2019");
			sapFatZOSDGCP.get(1).put("AUT_TIPO_PEDIDO", "ZVAS");

			sapFatZOSDGCP.get(1).put("AUT_FILA", "ARMAZENA");
			sapFatZOSDGCP.get(1).put("AUT_FORMATO", "16X20ITS");
			sapFatZOSDGCP.get(1).put("AUT_DOC_FORNECIMENTO", "800000109");
			sapFatZOSDGCP.get(1).put("INIT_TRANSACTION", true);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.AUT_SAP_FATURAMENTO_ZOSDGCP.toString(), sapFatZOSDGCP);


			java.util.HashMap<Integer, java.util.HashMap<String,Object>> pdv = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();
			pdv.put(1, new java.util.HashMap<String,Object>());
			pdv.get(1).put("AUT_OPERADOR", "951028487");
			pdv.get(1).put("AUT_PWD_OPERADOR", "9951028487");
			pdv.get(1).put("AUT_COORDENADOR", "51028487");
			pdv.get(1).put("AUT_PWD_COORDENADOR", "51028487");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.AUT_PDV_LINX.toString(), pdv);


			java.util.HashMap<Integer, java.util.HashMap<String,Object>> safeLinx = new java.util.HashMap<Integer, java.util.HashMap<String,Object>>();
			safeLinx.put(1, new java.util.HashMap<String,Object>());
			safeLinx.get(1).put("AUT_URL", "http://10.56.96.170/safe/asp/default.asp");
			//safeLinx.get(1).put("AUT_USER", "51028496");
			//safeLinx.get(1).put("AUT_PWD", "Leroy123");
			safeLinx.get(1).put("AUT_USER", "51028487");
			safeLinx.get(1).put("AUT_PWD", "Auto5@2017");
			safeLinx.get(1).put("AUT_TYPE_PERSON",AUT_SAFE_TYPE_PERSONS.FISICA);
			safeLinx.get(1).put("AUT_TYPE_DOC_FOREIGN",AUT_SAFE_TYPE_PERSONS.PASSAPORTE);
			safeLinx.get(1).put("AUT_DOCUMENT", "95102358146");



			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.AUT_SAFE_VALE_TROCA_LINX.toString(), safeLinx);



			java.util.HashMap<Integer, java.util.HashMap<String,Object>> safeCadCliConvenioLinx = new java.util.HashMap<Integer, java.util.HashMap<String,Object>>();
			safeCadCliConvenioLinx.put(1, new java.util.HashMap<String,Object>());

			safeCadCliConvenioLinx.get(1).put("AUT_USER", "51028487");
			safeCadCliConvenioLinx.get(1).put("AUT_PWD", "Auto5@2017");

			safeCadCliConvenioLinx.get(1).put("AUT_TIPO_CONVENIO", AUT_SAFE_TIPO_CONVENIO.COLABORADOR);
			safeCadCliConvenioLinx.get(1).put("AUT_TIPO_PESSOA", AUT_SAFE_TYPE_PERSONS.FISICA2);
			safeCadCliConvenioLinx.get(1).put("AUT_DOCUMENTO", AUTProjectsFunctions.gerarCPF());
			safeCadCliConvenioLinx.get(1).put("AUT_PF_RG", "124536879");			
			safeCadCliConvenioLinx.get(1).put("AUT_ORG_EMISSOR", "SSP/SP");
			safeCadCliConvenioLinx.get(1).put("AUT_DATA_NASCIMENTO", "23/02/1989");
			safeCadCliConvenioLinx.get(1).put("AUT_PROFISSAO", AUT_SAFE_PROFISSOES.ARQUITETO);
			safeCadCliConvenioLinx.get(1).put("AUT_EMAIL", "automation@softtek.com");
			safeCadCliConvenioLinx.get(1).put("AUT_LOGRADOURO", "LOGRADOURO TESTES");
			safeCadCliConvenioLinx.get(1).put("AUT_NUMERO_ENDERECO", "235");
			safeCadCliConvenioLinx.get(1).put("AUT_COMPLEMENTO_ENDERECO", "automation@softtek.com");
			safeCadCliConvenioLinx.get(1).put("AUT_BAIRRO", "JORDANESIA");
			safeCadCliConvenioLinx.get(1).put("AUT_CEP", "JORDANESIA");
			safeCadCliConvenioLinx.get(1).put("AUT_CIDADE", "CAJAMAR");
			safeCadCliConvenioLinx.get(1).put("AUT_BAIRRO", "JORDANESIA");
			safeCadCliConvenioLinx.get(1).put("AUT_UF", "SP");
			safeCadCliConvenioLinx.get(1).put("AUT_TEL_DD1", "11");
			safeCadCliConvenioLinx.get(1).put("AUT_TEL_FONE1", "963730179");
			safeCadCliConvenioLinx.get(1).put("AUT_TEL_RAMAL1", "0286");
			safeCadCliConvenioLinx.get(1).put("AUT_PF_NOME", vaDataCadastro.get(1).get("AUT_NOME"));
			safeCadCliConvenioLinx.get(1).put("AUT_PJ_NOME", vaDataCadastro.get(1).get("AUT_NOME_PJ"));
			safeCadCliConvenioLinx.get(1).put("AUT_ESTRANGEIRO_NOME", vaDataCadastro.get(1).get("AUT_NOME_ESTRANGEIRO"));

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.AUT_SAFE_CADASTROS_CLIENTE_CONVENIADO_LINX.toString(), safeCadCliConvenioLinx);


			java.util.HashMap<Integer, java.util.HashMap<String,Object>> safeGeradorVoucherLinx = new java.util.HashMap<Integer, java.util.HashMap<String,Object>>();
			safeGeradorVoucherLinx.put(1, new java.util.HashMap<String,Object>());
			safeGeradorVoucherLinx.get(1).put("AUT_TIPO_CONVENIO", AUT_SAFE_TIPO_CONVENIO.COLABORADOR);
			safeGeradorVoucherLinx.get(1).put("AUT_TIPO_PESSOA", AUT_SAFE_TYPE_PERSONS.FISICA2);
			safeGeradorVoucherLinx.get(1).put("AUT_DOCUMENTO", "45774410471");
			safeGeradorVoucherLinx.get(1).put("AUT_CLIENTE_NOME", "AUT NOME CLIENTE :".concat(safeGeradorVoucherLinx.get(1).get("AUT_DOCUMENTO").toString()));
			safeGeradorVoucherLinx.get(1).put("AUT_VALOR_VOUCHER", "50000000");
			safeGeradorVoucherLinx.get(1).put("AUT_LOJA",AUT_SAFE_LOJAS_ENUM.LJ0035);
			safeGeradorVoucherLinx.get(1).put("AUT_OBSERVACOES", "OBSERVACOES : GERADOR VOUCHER : ".concat(safeGeradorVoucherLinx.get(1).get("AUT_CLIENTE_NOME").toString()));


			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.AUT_SAFE_GERADOR_VOUCHER_LINX.toString(), safeGeradorVoucherLinx);





			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN000001_CTR00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTR00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTR00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTR00001.get(1).put("AUT_USER","55000035");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTR00001.get(1).put("AUT_CODIGO_ITEM","89407066");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTR00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTR00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTR00001);


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00009_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTP00001.put(1, new java.util.HashMap<String,Object>());			

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTP00001.get(1).put("AUT_USER", "55000035");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "758.536.958-19");  //  4294271183
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTP00001.get(1).put("AUT_TELEFONE_SERVIÃ‡O", "1187272345");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTP00001.get(1).put("AUT_NUMERO_ORCAMENTO", "");



			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTP00001);		




			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00002_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00002_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00002_CTP00001.put(1, new java.util.HashMap<String,Object>());			

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00002_CTP00001.get(1).put("AUT_USER", "55001063");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00002_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00002_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00002_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00002_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00002_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00002_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00002_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00002_CTP00001.get(1).put("AUT_TELEFONE_SERVIÃ‡O", "1187272345");



			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00002_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00002_CTP00001);



			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00003_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente do sistema VA
			 * 
			 * 
			 */
			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00003_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00003_CTP00001.put(1, new java.util.HashMap<String,Object>());			

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00003_CTP00001.get(1).put("AUT_USER", "55001063");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00003_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00003_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00003_CTP00001.get(1).put("AUT_CODIGO_ITEM_VA","89296193");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00003_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00003_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00003_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00003_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00003_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00003_CTP00001.get(1).put("AUT_TELEFONE_SERVIÃ‡O", "1187272345");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00003_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00003_CTP00001);

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00004_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente do sistema VA
			 * 
			 * 
			 */
			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00004_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00004_CTP00001.put(1, new java.util.HashMap<String,Object>());			

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00004_CTP00001.get(1).put("AUT_USER", "55001063");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00004_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00004_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00004_CTP00001.get(1).put("AUT_CODIGO_ITEM_VA","89296193");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00004_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00004_CTP00001.get(1).put("AUT_DESCONTO", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00004_CTP00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00004_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00004_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00004_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00004_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00004_CTP00001.get(1).put("AUT_TIPO_DESCONTO",br.lry.components.va.cat010.AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00004_CTP00001.get(1).put("AUT_TELEFONE_SERVIÃ‡O", "1187272345");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00004_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00004_CTP00001);


			//Criar carrinho no VA Hybris - RSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTR00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Pedidos do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTR00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTR00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTR00001.get(1).put("AUT_USER", "55001063");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTR00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTR00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTR00001.get(1).put("AUT_NUMERO_CARTAO", "4000000000000044");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTR00001.get(1).put("AUT_NOME_TITULAR", "Marcos Paulo de Oliveira");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTR00001.get(1).put("AUT_VALIDADE", "09/23");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTR00001.get(1).put("AUT_CODIGO_CARTAO", "183");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTR00001.get(1).put("AUT_NUMERO_PEDIDO","");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTR00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTR00001);


			// Realizar um pedido atravÃ©s da recuperaÃ§Ã£o do carrinho - RSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Pedidos do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTP00001.get(1).put("AUT_USER", "55001063");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTP00001.get(1).put("AUT_NUMERO_CARTAO", "4000000000000044");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTP00001.get(1).put("AUT_NOME_TITULAR", "Marcos Paulo de Oliveira");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTP00001.get(1).put("AUT_VALIDADE", "09/23");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTP00001.get(1).put("AUT_CODIGO_CARTAO", "183");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTP00001.get(1).put("AUT_NUMERO_CARRINHO","");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTP00001);			


			//Criar carrinho no VA Hybris - RSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTR00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Pedidos do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTR00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTR00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTR00001.get(1).put("AUT_USER", "55001063");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTR00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTR00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTR00001.get(1).put("AUT_NUMERO_CARTAO", "4000000000000044");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTR00001.get(1).put("AUT_NOME_TITULAR", "Marcos Paulo de Oliveira");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTR00001.get(1).put("AUT_VALIDADE", "09/23");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTR00001.get(1).put("AUT_CODIGO_CARTAO", "183");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTR00001.get(1).put("AUT_NUMERO_PEDIDO","");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTR00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTR00001);


			// Realizar um pedido atravÃ©s da recuperaÃ§Ã£o do carrinho - RSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Pedidos do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTP00001.get(1).put("AUT_USER", "55001063");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTP00001.get(1).put("AUT_NUMERO_CARTAO", "4000000000000044");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTP00001.get(1).put("AUT_NOME_TITULAR", "Marcos Paulo de Oliveira");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTP00001.get(1).put("AUT_VALIDADE", "09/23");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTP00001.get(1).put("AUT_CODIGO_CARTAO", "183");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTP00001.get(1).put("AUT_NUMERO_CARRINHO","");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTP00001);			



			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00007_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00007_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00007_CTP00001.put(1, new java.util.HashMap<String,Object>());			

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00007_CTP00001.get(1).put("AUT_USER", "55000035");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00007_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00007_CTP00001.get(1).put("AUT_CODIGO_ITEM","89296193;\n 89296193;\n 89296193");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00007_CTP00001.get(1).put("AUT_CODIGO_ITEM_VA","89296193");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00007_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00007_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00007_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00007_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00007_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);


			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00007_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00007_CTP00001);


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN000008_CTR00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTR00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTR00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTR00001.get(1).put("AUT_USER","55000035");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTR00001.get(1).put("AUT_CODIGO_ITEM","89407066");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTR00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTR00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTR00001);


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00009_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00009_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00009_CTP00001.put(1, new java.util.HashMap<String,Object>());			

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00009_CTP00001.get(1).put("AUT_USER", "55000035");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00009_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00009_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00009_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "758.536.958-19");  //  4294271183
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00009_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00009_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00009_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00009_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00009_CTP00001.get(1).put("AUT_TELEFONE_SERVIÃ‡O", "1187272345");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00009_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00009_CTP00001);		


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN000010_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.put(1, new java.util.HashMap<String,Object>());			

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_USER", "55000045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_CODIGO_ITEM_VA","89296193");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "18.783.107/6918-63");  //  4294271183
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_TELEFONE_SERVIÃ‡O", "1187272345");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_CPF", AUTProjectsFunctions.gerarCPF());

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_TIPO_CADASTRO", AUT_VA_CADASTROS.JURIDICA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_CPF", cpf);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_NOME", "AUT NOME PF: ".concat(cpf));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_NOME_COMPLETO", "AUT NOME COMPLETO: ".concat(AUTProjectsFunctions.gerarCPF()));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_NOME_PJ", "AUT NOME PJ: ".concat(cnpj));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_EMAIL", "aut.qaemail@automation.com");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_INCRICAO_ESTADUAL", "474.018.412.567");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_INCRICAO_ESTADUAL_PF", AUTProjectsFunctions.gerarEstrangeiro());	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_INCRICAO_MUNICIPAL", AUTProjectsFunctions.gerarEstrangeiro());	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_NOME_PJ_CONTATO", "NOME CONTATO PJ".concat(AUTProjectsFunctions.gerarCPF()));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_PJ_EMAIL_CONTATO", "EMAIL.QA@TESTE.COM.BR".concat(AUTProjectsFunctions.gerarCPF()));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_PJ_DEPARTAMENTO_CONTATO", "QUALIDADE".concat(AUTProjectsFunctions.gerarCPF()));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_TIPO_TELEFONE", AUT_VA_TIPO_CONTATO.CELULAR.name());	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_NUMERO_TELEFONE", "11966447035");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_NUMERO_TELEFONE_2", "1196306-3067");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_UF_PESQUISA", AUT_VA_ESTADOS.SP);	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_CIDADE_PESQUISA", "CAJAMAR");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_ENDERECO_PESQUISA", "RUA PREFEITO ANTONIO GARRIDO");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_BAIRRO_PESQUISA", "JORDANÃ‰SIA");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_TIPO_ENDERECO", AUT_VA_TIPO_ENDERECO.OBRA);

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_CEP", "07776-000");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_RUA_ENDERECO", "Rua Explanada");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_NUMERO_ENDERECO", "256");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_BAIRRO_ENDERECO", "EXPLANADA");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_COMPLEMENTO_ENDERECO", "CASA 123");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_CIDADE_ENDERECO", "BOCAIÃšVA");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_ESTADO_ENDERECO", AUT_VA_ESTADOS.MG);	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_REFERENCIA_ENDERECO", "ACOUGUE DA ESQUINA");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_TIPO_IMOVEL_RESIDENCIA", AUT_VA_TIPO_RESIDENCIA.RURAL_CHACARA_FAZENDA_OU_SITIO);	


			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001);		



			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN000011_CTR00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00011_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00011_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00011_CTP00001.get(1).put("AUT_USER","55300745");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00011_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00011_CTP00001.get(1).put("AUT_CODIGO_ITEM","89407066");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00011_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00011_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00011_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00011_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00011_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00011_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00011_CTP00001);


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN000013_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00013_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00013_CTP00001.put(1, new java.util.HashMap<String,Object>());			

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00013_CTP00001.get(1).put("AUT_USER", "55000045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00013_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00013_CTP00001.get(1).put("AUT_CODIGO_ITEM","89576445");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00013_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");  //  4294271183
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00013_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00013_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00013_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00013_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00013_CTP00001.get(1).put("AUT_TELEFONE_SERVIÃ‡O", "1187272345");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00013_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00013_CTP00001);		


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN000014_CTR00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00014_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00014_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00014_CTP00001.get(1).put("AUT_USER","55300745");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00014_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00014_CTP00001.get(1).put("AUT_CODIGO_ITEM","89407066");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00014_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00014_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00014_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00014_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00014_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00014_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00014_CTP00001);



			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTP00001.get(1).put("AUT_USER", "55000035");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTP00001.get(1).put("AUT_CODIGO_ITEM","89407066");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTP00001);





			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00012_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componentes de Pedidos do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00012_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00012_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00012_CTP00001.get(1).put("AUT_USER", "55300745"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00012_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00012_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00012_CTP00001.get(1).put("AUT_CODIGO_ITEM", "89407066"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00012_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00012_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.REAIS);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00012_CTP00001.get(1).put("AUT_DESCONTO", "20,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00012_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00012_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00012_CTP00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00012_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00012_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00012_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00012_CTP00001);




			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00015_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componentes de Pedidos do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00015_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00015_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00015_CTP00001.get(1).put("AUT_USER", "55300745"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00015_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00015_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00015_CTP00001.get(1).put("AUT_CODIGO_ITEM", "89407066"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00015_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00015_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.REAIS);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00015_CTP00001.get(1).put("AUT_DESCONTO", "700,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00015_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ESPECIAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00015_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00015_CTP00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00015_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00015_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00015_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00015_CTP00001);


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00017_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componentes de Pedidos do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00017_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00017_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00017_CTP00001.get(1).put("AUT_USER", "55300745"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00017_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00017_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00017_CTP00001.get(1).put("AUT_CODIGO_ITEM", "89407066"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00017_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00017_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00017_CTP00001.get(1).put("AUT_DESCONTO", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00017_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00017_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00017_CTP00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00017_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00017_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00017_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00017_CTP00001);


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00018_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componentes de Pedidos do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00018_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00018_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00018_CTP00001.get(1).put("AUT_USER", "55300745"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00018_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00018_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00018_CTP00001.get(1).put("AUT_CODIGO_ITEM", "89407066"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00018_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00018_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00018_CTP00001.get(1).put("AUT_DESCONTO", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00018_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00018_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00018_CTP00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00018_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00018_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00018_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00018_CTP00001);


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00019_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componentes de Pedidos do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00019_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00019_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00019_CTP00001.get(1).put("AUT_USER", "55300745"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00019_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00019_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00019_CTP00001.get(1).put("AUT_CODIGO_ITEM", "89407066"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00019_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00019_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00019_CTP00001.get(1).put("AUT_DESCONTO", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00019_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.SALDO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00019_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00019_CTP00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00019_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00019_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00019_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00019_CTP00001);



			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componentes de Pedidos do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.get(1).put("AUT_USER", "55300745"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.get(1).put("AUT_CODIGO_ITEM", "89407066"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.REAIS);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.get(1).put("AUT_DESCONTO", "20");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001);


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componentes de Pedidos do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_USER", "55300745"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_CODIGO_ITEM", "89407066"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_DESCONTO", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001);


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componentes de Pedidos do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_USER", "55300745"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_CODIGO_ITEM", "89407066"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.REAIS);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_DESCONTO", "20");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001);


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00023_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componentes de Pedidos do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00023_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00023_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00023_CTP00001.get(1).put("AUT_USER", "55300745"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00023_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00023_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00023_CTP00001.get(1).put("AUT_CODIGO_ITEM", "89407066"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00023_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00023_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00023_CTP00001.get(1).put("AUT_DESCONTO", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00023_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00023_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00023_CTP00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00023_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00023_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00023_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00023_CTP00001);



			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00024_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componentes de Pedidos do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00024_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00024_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00024_CTP00001.get(1).put("AUT_USER", "55300745"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00024_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00024_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00024_CTP00001.get(1).put("AUT_CODIGO_ITEM", "89407066"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00024_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00024_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.REAIS);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00024_CTP00001.get(1).put("AUT_DESCONTO", "20");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00024_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00024_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00024_CTP00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00024_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00024_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00024_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00024_CTP00001);


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00025_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componentes de Pedidos do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00025_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00025_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00025_CTP00001.get(1).put("AUT_USER", "55300745"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00025_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00025_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00025_CTP00001.get(1).put("AUT_CODIGO_ITEM", "89407066"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00025_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00025_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00025_CTP00001.get(1).put("AUT_DESCONTO", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00025_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00025_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00025_CTP00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00025_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00025_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00025_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00025_CTP00001);


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00026_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componentes de Pedidos do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00026_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00026_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00026_CTP00001.get(1).put("AUT_USER", "55300745"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00026_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00026_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00026_CTP00001.get(1).put("AUT_CODIGO_ITEM", "89407066"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00026_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00026_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.REAIS);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00026_CTP00001.get(1).put("AUT_DESCONTO", "20");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00026_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00026_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00026_CTP00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00026_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00026_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00026_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00026_CTP00001);


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00027_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componentes de Pedidos do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00027_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00027_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00027_CTP00001.get(1).put("AUT_USER", "55300745"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00027_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00027_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00027_CTP00001.get(1).put("AUT_CODIGO_ITEM", "89407066"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00027_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00027_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00027_CTP00001.get(1).put("AUT_DESCONTO", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00027_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00027_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00027_CTP00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00027_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00027_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00027_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00027_CTP00001);


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00028_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componentes de Pedidos do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00028_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00028_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00028_CTP00001.get(1).put("AUT_USER", "55300745"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00028_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00028_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00028_CTP00001.get(1).put("AUT_CODIGO_ITEM", "89407066"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00028_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00028_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00028_CTP00001.get(1).put("AUT_DESCONTO", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00028_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00028_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00028_CTP00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00028_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00028_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00028_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00028_CTP00001);


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00029_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componentes de Pedidos do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00029_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00029_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00029_CTP00001.get(1).put("AUT_USER", "55300745"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00029_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00029_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00029_CTP00001.get(1).put("AUT_CODIGO_ITEM", "89407066"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00029_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00029_CTP00001.get(1).put("AUT_TIPO_DESCONTO", AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00029_CTP00001.get(1).put("AUT_DESCONTO", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00029_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00029_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00029_CTP00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00029_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00029_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00029_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00029_CTP00001);






			//---------------------------------------------------------------------------------------------------------				


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00030_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componentes de Pedidos do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00030_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00030_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00030_CTP00001.get(1).put("AUT_USER", "55300745"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00030_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00030_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00030_CTP00001.get(1).put("AUT_CODIGO_ITEM", "89407066"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00030_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00030_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00030_CTP00001.get(1).put("AUT_DESCONTO", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00030_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00030_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00030_CTP00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00030_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00030_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00030_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00030_CTP00001);

			//---------------------------------------------------------------------------------------------------------				


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00032_CTP00001
			/**
			 * 
			 * 			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00032_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00032_CTP00001.put(1, new java.util.HashMap<String,Object>());			

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00032_CTP00001.get(1).put("AUT_USER", "55300745");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00032_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00032_CTP00001.get(1).put("AUT_CODIGO_ITEM","89407066");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00032_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00032_CTP00001.get(1).put("AUT_DESCONTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00032_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00032_CTP00001.get(1).put("AUT_DESCONTO", "8");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00032_CTP00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ESPECIAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00032_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00032_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00032_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00032_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);			

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00032_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00032_CTP00001);

			//---------------------------------------------------------------------------------------------------------				


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00034_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00034_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00034_CTP00001.put(1, new java.util.HashMap<String,Object>());			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00034_CTP00001.get(1).put("AUT_USER", "55300745");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00034_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00034_CTP00001.get(1).put("AUT_CODIGO_ITEM","89407066");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00034_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00034_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00034_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00034_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00034_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VISA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00034_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00034_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_MOTIVO_DESCONTO_FRETE.ATRASO_NA_ENTREGA_DE_PEDIDO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00034_CTP00001.get(1).put("AUT_TIPO_DESCONTO","PORCENTAGEM");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00034_CTP00001.get(1).put("AUT_DESCONTO","10");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00034_CTP00001.get(1).put("AUT_NUMERO_CARTAO", "5300332890376075");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00034_CTP00001.get(1).put("AUT_NOME_TITULAR", "UsuÃ¡rio AutomaÃ§Ã£o");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00034_CTP00001.get(1).put("AUT_VALIDADE", "09/18");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00034_CTP00001.get(1).put("AUT_CODIGO_CARTAO", "1234");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00034_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00034_CTP00001);

			//---------------------------------------------------------------------------------------------------------				

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00045_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componentes de Pedidos do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00045_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00045_CTP00001.put(1, new java.util.HashMap<String,Object>());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00045_CTP00001.get(1).put("AUT_USER", "55000035"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00045_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00045_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00045_CTP00001.get(1).put("AUT_CODIGO_ITEM", "89296186"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00045_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00045_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00045_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00045_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_2X);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00045_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00045_CTP00001);

			//---------------------------------------------------------------------------------------------------------				


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00051_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componentes de Pedidos do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00051_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00051_CTP00001.put(1, new java.util.HashMap<String,Object>());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00051_CTP00001.get(1).put("AUT_USER", "55300745"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00051_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00051_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00051_CTP00001.get(1).put("AUT_CODIGO_ITEM", " 89388684"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00051_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00051_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00051_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00051_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00051_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00051_CTP00001);

			//---------------------------------------------------------------------------------------------------------				

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00031_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00031_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00031_CTP00001.put(1, new java.util.HashMap<String,Object>());			

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00031_CTP00001.get(1).put("AUT_USER", "55300745");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00031_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00031_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00031_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00031_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00031_CTP00001.get(1).put("AUT_DESCONTO", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00031_CTP00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00031_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00031_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00031_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00031_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00031_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00031_CTP00001);

			//---------------------------------------------------------------------------------------------------------

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00033_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00033_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00033_CTP00001.put(1, new java.util.HashMap<String,Object>());			

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00033_CTP00001.get(1).put("AUT_USER", "55300745");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00033_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00033_CTP00001.get(1).put("AUT_CODIGO_ITEM","89296193");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00033_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00033_CTP00001.get(1).put("AUT_DESCONTO", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00033_CTP00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00033_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00033_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00033_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00033_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00033_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00033_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00033_CTP00001);

			//---------------------------------------------------------------------------------------------------------				
			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00035_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00035_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00035_CTP00001.put(1, new java.util.HashMap<String,Object>());			

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00035_CTP00001.get(1).put("AUT_USER", "55300745");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00035_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00035_CTP00001.get(1).put("AUT_CODIGO_ITEM","89296193");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00035_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00035_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.REAIS);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00035_CTP00001.get(1).put("AUT_DESCONTO", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00035_CTP00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00035_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00035_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00035_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00035_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00035_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00035_CTP00001);

			//---------------------------------------------------------------------------------------------------------				

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00036_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00036_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00036_CTP00001.put(1, new java.util.HashMap<String,Object>());			

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00036_CTP00001.get(1).put("AUT_USER", "55001063");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00036_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00036_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00036_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00036_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00036_CTP00001.get(1).put("AUT_DESCONTO", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00036_CTP00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00036_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00036_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00036_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00036_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);


			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00036_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00036_CTP00001);

			//---------------------------------------------------------------------------------------------------------				

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00038_CTP00001
			/**
			 * 
			 * 
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00038_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00038_CTP00001.put(1, new java.util.HashMap<String,Object>());			

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00038_CTP00001.get(1).put("AUT_USER", "55000045");  //55000035
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00038_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00038_CTP00001.get(1).put("AUT_CODIGO_ITEM","88334862");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00038_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //42942711833
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00038_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00038_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00038_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00038_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00038_CTP00001.get(1).put("AUT_TELEFONE_SERVIÃ‡O", "1187272345");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00038_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00038_CTP00001);

			//---------------------------------------------------------------------------------------------------------				

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN000040_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00040_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00040_CTP00001.put(1, new java.util.HashMap<String,Object>());			

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00040_CTP00001.get(1).put("AUT_USER", "55000035"); //51013346  55000035
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00040_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00040_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163"); //89391260 89576445
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00040_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");  //  4294271183    
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00040_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00040_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00040_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00040_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00040_CTP00001.get(1).put("AUT_TELEFONE_SERVIÃ‡O", "1187272345");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00040_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00040_CTP00001);		

			//----------------------------------------------------------------------------------------

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00041_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00041_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00041_CTP00001.put(1, new java.util.HashMap<String,Object>());			

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00041_CTP00001.get(1).put("AUT_USER", "55000045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00041_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00041_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00041_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00041_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00041_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00041_CTP00001.get(1).put("AUT_TIPO_FRETE", AUTFluxoSaida.AUT_VA_TIP_FRETE.EXPRESSA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00041_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00041_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X);


			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00041_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00041_CTP00001);

			//---------------------------------------------------------------------------------------------------------				

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00042_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00042_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00042_CTP00001.put(1, new java.util.HashMap<String,Object>());			

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00042_CTP00001.get(1).put("AUT_USER", "55000045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00042_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00042_CTP00001.get(1).put("AUT_CODIGO_ITEM","89296193");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00042_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00042_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00042_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00042_CTP00001.get(1).put("AUT_TIPO_FRETE", AUTFluxoSaida.AUT_VA_TIP_FRETE.ECONOMICA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00042_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00042_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X);


			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00042_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00042_CTP00001);

			//---------------------------------------------------------------------------------------------------------				



			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00043_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00043_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00043_CTP00001.put(1, new java.util.HashMap<String,Object>());			

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00043_CTP00001.get(1).put("AUT_USER", "55000035");  //55000035
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00043_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00043_CTP00001.get(1).put("AUT_CODIGO_ITEM","89296193");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00043_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //42942711833
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00043_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00043_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00043_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00043_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X);


			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00043_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00043_CTP00001);

			//---------------------------------------------------------------------------------------------------------				

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00044_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00044_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00044_CTP00001.put(1, new java.util.HashMap<String,Object>());			

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00044_CTP00001.get(1).put("AUT_USER", "55000035");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00044_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00044_CTP00001.get(1).put("AUT_CODIGO_ITEM","89296193");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00044_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00044_CTP00001.get(1).put("AUT_DESCONTO", "80,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00044_CTP00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00044_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00044_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00044_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00044_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X);


			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00044_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00044_CTP00001);

			//---------------------------------------------------------------------------------------------------------				


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00046_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00046_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00046_CTP00001.put(1, new java.util.HashMap<String,Object>());			

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00046_CTP00001.get(1).put("AUT_USER", "55001063");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00046_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00046_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00046_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00046_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00046_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00046_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VISA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00046_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00046_CTP00001.get(1).put("AUT_NUMERO_CARTAO", "5300332890376075");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00046_CTP00001.get(1).put("AUT_NOME_TITULAR", "UsuÃ¡rio AutomaÃ§Ã£o");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00046_CTP00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00046_CTP00001.get(1).put("AUT_CODIGO_CARTAO", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00046_CTP00001.get(1).put("AUT_CODE_VALE_TROCA","00620723");			

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00046_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00046_CTP00001);

			//---------------------------------------------------------------------------------------------------------	

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00047_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00047_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00047_CTP00001.put(1, new java.util.HashMap<String,Object>());			

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00047_CTP00001.get(1).put("AUT_USER", "55001063");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00047_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00047_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00047_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00047_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00047_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00047_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.MASTERCARD);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00047_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00047_CTP00001.get(1).put("AUT_NUMERO_CARTAO", "5300332890376075");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00047_CTP00001.get(1).put("AUT_NOME_TITULAR", "UsuÃ¡rio AutomaÃ§Ã£o");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00047_CTP00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00047_CTP00001.get(1).put("AUT_CODIGO_CARTAO", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00047_CTP00001.get(1).put("AUT_CODE_VALE_TROCA","00620723");			

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00047_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00047_CTP00001);	

			//---------------------------------------------------------------------------------------------------------	

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00048_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00048_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00048_CTP00001.put(1, new java.util.HashMap<String,Object>());			

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00048_CTP00001.get(1).put("AUT_USER", "55000035");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00048_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00048_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00048_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00048_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00048_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00048_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CHEQUE);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00048_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00048_CTP00001.get(1).put("AUT_NUMERO_CARTAO", "5300332890376075");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00048_CTP00001.get(1).put("AUT_NOME_TITULAR", "UsuÃ¡rio AutomaÃ§Ã£o");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00048_CTP00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00048_CTP00001.get(1).put("AUT_CODIGO_CARTAO", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00048_CTP00001.get(1).put("AUT_CODE_VALE_TROCA","00620723");			

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00048_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00048_CTP00001);	

			//---------------------------------------------------------------------------------------------------------	

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00049_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componentes de Pedidos do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00049_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00049_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00049_CTP00001.get(1).put("AUT_USER", "55001063"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00049_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00049_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00049_CTP00001.get(1).put("AUT_CODIGO_ITEM", "89296193"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00049_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00049_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00049_CTP00001.get(1).put("AUT_DESCONTO", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00049_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00049_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00049_CTP00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00049_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VALE_TROCA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00049_CTP00001.get(1).put("AUT_CODE_VALE_TROCA","00620723");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00049_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00049_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00049_CTP00001);

			//---------------------------------------------------------------------------------------------------------				

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00050_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componentes de Pedidos do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00050_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00050_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00050_CTP00001.get(1).put("AUT_USER", "55001063"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00050_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00050_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00050_CTP00001.get(1).put("AUT_CODIGO_ITEM", "89296193"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00050_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00050_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00050_CTP00001.get(1).put("AUT_DESCONTO", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00050_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00050_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00050_CTP00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00050_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VOUCHER);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00050_CTP00001.get(1).put("AUT_CODE_VOUCHER","6394422000000293143");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00050_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00050_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00050_CTP00001);

			//---------------------------------------------------------------------------------------------------------				


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00052_CTP00001
			/**
			 * 
			 * 
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00052_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00052_CTP00001.put(1, new java.util.HashMap<String,Object>());			

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00052_CTP00001.get(1).put("AUT_USER", "55000045");  //55000035
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00052_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00052_CTP00001.get(1).put("AUT_CODIGO_ITEM","88334862");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00052_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM_VA", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00052_CTP00001.get(1).put("AUT_CODIGO_ITEM_VA", "89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00052_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //42942711833
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00052_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00052_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00052_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VISA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00052_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00052_CTP00001.get(1).put("AUT_NUMERO_CARTAO", "5300332890376075");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00052_CTP00001.get(1).put("AUT_NOME_TITULAR", "UsuÃ¡rio AutomaÃ§Ã£o");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00052_CTP00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00052_CTP00001.get(1).put("AUT_CODIGO_CARTAO", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00052_CTP00001.get(1).put("AUT_CODE_VALE_TROCA","00620723");	

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00052_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00052_CTP00001);

			//---------------------------------------------------------------------------------------------------------				

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00053_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componentes de Pedidos do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00053_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00053_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00053_CTP00001.get(1).put("AUT_USER", "55001063 "); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00053_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00053_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00053_CTP00001.get(1).put("AUT_CODIGO_ITEM", "89407066"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00053_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00053_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00053_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VISA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00053_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00053_CTP00001.get(1).put("AUT_NUMERO_CARTAO", "5300332890376075");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00053_CTP00001.get(1).put("AUT_NOME_TITULAR", "UsuÃ¡rio AutomaÃ§Ã£o");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00053_CTP00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00053_CTP00001.get(1).put("AUT_CODIGO_CARTAO", "1234");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00053_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00053_CTP00001);

			//---------------------------------------------------------------------------------------------------------				


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00054_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00054_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00054_CTP00001.put(1, new java.util.HashMap<String,Object>());			

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00054_CTP00001.get(1).put("AUT_USER", "pedroantonioagostinho4@gmail.com");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00054_CTP00001.get(1).put("AUT_PASSWORD", "Leroy123");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00054_CTP00001.get(1).put("AUT_CODIGO_ITEM","85101184");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00054_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00054_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00054_CTP00001.get(1).put("AUT_NUMERO_CARTAO", "5300332890376075");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00054_CTP00001.get(1).put("AUT_NOME_TITULAR", "UsuÃ¡rio AutomaÃ§Ã£o");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00054_CTP00001.get(1).put("AUT_VALIDADE", "01/2019");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00054_CTP00001.get(1).put("AUT_CODIGO_CARTAO", "1234");

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00054_CTP00001.get(1).put("AUT_CEP", "05424-004");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00054_CTP00001.get(1).put("AUT_RUA_ENDERECO", "Rua ButantÃ£");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00054_CTP00001.get(1).put("AUT_NUMERO_ENDERECO", "225");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00054_CTP00001.get(1).put("AUT_BAIRRO_ENDERECO", "Pinheiros");	

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00054_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00054_CTP00001);	

			//---------------------------------------------------------------------------------------------------------	

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00055_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00055_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00055_CTP00001.put(1, new java.util.HashMap<String,Object>());			

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00055_CTP00001.get(1).put("AUT_USER", "pedroantonioagostinho4@gmail.com");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00055_CTP00001.get(1).put("AUT_PASSWORD", "Leroy123");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00055_CTP00001.get(1).put("AUT_CODIGO_ITEM","85101184");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00055_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00055_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00055_CTP00001.get(1).put("AUT_NUMERO_CARTAO", "5300332890376075");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00055_CTP00001.get(1).put("AUT_NOME_TITULAR", "UsuÃ¡rio AutomaÃ§Ã£o");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00055_CTP00001.get(1).put("AUT_VALIDADE", "01/2019");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00055_CTP00001.get(1).put("AUT_CODIGO_CARTAO", "1234");

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00055_CTP00001.get(1).put("AUT_CEP", "05424-004");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00055_CTP00001.get(1).put("AUT_RUA_ENDERECO", "Rua ButantÃ£");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00055_CTP00001.get(1).put("AUT_NUMERO_ENDERECO", "225");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00055_CTP00001.get(1).put("AUT_BAIRRO_ENDERECO", "Pinheiros");	

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00055_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00055_CTP00001);	

			//---------------------------------------------------------------------------------------------------------	


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN000056_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.put(1, new java.util.HashMap<String,Object>());			

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_USER", "55000045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_CODIGO_ITEM_VA","89296193");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "18.783.107/6918-63");  //  4294271183
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_TELEFONE_SERVIÃ‡O", "1187272345");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_CPF", AUTProjectsFunctions.gerarCPF());

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_TIPO_CADASTRO", AUT_VA_CADASTROS.JURIDICA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_CPF", cpf);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_NOME", "AUT NOME PF: ".concat(cpf));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_NOME_COMPLETO", "AUT NOME COMPLETO: ".concat(AUTProjectsFunctions.gerarCPF()));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_NOME_PJ", "AUT NOME PJ: ".concat(cnpj));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_EMAIL", "aut.qaemail@automation.com");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_INCRICAO_ESTADUAL", "474.018.412.567");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_INCRICAO_ESTADUAL_PF", AUTProjectsFunctions.gerarEstrangeiro());	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_INCRICAO_MUNICIPAL", AUTProjectsFunctions.gerarEstrangeiro());	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_NOME_PJ_CONTATO", "NOME CONTATO PJ".concat(AUTProjectsFunctions.gerarCPF()));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_PJ_EMAIL_CONTATO", "EMAIL.QA@TESTE.COM.BR".concat(AUTProjectsFunctions.gerarCPF()));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_PJ_DEPARTAMENTO_CONTATO", "QUALIDADE".concat(AUTProjectsFunctions.gerarCPF()));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_TIPO_TELEFONE", AUT_VA_TIPO_CONTATO.CELULAR.name());	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_NUMERO_TELEFONE", "11966447035");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_NUMERO_TELEFONE_2", "1196306-3067");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_UF_PESQUISA", AUT_VA_ESTADOS.SP);	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_CIDADE_PESQUISA", "CAJAMAR");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_ENDERECO_PESQUISA", "RUA PREFEITO ANTONIO GARRIDO");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_BAIRRO_PESQUISA", "JORDANÃ‰SIA");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_TIPO_ENDERECO", AUT_VA_TIPO_ENDERECO.OBRA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_CEP", "07776-000");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_RUA_ENDERECO", "Rua Explanada");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_NUMERO_ENDERECO", "256");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_BAIRRO_ENDERECO", "EXPLANADA");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_COMPLEMENTO_ENDERECO", "CASA 123");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_CIDADE_ENDERECO", "BOCAIÃšVA");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_ESTADO_ENDERECO", AUT_VA_ESTADOS.MG);	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_REFERENCIA_ENDERECO", "ACOUGUE DA ESQUINA");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_TIPO_IMOVEL_RESIDENCIA", AUT_VA_TIPO_RESIDENCIA.RURAL_CHACARA_FAZENDA_OU_SITIO);	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_NASCIMENTO", "26/11/1994");			

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001);		


			//---------------------------------------------------------------------------------------------------------				

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN000057_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.put(1, new java.util.HashMap<String,Object>());			

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_USER", "55000045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_CODIGO_ITEM_VA","89296193");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "18.783.107/6918-63");  //  4294271183
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_TELEFONE_SERVIÃ‡O", "1187272345");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_CPF", AUTProjectsFunctions.gerarCPF());

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_TIPO_CADASTRO", AUT_VA_CADASTROS.JURIDICA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_CPF", cpf);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_NOME", "AUT NOME PF: ".concat(cpf));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_NOME_COMPLETO", "AUT NOME COMPLETO: ".concat(AUTProjectsFunctions.gerarCPF()));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_NOME_PJ", "AUT NOME PJ: ".concat(cnpj));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_EMAIL", "aut.qaemail@automation.com");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_INCRICAO_ESTADUAL", "474.018.412.567");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_INCRICAO_ESTADUAL_PF", AUTProjectsFunctions.gerarEstrangeiro());	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_INCRICAO_MUNICIPAL", AUTProjectsFunctions.gerarEstrangeiro());	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_NOME_PJ_CONTATO", "NOME CONTATO PJ".concat(AUTProjectsFunctions.gerarCPF()));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_PJ_EMAIL_CONTATO", "EMAIL.QA@TESTE.COM.BR".concat(AUTProjectsFunctions.gerarCNPJ()));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_PJ_DEPARTAMENTO_CONTATO", "QUALIDADE".concat(AUTProjectsFunctions.gerarCPF()));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_TIPO_TELEFONE", AUT_VA_TIPO_CONTATO.CELULAR.name());	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_NUMERO_TELEFONE", "11966447035");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_NUMERO_TELEFONE_2", "1196306-3067");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_UF_PESQUISA", AUT_VA_ESTADOS.SP);	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_CIDADE_PESQUISA", "CAJAMAR");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_ENDERECO_PESQUISA", "RUA PREFEITO ANTONIO GARRIDO");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_BAIRRO_PESQUISA", "JORDANÃ‰SIA");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_TIPO_ENDERECO", AUT_VA_TIPO_ENDERECO.OBRA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_CEP", "07776-000");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_RUA_ENDERECO", "Rua Explanada");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_NUMERO_ENDERECO", "256");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_BAIRRO_ENDERECO", "EXPLANADA");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_COMPLEMENTO_ENDERECO", "CASA 123");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_CIDADE_ENDERECO", "BOCAIÃšVA");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_ESTADO_ENDERECO", AUT_VA_ESTADOS.MG);	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_REFERENCIA_ENDERECO", "ACOUGUE DA ESQUINA");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_TIPO_IMOVEL_RESIDENCIA", AUT_VA_TIPO_RESIDENCIA.RURAL_CHACARA_FAZENDA_OU_SITIO);	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_NASCIMENTO", "26/11/1994");			

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001);		

			//---------------------------------------------------------------------------------------------------------				


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN000058_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.put(1, new java.util.HashMap<String,Object>());			

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_USER", "55000045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_CODIGO_ITEM_VA","89296193");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "18.783.107/6918-63");  //  4294271183
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_CPF_ESTRANGEIRO", cpfEstrangeiro);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_NOME_ESTRANGEIRO", String.format("AUT NOME ESTRANG: %s : CPF EST: %s",codEstrangeiro,cpfEstrangeiro));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_NOME", "AUT NOME PF: ".concat(cpf));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_NOME_COMPLETO", "AUT NOME COMPLETO: ".concat(AUTProjectsFunctions.gerarCPF()));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_NOME_PJ", "AUT NOME PJ: ".concat(cnpj));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_NOME_PJ_FANTASIA", "AUT NOME PJ:".concat(AUTProjectsFunctions.gerarEstrangeiro()));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_NOME_PJ_FANTASIA2", "AUT NOME PJ 2:".concat(AUTProjectsFunctions.gerarEstrangeiro()));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_EMAIL", "aut.qaemail@automation.com");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_INCRICAO_ESTADUAL", "474.018.412.567");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_INCRICAO_ESTADUAL_PF", AUTProjectsFunctions.gerarEstrangeiro());	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_INCRICAO_MUNICIPAL", AUTProjectsFunctions.gerarEstrangeiro());	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_NOME_PJ_CONTATO", "NOME CONTATO PJ".concat(AUTProjectsFunctions.gerarCPF()));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_PJ_EMAIL_CONTATO", "EMAIL.QA@TESTE.COM.BR".concat(AUTProjectsFunctions.gerarCPF()));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_PJ_DEPARTAMENTO_CONTATO", "QUALIDADE".concat(AUTProjectsFunctions.gerarCPF()));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_TIPO_TELEFONE", AUT_VA_TIPO_CONTATO.CELULAR.name());	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_NUMERO_TELEFONE", "11966447035");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_NUMERO_TELEFONE_2", "1196306-3067");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_UF_PESQUISA", AUT_VA_ESTADOS.SP);	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_CIDADE_PESQUISA", "CAJAMAR");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_ENDERECO_PESQUISA", "RUA PREFEITO ANTONIO GARRIDO");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_BAIRRO_PESQUISA", "JORDANÃ‰SIA");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_TIPO_ENDERECO", AUT_VA_TIPO_ENDERECO.OBRA);

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_CEP", "07776-000");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_RUA_ENDERECO", "Rua Explanada");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_NUMERO_ENDERECO", "256");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_BAIRRO_ENDERECO", "EXPLANADA");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_COMPLEMENTO_ENDERECO", "CASA 123");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_CIDADE_ENDERECO", "BOCAIÃšVA");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_ESTADO_ENDERECO", AUT_VA_ESTADOS.MG);	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_REFERENCIA_ENDERECO", "ACOUGUE DA ESQUINA");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_TIPO_IMOVEL_RESIDENCIA", AUT_VA_TIPO_RESIDENCIA.RURAL_CHACARA_FAZENDA_OU_SITIO);	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_CEP_2", "06013-006");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_RUA_ENDERECO_2", "Rua AntÃ´nio AgÃº");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_NUMERO_ENDERECO_2", "256");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_BAIRRO_ENDERECO_2", "Centro");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_COMPLEMENTO_ENDERECO_2", "CASA 123");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_CIDADE_ENDERECO_2", "Osasco");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_ESTADO_ENDERECO_2", AUT_VA_ESTADOS.SP);	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_REFERENCIA_ENDERECO_2", "Loja Sabaroa");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_TIPO_IMOVEL_RESIDENCIA_2", AUT_VA_TIPO_RESIDENCIA.LOJA_OU_SOBRELOJA);	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_CNPJ_INVALIDO", "37.764.388/0009-90");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_CPF_INVALIDO", "510.354.523-93");	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_CEP_INVALIDO", "12345678");

			//---------------------------------------------------------------------------------------------------------				


			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001);		


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00059_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00059_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00059_CTP00001.put(1, new java.util.HashMap<String,Object>());			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00059_CTP00001.get(1).put("AUT_USER", "55000035");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00059_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00059_CTP00001.get(1).put("AUT_CODIGO_ITEM","89407066");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00059_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00059_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00059_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00059_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00059_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00059_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VISA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00059_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00059_CTP00001.get(1).put("AUT_NUMERO_CARTAO", "5300332890376075");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00059_CTP00001.get(1).put("AUT_NOME_TITULAR", "UsuÃ¡rio AutomaÃ§Ã£o");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00059_CTP00001.get(1).put("AUT_VALIDADE", "09/18");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00059_CTP00001.get(1).put("AUT_CODIGO_CARTAO", "1234");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00059_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00059_CTP00001);

			//---------------------------------------------------------------------------------------------------------				

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00050_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componentes de Pedidos do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00060_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00060_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00060_CTP00001.put(1, new java.util.HashMap<String,Object>());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00060_CTP00001.get(1).put("AUT_USER", "55300745"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00060_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00060_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00060_CTP00001.get(1).put("AUT_CODIGO_ITEM", " 89388684"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00060_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00060_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00060_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00060_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00060_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00060_CTP00001);

			//---------------------------------------------------------------------------------------------------------				

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00070_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componentes de Pedidos do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00070_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00070_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00070_CTP00001.get(1).put("AUT_USER", "55300745"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00070_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00070_CTP00001.get(1).put("AUT_QUANTIDADE_ITENS", 2); 			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00070_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00070_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00070_CTP00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00070_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00070_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00070_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00070_CTP00001.get(1).put("AUT_FILIAL_ESTOQUE","0035 - LOJA CURITIBA ATUBA");


			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00070_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00070_CTP00001);

			//---------------------------------------------------------------------------------------------------------				

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00071_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componentes de Pedidos do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00071_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00071_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00071_CTP00001.get(1).put("AUT_USER", "55300745"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00071_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00071_CTP00001.get(1).put("AUT_QUANTIDADE_ITENS", 2); 			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00071_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00071_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00071_CTP00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00071_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00071_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00071_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00071_CTP00001.get(1).put("AUT_FILIAL_ESTOQUE","0035 - LOJA CURITIBA ATUBA");


			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00071_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00071_CTP00001);

			//---------------------------------------------------------------------------------------------------------				

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00072_CTR00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00072_CTR00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00072_CTR00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00072_CTR00001.get(1).put("AUT_USER", "55000045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00072_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00072_CTR00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00072_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00072_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00072_CTR00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00072_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00072_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA.toString());

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00072_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00072_CTR00001);

			//---------------------------------------------------------------------------------------------------------				

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00073_CTR00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00073_CTR00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00073_CTR00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00073_CTR00001.get(1).put("AUT_USER", "55000045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00073_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00073_CTR00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00073_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00073_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00073_CTR00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00073_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00073_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA.toString());

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00073_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00073_CTR00001);

			//---------------------------------------------------------------------------------------------------------				


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00071_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componentes de Pedidos do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00074_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00074_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00074_CTP00001.get(1).put("AUT_USER", "55300745"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00074_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00074_CTP00001.get(1).put("AUT_QUANTIDADE_ITENS", 1); 			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00074_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00074_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00074_CTP00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00074_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00074_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00074_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00074_CTP00001.get(1).put("AUT_FILIAL_ESTOQUE","0035 - LOJA CURITIBA ATUBA");


			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00074_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00074_CTP00001);

			//---------------------------------------------------------------------------------------------------------				

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00075_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00075_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00075_CTP00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00075_CTP00001.get(1).put("AUT_USER", "55001063");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00075_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00075_CTP00001.get(1).put("AUT_CODIGO_ITEM","86607542");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00075_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00075_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00075_CTP00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00075_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VISA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00075_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00075_CTP00001.get(1).put("AUT_NUMERO_CARTAO", "4000 0000 0000 0044"); //5300332890376075
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00075_CTP00001.get(1).put("AUT_NOME_TITULAR", "UsuÃ¡rio AutomaÃ§Ã£o");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00075_CTP00001.get(1).put("AUT_VALIDADE", "01/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00075_CTP00001.get(1).put("AUT_CODIGO_CARTAO", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00075_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO_2",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VOUCHER);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00075_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO_2",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00075_CTP00001.get(1).put("AUT_CODE_VOUCHER",".*\\d{19}");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00075_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00075_CTP00001);

			//---------------------------------------------------------------------------------------------------------				


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00076_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00076_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00076_CTP00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00076_CTP00001.get(1).put("AUT_USER", "55001063");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00076_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00076_CTP00001.get(1).put("AUT_CODIGO_ITEM","86607542");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00076_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00076_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00076_CTP00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00076_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VOUCHER);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00076_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00076_CTP00001.get(1).put("AUT_CODE_VOUCHER",".*\\d{19}");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00076_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO_2",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VALE_TROCA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00076_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO_2",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00076_CTP00001.get(1).put("AUT_CODE_VALE_TROCA",".*\\d{8}");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00076_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00076_CTP00001);

			//---------------------------------------------------------------------------------------------------------				

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTR00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTR00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTR00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTR00001.get(1).put("AUT_USER", "55000045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTR00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTR00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X.toString());

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTR00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTR00001);

			// -------------------------------------------------------------------------

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTP00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTP00001.get(1).put("AUT_USER", "55000045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTP00001.get(1).put("AUT_NUMERO_PEDIDO", "929292929");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTP00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO_2",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO_2",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","NA");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTP00001.get(1).put("AUT_STATUS_PEDIDO",AUT_STATUS_PESQUISA.ELIMINADO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTP00001.get(1).put("AUT_CONDICOES_PEDIDO",AUT_MANTER_CONDICOES.SIM);			

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTP00001);

			//---------------------------------------------------------------------------------------------------------				

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00081_CTR00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00081_CTR00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00081_CTR00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00081_CTR00001.get(1).put("AUT_USER", "55000045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00081_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00081_CTR00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00081_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00081_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00081_CTR00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00081_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00081_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X.toString());

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00081_CTR00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00081_CTR00001);

			// -------------------------------------------------------------------------

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00081_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00081_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00081_CTP00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00081_CTP00001.get(1).put("AUT_USER", "55000045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00081_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00081_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00081_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00081_CTP00001.get(1).put("AUT_NUMERO_PEDIDO", "929292929");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00081_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00081_CTP00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00081_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00081_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00081_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO_2",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00081_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO_2",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00081_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","NA");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00081_CTP00001.get(1).put("AUT_STATUS_PEDIDO",AUT_STATUS_PESQUISA.ELIMINADO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00081_CTP00001.get(1).put("AUT_CONDICOES_PEDIDO",AUT_MANTER_CONDICOES.SIM);			

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00081_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00081_CTP00001);

			//---------------------------------------------------------------------------------------------------------				

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTR00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTR00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTR00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTR00001.get(1).put("AUT_USER", "55000045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTR00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTR00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X.toString());

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTR00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTR00001);

			//---------------------------------------------------------------------------------------------------------				


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTP00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTP00001.get(1).put("AUT_USER", "55000045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTP00001.get(1).put("AUT_NUMERO_PEDIDO", "929292929");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTP00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO_2",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO_2",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","NA");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTP00001.get(1).put("AUT_STATUS_PEDIDO",AUT_STATUS_PESQUISA.CANCELADO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTP00001.get(1).put("AUT_CONDICOES_PEDIDO",AUT_MANTER_CONDICOES.SIM);			

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTP00001);

			//---------------------------------------------------------------------------------------------------------				


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTR00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTR00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTR00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTR00001.get(1).put("AUT_USER", "55000045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTR00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTR00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X.toString());

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTR00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTR00001);

			//---------------------------------------------------------------------------------------------------------				

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTP00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTP00001.get(1).put("AUT_USER", "55000045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTP00001.get(1).put("AUT_NUMERO_PEDIDO", "929292929");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTP00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO_2",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO_2",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","NA");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTP00001.get(1).put("AUT_STATUS_PEDIDO",AUT_STATUS_PESQUISA.CANCELADO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTP00001.get(1).put("AUT_CONDICOES_PEDIDO",AUT_MANTER_CONDICOES.SIM);			

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTP00001);

			//---------------------------------------------------------------------------------------------------------				


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00084_CTR00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00084_CTR00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00084_CTR00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00084_CTR00001.get(1).put("AUT_USER", "55000045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00084_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00084_CTR00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00084_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00084_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00084_CTR00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00084_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00084_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X.toString());

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00084_CTR00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00084_CTR00001);

			//---------------------------------------------------------------------------------------------------------				

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00084_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00084_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00084_CTP00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00084_CTP00001.get(1).put("AUT_USER", "55000045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00084_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00084_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00084_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00084_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00084_CTP00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00084_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00084_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00084_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","NA");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00084_CTP00001.get(1).put("AUT_STATUS_PEDIDO",AUT_STATUS_PESQUISA.DEVOLVIDO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00084_CTP00001.get(1).put("AUT_CONDICOES_PEDIDO",AUT_MANTER_CONDICOES.SIM);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00084_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","NA");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00084_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00084_CTP00001);

			// -------------------------------------------------------------------------

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTR00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTR00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTR00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTR00001.get(1).put("AUT_USER", "55000045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTR00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTR00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X.toString());

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTR00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTR00001);

			// -------------------------------------------------------------------------


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTP00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTP00001.get(1).put("AUT_USER", "55000045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTP00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTP00001.get(1).put("AUT_STATUS_PEDIDO",AUT_STATUS_PESQUISA.DEVOLVIDO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTP00001.get(1).put("AUT_CONDICOES_PEDIDO",AUT_MANTER_CONDICOES.SIM);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","NA");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTP00001);

			//---------------------------------------------------------------------------------------------------------				

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00086_CTR00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00086_CTR00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00086_CTR00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00086_CTR00001.get(1).put("AUT_USER", "55000045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00086_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00086_CTR00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00086_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00086_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00086_CTR00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00086_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00086_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00086_CTR00001.get(1).put("AUT_NUMERO_PEDIDO","");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00086_CTR00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00086_CTR00001);

			// -------------------------------------------------------------------------


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00086_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00086_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00086_CTP00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00086_CTP00001.get(1).put("AUT_USER", "55000045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00086_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00086_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00086_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00086_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00086_CTP00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00086_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00086_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00086_CTP00001.get(1).put("AUT_CONDICOES_PEDIDO",AUT_MANTER_CONDICOES.SIM);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00086_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00086_CTP00001.get(1).put("AUT_STATUS_PEDIDO","NA");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00086_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00086_CTP00001);

			// -------------------------------------------------------------------------

			// 		----------------------- MD00002 ------------------------------




			// -------------------------------------------------------------------------

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00002_CN00001_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de ORÃ‡AMENTOS do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00001_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00001_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00001_CTP00001.get(1).put("AUT_USER", "55000035");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00001_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00001_CTP00001.get(1).put("AUT_CODIGO_ITEM","89407066");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00001_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00001_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00001_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00001_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00001_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00001_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00002_CN00001_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00001_CTP00001);

			//---------------------------------------------------------------------------------------------------------				

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00002_CN00002_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de ORÃ‡AMENTOS do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00002_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00002_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00002_CTP00001.get(1).put("AUT_USER", "55000035");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00002_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00002_CTP00001.get(1).put("AUT_CODIGO_ITEM","89407066");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00002_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00002_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00002_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00002_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00002_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00002_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00002_CN00002_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00002_CTP00001);

			//---------------------------------------------------------------------------------------------------------				


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00002_CN00003_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de ORÃ‡AMENTOS do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00003_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00003_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00003_CTP00001.get(1).put("AUT_USER", "55000035");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00003_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00003_CTP00001.get(1).put("AUT_CODIGO_ITEM","89407066");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00003_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00003_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00003_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_IMEDIATA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00003_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00003_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00003_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00002_CN00003_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00003_CTP00001);

			//---------------------------------------------------------------------------------------------------------				

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00002_CN00009_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de ORÃ‡AMENTOS do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00009_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00009_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00009_CTP00001.get(1).put("AUT_USER", "55192309");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00009_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00009_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00009_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00009_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00009_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_IMEDIATA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00009_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00009_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00009_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00009_CTP00001.get(1).put("AUT_DESCONTO", "8");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00009_CTP00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ESPECIAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00009_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00002_CN00009_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00009_CTP00001);

			//---------------------------------------------------------------------------------------------------------				

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00002_CN00010_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de ORÃ‡AMENTOS do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00010_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00010_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00010_CTP00001.get(1).put("AUT_USER", "55192309");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00010_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00010_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00010_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00010_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00010_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_IMEDIATA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00010_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00010_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00010_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00010_CTP00001.get(1).put("AUT_DESCONTO", "8");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00010_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00010_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00002_CN00010_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00010_CTP00001);

			//---------------------------------------------------------------------------------------------------------				


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00002_CN00011_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de ORÃ‡AMENTOS do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00011_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00011_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00011_CTP00001.get(1).put("AUT_USER", "55192309");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00011_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00011_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00011_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00011_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00011_CTP00001.get(1).put("AUT_QUANTIDADES_ITENS",2);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00011_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_IMEDIATA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00011_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00011_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00011_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00011_CTP00001.get(1).put("AUT_DESCONTO", "8");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00011_CTP00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ESPECIAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00011_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00011_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00002_CN00011_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00011_CTP00001);

			//---------------------------------------------------------------------------------------------------------				

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00002_CN00013_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de ORÃ‡AMENTOS do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00013_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00013_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00013_CTP00001.get(1).put("AUT_USER", "55000045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00013_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00013_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00013_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00013_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00013_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00013_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00013_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00013_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00013_CTP00001.get(1).put("AUT_DESCONTO", "8");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00013_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00013_CTP00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ESPECIAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00013_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00002_CN00013_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00013_CTP00001);

			//---------------------------------------------------------------------------------------------------------				

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00002_CN00014_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de ORÃ‡AMENTOS do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00014_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00014_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00014_CTP00001.get(1).put("AUT_USER", "55192309");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00014_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00014_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00014_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00014_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00014_CTP00001.get(1).put("AUT_QUANTIDADES_ITENS",2);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00014_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_IMEDIATA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00014_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00014_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00014_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00014_CTP00001.get(1).put("AUT_DESCONTO", "8");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00014_CTP00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ESPECIAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00014_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00014_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00002_CN00014_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00014_CTP00001);

			//---------------------------------------------------------------------------------------------------------				


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00002_CN00015_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de ORÃ‡AMENTOS do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00015_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00015_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00015_CTP00001.get(1).put("AUT_USER", "55000035");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00015_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00015_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00015_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00015_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00015_CTP00001.get(1).put("AUT_QUANTIDADES_ITENS",2);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00015_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_IMEDIATA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00015_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00015_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00015_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00015_CTP00001.get(1).put("AUT_DESCONTO", "8");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00015_CTP00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ESPECIAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00015_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00015_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00002_CN00015_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00015_CTP00001);

			//---------------------------------------------------------------------------------------------------------				


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00002_CN00016_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de ORÃ‡AMENTOS do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00016_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00016_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00016_CTP00001.get(1).put("AUT_USER", "55000035");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00016_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00016_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00016_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00016_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00016_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_IMEDIATA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00016_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00016_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00016_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00002_CN00016_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00016_CTP00001);

			//---------------------------------------------------------------------------------------------------------				


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00002_CN00018_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de ORÃ‡AMENTOS do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00018_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00018_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00018_CTP00001.get(1).put("AUT_USER", "55000035");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00018_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00018_CTP00001.get(1).put("AUT_CODIGO_ITEM","89407066");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00018_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00018_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00018_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_IMEDIATA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00018_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00018_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00018_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00018_CTP00001.get(1).put("AUT_DESCONTO", "80,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00018_CTP00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ESPECIAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00018_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00018_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			


			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00002_CN00018_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00018_CTP00001);

			//---------------------------------------------------------------------------------------------------------				


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componentes de Pedidos do sistema VA
			 * 
			 * 
			 */
			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00020_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.put(1, new java.util.HashMap<String,Object>());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.get(1).put("AUT_USER", "55300745"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.get(1).put("AUT_CODIGO_ITEM", "89407066"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.get(1).put("AUT_QUANTIDADE_ITENS", 2); 
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.get(1).put("AUT_DESCONTO", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.SALDO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.get(1).put("AUT_DESCONTO", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001);

			//---------------------------------------------------------------------------------------------------------				

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componentes de Pedidos do sistema VA
			 * 
			 * 
			 */
			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00021_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.put(1, new java.util.HashMap<String,Object>());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_USER", "55300745"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_CODIGO_ITEM", "89407066"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_QUANTIDADE_ITENS", 2); 
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_DESCONTO", "2");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.SALDO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_DESCONTO", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001);

			//---------------------------------------------------------------------------------------------------------				


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componentes de Pedidos do sistema VA
			 * 
			 * 
			 */
			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00022_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.put(1, new java.util.HashMap<String,Object>());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_USER", "55300745"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_CODIGO_ITEM", "89407066"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_QUANTIDADE_ITENS", 2); 
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.REAIS);

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_DESCONTO", "2");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.SALDO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.REAIS);

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_DESCONTO", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001);

			//---------------------------------------------------------------------------------------------------------				


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00002_CN00023_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de ORÃ‡AMENTOS do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00023_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00023_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00023_CTP00001.get(1).put("AUT_USER", "55000035");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00023_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00023_CTP00001.get(1).put("AUT_CODIGO_ITEM","89407066");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00023_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00023_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00023_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_IMEDIATA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00023_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00023_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00023_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00023_CTP00001.get(1).put("AUT_DESCONTO", "8");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00023_CTP00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ESPECIAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00023_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00023_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00002_CN00023_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00023_CTP00001);

			//---------------------------------------------------------------------------------------------------------				


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00002_CN00028_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de ORÃ‡AMENTOS do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00028_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00028_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00028_CTP00001.get(1).put("AUT_USER", "55000035");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00028_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00028_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00028_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00028_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00028_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_IMEDIATA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00028_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00028_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00028_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00028_CTP00001.get(1).put("AUT_COMENTARIO","Test cenario 28 adicionar comentÃ¡rio");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00002_CN00028_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00028_CTP00001);

			//---------------------------------------------------------------------------------------------------------				


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00002_CN00029_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de ORÃ‡AMENTOS do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00029_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00029_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00029_CTP00001.get(1).put("AUT_USER", "55000035");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00029_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00029_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00029_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00029_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00029_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_IMEDIATA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00029_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00029_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00029_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00029_CTP00001.get(1).put("AUT_COMENTARIO","Test cenario 28 adicionar comentÃ¡rio");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00002_CN00029_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00029_CTP00001);

			//---------------------------------------------------------------------------------------------------------				


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00002_CN00030_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de ORÃ‡AMENTOS do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00030_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00030_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00030_CTP00001.get(1).put("AUT_USER", "55000035");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00030_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00030_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00030_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00030_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00030_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_IMEDIATA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00030_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00030_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00030_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00030_CTP00001.get(1).put("AUT_COMENTARIO","Test cenario 28 adicionar comentÃ¡rio");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00002_CN00030_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00030_CTP00001);

			//---------------------------------------------------------------------------------------------------------				


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de AprovaÃ§Ã£o Comercial do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTP00001.get(1).put("AUT_USER", "55192309");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_IMEDIATA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTP00001.get(1).put("AUT_COMENTARIO","");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTP00001);

			//---------------------------------------------------------------------------------------------------------				


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00002_CN00004_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de OrÃ§amento do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00004_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00004_CTP00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00004_CTP00001.get(1).put("AUT_USER", "55000035");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00004_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00004_CTP00001.get(1).put("AUT_CODIGO_ITEM","89296193");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00004_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00004_CTP00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00002_CN00004_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00004_CTP00001);

			// -------------------------------------------------------------------------


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00002_CN00005_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de OrÃ§amento do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00005_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00005_CTP00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00005_CTP00001.get(1).put("AUT_USER", "55000045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00005_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00005_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00005_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "758.536.958-19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00005_CTP00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00002_CN00005_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00005_CTP00001);

			// -------------------------------------------------------------------------

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00002_CN00006_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de OrÃ§amento do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00006_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00006_CTP00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00006_CTP00001.get(1).put("AUT_USER", "55000045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00006_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00006_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00006_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "758.536.958-19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00006_CTP00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00006_CTP00001.get(1).put("AUT_DESCONTO", "08,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00006_CTP00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00002_CN00006_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00006_CTP00001);

			// -------------------------------------------------------------------------

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00002_CN00007_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de OrÃ§amento do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00007_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00007_CTP00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00007_CTP00001.get(1).put("AUT_USER", "55000045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00007_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00007_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00007_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "758.536.958-19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00007_CTP00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00007_CTP00001.get(1).put("AUT_DESCONTO", "80,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00007_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00007_CTP00001.get(1).put("AUT_DESCONTO", "80,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00007_CTP00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00002_CN00007_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00007_CTP00001);

			// -------------------------------------------------------------------------

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00002_CN00008_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de OrÃ§amento do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00008_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00008_CTP00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00008_CTP00001.get(1).put("AUT_USER", "55000045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00008_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00008_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00008_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "758.536.958-19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00008_CTP00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00008_CTP00001.get(1).put("AUT_TELEFONE_SERVIÃ‡O", "1187272345");		

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00002_CN00008_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00008_CTP00001);

			// -------------------------------------------------------------------------

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00002_CN00012_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de OrÃ§amento do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00012_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00012_CTP00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00012_CTP00001.get(1).put("AUT_USER", "55000045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00012_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00012_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00012_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "75853695819");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00012_CTP00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00002_CN00012_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00012_CTP00001);

			// -------------------------------------------------------------------------



			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00002_CN00017_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de OrÃ§amento do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00017_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00017_CTP00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00017_CTP00001.get(1).put("AUT_USER", "55000045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00017_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00017_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00017_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "758.536.958-19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00017_CTP00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00017_CTP00001.get(1).put("AUT_DESCONTO", "20,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00017_CTP00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00002_CN00017_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00017_CTP00001);

			// -------------------------------------------------------------------------

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00002_CN00024_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de OrÃ§amento do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00024_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00024_CTP00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00024_CTP00001.get(1).put("AUT_USER", "55000045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00024_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00024_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00024_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "758.536.958-19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00024_CTP00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00024_CTP00001.get(1).put("AUT_DESCONTO", "20,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00024_CTP00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00002_CN00024_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00024_CTP00001);

			// -------------------------------------------------------------------------

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00002_CN00025_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de OrÃ§amento do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00025_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00025_CTP00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00025_CTP00001.get(1).put("AUT_USER", "55000045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00025_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00025_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00025_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "758.536.958-19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00025_CTP00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00025_CTP00001.get(1).put("AUT_DESCONTO", "20,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00025_CTP00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00002_CN00025_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00025_CTP00001);

			// -------------------------------------------------------------------------



			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTR00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de AprovaÃ§Ã£o Comercial do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTR00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTR00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTR00001.get(1).put("AUT_USER", "55000035");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTR00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "758.536.958-19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTR00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTR00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTR00001.get(1).put("AUT_DESCONTO", "80,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTR00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTR00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTR00001);

			// -------------------------------------------------------------------------------------------------------------

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00003_CN00002_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de AprovaÃ§Ã£o Comercial do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00002_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00002_CTP00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00002_CTP00001.get(1).put("AUT_USER", "55192309");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00002_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00002_CTP00001.get(1).put("AUT_NUMERO_PEDIDO", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00002_CTP00001.get(1).put("AUT_DESCONTO_AUMENTA", "90,00");		

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00003_CN00002_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00002_CTP00001);

			// -------------------------------------------------------------------------


			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTR00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTR00001);

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00003_CN00002_CTR00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de AprovaÃ§Ã£o Comercial do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00002_CTR00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00002_CTR00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00002_CTR00001.get(1).put("AUT_USER", "55000035");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00002_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00002_CTR00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00002_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "758.536.958-19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00002_CTR00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00002_CTR00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00002_CTR00001.get(1).put("AUT_DESCONTO", "80,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00002_CTR00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);


			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00003_CN00002_CTR00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00002_CTR00001);

			// -------------------------------------------------------------------------

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00003_CN00003_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de AprovaÃ§Ã£o Comercial do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00003_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00003_CTP00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00003_CTP00001.get(1).put("AUT_USER", "55192309");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00003_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00003_CTP00001.get(1).put("AUT_NUMERO_PEDIDO", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00003_CTP00001.get(1).put("AUT_DESCONTO_AUMENTA", "20,00");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00003_CN00003_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00003_CTP00001);

			// -------------------------------------------------------------------------

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00003_CN00003_CTR00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de AprovaÃ§Ã£o Comercial do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00003_CTR00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00003_CTR00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00003_CTR00001.get(1).put("AUT_USER", "55000035");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00003_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00003_CTR00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00003_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "758.536.958-19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00003_CTR00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00003_CTR00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00003_CTR00001.get(1).put("AUT_DESCONTO", "80,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00003_CTR00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00003_CN00003_CTR00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00003_CTR00001);

			//-------------------------------------------------------------------------

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00003_CN00004_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de AprovaÃ§Ã£o Comercial do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00004_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00004_CTP00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00004_CTP00001.get(1).put("AUT_USER", "55192309");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00004_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00004_CTP00001.get(1).put("AUT_NUMERO_PEDIDO", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00004_CTP00001.get(1).put("AUT_DESCONTO_AUMENTA", "90,00");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00003_CN00004_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00004_CTP00001);

			// -------------------------------------------------------------------------

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00003_CN00004_CTR00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de AprovaÃ§Ã£o Comercial do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00004_CTR00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00004_CTR00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00004_CTR00001.get(1).put("AUT_USER", "55000035");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00004_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00004_CTR00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00004_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "758.536.958-19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00004_CTR00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00004_CTR00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00004_CTR00001.get(1).put("AUT_DESCONTO", "80,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00004_CTR00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00003_CN00004_CTR00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00004_CTR00001);

			//-------------------------------------------------------------------------------------------------------------------

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00003_CN00005_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de AprovaÃ§Ã£o Comercial do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00005_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00005_CTP00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00005_CTP00001.get(1).put("AUT_USER", "55192309");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00005_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00005_CTP00001.get(1).put("AUT_NUMERO_PEDIDO", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00005_CTP00001.get(1).put("AUT_DESCONTO_AUMENTA", "20,00");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00003_CN00005_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00005_CTP00001);

			// -------------------------------------------------------------------------

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00003_CN00005_CTR00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de AprovaÃ§Ã£o Comercial do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00005_CTR00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00005_CTR00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00005_CTR00001.get(1).put("AUT_USER", "55000035");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00005_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00005_CTR00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00005_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "758.536.958-19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00005_CTR00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00005_CTR00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00005_CTR00001.get(1).put("AUT_DESCONTO", "80,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00005_CTR00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00003_CN00005_CTR00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00005_CTR00001);
			//-------------------------------------------------------------------------------------------------------------------



			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTR00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTR00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTR00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTR00001.get(1).put("AUT_USER", "55001063");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTR00001.get(1).put("AUT_CODIGO_ITEM","89296193");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTR00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VISA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTR00001.get(1).put("AUT_NUMERO_CARTAO", "4000 0000 0000 0044"); //5300332890376075
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTR00001.get(1).put("AUT_NOME_TITULAR", "UsuÃ¡rio AutomaÃ§Ã£o");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTR00001.get(1).put("AUT_VALIDADE", "01/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTR00001.get(1).put("AUT_CODIGO_CARTAO", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO_2",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VOUCHER);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO_2",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTR00001.get(1).put("AUT_CODE_VOUCHER",".*\\d{19}");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTR00001.get(1).put("AUT_NUMERO_PEDIDO","");    

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTR00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTR00001);

			// -------------------------------------------------------------------------


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTP00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTP00001.get(1).put("AUT_USER", "55000045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTP00001.get(1).put("AUT_NUMERO_PEDIDO", "112221");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTP00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTP00001);


			// -------------------------------------------------------------------------

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00088_CTR00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00088_CTR00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00088_CTR00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00088_CTR00001.get(1).put("AUT_USER", "55000045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00088_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00088_CTR00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00088_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00088_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00088_CTR00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00088_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00088_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00088_CTR00001.get(1).put("AUT_NUMERO_PEDIDO","");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00088_CTR00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00088_CTR00001);

			// -------------------------------------------------------------------------


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00086_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00088_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00088_CTP00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00088_CTP00001.get(1).put("AUT_USER", "55000045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00088_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00088_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00088_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00088_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00088_CTP00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00088_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00088_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00088_CTP00001.get(1).put("AUT_CONDICOES_PEDIDO",AUT_MANTER_CONDICOES.NAO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00088_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00088_CTP00001.get(1).put("AUT_STATUS_PEDIDO","NA");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00088_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00088_CTP00001);

			// -------------------------------------------------------------------------


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00089_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00089_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00089_CTP00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00089_CTP00001.get(1).put("AUT_USER", "55000045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00089_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00089_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00089_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00089_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00089_CTP00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00089_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00089_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA.toString());

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00089_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00089_CTP00001);


			// -------------------------------------------------------------------------


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00004
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuração de componente de Televendas do sistema VA
			 * Parametros comuns aos cenários
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00004 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00004.put(1, new java.util.HashMap<String,Object>());
			vaDataRSP_PJTTRC_FRT001_VA_MD00004.get(1).put("AUT_USER", "55000035");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00004.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00004);



			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00004_CN00001_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuração de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00001_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00001_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00001_CTP00001.get(1).put("AUT_USER", "55001063");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00001_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00001_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00001_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00001_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO .toString());

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00004_CN00001_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00001_CTP00001);


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00004_CN00002_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuração de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00002_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00002_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00002_CTP00001.get(1).put("AUT_USER", "55001063");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00002_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00002_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00002_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00002_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00002_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00002_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VISA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00002_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00002_CTP00001.get(1).put("AUT_NUMERO_CARTAO", "4000000000000044");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00002_CTP00001.get(1).put("AUT_NOME_TITULAR", "Usuário Automação");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00002_CTP00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00002_CTP00001.get(1).put("AUT_CODIGO_CARTAO", "123");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00002_CTP00001.get(1).put("AUT_CODE_VALE_TROCA","00620723");			

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00004_CN00002_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00002_CTP00001);



			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00004_CN00003_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuração de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00003_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00003_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00003_CTP00001.get(1).put("AUT_USER", "55001063"); //5576316456068 55001062  55001063 55620980
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00003_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00003_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "120");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00003_CTP00001.get(1).put("AUT_CODIGO_ITEM", "89455163"); //89455163   89455163
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00003_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "22872006818"); //43409000283  89716885000133
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00003_CTP00001.get(1).put("AUT_FLUXO_SAIDA", br.lry.components.va.cat007.AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00003_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", br.lry.components.va.cat009.AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VISA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00003_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", br.lry.components.va.cat009.AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00003_CTP00001.get(1).put("AUT_NUMERO_CARTAO", "4000000000000044");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00003_CTP00001.get(1).put("AUT_NOME_TITULAR", "Usuário Automação");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00003_CTP00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00003_CTP00001.get(1).put("AUT_CODIGO_CARTAO", "123");		

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00004_CN00003_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00003_CTP00001);



			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00004_CN00004_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuração de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00004_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00004_CTP00001.put(1, new java.util.HashMap<String,Object>());			

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00004_CTP00001.get(1).put("AUT_USER", "55001063");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00004_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00004_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00004_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00004_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00004_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00004_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VISA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00004_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00004_CTP00001.get(1).put("AUT_NUMERO_CARTAO", "4000000000000044");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00004_CTP00001.get(1).put("AUT_NOME_TITULAR", "Usuário Automação");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00004_CTP00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00004_CTP00001.get(1).put("AUT_CODIGO_CARTAO", "123");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00004_CTP00001.get(1).put("AUT_CODE_VALE_TROCA","00620723");			

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00004_CN00004_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00004_CTP00001);



			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00004_CN00005_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuração de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00005_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00005_CTP00001.put(1, new java.util.HashMap<String,Object>());



			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00004_CN00005_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00005_CTP00001);



			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTR00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuração de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTR00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTR00001.put(1, new java.util.HashMap<String,Object>());
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTR00001.get(1).put("AUT_USER", "55001063");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTR00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTR00001.get(1).put("AUT_FLUXO_SAIDA", br.lry.components.va.cat007.AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTR00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO", br.lry.components.va.cat009.AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VISA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO", br.lry.components.va.cat009.AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTR00001.get(1).put("AUT_NUMERO_CARTAO", "4000000000000044");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTR00001.get(1).put("AUT_NOME_TITULAR", "Usuário Automação");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTR00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTR00001.get(1).put("AUT_CODIGO_CARTAO", "123");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTR00001.get(1).put("AUT_CODE_VOUCHER","6394422000000293143");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTR00001.get(1).put("AUT_VALOR_PAGAMENTO_2", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO_2", br.lry.components.va.cat009.AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VOUCHER);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO_2", br.lry.components.va.cat009.AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTR00001.get(1).put("AUT_NUMERO_PEDIDO","");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTR00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTR00001);



			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuração de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTP00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTP00001.get(1).put("AUT_USER", "55192309"); //5576316456068
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTP00001.get(1).put("AUT_PASSWORD", "1234");


			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTP00001);



			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuração de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA .toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001.get(1).put("AUT_USER", "55001063"); //5576316456068 55001062  55001063 55620980
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001.get(1).put("AUT_QUANTIDADE_ITEM", "120");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001.get(1).put("AUT_CODIGO_ITEM", "89455163"); //89455163   89455163
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //43409000283  89716885000133
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001.get(1).put("AUT_FLUXO_SAIDA", br.lry.components.va.cat007.AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO", br.lry.components.va.cat009.AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VISA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO", br.lry.components.va.cat009.AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001.get(1).put("AUT_NUMERO_CARTAO", "4000000000000044");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001.get(1).put("AUT_NOME_TITULAR", "Usuário Automação");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001.get(1).put("AUT_CODIGO_CARTAO", "123");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001.get(1).put("AUT_CODE_VOUCHER","6394422000000293143");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001.get(1).put("AUT_VALOR_PAGAMENTO_2", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO_2", br.lry.components.va.cat009.AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VOUCHER);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO_2", br.lry.components.va.cat009.AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001.get(1).put("AUT_NUMERO_PEDIDO","");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001);


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuração de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTP00001.get(1).put("AUT_USER", "55192309"); //5576316456068
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTP00001.get(1).put("AUT_PASSWORD", "1234");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTP00001);



			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00004_CN00008_CTR00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuração de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00008_CTR00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00008_CTR00001.put(1, new java.util.HashMap<String,Object>());

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00004_CN00008_CTR00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00008_CTR00001);



			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00004_CN00008_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuração de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00008_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00008_CTP00001.put(1, new java.util.HashMap<String,Object>());

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00004_CN00008_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00008_CTP00001);



			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTR00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuração de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTR00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTR00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTR00001.get(1).put("AUT_USER", "55001063");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTR00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTR00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTR00001.get(1).put("AUT_NUMERO_CARTAO", "4000000000000044");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTR00001.get(1).put("AUT_NOME_TITULAR", "Usuário Automação");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTR00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTR00001.get(1).put("AUT_CODIGO_CARTAO", "123");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTR00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTR00001);



			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuração de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTP00001.get(1).put("AUT_USER", "55001063");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTP00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTP00001.get(1).put("AUT_NUMERO_CARTAO", "4000000000000044");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTP00001.get(1).put("AUT_NOME_TITULAR", "Usuário Automação");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTP00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTP00001.get(1).put("AUT_CODIGO_CARTAO", "123");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTP00001.get(1).put("AUT_NUMERO_CARTAO", "4000000000000044");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTP00001.get(1).put("AUT_NOME_TITULAR", "Usuário Automação");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTP00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTP00001.get(1).put("AUT_CODIGO_CARTAO", "123");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO_2",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VOUCHER);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO_2",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTP00001.get(1).put("AUT_CODE_VOUCHER","6394422000000293143");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTP00001);



			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuração de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTP00001.put(1, new java.util.HashMap<String,Object>());

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTP00001);

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTR00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTR00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA .toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTR00001.get(1).put("AUT_USER", "55001063"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTR00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTR00001.get(1).put("AUT_CODIGO_ITEM", "89455163"); //89407066 89455163
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTR00001.get(1).put("AUT_FLUXO_SAIDA", br.lry.components.va.cat007.AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTR00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO", br.lry.components.va.cat009.AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO", br.lry.components.va.cat009.AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTR00001.get(1).put("AUT_NUMERO_CARTAO", "4000000000000044");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTR00001.get(1).put("AUT_NOME_TITULAR", "Usuário Automação");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTR00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTR00001.get(1).put("AUT_CODIGO_CARTAO", "123");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO_2",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VALE_TROCA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO_2",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTR00001.get(1).put("AUT_CODE_VALE_TROCA","00620723");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTR00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTR00001);
			//AUT_GLOBAL_PARAMETERS.put("CN00004_CADASTRO_CLIENTE_PF_LOJA0035",vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTR00001);


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuração de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA .toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001.get(1).put("AUT_USER", "55001063"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001.get(1).put("AUT_CODIGO_ITEM", "89455163"); //89407066 89455163
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001.get(1).put("AUT_FLUXO_SAIDA", br.lry.components.va.cat007.AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO", br.lry.components.va.cat009.AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO", br.lry.components.va.cat009.AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001.get(1).put("AUT_NUMERO_CARTAO", "4000000000000044");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001.get(1).put("AUT_NOME_TITULAR", "Usuário Automação");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001.get(1).put("AUT_CODIGO_CARTAO", "123");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO_2",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VALE_TROCA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO_2",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001.get(1).put("AUT_CODE_VALE_TROCA","00620723");


			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001);



			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuração de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTP00001.get(1).put("AUT_USER", "55192309"); //5576316456068
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTP00001.get(1).put("AUT_NUMERO_PEDIDO", "4566677778");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTP00001);



			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTR00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuração de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTR00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTR00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA .toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTR00001.get(1).put("AUT_USER", "55001063"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTR00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTR00001.get(1).put("AUT_CODIGO_ITEM", "89455163"); //89407066 89455163
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTR00001.get(1).put("AUT_FLUXO_SAIDA", br.lry.components.va.cat007.AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTR00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO", br.lry.components.va.cat009.AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO", br.lry.components.va.cat009.AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTR00001.get(1).put("AUT_NUMERO_CARTAO", "4000000000000044");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTR00001.get(1).put("AUT_NOME_TITULAR", "Usuário Automação");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTR00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTR00001.get(1).put("AUT_CODIGO_CARTAO", "123");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO_2",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VALE_TROCA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO_2",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTR00001.get(1).put("AUT_CODE_VALE_TROCA","00620723");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTR00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTR00001);



			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuração de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTP00001.get(1).put("AUT_USER", "55192309"); //5576316456068
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTP00001.get(1).put("AUT_NUMERO_PEDIDO", "4566677778");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTP00001);



			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTR00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuração de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTR00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTR00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA .toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTR00001.get(1).put("AUT_USER", "55001063"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTR00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTR00001.get(1).put("AUT_CODIGO_ITEM", "89455163"); //89407066 89455163
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTR00001.get(1).put("AUT_DESCONTO", "80,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTR00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTR00001.get(1).put("AUT_FLUXO_SAIDA", br.lry.components.va.cat007.AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTR00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO", br.lry.components.va.cat009.AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VISA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTR00001.get(1).put("AUT_NUMERO_CARTAO", "4000000000000044");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTR00001.get(1).put("AUT_NOME_TITULAR", "Usuário Automação");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTR00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTR00001.get(1).put("AUT_CODIGO_CARTAO", "123");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO_2",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VALE_TROCA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO_2",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTR00001.get(1).put("AUT_CODE_VALE_TROCA","6394422000000293143");



			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTR00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTR00001);



			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuração de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTP00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTP00001.get(1).put("AUT_USER", "55192309"); //5576316456068
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTP00001.get(1).put("AUT_NUMERO_PEDIDO", "4566677778");


			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTP00001);



			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTR00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuração de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTR00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTR00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTR00001.get(1).put("AUT_USER", "55001063");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTR00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTR00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTR00001.get(1).put("AUT_NUMERO_CARTAO", "4000000000000044");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTR00001.get(1).put("AUT_NOME_TITULAR", "Usuário Automação");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTR00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTR00001.get(1).put("AUT_CODIGO_CARTAO", "123");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO_2",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VALE_TROCA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO_2",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTR00001.get(1).put("AUT_CODE_VALE_TROCA","00620723");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTR00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTR00001);



			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuração de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001.get(1).put("AUT_USER", "55001063");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001.get(1).put("AUT_NUMERO_CARTAO", "4000000000000044");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001.get(1).put("AUT_NOME_TITULAR", "Usuário Automação");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001.get(1).put("AUT_CODIGO_CARTAO", "123");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001.get(1).put("AUT_NUMERO_CARTAO", "4000000000000044");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001.get(1).put("AUT_NOME_TITULAR", "Usuário Automação");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001.get(1).put("AUT_CODIGO_CARTAO", "123");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO_2",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VOUCHER);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO_2",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001.get(1).put("AUT_CODE_VOUCHER","6394422000000293143");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");


			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001);



			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTR00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuração de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTR00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTR00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTR00001.get(1).put("AUT_USER", "55001063");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTR00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTR00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VISA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTR00001.get(1).put("AUT_NUMERO_CARTAO", "4000000000000044");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTR00001.get(1).put("AUT_NOME_TITULAR", "Usuário Automação");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTR00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTR00001.get(1).put("AUT_CODIGO_CARTAO", "123");


			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTR00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTR00001);



			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuração de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTP00001.get(1).put("AUT_USER", "55001063");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VISA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTP00001.get(1).put("AUT_NUMERO_CARTAO", "4000000000000044");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTP00001.get(1).put("AUT_NOME_TITULAR", "Usuário Automação");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTP00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTP00001.get(1).put("AUT_CODIGO_CARTAO", "123");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTP00001);



			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTR00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuração de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTR00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTR00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTR00001.get(1).put("AUT_USER", "55001063");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTR00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTR00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VISA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTR00001.get(1).put("AUT_NUMERO_CARTAO", "4000000000000044");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTR00001.get(1).put("AUT_NOME_TITULAR", "Usuário Automação");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTR00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTR00001.get(1).put("AUT_CODIGO_CARTAO", "123");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTR00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTR00001);


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuração de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTP00001.get(1).put("AUT_USER", "55001063");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VISA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTP00001.get(1).put("AUT_NUMERO_CARTAO", "4000000000000044");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTP00001.get(1).put("AUT_NOME_TITULAR", "Usuário Automação");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTP00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTP00001.get(1).put("AUT_CODIGO_CARTAO", "123");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTP00001);



			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTR00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuração de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTR00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTR00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTR00001.put(1, new java.util.HashMap<String,Object>());
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTR00001.get(1).put("AUT_USER","55001063");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTR00001.get(1).put("AUT_PASSWORD","1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTR00001.get(1).put("AUT_QUANTIDADE_ITEM","2");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTR00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTR00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VISA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTR00001.get(1).put("AUT_NUMERO_CARTAO", "4000000000000044");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTR00001.get(1).put("AUT_NOME_TITULAR", "Usuário Automação");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTR00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTR00001.get(1).put("AUT_CODIGO_CARTAO", "123");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTR00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTR00001);



			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuração de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTP00001.put(1, new java.util.HashMap<String,Object>());
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTP00001.get(1).put("AUT_USER","55001063");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTP00001.get(1).put("AUT_PASSWORD","1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM","2");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTP00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VISA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTP00001.get(1).put("AUT_NUMERO_CARTAO", "4000000000000044");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTP00001.get(1).put("AUT_NOME_TITULAR", "Usuário Automação");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTP00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTP00001.get(1).put("AUT_CODIGO_CARTAO", "123");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTP00001);



			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00004_CN00018_CTR00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuração de componente de Televendas do sistema VA
			 * 
			 * 
			 */


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00004_CN00018_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuração de componente de Televendas do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00018_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00018_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00018_CTP00001.get(1).put("AUT_USER", "55001063");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00018_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00018_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00018_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00018_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00018_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00018_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00018_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X.toString());

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00004_CN00018_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00018_CTP00001);




			// -------------------------------------------------------------------------


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00006_CN00001_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Antifraude do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00001_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00001_CTP00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00001_CTP00001.get(1).put("AUT_USER", "55192309");
			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00001_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00001_CTP00001.get(1).put("AUT_NUMERO_PEDIDO", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00001_CTP00001.get(1).put("AUT_DESCONTO_AUMENTA", "20,00");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00006_CN00001_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00001_CTP00001);

			// -------------------------------------------------------------------------

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00006_CN00001_CTR00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Antifraude do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00001_CTR00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00001_CTR00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00001_CTR00001.get(1).put("AUT_USER", "55001062");
			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00001_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00001_CTR00001.get(1).put("AUT_CODIGO_ITEM","89296193");
			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00001_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "758.536.958-19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00001_CTR00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00001_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VISA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00001_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00001_CTR00001.get(1).put("AUT_NUMERO_CARTAO", "4000 0000 0000 0044");
			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00001_CTR00001.get(1).put("AUT_NOME_TITULAR", "UsuÃ¡rio AutomaÃ§Ã£o");
			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00001_CTR00001.get(1).put("AUT_VALIDADE", "01/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00001_CTR00001.get(1).put("AUT_CODIGO_CARTAO", "123");
			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00001_CTR00001.get(1).put("AUT_NUMERO_PEDIDO","");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00006_CN00001_CTR00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00001_CTR00001);		

			//-------------------------------------------------------------------------

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00006_CN00002_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Antifraude do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00002_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00002_CTP00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00002_CTP00001.get(1).put("AUT_USER", "55192309");
			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00002_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00002_CTP00001.get(1).put("AUT_NUMERO_PEDIDO", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00002_CTP00001.get(1).put("AUT_DESCONTO_AUMENTA", "20,00");


			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00006_CN00002_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00002_CTP00001);

			// -------------------------------------------------------------------------

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00006_CN00002_CTR00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Antifraude do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00002_CTR00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00002_CTR00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00002_CTR00001.get(1).put("AUT_USER", "55001062");
			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00002_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00002_CTR00001.get(1).put("AUT_CODIGO_ITEM","89296193");
			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00002_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "758.536.958-19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00002_CTR00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00002_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VISA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00002_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00002_CTR00001.get(1).put("AUT_NUMERO_CARTAO", "4000 0000 0000 0044");
			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00002_CTR00001.get(1).put("AUT_NOME_TITULAR", "UsuÃ¡rio AutomaÃ§Ã£o");
			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00002_CTR00001.get(1).put("AUT_VALIDADE", "01/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00002_CTR00001.get(1).put("AUT_CODIGO_CARTAO", "123");
			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00002_CTR00001.get(1).put("AUT_NUMERO_PEDIDO","");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00006_CN00002_CTR00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00002_CTR00001);

			//-------------------------------------------------------------------------

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00006_CN00003_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Antifraude do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00003_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00003_CTP00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00003_CTP00001.get(1).put("AUT_USER", "55192309");
			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00003_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00003_CTP00001.get(1).put("AUT_NUMERO_PEDIDO", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00003_CTP00001.get(1).put("AUT_DESCONTO_AUMENTA", "20,00");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00006_CN00003_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00003_CTP00001);

			// -------------------------------------------------------------------------

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00006_CN00003_CTR00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Antifraude do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00003_CTR00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00003_CTR00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00003_CTR00001.get(1).put("AUT_USER", "55001062");
			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00003_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00003_CTR00001.get(1).put("AUT_CODIGO_ITEM","89296193");
			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00003_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "758.536.958-19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00003_CTR00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00003_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VISA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00003_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00003_CTR00001.get(1).put("AUT_NUMERO_CARTAO", "4000 0000 0000 0044");
			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00003_CTR00001.get(1).put("AUT_NOME_TITULAR", "UsuÃ¡rio AutomaÃ§Ã£o");
			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00003_CTR00001.get(1).put("AUT_VALIDADE", "01/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00003_CTR00001.get(1).put("AUT_CODIGO_CARTAO", "123");
			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00003_CTR00001.get(1).put("AUT_NUMERO_PEDIDO","");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00006_CN00003_CTR00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00003_CTR00001);


			//-------------------------------------------------------------------------

			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00007_CN00001_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de ExclusÃ£o do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00007_CN00001_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00007_CN00001_CTP00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00007_CN00001_CTP00001.get(1).put("AUT_USER", "55000035");
			vaDataRSP_PJTTRC_FRT001_VA_MD00007_CN00001_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00007_CN00001_CTP00001.get(1).put("AUT_CODIGO_ITEM","89296193");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00007_CN00001_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00007_CN00001_CTP00001);

			//----------------------------------------------------------------------------------------------------------	


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00007_CN00002_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de ExclusÃ£o do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00007_CN00002_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00007_CN00002_CTP00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00007_CN00002_CTP00001.get(1).put("AUT_USER", "55000045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00007_CN00002_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00007_CN00002_CTP00001.get(1).put("AUT_CODIGO_ITEM","89296193");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00007_CN00002_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00007_CN00002_CTP00001);

			//----------------------------------------------------------------------------------------------------------	


			/**
			 * 
			 *************************************** MASSA DE DADOS - CENÁRIOS PRIORITÁRIOS - VENDAS ASSISTIDAS **********************
			 * 
			 */
			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.put(1, new java.util.HashMap<String,Object>());

			
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_INIT_APP", true);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_USER", "55001710");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_USUARIO_VA", "55001710");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_SENHA_VA", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_OPCAO_SELECAO_ITEM", AUT_SELECT_PRODUCT_OPTIONS_BY_STORE.CONDITION_BY_ID.name());
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_OPCAO_SELECAO_PARAMETRO", "57,55,35,36,40");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_QUANTIDADE_MAXIMA_ITENS", 2);
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_MATERIAL_QUANTIDADE", 1);
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_REMOVER_ITENS_CARRINHO", AUT_CONFIRMACAO_USUARIO.SIM.name());
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_TIPO_EXCLUSAO_ITEM", AUT_CONFIRMACAO_USUARIO.EXCLUSAO_GERAL_LIMPAR_CARRINHO.name());		
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_PARAMETRO_EXCLUSAO_ITENS", "3");		
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_TIPO_FILTRO_PESQUISA", AUT_VA_TIPO_DOCUMENTO_PESQUISA.CPF_OU_CNPJ.name());
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_DOCUMENTO_CLIENTE", "71137637749");//60280476000-LOJA 19 //26528652775//LOJA45-71137637749			
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_CT_MATERIAL_1", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_CT_MATERIAL_2", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_CT_MATERIAL_3", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_CT_MATERIAL_4", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_CT_MATERIAL_5", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_NUMERO_PEDIDO", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_NUMERO_CARTAO", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_EDICAO_ITEM_OPCAO", AUT_EDICAO_PEDIDO.QUANTIDADE_ITEM_QUANT_ADICIONAR_PADRAO.name());
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_ITENS_EDICAO", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_MODO_CONSULTA_ITEM", AUT_MODO_CONSULTAS_VA_SELECAO_ITEM.EDICAO.name());
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_EDIT_ITEM_SELECT", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_QUANTIDADE_PADRAO_EDICAO_ITEM_UNITARIO", 25);
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_QUANTIDADE_PADRAO_EDICAO_ITEM_FRACIONADO", 40);
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_PEDIDO", "");

			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("INDEX_MATERIAL_POSSUI_LOTE", "89627601");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_NUMERO_LOTE_ALTERACAO", "JOJO");
			
			
			//CAMPOS PARA CONFIGURAÇÃO DOS FLUXOS DO PDV
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_MATERIAL", "89296193");			
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_OPERADOR", "951028487");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_PWD_OPERADOR", "951028487");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_COORDENADOR", "9951028487");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_FLUXO_SAIDA",   br.lry.components.va.AUTVAGeradorPedido.AUT_VA_FLUXO_SAIDA.REITRADA_EXTERNA_IMEDIATA.name());
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_QUANTIDADE_PADRAO_DEVOLUCAO",   "20160");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_PWD_COORDENADOR",   "51028487");
				
			
			
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_LOJA_DEVOLUCAO",  "0045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_ITEM_QUANTIDADE",  1);
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_DD",  "11");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_TELEFONE",  "912123434");
					
			
			//PARAMETROS SAP
			
			
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_TEMPO_ESPERA_CRIACAO_REMESSA", "5"); //EM SEGUNDOS
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_FATURAR_ITENS_COM_LOTE", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_NUMERO_TENTATIVAS_CONSULTA_ORDEM_VENDA", "5");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("USER_SAP", "51032900");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("PWD_SAP", "leroy123");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("INIT_TRANSACTION", true);
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_DOCUMENTO_SD", "30003866");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_TIPO_PEDIDO", "ZVAS");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_LOJA", "0045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_DATA_INICIAL", "01.04.2019");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_DATA_FINAL", "01.06.2019");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_TIPO_PEDIDO", "ZVAS");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_FILA", "ARMAZENA");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_FORMATO", "16X20ITS");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_DOC_FORNECIMENTO", "800000109");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("INIT_TRANSACTION", 1);
				
			
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_NUMERO_REMESSA_SAP", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_NUMERO_ORDEM_TRANSPORTE_SAP", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO_FORNECIMENTO_SAP", "");
			
			
			
			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00001_CTP00001);
			
			
			
			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_FUNCTION_BY_ITEM_PROCESS", "");						
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_INIT_APP", true);						
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_TIPO_ACESSO_LOGIN", AUT_TIPO_ACESSO_LOGIN.USUARIO_LOJA.name());			
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_LOJA_TELEVENDAS", AUT_BOITATA_LOJAS.SAO_BERNARDO_CAMPO.name());						
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_USER", "55001710");//55000045
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_USER_TELEVENDAS", "55514016"); //LOJA19-55436723//LOJA45-55514016//
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_PASSWORD_TELEVENDAS", "1234");			
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_USER_GERENTE", "55740076");//LOJA19-55514016//LOJA45-55708317
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_PASSWORD_GERENTE", "1234");//LOJA19-55514016//LOJA45-55708317
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_PASSWORD_TELEVENDAS", "1234");			
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_USUARIO_VA", "55001710");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_SENHA_VA", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_MATERIAL", "89296193");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_OPCAO_SELECAO_ITEM", AUT_SELECT_PRODUCT_OPTIONS_BY_STORE.CONDITION_BY_STORE.name());
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_OPCAO_SELECAO_PARAMETRO", "41,42,43,44,45,46,,47,48,49,50,51"); //LOJA45-41,42,43,44,45,46,,47,48,49,50,51//LOJA19-12-14,15,16,17,18,20,21,22,23
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_MATERIAL_QUANTIDADE", 5);
			
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_QUANTIDADE_MAXIMA_ITENS", 5);
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_MATERIAL_QUANTIDADE", 5);
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_REMOVER_ITENS_CARRINHO", AUT_CONFIRMACAO_USUARIO.SIM.name());
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_TIPO_EXCLUSAO_ITEM", AUT_CONFIRMACAO_USUARIO.EXCLUSAO_GERAL_LIMPAR_CARRINHO.name());		
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_PARAMETRO_EXCLUSAO_ITENS", "3");		
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_TIPO_FILTRO_PESQUISA", AUT_VA_TIPO_DOCUMENTO_PESQUISA.CPF_OU_CNPJ.name());
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_DOCUMENTO_CLIENTE", "26528652775");//60280476000-LOJA 19 //26528652775-LOJA45//71137637749-LOJA45
			
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_CT_MATERIAL_1", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_CT_MATERIAL_2", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_CT_MATERIAL_3", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_CT_MATERIAL_4", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_CT_MATERIAL_5", "");
			
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_CT_MATERIAL_6", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_CT_MATERIAL_7", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_CT_MATERIAL_8", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_CT_MATERIAL_9", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_CT_MATERIAL_10", "");
			
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_NUMERO_PEDIDO", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_INDEX_NOVA_DATA_AGENDA", "");
			
			
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_EDICAO_ITEM_OPCAO", AUT_EDICAO_PEDIDO.QUANTIDADE_ITEM_QUANT_ADICIONAR_PADRAO.name());
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_ITENS_EDICAO", "");

			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_PONTO_VENDA_PEDIDO","Televendas");			
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_MODO_CONSULTA_ITEM", AUT_MODO_CONSULTAS_VA_SELECAO_ITEM.EDICAO.name());
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_EDIT_ITEM_SELECT", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_QUANTIDADE_PADRAO_EDICAO_ITEM_UNITARIO", 25);
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_QUANTIDADE_PADRAO_EDICAO_ITEM_FRACIONADO", 40);
							
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_NUMERO_CARTAO", "00000000000");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_PAGAR_NA_LOJA",0);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_METODO_PAGAMENTO", METODOS_PAGAMENTO.MASTERCARD.name());
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", PLANOS_PAGAMENTO.PARCELA_1X.name());
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_NUMERO_CARTAO_PESSOAL", "5000000000000001");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_CARTAO_NOME_TITULAR", "AUT NOME TITULAR CARTAO");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_CARTAO_DATA_VALIDADE","04/19");			
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_CARTAO_CODIGO_SEGURANCA", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_IGNORAR_ANTIFRAUDE", 1);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_QUANTIDADE_PADRAO_DEVOLUCAO",   "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_PWD_COORDENADOR",   "51028487");
				
			//PARAMETROS SAP
			
			
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_TEMPO_ESPERA_CRIACAO_REMESSA", "5"); //EM SEGUNDOS
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_FATURAR_ITENS_COM_LOTE", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_NUMERO_TENTATIVAS_CONSULTA_ORDEM_VENDA", "5");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("USER_SAP", "51032900");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("PWD_SAP", "leroy123");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("INIT_TRANSACTION", true);
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_DOCUMENTO_SD", "30003866");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_TIPO_PEDIDO", "ZVAS");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_LOJA", "0045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_DATA_INICIAL", "01.04.2019");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_DATA_FINAL", "01.06.2019");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_TIPO_PEDIDO", "ZVAS");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_FILA", "ARMAZENA");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_FORMATO", "16X20ITS");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_DOC_FORNECIMENTO", "800000109");
				
			
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_NUMERO_REMESSA_SAP", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_NUMERO_ORDEM_TRANSPORTE_SAP", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO_FORNECIMENTO_SAP", "");

			
			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00002_CTP00001);


			
			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.put(1, new java.util.HashMap<String,Object>());


			
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.get(1).put("AUT_INIT_APP", true);
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.get(1).put("AUT_TELEFONE_PARA_CONTATO_AGENDA_ENTREGA", "11944335566");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.get(1).put("AUT_TIPO_TELEFONE_PARA_CONTATO_AGENDA_ENTREGA",AUT_TIPO_TELEFONE_PARA_CONTATO.CELULAR.name());			
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.get(1).put("AUT_USER", "55001710");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.get(1).put("AUT_USUARIO_VA", "55001710");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.get(1).put("AUT_SENHA_VA", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.get(1).put("AUT_MATERIAL", "89296193");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.get(1).put("AUT_OPCAO_SELECAO_ITEM", AUT_SELECT_PRODUCT_OPTIONS_BY_STORE.CONDITION_BY_ID.name());
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.get(1).put("AUT_OPCAO_SELECAO_PARAMETRO", "53,54,55,56,57,58");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.get(1).put("AUT_QUANTIDADE_MAXIMA_ITENS", 2);
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.get(1).put("AUT_MATERIAL_QUANTIDADE", 1);
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.get(1).put("AUT_REMOVER_ITENS_CARRINHO", AUT_CONFIRMACAO_USUARIO.SIM.name());
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.get(1).put("AUT_TIPO_EXCLUSAO_ITEM", AUT_CONFIRMACAO_USUARIO.EXCLUSAO_GERAL_LIMPAR_CARRINHO.name());		
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.get(1).put("AUT_PARAMETRO_EXCLUSAO_ITENS", "3");		
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.get(1).put("AUT_TIPO_FILTRO_PESQUISA", AUT_VA_TIPO_DOCUMENTO_PESQUISA.CPF_OU_CNPJ.name());
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.get(1).put("AUT_DOCUMENTO_CLIENTE", "26528652775");//60280476000-LOJA 19 //26528652775
			
			
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.get(1).put("AUT_CT_MATERIAL_1", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.get(1).put("AUT_CT_MATERIAL_2", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.get(1).put("AUT_CT_MATERIAL_3", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.get(1).put("AUT_CT_MATERIAL_4", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.get(1).put("AUT_CT_MATERIAL_5", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.get(1).put("AUT_NUMERO_PEDIDO", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.get(1).put("AUT_NUMERO_CARTAO", "");
			
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.get(1).put("AUT_EDICAO_ITEM_OPCAO", AUT_EDICAO_PEDIDO.QUANTIDADE_ITEM_QUANT_ADICIONAR_PADRAO.name());
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.get(1).put("AUT_ITENS_EDICAO", "");
			
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.get(1).put("AUT_MODO_CONSULTA_ITEM", AUT_MODO_CONSULTAS_VA_SELECAO_ITEM.EDICAO.name());
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.get(1).put("AUT_EDIT_ITEM_SELECT", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.get(1).put("AUT_QUANTIDADE_PADRAO_EDICAO_ITEM_UNITARIO", 25);
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.get(1).put("AUT_QUANTIDADE_PADRAO_EDICAO_ITEM_FRACIONADO", 40);
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.get(1).put("AUT_PEDIDO", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.get(1).put("AUT_FLUXO_SAIDA",  br.lry.components.va.AUTVAGeradorPedido.AUT_VA_FLUXO_SAIDA.REITRADA_EXTERNA_IMEDIATA.name());
			
			

			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.get(1).put("AUT_MATERIAL_COM_SERVICO_CONFIGURADO", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.get(1).put("AUT_INDEX_SERVICO_ASSOCIADO_MATERIAL", "1");		
			
					
			//CONFIGURA INCLUSAO DE GARANTIA
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.get(1).put("AUT_MATERIAL_COM_GARANTIA_CONFIGURADO", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.get(1).put("AUT_GARANTIA_QUANTIDADE_PADRAO", "1");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.get(1).put("AUT_INDEX_GARANTIA_ASSOCIADO_MATERIAL", "1");

			
			//PARAMETROS PDV
					
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.get(1).put("AUT_OPERADOR", "951028518");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.get(1).put("AUT_PWD_OPERADOR", "51028518");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.get(1).put("AUT_COORDENADOR", "51028487");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.get(1).put("AUT_FLUXO_SAIDA",   br.lry.components.va.AUTVAGeradorPedido.AUT_VA_FLUXO_SAIDA.REITRADA_EXTERNA_IMEDIATA.name());
			
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.get(1).put("AUT_LOJA_DEVOLUCAO",  "0045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.get(1).put("AUT_ITEM_QUANTIDADE",  1);
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.get(1).put("AUT_DD",  "11");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.get(1).put("AUT_TELEFONE",  "912123434");
			vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.get(1).put("AUT_PEDIDO",  "");

				
			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00009_CN00003_CTP00001);

			return AUT_GLOBAL_PARAMETERS;
		}
		catch(java.lang.Exception e) {
			System.out.println("DATAFLOW : ERROR : INIT PARAMETERS");
			System.out.println(e.getMessage());
			//AUT_LOG_MANAGER.logMensagem("AUT ERROR: INICIALIZATION OF PARAMETERS DATAFLOW : ".concat(e.getMessage()));

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
		return AUT_GLOBAL_PARAMETERS.get(AUT_TABLE_PARAMETERS_NAMES.AUT_VA_LOGIN.toString()).get(1);
	}

	public <TDataTable extends java.util.HashMap<String,Object>> java.util.HashMap<String,Object> autGetMergeParameters(TDataTable hshData1,TDataTable hshData2){
		java.util.HashMap<String,Object> parametersOut = new java.util.HashMap<String,Object>();

		for(String key1 : hshData1.keySet())
		{
			parametersOut.put(key1, hshData1.get(key1));
		}		

		for(String key2 : hshData2.keySet())
		{
			if(!parametersOut.containsKey(key2)) {
				parametersOut.put(key2, hshData2.get(key2));
			}
		}

		System.out.println("AUT MERGE HASH: DATA 1");
		System.out.println(hshData1);
		System.out.println("AUT MERGE HASH: DATA 2");
		System.out.println(hshData2);
		System.out.println("AUT MERGE HASH: DATA MERGE");
		System.out.println(parametersOut);


		return parametersOut;
	}


	public <TDataTable extends java.util.HashMap<String,Object>> java.util.HashMap<String,Object> autGetMergeParameters(TDataTable inputData,AUT_TABLE_PARAMETERS_NAMES table){
		java.util.HashMap<String,Object> parametersOut = new java.util.HashMap<String,Object>();
		java.util.HashMap<String,Object> hshData = autGetParameters(table);
		for(String key1 : hshData.keySet())
		{
			if(!parametersOut.containsKey(key1)) {
				parametersOut.put(key1, hshData.get(key1));
			}
		}		

		for(String key2 : inputData.keySet())
		{
			if(!parametersOut.containsKey(key2)) {
				parametersOut.put(key2, inputData.get(key2));
			}
		}

		System.out.println("AUT MERGE HASH: DATA 1");
		System.out.println(inputData);
		System.out.println("AUT MERGE HASH: DATA 2");
		System.out.println(hshData);
		System.out.println("AUT MERGE HASH: DATA MERGE");
		System.out.println(parametersOut);


		return parametersOut;
	}


	public  java.util.HashMap<String,Object> autGetMergeParameters(AUT_TABLE_PARAMETERS_NAMES tabela1,AUT_TABLE_PARAMETERS_NAMES tabela2){

		java.util.HashMap<String,Object> parametersOut = new java.util.HashMap<String,Object>();
		java.util.HashMap<String,Object> data1 = autGetParameters(tabela1);
		java.util.HashMap<String,Object> data2 = autGetParameters(tabela2);

		for(String key1 : data1.keySet()) {
			parametersOut.put(key1, data1.get(key1));
		}

		for(String key2 : data2.keySet()) {
			if(!parametersOut.containsKey(key2)) {
				parametersOut.put(key2, data2.get(key2));
			}
		}

		System.out.println("AUT MERGE HASH: DATA 1");
		System.out.println(data1);
		System.out.println("AUT MERGE HASH: DATA 2");
		System.out.println(data2);
		System.out.println("AUT MERGE HASH: DATA MERGE");
		System.out.println(parametersOut);

		return parametersOut;
	}

	public <TDataFlowRows extends java.util.HashMap<Integer, java.util.HashMap<String,Object>>> TDataFlowRows autGetMergeParametersDataFlowFromRows(TDataFlowRows tabela1,TDataFlowRows tabela2){
		java.util.HashMap<Integer,java.util.HashMap<String,Object>> paramsOutput = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();

		for(Integer row: tabela1.keySet()) {
			if(!paramsOutput.containsKey(row)) 
			{
				paramsOutput.put(row, new java.util.HashMap<String,Object>());	
			}
			for(String columnName : tabela1.get(row).keySet()) {
				paramsOutput.get(row).put(columnName, tabela1.get(row).get(columnName));
			}
		}

		for(Integer row : tabela2.keySet()) {			
			for(String columnName : tabela2.get(row).keySet()) {
				if(paramsOutput.containsKey(row)) {
					if(!paramsOutput.get(row).containsKey(columnName)) {
						paramsOutput.get(row).put(columnName, tabela2.get(row).get(columnName));
					}
				}
			}
		}
		
		System.out.println("AUT INFO:  MERGE DE DATAFLOW : TABELA 1");
		System.out.println(tabela1);
		System.out.println("AUT INFO:  MERGE DE DATAFLOW : TABELA 2");
		System.out.println(tabela2);
		System.out.println("AUT INFO:  MERGE DE DATAFLOW : MERGE : TABELA 1      X      TABELA 2");
		System.out.println(paramsOutput);

		return (TDataFlowRows) paramsOutput;
	}

	/**
	 * Recupera um linhas da massa de dados configurada no dataflow
	 * 
	 * 
	 * @param table - Nome da tabela de dados associada
	 * 
	 * @return java.util.HashMap - Coleção de colunas
	 */
	public java.util.HashMap<String, Object> autGetParameters(AUT_TABLE_PARAMETERS_NAMES table) {
		autInitDataFlow();
		return AUT_GLOBAL_PARAMETERS.get(table.toString()).get(1);
	}

	public java.util.HashMap<Integer,java.util.HashMap<String, Object>> autGetParametersAllLines(AUT_TABLE_PARAMETERS_NAMES table) {
		if(AUT_GLOBAL_PARAMETERS==null) {
			autInitDataFlow();
		}
		return AUT_GLOBAL_PARAMETERS.get(table.toString());
	}

	public java.util.HashMap<String, Object> autGetParameters(AUT_TABLE_PARAMETERS_NAMES table,int rowNumber) {
		autInitDataFlow();
		return AUT_GLOBAL_PARAMETERS.get(table.toString()).get(rowNumber);
	}



	/**
	 * 
	 * Construtor padrão da classe
	 * 
	 */
	public AUTDataFlow() {
		AUT_LOG_MANAGER = new AUTLogMensagem();
	}


	/**
	 * 
	 * Limpa a base de dados
	 * 
	 */
	public void autClearDataFlow() {
		if(AUT_GLOBAL_PARAMETERS!=null) {
			AUT_GLOBAL_PARAMETERS.clear();						
		}
	}
}


