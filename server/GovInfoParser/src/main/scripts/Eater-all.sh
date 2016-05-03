#!/bin/bash
###############################################################################################
####					             Eater-all               		   		               ####
###############################################################################################
####											                                           ####
#### Esse script automatiza o trabalho de gerar e processar todos os dados dos deputados   ####
####                                                                                       ####
#### Modo de uso:									                                       ####
####		./FedDepEater <program_jar> <dir_saida>					                       ####
####											                                           ####
####		<program_jar> = Caminho do jar do GovInfoParser.			                   ####
####		<dir_saida> = Diretório de trabalho onde são salvos os arquivos.	           ####
####											                                           ####
###############################################################################################

set -e

program_jar=$1
output_dir=$2
root_dir=pwd

sh ./FedDepEater.sh $program_jar $output_dir
sh ./TseCandidateEater.sh $program_jar $output_dir
sh ./FedDepAndTseCandMixer.sh $program_jar $output_dir'/feddepinfo.json' $output_dir'/candidates_2014.json' $output_dir'feddepinfo-merged.json'
