package ua.edu.chmnu.ki.network.finance.service;

import ua.edu.chmnu.ki.network.finance.exception.PaymentNotFoundException;
import ua.edu.chmnu.ki.network.finance.persistance.entity.RecurringPayment;
import ua.edu.chmnu.ki.network.finance.persistance.repository.RecurringPaymentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RecurringPaymentService {

    private final RecurringPaymentRepository repository;

    @Transactional
    public RecurringPayment createPayment(RecurringPayment payment) {
        payment.setStatus("active");
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());
        return repository.save(payment);
    }

    @Transactional
    public void cancelPayment(Long id) {
        RecurringPayment payment = repository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Recurring Payment with id " + id + " not found"));
        payment.setStatus("cancelled");
        payment.setUpdatedAt(LocalDateTime.now());
    }

    @Transactional
    public void deletePayment(Long id) {
        if (!repository.existsById(id)) {
            throw new PaymentNotFoundException("Recurring Payment with id " + id + " not found");
        }
        repository.deleteById(id);
    }

    @Transactional
    public RecurringPayment getPaymentById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Recurring Payment with id " + id + " not found"));
    }

    public Page<RecurringPayment> getPayments(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<RecurringPayment> getPaymentsByStatus(String status, Pageable pageable) {
        return repository.findByStatus(status, pageable);
    }
}
