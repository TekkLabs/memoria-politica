#!/bin/bash
###############################################################################################
####					                 FedDepEater         		   		               ####
###############################################################################################
####											                                           ####
#### Esse script automatiza o trabalho de processar as informacões dos deputados federais. ####
#### Passos executados:                                                                    ####
####            Download do arquivo excel com informacões dos deputados.                   ####
####            Conversão do arquivo excel em um arquivo json.                             ####
####            Download do arquivo PDF com as fotos dos deputados.                        ####
####		Extracao das fotos dos deputados.					                           ####
####		Compressão das fotos dos deputados.					                           ####
####											                                           ####
#### Modo de uso:									                                       ####
####		./FedDepEater <program_jar> <dir_saida>					                       ####
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

if [ $# -eq 1 ] && ['$1' == '--help' ]; then
    echo 'Modo de uso: ./FedDepEater <program_jar> <dir_saida>'
    echo '<program_jar> = Caminho do jar do GovInfoParser.'
    echo '<dir_saida> = Diretório de trabalho onde são salvos os arquivos.'
    exit 0
fi

## Arguments
program_jar=$1
output_dir=$2



## Processing Politicians photos
photos_file=$output_dir'/photos.pdf'
mkdir $output_dir
wget -O $photos_file 'http://www.camara.leg.br/internet/infdoc/novoconteudo/Acervo/CELEG/Carometro/carometro_legislatura55.pdf'

photos_tmp_dir=$output_dir'/photos_tmp'
photos_final_dir=$output_dir'/photos'

java -cp $program_jar com.tekklabs.camaradep.FedDepPhotosUtility --extract $photos_file $photos_tmp_dir
java -cp $program_jar com.tekklabs.camaradep.FedDepPhotosUtility --compress $photos_tmp_dir $photos_final_dir

rm $photos_file
rm -r $photos_tmp_dir



## Processing Politicians Info
info_file_name='feddepinfo.xls'
info_file=$output_dir'/'$info_file_name
wget -O $info_file 'http://www2.camara.leg.br/deputados/pesquisa/arquivos/arquivo-formato-excel-com-informacoes-dos-deputados-1'

converted_info_file_name=$info_file_name'x'
converted_info_file=$output_dir'/'$converted_info_file_name
ssconvert --export-type=Gnumeric_Excel:xlsx $info_file $converted_info_file

java -cp $program_jar com.tekklabs.camaradep.FedDepXlsxConverter --xlsx2json $converted_info_file $output_dir'/feddepinfo.json'

rm $info_file
rm $converted_info_file
