package com.lab.controller;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.lab.entity.Report;
import com.lab.repository.ReportRepository;
import com.lab.repository.BookingRepository;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ReportController {

    @Autowired
    private ReportRepository reportRepo;

    @Autowired
    private BookingRepository bookingRepo;

    // 🔹 Show upload page
    @GetMapping("/admin/report/{id}")
    public String showReportPage(@PathVariable Long id, Model model) {
        model.addAttribute("bookingId", id);
        return "upload-report";
    }

    // 🔹 Save or Update report
    @PostMapping("/admin/report")
    public String saveReport(@RequestParam Long bookingId,
                             @RequestParam String result) {

        // ✅ check existing report
        Report r = reportRepo.findByBookingId(bookingId);

        if (r == null) {
            r = new Report();
            r.setBookingId(bookingId);
        }

        r.setResult(result);
        reportRepo.save(r);

        // ✅ update booking status
        var booking = bookingRepo.findById(bookingId).orElse(null);
        if (booking != null) {
            booking.setStatus("Report Ready");
            bookingRepo.save(booking);
        }

        return "redirect:/admin/bookings?success=Report saved successfully";
    }

    // 🔹 View report (Patient)
    @GetMapping("/report/{bookingId}")
    public String viewReport(@PathVariable Long bookingId,
                             Model model,
                             HttpSession session) {

        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }

        Report report = reportRepo.findByBookingId(bookingId);
        model.addAttribute("report", report);

        return "view-report";
    }

    // 🔹 Download PDF
    @GetMapping("/report/pdf/{bookingId}")
    public void downloadPdf(@PathVariable Long bookingId,
                            HttpServletResponse response) throws Exception {

        var booking = bookingRepo.findById(bookingId).orElse(null);
        Report report = reportRepo.findByBookingId(bookingId);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=LabReport.pdf");

        PdfWriter writer = new PdfWriter(response.getOutputStream());
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // 🔹 COLORS
        com.itextpdf.kernel.colors.Color blue =
                new com.itextpdf.kernel.colors.DeviceRgb(13, 110, 253);

        // 🔹 LOGO
        try {
            com.itextpdf.layout.element.Image logo =
                    new com.itextpdf.layout.element.Image(
                            com.itextpdf.io.image.ImageDataFactory.create("src/main/resources/static/images/logo.png"))
                            .scaleToFit(80, 80);
            document.add(logo);
        } catch (Exception e) {
            System.out.println("Logo not found");
        }

        // 🔹 TITLE
        document.add(new Paragraph("BLOOD TEST LAB REPORT")
                .setBold()
                .setFontSize(18)
                .setFontColor(blue));

        document.add(new Paragraph("\n"));

        // 🔹 TABLE (Patient Info)
        com.itextpdf.layout.element.Table table =
                new com.itextpdf.layout.element.Table(2).useAllAvailableWidth();

        table.addCell("Patient Name");
        table.addCell(booking != null ? booking.getPatientName() : "-");

        table.addCell("Test Name");
        table.addCell(booking != null ? booking.getTestName() : "-");

        table.addCell("Booking ID");
        table.addCell(String.valueOf(bookingId));

        table.addCell("Report Date");
        table.addCell(java.time.LocalDate.now().toString());

        document.add(table);

        document.add(new Paragraph("\n"));

        // 🔹 RESULT SECTION
        document.add(new Paragraph("Test Result")
                .setBold()
                .setFontSize(14)
                .setFontColor(blue));

        document.add(new Paragraph("\n"));

        if (report != null) {
            document.add(new Paragraph(report.getResult()));
        } else {
            document.add(new Paragraph("Report not available"));
        }

        document.add(new Paragraph("\n\n"));

        // 🔹 SIGNATURE
        document.add(new Paragraph("Doctor Signature: ____________________"));

        document.add(new Paragraph("\n"));

        // 🔹 LAB ADDRESS
        document.add(new Paragraph("Lab Address:")
                .setBold());

        document.add(new Paragraph("ABC Diagnostics Lab\nUdgir, Maharashtra\nPhone: 9876543210"));

        document.add(new Paragraph("\n"));

        // 🔹 FOOTER
        document.add(new Paragraph("Thank you for trusting our lab!")
                .setItalic()
                .setFontSize(10));

        document.close();
    }

    // 🔹 Edit report page
    @GetMapping("/admin/edit-report/{bookingId}")
    public String editReport(@PathVariable Long bookingId, Model model) {

        Report report = reportRepo.findByBookingId(bookingId);

        // ✅ handle null case
        if (report == null) {
            report = new Report();
            report.setBookingId(bookingId);
        }

        model.addAttribute("report", report);
        model.addAttribute("bookingId", bookingId);

        return "edit-report";
    }

    // 🔹 Update report
    @PostMapping("/admin/update-report")
    public String updateReport(@RequestParam Long bookingId,
                               @RequestParam String result) {

        Report r = reportRepo.findByBookingId(bookingId);

        if (r == null) {
            r = new Report();
            r.setBookingId(bookingId);
        }

        r.setResult(result);
        reportRepo.save(r);

        return "redirect:/admin/bookings?success=Report updated successfully";
    }
}