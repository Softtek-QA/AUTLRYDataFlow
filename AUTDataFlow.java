/**
 * 
 */
package br.lry.dataflow;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;

import org.junit.Test;

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
import br.lry.components.va.cat007.AUTFluxoSaida.AUT_VA_TIPO_FRETE;
import br.lry.components.va.cat009.AUTMeiosPagamento;
import br.lry.components.va.cat010.AUTDesconto;
import br.lry.components.va.cat010.AUTDesconto.AUT_TIPO_DESCONTO;
import br.lry.components.va.cat010.AUTEdicao.AUT_MANTER_CONDICOES;
import br.lry.components.va.cat010.AUTEdicao.AUT_STATUS_PESQUISA;
import br.lry.components.va.cat018.AUTSelecaoLojaBoitata;
import br.lry.components.va.cat018.AUTSelecaoLojaVA;
import br.lry.components.va.cat018.AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS;
import br.lry.dataflow.AUTDataFlow.AUT_TABLE_PARAMETERS_NAMES;
import br.lry.functions.AUTProjectsFunctions;
import br.lry.functions.AUTProjectsFunctions.AUTLogMensagem;
import br.lry.functions.AUTProjectsFunctions.AUTNumerosRandomicos;
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
	AUTLogMensagem AUT_LOG_MANAGER = null;
	AUTNumerosRandomicos AUT_CURRENT_RANDOM_MANAGER = null;
	public Boolean AUT_ENABLE_SYNCRONIZE_ALL_OBJECTS = false;	
	public static java.util.HashMap<String,java.util.HashMap<Integer,java.util.HashMap<String, Object>>> AUT_GLOBAL_PARAMETERS = null;

	public  <TOption extends java.lang.Enum> Object autGetParametersFromTable(TOption nomeTabela, String chave){
		Object valor = AUT_GLOBAL_PARAMETERS.get(nomeTabela.toString()).get(1).get(chave);
		return valor;		
	}

	public  <TOption extends java.lang.Enum> java.util.HashMap<String,Object> autGetParametersFromTable(TOption nomeTabela){
		return AUT_GLOBAL_PARAMETERS.get(nomeTabela.toString()).get(1);		

	}

	public <TOption extends java.lang.Enum> java.util.HashMap<String,Object> autGetParametersFromTable(TOption nomeTabela,Integer line){
		return AUT_GLOBAL_PARAMETERS.get(nomeTabela.toString()).get(line);				
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
	
	public enum AUT_VA_OPCAO_LOJA_FLUXO_SAIDA{
		LOJA_0019,
		CD_0019,
		LOJA_0045,
		CD_0045,
		LOJA_0035;
		
		@Override
		public String toString() {
			switch(this) {
				case LOJA_0045:{
					return "0045 - LOJA SÃO BERNARDO";
				}
				case CD_0045: {
					return "0014 - CD SAO BERNARDO";
				}
				case LOJA_0019:{
					return "0019 - LOJA PORTO ALEGRE";
				}
				case LOJA_0035:{
					return "0035 - LOJA CURITIBA ATUBA";
				}
				
				default:{
					return super.toString();
				}
			}
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
		RSP_PJTTRC_FRT001_VA_MD00000_CN00001_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00000_CN00002_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00000_CN00003_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00000_CN00007_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00000_CN00008_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00000_CN00009_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00000_CN00010_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00000_CN00011_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00000_CN00012_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00000_CN00013_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00000_CN00014_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00000_CN00015_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00000_CN00016A_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00000_CN00016B_CTP00001,
		RSP_PJTTRC_FRT001_VA_MD00000_CN00017_CTP00001,			
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
			String inscricaoEstadual = AUTProjectsFunctions.autGetIncriptionPR();

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
			vaDataCadastro.get(1).put("AUT_UF_PESQUISA", AUT_VA_ESTADOS.PR);	
			vaDataCadastro.get(1).put("AUT_CIDADE_PESQUISA", "BOCAIÚVA");
			vaDataCadastro.get(1).put("AUT_ENDERECO_PESQUISA", "RUA CIDADE JARDIM");
			vaDataCadastro.get(1).put("AUT_BAIRRO_PESQUISA", "Alto");	
			vaDataCadastro.get(1).put("AUT_TIPO_ENDERECO", AUT_VA_TIPO_ENDERECO.OBRA);
			
			vaDataCadastro.get(1).put("AUT_CEP", "82590-100");	
			vaDataCadastro.get(1).put("AUT_RUA_ENDERECO", "BR116");	
			vaDataCadastro.get(1).put("AUT_NUMERO_ENDERECO", "256");	
			vaDataCadastro.get(1).put("AUT_BAIRRO_ENDERECO", "Alto");	
			vaDataCadastro.get(1).put("AUT_COMPLEMENTO_ENDERECO", "CASA 123");	
			vaDataCadastro.get(1).put("AUT_CIDADE_ENDERECO", "Curitiba");	
			vaDataCadastro.get(1).put("AUT_ESTADO_ENDERECO", AUT_VA_ESTADOS.PR);	
			vaDataCadastro.get(1).put("AUT_REFERENCIA_ENDERECO", "ACOUGUE DA ESQUINA");	
			vaDataCadastro.get(1).put("AUT_TIPO_IMOVEL_RESIDENCIA", AUT_VA_TIPO_RESIDENCIA.RURAL_CHACARA_FAZENDA_OU_SITIO);	

			vaDataCadastro.get(1).put("AUT_CEP_2", "82590-100");	
			vaDataCadastro.get(1).put("AUT_RUA_ENDERECO_2", "BR116");	
			vaDataCadastro.get(1).put("AUT_NUMERO_ENDERECO_2", "256");	
			vaDataCadastro.get(1).put("AUT_BAIRRO_ENDERECO_2", "Centro");	
			vaDataCadastro.get(1).put("AUT_COMPLEMENTO_ENDERECO_2", "CASA 123");	
			vaDataCadastro.get(1).put("AUT_CIDADE_ENDERECO_2", "Osasco");	
			vaDataCadastro.get(1).put("AUT_ESTADO_ENDERECO_2", AUT_VA_ESTADOS.PR);	
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
			hmcLogin.get(1).put("AUT_USER", "marcos.oliveira");
			hmcLogin.get(1).put("AUT_PASSWORD", "1234");
			hmcLogin.get(1).put("AUT_USER_ID", "55".concat(AUTProjectsFunctions.gerarItemChaveRandomico(6)));	//criar numero randomico
			hmcLogin.get(1).put("AUT_USER_NAME", "AUT HMCVA USER: ");	//Criar nome padronizado
			hmcLogin.get(1).put("AUT_USER_EMAIL", "automationTest@softtek.com");
			hmcLogin.get(1).put("AUT_NOVA_SENHA", "1234");
			hmcLogin.get(1).put("AUT_CANAL", "channel_store");
			hmcLogin.get(1).put("AUT_TIPO", "B2BCustomer - Cliente B2B");
			hmcLogin.get(1).put("AUT_UNIDADE_B2B_PADRAO", "0035_LMStore");
			hmcLogin.get(1).put("AUT_DEPARTAMENTO", "50000425-PROJETO 3D VENDA ASSISTIDA");
			hmcLogin.get(1).put("AUT_CODIGO_CATEGORIA", "999");
			hmcLogin.get(1).put("AUT_CODIGO_DEPARTAMENTO", "50000425");
			hmcLogin.get(1).put("AUT_GESTOR", "51017672");
			hmcLogin.get(1).put("AUT_LOJA", "0035_LMStore");
			hmcLogin.get(1).put("AUT_PERFIL_ACESSO", AUT_HMC_PERFIL_ACESSO.USUARIO_LOJA.name());


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


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00000_CN000003_CTP00001
			/**
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00003_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00003_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00003_CTP00001.get(1).put("AUT_USER","55708317");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00003_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00003_CTP00001.get(1).put("AUT_CODIGO_ITEM","89407066");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00003_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370");
			

			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00003_CTP00001.get(1).put("AUT_TELEFONE_SERVIÃ‡O", "1187272345");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00003_CTP00001.get(1).put("AUT_CNPJ", AUTProjectsFunctions.gerarCNPJ());
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00003_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00003_CTP00001.get(1).put("AUT_TIPO_CADASTRO", AUT_VA_CADASTROS.JURIDICA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00003_CTP00001.get(1).put("AUT_CNPJ", cnpj);
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00003_CTP00001.get(1).put("AUT_FANTASIA", "Teste Fantasia");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00003_CTP00001.get(1).put("AUT_NOME", "AUT NOME PF: ".concat(cnpj));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00003_CTP00001.get(1).put("AUT_NOME_COMPLETO", "AUT NOME COMPLETO: ".concat(AUTProjectsFunctions.gerarCPF()));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00003_CTP00001.get(1).put("AUT_NOME_PJ", "AUT NOME PJ: ".concat(cnpj));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00003_CTP00001.get(1).put("AUT_EMAIL", "aut.qaemail@automation.com");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00003_CTP00001.get(1).put("AUT_INCRICAO_ESTADUAL", "135.540.076.562");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00003_CTP00001.get(1).put("AUT_INCRICAO_ESTADUAL_PF", AUTProjectsFunctions.gerarEstrangeiro());	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00003_CTP00001.get(1).put("AUT_INCRICAO_MUNICIPAL", AUTProjectsFunctions.gerarEstrangeiro());	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00003_CTP00001.get(1).put("AUT_NOME_PJ_CONTATO", "NOME CONTATO PJ".concat(AUTProjectsFunctions.gerarCPF()));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00003_CTP00001.get(1).put("AUT_PJ_EMAIL_CONTATO", "EMAIL.QA@TESTE.COM.BR".concat(AUTProjectsFunctions.gerarCNPJ()));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00003_CTP00001.get(1).put("AUT_PJ_DEPARTAMENTO_CONTATO", "QUALIDADE".concat(AUTProjectsFunctions.gerarCPF()));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00003_CTP00001.get(1).put("AUT_TIPO_TELEFONE","CELULAR");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00003_CTP00001.get(1).put("AUT_NUMERO_TELEFONE", "11966447035");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00003_CTP00001.get(1).put("AUT_NUMERO_TELEFONE_2", "1196306-3067");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00003_CTP00001.get(1).put("AUT_UF_PESQUISA", AUT_VA_ESTADOS.SP);	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00003_CTP00001.get(1).put("AUT_CIDADE_PESQUISA", "São Paulo");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00003_CTP00001.get(1).put("AUT_ENDERECO_PESQUISA", "Avenida Morumbi");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00003_CTP00001.get(1).put("AUT_BAIRRO_PESQUISA", "Morumbi");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00003_CTP00001.get(1).put("AUT_TIPO_ENDERECO", AUT_VA_TIPO_ENDERECO.OBRA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00003_CTP00001.get(1).put("AUT_CEP", "05606-200");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00003_CTP00001.get(1).put("AUT_RUA_ENDERECO", "Avenida Morumbi");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00003_CTP00001.get(1).put("AUT_NUMERO_ENDERECO", "256");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00003_CTP00001.get(1).put("AUT_BAIRRO_ENDERECO", "Morumbi");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00003_CTP00001.get(1).put("AUT_COMPLEMENTO_ENDERECO", "CASA 256");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00003_CTP00001.get(1).put("AUT_CIDADE_ENDERECO", "São Paulo");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00003_CTP00001.get(1).put("AUT_ESTADO_ENDERECO", AUT_VA_ESTADOS.SP);	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00003_CTP00001.get(1).put("AUT_REFERENCIA_ENDERECO","ACOUGUE DA ESQUINA");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00003_CTP00001.get(1).put("AUT_TIPO_IMOVEL_RESIDENCIA", AUT_VA_TIPO_RESIDENCIA.RURAL_CHACARA_FAZENDA_OU_SITIO);	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00003_CTP00001.get(1).put("AUT_NASCIMENTO", "26/11/1994");			
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00003_CTP00001.get(1).put("AUT_NUMERO_PEDIDO", "");


			
			
			//Ajustar dados desse dataflow
			
			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00000_CN00003_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00003_CTP00001);

					
						
			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00000_CN000004_CTP00001
			/**
			 * 
			 * Parametros para configuraÃ§Ã£o de componente cadastro PF
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_USER","55708317");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_CODIGO_ITEM","89407066");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_IMEDIATA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_NUMERO_PEDIDO", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_TIPO_CADASTRO", AUT_VA_CADASTROS.JURIDICA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_PASSAPORTE", codEstrangeiro);
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_CNPJ", cnpj);
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_CPF", cpf);
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_CPF_ESTRANGEIRO", cpfEstrangeiro);
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_NOME_ESTRANGEIRO", String.format("AUT NOME ESTRANG: %s : CPF EST: %s",codEstrangeiro,cpfEstrangeiro));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_NOME", "AUT NOME PF: ".concat(cpf));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_NOME_COMPLETO", "AUT NOME COMPLETO: ".concat(cpf));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_NOME_PJ", "AUT NOME PJ: ".concat(cnpj));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_NOME_PJ_FANTASIA", "AUT NOME PJ:".concat(AUTProjectsFunctions.gerarEstrangeiro()));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_NOME_PJ_FANTASIA2", "AUT NOME PJ 2:".concat(AUTProjectsFunctions.gerarEstrangeiro()));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_EMAIL", "aut.qaemail@automation.com");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_INCRICAO_ESTADUAL", inscricaoEstadual);
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_INCRICAO_ESTADUAL_PF", inscricaoEstadual);	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_INCRICAO_MUNICIPAL", inscricaoEstadual);	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_NOME_PJ_CONTATO", "NOME CONTATO PJ");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_PJ_EMAIL_CONTATO", "EMAIL.QA@TESTE.COM.BR");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_PJ_DEPARTAMENTO_CONTATO", "QUALIDADE");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_TIPO_TELEFONE", AUT_VA_TIPO_CONTATO.CELULAR.name());	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_NUMERO_TELEFONE", "11966447035");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_NUMERO_TELEFONE_2", "1196306-3067");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_UF_PESQUISA", AUT_VA_ESTADOS.PR);	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_CIDADE_PESQUISA", "BOCAIÚVA");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_ENDERECO_PESQUISA", "RUA CIDADE JARDIM");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_BAIRRO_PESQUISA", "Alto");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_TIPO_ENDERECO", AUT_VA_TIPO_ENDERECO.OBRA);
			                                             
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_CEP", "82590-100");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_RUA_ENDERECO", "BR116");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_NUMERO_ENDERECO", "256");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_BAIRRO_ENDERECO", "Alto");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_COMPLEMENTO_ENDERECO", "CASA 123");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_CIDADE_ENDERECO", "Curitiba");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_ESTADO_ENDERECO", AUT_VA_ESTADOS.PR);	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_REFERENCIA_ENDERECO", "ACOUGUE DA ESQUINA");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_TIPO_IMOVEL_RESIDENCIA", AUT_VA_TIPO_RESIDENCIA.RURAL_CHACARA_FAZENDA_OU_SITIO);	
                                                         
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_CEP_2", "82590-100");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_RUA_ENDERECO_2", "BR116");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_NUMERO_ENDERECO_2", "256");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_BAIRRO_ENDERECO_2", "Centro");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_COMPLEMENTO_ENDERECO_2", "CASA 123");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_CIDADE_ENDERECO_2", "Osasco");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_ESTADO_ENDERECO_2", AUT_VA_ESTADOS.PR);	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_REFERENCIA_ENDERECO_2", "Loja Sabaroa");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_TIPO_IMOVEL_RESIDENCIA_2", AUT_VA_TIPO_RESIDENCIA.LOJA_OU_SOBRELOJA);	
                                                         
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_CNPJ_INVALIDO", "37.764.388/0009-90");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_CPF_INVALIDO", "510.354.523-93");	

			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.get(1).put("AUT_CEP_INVALIDO", "12345678");
			
			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00004_CTP00001);


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00000_CN000005_CTR00001
			/**
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de cadastro pessoa estrangeira
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_USER", "55000045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_CODIGO_ITEM_VA","89296193");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "18.783.107/6918-63");  //  4294271183
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_CPF_ESTRANGEIRO", cpfEstrangeiro);
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_SEXO", "Feminino");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_NASCIMENTO", "04/03/1990");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_NOME_ESTRANGEIRO", String.format("AUT NOME ESTRANG: %s : CPF EST: %s",codEstrangeiro,cpfEstrangeiro));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_NOME", "AUT NOME PF: ".concat(cpf));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_NOME_COMPLETO", "AUT NOME COMPLETO: ".concat(AUTProjectsFunctions.gerarCPF()));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_NOME_PJ", "AUT NOME PJ: ".concat(cnpj));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_NOME_PJ_FANTASIA", "AUT NOME PJ:".concat(AUTProjectsFunctions.gerarEstrangeiro()));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_NOME_PJ_FANTASIA2", "AUT NOME PJ 2:".concat(AUTProjectsFunctions.gerarEstrangeiro()));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_EMAIL", "aut.qaemail@automation.com");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_INCRICAO_ESTADUAL", "474.018.412.567");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_INCRICAO_ESTADUAL_PF", AUTProjectsFunctions.gerarEstrangeiro());	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_INCRICAO_MUNICIPAL", AUTProjectsFunctions.gerarEstrangeiro());	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_NOME_PJ_CONTATO", "NOME CONTATO PJ".concat(AUTProjectsFunctions.gerarCPF()));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_PJ_EMAIL_CONTATO", "EMAIL.QA@TESTE.COM.BR".concat(AUTProjectsFunctions.gerarCPF()));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_PJ_DEPARTAMENTO_CONTATO", "QUALIDADE".concat(AUTProjectsFunctions.gerarCPF()));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_TIPO_TELEFONE", AUT_VA_TIPO_CONTATO.CELULAR.name());	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_NUMERO_TELEFONE", "11966447035");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_NUMERO_TELEFONE_2", "1196306-3067");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_UF_PESQUISA", AUT_VA_ESTADOS.SP);	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_CIDADE_PESQUISA", "CAJAMAR");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_ENDERECO_PESQUISA", "RUA PREFEITO ANTONIO GARRIDO");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_BAIRRO_PESQUISA", "JORDANÃ‰SIA");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_TIPO_ENDERECO", AUT_VA_TIPO_ENDERECO.OBRA);

			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_CEP", "07776-000");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_RUA_ENDERECO", "Rua Explanada");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_NUMERO_ENDERECO", "256");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_BAIRRO_ENDERECO", "EXPLANADA");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_COMPLEMENTO_ENDERECO", "CASA 123");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_CIDADE_ENDERECO", "BOCAIÃšVA");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_ESTADO_ENDERECO", AUT_VA_ESTADOS.MG);	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_REFERENCIA_ENDERECO", "ACOUGUE DA ESQUINA");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_TIPO_IMOVEL_RESIDENCIA", AUT_VA_TIPO_RESIDENCIA.RURAL_CHACARA_FAZENDA_OU_SITIO);	

			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_CEP_2", "06013-006");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_RUA_ENDERECO_2", "Rua AntÃ´nio AgÃº");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_NUMERO_ENDERECO_2", "256");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_BAIRRO_ENDERECO_2", "Centro");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_COMPLEMENTO_ENDERECO_2", "CASA 123");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_CIDADE_ENDERECO_2", "Osasco");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_ESTADO_ENDERECO_2", AUT_VA_ESTADOS.SP);	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_REFERENCIA_ENDERECO_2", "Loja Sabaroa");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_TIPO_IMOVEL_RESIDENCIA_2", AUT_VA_TIPO_RESIDENCIA.LOJA_OU_SOBRELOJA);	

			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_CNPJ_INVALIDO", "37.764.388/0009-90");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_CPF_INVALIDO", "510.354.523-93");	

			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.get(1).put("AUT_CEP_INVALIDO", "12345678");

			
			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00005_CTP00001);

			
			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP1
			/**
			 * 
			 * Parametros para configuraÃ§Ã£o de componente cadastro PF Varios Telefones
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001.get(1).put("AUT_USER", "55000045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001.get(1).put("AUT_PASSWORD", "1234");
//			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
//			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001.get(1).put("AUT_CODIGO_ITEM_VA","89296193");
//			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");  //  4294271183
//			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO.toString());
//			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
//			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
//			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
//			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001.get(1).put("AUT_TELEFONE_SERVIÃ‡O", "1187272345");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001.get(1).put("AUT_CPF", AUTProjectsFunctions.gerarCPF());
//			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001.get(1).put("AUT_TIPO_CADASTRO", AUT_VA_CADASTROS.JURIDICA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001.get(1).put("AUT_CPF", cpf);
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001.get(1).put("AUT_NOME", "AUT NOME PF: ".concat(cpf));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001.get(1).put("AUT_NOME_COMPLETO", "AUT NOME COMPLETO: ".concat(AUTProjectsFunctions.gerarCPF()));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001.get(1).put("AUT_NOME_PJ", "AUT NOME PJ: ".concat(cnpj));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001.get(1).put("AUT_EMAIL", "aut.qaemail@automation.com");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001.get(1).put("AUT_INCRICAO_ESTADUAL", "474.018.412.567");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001.get(1).put("AUT_INCRICAO_ESTADUAL_PF", AUTProjectsFunctions.gerarEstrangeiro());	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001.get(1).put("AUT_INCRICAO_MUNICIPAL", AUTProjectsFunctions.gerarEstrangeiro());	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001.get(1).put("AUT_NOME_PJ_CONTATO", "NOME CONTATO PJ".concat(AUTProjectsFunctions.gerarCPF()));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001.get(1).put("AUT_PJ_EMAIL_CONTATO", "EMAIL.QA@TESTE.COM.BR".concat(AUTProjectsFunctions.gerarCPF()));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001.get(1).put("AUT_PJ_DEPARTAMENTO_CONTATO", "QUALIDADE".concat(AUTProjectsFunctions.gerarCPF()));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001.get(1).put("AUT_TIPO_TELEFONE", AUT_VA_TIPO_CONTATO.CELULAR);
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001.get(1).put("AUT_TIPO_TELEFONE2", AUT_VA_TIPO_CONTATO.TELEFONE_FIXO);	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001.get(1).put("AUT_NUMERO_TELEFONE", "11966447035");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001.get(1).put("AUT_NUMERO_TELEFONE2", "1196306-3067");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001.get(1).put("AUT_UF_PESQUISA", AUT_VA_ESTADOS.SP);	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001.get(1).put("AUT_CIDADE_PESQUISA", "Sao Bernado do campo");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001.get(1).put("AUT_ENDERECO_PESQUISA", "Avenida Brigadeiro Faria Lima");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001.get(1).put("AUT_NUMERO_ENDERECO", "845");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001.get(1).put("AUT_BAIRRO_PESQUISA", "CENTRO");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001.get(1).put("AUT_CIDADE_ENDERECO", "Sao Bernado do campo");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001.get(1).put("AUT_TIPO_ENDERECO", AUT_VA_TIPO_ENDERECO.OBRA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001.get(1).put("AUT_CEP", "09720-971");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001.get(1).put("AUT_RUA_ENDERECO", "Rua Explanada");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001.get(1).put("AUT_NUMERO_ENDERECO", "84");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001.get(1).put("AUT_BAIRRO_ENDERECO", "CENTRO");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001.get(1).put("AUT_COMPLEMENTO_ENDERECO", "CASA 123");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001.get(1).put("AUT_CIDADE_ENDERECO", "BOCAIVA");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001.get(1).put("AUT_ESTADO_ENDERECO", AUT_VA_ESTADOS.MG);	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001.get(1).put("AUT_REFERENCIA_ENDERECO", "ACOUGUE DA ESQUINA");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001.get(1).put("AUT_TIPO_IMOVEL_RESIDENCIA", AUT_VA_TIPO_RESIDENCIA.RURAL_CHACARA_FAZENDA_OU_SITIO);	
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001.get(1).put("AUT_NASCIMENTO", "26/11/1994");			
			
			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00006_CTP00001);
			
			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00000_CN000008_CTP00001
			/**
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00008_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00008_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00008_CTP00001.get(1).put("AUT_USER","55708317");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00008_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00008_CTP00001.get(1).put("AUT_CODIGO_ITEM","89407066");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00008_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370");
			

			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00008_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00008_CTP00001.get(1).put("AUT_TIPO_CADASTRO", AUT_VA_CADASTROS.JURIDICA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00008_CTP00001.get(1).put("AUT_CNPJ", "10.943.648/0001-55");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00000_CN00008_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00008_CTP00001);
			
			

			//Inclusao de tabela - vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00013_CTP00001
			/**
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00013_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00013_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00013_CTP00001.get(1).put("AUT_USER","55000045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00013_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00013_CTP00001.get(1).put("AUT_CODIGO_ITEM","89407066");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00013_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370");
			

			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00013_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00013_CTP00001.get(1).put("AUT_TIPO_CADASTRO", AUT_VA_CADASTROS.JURIDICA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00013_CTP00001.get(1).put("AUT_CNPJ", "10.943.648/0001-55");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00000_CN00013_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00013_CTP00001);
			//-------------------------------------------------------------------------------
			//Inclusao de tabela - vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00014_CTP00001
			/**
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00014_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00014_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00014_CTP00001.get(1).put("AUT_USER","55000045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00014_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00014_CTP00001.get(1).put("AUT_CODIGO_ITEM","89407066");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00014_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370");
			

			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00014_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00014_CTP00001.get(1).put("AUT_TIPO_CADASTRO", AUT_VA_CADASTROS.JURIDICA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00014_CTP00001.get(1).put("AUT_CNPJ", "10.943.648/0001-55");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00000_CN00014_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00014_CTP00001);
			//--------------------------------------------------------------------------------------------
			//Inclusao de tabela - vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00015_CTP00001
			/**
			 * 
			 * Parametros para configuraÃ§Ã£o de componente de Televendas do sistema VA
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00015_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00015_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00015_CTP00001.get(1).put("AUT_USER","55000045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00015_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00015_CTP00001.get(1).put("AUT_CODIGO_ITEM","89407066");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00015_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370");
			

			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00015_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00015_CTP00001.get(1).put("AUT_TIPO_CADASTRO", AUT_VA_CADASTROS.JURIDICA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00015_CTP00001.get(1).put("AUT_NOME_PJ_FANTASIA2", "EIT");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00015_CTP00001.get(1).put("AUT_NOME_COMPLETO","LUIZIANA");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00015_CTP00001.get(1).put("AUT_EMAIL", "luziana@gmail.com");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00015_CTP00001.get(1).put("AUT_NUMERO_TELEFONE_2", "11 982020206");
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00015_CTP00001.get(1).put("AUT_TIPO_TELFONE", AUT_VA_TIPO_CONTATO.CELULAR.name());
			vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00015_CTP00001.get(1).put("AUT_CNPJ", "10.943.648/0001-55");
			
			
			
			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00000_CN00015_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00000_CN00014_CTP00001);
			
			//------------------------------------------------------------------------------------------------


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

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTR00001.get(1).put("AUT_USER","55708317");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTR00001.get(1).put("AUT_CODIGO_ITEM","89407066");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTR00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_IMEDIATA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTR00001.get(1).put("AUT_NUMERO_PEDIDO", "");
			
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

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTP00001.get(1).put("AUT_USER", "55708317");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370");  //  4294271183
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTP00001.get(1).put("AUT_TELEFONE_SERVIÃ‡O", "1187272345");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00001_CTP00001.get(1).put("AUT_NUMERO_PEDIDO", "");



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

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00002_CTP00001.get(1).put("AUT_USER", "55708317");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00002_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00002_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00002_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00002_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00002_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
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

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00003_CTP00001.get(1).put("AUT_USER", "55708317");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00003_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00003_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00003_CTP00001.get(1).put("AUT_CODIGO_ITEM_VA","89296193");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00003_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM","1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00003_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00003_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00003_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00003_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00003_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00003_CTP00001.get(1).put("AUT_TELEFONE_SERVIÃ‡O", "1187272345");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00003_CTP00001.get(1).put("AUT_TIPO_FRETE",AUT_VA_TIPO_FRETE.NORMAL);                               
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00003_CTP00001.get(1).put("AUT_HORARIO","MANHÂ");                
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00003_CTP00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0035);

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

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00004_CTP00001.get(1).put("AUT_USER", "55708317");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00004_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00004_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00004_CTP00001.get(1).put("AUT_CODIGO_ITEM_VA","89296193");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00004_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00004_CTP00001.get(1).put("AUT_DESCONTO", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00004_CTP00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00004_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00004_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00004_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00004_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00004_CTP00001.get(1).put("AUT_TIPO_DESCONTO",br.lry.components.va.cat010.AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00004_CTP00001.get(1).put("AUT_TELEFONE_SERVIÇO", "1187272345");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00004_CTP00001.get(1).put("AUT_TIPO_FRETE",AUT_VA_TIPO_FRETE.NORMAL);  
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00004_CTP00001.get(1).put("AUT_HORARIO","MANHÂ");          
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00004_CTP00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0035);
			
			
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

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTR00001.get(1).put("AUT_USER", "55708317");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTR00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTR00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_IMEDIATA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTR00001.get(1).put("AUT_NUMERO_CARTAO", "4000000000000044");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTR00001.get(1).put("AUT_NOME_TITULAR", "Marcos Paulo de Oliveira");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTR00001.get(1).put("AUT_VALIDADE", "09/23");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTR00001.get(1).put("AUT_CODIGO_CARTAO", "183");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTR00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTR00001.get(1).put("AUT_NUMERO_CARRINHO","");
			
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

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTP00001.get(1).put("AUT_USER", "55708317");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00005_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_IMEDIATA);
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

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTR00001.get(1).put("AUT_USER", "55708317");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTR00001.get(1).put("AUT_CODIGO_ITEM","89411714");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO); //LM_0035_CURITIBA
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTR00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_IMEDIATA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTR00001.get(1).put("AUT_NUMERO_CARTAO", "5000000000000001");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTR00001.get(1).put("AUT_NOME_TITULAR", "Marcos Paulo de Oliveira");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTR00001.get(1).put("AUT_VALIDADE", "04/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTR00001.get(1).put("AUT_CODIGO_CARTAO", "123");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTR00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTR00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0045);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTR00001.get(1).put("AUT_USER_APROVADOR","55230148");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTR00001.get(1).put("AUT_TIPO_FRETE",AUT_VA_TIPO_FRETE.NORMAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTR00001.get(1).put("AUT_HORARIO","MANHÂ");
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

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTP00001.get(1).put("AUT_USER", "55708317");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_IMEDIATA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTP00001.get(1).put("AUT_NUMERO_CARTAO", "4000000000000044");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTP00001.get(1).put("AUT_NOME_TITULAR", "Marcos Paulo de Oliveira");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTP00001.get(1).put("AUT_VALIDADE", "09/23");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTP00001.get(1).put("AUT_CODIGO_CARTAO", "183");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTP00001.get(1).put("AUT_NUMERO_CARRINHO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00006_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			
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

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00007_CTP00001.get(1).put("AUT_USER", "55708317");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00007_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00007_CTP00001.get(1).put("AUT_CODIGO_ITEM","89296193;\n 89296193;\n 89296193");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00007_CTP00001.get(1).put("AUT_CODIGO_ITEM_VA","89296193");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00007_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00007_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0019_PORTO_ALEGRE.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00007_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_IMEDIATA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00007_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00007_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00007_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00007_CTP00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0019);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00007_CTP00001.get(1).put("AUT_TIPO_FRETE",AUT_VA_TIPO_FRETE.NORMAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00007_CTP00001.get(1).put("AUT_HORARIO","MANHÂ");


			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00007_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00007_CTP00001);



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

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTP00001.get(1).put("AUT_USER", "55708317");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTP00001.get(1).put("AUT_CODIGO_ITEM","89407066");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_IMEDIATA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTP00001);



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

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00009_CTP00001.get(1).put("AUT_USER", "55000045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00009_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00009_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00009_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370");  //  4294271183
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00009_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00009_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00009_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00009_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00009_CTP00001.get(1).put("AUT_TELEFONE_SERVIÃ‡O", "1187272345");

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00009_CTP00001.get(1).put("AUT_INDEX_GARANTIA","1");
			
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
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_CODIGO_ITEM","89407066"); //89455163
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_CODIGO_ITEM_VA","89296193");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");  //  4294271183
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_INDEX_GARANTIA","1");
			
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_TELEFONE_SERVIÃ‡O", "1187272345");
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_CPF", AUTProjectsFunctions.gerarCPF());
//
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_TIPO_CADASTRO", AUT_VA_CADASTROS.JURIDICA);
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_CPF", cpf);
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_NOME", "AUT NOME PF: ".concat(cpf));	
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_NOME_COMPLETO", "AUT NOME COMPLETO: ".concat(AUTProjectsFunctions.gerarCPF()));	
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_NOME_PJ", "AUT NOME PJ: ".concat(cnpj));	
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_EMAIL", "aut.qaemail@automation.com");	
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_INCRICAO_ESTADUAL", "474.018.412.567");
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_INCRICAO_ESTADUAL_PF", AUTProjectsFunctions.gerarEstrangeiro());	
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_INCRICAO_MUNICIPAL", AUTProjectsFunctions.gerarEstrangeiro());	
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_NOME_PJ_CONTATO", "NOME CONTATO PJ".concat(AUTProjectsFunctions.gerarCPF()));	
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_PJ_EMAIL_CONTATO", "EMAIL.QA@TESTE.COM.BR".concat(AUTProjectsFunctions.gerarCPF()));	
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_PJ_DEPARTAMENTO_CONTATO", "QUALIDADE".concat(AUTProjectsFunctions.gerarCPF()));	
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_TIPO_TELEFONE", AUT_VA_TIPO_CONTATO.CELULAR.name());	
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_NUMERO_TELEFONE", "11966447035");	
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_NUMERO_TELEFONE_2", "1196306-3067");	
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_UF_PESQUISA", AUT_VA_ESTADOS.SP);	
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_CIDADE_PESQUISA", "CAJAMAR");
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_ENDERECO_PESQUISA", "RUA PREFEITO ANTONIO GARRIDO");
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_BAIRRO_PESQUISA", "JORDANÃ‰SIA");	
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_TIPO_ENDERECO", AUT_VA_TIPO_ENDERECO.OBRA);
//
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_CEP", "07776-000");	
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_RUA_ENDERECO", "Rua Explanada");	
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_NUMERO_ENDERECO", "256");	
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_BAIRRO_ENDERECO", "EXPLANADA");	
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_COMPLEMENTO_ENDERECO", "CASA 123");	
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_CIDADE_ENDERECO", "BOCAIÃšVA");	
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_ESTADO_ENDERECO", AUT_VA_ESTADOS.MG);	
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_REFERENCIA_ENDERECO", "ACOUGUE DA ESQUINA");	
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00010_CTP00001.get(1).put("AUT_TIPO_IMOVEL_RESIDENCIA", AUT_VA_TIPO_RESIDENCIA.RURAL_CHACARA_FAZENDA_OU_SITIO);	
//

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

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00011_CTP00001.get(1).put("AUT_USER","55708317");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00011_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00011_CTP00001.get(1).put("AUT_CODIGO_ITEM","89407066");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00011_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00011_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_AGENDADA);
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

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00013_CTP00001.get(1).put("AUT_USER", "55708317");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00013_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00013_CTP00001.get(1).put("AUT_CODIGO_ITEM","89576445");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00013_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370");  //  4294271183
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00013_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO.toString());
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

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00014_CTP00001.get(1).put("AUT_USER","55708317");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00014_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00014_CTP00001.get(1).put("AUT_CODIGO_ITEM","89576445");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00014_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00014_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00014_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00014_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00014_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00014_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00014_CTP00001);



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

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTR00001.get(1).put("AUT_USER","55708317"); //55000035
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTR00001.get(1).put("AUT_CODIGO_ITEM","89232220"); //89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370");//42942711833
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0019_PORTO_ALEGRE); //LM_0035_CURITIBA
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTR00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTR00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTR00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0019);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTR00001.get(1).put("AUT_TIPO_FRETE",AUT_VA_TIPO_FRETE.NORMAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTR00001.get(1).put("AUT_HORARIO","MANHÂ");
			
			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTR00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00008_CTR00001);





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

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00012_CTP00001.get(1).put("AUT_USER", "55708317"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00012_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00012_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00012_CTP00001.get(1).put("AUT_CODIGO_ITEM", "89407066"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00012_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00012_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.REAIS);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00012_CTP00001.get(1).put("AUT_DESCONTO", "20,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00012_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00012_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
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

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00015_CTP00001.get(1).put("AUT_USER", "55000119"); //55708317
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00015_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00015_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00015_CTP00001.get(1).put("AUT_CODIGO_ITEM", "89232220"); //89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00015_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00015_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.REAIS);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00015_CTP00001.get(1).put("AUT_DESCONTO", "9");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00015_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ESPECIAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00015_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_IMEDIATA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00015_CTP00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00015_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00015_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00015_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00015_CTP00001);


			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00016_CTP00001
			/**
			 * 
			 * 
			 * 
			 * Parametros para configuraÃ§Ã£o de componentes de Pedidos do sistema VA
			 * 
			 * 
			 */

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00016_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00016_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00016_CTP00001.get(1).put("AUT_USER", "55708317"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00016_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00016_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00016_CTP00001.get(1).put("AUT_CODIGO_ITEM", "85617833"); //89407066 89407066 85617833
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00016_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00016_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.REAIS);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00016_CTP00001.get(1).put("AUT_DESCONTO", "700,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00016_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ESPECIAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00016_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_IMEDIATA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00016_CTP00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00016_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00016_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00016_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00016_CTP00001);
			
			
			
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

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00017_CTP00001.get(1).put("AUT_USER", "55708317"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00017_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00017_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00017_CTP00001.get(1).put("AUT_CODIGO_ITEM", "85617833"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00017_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00017_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00017_CTP00001.get(1).put("AUT_DESCONTO", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00017_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ESPECIAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00017_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_IMEDIATA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00017_CTP00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00017_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00017_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00017_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			
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

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00018_CTP00001.get(1).put("AUT_USER", "55708317"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00018_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00018_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00018_CTP00001.get(1).put("AUT_CODIGO_ITEM", "85617833"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00018_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00018_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00018_CTP00001.get(1).put("AUT_DESCONTO", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00018_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00018_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_IMEDIATA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00018_CTP00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00018_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00018_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00018_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			
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

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00019_CTP00001.get(1).put("AUT_USER", "55000045"); //5576316456068   55001063   550522446 55300745
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00019_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00019_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00019_CTP00001.get(1).put("AUT_QUANTIDADE_ITENS", 2);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00019_CTP00001.get(1).put("AUT_CODIGO_ITEM", "85617833"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00019_CTP00001.get(1).put("AUT_CODIGO_ITEM_VA", "85617833"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00019_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00019_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00019_CTP00001.get(1).put("AUT_DESCONTO", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00019_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.SALDO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00019_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_IMEDIATA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00019_CTP00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00019_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00019_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00019_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");

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

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.get(1).put("AUT_USER", "55708317"); //55300745 5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.get(1).put("AUT_QUANTIDADE_ITENS", 1);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.get(1).put("AUT_CODIGO_ITEM_VA", "85617833"); 
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.get(1).put("AUT_CODIGO_ITEM", "85617833"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.REAIS);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.get(1).put("AUT_DESCONTO", "20,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_IMEDIATA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00020_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");


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

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_USER", "55000045"); //55708317 
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_QUANTIDADE_ITENS", 2);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_CODIGO_ITEM", "89232220"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_CODIGO_ITEM_VA", "89296193");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "82740645422"); //43409000283  94288636370
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.REAIS);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_DESCONTO", "10");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);//aqui
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_TIPO_FRETE",AUT_VA_TIPO_FRETE.NORMAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_HORARIO","MANHÂ");	 
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00021_CTP00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0045);


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

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_USER", "55708317"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_CODIGO_ITEM", "89232220"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.REAIS);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_DESCONTO", "100,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_IMEDIATA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			
			
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

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00023_CTP00001.get(1).put("AUT_USER", "55000119"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00023_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00023_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00023_CTP00001.get(1).put("AUT_CODIGO_ITEM", "89232220"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00023_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00023_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00023_CTP00001.get(1).put("AUT_DESCONTO", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00023_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00023_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_IMEDIATA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00023_CTP00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00023_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00023_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00023_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			
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

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00024_CTP00001.get(1).put("AUT_USER", "55000119"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00024_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00024_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00024_CTP00001.get(1).put("AUT_CODIGO_ITEM", "85617833"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00024_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00024_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.REAIS);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00024_CTP00001.get(1).put("AUT_DESCONTO", "20,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00024_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00024_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_IMEDIATA);
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

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00025_CTP00001.get(1).put("AUT_USER", "55000119"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00025_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00025_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00025_CTP00001.get(1).put("AUT_CODIGO_ITEM", "89232220"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00025_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00025_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00025_CTP00001.get(1).put("AUT_DESCONTO", "100,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00025_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00025_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_IMEDIATA);
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

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00026_CTP00001.get(1).put("AUT_USER", "55000119"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00026_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00026_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00026_CTP00001.get(1).put("AUT_CODIGO_ITEM", "89232220"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00026_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00026_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.REAIS);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00026_CTP00001.get(1).put("AUT_DESCONTO", "100,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00026_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00026_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_IMEDIATA);
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

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00027_CTP00001.get(1).put("AUT_USER", "55708317"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00027_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00027_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00027_CTP00001.get(1).put("AUT_CODIGO_ITEM", "89249286"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00027_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00027_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.REAIS);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00027_CTP00001.get(1).put("AUT_DESCONTO", "200,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00027_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00027_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_IMEDIATA);
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

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00028_CTP00001.get(1).put("AUT_USER", "55708317"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00028_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00028_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00028_CTP00001.get(1).put("AUT_CODIGO_ITEM", "89407066"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00028_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00028_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00028_CTP00001.get(1).put("AUT_DESCONTO", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00028_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00028_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_IMEDIATA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00028_CTP00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00028_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00028_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00028_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			
			
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

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00029_CTP00001.put(1, new java.util.HashMap<String,Object>());

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00029_CTP00001.get(1).put("AUT_USER", "55708317"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00029_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00029_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00029_CTP00001.get(1).put("AUT_CODIGO_ITEM", "89407066"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00029_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00029_CTP00001.get(1).put("AUT_TIPO_DESCONTO", AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00029_CTP00001.get(1).put("AUT_DESCONTO", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00029_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00029_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_IMEDIATA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00029_CTP00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00029_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00029_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00029_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");

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

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00030_CTP00001.get(1).put("AUT_USER", "51030887"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00030_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00030_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00030_CTP00001.get(1).put("AUT_CODIGO_ITEM", "89296193"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00030_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00030_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00030_CTP00001.get(1).put("AUT_DESCONTO", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00030_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00030_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_IMEDIATA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00030_CTP00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00030_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00030_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00030_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");

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
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00032_CTP00001.get(1).put("AUT_CODIGO_ITEM","89296193");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00032_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "82740645422");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00032_CTP00001.get(1).put("AUT_DESCONTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00032_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00032_CTP00001.get(1).put("AUT_DESCONTO", "8");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00032_CTP00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ESPECIAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00032_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00032_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00032_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00032_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00032_CTP00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0035);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00032_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");

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
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00034_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "385261679");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00034_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00034_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00034_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00034_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VISA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00034_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00034_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_MOTIVO_DESCONTO_FRETE.ATRASO_NA_ENTREGA_DE_PEDIDO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00034_CTP00001.get(1).put("AUT_TIPO_DESCONTO","PORCENTAGEM");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00034_CTP00001.get(1).put("AUT_DESCONTO","10");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00034_CTP00001.get(1).put("AUT_NUMERO_CARTAO", "5300332890376075");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00034_CTP00001.get(1).put("AUT_NOME_TITULAR", "Usuario Automacao");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00034_CTP00001.get(1).put("AUT_VALIDADE", "09/18");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00034_CTP00001.get(1).put("AUT_CODIGO_CARTAO", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00034_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00034_CTP00001.get(1).put("AUT_TIPO_FRETE",AUT_VA_TIPO_FRETE.NORMAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00034_CTP00001.get(1).put("AUT_HORARIO","MANHÂ");	 
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00034_CTP00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0035);  

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
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00045_CTP00001.get(1).put("AUT_USER", "55000119"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00045_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00045_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00045_CTP00001.get(1).put("AUT_CODIGO_ITEM", "89232220"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00045_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00045_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_AGENDADA);
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
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00051_CTP00001.get(1).put("AUT_CODIGO_ITEM", "89388684"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00051_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00051_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00051_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00051_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00051_CTP00001.get(1).put("AUT_TIPO_FRETE",AUT_VA_TIPO_FRETE.NORMAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00051_CTP00001.get(1).put("AUT_HORARIO","MANHÂ");	 
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00051_CTP00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0035);  

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
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00031_CTP00001.get(1).put("AUT_CODIGO_ITEM","89232220");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00031_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00031_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00031_CTP00001.get(1).put("AUT_DESCONTO", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00031_CTP00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00031_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00031_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_AGENDADA);
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
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00033_CTP00001.get(1).put("AUT_CODIGO_ITEM","89232220");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00033_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00033_CTP00001.get(1).put("AUT_DESCONTO", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00033_CTP00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00033_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00033_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00033_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_AGENDADA);
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

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00035_CTP00001.get(1).put("AUT_USER", "55000119");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00035_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00035_CTP00001.get(1).put("AUT_CODIGO_ITEM","89232220");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00035_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00035_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.REAIS);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00035_CTP00001.get(1).put("AUT_DESCONTO", "80");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00035_CTP00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00035_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0019_PORTO_ALEGRE.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00035_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_IMEDIATA);
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
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00036_CTP00001.get(1).put("AUT_CODIGO_ITEM","89296193");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00036_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00036_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00036_CTP00001.get(1).put("AUT_DESCONTO", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00036_CTP00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00036_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00036_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_AGENDADA);
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

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00038_CTP00001.get(1).put("AUT_USER", "55230148");  //55000035
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00038_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00038_CTP00001.get(1).put("AUT_CODIGO_ITEM","89232220");//89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00038_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //42942711833
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00038_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00038_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00038_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VISA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00038_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00038_CTP00001.get(1).put("AUT_TELEFONE_SERVICO", "1187272345");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00038_CTP00001.get(1).put("AUT_TIPO_FRETE",AUT_VA_TIPO_FRETE.NORMAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00038_CTP00001.get(1).put("AUT_HORARIO","MANHÂ");	 
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00038_CTP00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0035);  

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

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00040_CTP00001.get(1).put("AUT_USER", "55000119"); //51013346  55000035
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00040_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00040_CTP00001.get(1).put("AUT_CODIGO_ITEM","89232220"); //89391260 89576445
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00040_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370");  //  4294271183    
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00040_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0019_PORTO_ALEGRE.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00040_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00040_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00040_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00040_CTP00001.get(1).put("AUT_TELEFONE_SERVIÃ‡O", "1187272345");

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00040_CTP00001.get(1).put("AUT_TIPO_FRETE",AUT_VA_TIPO_FRETE.NORMAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00040_CTP00001.get(1).put("AUT_HORARIO","MANHÂ");	 
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00040_CTP00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0019);   


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
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00041_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00041_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00041_CTP00001.get(1).put("AUT_TIPO_FRETE", AUTFluxoSaida.AUT_VA_TIP_FRETE.EXPRESSA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00041_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00041_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00041_CTP00001.get(1).put("AUT_HORARIO","MANHÂ");	 
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00041_CTP00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0045);   
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00041_CTP00001.get(1).put("AUT_NUMERO_PEDIDO", "");

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
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00042_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "82740645422");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00042_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00042_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00042_CTP00001.get(1).put("AUT_TIPO_FRETE", AUTFluxoSaida.AUT_VA_TIP_FRETE.ECONOMICA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00042_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00042_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X);
		
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00042_CTP00001.get(1).put("AUT_TIPO_FRETE",AUT_VA_TIPO_FRETE.NORMAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00042_CTP00001.get(1).put("AUT_HORARIO","MANHÂ");	 
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00042_CTP00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0045);   
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00042_CTP00001.get(1).put("AUT_NUMERO_PEDIDO", "");


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

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00043_CTP00001.get(1).put("AUT_USER", "55000035"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00043_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00043_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00043_CTP00001.get(1).put("AUT_CODIGO_ITEM", "89407066"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00043_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00043_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_IMEDIATA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00043_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00043_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_2X);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00043_CTP00001.get(1).put("AUT_NUMERO_PEDIDO", "");

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

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00044_CTP00001.get(1).put("AUT_USER", "55000119");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00044_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00044_CTP00001.get(1).put("AUT_CODIGO_ITEM","89232220");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00044_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00044_CTP00001.get(1).put("AUT_DESCONTO", "80,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00044_CTP00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00044_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0019_PORTO_ALEGRE.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00044_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00044_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00044_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00044_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");


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
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00046_CTP00001.get(1).put("AUT_CODIGO_ITEM","89407066"); //85617833
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00046_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00046_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00046_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00046_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VISA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00046_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00046_CTP00001.get(1).put("AUT_NUMERO_CARTAO", "4000 0000 0000 0044");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00046_CTP00001.get(1).put("AUT_NOME_TITULAR", "Usuario Automacao");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00046_CTP00001.get(1).put("AUT_VALIDADE", "05/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00046_CTP00001.get(1).put("AUT_CODIGO_CARTAO", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00046_CTP00001.get(1).put("AUT_CODE_VALE_TROCA","00620723");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00046_CTP00001.get(1).put("AUT_TIPO_FRETE",AUT_VA_TIPO_FRETE.NORMAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00046_CTP00001.get(1).put("AUT_HORARIO","MANHÂ");	 
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00046_CTP00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0045);   
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00046_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");

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

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00047_CTP00001.get(1).put("AUT_USER", "55001063"); //55001063 11013231
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00047_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00047_CTP00001.get(1).put("AUT_CODIGO_ITEM","89407066"); //85617833
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00047_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00047_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00047_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00047_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.MASTERCARD);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00047_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00047_CTP00001.get(1).put("AUT_NUMERO_CARTAO", "5000000000000001");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00047_CTP00001.get(1).put("AUT_NOME_TITULAR", "Usuario Automacao");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00047_CTP00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00047_CTP00001.get(1).put("AUT_CODIGO_CARTAO", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00047_CTP00001.get(1).put("AUT_CODE_VALE_TROCA","00620723");		
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00047_CTP00001.get(1).put("AUT_TIPO_FRETE",AUT_VA_TIPO_FRETE.NORMAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00047_CTP00001.get(1).put("AUT_HORARIO","MANHÂ");	 
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00047_CTP00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0035);   
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00047_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");


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
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00048_CTP00001.get(1).put("AUT_CODIGO_ITEM","89296193");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00048_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00048_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00048_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00048_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CHEQUE);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00048_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00048_CTP00001.get(1).put("AUT_NUMERO_CARTAO", "5300332890376075");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00048_CTP00001.get(1).put("AUT_NOME_TITULAR", "Usuario Automação");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00048_CTP00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00048_CTP00001.get(1).put("AUT_CODIGO_CARTAO", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00048_CTP00001.get(1).put("AUT_CODE_VALE_TROCA","00620723");			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00048_CTP00001.get(1).put("AUT_TIPO_FRETE",AUT_VA_TIPO_FRETE.NORMAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00048_CTP00001.get(1).put("AUT_HORARIO","MANHÂ");	 
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00048_CTP00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0035);   
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00048_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");

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

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00049_CTP00001.get(1).put("AUT_USER", "11013200"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00049_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00049_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00049_CTP00001.get(1).put("AUT_CODIGO_ITEM", "89232220"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00049_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "68691640456"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00049_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00049_CTP00001.get(1).put("AUT_DESCONTO", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00049_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00049_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00049_CTP00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00049_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VALE_TROCA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00049_CTP00001.get(1).put("AUT_CODE_VALE_TROCA","00756375");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00049_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00049_CTP00001.get(1).put("AUT_NUMERO_PEDIDO", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00049_CTP00001.get(1).put("AUT_TIPO_FRETE",AUT_VA_TIPO_FRETE.NORMAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00049_CTP00001.get(1).put("AUT_HORARIO","MANHÂ");	 
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00049_CTP00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0019);   
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00049_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0019_PORTO_ALEGRE.toString());
		
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

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00050_CTP00001.get(1).put("AUT_USER", "11013200"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00050_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00050_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00050_CTP00001.get(1).put("AUT_CODIGO_ITEM", "89232220"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00050_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00050_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00050_CTP00001.get(1).put("AUT_DESCONTO", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00050_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00050_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00050_CTP00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00050_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VOUCHER);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00050_CTP00001.get(1).put("AUT_CODE_VOUCHER","6394422000000293143");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00050_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00050_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0019_PORTO_ALEGRE.toString());

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
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00052_CTP00001.get(1).put("AUT_CODIGO_ITEM","85617833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00052_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM_VA", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00052_CTP00001.get(1).put("AUT_CODIGO_ITEM_VA", "89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00052_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //42942711833
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00052_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00052_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00052_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VISA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00052_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00052_CTP00001.get(1).put("AUT_NUMERO_CARTAO", "5300332890376075");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00052_CTP00001.get(1).put("AUT_NOME_TITULAR", "Usuario Automacao");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00052_CTP00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00052_CTP00001.get(1).put("AUT_CODIGO_CARTAO", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00052_CTP00001.get(1).put("AUT_CODE_VALE_TROCA","00620723");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00052_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00052_CTP00001.get(1).put("AUT_EAN_OR_CODE","89455163");
			
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
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00053_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "82740645422"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00053_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_AGENDADA);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00053_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VISA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00053_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00053_CTP00001.get(1).put("AUT_NUMERO_CARTAO", "5300332890376075");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00053_CTP00001.get(1).put("AUT_NOME_TITULAR", "Usuario Automacao");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00053_CTP00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00053_CTP00001.get(1).put("AUT_CODIGO_CARTAO", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00053_CTP00001.get(1).put("AUT_NUMERO_PEDIDO", ""); 
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00053_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO.toString());

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
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00054_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "82740645422");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00054_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00054_CTP00001.get(1).put("AUT_REGIAO_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_REGIAO.SP_SAO_PAULO_REGIAO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00054_CTP00001.get(1).put("AUT_NUMERO_CARTAO", "5300332890376075");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00054_CTP00001.get(1).put("AUT_NOME_TITULAR", "Usuario Automação");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00054_CTP00001.get(1).put("AUT_VALIDADE", "12/2019");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00054_CTP00001.get(1).put("AUT_CODIGO_CARTAO", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00054_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00054_CTP00001.get(1).put("AUT_CEP", "05424-004");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00054_CTP00001.get(1).put("AUT_RUA_ENDERECO", "Rua ButantÃ£");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00054_CTP00001.get(1).put("AUT_NUMERO_ENDERECO", "225");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00054_CTP00001.get(1).put("AUT_BAIRRO_ENDERECO", "Pinheiros");	

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00054_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00054_CTP00001);	

			//--------------------------------------------------------------------------------------------------------	

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

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00055_CTP00001.get(1).put("AUT_USER", "55001063");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00055_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00055_CTP00001.get(1).put("AUT_CODIGO_ITEM","87489164");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00055_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); 
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00055_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00055_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00055_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VISA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00055_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00055_CTP00001.get(1).put("AUT_NUMERO_CARTAO", "4000000000000044");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00055_CTP00001.get(1).put("AUT_NOME_TITULAR", "Usuario Automacao");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00055_CTP00001.get(1).put("AUT_VALIDADE", "12/2019");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00055_CTP00001.get(1).put("AUT_CODIGO_CARTAO", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00055_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00055_CTP00001.get(1).put("AUT_CEP", "05424-004");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00055_CTP00001.get(1).put("AUT_RUA_ENDERECO", "Rua ButantÃ");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00055_CTP00001.get(1).put("AUT_NUMERO_ENDERECO", "225");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00055_CTP00001.get(1).put("AUT_BAIRRO_ENDERECO", "Pinheiros");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00055_CTP00001.get(1).put("AUT_NUMERO_PEDIDO", "");

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
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");  //  4294271183
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_TELEFONE_SERVIÃ‡O", "1187272345");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_CPF", AUTProjectsFunctions.gerarCPF());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
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
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_TIPO_TELEFONE", AUT_VA_TIPO_CONTATO.CELULAR);	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_NUMERO_TELEFONE", "11966447035");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_NUMERO_TELEFONE_2", "1196306-3067");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_UF_PESQUISA", AUT_VA_ESTADOS.SP);	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_CIDADE_PESQUISA", "Sao Bernado do campo");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_ENDERECO_PESQUISA", "Avenida Brigadeiro Faria Lima");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_NUMERO_ENDERECO", "845");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_BAIRRO_PESQUISA", "CENTRO");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_CIDADE_ENDERECO", "Sao Bernado do campo");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_TIPO_ENDERECO", AUT_VA_TIPO_ENDERECO.OBRA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_CEP", "09720-971");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_RUA_ENDERECO", "Rua Explanada");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_NUMERO_ENDERECO", "84");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_BAIRRO_ENDERECO", "CENTRO");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_COMPLEMENTO_ENDERECO", "CASA 123");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_CIDADE_ENDERECO", "BOCAIVA");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_ESTADO_ENDERECO", AUT_VA_ESTADOS.MG);	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_REFERENCIA_ENDERECO", "ACOUGUE DA ESQUINA");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_TIPO_IMOVEL_RESIDENCIA", AUT_VA_TIPO_RESIDENCIA.RURAL_CHACARA_FAZENDA_OU_SITIO);	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_NASCIMENTO", "26/11/1994");			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00056_CTP00001.get(1).put("AUT_NUMERO_PEDIDO", "");
			
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
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "336.250.296.224");  //  4294271183
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_TELEFONE_SERVIÃ‡O", "1187272345");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_CNPJ", AUTProjectsFunctions.gerarCNPJ());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_TIPO_CADASTRO", AUT_VA_CADASTROS.JURIDICA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_CNPJ", cnpj);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_FANTASIA", AUT_VA_CADASTROS.JURIDICA_ATUALIZACAO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_NOME", "AUT NOME PF: ".concat(cnpj));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_NOME_COMPLETO", "AUT NOME COMPLETO: ".concat(AUTProjectsFunctions.gerarCPF()));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_NOME_PJ", "AUT NOME PJ: ".concat(cnpj));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_EMAIL", "aut.qaemail@automation.com");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_INCRICAO_ESTADUAL", "135.540.076.562");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_INCRICAO_ESTADUAL_PF", AUTProjectsFunctions.gerarEstrangeiro());	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_INCRICAO_MUNICIPAL", AUTProjectsFunctions.gerarEstrangeiro());	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_NOME_PJ_CONTATO", "NOME CONTATO PJ".concat(AUTProjectsFunctions.gerarCPF()));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_PJ_EMAIL_CONTATO", "EMAIL.QA@TESTE.COM.BR".concat(AUTProjectsFunctions.gerarCNPJ()));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_PJ_DEPARTAMENTO_CONTATO", "QUALIDADE".concat(AUTProjectsFunctions.gerarCPF()));	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_TIPO_TELEFONE","CELULAR");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_NUMERO_TELEFONE", "11966447035");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_NUMERO_TELEFONE_2", "1196306-3067");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_UF_PESQUISA", AUT_VA_ESTADOS.SP);	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_CIDADE_PESQUISA", "São Paulo");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_ENDERECO_PESQUISA", "Avenida Morumbi");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_BAIRRO_PESQUISA", "Morumbi");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_TIPO_ENDERECO", AUT_VA_TIPO_ENDERECO.OBRA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_CEP", "05606-200");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_RUA_ENDERECO", "Avenida Morumbi");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_NUMERO_ENDERECO", "256");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_BAIRRO_ENDERECO", "Morumbi");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_COMPLEMENTO_ENDERECO", "CASA 256");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_CIDADE_ENDERECO", "São Paulo");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_ESTADO_ENDERECO", AUT_VA_ESTADOS.SP);	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_REFERENCIA_ENDERECO","ACOUGUE DA ESQUINA");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_TIPO_IMOVEL_RESIDENCIA", AUT_VA_TIPO_RESIDENCIA.RURAL_CHACARA_FAZENDA_OU_SITIO);	
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_NASCIMENTO", "26/11/1994");			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00057_CTP00001.get(1).put("AUT_NUMERO_PEDIDO", "");
			
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
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_CPF_ESTRANGEIRO", cpfEstrangeiro);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_SEXO", "Feminino");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00058_CTP00001.get(1).put("AUT_NASCIMENTO", "04/03/1990");
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
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00059_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "FR1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00059_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00059_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00059_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00059_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VISA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00059_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00059_CTP00001.get(1).put("AUT_NUMERO_CARTAO", "5300332890376075");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00059_CTP00001.get(1).put("AUT_NOME_TITULAR", "Usuario Automacao");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00059_CTP00001.get(1).put("AUT_VALIDADE", "09/18");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00059_CTP00001.get(1).put("AUT_CODIGO_CARTAO", "1234");		
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00059_CTP00001.get(1).put("AUT_TIPO_FRETE",AUT_VA_TIPO_FRETE.NORMAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00059_CTP00001.get(1).put("AUT_HORARIO","MANHÂ");	 
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00059_CTP00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0035);    


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
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00060_CTP00001.get(1).put("AUT_CODIGO_ITEM", " 85617833"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00060_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "FR1234"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00060_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00060_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00060_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00060_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00060_CTP00001.get(1).put("AUT_TIPO_FRETE",AUT_VA_TIPO_FRETE.NORMAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00060_CTP00001.get(1).put("AUT_HORARIO","MANHÂ");	 
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00060_CTP00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0035);   


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
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00070_CTP00001.get(1).put("AUT_CODIGO_ITEM", "85023484"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00070_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "FR1234"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00070_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_AGENDADA);
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

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00071_CTP00001.get(1).put("AUT_USER", "55000045"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00071_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00071_CTP00001.get(1).put("AUT_CODIGO_ITEM","89573176");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00071_CTP00001.get(1).put("AUT_QUANTIDADE_ITENS", 1); 			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00071_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "FR1234"); 
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00071_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00071_CTP00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00071_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00071_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00071_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO);
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00071_CTP00001.get(1).put("AUT_FILIAL_ESTOQUE","0019 - PORTO_ALEGRE");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00071_CTP00001.get(1).put("AUT_NUMERO_PEDIDO", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00071_CTP00001.get(1).put("AUT_QUANTIDADE_ITENS", 2);
			
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
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00072_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00072_CTR00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_AGENDADA);
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

			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00073_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();	

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00073_CTP00001.put(1, new java.util.HashMap<String,Object>());


			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00073_CTP00001.get(1).put("AUT_USER", "55708317");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00073_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00073_CTP00001.get(1).put("AUT_CODIGO_ITEM","85023484");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00073_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00073_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0035_CURITIBA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00073_CTP00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00073_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00073_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA.toString());

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00073_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00073_CTP00001);			//---------------------------------------------------------------------------------------------------------				


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

			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00074_CTP00001.get(1).put("AUT_USER", "55000119"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00074_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00074_CTP00001.get(1).put("AUT_CODIGO_ITEM","89232220"); //89573176
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00074_CTP00001.get(1).put("AUT_QUANTIDADE_ITENS", 1); 			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00074_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "FR1234"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00074_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA); 
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00074_CTP00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00074_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00074_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00074_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0019_PORTO_ALEGRE);
			//vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00074_CTP00001.get(1).put("AUT_FILIAL_ESTOQUE","0019 - PORTO_ALEGRE");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00074_CTP00001.get(1).put("AUT_FRETE_ADICIONAL","SIM");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00074_CTP00001.get(1).put("AUT_TIPO_FRETE",AUT_VA_TIPO_FRETE.NORMAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00074_CTP00001.get(1).put("AUT_HORARIO","MANHÂ");	 
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00074_CTP00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0019); 

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
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00075_CTP00001.get(1).put("AUT_CODIGO_ITEM","89573176");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00075_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00075_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0035_CURITIBA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00075_CTP00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00075_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VISA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00075_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00075_CTP00001.get(1).put("AUT_NUMERO_CARTAO", "4000 0000 0000 0044"); //5300332890376075
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00075_CTP00001.get(1).put("AUT_NOME_TITULAR", "Usuario Automacao");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00075_CTP00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00075_CTP00001.get(1).put("AUT_CODIGO_CARTAO", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00075_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO_2",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VOUCHER);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00075_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO_2",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00075_CTP00001.get(1).put("AUT_CODE_VOUCHER",".*\\d{19}");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00075_CTP00001.get(1).put("AUT_TIPO_FRETE",AUT_VA_TIPO_FRETE.NORMAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00075_CTP00001.get(1).put("AUT_HORARIO","MANHÂ");	 
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00075_CTP00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0035);   

			
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
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00076_CTP00001.get(1).put("AUT_CODIGO_ITEM","89573176");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00076_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00076_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0035_CURITIBA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00076_CTP00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00076_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VOUCHER);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00076_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00076_CTP00001.get(1).put("AUT_CODE_VOUCHER",".*\\d{19}");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00076_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO_2",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VALE_TROCA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00076_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO_2",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00076_CTP00001.get(1).put("AUT_CODE_VALE_TROCA",".*\\d{8}");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00076_CTP00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0035);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00076_CTP00001.get(1).put("AUT_TIPO_FRETE", AUTFluxoSaida.AUT_VA_TIP_FRETE.ECONOMICA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00076_CTP00001.get(1).put("AUT_TIPO_FRETE",AUT_VA_TIPO_FRETE.NORMAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00076_CTP00001.get(1).put("AUT_HORARIO","MANHÂ");	 
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00076_CTP00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0035); 
			
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
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTR00001.get(1).put("AUT_CODIGO_ITEM","89573176");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "FR1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTR00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X.toString());
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTR00001.get(1).put("AUT_NUMERO_PEDIDO","NA");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTR00001.get(1).put("AUT_STATUS_PEDIDO",AUT_STATUS_PESQUISA.ELIMINADO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTR00001.get(1).put("AUT_CONDICOES_PEDIDO",AUT_MANTER_CONDICOES.SIM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTR00001.get(1).put("AUT_NUMERO_PEDIDO","1000008194"); //buscar pedido eliminado para usar aqui
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTR00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0035); 
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTR00001.get(1).put("AUT_TIPO_FRETE", AUTFluxoSaida.AUT_VA_TIP_FRETE.ECONOMICA);
			
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


			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTP00001.get(1).put("AUT_USER", "55708317");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTP00001.get(1).put("AUT_CODIGO_ITEM","89573176");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "82740645422");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTP00001.get(1).put("AUT_NUMERO_PEDIDO", "1000010695");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTP00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X.toString());
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO_2",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO.toString());
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO_2",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X.toString());
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","NA");
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTP00001.get(1).put("AUT_STATUS_PEDIDO",AUT_STATUS_PESQUISA.ELIMINADO);
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTP00001.get(1).put("AUT_CONDICOES_PEDIDO",AUT_MANTER_CONDICOES.SIM);			
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTP00001.get(1).put("AUT_TIPO_FRETE",AUT_VA_TIPO_FRETE.NORMAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTP00001.get(1).put("AUT_HORARIO","MANHÂ");	 
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00080_CTP00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0045); 

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
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00081_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "FR1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00081_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0035_CURITIBA);
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
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00081_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "82740645422");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00081_CTP00001.get(1).put("AUT_NUMERO_PEDIDO", "1000006803");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00081_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00081_CTP00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00081_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00081_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X.toString());
			

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
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0035_CURITIBA);
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


			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTP00001.get(1).put("AUT_USER", "55708317");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTP00001.get(1).put("AUT_CODIGO_ITEM","89296193");			
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "82740645422");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTP00001.get(1).put("AUT_NUMERO_PEDIDO", "1000011134");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTP00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTP00001.get(1).put("AUT_STATUS_PEDIDO",AUT_STATUS_PESQUISA.CANCELADO);
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTP00001.get(1).put("AUT_CONDICOES_PEDIDO",AUT_MANTER_CONDICOES.SIM);			
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00082_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			
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
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTR00001.get(1).put("AUT_CODIGO_ITEM","89296193");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "FR1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0035_CURITIBA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTR00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTR00001.get(1).put("AUT_NUMERO_PEDIDO","");

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
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO","82740645422");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTP00001.get(1).put("AUT_NUMERO_PEDIDO", "1000006885"); // pedido cancelado
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTP00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO_2",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO.toString());
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO_2",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X.toString());
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","NA");
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTP00001.get(1).put("AUT_STATUS_PEDIDO",AUT_STATUS_PESQUISA.CANCELADO);
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTP00001.get(1).put("AUT_CONDICOES_PEDIDO",AUT_MANTER_CONDICOES.SIM);			
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00083_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");		

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
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00084_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0035_CURITIBA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00084_CTR00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00084_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00084_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00084_CTR00001.get(1).put("AUT_NUMERO_PEDIDO","");
			
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
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00084_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00084_CTP00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00084_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00084_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00084_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","1000008517"); // pedido devolvido
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00084_CTP00001.get(1).put("AUT_STATUS_PEDIDO",AUT_STATUS_PESQUISA.DEVOLVIDO);
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00084_CTP00001.get(1).put("AUT_CONDICOES_PEDIDO",AUT_MANTER_CONDICOES.SIM);
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00084_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","NA");


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
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0035_CURITIBA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTR00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTR00001.get(1).put("AUT_NUMERO_PEDIDO","NA");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTR00001.get(1).put("AUT_STATUS_PEDIDO",AUT_STATUS_PESQUISA.DEVOLVIDO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTR00001.get(1).put("AUT_CONDICOES_PEDIDO",AUT_MANTER_CONDICOES.SIM);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTR00001.get(1).put("AUT_NUMERO_PEDIDO","NA");
			
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
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "68691640456");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTP00001.get(1).put("AUT_NUMERO_PEDIDO", "1000007797");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0035_CURITIBA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTP00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO_2",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO.toString());
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO_2",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X.toString());
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","NA");
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTP00001.get(1).put("AUT_STATUS_PEDIDO",AUT_STATUS_PESQUISA.DEVOLVIDO);
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTP00001.get(1).put("AUT_CONDICOES_PEDIDO",AUT_MANTER_CONDICOES.SIM);
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTP00001.get(1).put("AUT_DATA_INICIAL", "28/03/2019");
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00085_CTP00001.get(1).put("AUT_DATA_FINAL", "29/03/2019");

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
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00086_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0035_CURITIBA);
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
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00086_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0035_CURITIBA);
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

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00001_CTP00001.get(1).put("AUT_USER", "55708317");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00001_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00001_CTP00001.get(1).put("AUT_CODIGO_ITEM","89407066");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00001_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00001_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00001_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00001_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00001_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00001_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00001_CTP00001.get(1).put("AUT_TIPO_FRETE",AUT_VA_TIPO_FRETE.NORMAL);   
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00001_CTP00001.get(1).put("AUT_HORARIO","MANHÂ");   
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00001_CTP00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0035);


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

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00002_CTP00001.get(1).put("AUT_USER", "55708317");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00002_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00002_CTP00001.get(1).put("AUT_CODIGO_ITEM","89407066");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00002_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370");
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

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00003_CTP00001.get(1).put("AUT_USER", "55708317");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00003_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00003_CTP00001.get(1).put("AUT_CODIGO_ITEM","89407066");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00003_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00003_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00003_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_IMEDIATA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00003_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00003_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00003_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00003_CTP00001.get(1).put("AUT_TELEFONE_SERVIÇO","11982929201");

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

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00009_CTP00001.get(1).put("AUT_USER", "55000045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00009_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00009_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00009_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00009_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00009_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_IMEDIATA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00009_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00009_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00009_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00009_CTP00001.get(1).put("AUT_DESCONTO", "8");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00009_CTP00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ESPECIAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00009_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00009_CTP00001.get(1).put("AUT_TELEFONE_SERVIÇO","11987652345");

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

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00010_CTP00001.get(1).put("AUT_USER", "55708317");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00010_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00010_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00010_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370");
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
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00013_CTP00001.get(1).put("AUT_CODIGO_ITEM","89407066"); //89455163
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
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00013_CTP00001.get(1).put("AUT_INDEX_GARANTIA","1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00013_CTP00001.get(1).put("AUT_TIPO_FRETE",AUT_VA_TIPO_FRETE.NORMAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00013_CTP00001.get(1).put("AUT_HORARIO","MANHÂ");	 
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00013_CTP00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0035);

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

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00014_CTP00001.get(1).put("AUT_USER", "55000045"); //55192309
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00014_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00014_CTP00001.get(1).put("AUT_CODIGO_ITEM","89407066"); //89455163
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00014_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00014_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00014_CTP00001.get(1).put("AUT_QUANTIDADES_ITENS",2);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00014_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);// RETIRA_INTERNA_IMEDIATA
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00014_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00014_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00014_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00014_CTP00001.get(1).put("AUT_DESCONTO", "8");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00014_CTP00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ESPECIAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00014_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00014_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00014_CTP00001.get(1).put("AUT_INDEX_GARANTIA","1");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00014_CTP00001.get(1).put("AUT_TIPO_FRETE",AUT_VA_TIPO_FRETE.NORMAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00014_CTP00001.get(1).put("AUT_HORARIO","MANHÂ");	 
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00014_CTP00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0045);
			
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

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00015_CTP00001.get(1).put("AUT_USER", "55000045"); //55000035
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00015_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00015_CTP00001.get(1).put("AUT_CODIGO_ITEM","89407066"); //89455163
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00015_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00015_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00015_CTP00001.get(1).put("AUT_QUANTIDADES_ITENS",2);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00015_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA); //RETIRA_INTERNA_IMEDIATA
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00015_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00015_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00015_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00015_CTP00001.get(1).put("AUT_DESCONTO", "8");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00015_CTP00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ESPECIAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00015_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00015_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00015_CTP00001.get(1).put("AUT_INDEX_GARANTIA","1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00015_CTP00001.get(1).put("AUT_TIPO_FRETE",AUT_VA_TIPO_FRETE.NORMAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00015_CTP00001.get(1).put("AUT_HORARIO","MANHÂ");	 
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00015_CTP00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0045);   
			
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
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00016_CTP00001.get(1).put("AUT_CODIGO_ITEM","89407066");  //89455163
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

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00020_CTP00001.put(1, new java.util.HashMap<String,Object>());
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00020_CTP00001.get(1).put("AUT_USER", "553000119"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00020_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00020_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00020_CTP00001.get(1).put("AUT_QUANTIDADE_ITENS", 1);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00020_CTP00001.get(1).put("AUT_CODIGO_ITEM", "89407066"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00020_CTP00001.get(1).put("AUT_QUANTIDADE_ITENS", 2); 
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00020_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00020_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00020_CTP00001.get(1).put("AUT_DESCONTO", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00020_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.SALDO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00020_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00020_CTP00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00020_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00020_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00020_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00020_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00020_CTP00001.get(1).put("AUT_DESCONTO", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00020_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00002_CN00020_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00020_CTP00001);

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

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00021_CTP00001.put(1, new java.util.HashMap<String,Object>());
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00021_CTP00001.get(1).put("AUT_USER", "55300745"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00021_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00021_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00021_CTP00001.get(1).put("AUT_CODIGO_ITEM", "89407066"); //89407066 89407066
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00021_CTP00001.get(1).put("AUT_QUANTIDADE_ITENS", 2); 
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00021_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00021_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00021_CTP00001.get(1).put("AUT_DESCONTO", "2");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00021_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.SALDO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00021_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00021_CTP00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00021_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00021_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00021_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00021_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00021_CTP00001.get(1).put("AUT_DESCONTO", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00021_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);

			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00002_CN00021_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00021_CTP00001);

			//---------------------------------------------------------------------------------------------------------				


//			//Inclusao de tabela - RSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001
//			/**
//			 * 
//			 * 
//			 * 
//			 * Parametros para configuraÃ§Ã£o de componentes de Pedidos do sistema VA
//			 * 
//			 * 
//			 */
//			java.util.HashMap<Integer,java.util.HashMap<String,Object>> vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00022_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();
//
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.put(1, new java.util.HashMap<String,Object>());
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_USER", "55300745"); //5576316456068   55001063   550522446
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_PASSWORD", "1234");
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_CODIGO_ITEM", "89407066"); //89407066 89407066
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_QUANTIDADE_ITENS", 2); 
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //43409000283
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.REAIS);
//
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_DESCONTO", "2");
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.SALDO);
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA);
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.REAIS);
//
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_DESCONTO", "1");
//			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.get(1).put("AUT_MOTIVO", AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
//
//			AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00022_CTP00001);

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
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00023_CTP00001.get(1).put("AUT_CODIGO_ITEM","89407066");//
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

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00028_CTP00001.get(1).put("AUT_USER", "55000119");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00028_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00028_CTP00001.get(1).put("AUT_CODIGO_ITEM","89232220");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00028_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00028_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0019_PORTO_ALEGRE.toString());
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

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00029_CTP00001.get(1).put("AUT_USER", "55000119");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00029_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00029_CTP00001.get(1).put("AUT_CODIGO_ITEM","89232220");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00029_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00029_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0019_PORTO_ALEGRE.toString());
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

			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00030_CTP00001.get(1).put("AUT_USER", "55000119");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00030_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00030_CTP00001.get(1).put("AUT_CODIGO_ITEM","89232220");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00030_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00030_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0019_PORTO_ALEGRE.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00030_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_IMEDIATA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00030_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00030_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00030_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00030_CTP00001.get(1).put("AUT_COMENTARIO","Test cenario 30 adicionar ocorrencia");

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

			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTP00001.get(1).put("AUT_USER", "55708317");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_IMEDIATA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTP00001.get(1).put("AUT_COMENTARIO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTP00001.get(1).put("AUT_CANAL","Loja");
			
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


			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00004_CTP00001.get(1).put("AUT_USER", "55708317");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00004_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00004_CTP00001.get(1).put("AUT_CODIGO_ITEM","89296193");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00004_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00004_CTP00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00004_CTP00001.get(1).put("AUT_TIPO_FRETE",AUT_VA_TIPO_FRETE.NORMAL);  
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00004_CTP00001.get(1).put("AUT_HORARIO","MANHÂ");                    
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00004_CTP00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0035);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00004_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			
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


			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00005_CTP00001.get(1).put("AUT_USER", "55708317");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00005_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00005_CTP00001.get(1).put("AUT_CODIGO_ITEM","89455163");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00005_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00005_CTP00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00005_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			
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
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00006_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00006_CTP00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00006_CTP00001.get(1).put("AUT_DESCONTO", "08,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00006_CTP00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00006_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.REAIS);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00006_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			
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
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00007_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00007_CTP00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00007_CTP00001.get(1).put("AUT_DESCONTO", "80,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00007_CTP00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00007_CTP00001.get(1).put("AUT_DESCONTO", "80,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00007_CTP00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00007_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			
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
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00008_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			
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
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00012_CTP00001.get(1).put("AUT_TIPO_FRETE",AUT_VA_TIPO_FRETE.NORMAL);      
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00012_CTP00001.get(1).put("AUT_HORARIO","MANHÂ");                         
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00012_CTP00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0035);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00012_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");			
			
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


			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00017_CTP00001.get(1).put("AUT_USER", "55000119"); //55000045
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00017_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00017_CTP00001.get(1).put("AUT_CODIGO_ITEM","89296193"); //89455163
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00017_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "758.536.958-19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00017_CTP00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00017_CTP00001.get(1).put("AUT_TIPO_DESCONTO", AUT_TIPO_DESCONTO.REAIS);
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


			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00024_CTP00001.get(1).put("AUT_USER", "55000119"); //55000045
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00024_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00024_CTP00001.get(1).put("AUT_CODIGO_ITEM","89296193"); //89455163
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00024_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "758.536.958-19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00024_CTP00001.get(1).put("AUT_DESCONTO", "20,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00024_CTP00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00024_CTP00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00024_CTP00001.get(1).put("AUT_NUMERO_PEDIDO", "");
			
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


			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00025_CTP00001.get(1).put("AUT_USER", "55000119"); //55000045
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00025_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00025_CTP00001.get(1).put("AUT_CODIGO_ITEM","85023484"); //89455163
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00025_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "758.536.958-19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00025_CTP00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00025_CTP00001.get(1).put("AUT_DESCONTO", "20,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00025_CTP00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00002_CN00025_CTP00001.get(1).put("AUT_NUMERO_PEDIDO", "");
			
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

			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTR00001.get(1).put("AUT_USER", "55000119"); //35
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTR00001.get(1).put("AUT_CODIGO_ITEM","89232220");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTR00001.get(1).put("AUT_QUANTIDADE_ITENS", 2);
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTR00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTR00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTR00001.get(1).put("AUT_DESCONTO", "80,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTR00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00001_CTR00001.get(1).put("AUT_NUMERO_ORÇAMENTO", "");

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


			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00002_CTP00001.get(1).put("AUT_USER", "55708317");
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


			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00002_CTR00001.get(1).put("AUT_USER", "55000119");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00002_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00002_CTR00001.get(1).put("AUT_CODIGO_ITEM","89232220");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00002_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00002_CTR00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00002_CTR00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00002_CTR00001.get(1).put("AUT_DESCONTO", "80,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00002_CTR00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00002_CTR00001.get(1).put("AUT_NUMERO_PEDIDO","");

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


			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00003_CTP00001.get(1).put("AUT_USER", "55708317");
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


			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00003_CTR00001.get(1).put("AUT_USER", "55000119");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00003_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00003_CTR00001.get(1).put("AUT_CODIGO_ITEM","89232220");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00003_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00003_CTR00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00003_CTR00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00003_CTR00001.get(1).put("AUT_DESCONTO", "60,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00003_CTR00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00003_CTR00001.get(1).put("AUT_NUMERO_PEDIDO", "");
			
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


			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00004_CTP00001.get(1).put("AUT_USER", "55708317");
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


			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00004_CTR00001.get(1).put("AUT_USER", "55000045");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00004_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00004_CTR00001.get(1).put("AUT_QUANTIDADE_ITENS", 2);
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00004_CTR00001.get(1).put("AUT_CODIGO_ITEM","89407066");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00004_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00004_CTR00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00004_CTR00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00004_CTR00001.get(1).put("AUT_DESCONTO", "80,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00004_CTR00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00004_CTR00001.get(1).put("AUT_NUMERO_PEDIDO", "");
			
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


			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00005_CTP00001.get(1).put("AUT_USER", "55708317");
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


			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00005_CTR00001.get(1).put("AUT_USER", "55000119");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00005_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00005_CTR00001.get(1).put("AUT_CODIGO_ITEM","89232220");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00005_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00005_CTR00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.CAIXA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00005_CTR00001.get(1).put("AUT_TIPO_DESCONTO",AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00005_CTR00001.get(1).put("AUT_DESCONTO", "80,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00005_CTR00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00003_CN00005_CTR00001.get(1).put("AUT_NUMERO_PEDIDO", "");
			
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
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTR00001.get(1).put("AUT_CODIGO_ITEM","89573176");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "FR1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTR00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VISA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTR00001.get(1).put("AUT_NUMERO_CARTAO", "4000 0000 0000 0044"); //5300332890376075
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTR00001.get(1).put("AUT_NOME_TITULAR", "Usuario Automação");
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


			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTP00001.get(1).put("AUT_USER", "55708317");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTP00001.get(1).put("AUT_CODIGO_ITEM","89573176");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO","FR1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTP00001.get(1).put("AUT_NUMERO_PEDIDO", "112221");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00087_CTP00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_IMEDIATA);
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
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00088_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "FR1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00088_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0035_CURITIBA);
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
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00088_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "FR1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00088_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0035_CURITIBA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00088_CTP00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA); 
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00088_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.CARTAO_CREDITO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00088_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00088_CTP00001.get(1).put("AUT_CONDICOES_PEDIDO",AUT_MANTER_CONDICOES.NAO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00088_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00088_CTP00001.get(1).put("AUT_STATUS_PEDIDO","NA");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00088_CTP00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0045);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00088_CTP00001.get(1).put("AUT_TIPO_FRETE",AUT_VA_TIPO_FRETE.NORMAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00088_CTP00001.get(1).put("AUT_HORARIO","MANHÂ");	

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
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00089_CTP00001.get(1).put("AUT_CODIGO_ITEM","89573176");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00089_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "FR1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00089_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0035_CURITIBA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00089_CTP00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_INTERNA_AGENDADA); 
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00089_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00089_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00001_CN00089_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			
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
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00001_CTP00001.get(1).put("AUT_LOJA_SELECIONADA","Loja | Porto Alegre"); //LM_0045_SAO_BERNARDO_DO_CAMPO

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
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00002_CTP00001.get(1).put("AUT_CODIGO_ITEM","89296193"); //89455163
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00002_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00002_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0019_PORTO_ALEGRE); //LM_0035_CURITIBA
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00002_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00002_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VISA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00002_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00002_CTP00001.get(1).put("AUT_NUMERO_CARTAO", "4000000000000044");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00002_CTP00001.get(1).put("AUT_NOME_TITULAR", "Usuário Automação");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00002_CTP00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00002_CTP00001.get(1).put("AUT_CODIGO_CARTAO", "123");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00002_CTP00001.get(1).put("AUT_CODE_VALE_TROCA","00620723");			
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00002_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00002_CTP00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0019);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00002_CTP00001.get(1).put("AUT_TIPO_FRETE",AUT_VA_TIPO_FRETE.NORMAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00002_CTP00001.get(1).put("AUT_HORARIO","MANHÂ");	
			
			
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

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00003_CTP00001.get(1).put("AUT_USER", "55436723"); //55001063
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00003_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00003_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM", "120");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00003_CTP00001.get(1).put("AUT_CODIGO_ITEM", "89407066"); 
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00003_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "82740645422"); //43409000283  89716885000133
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00003_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO); 		
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00003_CTP00001.get(1).put("AUT_FLUXO_SAIDA", br.lry.components.va.cat007.AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00003_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", br.lry.components.va.cat009.AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.MASTERCARD); // VISA
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00003_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", br.lry.components.va.cat009.AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA_1X);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00003_CTP00001.get(1).put("AUT_NUMERO_CARTAO", "5000000000000001"); //4000000000000044
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00003_CTP00001.get(1).put("AUT_NOME_TITULAR", "Usuário Automação");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00003_CTP00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00003_CTP00001.get(1).put("AUT_CODIGO_CARTAO", "123");	//123
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00003_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00003_CTP00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0045);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00003_CTP00001.get(1).put("AUT_TIPO_FRETE",AUT_VA_TIPO_FRETE.NORMAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00003_CTP00001.get(1).put("AUT_HORARIO","MANHÂ");	

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

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00004_CTP00001.get(1).put("AUT_USER", "55436723");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00004_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00004_CTP00001.get(1).put("AUT_CODIGO_ITEM","89232220");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00004_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "82740645422");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00004_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO.toString()); //LM_0035_CURITIBA
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00004_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00004_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.MASTERCARD);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00004_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA_1X);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00004_CTP00001.get(1).put("AUT_NUMERO_CARTAO", "5000000000000001");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00004_CTP00001.get(1).put("AUT_NOME_TITULAR", "Usuário Automação");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00004_CTP00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00004_CTP00001.get(1).put("AUT_CODIGO_CARTAO", "123");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00004_CTP00001.get(1).put("AUT_CODE_VALE_TROCA","00620723");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00004_CTP00001.get(1).put("AUT_TIPO_FRETE",AUT_VA_TIPO_FRETE.NORMAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00004_CTP00001.get(1).put("AUT_HORARIO","MANHÂ");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00004_CTP00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0045);

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
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTR00001.get(1).put("AUT_CODIGO_ITEM","89232220");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTR00001.get(1).put("AUT_FLUXO_SAIDA", br.lry.components.va.cat007.AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.RETIRA_EXTERNA_AGENDADA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTR00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO", br.lry.components.va.cat009.AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VISA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO", br.lry.components.va.cat009.AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTR00001.get(1).put("AUT_NUMERO_CARTAO", "5000000000000001");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTR00001.get(1).put("AUT_NOME_TITULAR", "Usuário Automação");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTR00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTR00001.get(1).put("AUT_CODIGO_CARTAO", "123");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTR00001.get(1).put("AUT_CODE_VOUCHER","6394422000000293143");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTR00001.get(1).put("AUT_VALOR_PAGAMENTO_2", "");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO_2", br.lry.components.va.cat009.AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VOUCHER);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO_2", br.lry.components.va.cat009.AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTR00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00006_CTR00001.get(1).put("AUT_USER_APROVADOR","");

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

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0019_PORTO_ALEGRE.toString()); //LM_0035_CURITIBA
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001.get(1).put("AUT_USER", "55436723"); //55001063
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001.get(1).put("AUT_QUANTIDADE_ITEM", "120");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001.get(1).put("AUT_CODIGO_ITEM", "86470475"); //89455163
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //43409000283  89716885000133
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001.get(1).put("AUT_FLUXO_SAIDA", br.lry.components.va.cat007.AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO", br.lry.components.va.cat009.AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VISA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO", br.lry.components.va.cat009.AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001.get(1).put("AUT_NUMERO_CARTAO", "4000000000000044");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001.get(1).put("AUT_NOME_TITULAR", "Usuário Automação");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001.get(1).put("AUT_CODIGO_CARTAO", "123");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0019);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001.get(1).put("AUT_DESCONTO", "80,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001.get(1).put("AUT_TIPO_DESCONTO",br.lry.components.va.cat010.AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001.get(1).put("AUT_USER_APROVADOR","55708317");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001.get(1).put("AUT_TIPO_FRETE",AUT_VA_TIPO_FRETE.NORMAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001.get(1).put("AUT_HORARIO","MANHÂ");	
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00007_CTR00001.get(1).put("AUT_CANAL","Televendas");	
			
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

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTR00001.get(1).put("AUT_USER", "55436723");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTR00001.get(1).put("AUT_CODIGO_ITEM","89411714");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO.toString()); //LM_0035_CURITIBA
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTR00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.MASTERCARD);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO", AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA_1X);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTR00001.get(1).put("AUT_NUMERO_CARTAO", "5000000000000001");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTR00001.get(1).put("AUT_NOME_TITULAR", "Usuário Automação");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTR00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTR00001.get(1).put("AUT_CODIGO_CARTAO", "123");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTR00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0045);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTR00001.get(1).put("AUT_USER_APROVADOR","55230148");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTR00001.get(1).put("AUT_TIPO_FRETE",AUT_VA_TIPO_FRETE.NORMAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00009_CTR00001.get(1).put("AUT_HORARIO","MANHÂ");
			
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

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO .toString()); //LM_0035_CURITIBA
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTR00001.get(1).put("AUT_USER", "55436723"); 
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTR00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTR00001.get(1).put("AUT_CODIGO_ITEM", "89411714"); //89407066 89455163
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTR00001.get(1).put("AUT_FLUXO_SAIDA", br.lry.components.va.cat007.AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTR00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO", br.lry.components.va.cat009.AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.MASTERCARD);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO", br.lry.components.va.cat009.AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA_1X);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTR00001.get(1).put("AUT_NUMERO_CARTAO", "5000000000000001");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTR00001.get(1).put("AUT_NOME_TITULAR", "Usuário Automação");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTR00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTR00001.get(1).put("AUT_CODIGO_CARTAO", "123");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTR00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTR00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0045);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTR00001.get(1).put("AUT_USER_APROVADOR","55230148");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTR00001.get(1).put("AUT_TIPO_FRETE",AUT_VA_TIPO_FRETE.NORMAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00010_CTR00001.get(1).put("AUT_HORARIO","MANHÂ");			

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

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0035_CURITIBA); //LM_0035_CURITIBA
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001.get(1).put("AUT_USER", "55001063"); //55001063
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001.get(1).put("AUT_CODIGO_ITEM", "89573176"); //89455163
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001.get(1).put("AUT_FLUXO_SAIDA", br.lry.components.va.cat007.AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO", br.lry.components.va.cat009.AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VISA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO", br.lry.components.va.cat009.AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001.get(1).put("AUT_NUMERO_CARTAO", "4000000000000044");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001.get(1).put("AUT_NOME_TITULAR", "Usuário Automação");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001.get(1).put("AUT_CODIGO_CARTAO", "123");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO_2",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VOUCHER);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO_2",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001.get(1).put("AUT_CODE_VOUCHER",".*\\\\d{19}");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0035);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001.get(1).put("AUT_TIPO_FRETE",AUT_VA_TIPO_FRETE.NORMAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001.get(1).put("AUT_HORARIO","MANHÂ");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001.get(1).put("AUT_DESCONTO", "80,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00011_CTR00001.get(1).put("AUT_TIPO_DESCONTO",br.lry.components.va.cat010.AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);
			
			
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


			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0019_PORTO_ALEGRE .toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTR00001.get(1).put("AUT_USER", "55000119"); //5576316456068   55001063   550522446
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTR00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTR00001.get(1).put("AUT_CODIGO_ITEM", "89590473"); 
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "94288636370"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTR00001.get(1).put("AUT_FLUXO_SAIDA", br.lry.components.va.cat007.AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTR00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO", br.lry.components.va.cat009.AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VISA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO", br.lry.components.va.cat009.AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTR00001.get(1).put("AUT_NUMERO_CARTAO", "4000000000000044");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTR00001.get(1).put("AUT_NOME_TITULAR", "Usuário Automação");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTR00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTR00001.get(1).put("AUT_CODIGO_CARTAO", "123");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTR00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTR00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0019);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTR00001.get(1).put("AUT_TIPO_FRETE",AUT_VA_TIPO_FRETE.NORMAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTR00001.get(1).put("AUT_HORARIO","MANHÂ");			
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTR00001.get(1).put("AUT_USER_EDITOR", "55436723"); //usar usuário de  televendas
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTR00001.get(1).put("AUT_DESCONTO", "5,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTR00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00012_CTR00001.get(1).put("AUT_TIPO_DESCONTO",br.lry.components.va.cat010.AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);			
			

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

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUT_VA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTR00001.get(1).put("AUT_USER", "55000119"); 
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTR00001.get(1).put("AUT_QUANTIDADE_ITEM", "1");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTR00001.get(1).put("AUT_CODIGO_ITEM", "89590473"); 
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "82740645422"); //43409000283
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTR00001.get(1).put("AUT_DESCONTO", "5,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTR00001.get(1).put("AUT_MOTIVO", br.lry.components.va.cat010.AUTDesconto.AUT_VA_MOTIVO.ERRO_NO_PRECO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTR00001.get(1).put("AUT_FLUXO_SAIDA", br.lry.components.va.cat007.AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTR00001.get(1).put("AUT_VALOR_PAGAMENTO", "10,00");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO", br.lry.components.va.cat009.AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VISA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO", br.lry.components.va.cat009.AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X);			
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTR00001.get(1).put("AUT_NUMERO_CARTAO", "4000000000000044");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTR00001.get(1).put("AUT_NOME_TITULAR", "Usuário Automação");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTR00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTR00001.get(1).put("AUT_CODIGO_CARTAO", "123");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTR00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTR00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0019);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTR00001.get(1).put("AUT_TIPO_DESCONTO",br.lry.components.va.cat010.AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTR00001.get(1).put("AUT_USER_EDITOR","55436723");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTR00001.get(1).put("AUT_TIPO_FRETE",AUT_VA_TIPO_FRETE.NORMAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00013_CTR00001.get(1).put("AUT_HORARIO","MANHÂ");	



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


			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTR00001.get(1).put("AUT_USER", "55000119");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTR00001.get(1).put("AUT_CODIGO_ITEM","89590473");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUT_VA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTR00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VISA.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTR00001.get(1).put("AUT_NUMERO_CARTAO", "4000000000000044");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTR00001.get(1).put("AUT_NOME_TITULAR", "Usuário Automação");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTR00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTR00001.get(1).put("AUT_CODIGO_CARTAO", "123");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTR00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTR00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0019);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTR00001.get(1).put("AUT_TIPO_DESCONTO",br.lry.components.va.cat010.AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTR00001.get(1).put("AUT_USER_EDITOR","55436723");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTR00001.get(1).put("AUT_TIPO_FRETE",AUT_VA_TIPO_FRETE.NORMAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTR00001.get(1).put("AUT_HORARIO","MANHÂ");

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

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001.get(1).put("AUT_USER", "55000119");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001.get(1).put("AUT_CODIGO_ITEM","89590473");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUT_VA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VISA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001.get(1).put("AUT_NUMERO_CARTAO", "4000000000000044");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001.get(1).put("AUT_NOME_TITULAR", "Usuário Automação");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001.get(1).put("AUT_CODIGO_CARTAO", "123");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001.get(1).put("AUT_NUMERO_CARTAO", "4000000000000044");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001.get(1).put("AUT_NOME_TITULAR", "Usuário Automação");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001.get(1).put("AUT_CODIGO_CARTAO", "123");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0019);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001.get(1).put("AUT_TIPO_DESCONTO",br.lry.components.va.cat010.AUTDesconto.AUT_TIPO_DESCONTO.PORCENTAGEM);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001.get(1).put("AUT_USER_EDITOR","55436723");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001.get(1).put("AUT_TIPO_FRETE",AUT_VA_TIPO_FRETE.NORMAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00014_CTP00001.get(1).put("AUT_HORARIO","MANHÂ");	

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

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTR00001.get(1).put("AUT_USER", "55436723");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTR00001.get(1).put("AUT_CODIGO_ITEM","85617833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0019_PORTO_ALEGRE);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTR00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VISA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTR00001.get(1).put("AUT_NUMERO_CARTAO", "4000000000000044");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTR00001.get(1).put("AUT_NOME_TITULAR", "Usuário Automação");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTR00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTR00001.get(1).put("AUT_CODIGO_CARTAO", "123");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTR00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTR00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0019);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTR00001.get(1).put("AUT_TIPO_FRETE",AUT_VA_TIPO_FRETE.NORMAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTR00001.get(1).put("AUT_HORARIO","MANHÂ");	


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

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTP00001.get(1).put("AUT_USER", "55436723");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTP00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTP00001.get(1).put("AUT_CODIGO_ITEM","85617833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0045_SAO_BERNARDO_DO_CAMPO.toString());
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTP00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VISA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTP00001.get(1).put("AUT_NUMERO_CARTAO", "4000000000000044");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTP00001.get(1).put("AUT_NOME_TITULAR", "Usuário Automação");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTP00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTP00001.get(1).put("AUT_CODIGO_CARTAO", "123");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTP00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0019);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTP00001.get(1).put("AUT_TIPO_FRETE",AUT_VA_TIPO_FRETE.NORMAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00015_CTP00001.get(1).put("AUT_HORARIO","MANHÂ");			

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

			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTR00001.get(1).put("AUT_USER", "55436723");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTR00001.get(1).put("AUT_PASSWORD", "1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTR00001.get(1).put("AUT_CODIGO_ITEM","89232220");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0019_PORTO_ALEGRE);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTR00001.get(1).put("AUT_FLUXO_SAIDA", AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.DINHEIRO);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.A_VISTA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTR00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTR00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0019);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTR00001.get(1).put("AUT_TIPO_FRETE",AUT_VA_TIPO_FRETE.NORMAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00016_CTR00001.get(1).put("AUT_HORARIO","MANHÂ");

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
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTR00001.get(1).put("AUT_USER","55000119");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTR00001.get(1).put("AUT_PASSWORD","1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTR00001.get(1).put("AUT_QUANTIDADE_ITEM","2");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTR00001.get(1).put("AUT_CODIGO_ITEM","89232220");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTR00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTR00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0019_PORTO_ALEGRE);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTR00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTR00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VISA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTR00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTR00001.get(1).put("AUT_NUMERO_CARTAO", "4000000000000044");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTR00001.get(1).put("AUT_NOME_TITULAR", "Usuário Automação");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTR00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTR00001.get(1).put("AUT_CODIGO_CARTAO", "123");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTR00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTR00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0019);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTR00001.get(1).put("AUT_TIPO_FRETE",AUT_VA_TIPO_FRETE.NORMAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTR00001.get(1).put("AUT_HORARIO","MANHÂ");	

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
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTP00001.get(1).put("AUT_USER","55436723");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTP00001.get(1).put("AUT_PASSWORD","1234");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTP00001.get(1).put("AUT_QUANTIDADE_ITEM","2");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTP00001.get(1).put("AUT_CODIGO_ITEM","89232220");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTP00001.get(1).put("AUT_NUMERO_DOCUMENTO", "42942711833");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaVA.AUT_VA_LISTA_LOJAS.LM_0019_PORTO_ALEGRE);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTP00001.get(1).put("AUT_FLUXO_SAIDA",AUTFluxoSaida.AUT_VA_FLUXO_SAIDA.ENTREGA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTP00001.get(1).put("AUT_MEIO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_MEIOS_PAGAMENTO.VISA);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTP00001.get(1).put("AUT_PLANO_PAGAMENTO",AUTMeiosPagamento.AUT_VA_PLANO_PAGAMENTO.SEM_JUROS_1X);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTP00001.get(1).put("AUT_NUMERO_CARTAO", "4000000000000044");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTP00001.get(1).put("AUT_NOME_TITULAR", "Usuário Automação");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTP00001.get(1).put("AUT_VALIDADE", "12/19");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTP00001.get(1).put("AUT_CODIGO_CARTAO", "123");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTP00001.get(1).put("AUT_NUMERO_PEDIDO","");
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTP00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0019);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTP00001.get(1).put("AUT_TIPO_FRETE",AUT_VA_TIPO_FRETE.NORMAL);
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00017_CTP00001.get(1).put("AUT_HORARIO","MANHÂ");

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
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00018_CTP00001.get(1).put("AUT_LOJA_SELECIONADA",AUTSelecaoLojaBoitata.AUT_BOITATA_LISTA_LOJAS.LM_0035_CURITIBA.toString());
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
			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00001_CTR00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0045);
			
			
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
			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00002_CTR00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0045);

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
			vaDataRSP_PJTTRC_FRT001_VA_MD00006_CN00003_CTR00001.get(1).put("AUT_OPCAO_LOJA",AUT_VA_OPCAO_LOJA_FLUXO_SAIDA.LOJA_0045);

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


			//Inclusao de tabela - Tabela de Itens
			/**
			 * 
			 * 
			 * 
			 * Parametros para carga dos Itens
			 * 
			 * 
			 */
			vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00018_CTP00001 = new java.util.HashMap<Integer,java.util.HashMap<String,Object>>();
			java.util.HashMap<String,java.util.HashMap<String,Object>> itens = new java.util.HashMap<String,java.util.HashMap<String,Object>>();	
			itens.put("Produto", new java.util.HashMap<String,Object>());
			//vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00018_CTP00001.get("Produto").put("Quantidade", "FluxoSaida");

			//AUT_GLOBAL_PARAMETERS.put(AUT_TABLE_PARAMETERS_NAMES.RSP_PJTTRC_FRT001_VA_MD00004_CN00018_CTP00001.toString(),vaDataRSP_PJTTRC_FRT001_VA_MD00004_CN00018_CTP00001);

			//AUT_IT00004_STVA_ID00004_FRT004_CN00004_CADASTRO_CLIENTE_PF_LOJA0035
			
			// -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------			




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


