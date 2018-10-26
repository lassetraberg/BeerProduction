package org.grp2.printmanager;

import com.itextpdf.io.font.FontNames;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import org.grp2.shared.Batch;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class SimplePdfPrinter2 implements IPrintManager {
	/**
	 * {@inheritDoc}
	 * @param path the path to write the document to, including name
	 * @param batch any batch
	 */
	@Override
	public void writeDocument(String path, Batch batch) {
		try {
			PdfWriter writer = new PdfWriter("document.pdf");
			PdfDocument pdf = new PdfDocument(writer);

			Document document = new Document(pdf);

			// *******    Header    ********
			Paragraph p = new Paragraph("Batch Report");

			p.setTextAlignment(TextAlignment.CENTER);
			p.setFontSize(30);
			p.setBold();

			document.add(p);



			// *******    Table    ********
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-uu   HH:mm:ss");
			float[] columnWidths = {100, 200};
			Table table1 = new Table(columnWidths);
			table1.setWidth(UnitValue.createPercentValue(100));

			Cell cellNoBorder = new Cell().setMinHeight(15).setBorder(Border.NO_BORDER);
			Cell cellBold = new Cell().setMinHeight(15).setBold();

			table1.addCell(cellBold.clone(true).add(new Paragraph("Batch ID")));
			table1.addCell(String.valueOf(batch.getBatchId()));
			table1.addCell(cellBold.clone(true).add(new Paragraph("Product type")));
			table1.addCell(batch.getBeerName());

			table1.addCell(cellNoBorder.clone(true));
			table1.addCell(cellNoBorder.clone(true));

			table1.addCell(new Cell(1,2).add(new Paragraph("Measurements").setFontSize(15).setBold()).setMinHeight(15).setBold().setTextAlignment(TextAlignment.CENTER));
			table1.addCell(cellBold.clone(true).add(new Paragraph("Beers produced")));
			table1.addCell(String.valueOf(batch.getAccepted() + batch.getDefect()));
			table1.addCell(cellBold.clone(true).add(new Paragraph("Beers accepted")));
			table1.addCell(String.valueOf(batch.getAccepted()));
			table1.addCell(cellBold.clone(true).add(new Paragraph("Beers defective")));
			table1.addCell(String.valueOf(batch.getDefect()));
			table1.addCell(cellBold.clone(true).add(new Paragraph("Batch started")));
			table1.addCell(String.valueOf(batch.getStarted().format(dateTimeFormatter)));
			table1.addCell(cellBold.clone(true).add(new Paragraph("Batch finished")));
			table1.addCell("08-11-2018\t09:21:01");
			table1.addCell(cellBold.clone(true).add(new Paragraph("Time elapsed (min:sec)")));
			table1.addCell("12:48");

			table1.addCell(cellNoBorder.clone(true));
			table1.addCell(cellNoBorder.clone(true));

			document.add(table1);

			UnitValue[] measurementColumnWidth = {UnitValue.createPercentValue(30), UnitValue.createPercentValue(35), UnitValue.createPercentValue(35)};
			Table measurementTable = new Table(measurementColumnWidth);
			measurementTable.setWidth(UnitValue.createPercentValue(100));
			measurementTable.addCell(new Cell(1,3).add(new Paragraph("Measurements").setFontSize(15).setBold()).setMinHeight(15).setBold().setTextAlignment(TextAlignment.CENTER));
			measurementTable.addCell(cellBold.clone(true).add(new Paragraph("Time")));
			measurementTable.addCell(cellBold.clone(true).add(new Paragraph("Temperature")));
			measurementTable.addCell(cellBold.clone(true).add(new Paragraph("Humidity")));
			int numberOfMeasurements = 9;
			for (int i = 0; i < numberOfMeasurements; i++) {
				measurementTable.addCell(new Cell().add(new Paragraph("Test")));
			}

			document.add(measurementTable);

			document.close();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}