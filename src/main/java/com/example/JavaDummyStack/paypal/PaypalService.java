package com.example.JavaDummyStack.paypal;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

@Service
public class PaypalService {

	@Autowired
	private APIContext apiContext;

	/**
	 * create payemnt
	 *
	 * @param total
	 * @param currency
	 * @param method
	 * @param intent
	 * @param description
	 * @param cancelUrl
	 * @param successUrl
	 * @return
	 * @throws PayPalRESTException
	 */
	public Payment createPayment(Double total, String currency, String method, String intent, String description,
			String cancelUrl, String successUrl) throws PayPalRESTException {
		// create amount
		Amount amount = getAmount(total, currency);
		// create transactions
		List<Transaction> transactions = getTransactions(amount, description);
		// create payer
		Payer payer = getPayer(method);

		// create payment
		Payment payment = new Payment();
		payment.setIntent(intent.toString());
		payment.setPayer(payer);
		payment.setTransactions(transactions);
		RedirectUrls redirectUrls = getRedirectUrls(cancelUrl, successUrl);
		payment.setRedirectUrls(redirectUrls);

		return payment.create(apiContext);
	}

	/**
	 * execute payment
	 *
	 * @param paymentId
	 * @param payerId
	 * @return
	 * @throws PayPalRESTException
	 */
	public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
		Payment payment = new Payment();
		payment.setId(paymentId);
		PaymentExecution paymentExecute = new PaymentExecution();
		paymentExecute.setPayerId(payerId);
		return payment.execute(apiContext, paymentExecute);
	}

	/**
	 * Redirect Urls
	 *
	 * @param cancelUrl
	 * @param successUrl
	 * @return
	 */
	private RedirectUrls getRedirectUrls(String cancelUrl, String successUrl) {
		RedirectUrls redirectUrls = new RedirectUrls();
		redirectUrls.setCancelUrl(cancelUrl);
		redirectUrls.setReturnUrl(successUrl);
		return redirectUrls;
	}

	/**
	 * create payer
	 *
	 * @param method
	 * @return
	 */
	private Payer getPayer(String method) {
		Payer payer = new Payer();
		payer.setPaymentMethod(method.toString());
		return payer;
	}

	/**
	 * transations
	 *
	 * @param amount
	 * @param description
	 * @return
	 */
	private List<Transaction> getTransactions(Amount amount, String description) {
		// create transaction
		Transaction transaction = new Transaction();
		transaction.setDescription(description);
		// set amount
		transaction.setAmount(amount);

		List<Transaction> transactions = Arrays.asList(transaction);
		return transactions;
	}

	/**
	 * create amount
	 *
	 * @param total
	 * @param currency
	 * @return
	 */
	private Amount getAmount(Double total, String currency) {
		Amount amount = new Amount();
		amount.setCurrency(currency);
		total = new BigDecimal(total).setScale(2, RoundingMode.HALF_UP).doubleValue();
		amount.setTotal(String.format("%.2f", total));
		return amount;
	}
}
