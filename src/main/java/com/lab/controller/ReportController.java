package com.lab.controller;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.lab.entity.Report;
import com.lab.repository.ReportRepository;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.lab.repository.BookingRepository;
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

    // Show upload page
    @GetMapping("/admin/report/{id}")
    public String showReportPage(@PathVariable Long id, org.springframework.ui.Model model) {
        model.addAttribute("bookingId", id);
        return "upload-report";
    }

    // Save report
    @PostMapping("/admin/report")
    public String saveReport(@RequestParam Long bookingId,
                            @RequestParam String result) {

        Report r = new Report();
        r.setBookingId(bookingId);
        r.setResult(result);

        reportRepo.save(r);

        // update booking status
        var booking = bookingRepo.findById(bookingId).orElse(null);
        if (booking != null) {
            booking.setStatus("Report Ready");
            bookingRepo.save(booking);
        }

        return "redirect:/admin/bookings";
    }
    @GetMapping("/report/{bookingId}")
    public String viewReport(@PathVariable Long bookingId,
                             Model model,
                             HttpSession session) {

        // ✅ ADD HERE
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }

        Report report = reportRepo.findByBookingId(bookingId);
        model.addAttribute("report", report);

        return "view-report";
    }
    @GetMapping("/report/pdf/{bookingId}")
    public void downloadPdf(@PathVariable Long bookingId,
                            HttpServletResponse response) throws Exception {

        Report report = reportRepo.findByBookingId(bookingId);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=report.pdf");

        PdfWriter writer = new PdfWriter(response.getOutputStream());
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Blood Test Report"));
        document.add(new Paragraph("Booking ID: " + bookingId));
        document.add(new Paragraph("Result: " + report.getResult()));

        document.close();
    }
}