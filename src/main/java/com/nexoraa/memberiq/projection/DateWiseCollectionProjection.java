package com.nexoraa.memberiq.projection;

import java.time.LocalDate;

public interface DateWiseCollectionProjection {
	LocalDate getDate();
    Double getTotalCollection();
}
