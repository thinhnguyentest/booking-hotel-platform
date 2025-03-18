package com.booking_hotel.api.booking.service;

import com.booking_hotel.api.booking.dto.BookingResponse;
import com.booking_hotel.api.booking.entity.Booking;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PDFService {

    public ResponseEntity<byte[]> generateBookingsReport(List<Booking> bookings) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Bookings Report").setFont(StandardFonts.HELVETICA_BOLD).setFontSize(20));

            for (Booking booking : bookings) {
                document.add(new Paragraph("Booking ID: " + booking.getBookingId()));
                document.add(new Paragraph("User: " + booking.getUser().getUsername()));
                document.add(new Paragraph("Room: " + booking.getRoom().getRoomNumber()));
                document.add(new Paragraph("Check-In: " + booking.getCheckInDate()));
                document.add(new Paragraph("Check-Out: " + booking.getCheckOutDate()));
                document.add(new Paragraph("Total Price: " + booking.getTotalPrice()));
                document.add(new Paragraph("Status: " + booking.getStatus()));
                document.add(new Paragraph("\n"));
            }

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        byte[] pdfData = outputStream.toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment().filename("DailyBookingReport.pdf").build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfData);
    }

}
