package com.booking_hotel.api.booking.service;

import com.booking_hotel.api.booking.dto.BookingResponse;
import com.booking_hotel.api.booking.entity.Booking;
import com.booking_hotel.api.utils.dateUtils.DateUtils;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PDFService {

    public static File generateDailyBookingReport(List<BookingResponse> bookings) throws FileNotFoundException {
        String filePath = "daily_booking_report.pdf";
        File file = new File(filePath);
        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Daily Booking Report"));
        document.add(new Paragraph("Date: " + DateUtils.now("yyyy-MM-dd HH:mm")));

        Table table = new Table(new float[]{3, 3, 3, 3, 3, 3});
        table.addHeaderCell(new Cell().add(new Paragraph("Booking ID")));
        table.addHeaderCell(new Cell().add(new Paragraph("Hotel")));
        table.addHeaderCell(new Cell().add(new Paragraph("Room")));
        table.addHeaderCell(new Cell().add(new Paragraph("Check-In")));
        table.addHeaderCell(new Cell().add(new Paragraph("Check-Out")));
        table.addHeaderCell(new Cell().add(new Paragraph("Total Price")));

        for (BookingResponse booking : bookings) {
            table.addCell(new Cell().add(new Paragraph(String.valueOf(booking.getBookingId()))));
            table.addCell(new Cell().add(new Paragraph(booking.getRoomResponse().getHotelResponse().getName())));
            table.addCell(new Cell().add(new Paragraph(booking.getRoomResponse().getRoomNumber())));
            table.addCell(new Cell().add(new Paragraph(DateUtils.formatZonedDateTimeString(booking.getCheckInDate()))));
            table.addCell(new Cell().add(new Paragraph(DateUtils.formatZonedDateTimeString(booking.getCheckOutDate()))));
            table.addCell(new Cell().add(new Paragraph(String.format("%.2f$", booking.getTotalPrice()))));
        }

        document.add(table);
        document.close();
        return file;
    }

}
