package ua.edu.chmnu.ki.network.finance.persistance.repository;

import ua.edu.chmnu.ki.network.finance.persistance.entity.RecurringPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Repository
public interface RecurringPaymentRepository extends JpaRepository<RecurringPayment, Long> {
    Page<RecurringPayment> findByStatus(String status, Pageable pageable);
}
