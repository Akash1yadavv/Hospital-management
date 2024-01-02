package com.hospital.util;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.hospital.model.Appointment;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;

import com.lowagie.text.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class PdfGenerator {
	
	private static final Logger logger = LoggerFactory.getLogger(PdfGenerator.class);
	
	public static ByteArrayInputStream generate(Appointment appointment) throws DocumentException, IOException {
	    
    	Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        try { 	    
    	    document.open();
    	    // Creating font
    	    // Setting font style and size
    	    Font fontTiltle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
    	    fontTiltle.setSize(20);
    	    // Creating paragraph
    	    Paragraph paragraph1 = new Paragraph("Appointment Invoice at Haarmk Hospital.", fontTiltle);
    	    paragraph1.setAlignment(Paragraph.ALIGN_CENTER);
    	    document.add(paragraph1);
    	    
    	    // Creating a table of the 4 columns
    	    PdfPTable table = new PdfPTable(8);
    	    // Setting width of the table, its columns and spacing
    	    table.setWidthPercentage(100f);
    	    table.setWidths(new int[] {10,10,10,10,10,10,10,10});
    	    table.setSpacingBefore(10);
    	    
    	    // Create Table Cells for the table header
    	    PdfPCell cell = new PdfPCell();
    	    // Setting the background color and padding of the table cell
    	    cell.setBackgroundColor(CMYKColor.BLUE);
    	    cell.setPadding(10);
    	    // Creating font
    	    // Setting font style and size
    	    Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
    	    font.setColor(CMYKColor.WHITE);
    	    // Adding headings in the created table cell or  header
    	    // Adding Cell to table
    	    cell.setPhrase(new Phrase("Appointment Id", font));
    	    table.addCell(cell);
    	    cell.setPhrase(new Phrase(String.valueOf(appointment.getId()),font));
    	    table.addCell(cell);
    	    
    	    cell.setPhrase(new Phrase("Patient Name", font));
    	    table.addCell(cell);
    	    cell.setPhrase(new Phrase(String.valueOf(appointment.getPatientInfo().getPatientName()),font));
    	    table.addCell(cell);
    	    
    	    cell.setPhrase(new Phrase("Email", font));
    	    table.addCell(cell);
    	    cell.setPhrase(new Phrase(String.valueOf(appointment.getPatientInfo().getEmail()), font));
    	    table.addCell(cell);
    	    
    	    cell.setPhrase(new Phrase("Mobile No", font));
    	    table.addCell(cell);
    	    cell.setPhrase(new Phrase(String.valueOf(appointment.getPatientInfo().getMobile()),font));
    	    table.addCell(cell);
    	    
    	    cell.setPhrase(new Phrase("Apointment Time", font));
    	    table.addCell(cell);
    	    cell.setPhrase(new Phrase(String.valueOf(appointment.getSlot().getStartTime().toString().substring(11)),font));
    	    table.addCell( cell);
    	    
    	    cell.setPhrase(new Phrase("Apointment EndTime", font));
    	    table.addCell(cell);
    	    cell.setPhrase(new Phrase(String.valueOf(appointment.getSlot().getEndTime().toString().substring(11)),font));
    	    table.addCell(cell);
    	    
    	    cell.setPhrase(new Phrase("Apointment Date", font));
    	    table.addCell(cell);
    	    cell.setPhrase(new Phrase(String.valueOf(appointment.getSlot().getStartTime().toString().substring(0,10)),font));
    	    table.addCell(cell);
    	    
    	    cell.setPhrase(new Phrase("Doctor Name", font));
    	    table.addCell(cell);
    	    cell.setPhrase(new Phrase(String.valueOf("Dr. Harveer Singh Parmar"),font));
    	    table.addCell(cell);


    	    document.add(table);
    	    document.close();

        } catch (DocumentException ex) {

            logger.error("Error occurred: {0}", ex);
        }

        return new ByteArrayInputStream(out.toByteArray());
	
	  }
	
}
