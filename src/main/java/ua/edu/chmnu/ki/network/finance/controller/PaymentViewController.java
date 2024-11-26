package ua.edu.chmnu.ki.network.finance.controller;

import ua.edu.chmnu.ki.network.finance.persistance.entity.RecurringPayment;
import ua.edu.chmnu.ki.network.finance.service.RecurringPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ui/payments")
@RequiredArgsConstructor
public class PaymentViewController {

    private final RecurringPaymentService service;

    @GetMapping
    public String listPayments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "all") String status,
            Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RecurringPayment> payments;

        if ("all".equalsIgnoreCase(status)) {
            payments = service.getPayments(pageable);
        } else {
            payments = service.getPaymentsByStatus(status, pageable);
        }

        model.addAttribute("payments", payments.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", payments.getTotalPages());
        model.addAttribute("totalItems", payments.getTotalElements());
        model.addAttribute("status", status);

        return "payments";
    }

    @PostMapping
    public String createPayment(@ModelAttribute RecurringPayment payment) {
        service.createPayment(payment);
        return "redirect:/ui/payments";
    }

    @PostMapping("/delete/{id}")
    public String deletePayment(@PathVariable Long id) {
        RecurringPayment payment = service.getPaymentById(id);
        if ("cancelled".equalsIgnoreCase(payment.getStatus())) {
            service.deletePayment(id);
        } else {
            service.cancelPayment(id);
        }
        return "redirect:/ui/payments";
    }
}


