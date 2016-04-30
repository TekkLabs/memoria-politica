Os dados dos deputados federais foram obtidos por consulta no site:
http://www2.camara.leg.br/deputados/pesquisa

Arquivo baixado: 
deputado.xls

O XLS foi salvo como CSV no Excel e,
o seguinte conversor CSV2JSON foi utilizado:
http://www.convertcsv.com/csv-to-json.htm


*** Candidatos ***
Os dados pessoais dos candidatos foram obtidos no site do TSE:
http://www.tse.jus.br/hotSites/pesquisas-eleitorais/candidatos_anos/2014.html
Baixado o arquivo consulta_cand_2014.zip

*** Fotos dos Deputados Federais ***
As fotos dos deputados (carometro) são extraídas do arquivo PDF pelo GovInfoParser, através do utilitário FedDepPhotosUtility.
Esse utilitário também é utilizado para comprimir as fotos para diminuir seu tamanho.
No geral, uma reducão de 30% na qualidade gera mais de 70% de reducão no tamanho do arquivo.

*** Próximos passos ***
 - Baixar o arquivo deputados.xls automaticamente.
 - Converter o arquivo .xls para .json.
 - Baixar o arquivo consulta_cand_YYYY.zip automaticamente
 - Fazer um script que automatiza os passos acima.