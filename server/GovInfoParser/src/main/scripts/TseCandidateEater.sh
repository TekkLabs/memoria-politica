#!/bin/bash
###############################################################################################
####					             TseCandidateEater         		   		               ####
###############################################################################################
####											                                           ####
#### Esse script automatiza o trabalho de processar informacões dos candidatos a deputado. ####
#### Passos executados:                                                                    ####
####            Download do arquivo zip com informacões dos candidatos.                    ####
####            Extracão do conteúdo do arquivo zip em uma pasta                           ####
####		    Procesamento do conteúdo e criacão de um arquivo json.					   ####
####											                                           ####
#### Modo de uso:									                                       ####
####		./TseCandidateEater <program_jar> <dir_saida>					               ####
####											                                           ####
####		<program_jar> = Caminho do jar do GovInfoParser.			                   ####
####		<dir_saida> = Diretório de trabalho onde são salvos os arquivos.	           ####
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
output_dir=$2


mkdir $output_dir
cand_zip_file=$output_dir'/candidates_2014.zip'
wget -O $cand_zip_file 'http://agencia.tse.jus.br/estatistica/sead/odsele/consulta_cand/consulta_cand_2014.zip'

exploded_zip_dir=$output_dir'/exploded'
java -cp $program_jar com.tekklabs.tse.TseCandidateInfoParser --extract $cand_zip_file $exploded_zip_dir

merged_file=$exploded_zip_dir'/merged.csv'
java -cp $program_jar com.tekklabs.tse.TseCandidateInfoParser --merge $exploded_zip_dir $merged_file

candidated_json_file=$output_dir'/candidates_2014.json'
java -cp $program_jar com.tekklabs.tse.TseCandidateInfoParser --csv2json $merged_file $candidated_json_file

rm $cand_zip_file
rm -r $exploded_zip_dir


