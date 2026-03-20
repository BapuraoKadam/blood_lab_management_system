package com.lab.repository;

import com.lab.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
	Report findByBookingId(Long bookingId);
}