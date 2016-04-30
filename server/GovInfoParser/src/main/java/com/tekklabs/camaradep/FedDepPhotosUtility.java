package com.tekklabs.camaradep;

import com.tekklabs.util.StringUtil;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;
import org.apache.pdfbox.util.PDFOperator;

import javax.imageio.*;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Petrobras - TIC-EP/GIDSEP
 * Created by y5kx on 28/04/16.
 */
public final class FedDepPhotosUtility {

    private static final String HELP_TAG = "--help";
    private static final String EXTRACT_TAG = "--extract";
    private static final String COMPRESS_TAG = "--compress";

    private static final Map<String, String> mapImgKeyToPolName = new HashMap<>();
    private static final Map<String, PDXObjectImage> mapImgKeyToImageObj = new HashMap<>();


    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Argumentos insuficientes. Utilize '" + HELP_TAG + "' para menu de ajuda.");
            return;
        }

        String command = args[0];
        boolean result;

        if (command.equals(HELP_TAG)) {
            printHelp();
            return;
        }
        else if (command.equals(EXTRACT_TAG)) {
            try {
                result = extractFromPdf(args);
            }
            catch(Exception e) {
                System.out.println("Erro na execução da extração de fotos do arquivo PDF.");
                e.printStackTrace();
                result = false;
            }
        }
        else if (command.equals(COMPRESS_TAG)) {
            try {
                result = compressImageFiles(args);
            }
            catch(Exception e) {
                System.out.println("Erro na execução da compressão de fotos.");
                e.printStackTrace();
                result = false;
            }
        }
        else {
            System.out.println("Comando invalido. Utilize '" + HELP_TAG + "' para ajuda.");
            result = false;
        }

        if (!result) {
            System.out.println("Operação terminou com erro.");
        }
        else {
            System.out.println("Operação terminou com sucesso.");
        }
    }

    private static void printHelp() {
        System.out.println("===== Ajuda do utilitário de manipulação das fotos dos deputados federais =====");
        System.out.println();
        System.out.println("Modo de uso: ./FedDepPhotosUtility " + EXTRACT_TAG + " <pdfFilePath> <outputDirectoryPath>");
        System.out.println("             ./FedDepPhotosUtility " + COMPRESS_TAG + " <inputFilesDir> <outputFilesDir>");
        System.out.println("======================================");
    }

    private static boolean extractFromPdf(String[] args) throws IOException {
        if (args.length != 3) {
            System.out.println("Número de argumentos incorreto. Utilize '--help' para ajuda.");
            return false;
        }

        File pdfFile = new File(args[1]);
        File outputDir = new File(args[2]);

        System.out.println("Loading PDF file...");
        PDDocument document = PDDocument.load(pdfFile);
        List pages = document.getDocumentCatalog().getAllPages();

        Iterator it = pages.iterator();
        int pageCount = 1;
        while(it.hasNext()) {
            System.out.println("Page " + pageCount + "...");
            PDPage page = (PDPage)it.next();
            System.out.println("Building ImgKey to Politician Name mapping...");
            buildImgKeyToPolNameMapping(page);
            System.out.println("Building ImgKey to Image Object mapping...");
            buildImgKeyToImageObjMapping(page);
            pageCount++;
        }

        System.out.println();
        System.out.println("Saving images...");
        exportImages(outputDir);

        document.close();
        return true;
    }

    private static void buildImgKeyToPolNameMapping(PDPage page) throws IOException {
        PDStream contents = page.getContents();
        PDFStreamParser parser = new PDFStreamParser(contents.getStream());
        parser.parse();
        List tokens = parser.getTokens();

        boolean concatStringPhase = false;
        String polName = "";
        String lastText = "";

        for (int index = 0; index < tokens.size(); index++) {
            Object obj = tokens.get(index);

            if (obj instanceof PDFOperator) {
                PDFOperator op = (PDFOperator) obj;
                if (op.getOperation().equals("BT")) {
                    concatStringPhase = true;
                    polName = lastText;
                    lastText = "";
                }
                else if (op.getOperation().equals("ET")) {
                    concatStringPhase = false;
                }
            }
            else if (concatStringPhase && obj instanceof COSString) {
                COSString cosString = (COSString) obj;
                lastText += " " + cosString.getString();
                lastText = lastText.trim();
            }
            else if (!concatStringPhase && obj instanceof COSName) {
                COSName cosName = (COSName) obj;
                if (cosName.getName().startsWith("img")) {
                    mapImgKeyToPolName.put(cosName.getName(), polName);
                }
            }
        }
    }

    private static void buildImgKeyToImageObjMapping(PDPage page) throws IOException {
        PDResources resources = page.getResources();
        Map images = resources.getImages();

        if(images != null) {
            Iterator imageIter = images.keySet().iterator();

            while(imageIter.hasNext()) {
                String key = (String)imageIter.next();
                PDXObjectImage image = (PDXObjectImage)images.get(key);
                mapImgKeyToImageObj.put(key, image);
            }
        }
    }

    private static void exportImages(File outputDir) throws IOException {
        for (String imgKey : mapImgKeyToPolName.keySet()) {
            String polName = StringUtil.stripAccents(mapImgKeyToPolName.get(imgKey));
            PDXObjectImage image = mapImgKeyToImageObj.get(imgKey);

            String dirPath = outputDir.getAbsolutePath();
            if (!dirPath.endsWith("/")) {
                dirPath += "/";
            }

            File outputFileName = new File(dirPath + polName + ".jpg");
            if (!outputFileName.getParentFile().exists() && !outputFileName.getParentFile().mkdirs()) {
                System.out.println("Não foi possível criar os diretórios intermediários para salvar o arquivo no caminho: " + outputFileName.getAbsolutePath());
            }

            if (!outputFileName.createNewFile()) {
                System.out.println("Não foi possível criar o arquivo: " + outputFileName.getAbsolutePath());
            }

            image.write2file(outputFileName);
            System.out.println("Arquivo salvo: " + outputFileName.getName());
        }
    }

    private static boolean compressImageFiles(String[] args) throws IOException {
        if (args.length != 3) {
            System.out.println("Número de argumentos incorreto. Utilize '--help' para ajuda.");
            return false;
        }

        File inputDir = new File(args[1]);
        File outputDir = new File(args[2]);

        return compressImageFiles(inputDir, outputDir);
    }

    private static boolean compressImageFiles(File inputDir, File outputDir) throws IOException {
        if (!inputDir.exists()) {
            System.out.println("O diretório de origem informado não existe");
            return false;
        }

        ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();
        jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        jpgWriteParam.setCompressionQuality(0.7f);

        boolean globalResult = true;
        for (File aFile : inputDir.listFiles()) {
            if (aFile.isDirectory() || aFile.isHidden()) {
                continue;
            }

            String outputPath = outputDir.getAbsolutePath();
            if (!outputDir.getAbsolutePath().endsWith("/")) {
                outputPath += "/";
            }
            outputPath += aFile.getName();
            File outputFile = new File(outputPath);

            System.out.println("Comprimindo imagem: " + outputFile.getAbsolutePath());
            boolean result = compressImageFile(jpgWriter, jpgWriteParam, aFile, outputFile);
            if (!result) {
                globalResult = false;
                System.out.println("Passando para a próxima imagem...");
            }
        }

        jpgWriter.dispose();
        return globalResult;
    }

    private static boolean compressImageFile(ImageWriter jpgWriter, ImageWriteParam jpgWriteParam, File inputFile, File outputFile) throws IOException {
        BufferedImage image = null;
        try {
            /* Para evitar problemas na leitura de imagens JPEG com
               modelo de cor CMYK um leitor terceiro é utilizado como extensão do ImageIO,
               Por isso precisamos da dependência: com.twelvemonkeys.imageio:imageio-jpeg.
               A extensão é instalada automaticamente, basta estar no classpath.
             */
            image = ImageIO.read(inputFile);
        }
        catch (Exception ex) {
            System.out.println("Falha ao ler arquivo com ImageIO.");
            ex.printStackTrace();
        }

        IIOImage outputImage = new IIOImage(image, null, null);

        if (!outputFile.exists()) {
            if (!outputFile.getParentFile().exists() && !outputFile.getParentFile().mkdirs()) {
                System.out.println("Não foi possível criar os diretórios intermediários para salvar o arquivo no caminho: " + outputFile.getAbsolutePath());
                return false;
            }

            if (!outputFile.createNewFile()) {
                System.out.println("Não foi possível criar o arquivo: " + outputFile.getAbsolutePath());
                return false;
            }
        }
        ImageOutputStream outputStream = new FileImageOutputStream(outputFile);
        jpgWriter.setOutput(outputStream);
        jpgWriter.write(null, outputImage, jpgWriteParam);

        return true;
    }

    public static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        BufferedImage image = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = image.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        return image;
    }
}
