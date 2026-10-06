package in.gracy.removebg.controller;

import com.razorpay.Order;
import com.razorpay.RazorpayException;
import in.gracy.removebg.dto.RazorpayOrderDTO;
import in.gracy.removebg.response.RemoveBgResponse;
import in.gracy.removebg.service.OrderService;
import in.gracy.removebg.service.RazorpayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final RazorpayService razorpayService;

    @PostMapping
    public ResponseEntity<?> createOrder(
            @RequestParam String planId,
            Authentication authentication
    ) throws RazorpayException {

        try {

            if (authentication == null || authentication.getName() == null ||
                    authentication.getName().isEmpty()) {

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                        RemoveBgResponse.builder()
                                .success(false)
                                .message("User not authorized")
                                .statusCode(HttpStatus.FORBIDDEN)
                                .build()
                );
            }

            Order order = orderService.createOrder(planId, authentication.getName());

            RazorpayOrderDTO orderDTO = convertToDTO(order);

            return ResponseEntity.ok(
                    RemoveBgResponse.builder()
                            .success(true)
                            .data(orderDTO)
                            .statusCode(HttpStatus.OK)
                            .build()
            );

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    RemoveBgResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                            .build()
            );

        }

    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyOrder(@RequestBody Map<String, Object> request) {

        try {

            String razorpayOrderId = request.get("razorpay_order_id").toString();

            Map<String, Object> result = razorpayService.verifyPayment(razorpayOrderId);

            return ResponseEntity.ok(result);

        } catch (Exception e) {

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);

        }
    }

    private RazorpayOrderDTO convertToDTO(Order order) {

        return RazorpayOrderDTO.builder()
                .id(order.get("id"))
                .entity(order.get("entity"))
                .amount(order.get("amount"))
                .currency(order.get("currency"))
                .status(order.get("status"))
                .receipt(order.get("receipt"))
                .build();

    }
}