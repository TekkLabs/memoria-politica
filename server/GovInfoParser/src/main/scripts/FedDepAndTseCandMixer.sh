#!/bin/bash
###############################################################################################
####					             FedDepAndTseCandMixer     		   		               ####
###############################################################################################
####											                                           ####
#### Esse script automatiza o trabalho de juntar os dados dos deputados federais com suas  ####
####     respectivas informacoes de candidato no TSE. O resultado é um arquivo JSON com    ####
####     informacões atualizadas dos deputados.                                            ####
#### Os arquivos utilizados nesse script podem ser gerados pelos scripts FedDepEater.sh e  ####
####     TseCandidateEater.sh.                                                             ####
####											                                           ####
#### Passos executados:                                                                    ####
####		    Procesamento do conteúdo e criacão de um arquivo json.					   ####
####											                                           ####
#### Modo de uso:									                                       ####
####	./FedDepAndTseCandMixer <program_jar> <fed_dep_file> <tse_cand_file> <arq_saida>   ####
####										                                               ####
####		<program_jar> = Caminho do jar do GovInfoParser.			                   ####
####		<fed_dep_file> = Arquivo JSON com informacões dos deputados federais.          ####
####		<tse_cand_file> = Caminho do jar do GovInfoParser.			                   ####
####		<arq_saida> = Diretório de trabalho onde são salvos os arquivos.	           ####
####											                                           ####
#### Dependências:                                                                         ####
####        wget - Download de documentos pela rede.                                       ####
####        gnumeric - Conversão de formatos de excel.                                     ####
###############################################################################################

set -e

if [ $# -eq 0 ]; then
    echo 'Faltam argumentos. Utilize --help para ajuda.'
    exit 1
fi

if [ $# -eq 1 ] && [ $1 == '--help' ]; then
    echo 'Modo de uso: ./TseCandidateEater <program_jar> <dir_saida>'
    echo '<program_jar> = Caminho do jar do GovInfoParser.'
    echo '<dir_saida> = Diretório de trabalho onde são salvos os arquivos.'
    exit 0
fi

## Arguments
program_jar=$1
fed_dep_file=$2
tse_cand_file=$3
output_file=$4

java -cp $program_jar com.tekklabs.camaradep.FedDepAndTseCandMerger --merge $fed_dep_file $tse_cand_file $output_file