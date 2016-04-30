## INSTRUCOES GERAIS PARA EXECUCAO

### Candidatos
Os dados pessoais dos candidatos foram obtidos no site do TSE:
http://www.tse.jus.br/hotSites/pesquisas-eleitorais/candidatos_anos/2014.html
Baixado o arquivo consulta_cand_2014.zip

### Deputados Federais
As fotos dos deputados (carômetro) são extraídas do arquivo PDF pelo GovInfoParser, através do utilitário FedDepPhotosUtility.
Esse utilitário também é utilizado para comprimir as fotos para diminuir seu tamanho.
No geral, uma reducão de 30% na qualidade gera mais de 70% de reducão no tamanho do arquivo.

As informacões dos deputados são extraídas pelo GovInfoParser através do utilitário FedDepXlsxConverter.
Esse utilitário lê os dados e cria um arquivo JSON ao final.

#### Como executar
cd GovInfoParser
gradle jarWithDependencies
cd src/main/scripts
./FedDepEater.sh ../../../build/libs/GovInfoParser-all-1.0.jar <output_dir>

