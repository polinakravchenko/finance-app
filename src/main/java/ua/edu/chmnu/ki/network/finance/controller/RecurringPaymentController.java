package ua.edu.chmnu.ki.network.finance.controller;

import ua.edu.chmnu.ki.network.finance.persistance.entity.RecurringPayment;
import ua.edu.chmnu.ki.network.finance.service.RecurringPaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments/recurring")
@RequiredArgsConstructor
public class RecurringPaymentController {

    private final RecurringPaymentService service;

    @GetMapping
    public ResponseEntity<Page<RecurringPayment>> getPayments(
            @RequestParam(required = false) String status,
            @PageableDefault(size = 5, sort = "id") Pageable pageable) {
        Page<RecurringPayment> payments = (status != null)
                ? service.getPaymentsByStatus(status, pageable)
                : service.getPayments(pageable);
        return ResponseEntity.ok(payments);
    }

    @PostMapping
    public ResponseEntity<RecurringPayment> createPayment(@Valid @RequestBody RecurringPayment payment) {
        RecurringPayment created = service.createPayment(payment);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelPayment(@PathVariable Long id) {
        service.cancelPayment(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        RecurringPayment payment = service.getPaymentById(id);
        if ("cancelled".equalsIgnoreCase(payment.getStatus())) {
            service.deletePayment(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}

