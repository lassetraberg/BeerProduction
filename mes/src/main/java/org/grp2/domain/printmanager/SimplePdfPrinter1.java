package org.grp2.domain.printmanager;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.BorderCollapsePropertyValue;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import org.grp2.shared.Batch;
import org.grp2.shared.MeasurementLog;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

public class SimplePdfPrinter1 implements IPrintManager {
    private String path;

    private Paragraph pTitle;
    private Table tBasicInfo;
    private Table tMeasurements;

    private DateTimeFormatter dtf;
    private DecimalFormat df;

    public SimplePdfPrinter1() {
        path = null;
        dtf = DateTimeFormatter.ofPattern("uuuu:MM:dd  HH:mm:ss");
        df = new DecimalFormat("#.0000");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Writes document in a very simple format. It contains a title and two sections.
     * The first section is basic information, which includes beer names etc.
     * The second section are the measurements in a table with three columns.
     *
     * @param batch any batch
     */
    public void writeDocument(Batch batch, MeasurementLog... logs) {
        try {
            PdfWriter writer = new PdfWriter(path);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            setupDocumentTitle();
            setupBasicInformation(batch);
            setupMeasurements(logs);

            document.add(pTitle);
            document.add(tBasicInfo);
            document.add(tMeasurements);

            document.close();

            pTitle = null;
            tBasicInfo = null;
            tMeasurements = null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //System.out.println(FontProgramFactory.getRegisteredFonts());
    }

    @Override
    public byte[] getDocument() {
        byte[] array = null;

        if (path != null) {
            try {
                array = Files.readAllBytes(Paths.get(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return array;
    }

    private void setupDocumentTitle() {
        pTitle = new Paragraph("Batch Rapport")
                .setBold().setTextAlignment(TextAlignment.CENTER)
                .setFontSize(30);
    }

    private void setupBasicInformation(Batch batch) {
        tBasicInfo = new Table(new UnitValue[]{UnitValue.createPercentValue(20), UnitValue.createPercentValue(80)}, true);
        tBasicInfo.setBorderCollapse(BorderCollapsePropertyValue.SEPARATE);

        Cell basicTitle = new Cell(0, 2).add(new Paragraph("Basic Information")
                .setTextAlignment(TextAlignment.CENTER)).setFontSize(16);

        tBasicInfo.addCell(basicTitle);

        buildInfoRow("Order no.:", String.valueOf(batch.getOrderNumber()));
        buildInfoRow("Batch Id:", String.valueOf(batch.getBatchId()));
        buildInfoRow("Beer name:", batch.getBeerName());
        buildInfoRow("Started:", batch.getStarted().format(dtf));
        buildInfoRow("Finished:", batch.getFinished().format(dtf));
        buildInfoRow("Accepted:", String.valueOf(batch.getAccepted()));
        buildInfoRow("Defected:", String.valueOf(batch.getDefect()));

        tBasicInfo.setVerticalBorderSpacing(10f);
    }

    private Table buildInfoRow(String text, String value) {
        Table row = tBasicInfo.startNewRow();
        Cell cellText = new Cell().setBorder(Border.NO_BORDER).add(new Paragraph(text).setFontSize(14));
        Cell cellValue = new Cell().setBorder(Border.NO_BORDER).add(new Paragraph(value).setFontSize(14));

        return row.addCell(cellText).addCell(cellValue);
    }

    private void setupMeasurements(MeasurementLog... logs) {
        tMeasurements = new Table(4, true);
        tMeasurements.addHeaderCell(new Cell(0, 4).add(new Paragraph("Measurements")
                .setFontSize(18))
                .setTextAlignment(TextAlignment.CENTER));

        tMeasurements.startNewRow().setTextAlignment(TextAlignment.CENTER)
                .addCell("Time").setBold()
                .addCell("Temperature").setBold()
                .addCell("Humidity").setBold()
                .addCell("Vibration").setBold();


        for (MeasurementLog log : logs) {
            tMeasurements.startNewRow()
                    .addCell(log.getMeasurementTime().format(dtf))
                    .addCell(String.valueOf(log.getMeasurements().getTemperature()))
                    .addCell(String.valueOf(log.getMeasurements().getHumidity()))
                    .addCell(String.valueOf(log.getMeasurements().getVibration()));
        }

        tMeasurements.setBorderCollapse(BorderCollapsePropertyValue.SEPARATE);
    }

//	private void fontSetup() {
//		FontSet fs = new FontSet();
//			try {
//				fs.addFont(FontProgramFactory.createRegisteredFont("courier"), PdfEncodings.UTF8);
//			} catch(IOException e) {
//				e.printStackTrace();
//			}
//
//			FontProvider fp = new FontProvider(fs, "courier");
//			document.setFontProvider(fp);
//			document.setFont("courier");
//	}
}