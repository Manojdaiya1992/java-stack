package com.example.JavaDummyStack.paypal;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

@RestController
public class PaypalController {

	@Autowired
	private PaypalService paypalService;
	@Value("${paypal.success.url}")
	private String successUrl;
	@Value("${paypal.cancel.url}")
	private String cancelUrl;

	@GetMapping("/paypal")
	public ResponseEntity<Map<String, Object>> paypal() {
		Map<String, Object> response = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		try {
			Payment payment = paypalService.createPayment(4.00, "USD", "PAYPAL", "sale", "payment description",
					cancelUrl, successUrl);
			if (Objects.nonNull(payment)) {
				System.out.println("Payment object " + payment);
				for (Links links : payment.getLinks()) {
					if (links.getRel().equals("approval_url")) {
						data.put("redirect", links.getHref());
					}
				}
				response.put("data", data);
				response.put("message", "Redirect to given url");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.put("message", "Invalid Payment");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
		} catch (PayPalRESTException e) {
			e.printStackTrace();
			response.put("message", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("message", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/success")
	public void getSuccess(@RequestParam Map<String, Object> keys) {
		System.out.println("success ===> " + keys);
		String paymentId = (String) keys.get("paymentId");
		String payerID = (String) keys.get("PayerID");
		try {
			Payment payment = paypalService.executePayment(paymentId, payerID);
			if (payment.getState().equals("approved")) {
				System.out.println("Payment Succesfully");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@GetMapping("/cancel")
	public void getcancel(@RequestParam Map<String, Object> keys) {
		System.out.println("cancel ===> " + keys);
	}
}
