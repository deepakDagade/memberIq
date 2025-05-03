package com.nexoraa.memberiq.utility;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.data.util.Pair;

import com.nexoraa.memberiq.exception.MemberIqException;

public class DateUtil {
	public static Pair<LocalDate, LocalDate> parseDateRange(String value) {
		try {
			String[] dates = value.split(",");

			if (dates.length != 2) {
				throw new MemberIqException(ResponseMessages.INVALID_DATE_RANGE_FORMAT);
			}

			LocalDate startDate = LocalDate.parse(dates[GlobalConstants.ZERO].trim(),
					DateTimeFormatter.ofPattern(GlobalConstants.DATE_FORMAT));

			LocalDate endDate = LocalDate
					.parse(dates[GlobalConstants.ONE].trim(), DateTimeFormatter.ofPattern(GlobalConstants.DATE_FORMAT))
					.plusDays(GlobalConstants.ONE); // inclusive end date

			return Pair.of(startDate, endDate);

		} catch (DateTimeParseException e) {
			throw new MemberIqException(ResponseMessages.INVALID_DATE_RANGE_FORMAT);
		}
	}
}
