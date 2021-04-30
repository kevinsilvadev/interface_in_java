package services;

import java.util.Calendar;
import java.util.Date;

import entities.Contract;
import entities.Installments;

public class ContractService {
	
	private OnlinePaymentService onlinePaymentService;
	
	public ContractService(OnlinePaymentService onlinePaymentService) {
		this.onlinePaymentService = onlinePaymentService;
	}
	
	public void ProcessContract(Contract contract, int months) {
		
		double basicQuota = contract.getTotalValue() / months;
		//basicQuota = 200
		
		for (int i = 1; i <= months; i++) {
			
			double updateQuota = basicQuota + onlinePaymentService.interest(basicQuota, i);
			//updateQuota = 202
			
			double fullQuota = updateQuota + onlinePaymentService.paymentFee(updateQuota);
			// fullQuota = 206.04
			
			Date dueDate = addMonths(contract.getDate(), i);
			
			contract.getInstallments().add(new Installments(dueDate, fullQuota));
		}
		
	}
	
	private Date addMonths(Date date, int n) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, n);
		return calendar.getTime();
	}
	
}
