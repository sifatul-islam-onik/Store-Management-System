package com.example.storemanagementsystem.Utilities;

import com.example.storemanagementsystem.Models.ProductData;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class InvoiceGenerator {
    PDDocument invoice;
    Double total = 0.0;
    List<ProductData> list;
    String InvoiceTitle = new String("KUET Departmental Store");
    String SubTitle = new String("Invoice");

    public InvoiceGenerator(List<ProductData> list) {
        invoice = new PDDocument();
        PDPage newpage = new PDPage();
        invoice.addPage(newpage);
        this.list = list;
    }

    public void write(int CustomerID, String EmployeeID, String date){
        PDPage mypage = invoice.getPage(0);
        try {
            PDPageContentStream cs = new PDPageContentStream(invoice, mypage);

            cs.beginText();
            cs.setFont(PDType1Font.TIMES_ROMAN, 20);
            cs.newLineAtOffset(210, 750);
            cs.showText(InvoiceTitle);
            cs.endText();

            cs.beginText();
            cs.setFont(PDType1Font.TIMES_ROMAN, 18);
            cs.newLineAtOffset(270, 700);
            cs.showText(SubTitle);
            cs.endText();

            cs.beginText();
            cs.setFont(PDType1Font.TIMES_ROMAN, 14);
            cs.setLeading(20f);
            cs.newLineAtOffset(60, 650);
            cs.showText("Customer ID: ");
            cs.newLine();
            cs.showText("Date: ");
            cs.endText();

            cs.beginText();
            cs.setFont(PDType1Font.TIMES_ROMAN, 14);
            cs.setLeading(20f);
            cs.newLineAtOffset(170, 650);
            cs.showText(Integer.toString(CustomerID));
            cs.newLine();
            cs.showText(date);
            cs.endText();

            cs.beginText();
            cs.setFont(PDType1Font.TIMES_ROMAN, 14);
            cs.newLineAtOffset(60, 600);
            cs.showText("Product Name");
            cs.endText();

            cs.beginText();
            cs.setFont(PDType1Font.TIMES_ROMAN, 14);
            cs.newLineAtOffset(180, 600);
            cs.showText("Unit Price");
            cs.endText();

            cs.beginText();
            cs.setFont(PDType1Font.TIMES_ROMAN, 14);
            cs.newLineAtOffset(290, 600);
            cs.showText("Quantity");
            cs.endText();

            cs.beginText();
            cs.setFont(PDType1Font.TIMES_ROMAN, 14);
            cs.newLineAtOffset(390, 600);
            cs.showText("Price");
            cs.endText();

            cs.beginText();
            cs.setFont(PDType1Font.TIMES_ROMAN, 12);
            cs.setLeading(20f);
            cs.newLineAtOffset(60, 580);
            for(ProductData productData:list){
                cs.showText(productData.getProductName());
                cs.newLine();
            }
            cs.endText();

            cs.beginText();
            cs.setFont(PDType1Font.TIMES_ROMAN, 12);
            cs.setLeading(20f);
            cs.newLineAtOffset(180, 580);
            for(ProductData productData:list){
                cs.showText(Double.toString((productData.getPrice()/productData.getQuantity())));
                cs.newLine();
            }
            cs.endText();

            cs.beginText();
            cs.setFont(PDType1Font.TIMES_ROMAN, 12);
            cs.setLeading(20f);
            cs.newLineAtOffset(290, 580);
            for(ProductData productData:list){
                cs.showText(Integer.toString(productData.getQuantity()));
                cs.newLine();
            }
            cs.endText();

            cs.beginText();
            cs.setFont(PDType1Font.TIMES_ROMAN, 12);
            cs.setLeading(20f);
            cs.newLineAtOffset(390, 580);

            for(ProductData productData:list){
                total+=productData.getPrice();
                cs.showText(Double.toString(productData.getPrice()));
                cs.newLine();
            }
            cs.endText();

            cs.beginText();
            cs.setFont(PDType1Font.TIMES_ROMAN, 14);
            cs.newLineAtOffset(290, (560-(20* list.size())));
            cs.showText("Total: ");
            cs.endText();

            cs.beginText();
            cs.setFont(PDType1Font.TIMES_ROMAN, 14);
            cs.newLineAtOffset(390, (560-(20* list.size())));
            cs.showText(total + " BDT");
            cs.endText();

            cs.beginText();
            cs.setFont(PDType1Font.TIMES_ROMAN, 8);
            cs.newLineAtOffset(400, (560-(20* (list.size()+2))));
            cs.showText("Serviced by: ");
            cs.endText();

            cs.beginText();
            cs.setFont(PDType1Font.TIMES_ROMAN, 8);
            cs.newLineAtOffset(450, (560-(20* (list.size()+2))));
            cs.showText(EmployeeID);
            cs.endText();

            cs.close();

            invoice.save(CustomerID + ".Pdf");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
