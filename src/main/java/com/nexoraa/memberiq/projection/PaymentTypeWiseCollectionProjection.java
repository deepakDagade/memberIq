package com.nexoraa.memberiq.projection;

import java.math.BigDecimal;

public interface PaymentTypeWiseCollectionProjection {
	String getPaymentType();

	BigDecimal getTotalAmount();
}
