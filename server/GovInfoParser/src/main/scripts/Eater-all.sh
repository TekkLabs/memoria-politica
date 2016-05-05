#!/bin/bash
###############################################################################################
####					             Eater-all               		   		               ####
###############################################################################################
####											                                           ####
#### Esse script automatiza o trabalho de gerar e processar todos os dados dos deputados   ####
####                                                                                       ####
#### Modo de uso:									                                       ####
####		./Eater-all <program_jar> <dir_saida>					                       ####
####											                                           ####
####		<program_jar> = Caminho do jar do GovInfoParser.			                   ####
####		<dir_saida> = Diretório de trabalho onde são salvos os arquivos.	           ####
####											                                           ####
###############################################################################################

set -e

if [ $# -eq 0 ]; then
    echo 'Faltam argumentos. Utilize --help para ajuda.'
    exit 1
fi

if [ $# -eq 1 ] && [ $1 == '--help' ]; then
    echo 'Modo de uso: ./Eater-all <program_jar> <dir_saida>'
    echo '<program_jar> = Caminho do jar do GovInfoParser.'
    echo '<dir_saida> = Diretório de trabalho onde são salvos os arquivos.'
    exit 0
fi

program_jar=$1
output_dir=$2
root_dir=pwd

sh ./FedDepEater.sh $program_jar $output_dir
sh ./TseCandidateEater.sh $program_jar $output_dir
sh ./FedDepAndTseCandMixer.sh $program_jar $output_dir'/feddepinfo.json' $output_dir'/candidates_2014.json' $output_dir'/feddepinfo-merged.json'
