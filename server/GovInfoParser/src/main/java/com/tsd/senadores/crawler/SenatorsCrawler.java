package com.tsd.senadores.crawler;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Normalizer;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

/**
 * Created by taciosd on 8/2/15.
 */
public class SenatorsCrawler extends WebCrawler {

    @Override
    public boolean shouldVisit(WebURL url) {
        boolean isPoliticianInfoPage = url.getURL().contains("/senadores/dinamico/paginst/senador");
        boolean isOneLevelDeep = (url.getDepth() <= 1);
        return isPoliticianInfoPage && isOneLevelDeep;
    }

    @Override
    public void visit(Page page) {
        HtmlParseData parseData = (HtmlParseData) page.getParseData();
        String html = parseData.getHtml();
        Document doc = Jsoup.parse(html);
        String politicianName = fetchName(doc);

        try {
            byte[] photo = fetchPhoto(doc);
            if (photo.length == 0) {
                return;
            }

            String outputFolder = getMyController().getConfig().getCrawlStorageFolder();
            String formattedName = stripAccents(politicianName);
            FileOutputStream out = new FileOutputStream(new java.io.File(outputFolder + formattedName + ".jpg"));
            out.write(photo);
            out.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private String fetchName(Document doc) {
        Elements elements = doc.select("[class=tituloSenadores]");
        Element elem = elements.first().child(0);
        String title = elem.text();
        return title.replaceFirst("Senadora? ", "");
    }

    private byte[] fetchPhoto(Document doc) throws IOException {
        Elements elements = doc.select("img[src^=/senadores/img/fotos/");
        if (elements.isEmpty()) {
            throw new IllegalStateException();
        }

        Element elem = elements.first();
        String relLink = elem.attr("src");
        String link = "http://www.senado.leg.br" + relLink;

        Connection.Response resultImageResponse = Jsoup.connect(link).ignoreContentType(true).execute();
        return resultImageResponse.bodyAsBytes();
    }

    private String stripAccents(String s)
    {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
    }
}
