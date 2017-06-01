package com.sense.frame.common.util;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

public class PdfUtils {

    private void handleText(PdfWriter writer, String content, String color,
            float x, float y, float z) {
        PdfContentByte canvas = writer.getDirectContent();
        Phrase phrase = new Phrase(content);
        if (color != null) {
            phrase = new Phrase(content, FontFactory.getFont(
                    FontFactory.COURIER, 12, Font.NORMAL, new Color(255, 0, 0)));
        }

        ColumnText.showTextAligned(canvas, Element.ALIGN_UNDEFINED, phrase, x,
                y, z);
    }

    public File Pdf(String imagePath, String mOutputPdfFileName) {
        Document doc = new Document(PageSize.A4, 20, 20, 20, 20);
        try {
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(
                    mOutputPdfFileName));
            doc.open();

            doc.newPage();
            Image png1 = Image.getInstance(imagePath);
            float heigth = png1.getHeight();
            float width = png1.getWidth();
            int percent = this.getPercent2(heigth, width);
            png1.setAlignment(Image.MIDDLE);
            png1.setAlignment(Image.TEXTWRAP);
            png1.scalePercent(percent + 3);
            doc.add(png1);
            this.handleText(writer, "This is a test", "red", 400, 725, 0);
            doc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File mOutputPdfFile = new File(mOutputPdfFileName);
        if (!mOutputPdfFile.exists()) {
            mOutputPdfFile.deleteOnExit();
            return null;
        }
        return mOutputPdfFile;
    }

    public int getPercent1(float h, float w) {
        int p = 0;
        float p2 = 0.0f;
        if (h > w) {
            p2 = 297 / h * 100;
        } else {
            p2 = 210 / w * 100;
        }
        p = Math.round(p2);
        return p;
    }

    private int getPercent2(float h, float w) {
        int p = 0;
        float p2 = 0.0f;
        p2 = 530 / w * 100;
        p = Math.round(p2);
        return p;
    }

    public static void main(String[] args) {
    	PdfUtils gp = new PdfUtils();
        String pdfUrl = "C:\\pdf.pdf";
        File file = gp
                .Pdf("C:\\683.JPG",pdfUrl);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
